package com.mijack.sootdemo;

import soot.*;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;

/**
 * @author Mr.Yuan
 * @since 2016/12/23.
 */
public class Utils {


    public static boolean isInvokeInstrumentation(Unit unit) {
        return unit instanceof JInvokeStmt
                && isInvokeInstrumentation(((JInvokeStmt) unit));
    }

    public static boolean isInvokeMethod(Unit unit, SootMethod method) {
        return unit instanceof JInvokeStmt
                && isSameMethod(((JInvokeStmt) unit), method);
    }

    public static boolean isInvokeInstrumentation(JInvokeStmt invokeStmt) {
        SootMethod instrumentationMethod = getInstrumentationMethod();
        return isSameMethod(invokeStmt, instrumentationMethod);
    }

    public static boolean isSameMethod(JInvokeStmt invokeStmt, SootMethod sootMethod) {
        Value exprValue = invokeStmt.getInvokeExprBox().getValue();
        if (exprValue instanceof JStaticInvokeExpr ) {
            JStaticInvokeExpr staticInvokeExpr = (JStaticInvokeExpr) exprValue;
            return staticInvokeExpr.getMethod().equals(sootMethod);
        } else if (exprValue instanceof JVirtualInvokeExpr){
            JVirtualInvokeExpr virtualInvokeExpr = (JVirtualInvokeExpr) exprValue;
            return virtualInvokeExpr.getMethod().equals(sootMethod);
        }else {
            throw new IllegalArgumentException(exprValue.getClass().getName() + " isn't supported!");
        }
    }

    public static SootMethod getInstrumentationMethod() {
        SootClass instrumentationHelper = Scene.v().getSootClass("com.mijack.faultlocationdemo.InstrumentationHelper");
        return instrumentationHelper.getMethodByName("instrumentation");
    }

}

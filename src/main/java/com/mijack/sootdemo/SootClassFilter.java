package com.mijack.sootdemo;

import rx.functions.Func1;
import soot.SootClass;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class SootClassFilter implements Func1<SootClass, Boolean> {
    @Override
    public Boolean call(SootClass sootClass) {
        return !sootClass.getPackageName().startsWith("com.mijack.faultlocationdemo");
    }
}

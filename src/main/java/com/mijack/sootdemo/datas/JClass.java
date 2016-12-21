package com.mijack.sootdemo.datas;

import soot.SootClass;
import soot.SootMethod;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class JClass {

    private Map<String,JMethod> methodMap;
    private SootClass clazz;

    public JClass(SootClass clazz) {
        this.clazz = clazz;
        methodMap=new LinkedHashMap<>();
        for (SootMethod method : clazz.getMethods()) {
            methodMap.put(method.getSignature(),new JMethod(method));
        }

    }

    public String getClassName() {
        return clazz.getName();
    }
}

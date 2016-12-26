package com.mijack.sootdemo;

import com.mijack.sootdemo.core.JClass;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;

import java.util.Collections;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class MyMain {
    public static final String ANDROID_JAR = "F:\\FaultLocation\\SootAndroid\\apk";
    private static final String JIMPLE_FILES = "F:\\FaultLocation\\SootAndroid\\sootOutput";

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            System.out.println("---");
            e.printStackTrace();
        }
    }

    public static void run() {
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_validate(true);
        Options.v().set_whole_program(true);
        Options.v().set_force_overwrite(true);
        Options.v().set_process_dir(Collections.singletonList(JIMPLE_FILES));
        Options.v().set_android_jars(ANDROID_JAR);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_src_prec(Options.src_prec_apk_class_jimple);
        Options.v().set_soot_classpath(ANDROID_JAR);

        Options.v().set_validate(false);
        Scene.v().loadNecessaryClasses();

        PackManager.v().runBodyPacks();
        SootClass sootClass = Scene.v().getSootClass("com.mijack.faultlocationdemo.InstrumentationActivity");

        JClass clazz = new JClass(sootClass);
////      ok
//        JMethod log = new JMethod(sootClass.getMethodByName("log"));
////      ok
//        JMethod fun1 = new JMethod(sootClass.getMethodByName("fun1"));
////      ok
//        JMethod fun2 = new JMethod(sootClass.getMethodByName("fun2"));
////      ok
//        JMethod fun3 = new JMethod(sootClass.getMethodByName("fun3"));
////      暂不考虑
////        JMethod fun4 = new JMethod(sootClass.getMethodByName("fun4"));
////      暂不考虑
////        JMethod fun5 = new JMethod(sootClass.getMethodByName("fun5"));
////      ok
//        JMethod fun6 = new JMethod(sootClass.getMethodByName("fun6"));
////      ok
//        JMethod fun7 = new JMethod(sootClass.getMethodByName("fun7"));
////      ok
//        JMethod fun8 = new JMethod(sootClass.getMethodByName("fun8"));
////      ok
//        JMethod fun9 = new JMethod(sootClass.getMethodByName("fun9"));
        System.out.println("");
    }
}

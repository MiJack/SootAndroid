package com.mijack.sootdemo;

import com.mijack.sootdemo.datas.JMethod;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
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
        SootMethod method = sootClass.getMethodByName("fun1");
        JMethod fun1 = new JMethod(method);
        System.out.println(fun1);
//        System.out.println(log);
//        Chain<SootClass> classes = Scene.v().getApplicationClasses();
//        Apk apk = new Apk();
//        apk.setScene(Scene.v());
//        for (SootClass clazz : classes) {
//           apk.addClass(new JClass(clazz));
//        }
//        System.out.println(apk.getClasses().size());
    }
}

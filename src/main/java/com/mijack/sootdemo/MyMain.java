package com.mijack.sootdemo;

import com.mijack.sootdemo.datas.JMethod;
import soot.*;
import soot.options.Options;

import java.util.Collections;
import java.util.List;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class MyMain {
    public static final String ANDROID_JAR = "F:\\FaultLocation\\SootAndroid\\apk";
    private static final String JIMPLE_FILES = "F:\\FaultLocation\\SootAndroid\\sootOutput";

    public static void main(String[] args) {
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
        SootClass sootClass = Scene.v().getSootClass("com.mijack.faultlocationdemo.InstrumentationNoEndActivity");
        Type objectType = Scene.v().getType(Object.class.getName());
        List<Type> types = Collections.singletonList(objectType);
        SootMethod method = sootClass.getMethod("log", types);
        JMethod log = new JMethod(method);
        System.out.println(log);
//        Chain<SootClass> classes = Scene.v().getApplicationClasses();
//        Apk apk = new Apk();
//        apk.setScene(Scene.v());
//        for (SootClass clazz : classes) {
//           apk.addClass(new JClass(clazz));
//        }
//        System.out.println(apk.getClasses().size());
    }
}

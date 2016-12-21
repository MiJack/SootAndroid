package com.mijack.sootdemo;

import soot.*;
import soot.jimple.Jimple;
import soot.jimple.toolkits.scalar.ConditionalBranchFolder;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.options.Options;
import soot.toolkits.scalar.UnusedLocalEliminator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {
    public static final String APK = "E:\\AndroidStudioProjects\\FaultLocationProject\\app\\build\\outputs\\apk\\app-debug.apk";
    public static final String ANDROID_JAR = "E:\\代码\\SootAndroid\\apk";

    public static void main(String[] args) {
        init();

//        PackManager.v().runPacks();
//        List<String> classesUnder = SourceLocator.v().getClassesUnder("E:\\AndroidStudioProjects\\FaultLocationProject\\app\\build\\outputs\\apk\\app-debug.apk");
        PackManager.v().runPacks();
//        PackManager.v().writeOutput();

        SootClass sootClass = Scene.v().loadClassAndSupport("com.mijack.faultlocationdemo.InstrumentationNoEndActivity");
        Type tVoid =Scene.v().getType("void");
        Type View = Scene.v().getType("android.view.View");
        SootMethod method = sootClass.getMethod("fun1",Collections.singletonList(View),tVoid);

        Body body2 = method.retrieveActiveBody();
//        CopyPropagator.v().transform(body2);
//        ConditionalBranchFolder.v().transform(body2);
//        UnreachableCodeEliminator.v().transform(body2);
//        DeadAssignmentEliminator.v().transform(body2);
//        UnusedLocalEliminator.v().transform(body2);
//        PackManager.v().getPack("jtp").apply(body2);
//        if(Options.v().validate()) {
//            body2.validate();
//        }
//        PackManager.v().getPack("jop").apply(body2);
//        PackManager. v().getPack("jap").apply(body2);
        System.out.println(body2);



    }

    private static void init() {
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_validate(true);
        Options.v().set_whole_program(true);
        Options.v().set_force_overwrite(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_process_dir(Collections.singletonList(APK));
        Options.v().set_android_jars(ANDROID_JAR);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_soot_classpath(ANDROID_JAR);

        Scene.v().loadNecessaryClasses();
    }
}

package com.mijack.sootdemo;

import soot.*;
import soot.options.Options;

import java.util.Collections;

public class GetJimpleFileMain {
    public static final String APK = "E:\\AndroidStudioProjects\\FaultLocationProject\\app\\build\\outputs\\apk\\app-debug.apk";
    public static final String ANDROID_JAR = "E:\\代码\\SootAndroid\\apk";

    public static void main(String[] args) {
        init();
        PackManager.v().runPacks();
        PackManager.v().writeOutput();
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

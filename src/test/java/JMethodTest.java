import com.mijack.sootdemo.datas.JMethod;
import org.junit.Before;
import org.junit.Test;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

import java.util.Collections;

/**
 * @author Mr.Yuan
 * @since 2016/12/23.
 */

public class JMethodTest {

    public static final String ANDROID_JAR = "F:\\FaultLocation\\SootAndroid\\apk";
    private static final String JIMPLE_FILES = "F:\\FaultLocation\\SootAndroid\\sootOutput";

    @Before
    public void setup() {
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
    }

    @Test
    public void testFun1() {
        Scene.v().loadNecessaryClasses();
        PackManager.v().runBodyPacks();
        SootClass sootClass = Scene.v().getSootClass("com.mijack.faultlocationdemo.InstrumentationActivity");
        SootMethod method = sootClass.getMethodByName("fun1");
        JMethod fun1 = new JMethod(method);
        System.out.println(fun1);
    }
}

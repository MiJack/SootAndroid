package com.mijack.sootdemo.datas;

import com.mijack.sootdemo.datas.JBlock;
import soot.Body;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class JMethod {
    final private SootMethod rawMethod;
    JBlock head;
    public JMethod(SootMethod rawMethod) {
        this.rawMethod = rawMethod;
        Body body = rawMethod.getActiveBody();
        PatchingChain<Unit> units = body.getUnits();
        for (int i = 0; i< units.size(); i++){

        }
    }
}

package com.mijack.sootdemo.datas;

import soot.Body;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.internal.*;

import java.util.Iterator;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class JMethod {
    private static final int STATUS_UNKNOWN = 1;
    private static final int STATUS_BIASC = 2;
    private static final int STATUS_RETURN = 3;
    final private SootMethod rawMethod;
    JBlock head;

    public JMethod(SootMethod rawMethod) {
        this.rawMethod = rawMethod;
        Body body = rawMethod.getActiveBody();
        PatchingChain<Unit> units = body.getUnits();
        JBlock correct = null;
        Iterator<Unit> iterator = units.iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            int status = getUnitStatus(unit);
//            判断语句状态
            if (status == STATUS_UNKNOWN) {
                throw new IllegalStateException(String.format("stmt[%s] is unknown", unit.toString()));
            }
            switch (status) {
                case STATUS_BIASC:
                    //常规语句
                    if (correct == null) {
                        correct = new JBlock();
                    }
                    correct.addUnit(unit);
                    break;
                case STATUS_RETURN:
                    //常规语句
                    if (correct == null) {
                        correct = new JBlock(BlockType.RETURN_BLOCK);
                    }
                    if (correct.isType(BlockType.BASIC_BLOCK)) {
                        correct.transform(BlockType.RETURN_BLOCK);
                    }
                    break;
            }
            //确保corrent不为空
            if (head == null) {
                head = correct;
            }
        }
    }
    private int getUnitStatus(Unit unit) {
        if (unit instanceof JReturnStmt || unit instanceof JReturnVoidStmt) {
            return STATUS_RETURN;
        } else if (unit instanceof JAssignStmt
                || unit instanceof JNopStmt
                || unit instanceof JIdentityStmt) {
            return STATUS_BIASC;
        } else {
            throw new IllegalStateException("Unit[" + unit.getClass().getName() + "] is not surpported ");
//            return STATUS_RETURN;
        }
    }
}

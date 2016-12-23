package com.mijack.sootdemo.datas;

import com.mijack.sootdemo.Status;
import com.mijack.sootdemo.Utils;
import com.mijack.sootdemo.block.BlockType;
import com.mijack.sootdemo.block.JBasicBlock;
import com.mijack.sootdemo.block.JBlock;
import com.mijack.sootdemo.block.JIfElseBlock;
import soot.*;
import soot.jimple.internal.*;
import soot.util.Chain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mijack.sootdemo.Status.*;
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
        JBlock cur = null;
        Status preStatus = STATUS_UNKNOWN;
//        输出body部分
        System.out.println("------------------------------------------------");
        Iterator<Unit> iterator = units.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("------------------------------------------------");
        iterator = units.iterator();
        int j = -1;
        while (iterator.hasNext()) {
            j++;
            Unit curUnit = iterator.next();
            Status status = getUnitStatus(curUnit, preStatus, cur);
            System.out.println(String.format("[%-20s]%s",status,curUnit));
//            System.out.println("unit:" + cur + ",preStatus:" + preStatus);

//            判断语句状态
            if (status == STATUS_UNKNOWN) {
                throw new IllegalStateException(String.format("stmt[%s] is unknown", curUnit.toString()));
            }
            switch (status) {
                case STATUS_BASIC:
                    //常规语句
                    JBasicBlock temp;
                    if (cur == null) {
                        temp = new JBasicBlock();
                    } else if (cur instanceof JBasicBlock) {
                        temp = (JBasicBlock) cur;
                    } else {
                        temp = new JBasicBlock();
                        cur.addNext(temp);
                    }
                    temp.addLast(curUnit);
                    cur = temp;
                    break;
                case STATUS_RETURN:
                    //常规语句
                    if (cur == null) {
                        cur = new JBasicBlock(BlockType.RETURN_BLOCK);
                    }
                    cur.transform(BlockType.RETURN_BLOCK);
                    cur.addLast(curUnit);
                    break;
                case STATUS_IF_CONDITION:
                    JIfElseBlock ifBlock = null;
                    if (cur instanceof JIfElseBlock) {
                        ifBlock = (JIfElseBlock) cur;
                    } else {
                        ifBlock = new JIfElseBlock();
                        if (cur != null) {
                            cur.addNext(ifBlock);
                        }
                    }
                    cur = ifBlock;
                    ifBlock.addCondition(curUnit);
                    break;
                case STATUS_IF_THEN:
//                    ifBlock = (JIfElseBlock) cur;
//                    ifBlock.addThenNext(curUnit);
//                    break;
                case STATUS_IF_THEN_END:
                    ifBlock= (JIfElseBlock) cur;
                    ifBlock.addThenNext(curUnit);
                    break;
                case STATUS_IF_ELSE_START:
                    ifBlock = (JIfElseBlock) cur;
                    ifBlock.addElseNext(curUnit);
                    break;
                case STATUS_IF_END:
//                    保存if结构，创建basic block
//                    if (preStatus ==STATUS_IF_THEN_END){
//
//                    }else {
//                        JBasicBlock basicBlock = new JBasicBlock();
//                        basicBlock.addLast(curUnit);
//                        cur.addNext(basicBlock);
//                        cur = basicBlock;
//                    }
            }
            //确保corrent不为空
            if (head == null) {
                head = cur;
            }
            preStatus = status;
        }
        System.out.println("");
    }

    private Status getUnitStatus(Unit unit, Status preStatus, JBlock cur) {
        if (preStatus == STATUS_IF_THEN) {
            //终止条件为
            // 1、遇到invoke插桩指令&&上一句中包含IF_THEN_END
            // 2、当前语句为return语句
            if (Utils.isInvokeInstrumentation(unit) &&
                    returnTypeIfHas(((JIfElseBlock) cur).getThenLastUnit().getUseBoxes())
                    .equals(STATUS_IF_THEN_END)) {
                return STATUS_IF_THEN_END;
            }
            if (unit instanceof JReturnStmt || unit instanceof JReturnVoidStmt) {
                return STATUS_IF_THEN_END;
            }
            return STATUS_IF_THEN;
        }
        if (preStatus == STATUS_IF_CONDITION) {
            if (unit instanceof JAssignStmt) {
                Status type = returnTypeIfHas(((JAssignStmt) unit).getUseBoxes());
                return type.value() > 0 ? type : STATUS_IF_CONDITION;
            }
            return STATUS_IF_CONDITION;
        }
        if (unit instanceof JNopStmt) {
            return STATUS_BASIC;
        } else if (unit instanceof JIdentityStmt) {
            return STATUS_BASIC;
        } else if (unit instanceof JAssignStmt) {
            //判断是否为包含对应的静态字段
            Status type = returnTypeIfHas(((JAssignStmt) unit).getUseBoxes());
            return type.value() >= 0 ? type : STATUS_BASIC;
        } else if (unit instanceof JReturnStmt || unit instanceof JReturnVoidStmt) {
            return STATUS_RETURN;
        } else if (unit instanceof JAssignStmt
                || unit instanceof JNopStmt
                || unit instanceof JIdentityStmt
                || unit instanceof JInvokeStmt) {
            return STATUS_BASIC;
        }
        if (unit instanceof JIfStmt) {
            return STATUS_IF_CONDITION;
        } else {
            throw new IllegalStateException("Unit[" + unit.getClass().getName() + "] is not surpported ");
//            return STATUS_RETURN;
        }
    }

    private Status returnTypeIfHas(List<ValueBox> useBoxes) {
//        List<ValueBox> useBoxes = unit.getUseBoxes();
        if (useBoxes.size() == 1) {
            ValueBox valueBox = useBoxes.get(0);
            if (!valueBox.getValue().getType().equals(type)) {
                return STATUS_UNKNOWN;
            }
            String value = valueBox.toString();
            if (value.contains("IF_START")) {
                return STATUS_IF_CONDITION;
            } else if (value.contains("IF_END")) {
                return STATUS_IF_END;
            } else if (value.contains("IF_THEN_START")) {
                return STATUS_IF_THEN;
            } else if (value.contains("IF_THEN_END")) {
                return STATUS_IF_THEN_END;
            } else if (value.contains("IF_ELSE_START")) {
                return STATUS_IF_ELSE_START;
            } else if (value.contains("IF_ELSE_END")) {
                return STATUS_IF_ELSE_END;
            } else if (value.contains("RETURN")) {
                return STATUS_RETURN;
                //no used
            }
        }
        return STATUS_UNKNOWN;
    }

    static Type type = Scene.v().getType("com.mijack.faultlocationdemo.Type");
    static List<SootField> list = new ArrayList<>();

    static {
        SootClass sootClass = Scene.v().getSootClass("com.mijack.faultlocationdemo.Type");
        Chain<SootField> fields = sootClass.getFields();
        for (SootField field : fields) {
            if (field.getType().equals(type)) {
                list.add(field);
            }
        }
    }
}

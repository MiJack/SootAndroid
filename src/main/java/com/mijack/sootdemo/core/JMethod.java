package com.mijack.sootdemo.core;

import com.mijack.faultlocationdemo.InstrumentationType;
import com.mijack.sootdemo.block.*;
import soot.Body;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.internal.*;

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class JMethod {
    final private SootMethod rawMethod;
    JBlock head = null;
    public JMethod(SootMethod rawMethod) {
        this.rawMethod = rawMethod;
        Body body = rawMethod.getActiveBody();
        PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> iterator = units.iterator();
//        System.out.println("---------------------------------------------");
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }
//        System.out.println("---------------------------------------------");
//        iterator = units.iterator();
        int i = 0;
        JBlock currentBlock = null;
        while (iterator.hasNext()) {
            Unit currentUnit = iterator.next();
            i++;
            UnitType unitType = getUnitType(currentUnit, currentBlock);
            log(i, currentUnit, currentBlock);
            if (unitType.equals(UnitType.BASIC)) {
                JBasicBlock tempBlock ;
                if (currentBlock instanceof JBasicBlock) {
                    tempBlock = (JBasicBlock) currentBlock;
                } else {
                    tempBlock = new JBasicBlock();
                    if (currentBlock != null) {
                        currentBlock.addNext(tempBlock);
                    }
                }
                tempBlock.addLast(new JStmtBlock(currentUnit));
                currentBlock = tempBlock;
            } else if (unitType.equals(UnitType.INSTRUMENTATION)) {
                Unit next = iterator.next();
                i++;
                JInstrumentationBlock instrumentationBlock = new JInstrumentationBlock(currentUnit, next);
                //根据JInstrumentationBlock中的type选定插入点
                if (instrumentationBlock.getType().equals(InstrumentationType.IF_ELSE_START)) {
                    //找到对应的id的模块
                    String id = instrumentationBlock.getId();
                    currentBlock = getBlock(JIfElseBlock.class, id);
                }
                if (currentBlock instanceof JBasicBlock) {
                    currentBlock.addNext(instrumentationBlock);
                    currentBlock = instrumentationBlock;
                } else if (currentBlock instanceof JIfElseBlock) {
                    //判断instrumentationBlock的类型
                    JIfElseBlock ifElseBlock = ((JIfElseBlock) currentBlock);
                    if (instrumentationBlock.getType().equals(InstrumentationType.IF_THEN_START)
                            || instrumentationBlock.getType().equals(InstrumentationType.IF_THEN_END)) {
                        ifElseBlock.addThenNext(instrumentationBlock);
                    } else if (instrumentationBlock.getType().equals(InstrumentationType.IF_ELSE_START)
                            || instrumentationBlock.getType().equals(InstrumentationType.IF_ELSE_END)) {
                        ifElseBlock.addElseNext(instrumentationBlock);
                    } else if (instrumentationBlock.getType().equals(InstrumentationType.IF_END)) {
                        //更新current block
                        ifElseBlock.addNext(instrumentationBlock);
                        currentBlock = instrumentationBlock;
                    }
                }
                log(i, next, currentBlock);
            } else if (unitType.equals(UnitType.IF_CONDITION)) {
                JIfElseBlock ifElseBlock = null;
                if (!(currentBlock instanceof JIfElseBlock)) {
                    //设置对应的id号
                    ifElseBlock = new JIfElseBlock();
                    JInstrumentationBlock block = (JInstrumentationBlock) currentBlock;
                    ifElseBlock.setId(block.getId());
                    currentBlock.addNext(ifElseBlock);
                } else {
                    ifElseBlock = (JIfElseBlock) currentBlock;
                }
                ifElseBlock.addCondition(new JStmtBlock(currentUnit));
                currentBlock = ifElseBlock;
            } else if (unitType.equals(UnitType.IF_THEN)) {
                JIfElseBlock ifElseBlock = (JIfElseBlock) currentBlock;
                ifElseBlock.addThenNext(new JStmtBlock(currentUnit));
            } else if (unitType.equals(UnitType.RETURN)) {
                if (currentBlock instanceof JBasicBlock
                        && !(currentBlock instanceof JInstrumentationBlock)) {
                    JBasicBlock basicBlock = (JBasicBlock) currentBlock;
                    basicBlock.addLast(new JReturnBlock(currentUnit));
                } else if (currentBlock instanceof JIfElseBlock) {
                    JIfElseBlock ifElseBlock = (JIfElseBlock) currentBlock;
                    if (!ifElseBlock.isThenEnd()) {
                        ifElseBlock.addThenNext(new JReturnBlock(currentUnit));
                    } else if (!ifElseBlock.isElseEnd()) {
                        ifElseBlock.addElseNext(new JReturnBlock(currentUnit));
                    }
                } else {
                    JBasicBlock basicBlock = new JBasicBlock();
                    basicBlock.addLast(new JReturnBlock(currentUnit));
                    currentBlock.addNext(basicBlock);
                    currentBlock = basicBlock;
                }
            } else if (unitType.equals(UnitType.IF_ELSE)) {
                JIfElseBlock jIfElseBlock = (JIfElseBlock) currentBlock;
                jIfElseBlock.addElseNext(new JStmtBlock(currentUnit));
            }
            if (head == null) {
                head = currentBlock;
            }
        }
        System.out.println("");
    }

    /**
     * 查找符合某个类型的id为@param{id}的JBlock
     *
     * @param clazz
     * @param id
     * @return
     */
    private JBlock getBlock(Class<?> clazz, String id) {
        JBlock result =  null;
        JBlock current = head;
        while (current != null) {
            result = current.getBlock(clazz, id);
            current = current.nextBlock();
            if (result != null) {
                return result;
            }
        }
        return result;
    }

    public static final Pattern PATTERN_ASSIGN = Pattern.compile("^([\\$|\\w]+) = " +
            "<com.mijack.faultlocationdemo.Type: com.mijack.faultlocationdemo.Type (\\w+)>$");
    public static final Pattern PATTERN_INVOKE = Pattern.compile("^staticinvoke " +
            "<com.mijack.faultlocationdemo.InstrumentationHelper: int instrumentation" +
            "\\(com.mijack.faultlocationdemo.Type,int\\)>\\(([\\$|\\w]+), (\\w+)\\)$");

    private void log(int i, Unit unit, JBlock currentBlock) {
        UnitType unitType = getUnitType(unit, currentBlock);
        String blockName = currentBlock == null ? "" : currentBlock.getClass().getSimpleName();
        System.out.println(String.format("%2d\t%-20s --%-20s %30s\t%s",
                i, unitType, unit.getClass().getSimpleName(), blockName,
                unit.toString()));
    }

    public static UnitType getUnitType(Unit unit, JBlock currentBlock) {
        String unitString = unit.toString();
        if (unit instanceof JIdentityStmt) {
            return UnitType.BASIC;
        }
        if (unit instanceof JReturnStmt || unit instanceof JReturnVoidStmt) {
            return UnitType.RETURN;
        }
        if (PATTERN_ASSIGN.matcher(unitString).matches() ||
                PATTERN_INVOKE.matcher(unitString).matches()) {
            return UnitType.INSTRUMENTATION;
        }
        if (currentBlock instanceof JInstrumentationBlock) {
            JInstrumentationBlock jib = (JInstrumentationBlock) currentBlock;
            InstrumentationType type = jib.getType();
            if (type.equals(InstrumentationType.IF_START)) {
                return UnitType.IF_CONDITION;
            } else if (type.equals(InstrumentationType.IF_THEN_START)
                    || type.equals(InstrumentationType.IF_THEN_END)) {
                return UnitType.IF_THEN;
            } else if (type.equals(InstrumentationType.IF_ELSE_START)
                    || type.equals(InstrumentationType.IF_THEN_END)) {
                return UnitType.IF_ELSE;
            } else if (type.equals(InstrumentationType.IF_END)) {
                return UnitType.BASIC;
            }
        }
        if (currentBlock instanceof JIfElseBlock) {
            if (unit instanceof JIfStmt) {
                return UnitType.IF_CONDITION;
            } else {
                // 判断then分支有没有结束
                JIfElseBlock ifElseBlock = (JIfElseBlock) currentBlock;
                if (!ifElseBlock.isThenStart()) {
                    return UnitType.IF_CONDITION;
                } else if (ifElseBlock.isThenStart() && !ifElseBlock.isThenEnd()) {
                    return UnitType.IF_THEN;
                } else if (ifElseBlock.isElseStart() && !ifElseBlock.isElseEnd()) {
                    return UnitType.IF_ELSE;
                } else if (unit instanceof JGotoStmt) {
                    return UnitType.GOTO;
                } else {
                    throw new RuntimeException("unknown");
                }
            }
        }
        if (unit instanceof JInvokeStmt) {
            if (currentBlock instanceof JBasicBlock) {
                return UnitType.BASIC;
            }
        }
        if (unit instanceof JAssignStmt) {
            return UnitType.BASIC;
        }
        throw new IllegalStateException(unit + " isn't supported!");
    }

}

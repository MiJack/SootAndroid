package com.mijack.sootdemo.datas;

import com.mijack.faultlocationdemo.InstrumentationType;
import com.mijack.sootdemo.block.JBasicBlock;
import com.mijack.sootdemo.block.JBlock;
import com.mijack.sootdemo.block.JIfElseBlock;
import com.mijack.sootdemo.block.JStmtBlock;
import soot.Body;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JReturnStmt;
import soot.jimple.internal.JReturnVoidStmt;

import java.util.Iterator;
import java.util.regex.Pattern;

import static com.mijack.sootdemo.datas.UnitType.*;

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
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("---------------------------------------------");
        iterator = units.iterator();
        int i = 0;
        JBlock currentBlock = null;
        while (iterator.hasNext()) {
            Unit currentUnit = iterator.next();
            i++;
            UnitType unitType = getUnitType(currentUnit, currentBlock);
            log(i, currentUnit, currentBlock);
            if (unitType.equals(BASIC)) {
                JBasicBlock tempBlock = currentBlock instanceof JBasicBlock ? (JBasicBlock) currentBlock : new JBasicBlock();
                tempBlock.addLast(new JStmtBlock(currentUnit));
                currentBlock = tempBlock;
            } else if (unitType.equals(INSTRUMENTATION)) {
                Unit next = iterator.next();
                i++;
                JInstrumentationBlock instrumentationBlock = new JInstrumentationBlock(currentUnit, next);
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
            } else if (unitType.equals(IF_CONDITION)) {
                JIfElseBlock ifElseBlock = null;
                if (!(currentBlock instanceof JIfElseBlock)) {
                    ifElseBlock = new JIfElseBlock();
                    currentBlock.addNext(ifElseBlock);
                } else {
                    ifElseBlock = (JIfElseBlock) currentBlock;
                }
                ifElseBlock.addCondition(new JStmtBlock(currentUnit));
                currentBlock = ifElseBlock;
            } else if (unitType.equals(IF_THEN)) {
                JIfElseBlock ifElseBlock = (JIfElseBlock) currentBlock;
                ifElseBlock.addThenNext(new JStmtBlock(currentUnit));
            } else if (unitType.equals(RETURN)) {
                if (currentBlock instanceof JBasicBlock) {
                    JBasicBlock basicBlock = (JBasicBlock) currentBlock;
                    basicBlock.addNext(new JStmtBlock(currentUnit));
                } else {
                    JBasicBlock basicBlock = new JBasicBlock();
                    basicBlock.addNext(new JStmtBlock(currentUnit));
                    currentBlock.addNext(basicBlock);
                    currentBlock = basicBlock;
                }
            }
            if (head == null) {
                head = currentBlock;
            }
        }
        System.out.println("");
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
            return BASIC;
        }
        if (unit instanceof JReturnStmt || unit instanceof JReturnVoidStmt) {
            return RETURN;
        }
        if (PATTERN_ASSIGN.matcher(unitString).matches() ||
                PATTERN_INVOKE.matcher(unitString).matches()) {
            return INSTRUMENTATION;
        }
        if (currentBlock instanceof JInstrumentationBlock) {
            JInstrumentationBlock jib = (JInstrumentationBlock) currentBlock;
            InstrumentationType type = jib.getType();
            if (type.equals(InstrumentationType.IF_START)) {
                return IF_CONDITION;
            } else if (type.equals(InstrumentationType.IF_THEN_START)
                    || type.equals(InstrumentationType.IF_THEN_END)) {
                return IF_THEN;
            }
        }
        if (currentBlock instanceof JIfElseBlock) {
            if (unit instanceof JIfStmt) {
                return IF_CONDITION;
            } else {
                // 判断then分支有没有结束
                JIfElseBlock ifElseBlock = (JIfElseBlock) currentBlock;
                if (!ifElseBlock.isThenStart()) {
                    return IF_CONDITION;
                }
                if (!ifElseBlock.isThenEnd()) {
                    return IF_THEN;
                } else {
                    throw new RuntimeException("unknown");
                }
            }
        }
        return null;
    }
}

package com.mijack.sootdemo.block;

import soot.Unit;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class JIfElseBlock extends JBlock {
    protected JBasicBlock ifConditionBlock = new JBasicBlock();
    protected JBasicBlock ifThenBlock = new JBasicBlock();
    protected JBasicBlock ifElseBlock = new JBasicBlock();

    public JIfElseBlock() {
        super(BlockType.IF_BLOCK);
    }

    @Override
    public boolean canReturn() {
        return true;
    }

    public void addCondition(Unit unit) {
        ifConditionBlock.addLast(unit);
    }

    public Unit getConditionLastUnit() {
        return ifConditionBlock.getUnitDeque().peekLast();
    }

    public void addThenNext(Unit unit) {
        ifThenBlock.addLast(unit);
    }

    public Unit getThenLastUnit() {
        return ifThenBlock.getUnitDeque().peekLast();
    }

    public void addElseNext(Unit unit) {
        ifElseBlock.addLast(unit);
    }
}

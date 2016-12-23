package com.mijack.sootdemo.block;

import com.mijack.sootdemo.datas.JInstrumentationBlock;

import java.util.Deque;

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

    public void addCondition(JBlock block) {
        ifConditionBlock.addLast(block);
    }

    //
//    public Unit getConditionLastUnit() {
//        return ifConditionBlock.getBlockDeque().peekLast();
//    }
//
    public void addThenNext(JBlock block) {
        ifThenBlock.addLast(block);
    }

    //
//    public Unit getThenLastUnit() {
//        return ifThenBlock.getBlockDeque().peekLast();
//    }
//
    public void addElseNext(JBlock block) {
        ifElseBlock.addLast(block);
    }

    public boolean isThenEnd() {
        Deque<JBlock> deque = ifConditionBlock.getBlockDeque();
        return ifConditionBlock.size() > 2
                && (deque.getFirst() instanceof JInstrumentationBlock)
                && (deque.getLast() instanceof JInstrumentationBlock);
    }

    public boolean isThenStart() {
        Deque<JBlock> deque = ifThenBlock.getBlockDeque();
        return deque.size() > 0;
    }
}

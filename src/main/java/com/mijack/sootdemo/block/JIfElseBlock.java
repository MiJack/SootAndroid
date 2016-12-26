package com.mijack.sootdemo.block;

import java.util.Deque;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class JIfElseBlock extends JBlock {
    protected JBasicBlock ifConditionBlock = new JBasicBlock();
    protected JBasicBlock ifThenBlock = new JBasicBlock();
    protected JBasicBlock ifElseBlock = new JBasicBlock();
    private String id;

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
        Deque<JBlock> deque = ifThenBlock.getBlockDeque();
        return deque.size() > 2 &&
                (deque.getFirst() instanceof JInstrumentationBlock)
                && (deque.getLast() instanceof JInstrumentationBlock || deque.getLast() instanceof JReturnBlock);
    }

    public boolean isThenStart() {
        Deque<JBlock> deque = ifThenBlock.getBlockDeque();
        return deque.size() > 0 && (deque.getFirst() instanceof JInstrumentationBlock);
    }

    public boolean isElseStart() {
        Deque<JBlock> deque = ifElseBlock.getBlockDeque();
        return deque.size() > 0 && (deque.getFirst() instanceof JInstrumentationBlock);
    }

    public boolean isElseEnd() {
        Deque<JBlock> deque = ifElseBlock.getBlockDeque();
        return deque.size() > 2
                && (deque.getFirst() instanceof JInstrumentationBlock)
                && (deque.getLast() instanceof JInstrumentationBlock || deque.getLast() instanceof JReturnBlock);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public JBlock getBlock(Class<?> clazz, String id) {
        if (this.getClass().equals(clazz) && getId().equals(id)) {
            return this;
        }
        JBlock block = ifThenBlock.getBlock(clazz, id);
        if (block != null) {
            return block;
        }
        return ifElseBlock.getBlock(clazz, id);
    }
}

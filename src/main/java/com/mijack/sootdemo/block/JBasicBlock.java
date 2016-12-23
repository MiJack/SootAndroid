package com.mijack.sootdemo.block;

import soot.Unit;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Mr.Yuan
 * @since 2016/12/23.
 */
public class JBasicBlock extends JBlock {
    private Deque<Unit> deque = new ArrayDeque<>();

    public JBasicBlock() {
        this(BlockType.BASIC_BLOCK);
    }

    public JBasicBlock(BlockType block) {
        super(block);
    }

    public void addLast(Unit unit) {
        deque.addLast(unit);
    }

    public Deque<Unit> getUnitDeque() {
        return deque;
    }
}

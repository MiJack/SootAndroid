package com.mijack.sootdemo.block;

import soot.Unit;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class JBlock {
    protected LinkedList<JBlock> nextBlocks;
    BlockType blockType;

    public JBlock() {
        this(BlockType.BASIC_BLOCK);
    }

    public JBlock(BlockType blockType) {
        this.blockType = blockType;
        nextBlocks = new LinkedList<>();
    }

    public boolean canReturn() {
        return blockType.canReturn();
    }

    public void transform(BlockType type) {
        blockType = type;
    }

    public Iterator<JBlock> nextBlockIterator() {
        return nextBlocks.iterator();
    }

    public boolean isType(BlockType type) {
        return blockType.equals(type);
    }

    public void addNext(JBlock block) {
        nextBlocks.addLast(block);
    }

    public void addLast(Unit unit) {
        throw new UnsupportedOperationException();
    }


//
//    public Deque<Unit> getUnitDeque() {
//        return null;
//    }
}

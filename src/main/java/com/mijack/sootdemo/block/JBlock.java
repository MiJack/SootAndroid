package com.mijack.sootdemo.block;

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

    public Iterator<JBlock> nextBlockIterator() {
        return nextBlocks.iterator();
    }

    public boolean isType(BlockType type) {
        return blockType.equals(type);
    }

    public void addNext(JBlock block) {
        nextBlocks.addLast(block);
    }

    public void addLast(JBlock block) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return 0;
    }

//
//    public Deque<Unit> getBlockDeque() {
//        return null;
//    }
}

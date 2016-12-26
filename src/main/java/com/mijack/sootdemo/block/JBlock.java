package com.mijack.sootdemo.block;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author admin
 * @date 2016/12/21.
 */
public abstract class JBlock {
    protected LinkedList<JBlock> nextBlocks;
    BlockType blockType;

    public JBlock() {
        this(BlockType.BASIC_BLOCK);
    }

    public JBlock(BlockType blockType) {
        this.blockType = blockType;
        nextBlocks = new LinkedList<>();
    }

    public String getId() {
        return null;
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

    public JBlock getLast() {
        return null;
    }

    public JBlock nextBlock() {
        Iterator<JBlock> iterator = nextBlocks.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public boolean hasNextBlock() {
        return !nextBlocks.isEmpty();
    }

    public JBlock getBlock(Class<?> clazz, String id) {
        return null;
    }

//
//    public Deque<Unit> getBlockDeque() {
//        return null;
//    }
}

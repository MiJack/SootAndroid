package com.mijack.sootdemo.block;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * @author Mr.Yuan
 * @since 2016/12/23.
 */
public class JBasicBlock extends JBlock {
    private Deque<JBlock> deque = new ArrayDeque<>();

    public JBasicBlock() {
        this(BlockType.BASIC_BLOCK);
    }

    public JBasicBlock(BlockType block) {
        super(block);
    }

    public void addLast(JBlock unit) {
        deque.addLast(unit);
    }

    public Deque<JBlock> getBlockDeque() {
        return deque;
    }

    @Override
    public JBlock getLast() {
        return deque.peekLast();
    }

    @Override
    public JBlock getBlock(Class<?> clazz, String id) {
        if (this.getClass().equals(clazz)&&getId().equals(id)){
            return this;
        }
        Iterator<JBlock> iterator = getBlockDeque().iterator();
        while (iterator.hasNext()) {
            JBlock block = iterator.next();
            if (block.getClass().equals(clazz) && block.getId().equals(id)) {
                return block;
            }
        }
        return null;
    }
}

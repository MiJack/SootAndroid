package com.mijack.sootdemo.datas;

import soot.Unit;

import java.util.*;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class JBlock {
    protected LinkedHashMap<String, Unit> unitMap;
    protected LinkedList<JBlock> nextBlocks;
    BlockType blockType;

    public JBlock() {
        unitMap = new LinkedHashMap<>();
        nextBlocks = new LinkedList<>();
        blockType = BlockType.BASIC_BLOCK;
    }

    public JBlock(BlockType blockType) {
        this.blockType = blockType;
        unitMap = new LinkedHashMap<>();
        nextBlocks = new LinkedList<>();
    }

    public boolean canReturn() {
        return blockType.canReturn();
    }

    public void transform(BlockType type) {
        blockType = type;
    }

    public Iterator<JBlock> nextBlocks() {
        return nextBlocks.iterator();
    }

    public void addUnit(Unit unit) {
        unitMap.put(unit.toString(), unit);
    }

    public void copyFrom(JBlock block) {
//        protected LinkedHashMap<String, Unit> unitMap;
        for (Map.Entry<String, Unit> entry : unitMap.entrySet()) {
            addUnit(entry.getValue());
        }
//        protected LinkedList<JBlock> nextBlocks;
        Iterator<JBlock> iterator = nextBlocks.iterator();
        while (iterator.hasNext()) {
            addNextBlock(iterator.next());
        }
    }

    public void addNextBlock(JBlock next) {
        nextBlocks.add(next);
    }

    public boolean isType(BlockType type) {
        return blockType.equals(type);
    }
}

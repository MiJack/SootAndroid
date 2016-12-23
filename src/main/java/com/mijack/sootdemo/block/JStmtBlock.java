package com.mijack.sootdemo.block;

import soot.Unit;
import soot.jimple.internal.JReturnStmt;
import soot.jimple.internal.JReturnVoidStmt;

/**
 * @author Mr.Yuan
 * @since 2016/12/23.
 */
public class JStmtBlock extends JBlock {
    protected Unit rawUnit;

    public JStmtBlock(Unit rawUnit) {
        this.rawUnit = rawUnit;
        blockType = BlockType.STMT_BLOCK;
    }

    @Override
    public boolean canReturn() {
        return rawUnit instanceof JReturnStmt ||
                rawUnit instanceof JReturnVoidStmt;
    }

    @Override
    public String toString() {
        return "JStmtBlock[" + this.hashCode() + "]" + rawUnit ;
    }
}

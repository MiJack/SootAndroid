package com.mijack.sootdemo.block;

import soot.Unit;
import soot.jimple.internal.JReturnStmt;
import soot.jimple.internal.JReturnVoidStmt;

/**
 * @author Mr.Yuan
 * @since 2016/12/26.
 */
public class JReturnBlock extends JBasicBlock {
    private Unit returnUnit;

    public JReturnBlock(Unit unit) {
        if (!(unit instanceof JReturnStmt || unit instanceof JReturnVoidStmt)) {
            throw new IllegalArgumentException(unit.toString());
        }
        this.returnUnit = unit;
    }
}

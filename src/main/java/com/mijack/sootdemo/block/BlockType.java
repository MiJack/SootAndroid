package com.mijack.sootdemo.block;

/**
 * @author admin
 * @date 2016/12/21.
 */
public enum BlockType {
    BASIC_BLOCK,
    RETURN_BLOCK {
        @Override
        public boolean canReturn() {
            return true;
        }
    },
    IF_CONDITION_BLOCK,
    IF_THEN_BLOCK {
        @Override
        public boolean canReturn() {
            return true;
        }
    },
    IF_ELSE_BLOCK {
        @Override
        public boolean canReturn() {
            return true;
        }
    },
    IF_BLOCK {
        @Override
        public boolean canReturn() {
            return true;
        }
    }, STMT_BLOCK;

    public boolean canReturn() {
        return false;
    }
}

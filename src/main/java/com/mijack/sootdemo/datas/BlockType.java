package com.mijack.sootdemo.datas;

/**
 * @author admin
 * @date 2016/12/21.
 */
public enum BlockType {
    BASIC_BLOCK {
        @Override
        public boolean canReturn() {
            return false;
        }
    }, RETURN_BLOCK {
        @Override
        public boolean canReturn() {
            return true;
        }
    };

    public abstract boolean canReturn();
}

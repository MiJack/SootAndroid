package com.mijack.sootdemo.datas;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class JBasicBlock extends JBlock {
    @Override
    public boolean canReturn() {
        return false;
    }

    @Override
    public <T> T transform(Class<T> clazz) {
        return super.transform(clazz);
    }
}

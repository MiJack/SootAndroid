package com.mijack.sootdemo.datas;

import soot.Unit;

import java.util.LinkedHashMap;

/**
 * @author admin
 * @date 2016/12/21.
 */
public abstract class JBlock {
    protected LinkedHashMap<String, Unit> unitMap;

    public JBlock() {
        unitMap = new LinkedHashMap<>();
    }

    public abstract boolean canReturn();

    public <T> T transform(Class<T> clazz) {
        return null;
    }
}

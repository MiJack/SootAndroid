package com.mijack.faultlocationdemo;

/**
 * @author admin
 * @date 2016/12/21
 */
public enum Type {
    IF_START,
    IF_END,
    IF_THEN_START,
    IF_THEN_END,
    IF_ELSE_START,
    IF_ELSE_END,
    RETURN;
    private String output_format;

    Type() {
        this("[%d]");
    }

    Type(String output_format) {
        this.output_format = output_format;
    }

    public String name(Object... objects) {
        return this.toString() + String.format(output_format, objects);
    }
}

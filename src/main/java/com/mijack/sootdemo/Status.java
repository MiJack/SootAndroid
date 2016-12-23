package com.mijack.sootdemo;

/**
 * @author Mr.Yuan
 * @since 2016/12/23.
 */
public enum Status {
    STATUS_UNKNOWN,
    STATUS_BASIC,
    STATUS_RETURN,
    STATUS_IF_CONDITION,
//    STATUS_IF_CONDITION_END,
    STATUS_IF_END,
    STATUS_IF_THEN,
    STATUS_IF_THEN_END,
    STATUS_IF_ELSE_START,
    STATUS_IF_ELSE_END;

    public int value() {
        Status[] values = Status.values();
        for (int i = 0; i < values.length; i++) {
            if (this.equals(values[i])) {
                return i;
            }
        }
        return -1;
    }
}

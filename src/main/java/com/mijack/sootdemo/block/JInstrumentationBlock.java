package com.mijack.sootdemo.block;

import com.mijack.faultlocationdemo.InstrumentationType;
import com.mijack.sootdemo.core.JMethod;
import soot.Unit;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.regex.Matcher;

/**
 * @author Mr.Yuan
 * @since 2016/12/23.
 */
public class JInstrumentationBlock extends JBlock {

    private final Unit assignStmt;
    private final Unit invokeStmt;
    private InstrumentationType type;
    private String id;

    public JInstrumentationBlock(Unit assignStmt, Unit invokeStmt) {
        this.assignStmt = assignStmt;
        this.invokeStmt = invokeStmt;
        Matcher matcher1 = JMethod.PATTERN_ASSIGN.matcher(assignStmt.toString());
        if (matcher1.matches()) {
            type = InstrumentationType.valueOf(matcher1.group(2));
        }else {
            throw new IllegalArgumentException("Assign stmt[" +assignStmt.toString()+ "] is illegal");
        }
        Matcher matcher2 = JMethod.PATTERN_INVOKE.matcher(invokeStmt.toString());
        if (matcher2.matches()) {
            id = matcher2.group(2);
        }else {
            throw new IllegalArgumentException("Invoke stmt[" + invokeStmt.toString() + "] is illegal");
        }
    }

    public String getId() {
        return id;
    }

    public InstrumentationType getType() {
        return type;
    }

    @Override
    public void addLast(JBlock unit) {
        throw new NotImplementedException();
    }

    @Override
    public boolean canReturn() {
        return false;
    }

    @Override
    public int size() {
        return 2;
    }
}

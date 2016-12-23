package com.mijack.sootdemo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author admin
 * @date 2016/12/20.
 */
public class Demo {
    public static final String PATTERN_ASSIGN = "^([\\$|\\w]+) = <com.mijack.faultlocationdemo.Type: " +
            "com.mijack.faultlocationdemo.Type (\\w+)>$";
    public static final String PATTERN_INVOKE = "^staticinvoke <com.mijack.faultlocationdemo.InstrumentationHelper: " +
            "int instrumentation\\(com.mijack.faultlocationdemo.Type,int\\)>\\(([\\$|\\w]+),\\w+\\)$";
    public static final String PATTERN_INVOKE2 = "^staticinvoke <com.mijack.faultlocationdemo.InstrumentationHelper: " +
            "int instrumentation\\(com.mijack.faultlocationdemo.Type,int\\)>\\(([\\$|\\w]+), (\\w+)\\)$";
    public static final String demo = "$r2 = <com.mijack.faultlocationdemo.Type: com.mijack.faultlocationdemo.Type IF_START>";
    public static final String demo2 = "staticinvoke <com.mijack.faultlocationdemo.InstrumentationHelper: int " +
            "instrumentation(com.mijack.faultlocationdemo.Type,int)>($r2, 1)";


    public static void main(String[] args) {
        Pattern p1 = Pattern.compile(PATTERN_INVOKE2);
        Matcher matcher = p1.matcher(demo2);
        if (matcher.find()) {
//    System.out.println(matcher.group(0));
        }
//        int count = matcher.groupCount();
//        System.out.println(count);
        System.out.println(matcher.group(1));
        System.out.println(matcher.group(2));
    }
}

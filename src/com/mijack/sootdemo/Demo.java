package com.mijack.sootdemo;

import javax.security.auth.Subject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2016/12/20.
 */
public class Demo {
    public static void main(String[] args) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("apple", "苹果");
        map.put("watermelon", "西瓜");
        map.put("banana", "香蕉");
        map.put("peach", "桃子");
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        map.get("banana");
        map.get("apple");

          iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

    }
}

package com.mijack.sootdemo;

import com.mijack.sootdemo.core.JClass;
import soot.Scene;

import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2016/12/21.
 */
public class Apk {
    private Scene scene;
    private Map<String, JClass> classMap;

    public Apk() {
        classMap = new HashMap<>();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public void addClass(JClass jClass) {
        classMap.put(jClass.getClassName(), jClass);
    }

    public Map<String, JClass> getClasses() {
        return classMap;
    }
}

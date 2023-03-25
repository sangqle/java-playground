package com.sangqle.sample;

public class MyReflection {
    public static final MyReflection Instance = new MyReflection();
    private MyReflection() {}
    public void method1() {
        System.err.println("method 1");
    }
    private void method2() {
        System.err.println("private method 2");
    }
}

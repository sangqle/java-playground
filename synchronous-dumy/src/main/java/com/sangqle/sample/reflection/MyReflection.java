package com.sangqle.sample.reflection;

public class MyReflection {
    public static final MyReflection Instance = new MyReflection();
    private MyReflection() {}
    public void method1() {
        System.err.println("method 1");
    }

    private boolean isValidPin(int pin) {
        System.err.println("Call authentication for pin: " + pin);
        // Maybe invoke authentication by external service here
        return pin == 123;
    }

    private void method2(int pin) {
        if(!isValidPin(pin)) {
            System.err.println("Invalid pin: " + pin);
            return;
        }
            System.err.println("private method 2");
            System.err.println("Call another method here");
    }
}

package com.sangqle.sample.reflection;

import java.util.ArrayList;
import java.util.List;

public class MyReflection {
    public static final MyReflection Instance = new MyReflection();

    private MyReflection() {

    }

    public void method1() {
        System.err.println("method 1");
    }

    private boolean isValidPin(int pin) {
        System.err.println("Call authentication for pin: " + pin);
        // Maybe invoke authentication by external service here
        return pin == 123;
    }

    private void method2(int pin) {
        if (!isValidPin(pin)) {
            System.err.println("Invalid pin: " + pin);
            return;
        }
        System.err.println("private method 2");
        System.err.println("Call another method here");
    }

    public int getUserId(int userId) {
        if (100 > 0) {
            if (userId > 0) {
            }

        }
        return userId;
    }

    public void getUserInfo(int userId) {
        try {
            // get userInfo here
            System.err.println("get user info");
            System.err.println("Hellohooo: " + userId);
            System.err.println(String.format("Hello %s, ", userId));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        System.err.println("Hello from Zalo Zalo Team");
        for (int i = 0; i < args.length; i++) {
            System.err.println(String.format("Hello from java %s", i));
        }
        List<Integer> userIds = new ArrayList<>();
        userIds.add(1);
        userIds.add(333);
        for (Integer userId : userIds) {
            System.err.println(userId);
        }

    }

}

package com.example;
import java.lang.reflect.Field;

public class HelloWorldJNI {

    static {
        System.loadLibrary("native");
    }

    public static void main(String[] args) {
        HelloWorldJNI hello = new HelloWorldJNI();

        hello.getCpuInfo();
    }

    // Declare a native method sayHello() that receives no arguments and returns void
    // Declare the native methods
    public native void sayHello();

    public native int add(int a, int b);

    public native String getUserInfo(int userId);

    public native String sayHelloToMe(String name, boolean isFemale);
    public native void getCpuInfo();
}

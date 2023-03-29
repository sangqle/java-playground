package com.example;

public class HelloWorldJNI {

    static {
        System.loadLibrary("native");
    }
    
    public static void main(String[] args) {
        new HelloWorldJNI().sayHello();
        int a = 10;
        int b = 20;
        int c = new HelloWorldJNI().add(a, b);
        System.err.println("a + b = " + c);
    }

    // Declare a native method sayHello() that receives no arguments and returns void
    private native void sayHello();
    private native int add(int a, int b);
}
package com.sangqle.sample.jintest;

public class MyActivity {
    static {
        System.setProperty("java.library.path", "/Users/sanglq3/java/playground/synchronous-dumy/libs");
        System.loadLibrary("libmynative");
    }

    public static void main(String[] args) {
        int a = 2;
        int b = 3;

        int sum = add(a, b);
        System.err.println(System.getProperty("java.library.path"));
        System.out.println("Sum: " + sum);
    }

    private static native int add(int a, int b);
}

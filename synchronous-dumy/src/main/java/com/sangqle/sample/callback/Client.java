package com.sangqle.sample.callback;

public class Client {
    public void doHello(String name) {
        System.err.println("Hello " + name);
    }
    public static void main(String[] args) {
        LongTask task = new LongTask();
        Callback onTaskComplete = new Callback() {
            @Override
            public void onComplete(String result) {
                System.err.println("Task completed and Processing result...");
                System.err.println("Result: " + result);
            }
        };

        task.execute(onTaskComplete);
    }
}

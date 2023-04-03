package com.sangqle.sample.callback;

public class LongTask {
    public void execute(Callback callback) {
        // Perform long task
        String result = "Task completed!";
        callback.onComplete(result);
    }
}

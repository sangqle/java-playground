package com.sangqle.sample.synchronous;

import java.util.HashMap;
public class MySynchronized {

    private static MySynchronized instance = new MySynchronized();

    private MySynchronized() {
    }

    private Integer count = 0;
    private HashMap<Integer, User> userMap = new HashMap<>();

    // synchronized method to increment the counter
    public synchronized void increment() {
        count++;
        System.out.println("Incremented count to " + count);
        
    }

    public static void setInstance(MySynchronized instance) {
        MySynchronized.instance = instance;
    }

    public static MySynchronized getInstance() {
        return instance;
    }

    public User put(Integer key, User user) {
        return userMap.put(key, user);
    }

    public User get(Integer key) {
        // clone user instance to return
        return new User(userMap.get(key));
    }

    public HashMap<Integer, User> getUserMap() {
        return this.userMap;
    }


    public static void main(String[] args) {
        System.out.println("Hello world!");
        MySynchronized Instance = MySynchronized.getInstance();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Instance.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Instance.increment();
            }
        });

        HashMap<Integer, User> userMap = Instance.getUserMap();
        userMap.put(1, new User().setUsername("user1"));
        User user1 = Instance.get(1);
        user1.setUsername("user11");

        System.err.println("user1: " + user1);

        System.err.println("userMap: " + userMap);

    }
}

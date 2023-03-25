package com.sangqle.sample;
import java.lang.reflect.*;

public class TestMyReflection {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<MyReflection> myReflectionClass = MyReflection.class;
        Method[] methods = myReflectionClass.getDeclaredMethods();
        for (Method method : methods) {
            System.err.println(method.getName());
        }

        Method method2 = myReflectionClass.getDeclaredMethod("method2");
        MyReflection instance = MyReflection.Instance;
        method2.setAccessible(true);
        method2.invoke(instance);
    }
}

package com.sangqle.sample.handle.exception;

import java.io.IOException;

public class CatchMyEx {
    public static void main(String[] args) {
        try {
            throw new Throwable("This is a Throwable");
            // throw new IOException();
        } catch(Exception e) {
            System.err.println("Exception caught: " + e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}

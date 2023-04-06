package com.sangqle.sample.uuid;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GenUUID {
    public static void main(String[] args) {
        String inputStr = "I am not a standard UUID representation.";

        UUID uuid = UUID.nameUUIDFromBytes(inputStr.getBytes());
        UUID uuid2 = UUID.nameUUIDFromBytes(inputStr.getBytes());
        UUID uuid3 = UUID.nameUUIDFromBytes(inputStr.getBytes());

        assertTrue(uuid != null);

        assertEquals(uuid, UUID.randomUUID());
        assertEquals(uuid, uuid3);
    }
}

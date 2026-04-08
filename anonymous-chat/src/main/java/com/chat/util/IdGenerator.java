package com.chat.util;

import java.util.UUID;

/**
 * Utility class for generating unique identifiers
 */
public class IdGenerator {

    /**
     * Generate a unique user ID
     * This can be used as the anonymous identifier for users
     * @return A unique string identifier
     */
    public static String generateUserId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate a short unique ID (8 characters)
     * Useful for display purposes or temporary identifiers
     * @return A short unique string identifier
     */
    public static String generateShortId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

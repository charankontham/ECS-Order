package com.ecs.ecs_order.util;

import io.github.cdimascio.dotenv.Dotenv;

public class ExtractSecrets {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getSecret(String key) {
        return dotenv.get(key);
    }
}

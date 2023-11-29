package net.paiique.util;

import io.github.cdimascio.dotenv.Dotenv;

//Thanks Carmello

public enum Env {
    DEBUG("DEBUG"),
    DB_NAME("DB_NAME"),
    DB_HOST("DB_HOST"),
    DB_PORT("DB_PORT"),
    DB_USER("DB_USER"),
    DB_PASS("DB_PASS"),
    PTERO_TOKEN("PTERO_TOKEN"),
    SLACK_PROD_WEBHOOK("SLACK_PROD_WEBHOOK"),
    SLACK_DEV_WEBHOOK("SLACK_DEV_WEBHOOK"),
    ;

    private static final Dotenv dotenv = Dotenv.load();

    private final String name;
    Env(String name) {
        this.name = name;
    }

    public String get() {
        String value = dotenv.get(name);
        if (value == null || value.isEmpty())
            throw new RuntimeException("Env " + name + " is empty");
        return value;
    }
}


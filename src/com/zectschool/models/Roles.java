package com.zectschool.models;

public enum Roles {
    ADMIN("ADMIN"),
    COMPTABLE("COMPTABLE"),
    ELEVE("ELEVE"),
    PROFESSEUR("PROFESSEUR"),;

    private final String value;

    Roles(String value) {

        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}


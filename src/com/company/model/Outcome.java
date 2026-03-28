package com.company.model;

/**
 * Итог миссии
 */
public enum Outcome {
    SUCCESS("Успех"),
    FAILURE("Провал"),
    PARTIAL("Частичный успех");

    private final String displayName;

    Outcome(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Outcome fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Outcome.valueOf(value.trim().toUpperCase());
    }
}

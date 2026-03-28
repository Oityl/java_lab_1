package com.company.model;

/**
 * Тип техники заклинателя
 */
public enum TechniqueType {
    INNATE("Врождённая"),
    SHIKIGAMI("Шикигами"),
    DOMAIN("Расширение территории"),
    CURSED_SPEECH("Проклятая речь"),
    OTHER("Другая");

    private final String displayName;

    TechniqueType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TechniqueType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }
        try {
            return TechniqueType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}

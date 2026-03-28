package com.company.model;

/**
 * Уровень угрозы (проклятия)
 */
public enum ThreatLevel {
    GRADE_4("4-й ранг"),
    GRADE_3("3-й ранг"),
    GRADE_2("2-й ранг"),
    GRADE_1("1-й ранг"),
    HIGH("Высокий"),
    SPECIAL_GRADE("Особый");

    private final String displayName;

    ThreatLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ThreatLevel fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return ThreatLevel.valueOf(value.trim().toUpperCase());
    }
}

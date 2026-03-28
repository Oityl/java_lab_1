package com.company.model;

/**
 * Ранг мага-заклинателя
 */
public enum SorcererRank {
    GRADE_4("4-й ранг"),
    GRADE_3("3-й ранг"),
    GRADE_2("2-й ранг"),
    GRADE_1("1-й ранг"),
    SPECIAL_GRADE("Особый");

    private final String displayName;

    SorcererRank(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SorcererRank fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return SorcererRank.valueOf(value.trim().toUpperCase());
    }
}

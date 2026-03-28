package com.company.model;

/**
 * Маг-заклинатель
 */
public class Sorcerer {
    private String name;
    private SorcererRank rank;

    public Sorcerer() {
    }

    public Sorcerer(String name, SorcererRank rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SorcererRank getRank() {
        return rank;
    }

    public void setRank(SorcererRank rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return name + " (" + (rank != null ? rank.getDisplayName() : "?") + ")";
    }
}

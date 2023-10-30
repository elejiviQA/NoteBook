package org.noBrains.model;

public class Label {
    private final String word;

    public Label(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word;
    }
}

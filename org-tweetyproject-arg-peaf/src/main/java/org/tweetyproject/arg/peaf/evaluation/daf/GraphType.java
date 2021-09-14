package org.tweetyproject.arg.peaf.evaluation.daf;

public enum GraphType {
    WATTS("watts"), RANDOM("random"), BARABASI("barabasi");

    private final String text;

    GraphType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

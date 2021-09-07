package org.tweetyproject.arg.peaf.evaluation;

public enum GraphType {
    WATTS("watts"), RANDOM("random"), BARABASI("barabasi"), FULLY_CONNECTED("fully");

    private final String text;

    GraphType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

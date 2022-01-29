package org.tweetyproject.arg.peaf.evaluation.daf;

/**
 * The graph types available in the evaluation package.
 *
 * @author Taha Dogan Gunes
 */
public enum GraphType {
    /**
     * Watts graphs
     */
    WATTS("watts"),
    /**
     * Random graphs
     */
    RANDOM("random"),
    /**
     * BarabasiAlbert graphs
     */
    BARABASI("barabasi");

    /**
     * The keyword for the string enum
     */
    private final String text;

    /**
     * The default constructor for the enum
     *
     * @param text the final keyword
     */
    GraphType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

package org.tweetyproject.arg.peaf.syntax.aif;

/**
 * The AIFJSONTheory, utility class for reading aif json files with GSON
 * Intermediate json node representation
 *
 * @author Taha Dogan Gunes
 */
public class AIFJSONNode {
    /**
     * The unique id of the node
     */
    public String nodeID;
    /**
     * The text of the node
     */
    public String text;
    /**
     * The type of the node
     */
    public String type;

    /**
     * The optional probability field assigned to the node
     * (TODO: given the AIF specification is not finalised yet, this may be removed in future.)
     */
    public transient double probability;
}

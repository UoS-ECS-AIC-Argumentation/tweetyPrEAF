package org.tweetyproject.arg.peaf.syntax.aif;


import org.tweetyproject.arg.peaf.syntax.aif.analysis.AIFJSONAnalysis;

/**
 * The AIFJSONTheory, utility class for reading aif json files with GSON
 *
 * @author Taha Dogan Gunes
 */
public class AIFJSONTheory {
    /**
     * All the nodes in the aif
     */
    public AIFJSONNode[] nodes;
    /**
     * All the edges in the aif
     */
    public AIFJSONEdge[] edges;
    /**
     * All analyses in the aif (this is an extra addition)
     */
    public AIFJSONAnalysis[] analyses;
}

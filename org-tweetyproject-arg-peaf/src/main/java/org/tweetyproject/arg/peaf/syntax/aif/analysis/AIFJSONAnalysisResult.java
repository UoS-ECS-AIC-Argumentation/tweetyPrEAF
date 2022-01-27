package org.tweetyproject.arg.peaf.syntax.aif.analysis;

/**
 * AIFJSONAnalysisResult class is used for writing back to the aif file after the reasoner finishes
 *
 * @author Taha Dogan Gunes
 */
public class AIFJSONAnalysisResult {
    /**
     * The datetime of the moment when the reasoner finishes
     */
    public String datetime;
    /**
     * The output of the reasoner (can be a double value or a set of arguments (may include virtual ones))
     */
    public String outcome;
    /**
     * The status of the reasoner (can be success or can be error)
     */
    public String status;
    /**
     * The time elapsed during the reasoner in milliseconds
     */
    public String elapsedTimeMS;
}

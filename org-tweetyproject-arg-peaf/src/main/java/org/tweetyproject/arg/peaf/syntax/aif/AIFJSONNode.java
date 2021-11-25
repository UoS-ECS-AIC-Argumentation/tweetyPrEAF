package org.tweetyproject.arg.peaf.syntax.aif;

public class AIFJSONNode {
    public final String nodeID;
    public final String text;
    public final String type;
    public final String timestamp;

    public transient final double probability;
    public transient final String uncert;

    public AIFJSONNode(String nodeID, String text, String type, String timestamp, double probability, String uncert) {
        this.nodeID = nodeID;
        this.text = text;
        this.type = type;
        this.timestamp = timestamp;
        this.probability = probability;
        this.uncert = uncert;
    }
}

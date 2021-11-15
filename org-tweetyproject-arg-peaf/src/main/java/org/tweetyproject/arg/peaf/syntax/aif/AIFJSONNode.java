package org.tweetyproject.arg.peaf.syntax.aif;

public class AIFJSONNode {
    public final String nodeID;
    public final String text;
    public final String type;
    public final String timestamp;
    public final double probability;

    public AIFJSONNode(String nodeID, String text, String type, String timestamp, double probability) {
        this.nodeID = nodeID;
        this.text = text;
        this.type = type;
        this.timestamp = timestamp;
        this.probability = probability;
    }
}

package org.tweetyproject.arg.peaf.syntax.aif;

public class AIFJSONEdge {
    public final String edgeID;
    public final String fromID;
    public final String toID;

    public AIFJSONEdge(String edgeID, String fromID, String toID) {
        this.edgeID = edgeID;
        this.fromID = fromID;
        this.toID = toID;
    }
}

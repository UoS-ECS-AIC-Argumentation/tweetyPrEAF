package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.gson.annotations.SerializedName;

public class AIFJSONEdge {
    public final String edgeID;

    @SerializedName(value = "fromID", alternate = {"source"})
    public final String fromID;

    @SerializedName(value = "toID", alternate = {"target"})
    public final String toID;

    public AIFJSONEdge(String edgeID, String fromID, String toID) {
        this.edgeID = edgeID;
        this.fromID = fromID;
        this.toID = toID;
    }
}

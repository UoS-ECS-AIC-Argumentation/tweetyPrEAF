package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.gson.annotations.SerializedName;

/**
 * AIFJSONEdge represents edge of the aif json or cis file format (CISpaces)
 *
 * @author Taha Dogan Gunes
 */
public class AIFJSONEdge {
    /**
     * Edge identifier
     */
    public String edgeID;

    /**
     * Corresponds to the identifier of the node that originates this edge
     */
    @SerializedName(value = "fromID", alternate = {"source"})
    public String fromID;

    /**
     * Corresponds to the identifier of the node that is targeted by this edge
     */
    @SerializedName(value = "toID", alternate = {"target"})
    public String toID;
}
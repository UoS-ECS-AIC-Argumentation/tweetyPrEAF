package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AIFJSONTheory {
    public final AIFJSONNode[] nodes;
    public final AIFJSONEdge[] edges;

    public AIFJSONTheory(AIFJSONNode[] nodes, AIFJSONEdge[] edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public String getJSONString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}

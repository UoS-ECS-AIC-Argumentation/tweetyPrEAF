package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.tweetyproject.arg.peaf.syntax.aif.analysis.AIFJSONAnalysis;

public class AIFJSONTheory {
    public final AIFJSONNode[] nodes;
    public final AIFJSONEdge[] edges;
    public final AIFJSONAnalysis[] analyses;

    public AIFJSONTheory(AIFJSONNode[] nodes, AIFJSONEdge[] edges, AIFJSONAnalysis[] analyses) {
        this.nodes = nodes;
        this.edges = edges;
        this.analyses = analyses;
    }

    public String getJSONString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}

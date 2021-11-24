package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.common.collect.Sets;

import java.util.Set;

public class AIFNode {
    private Set<AIFNode> froms = Sets.newHashSet();
    private Set<AIFNode> tos = Sets.newHashSet();
    public final String nodeID;
    public final AIFNodeType nodeType;
    public final String text;
    public final double probability;

    public AIFNode(String nodeID, AIFNodeType nodeType, String text, double probability) {
        this.nodeID = nodeID;
        this.nodeType = nodeType;
        this.text = text;
        this.probability = probability;
    }

    public Set<AIFNode> getFroms() {
        return froms;
    }

    public Set<AIFNode> getTos() {
        return tos;
    }

    @Override
    public String toString() {
        return "AIFNode{" +
                "id='" + nodeID + '\'' +
                ", type=" + nodeType +
                ", text='" + text + '\'' +
                ", prob=" + probability +
                '}';
    }
}


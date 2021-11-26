package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class AIFTheory {
    public Map<String, AIFNode> sNodeMap = Maps.newHashMap();
    public Map<String, AIFNode> iNodeMap = Maps.newHashMap();
    public Map<String, AIFNode> nodeMap = Maps.newHashMap();

    @Override
    public String toString() {
        return "AIFTheory{" +
                "\n  sNodeMap=" + sNodeMap +
                "\n, iNodeMap=" + iNodeMap +
                "\n, nodeMap=" + nodeMap +
                '}';
    }

    public void annotateEdges(String uncertaintyPath) throws FileNotFoundException {
        if (uncertaintyPath == null) {
            System.err.println("Warning: Uncertainty table is not given.");
            return;
        }
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(uncertaintyPath));
        UncertaintyTable uncertaintyTable = gson.fromJson(reader, UncertaintyTable.class);
        Map<String, Double> table = uncertaintyTable.getTable();

        if (table.size() == 0) {
            System.err.println("Uncertainty table is empty.");
            return;
        }

        for (Map.Entry<String, Double> entry : table.entrySet()) {
            String nodeID = entry.getKey();
            Double probability = entry.getValue();

            if (sNodeMap.containsKey(nodeID)) {
                AIFNode node = sNodeMap.get(nodeID);
                node.setProbability(probability);
            }
            else {
                System.err.println("Warning: The nodeID ('" + nodeID + "') was not in AIF file. Given table path is: " + uncertaintyPath);
            }
        }

    }
}

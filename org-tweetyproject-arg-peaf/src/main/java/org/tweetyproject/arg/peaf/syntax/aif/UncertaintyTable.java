package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.common.collect.Maps;
import org.tweetyproject.arg.peaf.syntax.YardstickProbability;

import java.util.Map;

public class UncertaintyTable {
    public Map<String, String> probabilities = Maps.newHashMap();

    public Map<String, Double> getTable() {
        Map<String, Double> doubleProbabilities = Maps.newHashMap();

        for (Map.Entry<String, String> entry : probabilities.entrySet()) {
            String nodeID = entry.getKey();
            String yardstickString = entry.getValue();

            Double probability = 0.5;
            YardstickProbability yardstickProbability = YardstickProbability.get(yardstickString);
            if (yardstickProbability != null) {
                probability = yardstickProbability.toDouble();
            }
            doubleProbabilities.put(nodeID, probability);
        }

        return doubleProbabilities;
    }
}

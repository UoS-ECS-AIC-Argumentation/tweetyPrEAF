package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.common.collect.Maps;
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

}

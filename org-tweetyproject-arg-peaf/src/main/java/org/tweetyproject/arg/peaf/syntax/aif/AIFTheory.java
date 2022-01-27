package org.tweetyproject.arg.peaf.syntax.aif;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * AIFTheory is created after AIFJSONTheory by AIFCISReader
 *
 * @author Taha Dogan Gunes
 */
public class AIFTheory {
    /**
     * All S-Nodes are stored in this map while key is the aif identifier and the value is the node
     */
    public Map<String, AIFNode> sNodeMap = Maps.newHashMap();
    /**
     * All i-Nodes are stored in this map while key is the aif identifier and the value is the node
     */
    public Map<String, AIFNode> iNodeMap = Maps.newHashMap();
    /**
     * All nodes are stored in this map while key is the aif identifier and the value is the node
     */
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

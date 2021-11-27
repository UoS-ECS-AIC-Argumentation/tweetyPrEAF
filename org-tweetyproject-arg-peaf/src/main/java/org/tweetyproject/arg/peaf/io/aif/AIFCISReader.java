package org.tweetyproject.arg.peaf.io.aif;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFTheoryReader;
import org.tweetyproject.arg.peaf.syntax.aif.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AIFCISReader can also read .cis files, since the field names in edges is the only
 * difference. This is handled by GSON's alternative argument in @SerializedName.
 */
public class AIFCISReader {
    private final String pathString;
    private static final ClassLoader loader = PEEAFTheoryReader.class.getClassLoader();

    public AIFCISReader(String pathString) {
        this.pathString = pathString;
    }

    public AIFTheory read() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(pathString));

        AIFJSONTheory aifJSON = gson.fromJson(reader, AIFJSONTheory.class);
        AIFTheory aif = new AIFTheory();

        Set<String> illocutionaryNodeIDs = Sets.newHashSet();
        // Iterate and add all nodes
        for (AIFJSONNode aifjsonNode : aifJSON.nodes) {
            AIFNodeType type = AIFNodeType.get(aifjsonNode.type);
            if (type == null) {
                if (AIFNodeType.isAnIgnoredNodeType(aifjsonNode.type)) {
                    // Ignore illocutionary nodes
                    illocutionaryNodeIDs.add(aifjsonNode.nodeID);
                    continue;
                }
                throw new UnsupportedNodeTypeException("The given AIF has an unsupported node type: "
                        + aifjsonNode.type + " nodeID: " + aifjsonNode.nodeID + " text: " + aifjsonNode.text + "\n in " + pathString);
            }
            if (aif.nodeMap.containsKey(aifjsonNode.nodeID)) {
                throw new DuplicateNodeIDException("The node ID: " + aifjsonNode.nodeID + " is a duplicate." + "\n in " + pathString);
            }

            // FIXME: Assume the probability is 0.5 if the field does not exists in JSON.
            // FIXME: The probabilities will be changed with categorical data.
            double probability = aifjsonNode.probability;
            if (probability == 0.0) {
                probability = 0.5;
            }

            AIFNode aifNode = new AIFNode(aifjsonNode.nodeID, type, aifjsonNode.text, probability);
            if (type == AIFNodeType.I) {
                aif.iNodeMap.put(aifjsonNode.nodeID, aifNode);
            }
            else { // S Nodes
                aif.sNodeMap.put(aifjsonNode.nodeID, aifNode);
            }

            aif.nodeMap.put(aifjsonNode.nodeID, aifNode);
        }

        // Add edges of the nodes
        for (AIFJSONEdge edge : aifJSON.edges) {
            String fromID = edge.fromID;
            String toID = edge.toID;

            if (illocutionaryNodeIDs.contains(fromID) || illocutionaryNodeIDs.contains(toID)) {
                continue;
            }

            AIFNode fromNode = aif.nodeMap.get(fromID);
            if (fromNode == null) {
                System.err.println("Warning: The given edge with id: " + edge.edgeID + " fromID: " + fromID + " node does not exist." + "\n in " + pathString);
                continue;
            }

            AIFNode toNode = aif.nodeMap.get(toID);
            if (toNode == null) {
                System.err.println("Warning: The given edge with id: " + edge.edgeID + " toID: " + toID + " node does not exist." + "\n in " + pathString);
                continue;
            }

            boolean valid = isValid(fromNode, toNode);
            valid = valid || isValid(toNode, fromNode);

            if (valid) {
                fromNode.getTos().add(toNode);
                toNode.getFroms().add(fromNode);
            } else {
                System.err.println("Warning: The edge id: " + edge.edgeID + " is not supported by tweetyPrEAF since, toID: " + toID + " fromID: " +
                        fromID + " fromType: " + fromNode.nodeType.toString() + " toType: " + toNode.nodeType.toString() + "\n in " + pathString);
            }
        }

        return aif;
    }

    private boolean isValid(AIFNode node1, AIFNode node2) {
        return node1.nodeType == AIFNodeType.I && (node2.nodeType == AIFNodeType.CA || node2.nodeType == AIFNodeType.RA);
    }

    public static class UnsupportedNodeTypeException extends RuntimeException {
        private static AtomicLong atomicLong = new AtomicLong(0);
        public UnsupportedNodeTypeException(String message) {
            super(message);
            atomicLong.getAndIncrement();
        }
        public static long getOccurrenceCount() {
            return atomicLong.get();
        }
    }

    public static class DuplicateNodeIDException extends RuntimeException {
        private static AtomicLong atomicLong = new AtomicLong(0);
        public DuplicateNodeIDException(String message) {
            super(message);
            atomicLong.getAndIncrement();
        }

        public static long getOccurrenceCount() {
            return atomicLong.get();
        }
    }

    public static class Exceptions {

        public static long describe() {
            long count = 0;
            System.out.println("AIFCISReader.UnsupportedNodeTypeException count: " + AIFCISReader.UnsupportedNodeTypeException.getOccurrenceCount());
            count += AIFCISReader.UnsupportedNodeTypeException.getOccurrenceCount();
            System.out.println("AIFCISReader.DuplicateNodeIDException count: " + AIFCISReader.DuplicateNodeIDException.getOccurrenceCount());
            count += AIFCISReader.DuplicateNodeIDException.getOccurrenceCount();
            return count;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        AIFCISReader reader = new AIFCISReader(loader.getResource("aif/5.json").getPath());
        AIFTheory aifTheory = reader.read();
        System.out.println(aifTheory.toString());
    }
}

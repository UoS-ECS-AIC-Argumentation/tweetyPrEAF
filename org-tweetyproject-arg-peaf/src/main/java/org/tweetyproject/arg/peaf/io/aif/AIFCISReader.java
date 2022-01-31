package org.tweetyproject.arg.peaf.io.aif;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.tweetyproject.arg.peaf.syntax.aif.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AIFCISReader can also read .cis files, since the field names in edges is the only
 * difference. This is handled by GSON's alternative argument in @SerializedName.
 *
 * @author Taha Dogan Gunes
 */
public class AIFCISReader {
    /**
     * The path of the aif or cis file path
     */
    private final String pathString;

    /**
     * Default constructor for AIFCISReader
     *
     * @param pathString aif or cis file path
     */
    public AIFCISReader(String pathString) {
        this.pathString = pathString;
    }

    /**
     * Reads (and converts) aifJSON to an intermediate format AIFTheory
     * <p>
     * During the conversion unsupported edges and errors are reported.
     *
     * @param aifJSON the raw aif in json format
     * @return AIFTheory object
     */
    public AIFTheory read(AIFJSONTheory aifJSON) {
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

            AIFNode aifNode = new AIFNode(aifjsonNode.nodeID, type, aifjsonNode.text);
            if (type == AIFNodeType.I) {
                aif.iNodeMap.put(aifjsonNode.nodeID, aifNode);
            } else { // S Nodes
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

    /**
     * Read and convert an aif or cis file from given file path
     *
     * @return AIFTheory object
     * @throws FileNotFoundException if the file is not found in the given file path
     */
    public AIFTheory read() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(pathString));

        AIFJSONTheory aifJSON = gson.fromJson(reader, AIFJSONTheory.class);
        return this.read(aifJSON);
    }

    /**
     * This function checks the validity of the connection between nodes.
     * <p>
     * Return true only if node1 is I-node and node2 is either CA, RA or MA node.
     * This means that we only allow I-node to have an edge to CA, RA or MA, not to an I-node.
     *
     * @param node1 a node in aif
     * @param node2 a node in aif
     * @return true if the connection is valid
     */
    private boolean isValid(AIFNode node1, AIFNode node2) {
        return node1.nodeType == AIFNodeType.I && (node2.nodeType == AIFNodeType.CA || node2.nodeType == AIFNodeType.RA || node2.nodeType == AIFNodeType.MA);
    }

    /**
     * This exception is thrown in the cases where the aif node's type is not supported by AIFCISReader.
     */
    public static class UnsupportedNodeTypeException extends RuntimeException {
        private static final AtomicLong atomicLong = new AtomicLong(0);

        public UnsupportedNodeTypeException(String message) {
            super(message);
            atomicLong.getAndIncrement();
        }

        public static long getOccurrenceCount() {
            return atomicLong.get();
        }
    }

    /**
     * This exception is thrown in the cases where the node id was defined previously in the aif.
     */
    public static class DuplicateNodeIDException extends RuntimeException {
        private static final AtomicLong atomicLong = new AtomicLong(0);

        public DuplicateNodeIDException(String message) {
            super(message);
            atomicLong.getAndIncrement();
        }

        public static long getOccurrenceCount() {
            return atomicLong.get();
        }
    }

    /**
     * Helper class for displaying associated exceptions' occurrence
     */
    public static class Exceptions {

        /**
         * Outputs the occurrence of AIFCISReader exceptions.
         *
         * @return the count of the total occurrence of exceptions
         */
        public static long describe() {
            long count = 0;
            System.out.println("AIFCISReader.UnsupportedNodeTypeException count: " + AIFCISReader.UnsupportedNodeTypeException.getOccurrenceCount());
            count += AIFCISReader.UnsupportedNodeTypeException.getOccurrenceCount();
            System.out.println("AIFCISReader.DuplicateNodeIDException count: " + AIFCISReader.DuplicateNodeIDException.getOccurrenceCount());
            count += AIFCISReader.DuplicateNodeIDException.getOccurrenceCount();
            return count;
        }
    }
}

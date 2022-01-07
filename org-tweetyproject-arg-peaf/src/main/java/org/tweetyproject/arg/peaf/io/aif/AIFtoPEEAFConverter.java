package org.tweetyproject.arg.peaf.io.aif;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFTheoryReader;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFToPEAFConverter;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEEAFTheory;
import org.tweetyproject.arg.peaf.syntax.aif.AIFNode;
import org.tweetyproject.arg.peaf.syntax.aif.AIFNodeType;
import org.tweetyproject.arg.peaf.syntax.aif.AIFTheory;
import org.tweetyproject.arg.peaf.syntax.aif.YardstickProbability;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class AIFtoPEEAFConverter {
    private static final ClassLoader loader = PEEAFTheoryReader.class.getClassLoader();

    public PEEAFTheory convert(AIFTheory aifTheory) {
        PEEAFTheory peeafTheory = new PEEAFTheory();
        peeafTheory.addArgument("eta", "eta");

        if (aifTheory.nodeMap.size() == 0
                && aifTheory.sNodeMap.size() == 0
                && aifTheory.iNodeMap.size() == 0) {
            System.err.println("Warning: Given aifTheory is empty.");
            return peeafTheory;
        }

        // I-nodes is for arguments
        for (Map.Entry<String, AIFNode> entry : aifTheory.iNodeMap.entrySet()) {
            String nodeID = entry.getKey();
            AIFNode node = entry.getValue();
            // FIXME: node.probability is not considered here for iNodes
            // FIXME: some preprocessing (perhaps not necessary)
            String text = "";
            if (node.text != null) {
                text = node.text.replaceAll("\\r|\\n", "");
            }
            peeafTheory.addArgument(nodeID, text);
        }

        // MA - Condition #3: [I* Node - A] -> [MA Node] -> [I node - C] <- [MA Node] <- [I* Node - B]
        //  to <- MA <- froms
        Map<AIFNode, Set<AIFNode>> maNodeTosMap = Maps.newHashMap();
        Set<String> restatedNodes = Sets.newHashSet();

        // attack map (For ignoring MA from nodes)
        Map<AIFNode, AIFNode> attackMap = Maps.newHashMap();

        int supportCount = 0;
        int attackCount = 0;
        for (Map.Entry<String, AIFNode> entry : aifTheory.sNodeMap.entrySet()) {
            AIFNode node = entry.getValue();
            // https://www.researchgate.net/publication/49466088_Moving_Between_Argumentation_Frameworks
            // RA node is for supports

            if (node.nodeType == AIFNodeType.RA) {
                for (AIFNode to : node.getTos()) {
                    if (node.getFroms().size() == 0) {
                        throw new INodeForRANodeNotFoundException("The I-node that originates this RA node (`" + node + "`) does not exist.");
                    }

                    String[] fromIdentifiers = new String[node.getFroms().size()];
                    int i = 0;
                    for (AIFNode from : node.getFroms()) {
                        fromIdentifiers[i] = from.nodeID;
                        i++;
                    }
                    peeafTheory.addSupport("" + supportCount, fromIdentifiers, to.nodeID, node.probability);
                    supportCount++;
                }
            }
            // CA node is for attacks
            else if (node.nodeType == AIFNodeType.CA) {
                for (AIFNode to : node.getTos()) {
                    for (AIFNode from : node.getFroms()) {
                        peeafTheory.addAttack("" + attackCount, from.nodeID, to.nodeID, node.probability);
                        attackCount++;
                        // MA - Condition #3: [I* Node - A] -> [MA Node] -> [I node - C] <- [MA Node] <- [I* Node - B]
                        attackMap.put(from, to);
                    }
                }
            }
            // MA node is for restatements (reframe)
            else if (node.nodeType == AIFNodeType.MA) {
                // Condition #1: [I* node] -> [MA Node] -> [I node]
                // There can be only a single I-node that is directed to a MA node
                // There can be only a single I-Node that is directed from a MA node
                if (node.getFroms().size() != 1) {
                    throw new MultipleNodesToMANodeException("There can not be multiple nodes directed to this MA node (`" + node + "`).");
                }

                if (node.getTos().size() != 1) {
                    throw new MultipleNodesFromMANodeException("There can not be multiple nodes directed from MA node (`" + node + "`).");
                }

                AIFNode from = node.getFroms().iterator().next();
                AIFNode to = node.getTos().iterator().next();

                if (from.nodeType != AIFNodeType.I) {
                    throw new FromMANodeNotINodeException("From node of MA node's type is not I, it is: `" + from.nodeType + "`).");
                }

                if (to.nodeType != AIFNodeType.I) {
                    throw new ToMANodeNotINodeException("To node of MA node's type is not I, it is: `" + to.nodeType + "`).");
                }

                // Condition #2: from [I* node] is a special I-Node where it's text is from YardstickProbability
                YardstickProbability yardstickProbability = YardstickProbability.get(from.text);
                double probability;
                if (yardstickProbability == null) {
                    System.err.println("The reframe originator (it was `" + from.text + "`does not comply with YardstickProbability, assuming the probability as 1.0.");
                    probability = 1.0;
                }
                else {
                    probability = yardstickProbability.toDouble();
                }

                peeafTheory.addSupport(""+ supportCount, new String[]{from.nodeID}, to.nodeID, probability);
                supportCount++;

                // Condition #3: [I* Node - A] -> [MA Node] -> [I node - C] <- [MA Node] <- [I* Node - B]
                // In this instance, we assume there is an attack between A node to B node and B node to A node.
                // These are marked to be checked later.
                if (!maNodeTosMap.containsKey(to)) {
                    maNodeTosMap.put(to, Sets.newHashSet(from));
                }
                else {
                    maNodeTosMap.get(to).add(from);
                }
                restatedNodes.add(to.nodeID);
            }
        }

        // MA - Condition #3: [I* Node - A] -> [MA Node] -> [I node - C] <- [MA Node] <- [I* Node - B]
        for (Map.Entry<AIFNode, Set<AIFNode>> entry : maNodeTosMap.entrySet()) {
            // to, froms
            Set<AIFNode> froms = entry.getValue();

            if (froms.size() >= 2) {
                for (Set<AIFNode> combination : Sets.combinations(froms, 2)) {
                    Iterator<AIFNode> iterator = combination.iterator();
                    AIFNode fromA = iterator.next();
                    AIFNode toB = iterator.next();

                    if (attackMap.get(fromA) != toB){
                        peeafTheory.addAttack("" + attackCount, fromA.nodeID, toB.nodeID, 1.0);
                        attackCount++;
                    }

                    if (attackMap.get(toB) != fromA){
                        peeafTheory.addAttack("" + attackCount, toB.nodeID, fromA.nodeID, 1.0);
                        attackCount++;
                    }
                }
            }
        }

        /*
        Retrieved from: https://www.researchgate.net/publication/49466088_Moving_Between_Argumentation_Frameworks
        "Finally, add an edge from η to any I-Node which does not have an edge leading to
        it that originates at an RA-Node. This last step makes the assumption that the I-Node is
        either true by default, or has some support from unassailable evidence. If the AIF graph
        encodes such evidential notions, then this last step is not necessary."
         */
        Set<PEEAFTheory.Argument> candidateArguments = Sets.newHashSet(peeafTheory.getArguments().subList(1, peeafTheory.getArguments().size()));
        // In contrast to the paper, we follow a different approach. We make a link from eta to any node that has not
        // supported by any support links. The candidate arguments here denote the ones that will get a support link
        // from eta to such arguments.
        for (PEEAFTheory.Support support : peeafTheory.getSupports()) {
            if (support.getTo() != null) {
                candidateArguments.remove(support.getTo());
            }
        }


        if (candidateArguments.size() == 0) {
            throw new NoNotAttackedINodeException("There is not a node that does not receive any attack in this AIF.");
        }

        for (PEEAFTheory.Argument candidateArgument : candidateArguments) {
            // MA - Condition #3: [I* Node - A] -> [MA Node] -> [I node - C] <- [MA Node] <- [I* Node - B]
            if (!restatedNodes.contains(candidateArgument.getIdentifier())) {
                peeafTheory.addSupport(""+supportCount, new String[]{"eta"}, candidateArgument.getIdentifier(), 1.0);
                supportCount++;
            }
        }



        return peeafTheory;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        AIFCISReader reader = new AIFCISReader(loader.getResource("cis/aif/aif-cap-phase4.json").getPath());
        AIFTheory aifTheory = reader.read();
        AIFtoPEEAFConverter aifConverter = new AIFtoPEEAFConverter();
        PEEAFTheory peeafTheory = aifConverter.convert(aifTheory);
        peeafTheory.prettyPrint();

        PEEAFToPEAFConverter peeafConverter = new PEEAFToPEAFConverter();
        PEAFTheory peafTheory = peeafConverter.convert(peeafTheory);
        peafTheory.prettyPrint();
    }

    public static class INodeForRANodeNotFoundException extends RuntimeException {
        private static AtomicLong atomicLong = new AtomicLong(0);
        public INodeForRANodeNotFoundException(String message) {
            super(message);
            atomicLong.getAndIncrement();
        }
        public static long getOccurrenceCount() {
            return atomicLong.get();
        }
    }

    public static class NoNotAttackedINodeException extends RuntimeException {
        private static AtomicLong atomicLong = new AtomicLong(0);
        public NoNotAttackedINodeException(String message) {
            super(message);
            atomicLong.getAndIncrement();
        }

        public static long getOccurrenceCount() {
            return atomicLong.get();
        }
    }

    public static class MultipleNodesToMANodeException extends RuntimeException {
        private static AtomicLong atomicLong = new AtomicLong(0);
        public MultipleNodesToMANodeException(String message) {
            super(message);
            atomicLong.getAndIncrement();
        }

        public static long getOccurrenceCount() {
            return atomicLong.get();
        }
    }

    public static class MultipleNodesFromMANodeException extends RuntimeException {
        private static AtomicLong atomicLong = new AtomicLong(0);
        public MultipleNodesFromMANodeException(String message) {
            super(message);
            atomicLong.getAndIncrement();
        }

        public static long getOccurrenceCount() {
            return atomicLong.get();
        }
    }

    public static class FromMANodeNotINodeException extends RuntimeException {
        private static AtomicLong atomicLong = new AtomicLong(0);
        public FromMANodeNotINodeException(String message) {
            super(message);
            atomicLong.getAndIncrement();
        }

        public static long getOccurrenceCount() {
            return atomicLong.get();
        }
    }

    public static class ToMANodeNotINodeException extends RuntimeException {
        private static AtomicLong atomicLong = new AtomicLong(0);
        public ToMANodeNotINodeException(String message) {
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
            System.out.println("AIFtoPEEAF.INodeForRANodeNotFoundException count: " + INodeForRANodeNotFoundException.getOccurrenceCount());
            count += INodeForRANodeNotFoundException.getOccurrenceCount();
            System.out.println("AIFtoPEEAF.NoNotAttackedINodeException count: " + NoNotAttackedINodeException.getOccurrenceCount());
            count += NoNotAttackedINodeException.getOccurrenceCount();
            System.out.println("AIFtoPEEAF.MultipleNodesForMANodeException count: " + MultipleNodesToMANodeException.getOccurrenceCount());
            count += MultipleNodesToMANodeException.getOccurrenceCount();
            System.out.println("AIFtoPEEAF.MultipleNodesFromMANodeException count: " + MultipleNodesFromMANodeException.getOccurrenceCount());
            count += MultipleNodesFromMANodeException.getOccurrenceCount();
            System.out.println("AIFtoPEEAF.FromMANodeNotINodeException count: " + FromMANodeNotINodeException.getOccurrenceCount());
            count += FromMANodeNotINodeException.getOccurrenceCount();
            System.out.println("AIFtoPEEAF.ToMANodeNotINodeException count: " + ToMANodeNotINodeException.getOccurrenceCount());
            count += ToMANodeNotINodeException.getOccurrenceCount();
            return count;
        }
    }


}

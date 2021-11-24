package org.tweetyproject.arg.peaf.io.aif;

import com.google.common.collect.Sets;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFTheoryReader;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFToPEAFConverter;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEEAFTheory;
import org.tweetyproject.arg.peaf.syntax.aif.AIFNode;
import org.tweetyproject.arg.peaf.syntax.aif.AIFNodeType;
import org.tweetyproject.arg.peaf.syntax.aif.AIFTheory;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

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
            // AIFNode node = entry.getValue();
            // FIXME: node.probability is not considered here for iNodes
            peeafTheory.addArgument(nodeID, nodeID);
        }

        int supportCount = 0;
        int attackCount = 0;
        for (Map.Entry<String, AIFNode> entry : aifTheory.sNodeMap.entrySet()) {
            AIFNode node = entry.getValue();
            // https://www.researchgate.net/publication/49466088_Moving_Between_Argumentation_Frameworks
            // RA node is for supports

            if (node.nodeType == AIFNodeType.RA) {
                for (AIFNode to : node.getTos()) {
                    if (node.getFroms().size() == 0) {
                        throw new RuntimeException("The I-node that originates this RA node (`" + node + "`) does not exist.");
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
        for (PEEAFTheory.Attack attack : peeafTheory.getAttacks()) {
            if (attack.getTo() instanceof PEEAFTheory.Argument) {
                candidateArguments.remove((PEEAFTheory.Argument) attack.getTo());
            }
        }
        PEEAFTheory.Argument argument = null;

        for (PEEAFTheory.Argument candidateArgument : candidateArguments) {
            argument = candidateArgument;
            break;
        }

        if (candidateArguments.size() == 0) {
            throw new RuntimeException("There is not a node that does not receive any attack in this AIF.");
        }

        peeafTheory.addSupport(""+supportCount, new String[]{"eta"}, argument.getName(), 1.0);

        return peeafTheory;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        AIFReader reader = new AIFReader(loader.getResource("aif/5.json").getPath());
        AIFTheory aifTheory = reader.read();
        AIFtoPEEAFConverter aifConverter = new AIFtoPEEAFConverter();
        PEEAFTheory peeafTheory = aifConverter.convert(aifTheory);
        peeafTheory.prettyPrint();

        PEEAFToPEAFConverter peeafConverter = new PEEAFToPEAFConverter();
        PEAFTheory peafTheory = peeafConverter.convert(peeafTheory);
        peafTheory.prettyPrint();
    }
}

package org.tweetyproject.arg.peaf.evaluation;

import org.graphstream.algorithm.TarjanStronglyConnectedComponents;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Adapted from Federico Cerutti's Java version AFBenchGen2
 * https://sourceforge.net/projects/afbenchgen/
 */
public abstract class SyntheticPEAF extends PEAFTheory {
    private final GraphType graphType;

    public SyntheticPEAF(GraphType graphType, int noArguments) {
        super(noArguments);
        this.graphType = graphType;
        this.validateNoArgs(noArguments);
    }

    protected void validateNoArgs(int noArguments) {
        if (noArguments <= 0) {
            throw new RuntimeException("The given number of arguments must be greater than 0.");
        }
    }

    protected void validateProbabilities(double probability) {
        if (probability < 0 || probability > 1) {
            throw new RuntimeException("The given probability value must be between 0 and 1.");
        }
    }

    protected void addEdges(Graph graph) {
        // This is important.
        this.addSupport(new int[]{}, new int[]{0}, 1.0);

        for (Edge edge : graph.getEdgeSet()) {

            double random = ThreadLocalRandom.current().nextDouble();
            double linkProbability = ThreadLocalRandom.current().nextDouble();

            int[] fromIndices = {edge.getNode0().getIndex()};
            int[] toIndices = {edge.getNode1().getIndex()};

            if (random >= 0.1) {
                this.addSupport(fromIndices, toIndices, linkProbability);
            }
            else {
                // FIXME: This makes attacks to eta ignored, there can be a better way
                if (edge.getNode1().getIndex() != 0) {
                    this.addAttack(fromIndices, toIndices, linkProbability);
                }
            }
        }

    }

    protected int computeStronglyConnectedComponents(Graph graph) {
        TarjanStronglyConnectedComponents cc = new TarjanStronglyConnectedComponents();
        cc.init(graph);
        cc.compute();
        int max = 0;
        for (Node n : graph.getNodeSet())
        {
            if ((Integer) n.getAttribute(cc.getSCCIndexAttribute()) > max)
                max = n.getAttribute(cc.getSCCIndexAttribute());
        }
        return max;
    }


    protected boolean edgeBetweenNodes(Graph graph, Node source, Node target)
    {
        for (Edge e : graph.getEdgeSet())
        {
            if (e.getNode0().equals(source) && e.getNode1().equals(target))
                return true;
        }
        return false;
    }
}

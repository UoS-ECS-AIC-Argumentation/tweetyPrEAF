package org.tweetyproject.arg.peaf.evaluation.daf;

import org.graphstream.algorithm.TarjanStronglyConnectedComponents;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;

/**
 * Adapted from Federico Cerutti's Java version AFBenchGen2
 * https://sourceforge.net/projects/afbenchgen/
 */
public abstract class SyntheticDAF extends DungTheory {
    private final GraphType graphType;
    private final Argument[] arguments;

    public SyntheticDAF(GraphType graphType, int noArguments) {
        this.graphType = graphType;
        this.validateNoArgs(noArguments);

        arguments = new Argument[noArguments];

        for (int i = 0; i < noArguments; i++) {
            Argument arg = new Argument("" + i);
            arguments[i] = arg;
            this.add(arg);
        }
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
        for (Edge edge : graph.getEdgeSet()) {
            int fromIndex = edge.getNode0().getIndex();
            int toIndex = edge.getNode1().getIndex();

            Argument from = arguments[fromIndex];
            Argument to = arguments[toIndex];

            this.addAttack(from, to);
        }
    }

    protected int computeStronglyConnectedComponents(Graph graph) {
        TarjanStronglyConnectedComponents cc = new TarjanStronglyConnectedComponents();
        cc.init(graph);
        cc.compute();
        int max = 0;
        for (Node n : graph.getNodeSet()) {
            if ((Integer) n.getAttribute(cc.getSCCIndexAttribute()) > max)
                max = n.getAttribute(cc.getSCCIndexAttribute());
        }
        return max;
    }

    protected boolean edgeBetweenNodes(Graph graph, Node source, Node target) {
        for (Edge e : graph.getEdgeSet()) {
            if (e.getNode0().equals(source) && e.getNode1().equals(target))
                return true;
        }
        return false;
    }
}

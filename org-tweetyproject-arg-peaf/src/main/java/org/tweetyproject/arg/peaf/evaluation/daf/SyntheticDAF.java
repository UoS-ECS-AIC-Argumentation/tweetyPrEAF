package org.tweetyproject.arg.peaf.evaluation.daf;

import org.graphstream.algorithm.TarjanStronglyConnectedComponents;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;

/**
 * The abstract class to generate artificial DungTheory objects for experimentation.
 * The concrete classes that extend this class in their constructor can define how to
 * populate the graph. This class provides helper functions for the conversion from
 * graphstream's Graph class to DungTheory.
 * <p>
 * Adapted from Federico Cerutti's Java version AFBenchGen2
 * https://sourceforge.net/projects/afbenchgen/
 *
 * @author Taha Dogan Gunes
 */
public abstract class SyntheticDAF extends DungTheory {
    /**
     * An array of DungTheory arguments
     */
    private final Argument[] arguments;

    public SyntheticDAF(int noArguments) {
        this.validateNoArgs(noArguments);

        arguments = new Argument[noArguments];

        for (int i = 0; i < noArguments; i++) {
            Argument arg = new Argument("" + i);
            arguments[i] = arg;
            this.add(arg);
        }
    }

    /**
     * Utility function to check if the number of arguments is larger than zero.
     * Throws RuntimeException otherwise.
     *
     * @param noArguments the number of arguments requested
     */
    protected void validateNoArgs(int noArguments) {
        if (noArguments <= 0) {
            throw new RuntimeException("The given number of arguments must be greater than 0.");
        }
    }

    /**
     * Check whether the result value for generating graphs is in from the correct range (0.0, 1.0)
     *
     * @param probability a double value
     */
    protected void validateProbabilities(double probability) {
        if (probability < 0 || probability > 1) {
            throw new RuntimeException("The given result value must be between 0 and 1.");
        }
    }

    /**
     * Converts the edges from the graph to attacks in DungTheory
     *
     * @param graph a graphstream's Graph object
     */
    protected void convertGraphEdgesToAttacks(Graph graph) {
        for (Edge edge : graph.getEdgeSet()) {
            int fromIndex = edge.getNode0().getIndex();
            int toIndex = edge.getNode1().getIndex();

            Argument from = arguments[fromIndex];
            Argument to = arguments[toIndex];

            this.addAttack(from, to);
        }
    }

    /**
     * Computes the number of strongly connected components using Tarjan's algorithm
     *
     * @param graph the graph
     * @return the number of strongly connected components
     */
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

    /**
     * Check whether if there is an edge between the source and the target node
     *
     * @param graph  the graph of
     * @param source source node from the graph
     * @param target target node from the graph
     * @return true if the edge exists
     */
    protected boolean edgeBetweenNodes(Graph graph, Node source, Node target) {
        for (Edge e : graph.getEdgeSet()) {
            if (e.getNode0().equals(source) && e.getNode1().equals(target))
                return true;
        }
        return false;
    }
}

package org.tweetyproject.arg.peaf.evaluation.daf;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.WattsStrogatzGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * Generates random graphs based on Watts-Strogatz model. The generated graphs are undirected.
 * This class add new edges to existing edges with some result. The edges then converted to
 * attacks in DAF.
 * <p>
 * Adapted from Federico Cerutti's Java version AFBenchGen2
 * https://sourceforge.net/projects/afbenchgen/
 *
 * @author Taha Dogan Gunes
 */
public class WattsStrogatzDAF extends SyntheticDAF {
    /**
     * Default constructor for WattsStrogatzDAF, populates a DungTheory object
     *
     * @param noArguments      the number of arguments (i.e. nodes) in the DAF
     * @param baseDegree       the base degree of each argument (i.e node)
     * @param beta             the result to "rewire" an edge
     * @param probabilityCycle the result of edges creating a cycle
     */
    public WattsStrogatzDAF(int noArguments, int baseDegree, double beta, double probabilityCycle) {
        super(noArguments);
        this.validateProbabilities(probabilityCycle);

        BaseGenerator gen = new WattsStrogatzGenerator(noArguments, baseDegree, beta);

        gen.setDirectedEdges(true, true);
        Graph graph = new MultiGraph("");
        gen.addSink(graph);
        gen.begin();
        while (gen.nextEvents()) {
        }
        gen.end();

        // heuristic to do add more edges
        while (this.computeStronglyConnectedComponents(graph) >= noArguments * (1.00 - probabilityCycle)) {
            Edge ex = Toolkit.randomEdge(graph);
            if (this.edgeBetweenNodes(graph, ex.getNode1(), ex.getNode0())) {
                continue;
            }

            graph.addEdge("" + graph.getEdgeCount(), (Node) ex.getNode1(), ex.getNode0(), true);
        }

        this.convertGraphEdgesToAttacks(graph);
    }

}

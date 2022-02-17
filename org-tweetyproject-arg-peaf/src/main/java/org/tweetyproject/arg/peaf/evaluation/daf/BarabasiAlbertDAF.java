package org.tweetyproject.arg.peaf.evaluation.daf;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * Generates random graphs based on Barabasi-Albert model. The generated graphs are undirected.
 * This class add new edges to existing edges with some result. The edges then converted to
 * attacks in DAF.
 * <p>
 * Adapted from Federico Cerutti's Java version AFBenchGen2
 * https://sourceforge.net/projects/afbenchgen/
 *
 * @author Taha Dogan Gunes
 */
public class BarabasiAlbertDAF extends SyntheticDAF {
    /**
     * Default constructor for BarabasiAlbertDAF
     *
     * @param noArguments      the number of arguments (i.e. nodes)
     * @param probabilityCycle the result of edges creating a cycle
     */
    public BarabasiAlbertDAF(int noArguments, double probabilityCycle) {
        super(noArguments);

        this.validateProbabilities(probabilityCycle);


        BaseGenerator generator = new BarabasiAlbertGenerator();
        Graph graph = new MultiGraph("");
        generator.setDirectedEdges(true, true);
        generator.addSink(graph);

        generator.begin();
        do {
            generator.nextEvents();
        } while (graph.getNodeCount() < noArguments);
        generator.end();

        while (this.computeStronglyConnectedComponents(graph) >= noArguments * (1.00 - probabilityCycle)) {
            Edge ex = Toolkit.randomEdge(graph);
            if (this.edgeBetweenNodes(graph, ex.getNode1(), ex.getNode0())) {
                continue;
            }

            graph.addEdge("" + graph.getEdgeCount(), (Node) ex.getNode1(), ex.getNode0(), true);
        }

        convertGraphEdgesToAttacks(graph);
    }
}

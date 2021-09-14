package org.tweetyproject.arg.peaf.evaluation.daf;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.WattsStrogatzGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * Adapted from Federico Cerutti's Java version AFBenchGen2
 * https://sourceforge.net/projects/afbenchgen/
 */
public class WattsStrogatzDAF extends SyntheticDAF {
    public WattsStrogatzDAF(int noArguments, int baseDegree, double beta, double probabilityCycle) {
        super(GraphType.WATTS, noArguments);
        this.validateProbabilities(probabilityCycle);

        BaseGenerator gen = new WattsStrogatzGenerator(noArguments, baseDegree, beta);

        gen.setDirectedEdges(true, true);
        Graph graph = new MultiGraph("");
        gen.addSink(graph);
        gen.begin();
        while (gen.nextEvents())
        {

        }
        gen.end();

        while (this.computeStronglyConnectedComponents(graph) >= noArguments * (1.00 - probabilityCycle))
        {
            Edge ex = Toolkit.randomEdge(graph);
            if (this.edgeBetweenNodes(graph, ex.getNode1(), ex.getNode0()))
            {
                continue;
            }

            graph.addEdge("" + graph.getEdgeCount(), (Node) ex.getNode1(), ex.getNode0(), true);
        }


        this.addEdges(graph);
    }


}

package org.tweetyproject.arg.peaf.evaluation.daf;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * Adapted from Federico Cerutti's Java version AFBenchGen2
 * https://sourceforge.net/projects/afbenchgen/
 */
public class RandomDAF extends SyntheticDAF {
    public RandomDAF(int noArguments, double probability) {
        super(GraphType.RANDOM, noArguments);

        this.validateProbabilities(probability);

        Generator generator = new RandomGenerator(probability * (double) noArguments, true, true);
        Graph graph = new MultiGraph("");
        generator.addSink(graph);

        generator.begin();
        do {
            generator.nextEvents();
        } while (graph.getNodeCount() < noArguments);

        generator.end();
        addEdges(graph);
    }
}

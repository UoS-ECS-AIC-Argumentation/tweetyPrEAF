package org.tweetyproject.arg.peaf.evaluation.daf;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * Uses graphstream's RandomGenerator to create random graphs
 * and converts the structure DungTheory attack and arguments
 * <p>
 * Adapted from Federico Cerutti's Java version AFBenchGen2
 * https://sourceforge.net/projects/afbenchgen/
 *
 * @author Taha Dogan Gunes
 */
public class RandomDAF extends SyntheticDAF {
    /**
     * The default constructor for RandomDAF
     *
     * @param noArguments the number of arguments (i.e nodes)
     * @param probability a double value (this is multiplied with noArguments to for setting average degree)
     */
    public RandomDAF(int noArguments, double probability) {
        super(noArguments);

        this.validateProbabilities(probability);

        Generator generator = new RandomGenerator(probability * (double) noArguments, true, true);
        Graph graph = new MultiGraph("");
        generator.addSink(graph);

        generator.begin();
        do {
            generator.nextEvents();
        } while (graph.getNodeCount() < noArguments);

        generator.end();
        convertGraphEdgesToAttacks(graph);
    }
}

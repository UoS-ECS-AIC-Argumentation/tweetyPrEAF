package org.tweetyproject.arg.peaf.analysis;

import com.google.common.util.concurrent.AtomicDouble;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class implements exact probabilistic justification of a set of queries by generating all possible
 * induces EAFs from a PEAF in parallel.
 *
 * @author Taha Dogan Gunes
 */
public class ConcurrentExactAnalysis extends AbstractAnalysis {
    /**
     * The fixed thread pool to run the contributions in parallel
     */
    private final ExecutorService executorService;

    /**
     * Constructs ConcurrentExactAnalysis with noThreads equal to availableProcessors - 1
     *
     * @param peafTheory        the PEAFTheory to be analyzed
     * @param extensionReasoner the extension reasoner
     */
    public ConcurrentExactAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner) {
        this(peafTheory, extensionReasoner, Runtime.getRuntime().availableProcessors() - 1);
    }

    /**
     * Constructs ConcurrentExactAnalysis with given noThreads
     *
     * @param peafTheory        the PEAFTheory to be analyzed
     * @param extensionReasoner the extension reasoner
     * @param noThreads         the number of threads
     */
    public ConcurrentExactAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner, int noThreads) {
        super(peafTheory, extensionReasoner, AnalysisType.CONCURRENT_EXACT);
        this.executorService = Executors.newFixedThreadPool(noThreads);
    }

    /**
     * Computes exactly what is probabilistic justification of the given set of arguments in the PEAF.
     * <p>
     * Warning: It is intractable when the number of arguments in PEAF is above 15.
     *
     * @param args the set of arguments necessary for the query
     * @return the result of the analysis
     * @see ConcurrentApproxAnalysis for larger PEAFs
     */
    @Override
    public AnalysisResult query(Set<EArgument> args) {
        ExactPEAFInducer exactPEAFInducer = new ExactPEAFInducer(this.peafTheory);

        AtomicLong i = new AtomicLong(0);
        AtomicDouble p = new AtomicDouble(0.0);
        AtomicDouble total = new AtomicDouble(0.0);

        exactPEAFInducer.induce(iEAF ->
                executorService.submit((Callable<Void>) () -> {
                    double contribution = computeContributionOfAniEAF(args, iEAF);
                    total.addAndGet(contribution);
                    p.addAndGet(contribution * iEAF.getInducePro());
                    i.incrementAndGet();
                    return null;
                }));

        try {
            executorService.shutdown();
        } finally {
            try {
                //noinspection ResultOfMethodCallIgnored
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return this.createResult(p.get(), i.get(), total.get());
    }

}

package org.tweetyproject.arg.peaf.analysis;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import org.checkerframework.checker.units.qual.A;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.inducers.ApproxPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ConcurrentApproxAnalysis extends AbstractAnalysis{
    private final double errorLevel;
    private final ExecutorService executorService;
    private final int noThreads;
    private final static int GROUP_SIZE = 100;

    public ConcurrentApproxAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner, double errorLevel) {
        this(peafTheory, extensionReasoner,errorLevel, Runtime.getRuntime().availableProcessors() - 1);
    }

    public ConcurrentApproxAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner, double errorLevel, int noThreads) {
        super(peafTheory, extensionReasoner, AnalysisType.CONCURRENT_APPROX);
        this.errorLevel = errorLevel;
        this.executorService = Executors.newFixedThreadPool(noThreads);
        this.noThreads = noThreads;
    }

    @Override
    public AnalysisResult query(Set<EArgument> args) {

        // tests for cyclic
        new ApproxPEAFInducer(peafTheory);

        final double[] M = {0.0};
        final double[] N = {0.0};
        final double[] metric = {0.0};
        final double[] p_i = {0.0};
        final long[] i = {0};
        AtomicDouble total = new AtomicDouble();
        AtomicInteger poolAvailability = new AtomicInteger(noThreads);
        List<Future<Double>> futures = Lists.newArrayList();


        do {
            // Submit a group of threads
            if (poolAvailability.get() > 0) {
                Future<Double> future = executorService.submit(new Callable<Double>() {
                    @Override
                    public Double call() throws Exception {
                        poolAvailability.decrementAndGet();
                        final double[] contribution = {0.0};
                        ApproxPEAFInducer approxPEAFInducer = new ApproxPEAFInducer(peafTheory);
                        approxPEAFInducer.induce(new Consumer<>() {
                            @Override
                            public void accept(InducibleEAF iEAF) {
                                contribution[0] = computeContributionOfAniEAF(args, iEAF);
                                total.addAndGet(contribution[0]);
                            }});
                        return contribution[0];
                    }
                });
                futures.add(future);
            }

            ListIterator<Future<Double>> iter = futures.listIterator();

            while(iter.hasNext()) {
                Future<Double> future = iter.next();
                if (future.isDone()) {
                    double contribution = 0;
                    try {
                        contribution = future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    M[0] = M[0] + contribution;
                    N[0] = N[0] + 1.0;
                    i[0] += 1;
                    p_i[0] = (M[0] + 2) / (N[0] + 4);
                    metric[0] = ((4.0 * p_i[0] * (1.0 - p_i[0])) / Math.pow(errorLevel, 2)) - 4.0;

                    poolAvailability.incrementAndGet();
                    iter.remove();
                }
            }

        } while (N[0] <= metric[0]);


        // The condition is now satisfied, we don't need to run all the tasks now.
        executorService.shutdownNow();

        return this.createResult(M[0] / N[0], i[0], total.get());
    }
}

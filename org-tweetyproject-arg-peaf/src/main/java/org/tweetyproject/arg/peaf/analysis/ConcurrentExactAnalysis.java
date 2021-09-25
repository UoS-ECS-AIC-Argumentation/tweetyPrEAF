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

public class ConcurrentExactAnalysis extends AbstractAnalysis {
    private final ExecutorService executorService;

    public ConcurrentExactAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner) {
        this(peafTheory, extensionReasoner, Runtime.getRuntime().availableProcessors() - 1);
    }

    public ConcurrentExactAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner, int noThreads) {
        super(peafTheory, extensionReasoner, AnalysisType.CONCURRENT_EXACT);
        this.executorService = Executors.newFixedThreadPool(noThreads);
    }

    @Override
    public AnalysisResult query(Set<EArgument> args) {
        ExactPEAFInducer exactPEAFInducer = new ExactPEAFInducer(this.peafTheory);

        AtomicLong i = new AtomicLong(0);
        AtomicDouble p = new AtomicDouble(0.0);
        AtomicDouble total = new AtomicDouble(0.0);

        exactPEAFInducer.induce(iEAF -> {
            executorService.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    double contribution = computeContributionOfAniEAF(args, iEAF);
                    total.addAndGet(contribution);
                    p.addAndGet(contribution  * iEAF.getInducePro());
                    i.incrementAndGet();
                    return null;
                }
            });
        });

        try {
            executorService.shutdown();
        } finally {
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return this.createResult(p.get(), i.get(), total.get());
    }

}

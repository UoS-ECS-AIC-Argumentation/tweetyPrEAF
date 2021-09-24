package org.tweetyproject.arg.peaf.analysis;


import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.Set;

public class ExactAnalysis<T extends AbstractExtensionReasoner> extends AbstractAnalysis<T> {

    public ExactAnalysis(PEAFTheory peafTheory, T extensionReasoner) {
        super(peafTheory, extensionReasoner);
    }

    @Override
    public AnalysisResult query(Set<EArgument> args) {

        ExactPEAFInducer exactPEAFInducer = new ExactPEAFInducer(this.peafTheory);

        final double[] p = {0.0};
        final long[] i = {0};

        exactPEAFInducer.induce(iEAF -> {
            double contribution = computeContributionOfAniEAF(args, iEAF);
            p[0] += contribution * iEAF.getInducePro();
            i[0] += 1;
        });

        return new AnalysisResult(p[0], i[0]);
    }
}

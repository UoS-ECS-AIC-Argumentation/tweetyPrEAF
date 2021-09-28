package org.tweetyproject.arg.peaf.analysis;


import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.peaf.inducers.ExactPEAFInducer;
import org.tweetyproject.arg.peaf.io.EdgeListWriter;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.Set;

public class ExactAnalysis extends AbstractAnalysis {

    public ExactAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner) {
        super(peafTheory, extensionReasoner, AnalysisType.EXACT);
    }

    @Override
    public AnalysisResult query(Set<EArgument> args) {

        ExactPEAFInducer exactPEAFInducer = new ExactPEAFInducer(this.peafTheory);

        final double[] p = {0.0};
        final double[] total = {0.0};
        final long[] i = {0};

        exactPEAFInducer.induce(iEAF -> {
            double contribution = computeContributionOfAniEAF(args, iEAF);
            p[0] += contribution * iEAF.getInducePro();
            total[0] += iEAF.getInducePro();
            i[0] += 1;
        });

        return this.createResult(p[0], i[0], total[0]);
    }

    public void saveEAFs () {
//        final long[] i = {0};
//        ExactPEAFInducer exactPEAFInducer = new ExactPEAFInducer(this.peafTheory);
//        exactPEAFInducer.induce(iEAF -> {
//            System.out.println(iEAF);
////            String probability = String.format("%.04f", iEAF.getInducePro());
////            probability = probability.replace(".", "_");
////            EdgeListWriter.write("/home/tdgunes/Projects/DrawPrEAF/input/"  + this.analysisType.toString() + "_" + i[0] + "" + "_" + probability + ".eaf", iEAF.toNewEAFTheory());
////            i[0] += 1;
//        });
    }
}

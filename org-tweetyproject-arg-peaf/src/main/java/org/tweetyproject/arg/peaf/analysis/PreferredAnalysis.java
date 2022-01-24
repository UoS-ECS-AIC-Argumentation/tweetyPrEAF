package org.tweetyproject.arg.peaf.analysis;

import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.Collection;
import java.util.Set;

public class PreferredAnalysis extends AbstractAnalysis {
    public PreferredAnalysis(PEAFTheory peaf) {
        super(peaf, new PreferredReasoner(), AnalysisType.PREFERRED);
    }

    @Override
    public AnalysisResult query(Set<EArgument> args) {
        return null;
    }

    public Collection<Extension> getExtensions() {
        // Convert peaf -> eaf -> daf, then run jargsemsat
        EAFTheory eafTheory = EAFTheory.newEAFTheory(peafTheory);
        DungTheory dungTheory = this.createDAF(eafTheory);
        return extensionReasoner.getModels(dungTheory);
    }
 }

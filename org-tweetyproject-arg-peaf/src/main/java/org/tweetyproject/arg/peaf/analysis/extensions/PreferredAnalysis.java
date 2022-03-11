package org.tweetyproject.arg.peaf.analysis.extensions;

import org.tweetyproject.arg.peaf.analysis.AnalysisType;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;

/**
 * Computes the preferred extension of the given PEAF
 * <p>
 * Uses `jargsemsat` for computing extensions.
 *
 * @author Taha Dogan Gunes
 */
public class PreferredAnalysis extends AbstractExtensionAnalysis {
    public PreferredAnalysis(NamedPEAFTheory peaf) {
        super(peaf, new PreferredReasoner(), AnalysisType.PREFERRED);
    }
}

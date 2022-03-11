package org.tweetyproject.arg.peaf.analysis.extensions;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.analysis.AbstractAnalysis;
import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.AnalysisType;
import org.tweetyproject.arg.peaf.io.eaf.EAFToDAFConverter;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class AbstractExtensionAnalysis extends AbstractAnalysis implements ExtensionAnalysis {
    /**
     * The default constructor
     *
     * @param peafTheory        The PEAF Theory
     * @param extensionReasoner The extension reasoner
     * @param analysisType      The type of the analysis
     */
    public AbstractExtensionAnalysis(PEAFTheory peafTheory, AbstractExtensionReasoner extensionReasoner, AnalysisType analysisType) {
        super(peafTheory, extensionReasoner, analysisType);
    }

    @Override
    public List<Set<String>> getExtensions() {
        // Convert peaf -> eaf -> daf, then run jargsemsat
        EAFTheory eafTheory = EAFTheory.newEAFTheory(peafTheory);
        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        Collection<Extension> extensions = extensionReasoner.getModels(dungTheory);

        NamedPEAFTheory namedPEAFTheory = (NamedPEAFTheory) this.peafTheory;
        List<Set<String>> results = Lists.newArrayList();
        for (Extension extension : extensions) {
            Set<String> extensionWithNames = Sets.newHashSet();
            for (Argument argument : extension) {

                String[] argumentNames = argument.getName().split("_");

                for (String argumentName : argumentNames) {
                    EArgument eArgument = namedPEAFTheory.getArguments().get(Integer.parseInt(argumentName));
                    String name = namedPEAFTheory.getNameOfArgument(eArgument);

                    extensionWithNames.add(name);
                }

            }
            results.add(extensionWithNames);
        }

        return results;
    }

    @Override
    public AnalysisResult query(Set<EArgument> args) {
        return null;
    }
}

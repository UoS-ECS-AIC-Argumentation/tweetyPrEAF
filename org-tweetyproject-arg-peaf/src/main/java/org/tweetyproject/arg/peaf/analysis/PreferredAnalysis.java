package org.tweetyproject.arg.peaf.analysis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PreferredAnalysis extends AbstractAnalysis {
    public PreferredAnalysis(NamedPEAFTheory peaf) {
        super(peaf, new PreferredReasoner(), AnalysisType.PREFERRED);
    }

    @Override
    public AnalysisResult query(Set<EArgument> args) {
        return null;
    }

    public List<Set<String>> getExtensions() {
        // Convert peaf -> eaf -> daf, then run jargsemsat
        EAFTheory eafTheory = EAFTheory.newEAFTheory(peafTheory);
        DungTheory dungTheory = this.createDAF(eafTheory);
        Collection<Extension> extensions = extensionReasoner.getModels(dungTheory);

        NamedPEAFTheory namedPEAFTheory = (NamedPEAFTheory) this.peafTheory;
        List<Set<String>> results = Lists.newArrayList();
        for (Extension extension : extensions) {
            Set<String> extensionWithNames = Sets.newHashSet();
            for (Argument argument : extension) {

                EArgument eArgument = namedPEAFTheory.getArguments().get(Integer.parseInt(argument.getName()));
                String name = namedPEAFTheory.getNameOfArgument(eArgument);

                extensionWithNames.add(name);
            }
            results.add(extensionWithNames);
        }

        return results;
    }
 }
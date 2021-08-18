package org.tweetyproject.arg.peaf.analysis;



import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class JustificationAnalysis {

    public static double computeJustificationOf(Set<EArgument> args, PEAFTheory peafTheory, AbstractExtensionReasoner abstractExtensionReasoner) {
        AtomicReference<Double> prob = new AtomicReference<>((double) 0);

        Set<String> stringArgs = new HashSet<>();

        for (EArgument arg : args) {
            stringArgs.add(arg.getName());
        }

        AtomicInteger i = new AtomicInteger();
        peafTheory.induceAll((Consumer<InducibleEAF>) ind -> {
            // If args is in EAFTheory and if X is in an extension of EAFTheory
            // then induce probability can be considered.
            EAFTheory eafTheory = ind.toNewEAFTheory();
            if (eafTheory.getArguments().containsAll(args)) {
                // If the X is in an extension of IEAF

                DungTheory dungTheory = eafTheory.convertToDAFNaively();
                Collection<Extension> extensionCollection = abstractExtensionReasoner.getModels(dungTheory);

                for (Extension extension : extensionCollection) {
                    boolean containsAll = true;
                    for (Argument argument : extension) {
                        String mainArgName = argument.getName();

                        if (mainArgName.contains("_")) {
                            // Must contain all subArgsNames from an extension
                            String[] subArgsNames = mainArgName.split("_");
                            for (String subArgsName : subArgsNames) {
                                containsAll = containsAll && stringArgs.contains(subArgsName);
                            }
                        }
                        else {
                            containsAll = containsAll && stringArgs.contains(mainArgName);
                        }
                    }

                    if (containsAll) {
                        prob.updateAndGet(v -> (v + ind.getInducePro()));
                    }
                }

            }

        });

        return prob.get();
    }
}

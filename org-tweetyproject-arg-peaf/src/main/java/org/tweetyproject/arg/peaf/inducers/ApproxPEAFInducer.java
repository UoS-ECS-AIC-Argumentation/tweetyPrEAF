package org.tweetyproject.arg.peaf.inducers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.peaf.syntax.*;

import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class ApproxPEAFInducer extends AbstractPEAFInducer {
    class EAF_F {
        Set<EArgument> eArguments;
        Set<ESupport> eSupports;
        Set<EArgument> newEArguments;

        public EAF_F(Set<EArgument> eArguments,
                     Set<ESupport> eSupports,
                     Set<EArgument> newEArguments) {
            this.eArguments = eArguments;
            this.eSupports = eSupports;
            this.newEArguments = newEArguments;
        }

        public InducibleEAF convertToInducible() {
            List<PSupport> supportList = Lists.newArrayList();
            for (ESupport eSupport : eSupports) {
                supportList.add((PSupport) eSupport);
            }

            InducibleEAF inducibleEAF = new InducibleEAF(Sets.newHashSet(eArguments),
                                                Sets.newHashSet(supportList),
                                                Sets.newHashSet(),
                                                Sets.newHashSet(),
                                         0, 0);
            // FIXME: This should be disabled.
//            inducibleEAF.arguments.sort(Comparator.comparing(EArgument::getName));
//            inducibleEAF.supports.sort(Comparator.comparing(ESupport::getName));
//            inducibleEAF.attacks.sort(Comparator.comparing(EAttack::getName));

            inducibleEAF.addAttackLinks();

            return inducibleEAF;
        }



        public EAF_F copy() {
            return new EAF_F(this.eArguments, this.eSupports, this.newEArguments);
        }
    }

    public ApproxPEAFInducer(PEAFTheory peafTheory) {
        super(peafTheory);
    }

    @Override
    public void induce(Consumer<InducibleEAF> consumer) {
        Stack<EAF_F> stack = new Stack<>();

        // eta is added, Algorithm 8 Line 2 EAF_F <- {eta}, {}, {}
        stack.push(new EAF_F(Sets.newHashSet(), Sets.newHashSet(peafTheory.getSupports().get(0)), Sets.newHashSet(peafTheory.getArguments().get(0))));

        // Turn recursive random induce to sequential
        while (!stack.isEmpty()) {
            EAF_F eaf = stack.pop();

            // compute expanding supports (ES)
            // if ES.isEmpty
            //     return eaf;
            Set<ESupport> expandingSupports = Sets.newHashSet();
            for (EArgument newEArgument : eaf.newEArguments) {
                // These new arguments have these supports
//                System.out.println(newEArgument.getSupports());
                expandingSupports.addAll(newEArgument.getSupports());
            }
            // these are cleared such that new ones can be added
            //       System.out.println("Add new arguments: " + eaf.newEArguments);
            eaf.eArguments.addAll(eaf.newEArguments);
            eaf.newEArguments.clear();

            if (expandingSupports.isEmpty()) {
                consumer.accept(eaf.convertToInducible());
                return;
            }

            // Line 6-7
            EAF_F eaf_c = eaf.copy();

            // Line 8-14
            for (ESupport eSupport : expandingSupports) {
                double r = ThreadLocalRandom.current().nextDouble();
                if (r <= ((PSupport) eSupport).getConditionalProbability()) {
                    eaf_c.eSupports.add(eSupport);

                    // This is to eliminate visiting same nodes again
                    for (EArgument to : eSupport.getTos()) {
                        if (!eaf_c.eArguments.contains(to)) {
                            eaf_c.newEArguments.add(to);
                        }
                    }
                }
            }

            // Line 15
            // This is omitted since attacks are not supported.

            stack.push(eaf_c);
        }
    }
}

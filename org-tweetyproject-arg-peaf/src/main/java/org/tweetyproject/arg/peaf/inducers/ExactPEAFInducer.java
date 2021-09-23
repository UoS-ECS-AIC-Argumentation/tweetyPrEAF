package org.tweetyproject.arg.peaf.inducers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.peaf.syntax.*;

import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

public class ExactPEAFInducer extends AbstractPEAFInducer{
    class EAF_F {
        Set<EArgument> eArguments;
        Set<ESupport> eSupports;
        Set<EArgument> newEArguments;
        double pi;

        public EAF_F(Set<EArgument> eArguments,
                     Set<ESupport> eSupports,
                     Set<EArgument> newEArguments, double pi) {
            this.eArguments = eArguments;
            this.eSupports = eSupports;
            this.newEArguments = newEArguments;
            this.pi = pi;
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
                    0, Math.log(this.pi));


            LiExactPEAFInducer.addAttackLinks(inducibleEAF);

            return inducibleEAF;
        }

        public EAF_F copy() {
            return new EAF_F(Sets.newHashSet(this.eArguments), Sets.newHashSet(this.eSupports), Sets.newHashSet(this.newEArguments), this.pi);
        }
    }

    public ExactPEAFInducer(PEAFTheory peafTheory) {
        super(peafTheory);
    }

    @Override
    public void induce(Consumer<InducibleEAF> consumer) {
        Stack<EAF_F> stack = new Stack<>();

        // eta is added, Algorithm 8 Line 2 EAF_F <- {eta}, {}, {}
        stack.push(new EAF_F(Sets.newHashSet(), Sets.newHashSet(peafTheory.getSupports().get(0)), Sets.newHashSet(peafTheory.getArguments().get(0)), 1.0));

        while (!stack.isEmpty()) {
            EAF_F eaf = stack.pop();

            double po = 1.0;

            // line 3, page 80
            for (PSupport support : peafTheory.getSupports()) {
                // all the support that are not in iEAF
                if (!eaf.eSupports.contains(support)) {
                    po *= (1 - support.getConditionalProbability());
                }
            }

            System.out.println("po: " + po);
            double npi = eaf.pi;
            System.out.println("eaf pi (before): " + eaf.pi);
            eaf.pi = eaf.pi * po;
            System.out.println("eaf pi (after): " + eaf.pi);


            Set<ESupport> expandingSupports = Sets.newHashSet();
            for (EArgument newEArgument : eaf.newEArguments) {
                expandingSupports.addAll(newEArgument.getSupports());
            }

            eaf.eArguments.addAll(eaf.newEArguments);
            eaf.newEArguments.clear();

            consumer.accept(eaf.convertToInducible());

            for (Set<ESupport> eSupports : Sets.powerSet(expandingSupports)) {

                EAF_F eaf_c = eaf.copy();
                for (ESupport eSupport : eSupports) {

                    eaf_c.eSupports.add(eSupport);
                    eaf_c.newEArguments.addAll(eSupport.getTos());
                    npi *= ((PSupport)  eSupport).getConditionalProbability();
                }
                if (!eSupports.isEmpty()) {
                    eaf_c.pi = npi;
                    stack.push(eaf_c);
                }
            }

        }
    }

}

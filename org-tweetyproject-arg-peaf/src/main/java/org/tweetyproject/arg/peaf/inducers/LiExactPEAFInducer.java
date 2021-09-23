package org.tweetyproject.arg.peaf.inducers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.peaf.syntax.*;

import java.util.*;
import java.util.function.Consumer;

public class LiExactPEAFInducer extends AbstractPEAFInducer {

    public LiExactPEAFInducer(PEAFTheory peafTheory) {
        super(peafTheory);
    }

    public void induce(Consumer<InducibleEAF> consumer) {
        InducibleEAF f = new InducibleEAF(Sets.newHashSet(),
                                          Sets.newHashSet(),
                                          Sets.newHashSet(),
                                          Sets.newHashSet(), Math.log(1.0), Math.log(1.0));

        // Store inducible that need to expand
        List<InducibleEAF> expansion = new ArrayList<>();
        expansion.add(f);
        int z = 0;

        while (!expansion.isEmpty()) {

            InducibleEAF toExpand = expansion.remove(0);
            // Before accepting explore all the attacks and add these links (traverse all the tree)
            addAttackLinks(toExpand);
            consumer.accept(toExpand);


            Map<Set<EArgument>, Map<String, Object>> expandingArgs = expand(toExpand);

            if (expandingArgs.isEmpty()) {
                continue;
            }


            int noCombinations = 1 << expandingArgs.size();

            for (int i = 1; i < noCombinations; i++) {
                List<EArgument> newArgs = new ArrayList<>();
                List<PSupport> supports = new ArrayList<>();

                double pInside = toExpand.getpInside();
                int n = i;

                for (Map.Entry<Set<EArgument>, Map<String, Object>> entry : expandingArgs.entrySet()) {
                    Set<EArgument> argIds = entry.getKey();
                    Map<String, Object> map = entry.getValue();

                    if ((n & 1) == 1) {
                        newArgs.addAll(argIds);
                        supports.addAll((Collection<? extends PSupport>) map.get("supports"));
                        pInside += Math.log(1.0 - Math.exp((double) map.get("pro")));
                    }
                    n = n >> 1;
                }
                List<EArgument> args = new ArrayList<>();
                args.addAll(toExpand.getArguments());
                args.addAll(newArgs);
                supports.addAll(toExpand.getSupports());

                double pOutside = Math.log(1.0);

                for (PSupport sr : this.peafTheory.getSupports()) {
                    if (supports.contains(sr)) {
                        continue;
                    }

                    EArgument notIn = null;

                    for (EArgument fa : sr.getFroms()) {
                        if (!args.contains(fa)) {
                            notIn = fa;
                            break;
                        }
                    }

                    if (notIn == null) {
                        pOutside += Math.log(1.0 - sr.getConditionalProbability());
                    }
                }

                double induceP = pInside + pOutside;
                InducibleEAF indu = new InducibleEAF(Sets.newHashSet(args), Sets.newHashSet(supports), Sets.newHashSet(), Sets.newHashSet(newArgs), pInside, induceP);

                expansion.add(indu);
                z++;
            }

        }

    }

    public static void addAttackLinks(InducibleEAF toExpand) {
        toExpand.attacks.clear();
        Set<EArgument> args = Sets.newHashSet(toExpand.getArguments());

        for (EArgument arg : args) {
            for (EAttack attack : arg.getAttacks()) {
                Set<EArgument> froms = attack.getFroms();
                Set<EArgument> tos = attack.getTos();

                if (args.containsAll(froms) && args.containsAll(tos)) {
                    toExpand.attacks.add(attack);
                }
            }
        }

    }

    private Map<Set<EArgument>, Map<String, Object>> expand(InducibleEAF indu) {
        Map<Set<EArgument>, Map<String, Object>> expandable = new HashMap<>();

        for (PSupport sr : this.peafTheory.getSupports()) {

            // next if indu.supports.include?(sr.id)
            if (indu.getSupports().contains(sr)) {
                continue;
            }

            boolean foundNotIn = false;
            boolean foundNewSup = false;


            //  sr.from.each { |fa|
            //        found_not_in = true if !indu.arguments.include?(fa.id)
            //        break if found_not_in
            //        found_new_sup = true if indu.new_args.include?(fa.id)
            //  }
            for (EArgument fa : sr.getFroms()) {
                if (!indu.getArguments().contains(fa)) {
                    foundNotIn = true;
                    break;
                }
                if (indu.getNewArguments().contains(fa)) {
                    foundNewSup = true;
                }
            }

            //if !found_not_in and (found_new_sup or indu.new_args.empty?)
            //        if expandable.has_key?(sr.to.id)
            //          expandable[sr.to.id][:supports].push(sr.id)
            //          expandable[sr.to.id][:pro] *= (1 - sr.cp)
            //        else
            //          expandable[sr.to.id] = {:supports => [sr.id], :pro => (1 - sr.cp)}
            //        end
            //      end
            if (!foundNotIn && (foundNewSup || indu.getNewArguments().isEmpty())) {
                if (expandable.containsKey(sr.getTos())) {
                    Map<String, Object> map = expandable.get(sr.getTos());

                    List<PSupport> supports = (List<PSupport>) map.get("supports");
                    supports.add(sr);

                    double probability = (double) map.get("pro");
                    map.replace("pro", probability + Math.log(1.0 - sr.getConditionalProbability()));
                } else {
                    Map<String, Object> map = new HashMap<>();
                    List<PSupport> supports = new ArrayList<>();
                    supports.add(sr);
                    map.put("supports", supports);
                    map.put("pro", Math.log(1.0 - sr.getConditionalProbability()));
                    expandable.put(sr.getTos(), map);
                }

            }

        }
        return expandable;
    }
}

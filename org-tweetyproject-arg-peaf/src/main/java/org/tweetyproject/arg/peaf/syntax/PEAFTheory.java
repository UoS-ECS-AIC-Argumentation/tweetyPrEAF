package org.tweetyproject.arg.peaf.syntax;

import java.util.*;
import java.util.function.Consumer;


public class PEAFTheory extends AbstractEAFTheory<PSupport, PAttack> {

    public PEAFTheory() {

    }

    public PEAFTheory(int noArguments) {
        for (int i = 0; i < noArguments; i++) {
            this.addArgument(i);
        }
    }


    private PSupport createSupport(String name, Set<EArgument> froms, Set<EArgument> tos, double cp) {
        PSupport support = new PSupport(name, froms, tos, cp);
        for (EArgument to : tos) {
            to.setSupportedBy(support);
        }
        for (EArgument from : froms) {
            from.setSupports(support);
        }
        return support;
    }

    protected PAttack createAttack(String name, Set<EArgument> froms, Set<EArgument> tos, double cp) {
        if (tos.contains(eta)) {
            throw new RuntimeException("Argument eta can't be attacked.");
        }
        PAttack attack = new PAttack(name, froms, tos, cp);
        for (EArgument from : froms) {
            from.addAttack(attack);
        }
        for (EArgument to : tos) {
            to.addAttackedBy(attack);
        }
        return attack;
    }

    public void addArgument(int identifier) {
        EArgument argument = this.createArgument(Integer.toString(identifier));
        this.addArgument(argument);
    }

    public void addSupport(int[] fromIndices, int[] toIndices, double cp) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = supports.size();
        PSupport support = this.createSupport(Integer.toString(identifier), froms, tos, cp);
        this.addSupport(support);
    }

    public void addAttack(int[] fromIndices, int[] toIndices, double cp) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = supports.size();
        PAttack attack = this.createAttack(Integer.toString(identifier), froms, tos, cp);
        this.addAttack(attack);
    }


    public void induceAll(Consumer<InducibleEAF> consumer) {
        InducibleEAF f = new InducibleEAF(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1.0, 1.0);

        // Store inducible that need to expand
        List<InducibleEAF> expansion = new ArrayList<>();
        expansion.add(f);

        while (!expansion.isEmpty()) {

            InducibleEAF toExpand = expansion.remove(0);
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
                        pInside *= 1.0 - (double) map.get("pro");
                    }
                    n = n >> 1;
                }
                List<EArgument> args = new ArrayList<>();
                args.addAll(toExpand.getArguments());
                args.addAll(newArgs);
                supports.addAll(toExpand.getSupports());

                double pOutside = 1.0;

                for (PSupport sr : this.getSupports()) {
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
                        pOutside *= (1.0 - sr.getConditionalProbability());
                    }
                }

                double induceP = pInside * pOutside;
                InducibleEAF indu = new InducibleEAF(args, supports, newArgs, pInside, induceP);
                expansion.add(indu);
            }

        }
    }

    private Map<Set<EArgument>, Map<String, Object>> expand(InducibleEAF indu) {
        Map<Set<EArgument>, Map<String, Object>> expandable = new HashMap<>();

        for (PSupport sr : this.getSupports()) {

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
            if (!foundNotIn && (foundNewSup || indu.getNewArguments().isEmpty()) ) {
                if (expandable.containsKey(sr.getTos())) {
                    Map<String, Object> map = expandable.get(sr.getTos());

                    List<PSupport> supports = (List<PSupport>) map.get("supports");
                    supports.add(sr);

                    double probability = (double) map.get("pro");
                    map.replace("pro", probability * (1.0 - sr.getConditionalProbability()));
                }
                else {
                    Map<String, Object> map = new HashMap<>();
                    List<PSupport> supports = new ArrayList<>();
                    supports.add(sr);
                    map.put("supports", supports);
                    map.put("pro", 1.0 - sr.getConditionalProbability());
                    expandable.put(sr.getTos(), map);
                }

            }

        }
        return expandable;
    }
}

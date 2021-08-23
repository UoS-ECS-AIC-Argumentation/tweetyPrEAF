package org.tweetyproject.arg.peaf.inducers;

import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.InducibleEAF;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PSupport;

import java.util.*;
import java.util.function.Consumer;

public class SomePEAFInducer extends AbstractPEAFInducer {
    private final Random random;
    private List<InducibleEAF> expansion;
    private InducibleEAF lastExpand;


    public SomePEAFInducer(PEAFTheory peafTheory) {
        super(peafTheory);
        // Uses random seed
        random = new Random();
//        System.out.println("--------- SomePEAFInducer is started!");
    }

    public SomePEAFInducer(PEAFTheory peafTheory, Random random) {
        super(peafTheory);
        this.random = random;
    }

    public void induce(Consumer<InducibleEAF> consumer) {

        InducibleEAF f = new InducibleEAF(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1.0, 1.0);

        // Store inducible that need to expand
        expansion = new ArrayList<>();
        expansion.add(f);



        while (!expansion.isEmpty()) {
            InducibleEAF toExpand = expansion.remove(0);
            lastExpand = toExpand;


            Map<Set<EArgument>, Map<String, Object>> expandingArgs = expand(toExpand);

            if (expandingArgs.isEmpty()) {
                continue;
            }


            List<EArgument> newArgs = new ArrayList<>();
            List<PSupport> supports = new ArrayList<>();

            double pInside = toExpand.getpInside();

            for (Map.Entry<Set<EArgument>, Map<String, Object>> entry : expandingArgs.entrySet()) {
                Set<EArgument> argIds = entry.getKey();
                Map<String, Object> map = entry.getValue();

                double r = random.nextDouble();
                if (r <= (1.0 - (double) map.get("pro"))) {
                    newArgs.addAll(argIds);
                    supports.addAll((Collection<? extends PSupport>) map.get("supports"));
                    pInside *= 1.0 - (double) map.get("pro");
                }

            }
            List<EArgument> args = new ArrayList<>();
            args.addAll(toExpand.getArguments());
            args.addAll(newArgs);
            supports.addAll(toExpand.getSupports());

            double pOutside = 1.0;

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
                    pOutside *= (1.0 - sr.getConditionalProbability());
                }
            }

            double induceP = pInside * pOutside;
            InducibleEAF indu = new InducibleEAF(args, supports, newArgs, pInside, induceP);
            expansion.add(indu);


        }
        consumer.accept(lastExpand);

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

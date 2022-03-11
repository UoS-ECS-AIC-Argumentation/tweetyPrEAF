package org.tweetyproject.arg.peaf.io.eaf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.EAttack;
import org.tweetyproject.arg.peaf.syntax.ESupport;
import org.tweetyproject.commons.util.Pair;

import java.util.*;

public class EAFToDAFConverter {

    public static DungTheory convert(EAFTheory eafTheory) {
        DungTheory dungTheory = new DungTheory();
        // Keeping track of which arguments are discovered
        Set<EArgument> discovered = Sets.newHashSet();
        // Stores which EArgument corresponds to which dafArguments (one-to-many)
        Map<EArgument, Set<Argument>> mapping = Maps.newHashMap();

        EArgument startingPoint = eafTheory.getEta();
        bfsTraverse(dungTheory, startingPoint, discovered, mapping);

        // Once the traversal is finished
        System.out.println(mapping);

        for (EArgument from : eafTheory.getArguments()) {
            for (EAttack attack : from.getAttacks()) {
                Set<EArgument> edgeArguments = Sets.newHashSet();

                findEdgeNodes(attack.getTos(), edgeArguments);

                Set<Argument> dafAttackers = mapping.get(from);


                System.out.println("Edge arguments: " + edgeArguments);
                if (dafAttackers == null) {
                    System.err.println("dafAttackers is null: " + from);
                }

                if (edgeArguments.isEmpty()) {
                    System.err.println("dafAttackeds is null: " + edgeArguments);
                }

                if (edgeArguments.isEmpty() || dafAttackers == null) {
                    return null;
                }


                for (Argument dafAttacker : dafAttackers) {
                    for (EArgument attacked : edgeArguments) {
                        if (mapping.get(attacked) == null) {
                            continue;
                        }
                        for (Argument argument : mapping.get(attacked)) {
                            dungTheory.addAttack(dafAttacker, argument);
                        }
                    }
                }
            }
        }

        System.out.println(dungTheory.prettyPrint());
        return dungTheory;
    }

    private static void findEdgeNodes(Set<EArgument> children, Set<EArgument> edgeArguments) {
        for (EArgument child : children) {
            if (child.getSupports().size() == 0) {
                edgeArguments.add(child);
            } else {
                edgeArguments.add(child);
                for (ESupport support : child.getSupports()) {
                    findEdgeNodes(support.getTos(), edgeArguments);
                }
            }
        }
    }

    private static void bfsTraverse(DungTheory dungTheory, EArgument startingPoint, Set<EArgument> discovered, Map<EArgument, Set<Argument>> mapping) {
        Queue<Pair<EArgument, List<EArgument>>> queue = new LinkedList<>();

        if (discovered.contains(startingPoint)) {
            return;
        }
        discovered.add(startingPoint);
        queue.add(new Pair<>(startingPoint, Lists.newArrayList(startingPoint)));


        while (!queue.isEmpty()) {
            Pair<EArgument, List<EArgument>> pair = queue.poll();
            EArgument v = pair.getFirst();

            System.out.println("h1: " + pair.getSecond());
            if (v.getSupports().size() > 0) {
                for (ESupport support : v.getSupports()) {
                    for (EArgument to : support.getTos()) {
//  FIXME: since the support structure is a tree perhaps we can avoid this
//                        if (!discovered.contains(to)) {
//                            discovered.add(to);
                        List<EArgument> newList = Lists.newArrayList();
                        newList.addAll(pair.getSecond());
                        newList.add(to);
                        queue.add(new Pair<>(to, newList));
//                        }
                    }
                }
            } else {
                addDAFArgument(dungTheory, mapping, pair);
            }

            if (v.getAttacks().size() > 0) {
//                System.out.println("h2: " + pair.getSecond());
                addDAFArgument(dungTheory, mapping, pair);
            }

            if (v.getIncomingAttacks().size() > 0) {
                addDAFArgument(dungTheory, mapping, pair);
            }


        }

    }

    private static void addDAFArgument(DungTheory dungTheory, Map<EArgument, Set<Argument>> mapping, Pair<EArgument, List<EArgument>> pair) {
        List<String> args = Lists.newArrayList();
        for (EArgument eArgument : pair.getSecond()) {
            args.add(eArgument.getName());
        }

        Argument dafArgument = new Argument(String.join("_", args));
        dungTheory.add(dafArgument);

        EArgument eArgument = pair.getSecond().get(pair.getSecond().size() - 1);
        if (!mapping.containsKey(eArgument)) {
            mapping.put(eArgument, Sets.newHashSet());
        }
        mapping.get(eArgument).add(dafArgument);


    }


}

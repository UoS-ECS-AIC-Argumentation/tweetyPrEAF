package org.tweetyproject.arg.peaf.evaluation.converters;

import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EtaToTreeConverter extends DAFToEAFConverter {

    public EAFTheory convert(DungTheory dungTheory, boolean etaFullyConnected, double dependencyDegree) {
        EAFTheory eafTheory = super.convert(dungTheory);
        // At this point, eta is not connected to any other argument

        List<Integer> interNodes = new ArrayList<>();
        Set<Integer> S = new HashSet<>();
        // Skip eta in S
        for (int i = 1; i < eafTheory.getNumberOfNodes(); i++) {
            S.add(i);
        }
        Integer s = 1; // skipping eta here

        while (!S.isEmpty()) {
            double r = ThreadLocalRandom.current().nextDouble();
            // If r is larger than dependencyDegree ignore the s, and get a new s from S
            if (r > dependencyDegree) {
                S.remove(s);

                s = this.getAnElementFromSet(S);
                // Even if s is null the while loop will terminate
                continue;
            }

            // If r is smaller or equal to dependencyDegree
            S.remove(s);
            Integer sPrime = getAnElementFromSet(S);
            if (sPrime == null) {
                break;
            }
            S.remove(sPrime);

            int alphaIndex = eafTheory.getNumberOfNodes();
            eafTheory.addArgument(alphaIndex);
            interNodes.add(alphaIndex);

            eafTheory.addSupport(new int[]{alphaIndex}, new int[]{s});
            eafTheory.addSupport(new int[]{alphaIndex}, new int[]{sPrime});

            s = alphaIndex;
        }

        if (etaFullyConnected) {
            // All other arguments are connected
            for (int i = 1; i < eafTheory.getNumberOfNodes(); i++) {
                eafTheory.addSupport(new int[]{0}, new int[]{i});
            }
        } else {
            if (!interNodes.isEmpty()) {
                eafTheory.addSupport(new int[]{0}, new int[]{interNodes.get(interNodes.size() - 1)});
            }
            // If there is no interNodes and eta not fully connected
            else {
                if (eafTheory.getNumberOfNodes() > 1) {
                    // connect to one argument to get a valid eaf
                    eafTheory.addSupport(new int[]{0}, new int[]{1});
                }
            }
        }

        return eafTheory;
    }

    public <T> T getAnElementFromSet(Set<T> set) {
        for (T t : set) {
            return t;
        }
        return null;
    }
}

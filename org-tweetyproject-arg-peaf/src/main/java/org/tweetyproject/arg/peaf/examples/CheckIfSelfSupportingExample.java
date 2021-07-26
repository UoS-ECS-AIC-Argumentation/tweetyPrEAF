package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckIfSelfSupportingExample {

    public static void main(String[] args) {
        EAFTheory eafTheory = new EAFTheory(4);

        // eta = 0
        // a = 1
        // b = 2
        // c = 3

        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addAttack(new int[]{2}, new int[]{1});
        eafTheory.addSupport(new int[]{1}, new int[]{3});


        List<EArgument> eArguments = eafTheory.getArguments();

        Set<EArgument> selfSupported = new HashSet<>();
        selfSupported.add(eArguments.get(0));

        System.out.println("{eta} is " + eafTheory.checkIsSelfSupporting(selfSupported) + ", but must be true");
        selfSupported.clear();

        selfSupported.add(eArguments.get(2));
        System.out.println("{b} is " + eafTheory.checkIsSelfSupporting(selfSupported) + ", but must be false");
        selfSupported.clear();

        selfSupported.add(eArguments.get(1));
        selfSupported.add(eArguments.get(2));
        System.out.println("{a, b} is " + eafTheory.checkIsSelfSupporting(selfSupported) + ", but must be false");
        selfSupported.clear();

        selfSupported.add(eArguments.get(0));
        selfSupported.add(eArguments.get(1));
        selfSupported.add(eArguments.get(3));
        System.out.println("{eta, a, c} is " + eafTheory.checkIsSelfSupporting(selfSupported) + ", but must be true");
        selfSupported.clear();

        selfSupported.add(eArguments.get(0));
        selfSupported.add(eArguments.get(2));
        System.out.println("{eta, b} is " + eafTheory.checkIsSelfSupporting(selfSupported) + ", but must be true");

        selfSupported.add(eArguments.get(0));
        selfSupported.add(eArguments.get(3));
        System.out.println("{eta, c} is " + eafTheory.checkIsSelfSupporting(selfSupported) + ", but must be false");

    }
}

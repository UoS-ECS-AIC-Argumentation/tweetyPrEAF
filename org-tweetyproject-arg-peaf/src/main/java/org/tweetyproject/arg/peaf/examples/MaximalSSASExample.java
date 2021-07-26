package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.peaf.syntax.EAFTheory;

public class MaximalSSASExample {

    public static void main(String[] args) {
        EAFTheory eafTheory = new EAFTheory(4);


        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{1}, new int[]{2});
        eafTheory.addSupport(new int[]{2}, new int[]{3});

        System.out.println(eafTheory.convertToDAFWithMaximalSSAS());
    }
}

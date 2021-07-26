package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.dung.reasoner.SimplePreferredReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;

public class OrenEtAl2010Figure2Example {
    public static void main(String[] args) {
        EAFTheory eafTheory = new EAFTheory(4);


        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addAttack(new int[]{2}, new int[]{1});
        eafTheory.addSupport(new int[]{1}, new int[]{3});

        DungTheory dungTheory = eafTheory.convertToDAFNaively();

        System.out.println("EAF Theory Pretty Print:");
        eafTheory.prettyPrint();

        System.out.println("DAF Theory Pretty Print:");
        System.out.println(dungTheory.prettyPrint());

        System.out.println("SimplePreferredReasoner:");
        SimplePreferredReasoner reasoner1 = new SimplePreferredReasoner();
        for (Extension model : reasoner1.getModels(dungTheory)) {
            System.out.println(model);
        }
    }
}

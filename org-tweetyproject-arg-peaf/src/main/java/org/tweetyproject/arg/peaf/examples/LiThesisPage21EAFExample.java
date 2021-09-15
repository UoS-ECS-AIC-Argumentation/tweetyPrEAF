package org.tweetyproject.arg.peaf.examples;

import org.tweetyproject.arg.dung.reasoner.SimplePreferredReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;

public class LiThesisPage21EAFExample {

    public static void main(String[] args) {
        EAFTheory eafTheory = new EAFTheory(9);

        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{0}, new int[]{3});
        eafTheory.addSupport(new int[]{0}, new int[]{4});
        eafTheory.addSupport(new int[]{3, 4}, new int[]{7});
        eafTheory.addSupport(new int[]{1}, new int[]{5});
        eafTheory.addSupport(new int[]{5}, new int[]{8});
        eafTheory.addSupport(new int[]{2}, new int[]{6});
        eafTheory.addSupport(new int[]{6}, new int[]{8});

        eafTheory.addAttack(new int[]{2}, new int[]{5});
        eafTheory.addAttack(new int[]{6}, new int[]{3});


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

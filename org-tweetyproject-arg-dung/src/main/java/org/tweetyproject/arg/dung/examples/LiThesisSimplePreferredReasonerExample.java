package org.tweetyproject.arg.dung.examples;

import org.tweetyproject.arg.dung.reasoner.SimplePreferredReasoner;
import org.tweetyproject.arg.dung.reasoner.StratifiedLabelingReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.semantics.Semantics;
import org.tweetyproject.arg.dung.semantics.StratifiedLabeling;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;

public class LiThesisSimplePreferredReasonerExample {

    public static void main(String[] args) {
        /*
            - Li's Thesis Page 17 (Technical Foundations)
         */
        DungTheory theory = new DungTheory();

        Argument a_1 = new Argument("a_1");
        Argument a_2 = new Argument("a_2");
        Argument a_3 = new Argument("a_3");
        Argument a_4 = new Argument("a_4");
        Argument a_5 = new Argument("a_5");
        Argument a_6 = new Argument("a_6");

        theory.add(a_1);
        theory.add(a_2);
        theory.add(a_3);
        theory.add(a_4);
        theory.add(a_5);
        theory.add(a_6);

        theory.addAttack(a_1, a_2);
        theory.addAttack(a_2, a_3);
        theory.addAttack(a_3, a_4);
        theory.addAttack(a_4, a_1);
        theory.addAttack(a_6, a_5);

//        StratifiedLabelingReasoner reasoner = new StratifiedLabelingReasoner(Semantics.PREFERRED_SEMANTICS);
//
//        for(StratifiedLabeling labeling: reasoner.getModels(theory)){
//            System.out.println(labeling);
//        }

        SimplePreferredReasoner reasoner1 = new SimplePreferredReasoner();

        for (Extension model : reasoner1.getModels(theory)) {
            System.out.println(model);
        }


    }
}

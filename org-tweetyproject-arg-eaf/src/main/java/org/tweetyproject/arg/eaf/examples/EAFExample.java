package org.tweetyproject.arg.eaf.examples;

import org.tweetyproject.arg.eaf.syntax.Argument;
import org.tweetyproject.arg.eaf.syntax.Attack;
import org.tweetyproject.arg.eaf.syntax.EAFTheory;

public class EAFExample {

    public static void main(String[] _args) {
        int numOfArgs = 8;


        EAFTheory eafTheory = new EAFTheory();

        for (int i = 0; i < numOfArgs; i++) {
            eafTheory.addArgument(i);
        }

        eafTheory.addAttack(5, 4);
        eafTheory.addAttack(2, 6);






    }
}

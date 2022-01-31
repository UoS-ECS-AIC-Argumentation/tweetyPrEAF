package org.tweetyproject.arg.peaf;

import org.junit.Assert;
import org.junit.Test;
import org.tweetyproject.arg.dung.reasoner.SimplePreferredReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.ExactAnalysis;
import org.tweetyproject.arg.peaf.evaluation.converters.EAFToPEAFConverter;
import org.tweetyproject.arg.peaf.evaluation.converters.EtaToAllConverter;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

import java.util.HashSet;
import java.util.Set;


public class EtaToAllConverterTest {

    @Test
    public void testSimple() {
        DungTheory dungTheory = new DungTheory();

        Argument a_1 = new Argument("a_1");
        Argument a_2 = new Argument("a_2");

        dungTheory.add(a_1);
        dungTheory.add(a_2);

        dungTheory.addAttack(a_1, a_2);

        System.out.println("DAF:");
        System.out.println(dungTheory.prettyPrint());

        System.out.println("EAF:");
        EtaToAllConverter eafConverter = new EtaToAllConverter();
        EAFTheory eafTheory = eafConverter.convert(dungTheory);

        eafTheory.prettyPrint();
        Assert.assertEquals(3, eafTheory.getNumberOfArguments());
    }


    @Test
    public void testPipeline() {
        DungTheory dungTheory = new DungTheory();

        Argument a_1 = new Argument("1");
        Argument a_2 = new Argument("2");

        dungTheory.add(a_1);
        dungTheory.add(a_2);

        dungTheory.addAttack(a_1, a_2);

        System.out.println("DAF:");
        System.out.println(dungTheory.prettyPrint());

        SimplePreferredReasoner reasoner = new SimplePreferredReasoner();
        Extension queryExtension = null;
        for (Extension model : reasoner.getModels(dungTheory)) {
            System.out.println(model);
            queryExtension = model;
            break;
        }

        System.out.println("EAF:");
        EtaToAllConverter eafConverter = new EtaToAllConverter();
        EAFTheory eafTheory = eafConverter.convert(dungTheory);

        eafTheory.prettyPrint();
        Assert.assertEquals(3, eafTheory.getNumberOfArguments());


        PEAFTheory peafTheory = EAFToPEAFConverter.convert(eafTheory, 10, 2);
        peafTheory.prettyPrint();

        if (queryExtension != null) {
            Set<EArgument> query = new HashSet<>();
            for (Argument argument : queryExtension) {
                System.out.println("Arg: "+argument.getName());
                query.add(peafTheory.getArguments().get(Integer.parseInt(argument.getName())));
            }

            System.out.println("Query is: " + query);
            ExactAnalysis approxAnalysis = new ExactAnalysis(peafTheory, new PreferredReasoner());
            AnalysisResult result = approxAnalysis.query(query);
            double p = result.getProbability();
            result.print();
        }

    }
}

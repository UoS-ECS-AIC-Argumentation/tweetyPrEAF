package org.tweetyproject.arg.peaf;

import org.junit.Assert;
import org.junit.Test;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.evaluation.converters.EtaToAllConverter;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;


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
        Assert.assertEquals(3, eafTheory.getNumberOfNodes());
    }
}

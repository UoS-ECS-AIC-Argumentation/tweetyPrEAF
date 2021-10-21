package org.tweetyproject.arg.peaf;

import org.junit.Assert;
import org.junit.Test;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.evaluation.converters.EtaToTreeConverter;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.io.preaf.EdgeListWriter;

public class EtaToTreeConverterTest {

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
        EtaToTreeConverter eafConverter = new EtaToTreeConverter();
        EAFTheory eafTheory = eafConverter.convert(dungTheory, false, 1.0);


        eafTheory.prettyPrint();
        Assert.assertEquals(4, eafTheory.getNumberOfNodes());
        EdgeListWriter.write("2.eaf", eafTheory);
    }

    @Test
    public void testTwoAllConnected() {
        DungTheory dungTheory = new DungTheory();

        Argument a_1 = new Argument("a_1");
        Argument a_2 = new Argument("a_2");

        dungTheory.add(a_1);
        dungTheory.add(a_2);

        dungTheory.addAttack(a_1, a_2);
        dungTheory.addAttack(a_2, a_1);

        System.out.println("DAF:");
        System.out.println(dungTheory.prettyPrint());

        System.out.println("EAF:");
        EtaToTreeConverter eafConverter = new EtaToTreeConverter();
        EAFTheory eafTheory = eafConverter.convert(dungTheory, true, 1.0);


        eafTheory.prettyPrint();
        Assert.assertEquals(4, eafTheory.getNumberOfNodes());
        EdgeListWriter.write("2_all.eaf", eafTheory);
    }

    @Test
    public void testThreeArguments() {
        DungTheory dungTheory = new DungTheory();

        Argument a_1 = new Argument("a_1");
        Argument a_2 = new Argument("a_2");
        Argument a_3 = new Argument("a_3");

        dungTheory.add(a_1);
        dungTheory.add(a_2);
        dungTheory.add(a_3);

        dungTheory.addAttack(a_1, a_2);
        dungTheory.addAttack(a_2, a_3);

        System.out.println("DAF:");
        System.out.println(dungTheory.prettyPrint());

        EtaToTreeConverter eafConverter = new EtaToTreeConverter();
        EAFTheory eafTheory = eafConverter.convert(dungTheory, false, 1.0);
        EdgeListWriter.write("3.eaf", eafTheory);
    }

    @Test
    public void testThreeArgumentsFully() {
        DungTheory dungTheory = new DungTheory();

        Argument a_1 = new Argument("a_1");
        Argument a_2 = new Argument("a_2");
        Argument a_3 = new Argument("a_3");

        dungTheory.add(a_1);
        dungTheory.add(a_2);
        dungTheory.add(a_3);

        dungTheory.addAttack(a_1, a_2);
        dungTheory.addAttack(a_2, a_3);
        dungTheory.addAttack(a_3, a_1);
        dungTheory.addAttack(a_1, a_3);

        System.out.println("DAF:");
        System.out.println(dungTheory.prettyPrint());

        EtaToTreeConverter eafConverter = new EtaToTreeConverter();
        EAFTheory eafTheory = eafConverter.convert(dungTheory, true, 1.0);
        EdgeListWriter.write("3_all.eaf", eafTheory);
    }

    @Test
    public void testThreeArgumentsHalfDegree() {
        DungTheory dungTheory = new DungTheory();

        Argument a_1 = new Argument("a_1");
        Argument a_2 = new Argument("a_2");
        Argument a_3 = new Argument("a_3");

        dungTheory.add(a_1);
        dungTheory.add(a_2);
        dungTheory.add(a_3);

        dungTheory.addAttack(a_1, a_2);
        dungTheory.addAttack(a_2, a_3);

        System.out.println("DAF:");
        System.out.println(dungTheory.prettyPrint());

        EtaToTreeConverter eafConverter = new EtaToTreeConverter();
        EAFTheory eafTheory = eafConverter.convert(dungTheory, false, 0.5);
        EdgeListWriter.write("3_half.eaf", eafTheory);
    }

    @Test
    public void testThreeArgumentsZeroDegree() {
        DungTheory dungTheory = new DungTheory();

        Argument a_1 = new Argument("a_1");
        Argument a_2 = new Argument("a_2");
        Argument a_3 = new Argument("a_3");

        dungTheory.add(a_1);
        dungTheory.add(a_2);
        dungTheory.add(a_3);

        dungTheory.addAttack(a_1, a_2);
        dungTheory.addAttack(a_2, a_3);

        System.out.println("DAF:");
        System.out.println(dungTheory.prettyPrint());

        EtaToTreeConverter eafConverter = new EtaToTreeConverter();
        EAFTheory eafTheory = eafConverter.convert(dungTheory, false, 0.0);
        EdgeListWriter.write("3_zero.eaf", eafTheory);
    }

}

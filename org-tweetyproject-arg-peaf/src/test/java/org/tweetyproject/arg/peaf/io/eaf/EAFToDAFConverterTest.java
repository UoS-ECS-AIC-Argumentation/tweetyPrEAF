package org.tweetyproject.arg.peaf.io.eaf;

import com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;

import java.util.Set;

/**
 * Please refer to `EAFtoDAF_Fixed_2.pdf` in the resources folder for illustrations of each test case.
 *
 * @author Taha Dogan Gunes
 */
public class EAFToDAFConverterTest extends TestCase {

    private void checkAttacks(Set<Attack> attackSet, Set<String> expectations) {
        System.out.println("Expectations: " + expectations);
        for (Attack attack : attackSet) {
            Argument attacker = attack.getAttacker();
            Argument attacked = attack.getAttacked();
            String expectation = attacker.getName() + "->" + attacked.getName();
            System.out.println("DAF Attack: " + expectation);
            assertTrue("Checks if the attacks are correctly labelled.", expectations.contains(expectation));
            expectations.remove(expectation);
        }

        System.out.println("Remaining attacks: " + expectations);
        assertEquals("All expected attacks are not in the generated DAF.", 0, expectations.size());
    }

    public void testConvert1() {
        EAFTheory eafTheory = new EAFTheory(4);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{1}, new int[]{2});
        eafTheory.addSupport(new int[]{1}, new int[]{3});
        eafTheory.addAttack(new int[]{3}, new int[]{2});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());

        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have two arguments", 2, dungTheory.getNodes().size());
        assertEquals("Must have one attack.", 1, dungTheory.getAttacks().size());
        Set<Attack> attackSet = dungTheory.getAttacks();
        Attack attack = (Attack) attackSet.toArray()[0];
        Argument attacker = attack.getAttacker();
        Argument attacked = attack.getAttacked();
        assertEquals("Must have correct attacker name.", "0_1_3", attacker.getName());
        assertEquals("Must have correct attacked name.", "0_1_2", attacked.getName());
    }

    public void testConvert2() {
        EAFTheory eafTheory = new EAFTheory(4);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addAttack(new int[]{1}, new int[]{2});
        eafTheory.addSupport(new int[]{2}, new int[]{3});


        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have three arguments", 3, dungTheory.getNodes().size());

        Set<Attack> attackSet = dungTheory.getAttacks();
        Set<String> expectations = Sets.newHashSet("0_1->0_2_3", "0_1->0_2");

        checkAttacks(attackSet, expectations);
    }

    public void testConvert3() {
        EAFTheory eafTheory = new EAFTheory(6);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{0}, new int[]{3});
        eafTheory.addAttack(new int[]{1}, new int[]{2});
        eafTheory.addAttack(new int[]{1}, new int[]{3});
        eafTheory.addAttack(new int[]{2}, new int[]{3});
        eafTheory.addSupport(new int[]{2}, new int[]{5});
        eafTheory.addSupport(new int[]{3}, new int[]{4});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have five arguments", 5, dungTheory.getNodes().size());

        Set<Attack> attackSet = dungTheory.getAttacks();
        Set<String> expectations = Sets.newHashSet("0_1->0_2",
                "0_1->0_3",
                "0_2->0_3",
                "0_1->0_2_5",
                "0_1->0_3_4",
                "0_2->0_3_4");

        checkAttacks(attackSet, expectations);
    }

    public void testConvert4() {
        EAFTheory eafTheory = new EAFTheory(6);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{3});
        eafTheory.addSupport(new int[]{0}, new int[]{4});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addAttack(new int[]{1}, new int[]{3});
        eafTheory.addAttack(new int[]{3}, new int[]{4});
        eafTheory.addSupport(new int[]{4}, new int[]{5});
        eafTheory.addAttack(new int[]{5}, new int[]{2});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have four arguments", 5, dungTheory.getNodes().size());

        Set<Attack> attackSet = dungTheory.getAttacks();

        Set<String> expectations = Sets.newHashSet("0_1->0_3", "0_3->0_4", "0_4_5->0_2", "0_3->0_4_5");

        checkAttacks(attackSet, expectations);
    }

    public void testConvert5() {
        EAFTheory eafTheory = new EAFTheory(4);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{1}, new int[]{2});
        eafTheory.addSupport(new int[]{1}, new int[]{3});


        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have two arguments", 2, dungTheory.getNodes().size());
    }

    public void testConvert6() {
        EAFTheory eafTheory = new EAFTheory(4);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{0}, new int[]{3});

        eafTheory.addAttack(new int[]{1}, new int[]{2});
        eafTheory.addAttack(new int[]{1}, new int[]{3});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have three arguments", 3, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1->0_3", "0_1->0_2");

        Set<Attack> attackSet = dungTheory.getAttacks();

        checkAttacks(attackSet, expectations);
    }

    public void testConvert7() {
        EAFTheory eafTheory = new EAFTheory(5);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{0}, new int[]{3});
        eafTheory.addSupport(new int[]{0}, new int[]{4});

        eafTheory.addAttack(new int[]{1}, new int[]{4});
        eafTheory.addAttack(new int[]{4}, new int[]{1});
        eafTheory.addAttack(new int[]{1}, new int[]{2});
        eafTheory.addAttack(new int[]{2}, new int[]{1});
        eafTheory.addAttack(new int[]{2}, new int[]{3});
        eafTheory.addAttack(new int[]{3}, new int[]{2});
        eafTheory.addAttack(new int[]{4}, new int[]{3});
        eafTheory.addAttack(new int[]{3}, new int[]{4});


        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have four arguments", 4, dungTheory.getNodes().size());

        // FIXME: all arguments must have 0 but in this instance they dont.
        Set<String> expectations = Sets.newHashSet("0_2->0_1",
                "0_3->0_2",
                "0_3->0_4",
                "0_1->0_2",
                "0_1->0_4",
                "0_2->0_3",
                "0_4->0_1",
                "0_4->0_3");

        Set<Attack> attackSet = dungTheory.getAttacks();

        checkAttacks(attackSet, expectations);

    }

    public void testConvert8() {
        // FIXME: The Argument 3 may not be created.

        EAFTheory eafTheory = new EAFTheory(7);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{0}, new int[]{3});

        eafTheory.addSupport(new int[]{1}, new int[]{4});
        eafTheory.addSupport(new int[]{2}, new int[]{5});
        eafTheory.addSupport(new int[]{3}, new int[]{6});

        eafTheory.addAttack(new int[]{3}, new int[]{2});
        eafTheory.addAttack(new int[]{4}, new int[]{6});


        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have five arguments", 5, dungTheory.getNodes().size());

        Set<Attack> attackSet = dungTheory.getAttacks();
        Set<String> expectations = Sets.newHashSet("0_3->0_2", "0_1_4->0_3_6", "0_3->0_2_5");


        checkAttacks(attackSet, expectations);
    }


    public void testConvert9() {
        EAFTheory eafTheory = new EAFTheory(4);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{1}, new int[]{2});
        eafTheory.addSupport(new int[]{2}, new int[]{3});
        eafTheory.addAttack(new int[]{1}, new int[]{3});


        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have two arguments", 2, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1->0_1_2_3");
        Set<Attack> attackSet = dungTheory.getAttacks();


        checkAttacks(attackSet, expectations);
    }

    public void testConvert10() {
        EAFTheory eafTheory = new EAFTheory(4);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{1}, new int[]{2});
        eafTheory.addSupport(new int[]{1}, new int[]{3});
        eafTheory.addAttack(new int[]{1}, new int[]{2});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have three arguments", 3, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1->0_1_2");

        Set<Attack> attackSet = dungTheory.getAttacks();

        checkAttacks(attackSet, expectations);
    }

    public void testConvert11() {
        EAFTheory eafTheory = new EAFTheory(3);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addAttack(new int[]{1}, new int[]{2});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have two arguments", 2, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1->0_2");

        Set<Attack> attackSet = dungTheory.getAttacks();

        checkAttacks(attackSet, expectations);
    }

    public void testConvert12() {
        EAFTheory eafTheory = new EAFTheory(4);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{3});
        eafTheory.addSupport(new int[]{1}, new int[]{2});
        eafTheory.addAttack(new int[]{2}, new int[]{3});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have two arguments", 2, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1_2->0_3");

        Set<Attack> attackSet = dungTheory.getAttacks();


        checkAttacks(attackSet, expectations);
    }

    public void testConvert13() {
        EAFTheory eafTheory = new EAFTheory(4);
        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{1}, new int[]{2});
        eafTheory.addAttack(new int[]{1}, new int[]{2});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have two arguments", 2, dungTheory.getNodes().size());
        Set<String> expectations = Sets.newHashSet("0_1->0_1_2");

        Set<Attack> attackSet = dungTheory.getAttacks();

        checkAttacks(attackSet, expectations);
    }

    public void testConvert14() {
        EAFTheory eafTheory = new EAFTheory(9);

        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{1}, new int[]{5});
        eafTheory.addSupport(new int[]{1}, new int[]{6});

        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{2}, new int[]{7});
        eafTheory.addSupport(new int[]{2}, new int[]{8});


        eafTheory.addSupport(new int[]{0}, new int[]{3});
        eafTheory.addSupport(new int[]{0}, new int[]{4});

        eafTheory.addAttack(new int[]{5}, new int[]{6});
        eafTheory.addAttack(new int[]{8}, new int[]{4});
        eafTheory.addAttack(new int[]{4}, new int[]{7});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have one argument", 6, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1_5->0_1_6", "0_4->0_2_7", "0_2_8->0_4");
        Set<Attack> attackSet = dungTheory.getAttacks();


        checkAttacks(attackSet, expectations);
    }

    public void testConvert15() {
        EAFTheory eafTheory = new EAFTheory(8);

        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{4});
        eafTheory.addSupport(new int[]{0}, new int[]{5});
        eafTheory.addSupport(new int[]{1}, new int[]{2});
        eafTheory.addSupport(new int[]{1}, new int[]{3});

        eafTheory.addAttack(new int[]{2}, new int[]{4});
        eafTheory.addAttack(new int[]{3}, new int[]{4});
        eafTheory.addAttack(new int[]{2}, new int[]{5});
        eafTheory.addAttack(new int[]{3}, new int[]{5});

        eafTheory.addSupport(new int[]{4}, new int[]{6});
        eafTheory.addAttack(new int[]{5}, new int[]{6});
        eafTheory.addSupport(new int[]{5}, new int[]{7});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have six arguments", 6, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1_2->0_4",
                "0_1_2->0_5",
                "0_1_3->0_4",
                "0_1_3->0_5",
                "0_5->0_4_6",
                "0_1_2->0_5_7",
                "0_1_3->0_4_6",
                "0_1_3->0_5_7",
                "0_1_2->0_4_6");

        Set<Attack> attackSet = dungTheory.getAttacks();


        checkAttacks(attackSet, expectations);
    }

    public void testConvert16() {
        EAFTheory eafTheory = new EAFTheory(6);

        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{3});
        eafTheory.addSupport(new int[]{0}, new int[]{4});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{4}, new int[]{5});

        eafTheory.addAttack(new int[]{1}, new int[]{3});
        eafTheory.addAttack(new int[]{2}, new int[]{3});
        eafTheory.addAttack(new int[]{3}, new int[]{4});
        eafTheory.addAttack(new int[]{4}, new int[]{1});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have five arguments", 5, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1->0_3", "0_2->0_3", "0_4->0_1", "0_3->0_4", "0_3->0_4_5");

        assertEquals("Must have five attacks.", expectations.size(), dungTheory.getAttacks().size());

        Set<Attack> attackSet = dungTheory.getAttacks();

        checkAttacks(attackSet, expectations);
    }

    public void testConvert17() {
        EAFTheory eafTheory = new EAFTheory(7);

        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addAttack(new int[]{1}, new int[]{2});
        eafTheory.addSupport(new int[]{2}, new int[]{3});
        eafTheory.addSupport(new int[]{3}, new int[]{5});
        eafTheory.addSupport(new int[]{2}, new int[]{4});
        eafTheory.addSupport(new int[]{2}, new int[]{6});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);
        assertEquals("Must have five arguments", 5, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1->0_2", "0_1->0_2_3_5", "0_1->0_2_4", "0_1->0_2_6");

        Set<Attack> attackSet = dungTheory.getAttacks();


        checkAttacks(attackSet, expectations);
    }

    public void testConvert18() {
        EAFTheory eafTheory = new EAFTheory(6);

        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{2}, new int[]{5});
        eafTheory.addSupport(new int[]{0}, new int[]{3});
        eafTheory.addSupport(new int[]{3}, new int[]{4});

        eafTheory.addAttack(new int[]{3}, new int[]{2});
        eafTheory.addAttack(new int[]{4}, new int[]{5});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);

        assertEquals("Must have five arguments", 5, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_3->0_2", "0_3->0_2_5", "0_3_4->0_2_5");

        Set<Attack> attackSet = dungTheory.getAttacks();
        checkAttacks(attackSet, expectations);
    }

    public void testConvert19() {
        EAFTheory eafTheory = new EAFTheory(7);

        eafTheory.addSupport(new int[]{}, new int[]{0});
        eafTheory.addSupport(new int[]{0}, new int[]{1});
        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{2}, new int[]{3});
        eafTheory.addSupport(new int[]{2}, new int[]{4});
        eafTheory.addSupport(new int[]{2}, new int[]{5});
        eafTheory.addSupport(new int[]{2}, new int[]{6});
        eafTheory.addAttack(new int[]{1}, new int[]{2});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);

        assertEquals("Must have five arguments", 6, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_1->0_2_3",
                "0_1->0_2_4",
                "0_1->0_2_5",
                "0_1->0_2_5",
                "0_1->0_2_6",
                "0_1->0_2");

        Set<Attack> attackSet = dungTheory.getAttacks();
        checkAttacks(attackSet, expectations);

    }

    public void testConvert24436() {
        EAFTheory eafTheory = new EAFTheory(8);

        eafTheory.addSupport(new int[]{}, new int[]{0});

        eafTheory.addSupport(new int[]{0}, new int[]{4});
        eafTheory.addSupport(new int[]{4}, new int[]{3});

        eafTheory.addSupport(new int[]{0}, new int[]{2});
        eafTheory.addSupport(new int[]{0}, new int[]{7});

        eafTheory.addSupport(new int[]{4}, new int[]{3});
        eafTheory.addSupport(new int[]{2}, new int[]{5});
        eafTheory.addSupport(new int[]{7}, new int[]{6});
        eafTheory.addSupport(new int[]{5}, new int[]{1});
        eafTheory.addSupport(new int[]{6}, new int[]{1});

        eafTheory.addAttack(new int[]{1}, new int[]{3});
        eafTheory.addAttack(new int[]{3}, new int[]{1});

        DungTheory dungTheory = EAFToDAFConverter.convert(eafTheory);
        System.out.println(dungTheory.prettyPrint());
        assertNotNull("DungTheory must be not null.", dungTheory);

        assertEquals("Must have two arguments", 3, dungTheory.getNodes().size());

        Set<String> expectations = Sets.newHashSet("0_4_3->0_2_5_1",
                "0_2_5_1->0_4_3",
                "0_4_3->0_7_6_1",
                "0_7_6_1->0_4_3");

        Set<Attack> attackSet = dungTheory.getAttacks();
        checkAttacks(attackSet, expectations);
    }
}
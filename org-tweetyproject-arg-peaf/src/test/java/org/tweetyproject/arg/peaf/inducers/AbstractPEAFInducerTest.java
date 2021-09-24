package org.tweetyproject.arg.peaf.inducers;

import org.junit.Test;
import org.tweetyproject.arg.peaf.syntax.CyclicException;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;

public class AbstractPEAFInducerTest {

    @Test(expected = CyclicException.class)
    public void testSimpleCyclic() {
        //        ┌─┐
        //        │0│
        //        └┬┘
        //        ┌▽┐
        //        │1│<+
        //        └┬┘ |
        //        ┌▽──┐
        //        │1-2│
        //        └───┘

        PEAFTheory peafTheory = new PEAFTheory(8);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 0.9);
        peafTheory.addSupport(new int[]{1}, new int[]{1,2}, 0.8);

        peafTheory.printASCIITree();
        new ExactPEAFInducer(peafTheory);
    }

    @Test(expected = CyclicException.class)
    public void testLargeCyclic() {
        //        ┌─┐
        //        │0│
        //        └┬┘
        //        ┌▽┐
        //        │1│ <+
        //        └┬┘  |
        //        ┌▽┐  |
        //        │2│  |
        //        └┬┘  |
        //        ┌▽┐  |
        //        │3│  |
        //        └┬┘  |
        //        ┌▽┐  |
        //        │4│  |
        //        └┬┘  |
        //        ┌▽┐  |
        //        │5│  |
        //        └┬┘  |
        //        ┌▽┐  |
        //        │6│  |
        //        └┬┘  |
        //        ┌▽┐  |
        //        │7│ -+
        //        └─┘


        PEAFTheory peafTheory = new PEAFTheory(8);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 1.0);
        peafTheory.addSupport(1, 2, 1.0);
        peafTheory.addSupport(2, 3, 1.0);
        peafTheory.addSupport(3, 4, 1.0);
        peafTheory.addSupport(4, 5, 1.0);
        peafTheory.addSupport(5, 6, 1.0);
        peafTheory.addSupport(6, 7, 1.0);
        peafTheory.addSupport(7, 1, 1.0);

        peafTheory.printASCIITree();
        new ExactPEAFInducer(peafTheory);
    }

    @Test(expected = CyclicException.class)
    public void testComplexCyclic() {
        //        ┌─┐┌───┐
        //     +> │0││5  │
        //     |  └┬┘└──┬┘
        //     |  ┌▽───┐│
        //     |  │1   ││
        //     |  └┬──┬┘│
        //     |  ┌▽┐┌▽┐│
        //     |  │2││3││
        //     |  └┬┘└┬┘│
        //     |  ┌▽──▽┐│
        //     |  │4   ││
        //     |  └┬───┘│
        //     |  ┌▽────▽┐
        //     +- │6     │
        //        └──────┘


        PEAFTheory peafTheory = new PEAFTheory(7);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 1.0);
        peafTheory.addSupport(1, 2, 1.0);
        peafTheory.addSupport(1, 3, 1.0);
        peafTheory.addSupport(3, 4, 1.0);
        peafTheory.addSupport(2, 4, 1.0);
        peafTheory.addSupport(5, 6, 1.0);
        peafTheory.addSupport(4, 6, 1.0);
        peafTheory.addSupport(6, 1, 1.0);

        peafTheory.printASCIITree();
        new ExactPEAFInducer(peafTheory);
    }

    @Test(expected = CyclicException.class)
    public void testComplexCyclic2() {
        //        ┌─┐┌───┐
        //        │0││5  │<+
        //        └┬┘└──┬┘ │
        //        ┌▽───┐│  │
        //        │1   ││  │
        //        └┬──┬┘│  │
        //        ┌▽┐┌▽┐│  │
        //        │2││3││  │
        //        └┬┘└┬┘│  │
        //        ┌▽──▽┐│  │
        //        │4   ││  │
        //        └┬───┘│  │
        //        ┌▽────▽┐ │
        //        │6     │-+
        //        └──────┘

        PEAFTheory peafTheory = new PEAFTheory(7);

        peafTheory.addSupport(new int[]{}, new int[]{0}, 1.0);
        peafTheory.addSupport(0, 1, 1.0);
        peafTheory.addSupport(1, 2, 1.0);
        peafTheory.addSupport(1, 3, 1.0);
        peafTheory.addSupport(3, 4, 1.0);
        peafTheory.addSupport(2, 4, 1.0);
        peafTheory.addSupport(5, 6, 1.0);
        peafTheory.addSupport(4, 6, 1.0);
        peafTheory.addSupport(6, 5, 1.0);

        peafTheory.printASCIITree();
        new ExactPEAFInducer(peafTheory);
    }
}

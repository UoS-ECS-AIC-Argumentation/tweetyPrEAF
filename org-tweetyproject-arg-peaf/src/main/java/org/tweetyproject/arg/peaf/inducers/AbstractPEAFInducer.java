package org.tweetyproject.arg.peaf.inducers;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.tweetyproject.arg.peaf.syntax.*;

import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

public abstract class AbstractPEAFInducer implements PEAFInducer {

    protected PEAFTheory peafTheory;

    public AbstractPEAFInducer(PEAFTheory peafTheory) {
        this.peafTheory = peafTheory;

        if (isSupportLinksCyclic()){
            throw new CyclicException("This PEAF can not be induced, because a cyclic was found.");
        }
    }

    private boolean isSupportLinksCyclic() {
        boolean isCyclic = false;

        Set<EArgument> visited = Sets.newHashSet();
        // iterative DFS to find a cyclic in the support links
        Stack<EArgument> stack = new Stack<EArgument>();
        stack.push(this.peafTheory.getEta());
        while(!stack.isEmpty()) {
            EArgument node = stack.pop();

            if (!visited.contains(node)) {
                visited.add(node);

                for (ESupport support : node.getSupports()) {
                    for (EArgument to : support.getTos()) {
                        stack.push(to);
                    }
                }
            }
            else {
                isCyclic = true;
                break;
            }
        }


        return isCyclic;
    }

    public void induceNTimes(Consumer<InducibleEAF> consumer, int n) {
        while (n > 0 ) {
            this.induce(consumer);
            n--;
        }
    }

    public PEAFTheory getPeafTheory() {
        return peafTheory;
    }
}

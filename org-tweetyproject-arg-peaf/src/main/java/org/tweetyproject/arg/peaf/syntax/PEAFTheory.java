package org.tweetyproject.arg.peaf.syntax;

import java.util.*;

public class PEAFTheory extends AbstractEAFTheory<PSupport, EAttack> {

    public PEAFTheory() {

    }

    public PEAFTheory(int noArguments) {
        for (int i = 0; i < noArguments; i++) {
            this.addArgument(i);
        }
    }

    private PSupport createSupport(String name, Set<EArgument> froms, Set<EArgument> tos, double cp) {
        PSupport support = new PSupport(name, froms, tos, cp);
        for (EArgument to : tos) {
            to.setSupportedBy(support);
        }
        for (EArgument from : froms) {
            from.setSupports(support);
        }
        return support;
    }

    protected EAttack createAttack(String name, Set<EArgument> froms, Set<EArgument> tos) {
        if (tos.contains(eta)) {
            throw new RuntimeException("Argument eta can't be attacked.");
        }
        EAttack attack = new EAttack(name, froms, tos);
        for (EArgument from : froms) {
            from.addAttack(attack);
        }
        for (EArgument to : tos) {
            to.addAttackedBy(attack);
        }
        return attack;
    }


    public EArgument addArgument(int identifier) {
        EArgument argument = this.createArgument(Integer.toString(identifier));
        this.addArgument(argument);
        return argument;
    }

    public void addSupport(int fromIndex, int toIndex, double cp) {
        this.addSupport(new int[]{fromIndex}, new int[]{toIndex}, cp);
    }

    public void addSupport(int[] fromIndices, int[] toIndices, double cp) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = supports.size();
        PSupport support = this.createSupport(Integer.toString(identifier), froms, tos, cp);
        this.addSupport(support);
    }

    public void addAttack(int[] fromIndices, int[] toIndices) {
        Set<EArgument> froms = createEmptyArgSet(fromIndices);
        Set<EArgument> tos = createEmptyArgSet(toIndices);

        int identifier = attacks.size();
        EAttack attack = this.createAttack(Integer.toString(identifier), froms, tos);
        this.addAttack(attack);
    }

    public void addAttack(int fromIndex, int toIndex) {
        this.addAttack(new int[]{fromIndex}, new int[]{toIndex});
    }

    public Set<EArgument> getRandomArguments(Random random) {
        int nTimes = random.nextInt(this.getArguments().size());
        return getRandomArguments(random, nTimes);
    }

    public Set<EArgument> getRandomArguments(Random random, int nTimes) {
        List<EArgument> randomList = new ArrayList<>(this.getArguments());
        Set<EArgument> set = new HashSet<>();

        for (int i = 0; i < nTimes; i++) {
            Collections.shuffle(randomList, random);
            set.add(randomList.get(0));
            randomList.remove(0);
        }
        return set;
    }

}

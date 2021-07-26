package org.tweetyproject.arg.peaf.syntax;

import java.util.List;

public class InducibleEAF {
    private final List<EArgument> arguments;
    private final List<PSupport> supports;
    private final List<EArgument> newArguments;
    private final double pInside;
    private final double inducePro;

    public InducibleEAF(List<EArgument> arguments,
                        List<PSupport> supports,
                        List<EArgument> newArguments,
                        double pInside, double inducePro) {

        this.arguments = arguments;
        this.supports = supports;
        this.newArguments = newArguments;
        this.pInside = pInside;
        this.inducePro = inducePro;
    }

    public List<EArgument> getArguments() {
        return arguments;
    }

    public List<PSupport> getSupports() {
        return supports;
    }

    public List<EArgument> getNewArguments() {
        return newArguments;
    }

    public double getpInside() {
        return pInside;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("InducibleEAF{");
        builder.append("arguments=[");
        int i = 0;
        for (EArgument argument : arguments) {
            builder.append(argument.getName());
            if (i != arguments.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("], supports=[");
        i = 0;
        for (PSupport support : supports) {
            builder.append(support.getName());
            if (i != arguments.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("] induce probability=");
        builder.append(inducePro);
        builder.append("}");


        return builder.toString();
    }
}

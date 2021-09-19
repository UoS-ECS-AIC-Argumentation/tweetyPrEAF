package org.tweetyproject.arg.peaf.io;

import org.tweetyproject.arg.peaf.syntax.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.StringJoiner;


public class EdgeListWriter {

    public static void write(String path, PEAFTheory peafTheory) {
        try {
            FileWriter writer = new FileWriter(path);

            write(peafTheory, writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void write(String path, PEAFTheory peafTheory, Set<EArgument> query) {
        try {
            FileWriter writer = new FileWriter(path);

            writer.write("# Query: ");
            boolean first = true;
            for (EArgument argument : query) {
                if (first) {
                    first = false;
                }
                else {
                    writer.write(" ");
                }
                writer.write(argument.getName());
            }
            writer.write(System.lineSeparator());

            write(peafTheory, writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String path, EAFTheory eafTheory) {
        try {
            FileWriter writer = new FileWriter(path);

            for (ESupport o : eafTheory.getSupports()) {
                StringBuilder builder = getStringBuilder(o.getFroms(), o.getTos());
                if (builder != null) {
                    builder.append(" {'color': 'green'}");
                    writer.write(builder.toString());
                    writer.write(System.lineSeparator());
                }
            }


            for (EAttack o : eafTheory.getAttacks()) {
                StringBuilder builder = getStringBuilder(o.getFroms(), o.getTos());
                if (builder != null) {
                    builder.append(" {'color': 'red'}");
                    writer.write(builder.toString());
                    writer.write(System.lineSeparator());
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder getStringBuilder(Set<EArgument> froms, Set<EArgument> tos) {
        StringBuilder builder = new StringBuilder();
        StringJoiner joiner = new StringJoiner("-");
        for (EArgument from : froms) {
            joiner.add(from.getName());
        }
        if (froms.isEmpty()) {
            joiner.add("E");
            return null;
        }
        builder.append(joiner);
        builder.append(" ");
        joiner = new StringJoiner("-");
        for (EArgument from : tos) {
            joiner.add(from.getName());
        }
        builder.append(joiner);
        return builder;
    }

    private static void write(PEAFTheory peafTheory, FileWriter writer) throws IOException {
        for (PSupport o : peafTheory.getSupports()) {
            StringBuilder builder = getStringBuilder(o.getFroms(), o.getTos());
            if (builder != null) {
                builder.append(" {'color': 'green', 'weight': " + o.getConditionalProbability() + "  }");
                writer.write(builder.toString());
                writer.write(System.lineSeparator());
            }
        }


        for (EAttack o : peafTheory.getAttacks()) {
            StringBuilder builder = getStringBuilder(o.getFroms(), o.getTos());
            if (builder != null) {
                builder.append(" {'color': 'red'  }");
                writer.write(builder.toString());
                writer.write(System.lineSeparator());
            }
        }
    }
}

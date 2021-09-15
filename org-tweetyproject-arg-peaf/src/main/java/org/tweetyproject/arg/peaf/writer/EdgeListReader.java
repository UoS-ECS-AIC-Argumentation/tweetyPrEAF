package org.tweetyproject.arg.peaf.writer;

import org.tweetyproject.arg.peaf.syntax.PEAFTheory;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class EdgeListReader {

    public static PEAFTheory readPEAF(String filePath) throws IOException {
        return EdgeListReader.readPEAF(filePath, false);
    }

    public static PEAFTheory readPEAF(String filePath, boolean printLines) throws IOException {
        // FIXME: This regex pattern requires the json attachment to be in same order as the writer
        // FIXME: This can be avoided by having a proper JSON parser for the last part of each line
        Matcher m = Pattern.compile("(.+) (.+) +.*'color' *: *'(.+)' *, *'weight' *: *(.+) *}").matcher("");

        // First pass to learn how many arguments available
        // FIXME: This can be avoided by loading PEAF dynamically
        final int[] maxArgumentNo = {0};
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.flatMap(line -> m.reset(line).results())
                    .forEach(new Consumer<MatchResult>() {
                        @Override
                        public void accept(MatchResult matchResult) {
                            String fromIndicesString = matchResult.group(1);
                            String toIndicesString = matchResult.group(2);
                            String color = matchResult.group(3);
                            String weight = matchResult.group(4);

                            int[] fromIndices = EdgeListReader.parseIndices(fromIndicesString);
                            int[] toIndices = EdgeListReader.parseIndices(toIndicesString);

                            for (int fromIndex : fromIndices) {
                                if (fromIndex > maxArgumentNo[0]) {
                                    maxArgumentNo[0] = fromIndex;
                                }
                            }

                            for (int toIndex : toIndices) {
                                if (toIndex > maxArgumentNo[0]) {
                                    maxArgumentNo[0] = toIndex;
                                }
                            }

                            if (printLines) {
                                System.out.println(fromIndicesString + "-" + toIndicesString + "-" + color + "-" + weight);
                            }
                        }
                    });
        }

        // Number of arguments include zero (as eta), therefore plus one here
        PEAFTheory peaf = new PEAFTheory(maxArgumentNo[0] + 1);

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.flatMap(line -> m.reset(line).results())
                    .forEach(new Consumer<MatchResult>() {
                        @Override
                        public void accept(MatchResult matchResult) {
                            String fromIndicesString = matchResult.group(1);
                            String toIndicesString = matchResult.group(2);
                            String color = matchResult.group(3);
                            double probability = Double.parseDouble(matchResult.group(4));

                            // FIXME: only supports PEAFs with index names for the time being
                            int[] fromIndices = EdgeListReader.parseIndices(fromIndicesString);
                            int[] toIndices = EdgeListReader.parseIndices(toIndicesString);

                            if (color.equals("green")) {
                                peaf.addSupport(fromIndices, toIndices, probability);
                            } else if (color.equals("red")) {
                                peaf.addAttack(fromIndices, toIndices, probability);
                            } else {
                                System.out.println("The edge category must be 'green' or 'red'.");
                            }

                        }
                    });
        }

        return peaf;
    }

    private static int[] parseIndices(String indicesString) {
        String[] parts = indicesString.split("-");
        int[] indices = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            // If the character E is found, this means the whole set (can be returned as empty array)
            if (!parts[i].equals("E")) {
                indices[i] = Integer.parseInt(parts[i]);
            }
        }

        return indices;
    }

    public static void main(String[] args) throws IOException {
        PEAFTheory peafTheory = EdgeListReader.readPEAF("./evaluation/barabasi/5/5.peaf", true);

        peafTheory.prettyPrint();
    }
}

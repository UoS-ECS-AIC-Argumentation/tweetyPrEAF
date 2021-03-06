package org.tweetyproject.arg.peaf.io.preaf;

import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.PEAFTheory;
import org.tweetyproject.commons.util.Pair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * EdgeListReader is a utility class for testing purposes to read networkx compatible EdgeList file format.
 * <p>
 * The more details can be found here: https://networkx.org/documentation/stable/reference/readwrite/edgelist.html
 * <p>
 * The only addition is the first commented line that has the queried arguments of the given custom graph, PEAF.
 * Examples of this file format can be found in the `resources` folder. The file extension is `.peaf`.
 * <p>
 * An example of a PEAF file without a query is:
 * <p>
 * ```
 * 0 1 { 'color': 'green', 'weight': 0.8148851406241553 }
 * 0 2 { 'color': 'green', 'weight': 0.9428105506184434 }
 * 0 3 { 'color': 'green', 'weight': 0.8569966496275188 }
 * 1 2 { 'color': 'red'}
 * 1 6 { 'color': 'red'}
 * 1 7 { 'color': 'red'}
 * ```
 * <p>
 * Color green denotes the support link and the weight is the result given to PSupport.
 * Color red denotes the attack link.
 * This is useful to get plots of the PEAF directly from networkx library.
 */
public class EdgeListReader {
    private static final String QUERY_LINE = "# Query: ";

    /**
     * Utility method to read a PEAF in edgelist file format (.peaf) which does not print the read lines
     *
     * @param filePath the file path of the edgelist file
     * @return a PEAFTheory object
     * @throws IOException if the file was not read properly
     */
    public static PEAFTheory readPEAF(String filePath) throws IOException {
        return EdgeListReader.readPEAF(filePath, false);
    }

    /**
     * Read a PEAF in edgelist file format that has a query (.peaf)
     *
     * @param filePath   the file path of the edgelist file
     * @param printLines true if the user wants to see the read lines
     * @return a pair that contains the PEAFTheory object and the set of arguments
     * @throws IOException if the file was not read properly
     */
    public static Pair<PEAFTheory, Set<EArgument>> readPEAFWithQuery(String filePath, boolean printLines) throws IOException {
        // FIXME: We assume first line is the query line commented as in an EdgeList comment with #
        FileInputStream fs = new FileInputStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        String firstLine = br.readLine();
        if (printLines) {
            System.out.println(firstLine);
        }
        Set<String> stringQueryArgs = new HashSet<>();
        if (firstLine.startsWith(QUERY_LINE)) {
            String tokens = firstLine.substring(QUERY_LINE.length());
            StringTokenizer tokenizer = new StringTokenizer(tokens, " ");

            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                stringQueryArgs.add(token);
            }
        } else {
            br.close();
            fs.close();
            throw new RuntimeException("File (" + filePath + ") does not contain query line.");
        }
        br.close();
        fs.close();

        PEAFTheory peafTheory = readPEAF(filePath, printLines);
        Set<EArgument> eArguments = new HashSet<>();

        for (String stringQueryArg : stringQueryArgs) {
            // FIXME: Queries only are allowed within integer indices format if query part, for example
            // FIXME: # Query: 1 0 9 4
            try {
                eArguments.add(peafTheory.getArguments().get((Integer.parseInt(stringQueryArg))));
            } catch (IndexOutOfBoundsException exception) {
                peafTheory.prettyPrint();

                throw exception;
            }

        }

        return new Pair<>(peafTheory, eArguments);
    }

    /**
     * Reads a PEAFTheory in edgelist format (.peaf) and prints the lines if desired
     *
     * @param filePath   the file path of the edgelist file
     * @param printLines true if the user wants to see the read lines
     * @return a PEAFTheory object
     * @throws IOException if the file was not read properly
     */
    public static PEAFTheory readPEAF(String filePath, boolean printLines) throws IOException {
        // FIXME: This regex pattern requires the json attachment to be in same order as the writer
        // FIXME: This can be avoided by having a proper JSON parser for the last part of each line
        Matcher m = Pattern.compile("(.+) (.+) +.*'color' *: *'(.+)' *(?:(?:, *'weight' *: *(.+) *})|(?:}))").matcher("");

        // This can match the following:
        // 0 1 {'color': 'green',          'weight': 0.8148851406241553  }
        // 0 2 {'color': 'green', 'weight': 0.9428105506184434  }
        // 0 3 {'color': 'green', 'weight':           0.8569966496275188  }
        // 1 2 {'color': 'red'}
        // 1 6 {'color': 'red'}
        // 1 7 {'color': 'red'}

        // First pass to learn how many arguments available
        // FIXME: This can be avoided by loading PEAF dynamically
        final int[] maxArgumentNo = {0};
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.flatMap(line -> {
                        return m.reset(line).results();
                    })
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

        // This is important for making inducers work properly.
        peaf.addSupport(new int[]{}, new int[]{0}, 1.0);

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.flatMap(line -> m.reset(line).results())
                    .forEach(new Consumer<MatchResult>() {
                        @Override
                        public void accept(MatchResult matchResult) {
                            String fromIndicesString = matchResult.group(1);
                            String toIndicesString = matchResult.group(2);
                            String color = matchResult.group(3);

                            // FIXME: only supports PEAFs with index names for the time being
                            int[] fromIndices = EdgeListReader.parseIndices(fromIndicesString);
                            int[] toIndices = EdgeListReader.parseIndices(toIndicesString);

                            if (color.equals("green")) {
                                double probability = Double.parseDouble(matchResult.group(4));
                                peaf.addSupport(fromIndices, toIndices, probability);
                            } else if (color.equals("red")) {
                                peaf.addAttack(fromIndices, toIndices);
                            } else {
                                System.out.println("The edge category must be 'green' or 'red'.");
                            }

                        }
                    });
        }

        return peaf;
    }

    /**
     * Utility function to parse the grouped indices to represent a set of arguments support or attack
     * another set of arguments
     *
     * @param indicesString indices in string format (e.g. 1_0_1)
     * @return integer array of the indices
     */
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
}

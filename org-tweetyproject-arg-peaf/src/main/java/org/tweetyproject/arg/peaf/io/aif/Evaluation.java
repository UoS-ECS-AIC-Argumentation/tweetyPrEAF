package org.tweetyproject.arg.peaf.io.aif;

import com.google.gson.internal.LinkedTreeMap;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFTheoryReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Evaluation {
    private final static String PRB1 = "Sorry, the input was corrupted, please try again.";
    private final static String PRB2 = "Sorry, there is no action in your input, please try again.";
    private final static String PRB3 = "Sorry, action not recognised, please try again.";

    private static String setSomethingWrongResponse(String string, JsonHelper jsh) {
        HashMap map = new HashMap();
        map.put("fail", true);
        map.put("cause", string);
        String ersResponse = jsh.convertInputJson(map);
        return ersResponse;
    }

    public static String evaluate(String input) {
        JsonHelper jsh = new JsonHelper();
        String output = null;

        LinkedTreeMap graph = jsh.convertInputMapG(input);
        if (graph.isEmpty()) {
            output = setSomethingWrongResponse(PRB1, jsh);
        } else {
            ERSControl ersWork = new ERSControl(true, null);

            System.out.println("ERS: Evaluating JSON Graph...");
            //change the naming of the links to ensure that current CIS format still works
            graph = AIFDBConverter.convertToCIS(graph);
            output = ersWork.evaluateJsonString(graph);

            System.out.println("ERS: Evaluation is completed.");
        }

        //System.out.println(output);
        return output;

    }

    private static final ClassLoader loader = PEEAFTheoryReader.class.getClassLoader();

    public static void main(String[] args) throws IOException {
        String pathString = loader.getResource("aif/6.json").getPath();
        Path path = Paths.get(pathString);
        String input = Files.readString(path);
        System.out.println(input);

        String output = Evaluation.evaluate(input);

        System.out.println(output);
    }
}

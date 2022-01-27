package org.tweetyproject.arg.peaf.io.cis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.tweetyproject.arg.peaf.io.aif.AIFCISReader;
import org.tweetyproject.arg.peaf.syntax.aif.AIFJSONTheory;
import org.tweetyproject.arg.peaf.syntax.aif.AIFTheory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * CIStoAIFWriter, converts CISpaces json files to AIF file format
 *
 * This only changes source and target of CISpaces to fromID to toID.
 *
 * @author Taha Dogan Gunes
 */
public class CIStoAIFWriter {

    private static final ClassLoader loader = CIStoAIFWriter.class.getClassLoader();
    /**
     * The path of the CISpaces file to be written.
     */
    private final String inputPath;

    /**
     * CIStoAIFWriter's default constructor that takes inputPath
     * @param inputPath the path of the cis file
     */
    public CIStoAIFWriter(String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * Writes the CIS file into AIF format
     *
     * @param outputPath string
     * @throws IOException if writing to a file fails
     */
    public void write(String outputPath) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(inputPath));

        AIFJSONTheory aifJSON = gson.fromJson(reader, AIFJSONTheory.class);

        gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(outputPath);
        gson.toJson(aifJSON, writer);
        writer.close();
    }

    /**
     * @param pathString the path of the cis file
     * @return AIFTheory object
     * @throws FileNotFoundException if given cis file is not found
     */
    public AIFTheory convert(String pathString) throws FileNotFoundException {
        AIFCISReader aifAndCISReader = new AIFCISReader(pathString);
        return aifAndCISReader.read();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        String name = "NEO.json";
        String path = loader.getResource("cis/" + name).getPath();

        CIStoAIFWriter writer = new CIStoAIFWriter(path);
        writer.write("aif-"+name);

    }
}

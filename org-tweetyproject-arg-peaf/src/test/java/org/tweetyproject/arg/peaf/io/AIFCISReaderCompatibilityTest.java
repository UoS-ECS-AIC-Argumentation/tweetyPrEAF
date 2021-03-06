package org.tweetyproject.arg.peaf.io;

import com.google.common.collect.Lists;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.tweetyproject.arg.peaf.io.aif.AIFCISReader;
import org.tweetyproject.arg.peaf.io.aif.AIFtoPEEAFConverter;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFToPEAFConverter;
import org.tweetyproject.arg.peaf.syntax.*;
import org.tweetyproject.arg.peaf.syntax.aif.AIFTheory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class AIFCISReaderCompatibilityTest {
    @BeforeClass
    public static void setup() {

    }

    @AfterClass
    public static void finish() {
        long exceptionCount = 0;
        exceptionCount += PEEAFTheory.Exceptions.describe();
        exceptionCount += AIFtoPEEAFConverter.Exceptions.describe();
        exceptionCount += AIFCISReader.Exceptions.describe();
        System.out.println("Exception count: " + exceptionCount);
    }

    @Parameters
    public static Collection<Object[]> getFiles() {
        Collection<Object[]> params = Lists.newArrayList();
        params.add(new Object[]{""});
        Path rootDirectory = Paths.get("/Users/tdgunes/Projects/Serapis/aif/json/");
        File[] aifs = rootDirectory.toFile().listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".json.gz");
            }
        });
        Arrays.sort(aifs);

        for (File aif : aifs) {
            Object[] arr = new Object[]{aif.getAbsolutePath()};
            params.add(arr);
        }

        return params;
    }


    private String absolutePath;

    public AIFCISReaderCompatibilityTest(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Test
    public void testAIFReader() throws FileNotFoundException {
        if (absolutePath.equals("")){
            return;
        }
        System.out.println("Testing the file: " + absolutePath);
        AIFCISReader reader = new AIFCISReader(this.absolutePath);
        AIFTheory theory = reader.read();
        AIFtoPEEAFConverter converter = new AIFtoPEEAFConverter();
        PEEAFTheory peeafTheory = converter.convert(theory);
        peeafTheory.prettyPrint();
        PEEAFToPEAFConverter converter2 = new PEEAFToPEAFConverter();
        NamedPEAFTheory peafTheory = converter2.convert(peeafTheory);


    }

}

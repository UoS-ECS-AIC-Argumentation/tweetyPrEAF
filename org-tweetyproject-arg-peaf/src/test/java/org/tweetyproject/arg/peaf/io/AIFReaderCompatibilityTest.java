package org.tweetyproject.arg.peaf.io;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.tweetyproject.arg.peaf.io.aif.AIFReader;
import org.tweetyproject.arg.peaf.syntax.aif.AIFTheory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class AIFReaderCompatibilityTest {

    @Parameters
    public static Collection<Object[]> getFiles() {
        Collection<Object[]> params = Lists.newArrayList();
        params.add(new Object[]{""});
        Path rootDirectory = Paths.get("/Users/tdgunes/temp/aif_downloader/downloads");
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

    public AIFReaderCompatibilityTest(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Test
    public void testAIFReader() throws FileNotFoundException {
        if (absolutePath.equals("")){
            return;
        }
        AIFReader reader = new AIFReader(this.absolutePath);
        AIFTheory theory = reader.read();
    }

}

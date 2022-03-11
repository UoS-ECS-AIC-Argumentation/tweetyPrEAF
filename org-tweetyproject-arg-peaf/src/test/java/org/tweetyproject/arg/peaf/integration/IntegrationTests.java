package org.tweetyproject.arg.peaf.integration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.Assert;
import org.junit.Test;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.peaf.analysis.AbstractAnalysis;
import org.tweetyproject.arg.peaf.analysis.AnalysisResult;
import org.tweetyproject.arg.peaf.analysis.ExactAnalysis;
import org.tweetyproject.arg.peaf.analysis.extensions.PreferredAnalysis;
import org.tweetyproject.arg.peaf.analysis.voi.MaximiseChangeAnalysis;
import org.tweetyproject.arg.peaf.inducers.jargsemsat.tweety.PreferredReasoner;
import org.tweetyproject.arg.peaf.io.PEAFToEAFConverter;
import org.tweetyproject.arg.peaf.io.aif.AIFCISReader;
import org.tweetyproject.arg.peaf.io.aif.AIFtoPEEAFConverter;
import org.tweetyproject.arg.peaf.io.eaf.EAFToDAFConverter;
import org.tweetyproject.arg.peaf.io.preeaf.PEEAFToPEAFConverter;
import org.tweetyproject.arg.peaf.syntax.EAFTheory;
import org.tweetyproject.arg.peaf.syntax.EArgument;
import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEEAFTheory;
import org.tweetyproject.arg.peaf.syntax.aif.AIFJSONTheory;
import org.tweetyproject.arg.peaf.syntax.aif.AIFTheory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

/**
 * For the visualisations of these please refer to:
 * https://www.aifdb.org/argview/{filename without extension}
 *
 * @author Taha Dogan Gunes
 */
public class IntegrationTests {

    @Test
    public void test24429() throws FileNotFoundException {
        String inputFilePath = "./org-tweetyproject-arg-peaf/src/main/resources/aif/tests/24429.json";

        NamedPEAFTheory peaf = getNamedPEAFTheory(inputFilePath);

        // e1
        AnalysisResult result = getAnalysisResult("95932", peaf);
        System.out.println(result);
        Assert.assertEquals("exact(e1) must be 1.0", 1.0, result.getResult(), 0.00001);

        // e2
        AnalysisResult result2 = getAnalysisResult("923543", peaf);
        System.out.println(result2);
        Assert.assertEquals("exact(e2) must be 0.0", 0.0, result2.getResult(), 0.00001);

        // e3
        AnalysisResult result3 = getAnalysisResult("923545", peaf);
        System.out.println(result3);
        Assert.assertEquals("exact(e3) must be 1.0", 1.0, result3.getResult(), 0.00001);
    }

    @Test
    public void test24434() throws FileNotFoundException {
        String inputFilePath = "./org-tweetyproject-arg-peaf/src/main/resources/aif/tests/24434.json";

        NamedPEAFTheory peaf = getNamedPEAFTheory(inputFilePath);

        System.out.println("START: Conversion from PEAF to DAF without probabilites.");
        EAFTheory eaf = PEAFToEAFConverter.convert(peaf);
        eaf.prettyPrint();
        DungTheory daf = EAFToDAFConverter.convert(eaf);
        System.out.println("END: Conversion from PEAF to DAF without probabilites.");
        System.out.println();

        // e1
        AnalysisResult result = getAnalysisResult("95932", peaf);
        System.out.println(result);
        Assert.assertEquals("exact(e1) must be 1.0", 1.0, result.getResult(), 0.00001);

        // e2
        AnalysisResult result2 = getAnalysisResult("923543", peaf);
        System.out.println(result2);
        Assert.assertEquals("exact(e2) must be 0.65", 0.65, result2.getResult(), 0.00001);

        // e3
        AnalysisResult result3 = getAnalysisResult("923545", peaf);
        System.out.println(result3);
        Assert.assertEquals("exact(e3) must be 0.65", 0.65, result3.getResult(), 0.00001);

        // e4
        AnalysisResult result4 = getAnalysisResult("926232", peaf);
        System.out.println(result4);
        Assert.assertEquals("exact(e4) must be 1.0", 1.0, result4.getResult(), 0.00001);

        // likely
        AnalysisResult result5 = getAnalysisResult("926232", peaf);
        System.out.println(result4);
        Assert.assertEquals("exact(likely) must be 1.0", 1.0, result5.getResult(), 0.00001);
    }

    @Test
    public void test24436() throws FileNotFoundException {
        String inputFilePath = "./org-tweetyproject-arg-peaf/src/main/resources/aif/tests/24436.json";

        NamedPEAFTheory peaf = getNamedPEAFTheory(inputFilePath);

        System.out.println("START: Conversion from PEAF to DAF without probabilites.");
        EAFTheory eaf = PEAFToEAFConverter.convert(peaf);
        eaf.prettyPrint();
        DungTheory daf = EAFToDAFConverter.convert(eaf);
        System.out.println("END: Conversion from PEAF to DAF without probabilites.");
        System.out.println();

        // e1
        AnalysisResult result = getAnalysisResult("95932", peaf);
        System.out.println(result);
        Assert.assertEquals("exact(e1) must be 1.0", 0.3175, result.getResult(), 0.00001);

        // e2
        AnalysisResult result2 = getAnalysisResult("923543", peaf);
        System.out.println(result2);
        Assert.assertEquals("exact(e2) must be 0.65", 0.65, result2.getResult(), 0.00001);


        // e5
        AnalysisResult result4 = getAnalysisResult("926232", peaf);
        System.out.println(result4);
        Assert.assertEquals("exact(e4) must be 1.0", 0.3000, result4.getResult(), 0.00001);

        // likely
        AnalysisResult result5 = getAnalysisResult("926244", peaf);
        System.out.println(result4);
        Assert.assertEquals("exact(e5) must be 1.0", 0.0250, result5.getResult(), 0.00001);

        PreferredAnalysis abstractAnalysis = new PreferredAnalysis(peaf);
        System.out.println("Preferred Extensions: " + abstractAnalysis.getExtensions());

        MaximiseChangeAnalysis<ExactAnalysis> analysis = new MaximiseChangeAnalysis<>(peaf, new PreferredReasoner(),
                // e1
                new ExactAnalysis(peaf, new PreferredReasoner()), Sets.newHashSet(peaf.getArgumentByIdentifier("95932")));
        // e4
        AnalysisResult result1 = analysis.query(Sets.newHashSet(peaf.getArgumentByIdentifier("926232")));

        System.out.println("MaximiseChange: " + result1);


        MaximiseChangeAnalysis<ExactAnalysis> analysis2 = new MaximiseChangeAnalysis<>(peaf, new PreferredReasoner(),
                // e1
                new ExactAnalysis(peaf, new PreferredReasoner()), Sets.newHashSet(peaf.getArgumentByIdentifier("95932")));
        // e4
        AnalysisResult result3 = analysis.query(Sets.newHashSet(peaf.getArgumentByIdentifier("923543")));

        System.out.println("MaximiseChange: " + result3);

    }


    private AnalysisResult getAnalysisResult(String identifier, NamedPEAFTheory peaf) {
        AbstractAnalysis abstractAnalysis = new ExactAnalysis(peaf, new PreferredReasoner());
        EArgument eArgument = peaf.getArgumentByIdentifier(identifier);
        AnalysisResult result = abstractAnalysis.query(Sets.newHashSet(eArgument));
        return result;
    }

    @Test
    public void testAIFCapP1() throws FileNotFoundException {
        String inputFilePath = "./org-tweetyproject-arg-peaf/src/main/resources/aif/tests/aif-Cap_p1.json";

        NamedPEAFTheory peaf = getNamedPEAFTheory(inputFilePath);

        System.out.println("START: Conversion from PEAF to DAF without probabilites.");
        EAFTheory eaf = PEAFToEAFConverter.convert(peaf);
        eaf.prettyPrint();
        DungTheory daf = EAFToDAFConverter.convert(eaf);

        assert daf != null;
        DungTheory namedDaf = new DungTheory();
        giveNamesToDAF(peaf, daf, namedDaf);


        System.out.println(namedDaf.prettyPrint());

        System.out.println("END: Conversion from PEAF to DAF without probabilites.");
        System.out.println();

        PreferredAnalysis abstractAnalysis = new PreferredAnalysis(peaf);
        System.out.println("Preferred Extensions: " + abstractAnalysis.getExtensions());


    }


    private void giveNamesToDAF(NamedPEAFTheory peaf, DungTheory daf, DungTheory namedDaf) {
        Map<Argument, Argument> mapping = Maps.newHashMap();
        for (Argument node : daf.getNodes()) {
            String[] argumentIndices = node.getName().split("_");
            List<String> argumentNames = Lists.newArrayList();
            for (String argumentIndex : argumentIndices) {
                EArgument eArgument = peaf.getArguments().get(Integer.parseInt(argumentIndex));
                String name = peaf.getNameOfArgument(eArgument);
                if (name.length() > 10) {
                    name = name.substring(0, 10);
                }
                name = "`" + name.strip() + "`";
                argumentNames.add(name);
            }
            String newName = String.join("_", argumentNames);
            Argument newArgument = new Argument(newName);
            namedDaf.add(newArgument);
            mapping.put(node, newArgument);
        }

        for (Attack attack : daf.getAttacks()) {
            Argument attacker = mapping.get(attack.getAttacker());
            Argument attacked = mapping.get(attack.getAttacked());
            namedDaf.addAttack(attacker, attacked);
        }
    }

    private NamedPEAFTheory getNamedPEAFTheory(String inputFilePath) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader(inputFilePath));
        AIFJSONTheory aifJSON = gson.fromJson(jsonReader, AIFJSONTheory.class);

        AIFCISReader reader = new AIFCISReader(inputFilePath);
        AIFTheory aifTheory = reader.read(aifJSON);
        AIFtoPEEAFConverter aifConverter = new AIFtoPEEAFConverter();
        PEEAFTheory peeafTheory = aifConverter.convert(aifTheory);
        PEEAFToPEAFConverter peeafConverter = new PEEAFToPEAFConverter();
        peeafTheory.prettyPrint();

        NamedPEAFTheory peaf = peeafConverter.convert(peeafTheory);
        peaf.prettyPrint();
        return peaf;
    }
}

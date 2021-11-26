package org.tweetyproject.arg.peaf.io.preeaf;

import org.tweetyproject.arg.peaf.syntax.NamedPEAFTheory;
import org.tweetyproject.arg.peaf.syntax.PEEAFTheory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;


public class PEEAFTheoryReader {

    private final String pathString;
    private final String schemaPathString = "preeaf.xsd";
    private final Validator validator;
    private static final ClassLoader loader = PEEAFTheoryReader.class.getClassLoader();


    public PEEAFTheoryReader(String pathString) throws MalformedURLException, SAXException {
        this.pathString = pathString;
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema = schemaFactory.newSchema(loader.getResource(schemaPathString));
        this.validator = schema.newValidator();
    }

    public PEEAFTheory read() throws IOException, SAXException, ParserConfigurationException {
        Source xmlFile = new StreamSource(new File(pathString));
        this.validator.validate(xmlFile);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // optional, but recommended
        // process XML securely, avoid attacks like XML External Entities (XXE)
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        // parse XML file
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(new File(pathString));

        // optional, but recommended
        // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        PEEAFTheory peeafTheory = new PEEAFTheory();
        System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());
        System.out.println("------");

        // Add arguments
        System.out.println("Arguments:\n------------");
        NodeList list = doc.getElementsByTagName("Argument");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String identifier = element.getAttribute("id");
                System.out.println("Identifier: `" + identifier + "`: " + element.getTextContent().strip());
                peeafTheory.addArgument(identifier, element.getTextContent().strip());
            }
        }

        // Add supports
        System.out.println("\nSupports:\n------------");
        list = doc.getElementsByTagName("Support");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String identifier = element.getAttribute("id");
                String froms = element.getElementsByTagName("Froms").item(0).getAttributes().getNamedItem("refids").getTextContent().strip();
                String toIdentifier = element.getElementsByTagName("To").item(0).getAttributes().getNamedItem("refid").getTextContent().strip();

                String[] fromIdentifiers = froms.split(" ");
                double probability = Double.parseDouble(element.getElementsByTagName("probability").item(0).getTextContent());
                System.out.println(identifier + ": " + Arrays.toString(fromIdentifiers) + " -> " + toIdentifier + " prob: " + probability);
                peeafTheory.addSupport(identifier, fromIdentifiers, toIdentifier, probability);
            }
        }

        System.out.println("\nAttacks:\n------------");
        list = doc.getElementsByTagName("Attack");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String identifier = element.getAttribute("id");

                String fromIdentifier = element.getElementsByTagName("From").item(0).getAttributes().getNamedItem("refid").getTextContent().strip();
                String toIdentifier = element.getElementsByTagName("To").item(0).getAttributes().getNamedItem("refid").getTextContent().strip();

                double probability = Double.parseDouble(element.getElementsByTagName("probability").item(0).getTextContent());

                System.out.println(identifier + ": " + fromIdentifier + " -> " + toIdentifier + " prob: " + probability);
                peeafTheory.addAttack(identifier, fromIdentifier, toIdentifier, probability);
            }
        }


        return peeafTheory;
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        PEEAFTheoryReader reader = new PEEAFTheoryReader(loader.getResource("preeaf.xml").getPath());
        PEEAFTheory peeafTheory = reader.read();
        peeafTheory.prettyPrint();

        System.out.println("\nPEAF Conversion:");
        PEEAFToPEAFConverter converter = new PEEAFToPEAFConverter();
        NamedPEAFTheory peafTheory = converter.convert(peeafTheory);
        peafTheory.prettyPrint();
    }
}

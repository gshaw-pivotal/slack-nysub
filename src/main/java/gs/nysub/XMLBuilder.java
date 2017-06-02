package gs.nysub;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XMLBuilder {

    public Document buildXMLDocument(String input) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;

        documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(input)));

        return document;
    }
}

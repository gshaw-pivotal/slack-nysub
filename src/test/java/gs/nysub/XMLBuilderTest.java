package gs.nysub;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class XMLBuilderTest {

    private XMLBuilder xmlBuilder;

    private String sampleMTAStatusString =
            "<service>" +
                "<responsecode>0</responsecode>" +
                "<timestamp>3/11/2017 1:36:00 AM</timestamp>" +
                "<subway>" +
                    "<line>" +
                        "<name>L</name>" +
                        "<status>GOOD SERVICE</status>" +
                        "<text />" +
                        "<Date></Date>" +
                        "<Time></Time>" +
                    "</line>" +
                "</subway>" +
            "</service>";

    @Before
    public void setup() {
        xmlBuilder = new XMLBuilder();
    }

    @Test
    public void givenAStringContainingXML_returnsANotNullXMLDoc() throws IOException, SAXException, ParserConfigurationException {
        Document document = xmlBuilder.buildXMLDocument("<?xml version=\"1.0\" encoding=\"utf-8\"?><a></a>");

        assertThat(document, not(equalTo(null)));
    }

    @Test
    public void givenAStringContainingXML_returnsACorrectlyStructuredXMLDoc() throws IOException, SAXException, ParserConfigurationException {
        Document document = xmlBuilder.buildXMLDocument(sampleMTAStatusString);

        assertTrue(document.getChildNodes().getLength() == 1);
        assertTrue(document.getChildNodes().item(0).getNodeName().equals("service"));
        assertTrue(document.getChildNodes().item(0).getChildNodes().item(2).getChildNodes().item(0).getChildNodes().getLength() == 5);
        assertTrue(document.getChildNodes().item(0).getChildNodes().item(2).getChildNodes().item(0).getChildNodes().item(1).getTextContent().equals("GOOD SERVICE"));
    }

    @Test(expected = SAXParseException.class)
    public void givenAStringContainingInvalidXML_throwsAnException() throws IOException, SAXException, ParserConfigurationException {
        xmlBuilder.buildXMLDocument("Not valid xml string");
    }
}
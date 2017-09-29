package de.fraunhofer.iais.eis.biotope;

import org.apache.http.HttpStatus;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

@RestController
public class Controller {

    @Autowired
    private OdfRdfConverter odfRdfConverter;

    private String baseUri = System.getenv("BASE_URL");

    @RequestMapping(value = "/toRDF", method = RequestMethod.POST)
    @ResponseBody
    public String toRDF(@RequestBody String omiOdfXmlResponse, String omiNodeHostName)
        throws IOException, SAXException
    {
        if (baseUri == null) baseUri = "http://localhost/";
        String odfStructure = extractOdfContent(omiOdfXmlResponse);
        Model odfData = odfRdfConverter.odf2rdf(new StringReader(odfStructure), baseUri, omiNodeHostName);
        return rdfModelToTurtle(odfData);

    }

    /**
     * The toRDF method is intended to be called with an O-MI/O-DF response. This contains the O-DF data structured
     * wrapped inside an O-MI "envelope". This method strips this envelope and returns only the O-DF parts of the
     * XML document, i.e., everything including and below the <Objects> elements.
     * @param omiOdfXmlResponse The whole O-MI/O-DF response
     * @return The part of the XML response which has been passed as a parameter and which only contains O-DF information
     */
    private String extractOdfContent(String omiOdfXmlResponse) throws IOException, SAXException {
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(omiOdfXmlResponse.getBytes(StandardCharsets.UTF_8)));
        return innerXml(doc.getElementsByTagName("msg").item(0));
    }

    private String innerXml(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS)node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        lsSerializer.getDomConfig().setParameter("xml-declaration", false);
        NodeList childNodes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getTextContent().trim().isEmpty()) continue;

            sb.append(lsSerializer.writeToString(childNodes.item(i)));
        }
        return sb.toString();
    }

    private String rdfModelToTurtle(Model model) {
        RDFWriter rdfWriter = new TurtleWriter(System.out);
        rdfWriter.startRDF();
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
        return rdfWriter.toString();
    }

}

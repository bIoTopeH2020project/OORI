package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.domainObjs.InfoItem;
import de.fraunhofer.iais.eis.biotope.domainObjs.Object;
import de.fraunhofer.iais.eis.biotope.domainObjs.Objects;
import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ModelFactory;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.Charset;

@Component
public class OdfRdfConverter {

    private final Logger logger = LoggerFactory.getLogger(OdfRdfConverter.class);


    /**
     * Converts an O-DF structure from XML format into an RDF model
     * @param odfStructureReader XML serialization of the O-MI/O-DF response that should be converted to RDF
     * @param baseUri Base Uri of each generated RDF resource
     * @param omiNodeHostName Hostname of the O-MI node that provides the O-MI/O-DF response
     * @return
     */
    public Model odf2rdf(Reader odfStructureReader, String baseUri, String omiNodeHostName) {
        Model model = new ModelBuilder().build();

        try {
            Objects beans = unmarshalObjects(odfStructureReader);

            ValueFactory vf = new MemValueFactory();
            String objectBaseIri = baseUri + omiNodeHostName + "/obj/";
            String infoItemBaseIri = baseUri + omiNodeHostName + "/infoitem/";
            beans.getObjects().forEach(objectBean -> model.addAll(objectBean.serialize(vf, objectBaseIri, infoItemBaseIri)));
        }
        catch (JAXBException | XMLStreamException e) {
            logger.error("Error reading O-DF structure", e);
        }

		return model;
    }

    private Objects unmarshalObjects(Reader odfStructureReader) throws JAXBException, XMLStreamException {
        JAXBContext jc = JAXBContext.newInstance(Objects.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);

        StreamSource source = new StreamSource(odfStructureReader);
        XMLStreamReader xsr = xif.createXMLStreamReader(source);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (Objects) unmarshaller.unmarshal(xsr);
    }

}
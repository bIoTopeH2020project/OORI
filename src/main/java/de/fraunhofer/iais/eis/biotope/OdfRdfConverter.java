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

    private final static String BASE_URI = "https://biotope-omi.datalab.erasme.org/";

    private String hostname = "localhost";

    public Model odf2rdf(InputStream odfStructure) {
    	JAXBContext jc = null;
    	try {
			 jc = JAXBContext.newInstance(Objects.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
    	Objects beans = null;
		try {
			beans = (Objects) jc.createUnmarshaller().unmarshal(odfStructure);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
        //Objects beans = JAXB.unmarshal(odfStructure, Objects.class);
		
        ValueFactory vf = new MemValueFactory();
        String objectBaseIri = BASE_URI + hostname + "/obj/";
        String infoItemBaseIri = BASE_URI + hostname + "/infoitem/";
	    Model model = new ModelBuilder().build();
	    beans.getObjects().forEach(objectBean -> model.addAll(objectBean.serialize(vf, objectBaseIri, infoItemBaseIri)));
		
		return model;
    }

    private void dumpModel(Model model) {
        RDFWriter rdfWriter = new TurtleWriter(System.out);
        rdfWriter.startRDF();
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
}
}
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.eclipse.rdf4j.model.Model;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import de.fraunhofer.iais.eis.biotope.OdfRdfConverter;
import de.fraunhofer.iais.eis.biotope.exceptions.OMIRequestCreationException;

public class NodeServiceTest {
	
	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private OdfRdfConverter odfRdfConverter  = new OdfRdfConverter();;
	private Model omiRDFModel;
	
	@Test
	public void returnOmiMessageContent() {
		
		String path=System.getProperty("user.dir")+"\\src\\test\\resources\\example2.xml";
    	FileInputStream fisTargetFile = null;
        Model odfData = null;
        try {
        	fisTargetFile = new FileInputStream(new File(path));
        	String omiMessage = IOUtils.toString(fisTargetFile, "UTF-8");
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(omiMessage.getBytes(StandardCharsets.UTF_8)));
            int omiRetCode = getOmiDocumentReturnCode(doc);
            if (omiRetCode == HttpStatus.SC_OK) {
                String odfStructure = innerXml(doc.getElementsByTagName("msg").item(0));
                //System.out.println(odfStructure);
                returnOdfStructure(odfStructure);
                 
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new OMIRequestCreationException("Error parsing O-MI message", e);
        }
        
    }
	
	private int getOmiDocumentReturnCode(Document doc) {
        Node returnNode = doc.getElementsByTagName("return").item(0);
        return Integer.parseInt(returnNode.getAttributes().getNamedItem("returnCode").getNodeValue());
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
	
	private void returnOdfStructure(String odfStructure) {

    	//System.out.println(odfStructure);
        Model odfData = odfRdfConverter.odf2rdf(new ByteArrayInputStream(odfStructure.getBytes(StandardCharsets.UTF_8)));
        this.omiRDFModel=odfData;
        //odfRdfRepository.persist(odfData);
        //return odfData;
    }

}

package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.exceptions.OMIRequestParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
public class OmiNodeResult {
	 private final static Logger logger = LoggerFactory.getLogger(OmiNodeResult.class);

	    private URL url;
	    private String subscriptionXMLResult;
	    private String subscriptionId;

	    public OmiNodeResult() {
	    }

	    public void validate()  {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	        try {
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            InputStream is = new ByteArrayInputStream( subscriptionXMLResult.getBytes() );
	            db.parse(is);
	        }
	        catch (SAXException | IOException | ParserConfigurationException e) {
	            throw new OMIRequestParseException("Invalid O-MI request", e);
	        }
	    }

	    public URL getUrl() {
	        return url;
	    }

	    public void setUrl(URL url) {
	        this.url = url;
	    }

	    public String getSubscriptionXMLResult() {
	        return subscriptionXMLResult;
	    }

	    public void setSubscriptionXMLResult(String subscriptionXMLResult) {
	        this.subscriptionXMLResult = subscriptionXMLResult;
	    }

	    public String getSubscriptionId() {
	        return subscriptionId;
	    }

	    public void setSubscriptionId(String subscriptionId) {
	        this.subscriptionId = subscriptionId;
	    }

	    @Override
	    public String toString() {
	        return "OmiNode: '" +url.toString()+ "', result: '" +subscriptionXMLResult+ "'";
	    }
	


}

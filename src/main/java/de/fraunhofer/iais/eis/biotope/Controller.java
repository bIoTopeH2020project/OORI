package de.fraunhofer.iais.eis.biotope;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import de.fraunhofer.iais.eis.biotope.NodeService;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;

import de.fraunhofer.iais.eis.biotope.exceptions.OMIRequestResponseException;

import javax.servlet.http.HttpServletRequest;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;



@RestController
public class Controller{

	 
    private final Logger logger = LoggerFactory.getLogger(Controller.class);
    static String msg;
    
    @Autowired
    NodeService service;

    @RequestMapping(value = "/nodes", method = RequestMethod.POST)
    public void addAndSubscribeAndSyncNode(@RequestBody OmiNode node) throws IOException {
        node.validate();
        service.addNode(node);
        service.baselineSync(node);
        //service.subscribe(node);
        
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Collection<OmiNode> getNodes() {
        return service.getOmiNodes();
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public void callback(HttpServletRequest body) throws IOException {
        String messageContent = IOUtils.toString(body.getInputStream(), Charset.defaultCharset());
        service.persistOmiMessageContent(messageContent);
    }
    
    @RequestMapping(value = "/toRDF", method = RequestMethod.POST)
    public void toRDF(@RequestBody OmiNodeResult nodeResult)
    {
    	String odfContent = nodeResult.getSubscriptionXMLResult();
	    odfContent=odfContent.replace(" xmlns=\"http://www.opengroup.org/xsd/odf/1.0/\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:odf=\"http://www.opengroup.org/xsd/odf/1.0/\"", "");
	    //System.out.println(odfContent);
	    service.returnOmiMessageContent(odfContent);
    }
    
    
    @RequestMapping(value = "/rdfModel", method = RequestMethod.GET)
    @ResponseBody
    public String getRDFModel() {
    try
    {
    	Model m=service.getRDFModel();
    	StringWriter writer= new StringWriter();
    	TurtleWriter rdfWriter = new TurtleWriter(writer);
    	rdfWriter.startRDF();
    	m.forEach(statment -> rdfWriter.handleStatement(statment));
    	rdfWriter.endRDF();
        return writer.toString();
    }
    catch (NullPointerException e)
    {
    	
    }
	return null;
    }
    
}

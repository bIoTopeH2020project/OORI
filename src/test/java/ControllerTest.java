import java.io.StringWriter;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fraunhofer.iais.eis.biotope.NodeService;
import de.fraunhofer.iais.eis.biotope.OmiNodeResult;

public class ControllerTest {
	
	//NodeService service=new NodeService();
	/*
	@Test
	@RequestMapping(value = "/toRDF", method = RequestMethod.POST)
    public void toRDF(@RequestBody OmiNodeResult nodeResult)
    {
    	String odfContent = nodeResult.getSubscriptionXMLResult();
	    odfContent=odfContent.replace(" xmlns=\"http://www.opengroup.org/xsd/odf/1.0/\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:odf=\"http://www.opengroup.org/xsd/odf/1.0/\"", "");
	    service.returnOmiMessageContent(odfContent);
    }
    */
	/*
	@Test
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
    }  */

}

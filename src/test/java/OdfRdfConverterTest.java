import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.fraunhofer.iais.eis.biotope.OdfRdfConverter;

import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class OdfRdfConverterTest {

	private OdfRdfConverter odfRdfConverter = new OdfRdfConverter();
	
    @Test
    public void omi2rdf_simple() {
    	String path=System.getProperty("user.dir")+"\\src\\test\\resources\\example1.xml";
    	FileInputStream fisTargetFile = null;
		try {
			fisTargetFile = new FileInputStream(new File(path));
			String odfContent = IOUtils.toString(fisTargetFile, "UTF-8");
			Model odfData = odfRdfConverter.odf2rdf(new ByteArrayInputStream(odfContent.getBytes(StandardCharsets.UTF_8)));
			//dumpModel(odfData);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
   /* private void dumpModel(Model model) {
        RDFWriter rdfWriter = new TurtleWriter(System.out);
        rdfWriter.startRDF();
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
    }
  */
    
	
}

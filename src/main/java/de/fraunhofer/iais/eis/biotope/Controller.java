package de.fraunhofer.iais.eis.biotope;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.StringReader;

@RestController
public class Controller{

    @Autowired
    private OdfRdfConverter odfRdfConverter;

    private String baseUri = System.getenv("BASE_URL");

    @RequestMapping(value = "/toRDF", method = RequestMethod.POST)
    @ResponseBody
    public String toRDF(@RequestBody String omiOdfXmlResponse, String omiNodeHostName)
    {
        if (baseUri == null) baseUri = "http://localhost/";
        Model odfData = odfRdfConverter.odf2rdf(new StringReader(omiOdfXmlResponse), baseUri, omiNodeHostName);
        return rdfModelToTurtle(odfData);

    }

    private String rdfModelToTurtle(Model model) {
        RDFWriter rdfWriter = new TurtleWriter(System.out);
        rdfWriter.startRDF();
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
        return rdfWriter.toString();
    }

}

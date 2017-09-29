package de.fraunhofer.iais.eis.biotope;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.StringReader;

@RestController
public class Controller {

    @Autowired
    private OdfRdfConverter odfRdfConverter;

    private String baseUri = System.getenv("BASE_URL");

    @RequestMapping(value = "/toRDF", method = RequestMethod.POST)
    @ResponseBody
    public String toRDF(@RequestBody String omiOdfXmlResponse, String omiNodeHostName)
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
    private String extractOdfContent(String omiOdfXmlResponse) {
        // todo: implement me!
        return omiOdfXmlResponse;
    }

    private String rdfModelToTurtle(Model model) {
        RDFWriter rdfWriter = new TurtleWriter(System.out);
        rdfWriter.startRDF();
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
        return rdfWriter.toString();
    }

}

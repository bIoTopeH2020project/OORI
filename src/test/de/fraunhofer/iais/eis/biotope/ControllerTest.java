package de.fraunhofer.iais.eis.biotope;

import com.jayway.restassured.RestAssured;
import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import org.eclipse.rdf4j.rio.turtle.TurtleParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;

import static com.jayway.restassured.RestAssured.*;
import static org.apache.commons.lang3.StringUtils.containsOnly;
import static org.hamcrest.Matchers.equalTo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Controller.class, OdfRdfConverter.class })
/*
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
*/
public class ControllerTest {

    @Autowired
    private Controller controller;

    /*
    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void listDatasetDescriptions() {
        when().
                get("/").
        then().
                statusCode(200).
                body("id.size()", equalTo(1));
    }

    @Test
    public void getWholeDatasetContent() {
        Assert.assertEquals(6306600, when().get("/dataset/{id}", 0).asByteArray().length);
    }

    @Test
    public void getWholeDatasetMetadata() {
        Assert.assertEquals(HttpStatus.SC_OK, when().get("/dataset/{id}/meta", 0).statusCode());
        Assert.assertFalse(when().get("/dataset/{id}/meta", 0).body().asString().isEmpty());
    }
    */

    @Test
    public void convertOmiResponse() throws Exception {
        InputStream omiResponse = getClass().getResourceAsStream("/resources/xml/omiResponse.xml");
        String rdf = controller.toRDF(IOUtils.toString(omiResponse, Charset.defaultCharset()), "omiNode");

        Model model = new LinkedHashModel();
        RDFParser parser = new TurtleParser();
        parser.setRDFHandler(new AbstractRDFHandler() {
            @Override
            public void handleStatement(Statement st) throws RDFHandlerException {
                model.add(st);
            }
        });
        parser.parse(new StringReader(rdf), "");

        // RDF model is valid
        Assert.assertFalse(model.isEmpty());
    }

}

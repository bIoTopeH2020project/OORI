package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import org.eclipse.rdf4j.rio.turtle.TurtleParser;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Iterator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Controller.class, OdfRdfConverter.class })
public class CompetenceQueryTest {

    private final Logger logger = LoggerFactory.getLogger(CompetenceQueryTest.class);

    @Autowired
    private Controller controller;

    private Repository parkingDataRepo;

    @Before
    public void setUp() throws Exception {
        InputStream omiResponse = getClass().getResourceAsStream("/resources/xml/dipoliParking.xml");
        String rdf1 = controller.toRDF(IOUtils.toString(omiResponse, Charset.defaultCharset()), "http://biotope.cs.hut.fi");

        omiResponse = getClass().getResourceAsStream("/resources/xml/csbuildingParking.xml");
        String rdf2 = controller.toRDF(IOUtils.toString(omiResponse, Charset.defaultCharset()), "http://biotope.cs.hut.fi");

        parkingDataRepo = new SailRepository(new MemoryStore());
        parkingDataRepo.initialize();
        RDFParser parser = new TurtleParser();
        parser.setRDFHandler(new AbstractRDFHandler() {
            @Override
            public void handleStatement(Statement st) throws RDFHandlerException {
                parkingDataRepo.getConnection().add(st);
            }
        });

        parser.parse(new StringReader(rdf1), "");
        parser.parse(new StringReader(rdf2), "");
    }

    @Test
    public void runSparqlQueries() {

        /* todo: use vocabulary term for vehicly type, define height value without unit so that it can be comparable, price with currency unit, weird hierarchy
        */

        parkingDataRepo.getConnection().getNamespace("mv");

        // what parking facilities exist that are suited for electrical vehicles with a height of 2m and what is their hourly price?
        String sparqlQuery =
                "PREFIX mv:<" +NS.MV+ "> " +
                "PREFIX skos:<" + SKOS.NAMESPACE+ "> " +
                "PREFIX odf:<" + NS.ODF+ "> " +
                "PREFIX schema:<" + NS.SCHEMA+ "> " +
                "SELECT * WHERE {" +
                    "?type a mv:ParkingUsageType; " +
                    "  skos:notation ?notation;" +
                    "  odf:object ?vehicle;" +
                    "  mv:PriceParking ?hourlyPrice." +
                    "?vehicle a schema:Vechile;" +
                    "  schema:height ?height." +
                    "?facility a schema:ParkingFacility;" +
                    "  odf:object/odf:object ?type." +

                    "BIND (xsd:float(SUBSTR(?height, 1, STRLEN(?height) - 1)) AS ?height_numeric) " +
                    "FILTER (?height_numeric > 2)" +
                    "FILTER (CONTAINS(LCASE(?notation), 'electric'))" +
                "}";

        TupleQuery query = parkingDataRepo.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);
        TupleQueryResult result = query.evaluate();
        Assert.assertTrue(result.hasNext());

        while (result.hasNext()) {
            BindingSet bs = result.next();
            logger.debug("facility: " +bs.getBinding("facility")+ ", price: " +bs.getBinding("hourlyPrice"));
        }
    }

}

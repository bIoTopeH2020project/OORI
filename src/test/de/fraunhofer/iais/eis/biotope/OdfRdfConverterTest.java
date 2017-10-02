package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import de.fraunhofer.iais.eis.biotope.vocabs.ODF;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {OdfRdfConverter.class})
public class OdfRdfConverterTest {

    @Autowired
    private OdfRdfConverter odfRdfConverter;

    private String baseUri = "http://localhost/";
    private String omiNodeHostName = "someOmiNode";

    @Test
    public void omi2rdf_simple() {
        // get the O-MI/O-DF response which we want to use in our test as an input
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/simpleOdf.xml");

        // run the converter method with the test input
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        // now check if the converter method's ouput is correct, i.e. it satiesfies our requirements
        // therefore, we check that a certain information is contained in the created RDF model

        // make sure that the model contains exactly one ODF Object
        Assert.assertEquals(1, rdfModel.filter(null, RDF.TYPE, ODF.OBJECT).size());

        // make sure that the model contains exactly one ODF InfoItem
        // todo: implement me!

        // make sure that the value of the InfoItem has a timestamp
        // todo: implement me!

        // make sure that the value of the InfoItem has a data value timestamp with value "100"
        // todo: implement me!

        // remove this if all assertions are implemented
        Assert.fail();
    }

    @Test
    public void omi2rdf_with_custom_annotations() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/annotatedOdf_Lyon.xml");
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        System.out.println(Util.rdfModelToTurtle(rdfModel));

        //todo add assertion: the model contains 3 resources that are of type odf:InfoItem and which have values assigned that use the properties geo:lat or geo:long or gr:name to link to their values

        //todo add assertion: the model contains 4 Objects that are of type odf:Object AND one of org:Organization, org:OrganizationalUnit, seas:LoRaCommunicationDevice, gr:Brand

        // remove this if all assertions are implemented
        Assert.fail();
    }

    @Test
    public void omi2rdf_with_standard_annotations() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/annotatedOdf_Helsinki.xml");
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        System.out.println(Util.rdfModelToTurtle(rdfModel));

        //todo add assertion: the model contains 7 InfoItems that have 2 type definitions

        //todo add assertion: the model contains 3 Objects that have 2 type definitions

        //todo add assertion: the InfoItem of tyle isOwnedBy" name="Owner"

        // remove this if all assertions are implemented
        Assert.fail();
    }

    /*
    @Test
    public void omi2rdf_metadata_and_description() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/objTreeMetadataAndDescription.xml");
        odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);
    }

    @Test
    public void omi2rdf_multi_objects() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/objTreeMultiObjects.xml");
        odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);
    }
    */


}

package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
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
    private ValueFactory factory = SimpleValueFactory.getInstance();

    @Test
    public void omi2rdf_simple() {
        // get the O-MI/O-DF response which we want to use in our test as an input
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/simpleOdf.xml");

        // run the converter method with the test input
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        // now check if the converter method's ouput is correct, i.e. it satiesfies our requirements
        // therefore, we check that a certain information is contained in the created RDF model

        // make sure that the model contains exactly one ODF Object
        IRI object = factory.createIRI(NS.ODF, "Object");
        Assert.assertEquals(1, rdfModel.filter(null, RDF.TYPE, object).size());

        // make sure that the model contains exactly one ODF InfoItem
        // todo: implement me!
        IRI object = factory.createIRI(NS.ODF, "InfoItem");
        Assert.assertEquals(1, rdfModel.filter(null, RDF.TYPE, object).size());
        
        // make sure that the value of the InfoItem has a timestamp
        // todo: implement me!
        IRI object3 = factory.createIRI(NS.ODF, "Value");	        
        rdfModel.filter(null, RDF.TYPE, object3).forEach(info->{
        														IRI object4 = factory.createIRI(NS.DCT, "created");
        														Assert.assertEquals(1,rdfModel.filter(info.getSubject(),object4,null).size());});
        

        // make sure that the value of the InfoItem has a data value timestamp with value "20.3125"
        // todo: implement me!
        IRI object5 = factory.createIRI(NS.ODF, "Value");	        
        Model filteredModel=rdfModel.filter(null, RDF.TYPE, object5);
        boolean flag=false;
        IRI object6 = factory.createIRI(NS.ODF, "dataValue");
		Literal value=factory.createLiteral("20.3125", factory.createIRI("xsd:decimal"));
        for (Statement info : filteredModel) {
			try{
				Assert.assertTrue(rdfModel.contains(info.getSubject(),object6,value));
				flag=true;
				break;
			}
			catch (AssertionError e)
			{
				
			}
		}
        if (flag==false)
        	System.out.println("Value not found");

        // remove this if all assertions are implemented
        Assert.fail();
    }

    @Test
    public void omi2rdf_with_standard_annotations() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/annotatedOdf_Helsinki.xml");
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        //todo: fix nullpointerexception
        //todo: add assertions that check for availability of new triples that cover rdf types

        // remove this if all assertions are implemented
        Assert.fail();
    }

    @Test
    public void omi2rdf_with_custom_annotations() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/annotatedOdf_Lyon.xml");
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        //todo: fix nullpointerexception
        //todo: add assertions that check for availability of new triples that cover rdf types


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

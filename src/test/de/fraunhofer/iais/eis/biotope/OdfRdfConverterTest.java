package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.vocabs.*;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.security.auth.Subject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

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
        Assert.assertEquals(1, rdfModel.filter(null, RDF.TYPE, ODF.Object).size());

        // make sure that the model contains exactly one ODF InfoItem
        Assert.assertEquals(1, rdfModel.filter(null, RDF.TYPE, ODF.InfoItem).size());
        
        // make sure that the value of the InfoItem has a timestamp
        rdfModel.filter(null, RDF.TYPE, ODF.Value).forEach(info->{
            Assert.assertEquals(1,rdfModel.filter(info.getSubject(),ODF.timeStamp,null).size());
        });

        // make sure that the value of the InfoItem has a data value timestamp with value "20.3125"
        Resource infoItem = rdfModel.filter(null, RDF.TYPE, ODF.InfoItem).subjects().iterator().next();
        Resource valueNode = (Resource) rdfModel.filter(infoItem, ODF.value, null).objects().iterator().next();
        Literal valueLiteral = (Literal) rdfModel.filter(valueNode, ODF.datavalue, null).objects().iterator().next();
        Assert.assertEquals("100", valueLiteral.stringValue());
    }

    @Test
    public void omi2rdf_with_custom_annotations() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/annotatedOdf_Lyon.xml");
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        System.out.println(Util.rdfModelToTurtle(rdfModel));

        // the model contains 3 resources that are of type odf:InfoItem and which have values assigned that use the
        // properties geo:lat or geo:long or gr:name to link to their values
        int count = 0;
        Model model = rdfModel.filter(null, RDF.TYPE, ODF.InfoItem);
        for (Resource infoItem : model.subjects()) {
            if (
            rdfModel.filter(infoItem, null, null).predicates()
                    .stream().allMatch(predicate ->
                Arrays.asList(GR.name, GEO.latitude, GEO.longitude).contains(predicate)
            ))
			count++;
		}
        Assert.assertEquals(3, count);

        //the model contains 4 Objects that are of type odf:Object AND one of org:Organization, org:OrganizationalUnit, seas:LoRaCommunicationDevice, gr:Brand
        model=rdfModel.filter(null, RDF.TYPE, ODF.Object);
        count = 0;
        for (Statement info : model) {
        	if (rdfModel.contains(info.getSubject(), RDF.TYPE, ORG.Organization) ||
                    rdfModel.contains(info.getSubject(),RDF.TYPE, ORG.OrganizationalUnit) ||
                    rdfModel.contains(info.getSubject(),RDF.TYPE, SEAS.LoRaCommunicationDevice) ||
                    rdfModel.contains(info.getSubject(),RDF.TYPE, GR.Brand))
                count++;
		}
        Assert.assertEquals(4, count);
    }

    @Test
    public void omi2rdf_with_standard_annotations() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/annotatedOdf_Helsinki.xml");
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        System.out.println(Util.rdfModelToTurtle(rdfModel));

        //the model contains 3 Objects that have 2 type definitions
        Assert.assertEquals(3,
                rdfModel.filter(null, RDF.TYPE, ODF.Object).subjects().stream().filter(resource -> rdfModel.filter(resource, RDF.TYPE, null).objects().size() == 2).count());

        //exemplary Object that is related to an InfoItem's value literal through the InfoItem's type
        ValueFactory vf = new MemValueFactory();
        IRI subj = vf.createIRI("http://localhost/someOmiNode/obj/ParkingService/ParkingFacilities/DipoliParkingLot");

        Assert.assertTrue(rdfModel.contains(subj, MV.isOwnedBy, null));
    }

}

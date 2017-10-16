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
import javax.sound.sampled.SourceDataLine;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {OdfRdfConverter.class})
public class OdfRdfConverterTest {

    @Autowired
    private OdfRdfConverter odfRdfConverter;

    private String baseUri = "http://localhost/";
    private String omiNodeHostName = "someOmiNode";

    @Test
    public void omi2rdf_simple() throws JAXBException, XMLStreamException {
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
    public void omi2rdf_with_custom_annotations() throws JAXBException, XMLStreamException {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/annotatedOdf_Lyon.xml");
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        System.out.println(Util.rdfModelToTurtle(rdfModel));

        // the model contains 2 objects that have asserted values using custom ontologies
        Resource commDev = rdfModel.filter(null, RDF.TYPE,  SEAS.LoRaCommunicationDevice).subjects().iterator().next();
        Assert.assertTrue(rdfModel.filter(commDev, null, null).predicates().containsAll(Arrays.asList(GEO.latitude, GEO.longitude)));

        Resource brand = rdfModel.filter(null, RDF.TYPE,  GR.Brand).subjects().iterator().next();
        Assert.assertTrue(rdfModel.filter(brand, null, null).predicates().contains(GR.name));

        //the model contains 4 Objects that are of type odf:Object AND one of org:Organization, org:OrganizationalUnit, seas:LoRaCommunicationDevice, gr:Brand
        Model model=rdfModel.filter(null, RDF.TYPE, ODF.Object);
        int count = 0;
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
    public void omi2rdf_with_standard_annotations() throws JAXBException, XMLStreamException {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/annotatedOdf_Helsinki.xml");
        Model rdfModel = odfRdfConverter.odf2rdf(new InputStreamReader(odfStructure), baseUri, omiNodeHostName);

        System.out.println(Util.rdfModelToTurtle(rdfModel));

        Collection<IRI> customPredicates =  Arrays.asList(MV.isOwnedBy,
                MV.PriceParking,
                MV.hasTotalCapacity,
                MV.hasNumberOfVacantParkingSpaces,
                SCHEMA.depth,
                SCHEMA.height,
                SCHEMA.width);

        int objectsWithCustomPredicates = 0;
        for (Resource object : rdfModel.filter(null, RDF.TYPE, ODF.Object).subjects()) {
            Set<IRI> objectPredicates = rdfModel.filter(object, null, null).predicates();

            Collection<IRI> intersection = new ArrayList<>(objectPredicates);
            intersection.retainAll(customPredicates);
            if (!intersection.isEmpty()) objectsWithCustomPredicates++;
        }
        Assert.assertEquals(3, objectsWithCustomPredicates);
    }

}

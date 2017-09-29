package de.fraunhofer.iais.eis.biotope;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class OdfRdfConverterTest {

    private OdfRdfConverter odfRdfConverter = new OdfRdfConverter();

    @Test
    public void omi2rdf_simple() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/simpleOdf.xml");
        odfRdfConverter.odf2rdf(odfStructure);

        //todo: add assertions

    }

    @Test
    public void omi2rdf_metadata_and_description() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/objTreeMetadataAndDescription.xml");
        odfRdfConverter.odf2rdf(odfStructure);

        //todo: add assertions
    }

    @Test
    public void omi2rdf_multi_objects() {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/xml/objTreeMultiObjects.xml");
        odfRdfConverter.odf2rdf(odfStructure);

        //todo: add assertions
    }

}

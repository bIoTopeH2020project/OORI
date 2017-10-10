package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Created by christian on 10.10.17.
 */
public class SEAS {

    private static ValueFactory factory = SimpleValueFactory.getInstance();

    // classes
    public final static IRI LoRaCommunicationDevice = factory.createIRI(NS.SEAS, "LoRaCommunicationDevice");

}

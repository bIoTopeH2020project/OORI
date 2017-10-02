package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Created by christian on 02.10.17.
 */
public class ODF {

    private static ValueFactory factory = SimpleValueFactory.getInstance();

    // classes
    public final static IRI OBJECT = factory.createIRI(NS.ODF, "Object");

    // properties
    public final static IRI DATAVALUE = factory.createIRI(NS.ODF, "dataValue");

}

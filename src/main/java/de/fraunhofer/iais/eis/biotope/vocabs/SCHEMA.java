package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Created by christian on 16.10.17.
 */
public class SCHEMA {

    private static ValueFactory factory = SimpleValueFactory.getInstance();

    // properties
    public final static IRI height = factory.createIRI(NS.SCHEMA, "height");
    public final static IRI depth = factory.createIRI(NS.SCHEMA, "depth");
    public final static IRI width = factory.createIRI(NS.SCHEMA, "width");

}

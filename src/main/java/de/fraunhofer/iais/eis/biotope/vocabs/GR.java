package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Created by christian on 10.10.17.
 */
public class GR {

    private static ValueFactory factory = SimpleValueFactory.getInstance();

    // classes
    public final static IRI Brand = factory.createIRI(NS.GR, "Brand");

    // properties
    public final static IRI name = factory.createIRI(NS.GR, "name");

}

package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Created by christian on 10.10.17.
 */
public class GEO {

    private static ValueFactory factory = SimpleValueFactory.getInstance();

    // properties
    public final static IRI latitude = factory.createIRI(NS.GEO, "lat");
    public final static IRI longitude = factory.createIRI(NS.GEO, "long");

}

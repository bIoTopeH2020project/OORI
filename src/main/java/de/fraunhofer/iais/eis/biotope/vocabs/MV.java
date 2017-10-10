package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Created by christian on 10.10.17.
 */
public class MV {

    private static ValueFactory factory = SimpleValueFactory.getInstance();

    // properties
    public final static IRI isOwnedBy = factory.createIRI(NS.MV, "isOwnedBy");
}

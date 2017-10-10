package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Created by christian on 10.10.17.
 */
public class ORG {

    private static ValueFactory factory = SimpleValueFactory.getInstance();

    // classes
    public final static IRI Organization = factory.createIRI(NS.ORG, "Organization");
    public final static IRI OrganizationalUnit = factory.createIRI(NS.ORG, "OrganizationalUnit");

}

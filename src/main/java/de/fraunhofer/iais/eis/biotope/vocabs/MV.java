package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Created by christian on 10.10.17.
 */
public class MV {

    private static ValueFactory factory = SimpleValueFactory.getInstance();

    // classes
    public final static IRI PriceParking = factory.createIRI(NS.MV, "PriceParking");

    // properties
    public final static IRI isOwnedBy = factory.createIRI(NS.MV, "isOwnedBy");
    public final static IRI hasTotalCapacity = factory.createIRI(NS.MV, "hasTotalCapacity");
    public final static IRI hasNumberOfVacantParkingSpaces = factory.createIRI(NS.MV, "hasNumberOfVacantParkingSpaces");

}

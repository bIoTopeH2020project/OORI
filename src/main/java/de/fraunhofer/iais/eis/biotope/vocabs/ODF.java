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
    public final static IRI INFOITEM = factory.createIRI(NS.ODF, "InfoItem");

    // properties
    public final static IRI datavalue = factory.createIRI(NS.ODF, "dataValue");
    public final static IRI infoitem = factory.createIRI(NS.ODF, "infoitem");

}

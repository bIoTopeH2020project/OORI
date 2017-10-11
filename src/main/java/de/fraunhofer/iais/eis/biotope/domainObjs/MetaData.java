package de.fraunhofer.iais.eis.biotope.domainObjs;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.HashSet;

@XmlRootElement
public class MetaData {

    private Collection<InfoItem> infoItems;

    public Collection<InfoItem> getInfoItems() {
        return infoItems;
    }

    @XmlElement(name = "InfoItem")
    public void setInfoItems(Collection<InfoItem> infoItems) {
        this.infoItems = infoItems;
    }

    public BNode serialize(Model model, String baseIri) {
        ValueFactory vf = new MemValueFactory();
        BNode subject = vf.createBNode();

        ModelBuilder builder = new ModelBuilder(model);
        builder.subject(subject)
                .add("rdf:type", "odf:MetaData");

        infoItems.forEach(infoitem -> {
            IRI infoitemIri = infoitem.serialize(model, baseIri);
            builder.add("odf:infoitem", infoitemIri);
        });

        return subject;
    }

}

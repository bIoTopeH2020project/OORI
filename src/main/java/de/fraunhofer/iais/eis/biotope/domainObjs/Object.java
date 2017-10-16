package de.fraunhofer.iais.eis.biotope.domainObjs;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import de.fraunhofer.iais.eis.biotope.vocabs.ODF;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;

@XmlRootElement(name="Object")
public class Object {

    private final Logger logger = LoggerFactory.getLogger(Object.class);

    private String id, type, prefix;
    private Collection<InfoItem> infoItems = new ArrayList<>();
    private Collection<Object> objects = new ArrayList<>();

    public String getId() {
        return id;
    }

    @XmlElement(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setName(String type) {
        this.type = type;
    }

    public Collection<InfoItem> getInfoItems() {
        return infoItems;
    }

    @XmlElement(name = "InfoItem")
    public void setInfoItems(Collection<InfoItem> infoItems) {
        this.infoItems = infoItems;
    }

    public Collection<Object> getObjects() {
        return objects;
    }

    @XmlElement(name = "Object")
    public void setObjects(Collection<Object> objects) {
        this.objects = objects;
    }

    public String getPrefix() {
        return prefix;
    }

    @XmlAttribute(name = "prefix")
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public IRI serialize(Model model, String objectBaseIri, String infoItemBaseIri) {
        ValueFactory vf = new MemValueFactory();
        IRI subject = vf.createIRI(objectBaseIri + id);

        ModelBuilder builder = new ModelBuilder(model);
        builder.subject(subject)
                .add("rdf:type", "odf:Object")
                .add("skos:notation", id);

        if (type != null && type.contains(":")) {
            try {
                builder.add("rdf:type", type);
            }
            catch (IllegalArgumentException e) {
                logger.info("Type is not a valid IRI, omitting additional type assertion.");
            }
        }

        String objRelatedInfoItemBaseIri = infoItemBaseIri + id + "/";
        infoItems.forEach(infoitem -> {
            IRI infoItemIri = infoitem.serialize(model, objRelatedInfoItemBaseIri);
            builder.add("odf:infoitem", infoItemIri);
            addInfoItemValues(infoitem, infoItemIri, model, builder);
        });

        String nestedObjectsBaseIri = subject.toString() + "/";
        objects.forEach(object -> {
            IRI objectIri = object.serialize(model, nestedObjectsBaseIri, objRelatedInfoItemBaseIri);
            builder.add("odf:object", objectIri);
        });

        return subject;
    }

    private void addInfoItemValues(InfoItem infoItem, IRI infoItemIri, Model model, ModelBuilder builder) {
        String infoItemType = infoItem.getType();
        if (infoItemType == null) {
            infoItemType = interpretNameAsType(infoItem.getName());
            if (infoItemType == null) return;
        }

        for (org.eclipse.rdf4j.model.Value valueNode : model.filter(infoItemIri, ODF.value, null).objects()) {
            for (org.eclipse.rdf4j.model.Value dataValue : model.filter((BNode) valueNode, ODF.datavalue, null).objects())
            {
                builder.add(infoItemType, dataValue);
            }
        };
    }

    private String interpretNameAsType(String infoItemName) {
        return infoItemName.contains(":") ? infoItemName : null;
    }

}

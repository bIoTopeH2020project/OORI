package de.fraunhofer.iais.eis.biotope.domainObjs;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import de.fraunhofer.iais.eis.biotope.vocabs.ODF;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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

    public Model serialize(ValueFactory vf, String objectBaseIri, String infoItemBaseIri) {

        IRI subject = vf.createIRI(objectBaseIri + id);

        ModelBuilder builder = new ModelBuilder();
        addNamespaces(builder);
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE)
                .setNamespace("org", NS.ORG)
                .subject(subject)
                .add("rdf:type", "odf:Object")
                .add("skos:notation", id);

        if (type != null) {
            try {
                builder.add("rdf:type", vf.createIRI(type));
            }
            catch (IllegalArgumentException e) {
                logger.info("Type is not a valid IRI, omitting additional type assertion.");
            }
        }

        Collection<Model> infoItemModels = new HashSet<>();
        String objRelatedInfoItemBaseIri = infoItemBaseIri + id + "/";
        infoItems.forEach(infoitem -> infoItemModels.add(infoitem.serialize(vf, objRelatedInfoItemBaseIri)));

        Collection<Model> nestedObjectsModels = new HashSet<>();
        String nestedObjectsBaseIri = subject.toString() + "/";
        objects.forEach(object -> nestedObjectsModels.add(object.serialize(vf, nestedObjectsBaseIri, objRelatedInfoItemBaseIri)));

        infoItemModels.forEach(model -> {
            builder.add("odf:infoitem", model.iterator().next().getSubject());
        });

        nestedObjectsModels.forEach(model -> {
            builder.add("odf:object", model.iterator().next().getSubject());
        });

        addInfoItemValues(vf, builder);

        Model objectModel = builder.build();
        infoItemModels.forEach(infoItemModel -> objectModel.addAll(infoItemModel));
        nestedObjectsModels.forEach(nestedObjectsModel -> objectModel.addAll(nestedObjectsModel));
        return objectModel;
    }

    private void addNamespaces(ModelBuilder builder) {
        if (prefix == null) return;

        StringTokenizer tokenizer = new StringTokenizer(prefix, " ");
        if (tokenizer.countTokens() % 2 != 0) {
            logger.error("Odd number of prefix definitions");
            return;
        }

        while (tokenizer.hasMoreTokens()) {
            String prefix = tokenizer.nextToken();
            String url = tokenizer.nextToken();

            builder.setNamespace(prefix, url);
        }
    }

    private void addInfoItemValues(ValueFactory vf, ModelBuilder builder) {
        for (InfoItem infoItem : infoItems) {
            String infoItemType = infoItem.getType();
            if (infoItemType == null) continue;

            infoItem.getValues().forEach(value -> {
                builder.add(infoItemType, value.serialize(vf).filter(null, ODF.datavalue, null).objects().iterator().next());
            });
        }
    }
}

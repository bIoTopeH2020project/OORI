package de.fraunhofer.iais.eis.biotope.domainObjs;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Namespaces;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

@XmlRootElement(name="Object")
public class Object {

    private String id;
    private String type;
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

    public Model serialize(ValueFactory vf, String objectBaseIri, String infoItemBaseIri) {

        IRI subject = vf.createIRI(objectBaseIri + id);

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE)
                .setNamespace("org", NS.ORG)
                .subject(subject)
                .add("rdf:type", "odf:Object")
                .add("skos:notation", id);
        
        builder.add("rdf:type",vf.createIRI(type));

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

        Model objectModel = builder.build();
        infoItemModels.forEach(infoItemModel -> objectModel.addAll(infoItemModel));
        nestedObjectsModels.forEach(nestedObjectsModel -> objectModel.addAll(nestedObjectsModel));

        return objectModel;
    }


}

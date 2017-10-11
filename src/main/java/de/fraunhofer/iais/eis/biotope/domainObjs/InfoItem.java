package de.fraunhofer.iais.eis.biotope.domainObjs;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@XmlRootElement
public class InfoItem {

    private String name, type;
    private Collection<Value> values = new ArrayList<>();
    private Collection<MetaData> metaData = new ArrayList<>();

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public Collection<Value> getValues() {
        return values;
    }

    @XmlElement(name = "value")
    public void setValues(Collection<Value> values) {
        this.values = values;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public Collection<MetaData> getMetaData() {
        return metaData;
    }

    @XmlElement(name = "MetaData")
    public void setMetaData(Collection<MetaData> metaData) {
        this.metaData = metaData;
    }


    public IRI serialize(Model model, String infoItemBaseIri) {
        ValueFactory vf = new MemValueFactory();
        IRI subject = vf.createIRI(infoItemBaseIri + name);

        ModelBuilder builder = new ModelBuilder(model);
        builder.subject(subject)
                .add("rdf:type", "odf:InfoItem")
                .add("dct:title", name);

        values.forEach(value -> {
            BNode valueBnode = value.serialize(model);
            builder.add("odf:value", valueBnode);
        });

        metaData.forEach(md -> {
            BNode metadataBnode = md.serialize(model, subject + "/");
            builder.add("odf:metadata", metadataBnode);
        });

        return subject;
    }

}

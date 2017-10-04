package de.fraunhofer.iais.eis.biotope.domainObjs;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.datatypes.XMLDateTime;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Namespaces;
import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.Element;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@XmlRootElement(name = "value")
public class Value {

    private String datetime, type, datavalue;

    public String getDatetime() {
        return datetime;
    }

    @XmlAttribute(name = "dateTime")
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getDatavalue() {
        return datavalue;
    }

    @XmlValue
    public void setDatavalue(String datavalue) {
        this.datavalue = datavalue;
    }

    public Model serialize(ValueFactory vf) {
        Literal createdValue = vf.createLiteral(DatatypeConverter.parseDateTime(datetime).getTime());

        Literal dataValue;
        if (type == null) {
            dataValue = vf.createLiteral(datavalue);
        }
        else {
            dataValue = vf.createLiteral(datavalue, vf.createIRI(type));
        }

        BNode subject = vf.createBNode();

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE)

                .subject(subject)
                .add("rdf:type", "odf:Value")
                .add("dct:created", createdValue)
                .add("odf:dataValue", dataValue);

        return builder.build();
    }
}

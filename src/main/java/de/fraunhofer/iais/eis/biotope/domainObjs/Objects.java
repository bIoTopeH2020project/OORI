package de.fraunhofer.iais.eis.biotope.domainObjs;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.Collection;

@XmlRootElement(name="Objects")
public class Objects {

    private Collection<Object> objects;
    private String uri;

    public Collection<Object> getObjects() {
        return objects;
    }
    
    @XmlAttribute(name = "xmlns")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
    
    @XmlElement(name = "Object")
    public void setObjects(Collection<Object> objects) {
        this.objects = objects;
    }
}

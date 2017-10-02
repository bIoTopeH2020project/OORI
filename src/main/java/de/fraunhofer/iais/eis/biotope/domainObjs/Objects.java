package de.fraunhofer.iais.eis.biotope.domainObjs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@XmlRootElement(name="Objects")
public class Objects {

    private Collection<Object> objects;

    public Collection<Object> getObjects() {
        return objects;
    }

    @XmlElement(name = "Object")
    public void setObjects(Collection<Object> objects) {
        this.objects = objects;
    }
}

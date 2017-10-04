package de.fraunhofer.iais.eis.biotope;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;

import java.io.StringWriter;

public class Util {

    public static String rdfModelToTurtle(Model model) {
        StringWriter turtleWriter = new StringWriter();
        RDFWriter rdfWriter = new TurtleWriter(turtleWriter);
        rdfWriter.startRDF();
        model.getNamespaces().forEach(ns -> rdfWriter.handleNamespace(ns.getPrefix(), ns.getName()));
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
        return turtleWriter.toString();
    }
}

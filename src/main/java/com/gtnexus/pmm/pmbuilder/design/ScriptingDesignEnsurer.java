package com.gtnexus.pmm.pmbuilder.design;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.gtnexus.pmm.pmbuilder.exception.PMBuilderException;

public class ScriptingDesignEnsurer {

    private final DocumentBuilder builder;

    public ScriptingDesignEnsurer() throws ParserConfigurationException {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	builder = factory.newDocumentBuilder();
    }

    public void ensure(File xml, File customObject, boolean js) throws PMBuilderException {
	if (xml == null || customObject == null) {
	    throw new NullPointerException("Cannot ensure design for null xml or null custom object");
	}
	if (!xml.exists()) {
	    throw new PMBuilderException("Xml file " + xml.getAbsolutePath()
		    + " does not exist. Cannot ensure design for script " + customObject.getAbsolutePath() + " .");
	}
	Document xmlDoc;
	ScriptDesignTagModifierFactory factory = new ScriptDesignTagModifierFactory();
	try {
	    xmlDoc = builder.parse(xml);
	    factory.newTagModifierFor(xmlDoc, customObject, false).modifyAsNeeded();
	    xmlDoc.getDocumentElement().normalize();
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(xmlDoc);
	    StreamResult result = new StreamResult(xml);
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.transform(source, result);
	} catch (SAXException | IOException | TransformerException e) {
	    throw new PMBuilderException("Exception when transforming xml!", e);
	}
    }

}

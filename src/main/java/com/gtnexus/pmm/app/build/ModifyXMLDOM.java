package com.gtnexus.pmm.app.build;

import static com.gtnexus.pmm.app.AppXpressConstants.$;
import static com.gtnexus.pmm.app.AppXpressConstants.JS_EXTENSION;
import static com.gtnexus.pmm.app.AppXpressConstants.SCRIPTING_FEATURE;
import static com.gtnexus.pmm.app.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.pmm.app.AppXpressConstants.ZIP_EXTENSION;

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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Modifies an xml file to include a specific tag, that being scriptingFeature
 * 
 * @author Andrew Reynolds
 * @Author john donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
@Deprecated
public class ModifyXMLDOM {

    private static final String XML_DESIGN_TAG = "CustomObjectDesignV110";
    private static final String TEXT_JS = "text/javascript";
    private static final String APPLICATION_ZIP = "application/zip";
    private static final String FILENAME = "fileName";
    private static final String FIELD_MASKING_ENABLED = "fieldMaskingEnabled";
    private static final String MIME_TYPE = "mimeType";
    private static final String DESCRIPTION = "description";
    private static final String GENERIC_FILE_JS = "generic.js";
    private static final String GENERIC_FILE_ZIP = "generic.zip";

    /**
     * @param path
     *            path of xml file
     * @param co
     *            custom object name
     * @param isJS
     *            true if scriptingFeature should indicate a js file false if
     *            scriptingFeature should indicate a zip file
     */
    public static void modify(String path, String co, boolean isJS) {
	File xmlFile = new File(path);
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder;
	try {
	    dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(xmlFile);
	    doc.getDocumentElement().normalize();
	    if (hasScriptingTag(doc)) {
		if (isJS) {
		    updateTagJs(doc, co);
		} else {
		    updateTagZip(doc, co);
		}
	    } else {
		addScriptingTag(doc, co, isJS);
	    }
	    // write the updated document to file or console
	    doc.getDocumentElement().normalize();
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);
	    StreamResult result = new StreamResult(new File(path));
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.transform(source, result);
	    System.out.println("XML file updated successfully");
	} catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) {
	    System.err.println("Cannot find file - > " + xmlFile);
	}
    }

    /**
     * Checks the xml DOM to see if the scriptingFeature tag is included in the tree
     * 
     * @param doc
     *            XML document object to search through
     */
    private static boolean hasScriptingTag(Document doc) {
	NodeList scriptingFeature = doc.getElementsByTagName(SCRIPTING_FEATURE);
	if (scriptingFeature.getLength() == 0) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * Updates the scriptingFeature tag to indicate presence of 1 custom object JS
     * file
     * 
     * @param doc
     *            XML document object
     * @param co
     *            Custom object name
     */
    private static void updateTagJs(Document doc, String co) {
	NodeList scriptingTag = doc.getElementsByTagName(SCRIPTING_FEATURE);
	Element elm = (Element) scriptingTag.item(0);
	Node name = elm.getElementsByTagName(MIME_TYPE).item(0).getFirstChild();
	name.setNodeValue(TEXT_JS);
	String jsName = SCRIPT_DESIGN + $ + co + JS_EXTENSION;
	name = elm.getElementsByTagName(FILENAME).item(0).getFirstChild();
	name.setNodeValue(jsName);
    }

    /**
     * Updates the scriptingFeature tag to indicate presence of more than one custom
     * object JS files - therefore the files must be zipped up in order to properly
     * import
     * 
     * @param doc
     *            XML document object
     * @param co
     *            Name of custom object
     */
    private static void updateTagZip(Document doc, String co) {
	NodeList scriptingTag = doc.getElementsByTagName(SCRIPTING_FEATURE);
	Element elm = (Element) scriptingTag.item(0);
	Node name = elm.getElementsByTagName(MIME_TYPE).item(0).getFirstChild();
	name.setNodeValue(APPLICATION_ZIP);
	String zipName = SCRIPT_DESIGN + $ + co + ZIP_EXTENSION;
	name = elm.getElementsByTagName(FILENAME).item(0).getFirstChild();
	name.setNodeValue(zipName);
    }

    /**
     * Adds scriptingTag to DOM of xml script design. Adds the tag as a jsFile if
     * jsFile is true or a zip file is jsFile is false
     * 
     * @param doc
     *            XML document object
     * @param isJsFile
     *            true if indicating a js file false if indicating a zip file
     * @param customObjectName
     *            Name of custom object
     */
    private static void addScriptingTag(Document doc, String customObjectName, boolean isJsFile) {
	NodeList dom = doc.getElementsByTagName(XML_DESIGN_TAG);
	Node xmlDesignTag = null;
	if (dom != null && (xmlDesignTag = dom.item(0)) != null) {
	    xmlDesignTag.appendChild(generateScriptingTag(doc, customObjectName, isJsFile));
	}
    }

    /**
     * @param doc
     * @param customObjectName
     * @param isJsFile
     */
    private static Node generateScriptingTag(Document doc, String customObjectName, boolean isJsFile) {
	final String _true = "true";
	final String _enabled = "enabled";
	Element scripting = doc.createElement(SCRIPTING_FEATURE);
	Element enabled = doc.createElement(_enabled);
	enabled.appendChild(doc.createTextNode(_true));
	scripting.appendChild(enabled);
	Element mime = doc.createElement(MIME_TYPE);
	mime.appendChild(doc.createTextNode(getMimeType(isJsFile)));
	scripting.appendChild(mime);
	Element description = doc.createElement(DESCRIPTION);
	description.appendChild(doc.createTextNode(getDescriptionType(isJsFile)));
	scripting.appendChild(description);
	Element filename = doc.createElement(FILENAME);
	filename.appendChild(doc.createTextNode(getFileType(isJsFile, customObjectName)));
	scripting.appendChild(filename);
	Element fieldMaskingEnabled = doc.createElement(FIELD_MASKING_ENABLED);
	fieldMaskingEnabled.appendChild(doc.createTextNode("true"));
	scripting.appendChild(fieldMaskingEnabled);
	return scripting;
    }

    private static String getMimeType(boolean isJsFile) {
	if (isJsFile) {
	    return TEXT_JS;
	} else {
	    return APPLICATION_ZIP;
	}
    }

    private static String getDescriptionType(boolean isJsFile) {
	if (isJsFile) {
	    return GENERIC_FILE_JS;
	} else {
	    return GENERIC_FILE_ZIP;
	}
    }

    private static String getFileType(boolean isJsFile, String customObjectName) {
	final String fileTypeTemplate = SCRIPT_DESIGN + $ + customObjectName + "%s";
	if (isJsFile) {
	    return String.format(fileTypeTemplate, JS_EXTENSION);
	} else {
	    return String.format(fileTypeTemplate, ZIP_EXTENSION);
	}
    }

}
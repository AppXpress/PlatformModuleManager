package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.JS_EXTENSION;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTING_FEATURE;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

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
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class ModifyXMLDOM {

	static final String XML_DESIGN_TAG = "CustomObjectDesignV110";
	static final String TEXT_JS = "text/javascript";
	static final String APPLICATION_ZIP = "application/zip";
	static final String FILENAME = "fileName";
	static final String FIELD_MASKING_ENABLED = "fieldMaskingEnabled";
	static final String MIME_TYPE = "mimeType";
	static final String DESCRIPTION = "description";
	static final String GENERIC_FILE_JS = "generic.js";
	static final String GENERIC_FILE_ZIP = "generic.zip";

	/**
	 * @param path
	 *            path of xml file
	 * @param co
	 *            custom object name
	 * @param js
	 *            true if scriptingFeature should indicate a js file false if
	 *            scriptingFeature should indicate a zip file
	 */
	public static void modify(String path, String co, boolean js) {
		File xmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			if (checkTag(doc)) {
				if (js) {
					updateTagJs(doc, co);
				} else {
					updateTagZip(doc, co);
				}
			} else {
				addScriptingTag(doc, co, js);
			}
			// write the updated document to file or console
			doc.getDocumentElement().normalize();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			System.out.println("XML file updated successfully");
		} catch (SAXException | ParserConfigurationException | IOException
				| TransformerException e1) {
			System.err.println("Cannot find file - > " + xmlFile);
		}
	}

	/**
	 * Checks the xml DOM to see if the scriptingFeature tag is included in the
	 * tree
	 * 
	 * @param doc
	 *            XML document object to search through
	 */
	private static boolean checkTag(Document doc) {
		NodeList scriptingFeature = doc
				.getElementsByTagName(SCRIPTING_FEATURE);
		if (scriptingFeature.getLength() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Updates the scriptingFeature tag to indicate presence of 1 custom object
	 * JS file
	 * 
	 * @param doc
	 *            XML document object
	 * @param co
	 *            Custom object name
	 */
	private static void updateTagJs(Document doc, String co) {
		NodeList scriptingTag = doc.getElementsByTagName(SCRIPTING_FEATURE);
		Element elm = (Element) scriptingTag.item(0);
		Node name = elm.getElementsByTagName(MIME_TYPE).item(0)
				.getFirstChild();
		name.setNodeValue(TEXT_JS);
		String jsName = SCRIPT_DESIGN + $ + co + JS_EXTENSION;
		name = elm.getElementsByTagName(FILENAME).item(0).getFirstChild();
		name.setNodeValue(jsName);
	}

	/**
	 * Updates the scriptingFeature tag to indicate presence of more than one
	 * custom object JS files - therefore the files must be zipped up in order
	 * to properly import
	 * 
	 * @param doc
	 *            XML document object
	 * @param co
	 *            Name of custom object
	 */
	private static void updateTagZip(Document doc, String co) {
		NodeList scriptingTag = doc.getElementsByTagName(SCRIPTING_FEATURE);
		Element elm = (Element) scriptingTag.item(0);
		Node name = elm.getElementsByTagName(MIME_TYPE).item(0)
				.getFirstChild();
		name.setNodeValue(APPLICATION_ZIP);
		String zipName = SCRIPT_DESIGN + $ + co + ZIP_EXTENSION;
		name = elm.getElementsByTagName(FILENAME).item(0).getFirstChild();
		name.setNodeValue(zipName);
	}

	/**
	 * Adds scriptingTag to DOM of xml script design. Adds the tag as a jsFile
	 * if jsFile is true or a zip file is jsFile is false
	 * 
	 * @param doc
	 *            XML document object
	 * @param jsFile
	 *            true if indicating a js file false if indicating a zip file
	 * @param co
	 *            Name of custom object
	 */
	private static void addScriptingTag(Document doc, String co, boolean jsFile) {
		Element scripting = doc.createElement(SCRIPTING_FEATURE);
		Element enabled = doc.createElement("enabled");
		enabled.appendChild(doc.createTextNode("true"));
		scripting.appendChild(enabled);
		Element mime = doc.createElement(MIME_TYPE);
		String type;
		mime.appendChild(doc.createTextNode(getMimeType(jsFile)));
		scripting.appendChild(mime);
		Element desc = doc.createElement(DESCRIPTION);
		if (jsFile) {
			type = GENERIC_FILE_JS;
		} else {
			type = GENERIC_FILE_ZIP;
		}
		desc.appendChild(doc.createTextNode(type));
		scripting.appendChild(desc);
		Element fn = doc.createElement(FILENAME);
		if (jsFile) {
			type = SCRIPT_DESIGN + $ + co + JS_EXTENSION;
		} else {
			type = SCRIPT_DESIGN + $ + co + ZIP_EXTENSION;
		}
		fn.appendChild(doc.createTextNode(type));
		scripting.appendChild(fn);
		Element fme = doc.createElement(FIELD_MASKING_ENABLED);
		fme.appendChild(doc.createTextNode("true"));
		scripting.appendChild(fme);
		try {
			NodeList dom = doc.getElementsByTagName(XML_DESIGN_TAG);
			dom.item(0).appendChild(scripting);
		} catch (NullPointerException e) {
			System.err.println("Cannot find tag " + XML_DESIGN_TAG);
		}
	}
	
	private static String getMimeType(boolean isJsFile) {
		if (isJsFile) {
			return TEXT_JS;
		} else {
			return APPLICATION_ZIP;
		}
	}
	
	private String getDescription(boolean isJsFile) {
		return null;
	}
}
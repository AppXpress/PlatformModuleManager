package com.gtnexus.pmm.app.build.design;

import static com.gtnexus.pmm.app.AppXpressConstants.$;
import static com.gtnexus.pmm.app.AppXpressConstants.SCRIPTING_FEATURE;
import static com.gtnexus.pmm.app.AppXpressConstants.SCRIPT_DESIGN;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AppXpressScriptDesignTagAppender implements ScriptDesignTagModifier {

    private final Document xml;
    private final File customObject;
    private final String fileExtension;
    private final String mimeTypeValue;
    private final String genericFileName;

    AppXpressScriptDesignTagAppender(Document xml, File customObject, String fileExtension, String mimeTypeValue,
	    String genericFileName) {
	this.xml = xml;
	this.customObject = customObject;
	this.fileExtension = fileExtension;
	this.mimeTypeValue = mimeTypeValue;
	this.genericFileName = genericFileName;
    }

    @Override
    public void modifyAsNeeded() {
	addScriptingTag(xml, customObject.getName());
    }

    private void addScriptingTag(Document doc, String customObjectName) {
	NodeList dom = doc.getElementsByTagName(XML_DESIGN_TAG);
	Node xmlDesignTag = null;
	if (dom != null && (xmlDesignTag = dom.item(0)) != null) {
	    xmlDesignTag.appendChild(generateScriptingTag(doc, customObjectName));
	}
    }

    /**
     * @param doc
     * @param customObjectName
     * @param isJsFile
     */
    private Node generateScriptingTag(Document doc, String customObjectName) {
	final String _true = "true";
	final String _enabled = "enabled";
	final Element scripting = doc.createElement(SCRIPTING_FEATURE);
	final Element enabled = doc.createElement(_enabled);
	enabled.appendChild(doc.createTextNode(_true));
	scripting.appendChild(enabled);
	final Element mime = doc.createElement(MIME_TYPE);
	mime.appendChild(doc.createTextNode(mimeTypeValue));
	scripting.appendChild(mime);
	final Element description = doc.createElement(DESCRIPTION);
	description.appendChild(doc.createTextNode(genericFileName));
	scripting.appendChild(description);
	final Element filename = doc.createElement(FILENAME);
	filename.appendChild(doc.createTextNode(buildFileName(customObjectName)));
	scripting.appendChild(filename);
	final Element fieldMaskingEnabled = doc.createElement(FIELD_MASKING_ENABLED);
	fieldMaskingEnabled.appendChild(doc.createTextNode("true"));
	scripting.appendChild(fieldMaskingEnabled);
	return scripting;
    }

    private String buildFileName(String customObjectName) {
	final String fileTypeTemplate = SCRIPT_DESIGN + $ + customObjectName + "%s";
	return String.format(fileTypeTemplate, fileExtension);
    }

}

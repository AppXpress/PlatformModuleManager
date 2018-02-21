package com.gtnexus.appxpress.pmbuilder.design;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTING_FEATURE;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AppXpressScriptDesignTagModifier implements ScriptDesignTagModifier {

    private final Document xml;
    private final File customObject;
    private final String fileExtension;
    private final String mimeTypeValue;

    AppXpressScriptDesignTagModifier(Document xml, File customObject, final String fileExtension,
	    final String mimeTypeValue) {
	this.xml = xml;
	this.customObject = customObject;
	this.fileExtension = fileExtension;
	this.mimeTypeValue = mimeTypeValue;
    }

    @Override
    public void modifyAsNeeded() {
	updateTag(xml, customObject.getName());
    }

    public void updateTag(Document doc, String customObjectName) {
	NodeList scriptingTag = doc.getElementsByTagName(SCRIPTING_FEATURE);
	Element elm = (Element) scriptingTag.item(0);
	Node name = elm.getElementsByTagName(MIME_TYPE).item(0).getFirstChild();
	name.setNodeValue(mimeTypeValue);
	String fileName = SCRIPT_DESIGN + $ + customObjectName + fileExtension;
	name = elm.getElementsByTagName(FILENAME).item(0).getFirstChild();
	name.setNodeValue(fileName);
    }

}
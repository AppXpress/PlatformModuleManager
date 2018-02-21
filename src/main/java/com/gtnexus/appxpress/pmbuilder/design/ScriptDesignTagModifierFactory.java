package com.gtnexus.appxpress.pmbuilder.design;

import static com.gtnexus.appxpress.AppXpressConstants.JS_EXTENSION;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTING_FEATURE;
import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ScriptDesignTagModifierFactory {

    private static final String TEXT_JS = "text/javascript";
    private static final String APPLICATION_ZIP = "application/zip";
    private static final String GENERIC_FILE_JS = "generic.js";
    private static final String GENERIC_FILE_ZIP = "generic.zip";

    private static final FileDescriptorTriplet zipDescriptor = new FileDescriptorTriplet(ZIP_EXTENSION, APPLICATION_ZIP,
	    GENERIC_FILE_ZIP);
    private static final FileDescriptorTriplet jsDescriptor = new FileDescriptorTriplet(JS_EXTENSION, TEXT_JS,
	    GENERIC_FILE_JS);

    public ScriptDesignTagModifierFactory() {
    }

    public ScriptDesignTagModifier newTagModifierFor(final Document xml, final File customObject, final boolean isJS) {
	if (isJS) {
	    return buildModifier(xml, customObject, jsDescriptor);
	} else {
	    return buildModifier(xml, customObject, zipDescriptor);
	}
    }

    private ScriptDesignTagModifier buildModifier(final Document xml, final File customObject,
	    final FileDescriptorTriplet desc) {
	if (hasScriptingTag(xml)) {
	    return new AppXpressScriptDesignTagModifier(xml, customObject, desc.fileExtension, desc.mimeType);
	} else {
	    return new AppXpressScriptDesignTagAppender(xml, customObject, desc.fileExtension, desc.mimeType,
		    desc.genericFileName);
	}
    }

    /**
     * Checks the xml DOM to see if the scriptingFeature tag is included in the tree
     * 
     * @param doc
     *            XML document object to search through
     */
    private boolean hasScriptingTag(final Document doc) {
	final NodeList scriptingFeature = doc.getElementsByTagName(SCRIPTING_FEATURE);
	if (scriptingFeature.getLength() == 0) {
	    return false;
	} else {
	    return true;
	}
    }

    private static class FileDescriptorTriplet {
	final String fileExtension, mimeType, genericFileName;

	FileDescriptorTriplet(final String fileExtension, final String mimeType, final String genericFileName) {
	    this.fileExtension = fileExtension;
	    this.mimeType = mimeType;
	    this.genericFileName = genericFileName;
	}

    }
}

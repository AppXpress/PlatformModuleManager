package com.gtnexus.appxpress.pmbuilder;

import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

import java.io.File;

import static com.gtnexus.appxpress.AppXpressConstants.*;

/**
 * Ensures that custom object designs xml match up with their scripting
 * 
 * @author Andrew Reynolds
 * @author Eric Hu
 * @author John Donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class CoDesignXML {

	private String root;
	private String designPath;
	private final String designPathTemplate = "%s" + File.separator
			+ CUSTOM_OBJECT_MODULE + File.separator + "designs";
	private String scriptPath;
	private final String scriptPathTemplate = designPathTemplate
			+ File.separator + "designs";

	public CoDesignXML(String root) {
		this.root = root;
		this.designPath = String.format(designPathTemplate, root);
		this.scriptPath = String.format(scriptPathTemplate, root);
	}

	/**
	 * Iterates through a platform module
	 * 
	 * @param customerFolder
	 *            Customer folder where platform module exists
	 * @param pmFolder
	 *            Platform module folder
	 */
	//TODO iter() is a horrible method name. No iteration is done here.
	public void iter() throws PMBuilderException {
		File rootFile = new File(root);
		if (!rootFile.exists()) {
			throw new PMBuilderException("Cannot find path to customer: "
					+ rootFile.getAbsolutePath());
		}
		File scriptDir  = new File(scriptPath);
		if (!scriptDir.exists() || !scriptDir.isDirectory()) {
			System.out.println("No scripts folder, must not be any CO scripts");
			return;
		}
		checkXML(scriptDir);
	}

	/**
	 * Checks to see if xml matches with scripts for custom object
	 */
	private void checkXML(File scriptDir) {
		for (String s : scriptDir.list()) {
			File sub = new File(scriptPath + File.separator + s);
			if (!sub.exists()) {
				System.err.println("abort " + scriptPath + File.separator + s);
				break;
			}
			System.out.println(scriptPath + File.separator + s);
			String xmlName = designPath + File.separator + DESIGN_ + s
					+ XML_EXTENSION;
			System.out.println("xml " + xmlName);
			System.out.println(sub.list().length);
			if (sub.list().length == 1) {	//TODO how does this make any sense?
											//are .js file name garunteed to be 
											//one char?...I don't think so...
				ModifyXMLDOM.modify(xmlName, s, true); //if more than one we bundle that is the logic there.
			} else {
				ModifyXMLDOM.modify(xmlName, s, false);
			}
		}
	}
}
package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_OBJECT_MODULE;
import static com.gtnexus.appxpress.AppXpressConstants.DESIGN_;
import static com.gtnexus.appxpress.AppXpressConstants.XML_EXTENSION;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * Ensures that custom object designs xml match up with their scripting
 * 
 * @author Andrew Reynolds
 * @author Eric Hu
 * @author John Donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class CustomObjectDesignXML {

	private String root;
	private String designPath;
	private final String designPathTemplate = "%s" + File.separator
			+ CUSTOM_OBJECT_MODULE + File.separator + "designs";
	private String scriptPath;
	private final String scriptPathTemplate = designPathTemplate
			+ File.separator + "designs";

	public CustomObjectDesignXML(String root) {
		this.root = root;
		this.designPath = String.format(designPathTemplate, root);
		this.scriptPath = String.format(scriptPathTemplate, root);
	}

	private File moduleRoot;
	private File designDirectory;
	private File scriptDirectory;

	public CustomObjectDesignXML(File root) {

	}

	/**
	 * Iterates through a platform module
	 * 
	 * @param customerFolder
	 *            Customer folder where platform module exists
	 * @param pmFolder
	 *            Platform module folder
	 */
	// TODO iter() is a horrible method name. No iteration is done here.
	public void iter() throws PMBuilderException {
		File rootFile = new File(root);
		if (!rootFile.exists()) {
			throw new PMBuilderException("Cannot find path to customer: "
					+ rootFile.getAbsolutePath());
		}
		File scriptDir = new File(scriptPath);
		if (!scriptDir.exists() || !scriptDir.isDirectory()) {
			System.out.println("No scripts folder, must not be any CO scripts");
			return;
		}
		checkXML(scriptDir);
	}

	public void ensureSoundDesign() throws PMBuilderException {
		ScriptingDesignEnsurer ensurer;
		try {
			ensurer = new ScriptingDesignEnsurer();
		} catch (ParserConfigurationException e) {
			throw new PMBuilderException(
					"Something went wrong. Could not ensure a sound design.", e);
		}
		for (File script : scriptDirectory.listFiles()) {
			File correspondingXML = null;
			ensurer.ensure(correspondingXML, script, script.list().length == 1);
		}
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
			if (sub.list().length == 1) { // if there is only 1 file in the
											// directory
				ModifyXMLDOM.modify(xmlName, s, true); // if more than one we
														// bundle, that is the
														// logic there.
			} else {
				ModifyXMLDOM.modify(xmlName, s, false);
			}
		}
	}
}
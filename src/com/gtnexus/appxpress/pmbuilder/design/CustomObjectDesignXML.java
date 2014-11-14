package com.gtnexus.appxpress.pmbuilder.design;

import static com.gtnexus.appxpress.AppXpressConstants.$;
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

	private final String designPathTemplate = "%s" + File.separator
			+ CUSTOM_OBJECT_MODULE + File.separator + "designs";
	private final String scriptPathTemplate = designPathTemplate
			+ File.separator + "Scripts";
	private File moduleRoot;
	private File designDirectory;
	private File scriptDirectory;

	/**
	 * 
	 * @param root
	 *            - the root for this customer/module
	 */
	public CustomObjectDesignXML(File root) {
		this.moduleRoot = root;
		this.designDirectory = new File(String.format(designPathTemplate,
				root.getAbsolutePath()));
		this.scriptDirectory = new File(String.format(scriptPathTemplate,
				root.getAbsolutePath()));
	}

	/**
	 * 
	 * @throws PMBuilderException
	 *             If the module root does not exist, or there
	 *             ParserConfigurationException.
	 */
	public void ensureSoundDesign() throws PMBuilderException {
		if (!moduleRoot.exists()) {
			throw new PMBuilderException("Cannot find path to customer: "
					+ moduleRoot.getAbsolutePath());
		}
		if (!preConditionsMet()) {
			return;
		}
		ScriptingDesignEnsurer ensurer;
		try {
			ensurer = new ScriptingDesignEnsurer();
		} catch (ParserConfigurationException e) {
			throw new PMBuilderException(
					"Something went wrong. Could not ensure a sound design.", e);
		}
		checkEachScriptAndDesign(ensurer);
	}

	/**
	 * Asserts that the necessary directories exist. In order for the scripts
	 * directory to exist the design directory must exist, so a single check is
	 * all the is necessary.
	 * 
	 * @return
	 */
	private boolean preConditionsMet() {
		if (!scriptDirectory.exists() || !scriptDirectory.isDirectory()) {
			System.out
					.println("No scripts folder, must not be any CustomObject scripts");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param ensurer
	 */
	private void checkEachScriptAndDesign(ScriptingDesignEnsurer ensurer) throws PMBuilderException {
		final String correspondingXMLPathTemplate = designDirectory
				.getAbsolutePath() + File.separator + DESIGN_ + $ + "%s" + XML_EXTENSION;
		for (File script : scriptDirectory.listFiles()) {
			File correspondingXML = new File(String.format(
					correspondingXMLPathTemplate, script.getName()));
			ensurer.ensure(correspondingXML, script, script.list().length == 1);
		}
	}

}
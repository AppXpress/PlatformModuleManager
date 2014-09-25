package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_OBJECT_MODULE;
import static com.gtnexus.appxpress.AppXpressConstants.DESIGN_;
import static com.gtnexus.appxpress.AppXpressConstants.XML_EXTENSION;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOMER;

import java.io.File;

/**
 * Ensures that custom object designs xml match up with their scripting
 * 
 * @author Andrew Reynolds
 * @author Eric Hu
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class CoDesignXML {

	private  String designPath;
	private  String scriptPath;

	public CoDesignXML(){}

	/**
	 * Iterates through a platform module
	 * 
	 * @param customerFolder
	 *            Customer folder where platform module exists
	 * @param pmFolder
	 *            Platform module folder
	 */
	public void iter(String customerFolder, String pmFolder) {
		String path = CUSTOMER + File.separator + customerFolder + File.separator + pmFolder;
		File exist = new File(path);
		if (!exist.exists()) {
			System.out.println("Cannot find path customer" + File.separator + customerFolder
					+ File.separator + pmFolder);
			return;
		}
		scriptPath = path + File.separator + CUSTOM_OBJECT_MODULE + File.separator + "designs" + File.separator
				+ "Scripts";
		exist = new File(scriptPath);
		if (!exist.exists()) {
			System.out.println("No scripts folder, must not be any CO scripts");
			return;
		}
		designPath = path + File.separator + CUSTOM_OBJECT_MODULE + File.separator + "designs";
		checkXML();
	}

	/**
	 * Checks to see if xml matches with scripts for custom object
	 */
	private  void checkXML() {
		File scripts = new File(scriptPath);
		for (String s : scripts.list()) {
			File sub = new File(scriptPath + File.separator + s);
			if (!sub.exists()) {
				System.err.println("abort " + scriptPath + File.separator + s);
				break;
			}
			System.out.println(scriptPath + File.separator + s);
			String xmlName = designPath + File.separator + DESIGN_ + s + XML_EXTENSION;
			System.out.println("xml " + xmlName);
			System.out.println(sub.list().length);
			if (sub.list().length == 1) {
				ModifyXMLDOM.modify(xmlName, s, true);
			} else {
				ModifyXMLDOM.modify(xmlName, s, false);
			}
		}
	}
}
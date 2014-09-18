package com.gtnexus.appxpress.pmbuilder;

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
	public static String designPath;
	public static String scriptPath;

	public static final String SEP = File.pathSeparator;

	/**
	 * Iterates through a platform module
	 * 
	 * @param customer
	 *            Customer folder where platform module exists
	 * @param folder
	 *            Platform module folder
	 */
	public static void iter(String customer, String folder) {
		String path = "customer" + SEP + customer + SEP + folder;
		File exist = new File(path);
		if (!exist.exists()) {
			System.out.println("Cannot find path customer" + SEP + customer
					+ SEP + folder);
			return;
		}
		scriptPath = path + SEP + "CustomObjectModule" + SEP + "designs" + SEP
				+ "Scripts";
		exist = new File(scriptPath);
		if (!exist.exists()) {
			System.out.println("No scripts folder, must not be any CO scripts");
			return;
		}
		designPath = path + SEP + "CustomObjectModule" + SEP + "designs";
		checkXML();
	}

	/**
	 * Checks to see if xml matches with scripts for custom object
	 */
	private static void checkXML() {
		try {
			File scripts = new File(scriptPath);
			for (String s : scripts.list()) {
				File sub = new File(scriptPath + SEP + s);
				if (!sub.exists()) {
					System.err.println("abort " + scriptPath + SEP + s);
					break;
				}
				System.out.println(scriptPath + SEP + s);
				String xmlName = designPath + SEP + "Design_" + s + ".xml";
				System.out.println("xml " + xmlName);
				System.out.println(sub.list().length);
				if (sub.list().length == 1) {
					ModifyXMLDOM.modify(xmlName, s, true);
				} else {
					ModifyXMLDOM.modify(xmlName, s, false);
				}
			}
		} catch (Throwable t) {
			System.err.println("Check XML exception");
		}
	}

}
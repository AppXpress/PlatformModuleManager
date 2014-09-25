package com.gtnexus.appxpress.pmbuilder;

import java.io.File;
import java.io.IOException;

import static com.gtnexus.appxpress.AppXpressConstants.CUSTOMER;

/**
 * This executable does the following
 * things in order with the end goal to create an importable .zip file.
 * 
 * 1) 	Scans the js scripts in the platform module for the <b>!import symbol</b>. If
 * 		found, automatically imports indicated scripts from lib folder into correct
 * 		folder. 
 * 2) 	Makes sure each of the custom object design xml's contain the
 * 		correct scriptingFeature tag, therefore ensuring that the platform module's
 * 		scripts will import correctly. 
 * 3) 	Maps the local git structure to the folder
 * 		structure required to become importable by GTNexus platform. This requires
 * 		correct folder names and bundling javascript file into zip files when necessary.
 * 4) 	Zips up correctly mapped file structure into a zip file, ready to import
 * 		into GTNexus system
 * 
 * @author Andrew Reynolds
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class PlatformModuleBuilder {

	/**
	 * Main method of pm_builder_util.jar. Instantiates PlatfromModuleBuilder.
	 * @param args
	 *            0 - Name of customer folder 
	 *            1 - Name of platform module folder
	 */
	public static void main(String args[]) {
		if (args.length != 2) {
			System.err
					.println("Incorrect parameters. Program requires two parameters");
			return;
		}
		PlatformModuleBuilder pmb = new PlatformModuleBuilder(args[0], args[1]);
		pmb.build();
	}

	
	private String customer, module, root;
	
	/**
	 * Inputs recently pulled down git repository and outputs zip file that is
	 * ready to be imported onto GTNexus system
	 * 
	 * @param customer
	 *            Name of customer folder
	 * @param module
	 *            Name of platform module folder
	 */
	public PlatformModuleBuilder(String customer, String module) {
		this.customer = customer;
		this.module = module;
		this.root = CUSTOMER + File.separator + customer 
				+ File.separator + module;
		
	}

	public void build() {
		runImportFind();
		xmlDesignCustomObjectScriptMatcher(customer, module);
		map(root);
		ZipUtility zu = new ZipUtility(root);
		try {
			zu.zipDirectory();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Maps pulled platform module to a file structure that can be imported onto
	 * GTNexus system
	 * 
	 * @param root
	 *            File path of platform module
	 */
	private void map(String root) {
		PlatformMapUtil.map(root);
	}

	/**
	 * Searches through the custom object module folder and ensures that each
	 * custom object design xml file corresponds to the correct number of custom
	 * object scripts
	 * 
	 * @param customer
	 *            Name of customer folder
	 * @param platform
	 *            Name of platform module folder
	 */
	private void xmlDesignCustomObjectScriptMatcher(String customer,
			String platform) {
		CoDesignXML coDes = new CoDesignXML();
		coDes.iter(customer, platform);
	}

	/**
	 * Iterates through folder customer/customer/folder
	 * 
	 * @param customer
	 *            Name of customer folder
	 * @param folder
	 *            Name of platform module folder
	 */
	private void runImportFind() {
		System.out.println("Gathering imports...");
		ImportScanner iScanner = new ImportScanner();
		iScanner.search(root);
	}
}

package com.gtnexus.appxpress.pmbuilder;

import static com.gtnexus.appxpress.AppXpressConstants.CUSTOMER;

import java.io.File;

import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * This executable does the following things in order with the end goal to
 * create an importable .zip file.
 * 
 * <ol>
 * <li>
 * 1) Scans the javascripts in the platform module for the <b>!import
 * symbol</b>. If found, automatically imports indicated scripts from lib/ into
 * correct folder.</li>
 * <li>
 * 2) Makes sure each of the custom object design xml's contain the correct
 * scriptingFeature tag, therefore ensuring that the platform module's scripts
 * will import correctly.</li>
 * <li>
 * 3) Maps the local git structure to the folder structure required to become
 * importable by GTNexus platform. This requires correct folder names and
 * bundling javascript file into zip files when necessary.</li>
 * <li>
 * 4) Zips up correctly mapped file structure into a zip file, ready to import
 * into GTNexus system</li>
 * </ol>
 *
 * @author Andrew Reynolds
 * @author john donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
public class PlatformModuleBuilder {

	/**
	 * Main method of pm_builder_util.jar. Instantiates PlatfromModuleBuilder.
	 * 
	 * @param args
	 *            0 - Name of customer folder 1 - Name of platform module folder
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
	private File rootFile;

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
		this.root = CUSTOMER + File.separator + customer + File.separator
				+ module;
		this.rootFile = new File(root);
	}

	public void build() {
		try {
			runImportFind();
			xmlDesignCustomObjectScriptMatcher();
			map();
			ZipService zs = new ZipService();
			zs.zipDirectory(rootFile);
		} catch (PMBuilderException e) {
			System.out.println("Failure when building module for [customer: "
					+ customer + " module: " + module + "]");
			System.out.println(e.getMessage());
		}
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
		ImportService iScanner = new ImportService(rootFile);
		iScanner.scanAndImport();
	}

	/**
	 * Searches through the custom object module folder and ensures that each
	 * custom object design xml file corresponds to the correct number of custom
	 * object scripts
	 */
	private void xmlDesignCustomObjectScriptMatcher() throws PMBuilderException {
		CustomObjectDesignXML coDes = new CustomObjectDesignXML(rootFile);
		coDes.ensureSoundDesign();
	}

	/**
	 * Maps pulled platform module to a file structure that can be imported onto
	 * GTNexus system
	 * 
	 * @param root
	 *            File path of platform module
	 */
	private void map() {
		Mapper mapper = new AppXpressMapper(rootFile);
		mapper.doMapping();
	}

}

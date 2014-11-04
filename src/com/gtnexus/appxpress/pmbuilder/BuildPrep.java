package com.gtnexus.appxpress.pmbuilder;

import java.io.File;

import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.pmbuilder.bundle.Preparation;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

public class BuildPrep implements Preparation {

	@Override
	public void prepare(File rootFile) throws PMBuilderException {
		runImportFind(rootFile);
		xmlDesignCustomObjectScriptMatcher(rootFile);
		map(rootFile);
	}
	
	/**
	 * Iterates through folder customer/customer/folder
	 * 
	 * @param customer
	 *            Name of customer folder
	 * @param folder
	 *            Name of platform module folder
	 */
	private void runImportFind(File rootFile) {
		System.out.println("Gathering imports...");
		ImportService iScanner = new ImportService(rootFile);
		iScanner.scanAndImport();
	}

	/**
	 * Searches through the custom object module folder and ensures that each
	 * custom object design xml file corresponds to the correct number of custom
	 * object scripts
	 */
	private void xmlDesignCustomObjectScriptMatcher(File rootFile) throws PMBuilderException {
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
	private void map(File rootFile) {
		Mapper mapper = new AppXpressMapper(rootFile);
		mapper.doMapping();
	}

}

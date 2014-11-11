package com.gtnexus.appxpress.pmbuilder.bundle.platform;

import java.io.File;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.Preparation;
import com.gtnexus.appxpress.pmbuilder.AppXpressMapper;
import com.gtnexus.appxpress.pmbuilder.design.CustomObjectDesignXML;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;
import com.gtnexus.appxpress.pmbuilder.scriptimport.ImportService;

public class BuildPrep implements Preparation<File> {

	@Override
	public void prepare(File rootFile) throws PMBuilderException {
		runImportFind(rootFile);
		xmlDesignCustomObjectScriptMatcher(rootFile);
		try {
			map(rootFile);
		} catch (AppXpressException e) {
			throw new PMBuilderException(
					"Exception when mapping file structure.", e);
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
	private void xmlDesignCustomObjectScriptMatcher(File rootFile)
			throws PMBuilderException {
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
	private void map(File rootFile) throws AppXpressException {
		Mapper mapper = new AppXpressMapper(rootFile);
		mapper.doMapping();
	}

}

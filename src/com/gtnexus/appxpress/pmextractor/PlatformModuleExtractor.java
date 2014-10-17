package com.gtnexus.appxpress.pmextractor;

import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

import java.io.IOException;
import java.util.Map;

/**
 * 
 * @author jdonovan
 *
 */
public class PlatformModuleExtractor {

	/**
	 * Takes 4-6 arguments -->
	 *
	 *@param args
	 * [0] Exported Platform Module Name
	 * [1] Relative Path of GIT staging folder
	 * [2] Customer of Platform Module
	 * [3] Platform Module that is being exported
	 * 
	 *          Optional args
	 * [4] If Y -> overwriteScripts = true
	 * [5] If Y -> overwriteFEF = true
	 */
	public static void main(String args[]) throws IOException {
        PlatformModuleExtractor extractor = new PlatformModuleExtractor(args);
        extractor.extract();
	}

    private String[] userArgs;

    public PlatformModuleExtractor(String[] userArgs){
        this.userArgs = userArgs;
    }

    public void extract() {
        DirectoryHelper dHelper = new DirectoryHelper();
        try {
            dHelper.ensureAppXpress();
            PMBProperties pmbProperties = dHelper.getPmbProperties();
            ArgsAndPropertiesConsolidator consolidator = new ArgsAndPropertiesConsolidator(
                    userArgs, pmbProperties.getProperties());
            Map<ExtractorOption, String> optMap = consolidator.consolidate();
            GitMap tool = GitMap.createMapper(optMap);
            tool.doMapping();
            consolidator.presentSaveOption(pmbProperties.getPropertiesPath());
        } catch (PMExtractorException e) {
            System.err.println(e.getMessage());
        }
    }
}

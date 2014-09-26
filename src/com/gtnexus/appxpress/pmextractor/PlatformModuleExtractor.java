package com.gtnexus.appxpress.pmextractor;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author jdonovan
 *
 */
public class PlatformModuleExtractor {

	/**
	 * Takes 4-6 args ->
	 * 
	 * @args[0] Exported Platform Module Name
	 * @args[1] Relative Path of GIT staging folder
	 * @args[2] Customer of Platform Module
	 * @args[3] Platform Module that is being exported
	 * 
	 *          Optional args
	 * @args[4] If Y -> overwriteScripts = true
	 * @args[5] If Y -> overwriteFEF = true
	 */
	public static void main(String args[]) throws IOException {
		DirectoryHelper dHelper = new DirectoryHelper();
		dHelper.getAndEnsureAppXPressDirectoryExists();
		Properties properties = dHelper.getAndEnsurePropertiesExist();
		ArgsAndPropertiesConsolidator consolidator = new ArgsAndPropertiesConsolidator(
				args, properties);
		Map<ExtractorOption, String> optMap = consolidator.consolidate();
		GitMap tool = new GitMap(optMap);
		tool.doMapping();
		consolidator.presentSaveOption(dHelper.getPropertiesFilePath());
	}

}

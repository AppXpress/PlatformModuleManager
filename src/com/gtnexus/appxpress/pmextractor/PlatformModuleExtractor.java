package com.gtnexus.appxpress.pmextractor;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;

import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.cli.CommandLineInterfaceParser;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

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
        try {
        	extractor.run();
        } catch (PMExtractorException e) {
        	System.err.println("Failure when running pmextractor.");
        }
	}

    private String[] userArgs;

    public PlatformModuleExtractor(String[] userArgs){
        this.userArgs = userArgs;
    }

	/**
	 * 
	 * @throws PMExtractorException
	 *             if CommandLine is not parsable, or if extraction cannot be
	 *             performed.
	 */
	public void run() throws PMExtractorException {
		CommandLineInterfaceParser<ExtractorOption> cli = new CommandLineInterfaceParser<>(
				userArgs, EnumSet.allOf(ExtractorOption.class));
		cli.parseCommandLine();
		if (cli.hasOption(ExtractorOption.HELP)) {
			cli.displayHelpAndExit();
		} else {
			performExtraction(cli);
		}
	}

	private void performExtraction(
			CommandLineInterfaceParser<ExtractorOption> cli)
			throws PMExtractorException {
		DirectoryHelper dHelper = new DirectoryHelper();
		dHelper.ensureAppXpress();
		PMBProperties pmbProperties = dHelper.getPmbProperties();
		ArgsAndPropertiesConsolidator<ExtractorOption> consolidator = new ArgsAndPropertiesConsolidator<>(
				cli.getOptionsMap(), cli.getCliOptionSet(),
				pmbProperties.getProperties());
		Map<ExtractorOption, String> optMap = consolidator.consolidate();
		Mapper tool = GitMap.createMapper(optMap);
		tool.doMapping();
		consolidator.presentSaveOption(pmbProperties.getPropertiesPath());
	}
    
}

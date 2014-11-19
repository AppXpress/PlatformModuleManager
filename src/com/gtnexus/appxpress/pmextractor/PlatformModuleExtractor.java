package com.gtnexus.appxpress.pmextractor;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.DirectoryHelper;
import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.cli.CommandLineInterfaceParser;
import com.gtnexus.appxpress.pmextractor.cli.ArgsAndPropertiesConsolidator;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;
import com.gtnexus.appxpress.pmextractor.gitmap.GitMapper;

import static com.gtnexus.appxpress.AppXpressConstants.PROPERTIES_EXTENSION;

/**
 * 
 * @author jdonovan
 *
 */
public class PlatformModuleExtractor {

	/**
	 * Takes 4-6 arguments -->
	 *
	 * @param args
	 *            [0] Exported Platform Module Name [1] Relative Path of GIT
	 *            staging folder [2] Customer of Platform Module [3] Platform
	 *            Module that is being exported
	 * 
	 *            Optional args [4] If Y -> overwriteScripts = true [5] If Y ->
	 *            overwriteFEF = true
	 */
	public static void main(String args[]) throws IOException {
		PlatformModuleExtractor extractor = new PlatformModuleExtractor(args);
		try {
			extractor.extract();
		} catch (PMExtractorException e) {
			System.err.println("Failure when running pmextractor.");
			System.err.println(e.getMessage());
		}
	}

	private static final String NAME = "pmextractor";
	private String[] userArgs;

	public PlatformModuleExtractor(String[] userArgs) {
		this.userArgs = userArgs;
	}

	/**
	 * 
	 * @throws PMExtractorException
	 *             if CommandLine is not parsable, or if extraction cannot be
	 *             performed.
	 */
	public void extract() throws PMExtractorException {
		CommandLineInterfaceParser<ExtractorOption> cli = new CommandLineInterfaceParser<>(
				NAME, userArgs, EnumSet.allOf(ExtractorOption.class));
		cli.parseCommandLine();
		if (cli.hasOption(ExtractorOption.HELP)) {
			cli.displayHelpAndExit();
		}
		try {
			performExtraction(cli);
		} catch (AppXpressException e) {
			throw new PMExtractorException(
					"Error when mapping to git structure.", e);
		}
	}

	private void performExtraction(
			CommandLineInterfaceParser<ExtractorOption> cli)
			throws AppXpressException {
		DirectoryHelper dHelper = new DirectoryHelper(NAME + PROPERTIES_EXTENSION);
		dHelper.ensureAppXpress();
		PMProperties pmbProperties = dHelper.getPmProperties();
		ArgsAndPropertiesConsolidator<ExtractorOption> consolidator = new ArgsAndPropertiesConsolidator<>(
				cli.getOptionsMap(), cli.getCliOptionSet(),
				pmbProperties.getProperties());
		Map<ExtractorOption, String> optMap = consolidator.consolidate();
		Mapper tool = GitMapper.createMapper(optMap);
		System.out.println("Mapping...");
		tool.doMapping();
		consolidator.presentSaveOption(pmbProperties.getPropertiesPath());
	}

}

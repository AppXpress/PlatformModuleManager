package com.gtnexus.appxpress.pmextractor;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
        	
        }
	}

    private String[] userArgs;
    private Options options;
    private final Set<ExtractorOption> extractorOptions;

    public PlatformModuleExtractor(String[] userArgs){
        this.userArgs = userArgs;
    	this.options = new Options();
    	this.extractorOptions = EnumSet
                .allOf(ExtractorOption.class);
    	for(ExtractorOption opt : extractorOptions) {
    		options.addOption(Option.builder(opt.getName())
        			.type(opt.getType())
        			.desc(opt.getDescription())
        			.hasArg(opt.hasArg())
        			.required(false)
        			.build());
    	}
    }

    public void run() throws PMExtractorException {
    	CommandLine cmd = getCommandLineInput(userArgs);
    	if(cmd.hasOption(ExtractorOption.HELP.toString())) {
    		HelpFormatter formatter = new HelpFormatter();
    		formatter.printHelp("pmextractor", options);
    		System.exit(0);
    	}
        DirectoryHelper dHelper = new DirectoryHelper();
        dHelper.ensureAppXpress();
        PMBProperties pmbProperties = dHelper.getPmbProperties();
        ArgsAndPropertiesConsolidator consolidator = new ArgsAndPropertiesConsolidator(
        		getOptionValues(cmd), pmbProperties.getProperties());
        Map<ExtractorOption, String> optMap = consolidator.consolidate();
        GitMap tool = GitMap.createMapper(optMap);
        tool.doMapping();
        consolidator.presentSaveOption(pmbProperties.getPropertiesPath());
    }
    
    private String[] getOptionValues(CommandLine cmd)  {
		int i = 0;
		String[] argVals = new String[extractorOptions.size()];
		for(ExtractorOption opt : extractorOptions) {
			if(cmd.hasOption(opt.getName())) {
				argVals[i] = cmd.getOptionValue(opt.getName());
			}
			i++;
		}
		return argVals;
    }
    
    
    private CommandLine getCommandLineInput(String[] args)throws PMExtractorException{
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			throw new PMExtractorException("Exception when parsing args from command line!");
		}
		return cmd;
    }
    
}

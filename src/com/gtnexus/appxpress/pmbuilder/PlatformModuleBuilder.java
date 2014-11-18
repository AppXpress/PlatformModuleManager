package com.gtnexus.appxpress.pmbuilder;

import java.io.File;
import java.util.EnumSet;
import java.util.Map;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.DirectoryHelper;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.Preparation;
import com.gtnexus.appxpress.cli.CommandLineInterfaceParser;
import com.gtnexus.appxpress.pmbuilder.bundle.Bundler;
import com.gtnexus.appxpress.pmbuilder.bundle.platform.BuildPrep;
import com.gtnexus.appxpress.pmbuilder.bundle.platform.PlatformModuleBundler;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmbuilder.cli.PMBuilderVO;
import com.gtnexus.appxpress.pmextractor.cli.ArgsAndPropertiesConsolidator;

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

	private static final String NAME = "pmbuilder";

	/**
	 * Main method of pm_builder_util.jar. Instantiates PlatfromModuleBuilder.
	 * 
	 * @param args
	 *            0 - Name of customer folder 1 - Name of platform module folder
	 */
	public static void main(String args[]) {
		/*
		 * if (args.length != 2) { System.err
		 * .println("Incorrect parameters. Program requires two parameters");
		 * return; } PlatformModuleBuilder pmb = new
		 * PlatformModuleBuilder(args[0], args[1]);
		 */
		PlatformModuleBuilder pmb = new PlatformModuleBuilder(args);
		pmb.build();
	}

	private final String[] args;
	/*
	 * private final String customer, module; private final File rootFile;
	 */
	private final Preparation<File> prep;
	private final Bundler bundler;

	/**
	 * Inputs recently pulled down git repository and outputs zip file that is
	 * ready to be imported onto GTNexus system
	 * 
	 * @param customer
	 *            Name of customer folder
	 * @param module
	 *            Name of platform module folder
	 */
	public PlatformModuleBuilder(String[] args) {
		this.args = args;
		this.prep = new BuildPrep();
		this.bundler = new PlatformModuleBundler();
	}

	public void build() {
		CommandLineInterfaceParser<BuilderOption> cli = new CommandLineInterfaceParser<>(
				NAME, args, EnumSet.allOf(BuilderOption.class));
		if (cli.hasOption(BuilderOption.HELP)) {
			cli.displayHelpAndExit();
		}
		try {
			performBuild(cli);
		} catch (AppXpressException e) {
			System.out.println("Failure when building module");
			// TODO
			/*
			 * System.out.println("Failure when building module for [customer: "
			 * + customer + " module: " + module + "]");
			 */
			System.out.println(e.getAppXpressMessage());
		}
	}

	/**
	 * Builds the platform module.
	 */
	private void performBuild(CommandLineInterfaceParser<BuilderOption> cli)
			throws AppXpressException {
		DirectoryHelper dHelper = new DirectoryHelper();
		dHelper.ensureAppXpress();
		PMProperties pmProperties = dHelper.getPmProperties();
		ArgsAndPropertiesConsolidator<BuilderOption> consolidator = new ArgsAndPropertiesConsolidator<>(
				cli.getOptionsMap(), cli.getCliOptionSet(),
				pmProperties.getProperties());
		Map<BuilderOption, String> optMap = consolidator.consolidate();
		PMBuilderVO vo = new PMBuilderVO(optMap);
		prep.prepare(vo.getRootFile());
		bundler.bundle(vo.getRootFile());
		System.out.println("Success!");

	}

}

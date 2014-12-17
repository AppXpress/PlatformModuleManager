package com.gtnexus.appxpress.pmbuilder;

import java.util.Map;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.pmbuilder.bundle.platform.BuildPrep;
import com.gtnexus.appxpress.pmbuilder.bundle.platform.PlatformModuleBundler;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmbuilder.cli.PMBuilderVO;


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
	 *            0 - Name of customer folder 
	 *            1 - Name of platform module folder
	 */
	public static void main(String args[]) {
		PlatformModuleBuilder pmb = new PlatformModuleBuilder(args);
		pmb.build();
	}

	private final String[] args;
	private AppXpressContext<BuilderOption> context;

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
		
	}
	
	private void init() throws AppXpressException {
		context = ContextFactory.creatContext(NAME, BuilderOption.class, args);
	}
	
	/**
	 * Builds the platform module.
	 */
	private void performBuild(Map<BuilderOption, String> optMap)
			throws AppXpressException {
		init();
		PMBuilderVO vo = new PMBuilderVO(optMap);
		BuildPrep prep = new BuildPrep();
		PlatformModuleBundler bundler = new PlatformModuleBundler(vo.getRootFile());
		prep.prepare(vo);
		bundler.bundle(vo.getWorkingDir());
		System.out.println("Success!");
		//consolidator.presentSaveOption(pmProperties.getPropertiesPath());
		//move me up to build() or even further up ... 
	}
	
	public void build() {
		//init();
		PMBuilderVO vo = new PMBuilderVO(context.getOptMap());
		BuildPrep prep = new BuildPrep();
		PlatformModuleBundler bundler = new PlatformModuleBundler(vo.getRootFile());
		try {
			prep.prepare(vo);
			bundler.bundle(vo.getWorkingDir());
			System.out.println("Success!");
		} 
		catch (AppXpressException e) {
			e.printStackTrace();
			context.shutdown();
		}
	}

}

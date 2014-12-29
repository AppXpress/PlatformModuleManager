package com.gtnexus.appxpress.pmextractor;

import java.io.IOException;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.commons.ApplicationInfo;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;
import com.gtnexus.appxpress.pmextractor.gitmap.GitMapper;
import com.gtnexus.appxpress.pmextractor.gitmap.Mapper;

/**
 * 
 * @author jdonovan
 *
 */
public class PlatformModuleExtractor implements ApplicationInfo {

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
		ContextFactory factory = new ContextFactory();
		try {
			PlatformModuleExtractor extractor = new PlatformModuleExtractor();
			AppXpressContext<ExtractorOption> context = factory.creatContext(extractor, args);
			extractor.extract(context);
		} catch (AppXpressException e) {
			System.err.println("Failure when running pmextractor.");
			System.err.println(e.getMessage());
		}
	}

	private static final String NAME = "pmextractor";

	public PlatformModuleExtractor() {
		
	}
	
	public void extract(AppXpressContext<ExtractorOption> context) throws AppXpressException {
		attachCleanUpHook(context);
		Mapper tool = GitMapper.createMapper(context);
		tool.doMapping();
		System.out.println("Success!");
	}
	
	private void attachCleanUpHook(AppXpressContext<ExtractorOption> ctx) {
		Runtime.getRuntime().addShutdownHook(
				new Thread(new ExtractorCleanup(ctx)));
	}
	
	@Override
	public String getAppName() {
		return NAME;
	}

	@Override
	public String getHelpHeader() {
		return "";
	}

	@Override
	public String getHelpFooter() {
		return "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public <M extends Enum<M> & AppXpressOption> Class<M> getContextType() {
		return (Class<M>) ExtractorOption.class;
	}

}

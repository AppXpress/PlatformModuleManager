package com.gtnexus.appxpress;

import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.pmbuilder.PlatformModuleBuilder;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmextractor.PlatformModuleExtractor;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

public class PlatformModuleManager {
	
	public static void main(String ... args) {
		PlatformModuleManager pmm = new PlatformModuleManager();
		if("build".equals(args[0])) {
			pmm.runBuilder();
		} else if ("extract".equals(args[0])) {
			pmm.runExtractor();
		} else {
			System.out.println("Not a recognized command");
			return;
		}
	}
	
	private void runBuilder(String ... args) {
		ContextFactory factory = new ContextFactory();
		try {
			PlatformModuleBuilder pmb = new PlatformModuleBuilder();
			AppXpressContext<BuilderOption> context = factory.creatContext(pmb,
					args);
			pmb.build(context);
		} catch (AppXpressException e) {
			System.out.println(e.getAppXpressMessage());
		}
	}
	
	private void runExtractor(String... args ) {
		ContextFactory factory = new ContextFactory();
		try {
			PlatformModuleExtractor extractor = new PlatformModuleExtractor();
			AppXpressContext<ExtractorOption> context = factory.creatContext(extractor, args);
			extractor.extract(context);
		} catch (AppXpressException e) {
			System.err.println(e.getAppXpressMessage());
		}
	}
}

package com.gtnexus.appxpress;

import java.util.Arrays;

import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.pmbuilder.PlatformModuleBuilder;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmextractor.PlatformModuleExtractor;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

public class PlatformModuleManager {
	
	public static void main(String ... args) {
		if(args.length == 0) {
			System.out.println("Please select build or extract.");
			return;
		}
		String commandName = args[0];
		PlatformModuleManager pmm = new PlatformModuleManager();
		String[] childArgs = pmm.restOf(args);
		if("build".equals(commandName)) {
			pmm.runBuilder(childArgs);
		} else if ("extract".equals(commandName)) {
			pmm.runExtractor(childArgs);
		} else {
			System.out.println("Not a recognized command");
			return;
		}
	}
	
	private String[] restOf(String ...args) {
		if(args.length < 2) {
			return new String[0];
		}
		return Arrays.copyOfRange(args, 1, args.length);
	}
	
	private void runBuilder(String ... args) {
		ContextFactory factory = new ContextFactory();
		try {
			PlatformModuleBuilder pmb = new PlatformModuleBuilder();
			AppXpressContext<BuilderOption> context = factory.createContext(pmb,
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
			AppXpressContext<ExtractorOption> context = factory.createContext(extractor, args);
			extractor.extract(context);
		} catch (AppXpressException e) {
			System.err.println(e.getAppXpressMessage());
		}
	}
}

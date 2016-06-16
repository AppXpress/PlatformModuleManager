package com.gtnexus.appxpress;

import com.gtnexus.appxpress.commons.AppXCommand;
import com.gtnexus.appxpress.commons.exception.AppXpressException;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.pmbuilder.PlatformModuleBuilder;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmextractor.PlatformModuleExtractor;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

/**
 * 
 * @author jjdonov
 *
 */
public class AppXpressManager {

	public static void main(String... args) {
		AppXpressManager mgr = new AppXpressManager(args);
		mgr.run();
	}
	
	private final String[] args;
	
	public AppXpressManager(String... args) {
		this.args = args;
	}
	
	private void run() {
		/**
		 * 0. lets run all our appropriate start up checks here first. 1.
		 * Determine appropriate context type 2. pass work off to tool 3. handle
		 * clean up (i.e. builder, and (selector should not need to) 4.
		 * implement axm test
		 */
		if (args.length == 0) {
			displayUsage();
			return;
		}
		String target = args[0];
		AppXCommand cmd = getCommand(target);
		if(cmd == null) {
			displayUsage();
			return;
		}
		cmd.run();
	}
	
	AppXCommand getCommand(String commandString) {
		AppXCommand cmd = null;
		ContextFactory factory = new ContextFactory();
		if(commandString.equals("build")) {
			try {
				PlatformModuleBuilder pmb = new PlatformModuleBuilder();
				AppXpressContext<BuilderOption> context = factory.createContext(pmb, args);
				pmb.build(context);
				System.out.println("Success!");
			} catch (AppXpressException e) {
				System.out.println(e.getAppXpressMessage());
			}
		} else if (commandString.equals("extract")) {
			try {
				PlatformModuleExtractor extractor = new PlatformModuleExtractor();
				AppXpressContext<ExtractorOption> context = factory.createContext(extractor, args);
				extractor.extract(context);
			} catch (AppXpressException e) {
				System.err.println(e.getAppXpressMessage());
			}
		}
		return cmd;
	}

	static void displayUsage() {
		System.out.println("Heh");
	}
}

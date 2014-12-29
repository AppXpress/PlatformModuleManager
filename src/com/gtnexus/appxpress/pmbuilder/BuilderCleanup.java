package com.gtnexus.appxpress.pmbuilder;

import com.gtnexus.appxpress.commons.file.FileCleanup;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;

/**
 * 
 * @author jdonovan
 *
 */
public class BuilderCleanup implements Runnable {

	private final AppXpressContext<BuilderOption> ctx;
	private final FileCleanup cleanup;
	
	public BuilderCleanup(AppXpressContext<BuilderOption> context) {
		this.ctx = context;
		this.cleanup = new FileCleanup();
	}
	
	@Override
	public void run() {
		cleanup.cleanup(ctx.getFilesToDeleteOnExit());
	}
	
}

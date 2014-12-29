package com.gtnexus.appxpress.pmextractor;

import com.gtnexus.appxpress.commons.file.FileCleanup;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

/**
 * 
 * @author jdonovan
 *
 */
public class ExtractorCleanup implements Runnable {

	private final AppXpressContext<ExtractorOption> ctx;
	private final FileCleanup cleanup;

	public ExtractorCleanup(AppXpressContext<ExtractorOption> context) {
		this.ctx = context;
		this.cleanup = new FileCleanup();
	}

	@Override
	public void run() {
		cleanup.cleanup(ctx.getFilesToDeleteOnExit());
	}

}

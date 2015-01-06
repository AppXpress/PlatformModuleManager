package com.gtnexus.appxpress.context;

import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.commons.file.FileCleanup;

/**
 * Used {@link FileCleanup} to delete all temporary files marked for deletion by a {@link TempResourceHolder}
 * @author jdonovan
 * @param <T>
 *
 */
public class ContextBasedCleanUp<T extends Enum<T> & AppXpressOption> implements Runnable {

	private final AppXpressContext<T> ctx;
	private final FileCleanup cleanup;
	
	public ContextBasedCleanUp(AppXpressContext<T> context) {
		this.ctx = context;
		this.cleanup = new FileCleanup();
	}
	
	@Override
	public void run() {
		cleanup.cleanup(ctx.getFilesToDeleteOnExit());
	}
	
}

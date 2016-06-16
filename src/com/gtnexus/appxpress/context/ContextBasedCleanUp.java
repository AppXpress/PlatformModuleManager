package com.gtnexus.appxpress.context;

import com.gtnexus.appxpress.commons.exception.AppXpressException;
import com.gtnexus.appxpress.commons.file.FileCleanup;

/**
 * Used {@link FileCleanup} to delete all temporary files marked for deletion by a {@link TempResourceHolder}
 * @author jdonovan
 * @param <T>
 *
 */
public class ContextBasedCleanUp implements Runnable {

	private final AppXpressContext ctx;
	private final FileCleanup cleanup;
	
	public ContextBasedCleanUp(AppXpressContext context) {
		this.ctx = context;
		this.cleanup = new FileCleanup();
	}
	
	@Override
	public void run() {
		checkToSave();
		cleanup.cleanup(ctx.getFilesToDeleteOnExit());
	}
	
	public void checkToSave() {
		if (ctx.isTerminatedRegulary() && ctx.propertiesWereChanged()) {
			try {
				ctx.presentSaveOption();
			} catch (AppXpressException e) {
				System.out
						.println("Could not save properties file. Continuing with cleanup.");
			}
		}
	}
	
}

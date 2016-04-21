package com.gtnexus.appxpress.context;

import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.commons.file.FileCleanup;

/**
 * Used {@link FileCleanup} to delete all temporary files marked for deletion by a {@link TempResourceHolder}
 * @author jdonovan
 * @param <T>
 *
 */
public class ContextBasedCleanUp<T extends Enum<T> & CLICommandOption> implements Runnable {

	private final AppXpressContext<T> ctx;
	private final FileCleanup cleanup;
	
	public ContextBasedCleanUp(AppXpressContext<T> context) {
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

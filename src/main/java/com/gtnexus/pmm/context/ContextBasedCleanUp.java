package com.gtnexus.pmm.context;

import com.gtnexus.pmm.commons.file.FileCleanup;

/**
 * Used {@link FileCleanup} to delete all temporary files marked for deletion by
 * a {@link TempResourceHolder}
 * 
 * @author jdonovan
 * @param <T>
 *
 */
public class ContextBasedCleanUp implements Runnable {

//    private final PmmContext ctx;
//    private final FileCleanup cleanup;

    public ContextBasedCleanUp(PmmContext context) {
//	this.ctx = context;
//	this.cleanup = new FileCleanup();
    }

    //TODO: revisit and reimplement elsewhere
    @Override
    public void run() {
	checkToSave();
//	cleanup.cleanup(ctx.getTempResourceHolder().getFilesToDeleteOnExit());
    }

    public void checkToSave() {
//	if (ctx.isTerminatedRegulary() && ctx.propertiesWereChanged()) {
//	    try {
//		ctx.presentSaveOption();
//	    } catch (AppXpressException e) {
//		System.out.println("Could not save properties file. Continuing with cleanup.");
//	    }
//	}
    }

}

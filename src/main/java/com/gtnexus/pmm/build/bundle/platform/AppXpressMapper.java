package com.gtnexus.pmm.build.bundle.platform;

import java.io.File;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.build.bundle.Bundler;
import com.gtnexus.pmm.build.bundle.scripts.FolderPrep;
import com.gtnexus.pmm.build.bundle.scripts.ScriptBundler;

/**
 * Replacement for PlatfromMapUtil.
 * 
 * Takes a git repository and maps to a file structure that can be imported onto
 * the platform.
 * 
 * @author jdonovan
 *
 */
public class AppXpressMapper {

    private final File root;
    private final Bundler bundler;
    private final FolderPrep prep;

    public AppXpressMapper(File root) {
	this.root = root;
	this.bundler = new ScriptBundler();
	this.prep = new FolderPrep();
    }

    /**
     * Performs the directory mappings (renaming, moving, etc.)
     */
    public void doMapping() throws AppXpressException {
	if (root == null || !root.isDirectory()) {
	    System.err.println("Module root cannot be found, or is empty. " + "No mapping can be performed.");
	    return;
	}
	prep.prepare(root);
	bundler.bundle(root);
    }

}

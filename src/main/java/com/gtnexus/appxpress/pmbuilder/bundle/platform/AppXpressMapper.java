package com.gtnexus.appxpress.pmbuilder.bundle.platform;

import java.io.File;

import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.commons.Preparation;
import com.gtnexus.appxpress.pmbuilder.bundle.Bundler;
import com.gtnexus.appxpress.pmbuilder.bundle.scripts.FolderPrep;
import com.gtnexus.appxpress.pmbuilder.bundle.scripts.ScriptBundler;
import com.gtnexus.appxpress.pmextractor.gitmap.Mapper;

/**
 * Replacement for PlatfromMapUtil.
 * 
 * Takes a git repository and maps to a file structure that can be imported onto
 * the platform.
 * 
 * @author jdonovan
 *
 */
public class AppXpressMapper implements Mapper {

	private final File root;
	private final Bundler bundler;
	private final Preparation<File> prep;

	public AppXpressMapper(File root) {
		this.root = root;
		this.bundler = new ScriptBundler();
		this.prep = new FolderPrep();
	}

	/**
	 * Performs the directory mappings (renaming, moving, etc.)
	 */
	@Override
	public void doMapping() throws AppXpressException {
		if (root == null || !root.isDirectory()) {
			System.err.println("Module root cannot be found, or is empty. "
					+ "No mapping can be performed.");
			return;
		}
		prep.prepare(root);
		bundler.bundle(root);
	}
	
}

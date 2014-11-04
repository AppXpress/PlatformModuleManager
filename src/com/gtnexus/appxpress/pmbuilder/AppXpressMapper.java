package com.gtnexus.appxpress.pmbuilder;

import java.io.File;

import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.pmbuilder.bundle.Bundler;
import com.gtnexus.appxpress.pmbuilder.bundle.Preparation;
import com.gtnexus.appxpress.pmbuilder.bundle.scripts.FolderPrep;
import com.gtnexus.appxpress.pmbuilder.bundle.scripts.ScriptBundler;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

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
	private final Preparation prep;

	public AppXpressMapper(File root) {
		this.root = root;
		this.bundler = new ScriptBundler();
		this.prep = new FolderPrep();
	}

	/**
	 * Performs the directory mappings (renaming, moving, etc.)
	 */
	public void doMapping() {
		if (root == null || !root.isDirectory()) {
			System.err.println("Module root cannot be found, or is empty. "
					+ "No mapping can be performed.");
			return;
		}
		try {
			prep.prepare(root);
			bundler.bundle(root);
		} catch (PMBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

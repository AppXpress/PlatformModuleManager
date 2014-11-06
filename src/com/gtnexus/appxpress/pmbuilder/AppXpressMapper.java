package com.gtnexus.appxpress.pmbuilder;

import java.io.File;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.Mapper;
import com.gtnexus.appxpress.Preparation;
import com.gtnexus.appxpress.pmbuilder.bundle.Bundler;
import com.gtnexus.appxpress.pmbuilder.bundle.scripts.FolderPrep;
import com.gtnexus.appxpress.pmbuilder.bundle.scripts.ScriptBundler;

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
	public void doMapping() {
		if (root == null || !root.isDirectory()) {
			System.err.println("Module root cannot be found, or is empty. "
					+ "No mapping can be performed.");
			return;
		}
		try {
			prep.prepare(root);
			bundler.bundle(root);
		} catch (AppXpressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

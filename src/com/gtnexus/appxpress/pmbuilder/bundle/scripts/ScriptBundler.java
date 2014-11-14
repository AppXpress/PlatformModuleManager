package com.gtnexus.appxpress.pmbuilder.bundle.scripts;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.BUNDLE;
import static com.gtnexus.appxpress.AppXpressConstants.JS_EXTENSION;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.file.FileService;
import com.gtnexus.appxpress.file.filter.ChainedAnd;
import com.gtnexus.appxpress.file.filter.FileFilterFactory;
import com.gtnexus.appxpress.pmbuilder.bundle.Bundler;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * 
 * @author jdonovan
 *
 */
public class ScriptBundler implements Bundler {

	private final ZipService zu;
	private final FileService fs;

	public ScriptBundler() {
		this.zu = new ZipService();
		this.fs = new FileService();
	}

	@Override
	public void bundle(final File directory) {
		for (File f : directory.listFiles(FileFilterFactory.directoriesOnly())) {
			searchForPotentialBundles(f);
		}
	}

	private void searchForPotentialBundles(final File dir) {
		if (wasSpecialCase(dir)) {
			return;
		}
		bundleGenerically(dir);
	}

	private boolean wasSpecialCase(final File dir) {
		final String platformIndependent = dir.getAbsolutePath()
				+ File.separator + "designs" + File.separator + "scripts";
		boolean isSpecial = false;
		if (dir.getAbsolutePath().toLowerCase().contains(platformIndependent)) {
			handleCustomObjectDesignScripts(dir);
			isSpecial = true;
		} else if (dir.getName().endsWith("customUi")) {
			handleFef(dir);
			isSpecial = true;
		}
		return isSpecial;
	}

	private void bundleGenerically(File dir) {
		final List<File> jsFiles = new LinkedList<>();
		for (File f : dir.listFiles()) {
			if (fs.isFileType(f, "js")) {
				jsFiles.add(f);
			} else if (f.isDirectory()) {
				searchForPotentialBundles(f);
			}
		}
		if (jsFiles.size() > 1) {
			try {
				zu.zipFiles(jsFiles, dir.getAbsolutePath() + BUNDLE + ZIP_EXTENSION);
				fs.emptyDir(dir, true);
			} catch (PMBuilderException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Handle's special logic for handling the directory for the Front End
	 * Framework
	 * 
	 * @param dir
	 */
	private void handleFef(final File dir) {
		final ChainedAnd filter = new ChainedAnd(
				FileFilterFactory.directoriesOnly(),
				FileFilterFactory.doesNotEndWith(ZIP_EXTENSION));
		for (File file : dir.listFiles(filter)) {
			try {
				zu.zipDirectory(file);
				fs.emptyDir(file, true);
			} catch (PMBuilderException | IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * @param dir
	 *            The CustomObject/designs/scripts directory.
	 */
	private void handleCustomObjectDesignScripts(final File dir) {
		for (File subDir : dir.listFiles(FileFilterFactory.directoriesOnly())) {
			handleSingleCODScript(subDir);
		}
	}

	private void handleSingleCODScript(final File dir) {
		try {
			if (dir.list().length == 1) {
				moveUpAndRename(dir);
			} else if (dir.list().length > 1) {
				bundleCODScript(dir);
			}
			fs.emptyDir(dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void bundleCODScript(final File dir) throws IOException {
		String rename = SCRIPT_DESIGN + $ + dir.getName();
		Path newPath = dir.toPath().resolveSibling(rename);
		Files.move(dir.toPath(), newPath);
	}

	private void moveUpAndRename(final File dir) throws IOException {
		String newName = SCRIPT_DESIGN + $ + dir.getName() + JS_EXTENSION;
		Path newPath = dir.toPath().resolveSibling(newName);
		File loneFile = dir.listFiles()[0];
		Files.move(loneFile.toPath(), newPath);
	}

}

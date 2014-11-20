package com.gtnexus.appxpress.pmbuilder.bundle.scripts;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.BUNDLE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_UI;
import static com.gtnexus.appxpress.AppXpressConstants.DESIGNS;
import static com.gtnexus.appxpress.AppXpressConstants.JS_EXTENSION;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTS;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.ZipService;
import com.gtnexus.appxpress.file.FileService;
import com.gtnexus.appxpress.file.filter.ChainedAnd;
import com.gtnexus.appxpress.file.filter.FileFilterFactory;
import com.gtnexus.appxpress.pmbuilder.bundle.Bundler;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

/**
 * 
 * @author jdonovan
 *
 */
public class ScriptBundler implements Bundler {

	private final ZipService zs;
	private final FileService fs;

	public ScriptBundler() {
		this.zs = new ZipService();
		this.fs = new FileService();
	}

	@Override
	public void bundle(final File directory) throws AppXpressException {
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
		final String platformIndependent = DESIGNS + File.separator + SCRIPTS;
		boolean isSpecial = false;
		try {
			if (dir.getAbsolutePath().toLowerCase()
					.contains(platformIndependent.toLowerCase())) {
				handleCustomObjectDesignScripts(dir);
				isSpecial = true;
			} else if (dir.getName().endsWith(CUSTOM_UI)) {
				handleFef(dir);
				isSpecial = true;
			}
		} catch (PMExtractorException e) {
			System.err.println("Unable to handle special case. Caused by: ");
			System.err.println(e.getMessage());
			System.out.println("Defaulting to standard bundling.");
			isSpecial = false;
		}
		return isSpecial;
	}

	private void bundleGenerically(final File dir) {
		final List<File> jsFiles = new LinkedList<>();
		for (File f : dir.listFiles()) {
			if (fs.isFileType(f, JS_EXTENSION)) {
				jsFiles.add(f);
			} else if (f.isDirectory()) {
				searchForPotentialBundles(f);
			}
		}
		if (jsFiles.size() > 1) {
			try {
				zs.zipFiles(jsFiles, dir.getAbsolutePath() + BUNDLE
						+ ZIP_EXTENSION);
				fs.emptyDir(dir, true);
			} catch (AppXpressException | IOException e) {
				//TODO
				System.err.println("Failed to bundle directory " + dir.toString() + " generically.");
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Handle's special logic for handling the directory for the Front End
	 * Framework
	 * 
	 * @param dir
	 * @throws PMExtractorException
	 */
	private void handleFef(final File dir) throws PMExtractorException {
		final ChainedAnd filter = new ChainedAnd(
				FileFilterFactory.directoriesOnly(),
				FileFilterFactory.doesNotEndWith(ZIP_EXTENSION));
		for (File file : dir.listFiles(filter)) {
			try {
				zs.zipDirectory(file);
				fs.emptyDir(file, true);
			} catch (AppXpressException | IOException e) {
				throw new PMExtractorException("Could not handle FEF", e);
			}
		}
	}

	/**
	 * @param dir
	 *            The CustomObject/designs/scripts directory.
	 * @throws PMExtractorException
	 */
	private void handleCustomObjectDesignScripts(final File dir)
			throws PMExtractorException {
		for (File subDir : dir.listFiles(FileFilterFactory.directoriesOnly())) {
			handleSingleCODScript(subDir);
		}
	}

	private void handleSingleCODScript(final File dir)
			throws PMExtractorException {
		Path p = dir.toPath();
		try {
			if (dir.list().length == 1) {
				moveUpAndRename(dir);
			} else if (dir.list().length > 1) {
				p = bundleCODScript(dir);
				zs.zipDirectory(p.toFile());
			}
			fs.emptyDir(p, true);
		} catch (AppXpressException | IOException e) {
			throw new PMExtractorException(
					"Couldn't handle single custom object design script "
							+ dir.toString(), e);
		}
	}

	private Path bundleCODScript(final File dir) throws IOException {
		String rename = SCRIPT_DESIGN + $ + dir.getName();
		Path newPath = dir.toPath().resolveSibling(rename);
		return Files.move(dir.toPath(), newPath);
	}

	private Path moveUpAndRename(final File dir) throws IOException {
		String newName = dir.getName();
		newName = scriptNameForDir(dir);
		Path newPath = dir.toPath().resolveSibling(newName);
		File loneFile = dir.listFiles()[0];
		return Files.move(loneFile.toPath(), newPath);
	}
	
	private String scriptNameForDir(File dir) {
		String name = dir.getName();
		if(name.startsWith(SCRIPT_DESIGN) && name.startsWith(SCRIPT_DESIGN + $)) {
			return name + JS_EXTENSION;
		}
		if(name.startsWith(SCRIPT_DESIGN) && !name.startsWith(SCRIPT_DESIGN + $)) {
			return name.replace(SCRIPT_DESIGN, SCRIPT_DESIGN + $) + JS_EXTENSION;
		} else {
			return SCRIPT_DESIGN + $ + name + JS_EXTENSION;
		}
	}

}

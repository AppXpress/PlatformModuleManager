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
		if (dir.getAbsolutePath().toLowerCase().contains(platformIndependent.toLowerCase())) {
			handleCustomObjectDesignScripts(dir);
			isSpecial = true;
		} else if (dir.getName().endsWith(CUSTOM_UI)) {
			handleFef(dir);
			isSpecial = true;
		}
		return isSpecial;
	}

	private void bundleGenerically(File dir) {
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
				zs.zipFiles(jsFiles, dir.getAbsolutePath() + BUNDLE + ZIP_EXTENSION);
				fs.emptyDir(dir, true);
			} catch (AppXpressException | IOException e) {
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
				zs.zipDirectory(file);
				fs.emptyDir(file, true);
			} catch (AppXpressException | IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * @param dir
	 *            The CustomObject/designs/scripts directory.
	 */
	private void handleCustomObjectDesignScripts(final File dir) {
		System.out.println("> Handling cod");
		for (File subDir : dir.listFiles(FileFilterFactory.directoriesOnly())) {
			handleSingleCODScript(subDir);
		}
	}

	private void handleSingleCODScript(final File dir) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Path bundleCODScript(final File dir) throws IOException {
		String rename = SCRIPT_DESIGN + $ + dir.getName();
		Path newPath = dir.toPath().resolveSibling(rename);
		return Files.move(dir.toPath(), newPath);
	}

	private Path moveUpAndRename(final File dir) throws IOException {
		String newName = SCRIPT_DESIGN + $ + dir.getName() + JS_EXTENSION;
		Path newPath = dir.toPath().resolveSibling(newName);
		File loneFile = dir.listFiles()[0];
		return Files.move(loneFile.toPath(), newPath);
	}

}

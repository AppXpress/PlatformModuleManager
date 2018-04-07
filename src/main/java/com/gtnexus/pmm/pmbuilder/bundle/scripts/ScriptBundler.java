package com.gtnexus.pmm.pmbuilder.bundle.scripts;

import static com.gtnexus.pmm.AppXpressConstants.$;
import static com.gtnexus.pmm.AppXpressConstants.BUNDLE;
import static com.gtnexus.pmm.AppXpressConstants.CUSTOM_UI;
import static com.gtnexus.pmm.AppXpressConstants.DESIGNS;
import static com.gtnexus.pmm.AppXpressConstants.JS_EXTENSION;
import static com.gtnexus.pmm.AppXpressConstants.SCRIPTS;
import static com.gtnexus.pmm.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.pmm.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.service.FileService;
import com.gtnexus.pmm.api.v100.service.ZipService;
import com.gtnexus.pmm.commons.file.filter.ChainedAnd;
import com.gtnexus.pmm.commons.file.filter.FileFilterFactory;
import com.gtnexus.pmm.pmbuilder.bundle.Bundler;
import com.gtnexus.pmm.pmbuilder.exception.PMBuilderException;
import com.gtnexus.pmm.pmextractor.exception.PMExtractorException;
import com.gtnexus.pmm.service.FileServiceImpl;
import com.gtnexus.pmm.service.ZipServiceImpl;

/**
 * 
 * @author jdonovan
 *
 */
public class ScriptBundler implements Bundler {

    private final ZipService zs;
    private final FileService fs;

    public ScriptBundler() {
	this.zs = new ZipServiceImpl();
	this.fs = new FileServiceImpl();
    }

    @Override
    public void bundle(final File directory) throws AppXpressException {
	for (File f : directory.listFiles(FileFilterFactory.directoriesOnly())) {
	    searchForPotentialBundles(f);
	}
    }

    private void searchForPotentialBundles(final File dir) throws PMBuilderException {
	if (wasSpecialCase(dir)) {
	    return;
	}
	bundleGenerically(dir);
    }

    private boolean wasSpecialCase(final File dir) {
	final String platformIndependent = DESIGNS + File.separator + SCRIPTS;
	boolean isSpecial = false;
	try {
	    if (dir.getAbsolutePath().toLowerCase().contains(platformIndependent.toLowerCase())) {
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

    private void bundleGenerically(final File dir) throws PMBuilderException {
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
		throw new PMBuilderException("Failed to bundle directory generically", e);
	    }
	}
    }

    /**
     * Handle's special logic for handling the directory for the Front End Framework
     * 
     * @param dir
     * @throws PMExtractorException
     */
    private void handleFef(final File dir) throws PMExtractorException {
	final ChainedAnd filter = new ChainedAnd(FileFilterFactory.directoriesOnly(),
		FileFilterFactory.doesNotEndWith(ZIP_EXTENSION));
	if (filter.hasResults(dir)) {
	    Path customUiBundle = dir.toPath().resolve("CustomUIBundle");
	    try {
		addPathsToUIBundle(filter.listPaths(dir), customUiBundle);
		zs.zipDirectory(customUiBundle);
		fs.emptyDir(customUiBundle, true);
	    } catch (AppXpressException | IOException e) {
		throw new PMExtractorException("Could not handle FEF", e);
	    }
	}
    }

    private void addPathsToUIBundle(Collection<Path> paths, Path customUiBundle) throws IOException {
	for (Path subdir : paths) {
	    fs.copyDirectory(subdir, customUiBundle.resolve(subdir.getFileName()));
	}
    }

    /**
     * @param dir
     *            The CustomObject/designs/scripts directory.
     * @throws PMExtractorException
     */
    private void handleCustomObjectDesignScripts(final File dir) throws PMExtractorException {
	for (File subDir : dir.listFiles(FileFilterFactory.directoriesOnly())) {
	    handleSingleCODScript(subDir);
	}
    }

    private void handleSingleCODScript(final File dir) throws PMExtractorException {
	Path p = dir.toPath();
	try {
	    if (dir.list().length == 1) {
		moveUpAndRename(dir);
	    } else if (dir.list().length > 1) {
		p = bundleCODScript(dir);
		zs.zipDirectory(p);
	    }
	    fs.emptyDir(p, true);
	} catch (AppXpressException | IOException e) {
	    throw new PMExtractorException("Couldn't handle single custom object design script " + dir.toString(), e);
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
	if (name.startsWith(SCRIPT_DESIGN) && name.startsWith(SCRIPT_DESIGN + $)) {
	    return name + JS_EXTENSION;
	}
	if (name.startsWith(SCRIPT_DESIGN) && !name.startsWith(SCRIPT_DESIGN + $)) {
	    return name.replace(SCRIPT_DESIGN, SCRIPT_DESIGN + $) + JS_EXTENSION;
	} else {
	    return SCRIPT_DESIGN + $ + name + JS_EXTENSION;
	}
    }

}

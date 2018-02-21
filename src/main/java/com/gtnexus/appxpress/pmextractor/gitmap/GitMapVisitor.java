package com.gtnexus.appxpress.pmextractor.gitmap;

import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_UI;
import static com.gtnexus.appxpress.AppXpressConstants.IGNORE_SET;
import static com.gtnexus.appxpress.AppXpressConstants.JS_EXTENSION;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

import com.gtnexus.appxpress.commons.file.CopyDirVisitor;

public class GitMapVisitor extends CopyDirVisitor {

    private GitMapVO vo;
    private List<Path> overwrittenScripts;

    public GitMapVisitor(final GitMapVO vo, final Path source, final Path destination) {
	super(source, destination);
	this.vo = vo;
	overwrittenScripts = new LinkedList<Path>();
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
	if (isIgnorePath(dir)) {
	    return FileVisitResult.SKIP_SUBTREE;
	}
	return super.preVisitDirectory(dir, attrs);
    }

    protected boolean isIgnorePath(Path dir) {
	if (dir.endsWith(CUSTOM_UI) && !vo.isOverwriteFef()) {
	    return true;
	}
	String dirName = dir.getName(dir.getNameCount() - 1).toString();
	return IGNORE_SET.contains(dirName);
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
	if (!isPreExistingJSFile(file)) {
	    return super.visitFile(file, attrs);
	} else if (vo.isOverwriteScripts()) {
	    overwrittenScripts.add(file);
	    return super.visitFile(file, attrs);
	} else {
	    return FileVisitResult.CONTINUE;
	}
    }

    public List<Path> getOverwrittenScripts() {
	return overwrittenScripts;
    }

    private boolean isPreExistingJSFile(Path file) {
	if (!file.endsWith(JS_EXTENSION)) {
	    return false;
	}
	Path p = destination.resolve(file);
	return (Files.exists(p));
    }

}

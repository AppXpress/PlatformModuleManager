package com.gtnexus.appxpress.commons.file;

import static com.gtnexus.appxpress.AppXpressConstants.IGNORE_SET;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * When used with Files.walkFileTree() this class will copy entire File tree
 * from source to destination.
 * 
 * @author jdonovan
 *
 */
public class CopyDirVisitor extends SimpleFileVisitor<Path> {

    protected final Path source;
    protected final Path destination;
    private final StandardCopyOption replace = StandardCopyOption.REPLACE_EXISTING;

    public CopyDirVisitor(final Path source, final Path destination) {
	super();
	this.source = source;
	this.destination = destination;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
	Path target = destination.resolve(source.relativize(dir));
	if (!Files.exists(target)) {
	    Files.createDirectory(target);
	}
	return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
	Path target = destination.resolve(source.relativize(file));
	if (!IGNORE_SET.contains(file.getFileName().toString())) {
	    Files.copy(file, target, replace);
	}
	return FileVisitResult.CONTINUE;
    }

}

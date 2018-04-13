package com.gtnexus.pmm.common.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteDirVisitor extends SimpleFileVisitor<Path> {

    protected final Path root;
    private final boolean deleteRoot;

    public DeleteDirVisitor(final Path root, final boolean deleteRoot) {
	super();
	this.root = root;
	this.deleteRoot = deleteRoot;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
	Files.delete(file);
	return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
	if (!dir.equals(root) || deleteRoot) {
	    Files.delete(dir);
	}
	return FileVisitResult.CONTINUE;
    }

}

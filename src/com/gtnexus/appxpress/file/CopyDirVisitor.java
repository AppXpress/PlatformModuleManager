package com.gtnexus.appxpress.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

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
	public FileVisitResult preVisitDirectory(final Path dir,
			final BasicFileAttributes attrs) throws IOException {
		Path target = destination.resolve(source.relativize(dir));
		if (!Files.exists(target)) {
			Files.createDirectory(target);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(final Path file,
			final BasicFileAttributes attrs) throws IOException {
		Files.copy(file, destination.resolve(source.relativize(file)), replace);
		return FileVisitResult.CONTINUE;
	}

}

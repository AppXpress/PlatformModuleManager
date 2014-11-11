package com.gtnexus.appxpress.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CopyDirVisitorTest {
	
	@Rule
	public TemporaryFolder source;
	@Rule
	public TemporaryFolder destination; 

	@Test
	public void testPreVisitDirectory() throws IOException {
		File subDir = source.newFolder("subDir");
		SimpleFileVisitor<Path> visitor = new CopyDirVisitor(source.getRoot().toPath(),
				destination.getRoot().toPath());
		visitor.preVisitDirectory(subDir.toPath(), BasicFileAttributes.);
		Path p = destination.getRoot().toPath().resolve(subDir.getName());
		File f = p.toFile();
		assertEquals(true, f.exists());
		assertEquals(true, f.isDirectory());
	}

}
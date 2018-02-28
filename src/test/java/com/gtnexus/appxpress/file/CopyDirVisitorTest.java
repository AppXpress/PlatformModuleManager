package com.gtnexus.appxpress.file;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.gtnexus.pmm.commons.file.CopyDirVisitor;

public class CopyDirVisitorTest {

    @Rule
    public TemporaryFolder sourceDir = new TemporaryFolder();
    @Rule
    public TemporaryFolder destinationDir = new TemporaryFolder();

    @Test
    public void testPreVisitDirectory() throws IOException {
	File subDir = sourceDir.newFolder("subDir");
	SimpleFileVisitor<Path> visitor = new CopyDirVisitor(sourceDir.getRoot().toPath(),
		destinationDir.getRoot().toPath());
	Path path = subDir.toPath();
	BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
	visitor.preVisitDirectory(path, attributes);
	Path p = destinationDir.getRoot().toPath().resolve(subDir.getName());
	File f = p.toFile();
	assertEquals(true, f.exists());
	assertEquals(true, f.isDirectory());
    }

    @Test
    public void testVisitFile() throws IOException {
	File file = sourceDir.newFile("file1");
	File sourceRoot = sourceDir.getRoot();
	File destinationRoot = destinationDir.getRoot();
	SimpleFileVisitor<Path> visitor = new CopyDirVisitor(sourceRoot.toPath(), destinationRoot.toPath());
	Path path = file.toPath();
	BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
	visitor.visitFile(path, attributes);
	Path destinationFilePath = destinationRoot.toPath().resolve(path);
	assertEquals(true, Files.exists(destinationFilePath));
    }

}
package com.gtnexus.appxpress.file;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.gtnexus.pmm.commons.file.DeleteDirVisitor;

public class DeleteDirVisitorTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void testVisitFile() throws IOException {
	File f = tmp.newFile();
	Path p = f.toPath();
	DeleteDirVisitor vis = new DeleteDirVisitor(tmp.getRoot().toPath(), false);
	BasicFileAttributes attributes = Files.readAttributes(p, BasicFileAttributes.class);
	assertEquals(true, Files.exists(p));
	vis.visitFile(p, attributes);
	assertEquals(false, Files.exists(p));
    }

    @Test
    public void testPostVisitDirectory() throws IOException {
	File f = tmp.newFolder();
	Path p = f.toPath();
	DeleteDirVisitor vis = new DeleteDirVisitor(tmp.getRoot().toPath(), false);
	vis.postVisitDirectory(p, null);
	assertEquals(false, Files.exists(p));
    }

    @Test
    public void testPostVisitRoot() throws IOException {
	Path path = tmp.getRoot().toPath();
	DeleteDirVisitor vis = new DeleteDirVisitor(path, false);
	vis.postVisitDirectory(path, null);
	assertEquals(true, Files.exists(path));
    }

    @Test
    public void testPostVisitAndDeleteRoot() throws IOException {
	Path path = tmp.getRoot().toPath();
	DeleteDirVisitor vis = new DeleteDirVisitor(path, true);
	vis.postVisitDirectory(path, null);
	assertEquals(false, Files.exists(path));
    }

}

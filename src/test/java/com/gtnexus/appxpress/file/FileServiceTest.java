package com.gtnexus.appxpress.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.gtnexus.pmm.api.v100.service.FileService;
import com.gtnexus.pmm.commons.HasPrerequisite;
import com.gtnexus.pmm.service.FileServiceImpl;

public class FileServiceTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private FileService fs = new FileServiceImpl();

    @Test
    public void testPrependToName() throws IOException {
	File f = tmp.newFile("burger");
	Path result = fs.prependToName(f, "cheese");
	assertEquals("cheeseburger", result.toFile().getName());
    }

    @Test(expected = IOException.class)
    public void testPrependToNonExistingFile() throws IOException {
	File f = new File("burger");
	Path result = fs.prependToName(f, "cheese");
	assertEquals("cheeseburger", result.toFile().getName());
    }

    @Test
    public void testPrependToCollection() throws IOException {
	for (int i = 0; i < 10; i++) {
	    tmp.newFile();
	}
	File f = tmp.getRoot();
	String prependString = "octo";
	fs.prependToName(Arrays.asList(f.listFiles()), prependString);
	for (File file : f.listFiles()) {
	    assertEquals(true, file.getName().startsWith(prependString));
	}
    }

    @Test
    public void testRenameFile() throws IOException {
	File f = tmp.newFile("prince");
	fs.renameFile(f, "king");
	assertEquals(false, f.exists());
	f = tmp.getRoot().listFiles()[0];
	assertEquals("king", f.getName());
    }

    @Test
    public void testRenameCollection() throws IOException {
	String toReplace = "gucci";
	for (int i = 0; i < 10; i++) {
	    tmp.newFile(toReplace + "_" + i);
	}
	File f = tmp.getRoot();

	fs.renameFile(Arrays.asList(f.listFiles()), toReplace, "coffee");
	for (File file : f.listFiles()) {
	    assertEquals(true, file.getName().startsWith("coffee_"));
	}
    }

    @Test
    public void testRenameEmptyCollection() throws IOException {
	List<Path> p = fs.renameFile(new ArrayList<File>(), "whatever", "kitty");
	assertEquals(0, p.size());
    }

    @Test
    public void testMoveFiles() throws IOException {
	TemporaryFolder source = new TemporaryFolder(tmp.getRoot());
	source.create();
	for (int i = 0; i < 10; i++) {
	    source.newFile();
	}
	File destination = tmp.newFolder("dest");
	fs.moveFiles(Arrays.asList(source.getRoot().listFiles()), destination);
	assertEquals(0, source.getRoot().list().length);
	assertEquals(10, destination.list().length);
    }

    @Test
    public void testMoveEmptyCollection() throws IOException {
	File destination = tmp.newFolder("dest");
	fs.moveFiles(new ArrayList<File>(), destination);
	assertEquals(0, destination.list().length);
    }

    @Test
    public void testCopyFiles() throws IOException {
	final String[] extension = { ".js", ".zip" };
	HasPrerequisite<File> pre = new HasPrerequisite<File>() {

	    @Override
	    public boolean isMet(File f) {
		return f.getName().endsWith(extension[0]);
	    }

	};
	TemporaryFolder source = new TemporaryFolder(tmp.getRoot());
	source.create();
	for (int i = 0; i < 10; i++) {
	    source.newFile(i + extension[i % 2]);
	}
	File destination = tmp.newFolder("destination");
	fs.copyFiles(Arrays.asList(source.getRoot().listFiles()), destination, pre);
	assertTrue(destination.list().length > 0);
	for (File f : destination.listFiles()) {
	    assertEquals(true, f.getName().endsWith(extension[0]));
	}
    }

    @Test
    public void testCopyFilesWithNullPrecondition() throws IOException {
	final String[] extension = { ".js", ".zip" };
	TemporaryFolder source = new TemporaryFolder(tmp.getRoot());
	source.create();
	for (int i = 0; i < 10; i++) {
	    source.newFile(i + extension[i % 2]);
	}
	File destination = tmp.newFolder("destination");
	fs.copyFiles(Arrays.asList(source.getRoot().listFiles()), destination, null);
	assertTrue(destination.list().length > 0);
	assertEquals(source.getRoot().list().length, destination.list().length);
	String[] destFiles = destination.list();
	String[] sourceFiles = source.getRoot().list();
	Arrays.sort(destFiles);
	Arrays.sort(sourceFiles);
	for (int i = 0; i < destFiles.length; i++) {
	    assertEquals(sourceFiles[i], destFiles[i]);
	}
    }

    @Ignore("Not yet implemented.")
    @Test
    public void testIsFileType() {

    }

    @Ignore("Not yet implemented.")
    @Test
    public void testCopyDirectory() {
	fail("Not yet implemented.");
    }

    @Ignore("Not yet implemented.")
    @Test
    public void testEmptyDir() {
	fail("Not yet implemented.");
    }

}

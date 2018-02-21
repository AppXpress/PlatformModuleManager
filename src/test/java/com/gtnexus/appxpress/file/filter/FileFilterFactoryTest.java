package com.gtnexus.appxpress.file.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.gtnexus.appxpress.commons.file.filter.FileFilterFactory;

public class FileFilterFactoryTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void testFileNameContains() {
	FileFilter filter = FileFilterFactory.fileNameContains("coke");
	assertFalse(filter.accept(new File("pepsi")));
	assertTrue(filter.accept(new File("coke")));
	assertTrue(filter.accept(new File("coke-a-doo-doo")));
    }

    @Test
    public void testFileNameDoesNotContain() {
	FileFilter filter = FileFilterFactory.fileNameDoesNotContain("sausage");
	assertFalse(filter.accept(new File("chicken sausage")));
	assertTrue(filter.accept(new File("cheeseburger")));
    }

    @Test
    public void testDirectoriesOnly() throws IOException {
	tmp.newFolder();
	tmp.newFolder();
	tmp.newFolder();
	tmp.newFile();
	File[] results = tmp.getRoot().listFiles(FileFilterFactory.directoriesOnly());
	assertEquals(3, results.length);
    }

    @Test
    public void testEndsWith() {
	FileFilter filter = FileFilterFactory.endsWith("tie");
	assertTrue(filter.accept(new File("bowtie")));
	assertTrue(filter.accept(new File("bow.tie")));
	assertFalse(filter.accept(new File("scarf")));
    }

    @Test
    public void testDoesNotEndWith() {
	FileFilter filter = FileFilterFactory.doesNotEndWith("tie");
	assertFalse(filter.accept(new File("bowtie")));
	assertFalse(filter.accept(new File("bow.tie")));
	assertTrue(filter.accept(new File("scarf")));

    }

}

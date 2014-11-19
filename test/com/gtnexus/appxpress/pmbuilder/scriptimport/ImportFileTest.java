package com.gtnexus.appxpress.pmbuilder.scriptimport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ImportFileTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	
	@Test
	public void testListFilesWithoutValidFile() {
		ImportFile f = new ImportFile("");
		ImportFile[] fs = f.listFiles();
		assertNull(fs);
	}
	
	@Test
	public void testListFiles() throws IOException {
		int max = 10;
		for(int i = 0; i < max; i++) {
			tmp.newFile();
		}
		ImportFile f = new ImportFile(tmp.getRoot());
		ImportFile[] files = f.listFiles();
		assertEquals(max, files.length);
	}
	
	@Test
	public void testCouldHaveImportsWithDir() throws IOException  {
		File d = tmp.newFolder();
		ImportFile f = new ImportFile(d);
		assertEquals(false, f.couldHaveImports());
	}
	
	@Test
	public void testCouldHaveImportsWithFile() throws IOException {
		File f = tmp.newFile("test.js");
		ImportFile ff = new ImportFile(f);
		assertEquals(true, ff.couldHaveImports());
	}
	
	@Test
	public void testCouldHaveImportsWithBlacklistedExtensions() throws IOException {
		File f = tmp.newFile("invalid.zip");
		ImportFile ff = new ImportFile(f);
		assertEquals(false, ff.couldHaveImports());
	}

}

package com.gtnexus.appxpress.pmbuilder.scriptimport;

import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Set;

import org.junit.Test;

public class ImportScannerTest {

	@Test
	public void testSingleLineSingleImport() {
		ImportScanner scanner = new ImportScanner();
		String testString = "//!import someTestFile";
		StringReader testReader = new StringReader(testString);
		Set<String> fileNames = scanner.parseDoc(testReader);
		assertTrue(fileNames != null);
		assertTrue(!fileNames.isEmpty());
	}

	@Test
	public void testSingleLineMultiImport() {
		ImportScanner scanner = new ImportScanner();
		String testString = "//!import someTestFile, anotherTestFile";
		StringReader testReader = new StringReader(testString);
		Set<String> fileNames = scanner.parseDoc(testReader);
		assertTrue(fileNames != null && !fileNames.isEmpty());
		assertTrue(fileNames.size() == 2);
	}

	@Test
	public void testBlockCommentSingleImport() {
		ImportScanner scanner = new ImportScanner();
		String testString = "/*\n* !import someTestFile\n*/";
		StringReader testReader = new StringReader(testString);
		Set<String> fileNames = scanner.parseDoc(testReader);
		assertTrue(fileNames != null);
		assertTrue(!fileNames.isEmpty());
	}

}

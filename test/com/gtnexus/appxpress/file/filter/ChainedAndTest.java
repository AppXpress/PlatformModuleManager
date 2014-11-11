package com.gtnexus.appxpress.file.filter;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ChainedAndTest {
	
	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	@Test
	public void testAccept() throws IOException {
		tmp.newFile("File");
		tmp.newFile("File2");
		tmp.newFile("file3");
		File root = tmp.getRoot();
		File[] result = root.listFiles(new ChainedAnd(FileFilterFactory.fileNameContains("File"),
				FileFilterFactory.fileNameContains("2")));
		assertEquals(1, result.length);
		assertEquals("File2", result[0].getName());
	}

}

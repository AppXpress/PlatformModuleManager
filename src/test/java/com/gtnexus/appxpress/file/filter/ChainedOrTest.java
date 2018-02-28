package com.gtnexus.appxpress.file.filter;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.gtnexus.pmm.commons.file.filter.ChainedOr;
import com.gtnexus.pmm.commons.file.filter.FileFilterFactory;

public class ChainedOrTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void testBasicAccept() throws IOException {
	tmp.newFile("booger.json");
	tmp.newFile("snot.xml");
	File root = tmp.getRoot();
	File[] result = root.listFiles(
		new ChainedOr(FileFilterFactory.endsWith(".json"), FileFilterFactory.fileNameContains("snot")));
	assertEquals(2, result.length);
    }

    @Test
    public void testEmptyChain() throws IOException {
	tmp.newFile("booger.json");
	tmp.newFile("snot.xml");
	File root = tmp.getRoot();
	File[] result = root.listFiles(new ChainedOr());
	assertEquals(2, result.length);
    }

    @Test
    public void testSingleFilter() throws IOException {
	tmp.newFile("booger.json");
	tmp.newFile("snot.xml");
	File root = tmp.getRoot();
	File[] result = root.listFiles(new ChainedOr(FileFilterFactory.endsWith(".json")));
	assertEquals(1, result.length);
    }

}

package com.gtnexus.appxpress.commons;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class PMPropertiesTest {

	@Rule
	public TemporaryFolder sourceDir = new TemporaryFolder();
	
	@Test
	public void testPropertiesChange() throws IOException {
		Properties raw = new Properties();
		raw.put("foo", "bar");
		PMProperties pmProperties= new PMProperties(raw, sourceDir.newFile());
		pmProperties.put("foo", "bar");
		assertFalse("Expected no change to properties file", pmProperties.haveChanged());
	}

}

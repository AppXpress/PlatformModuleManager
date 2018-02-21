package com.gtnexus.appxpress.pmextractor;

import static junit.framework.TestCase.fail;

import java.io.File;

import org.junit.Test;

import com.gtnexus.appxpress.commons.DirectoryHelper;
import com.gtnexus.appxpress.exception.AppXpressException;

/**
 * Created by jjdonov on 9/29/14.
 */
@Deprecated
public class DirectoryHelperTest {

    @Test(expected = AppXpressException.class)
    public void testWithoutEnsuring() throws AppXpressException {
	DirectoryHelper directoryHelper = new DirectoryHelper("test");
	directoryHelper.getPmProperties();
    }

    @Test
    public void testWithHomeDirectory() {
	DirectoryHelper directoryHelper = new DirectoryHelper("test");
	try {
	    directoryHelper.ensureAppXpress();
	    File propFile = directoryHelper.getPmProperties().getPropertiesFile();
	    if (propFile == null || !propFile.exists()) {
		fail("Property file does not exist");
	    }
	} catch (AppXpressException e) {
	    fail("Exception when ensuring\n" + e.getMessage());
	}
    }

}

package com.gtnexus.appxpress.pmextractor;

import static junit.framework.TestCase.fail;

import java.io.File;

import org.junit.Test;

import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.manager.DirectoryHelper;

/**
 * Created by jjdonov on 9/29/14.
 */
@Deprecated
public class DirectoryHelperTest {

    @Test(expected = SubCommandException.class)
    public void testWithoutEnsuring() throws SubCommandException {
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
	} catch (SubCommandException e) {
	    fail("Exception when ensuring\n" + e.getMessage());
	}
    }

}

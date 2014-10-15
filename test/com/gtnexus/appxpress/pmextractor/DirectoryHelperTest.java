package com.gtnexus.appxpress.pmextractor;

import com.gtnexus.appxpress.pmextractor.DirectoryHelper;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.fail;


/**
 * Created by jjdonov on 9/29/14.
 */
public class DirectoryHelperTest {

    @Test(expected = PMExtractorException.class)
    public void testWithoutEnsuring() throws PMExtractorException {
        DirectoryHelper directoryHelper = new DirectoryHelper();
        directoryHelper.getPmbProperties();
    }

    @Test
    public void testWithHomeDirectory() {
        DirectoryHelper directoryHelper = new DirectoryHelper();
        try {
            directoryHelper.ensureAppXpress();
            File propFile = directoryHelper.getPmbProperties().getPropertiesFile();
            if(propFile == null || !propFile.exists()) {
                fail("Property file does not exist");
            }
        } catch (PMExtractorException e) {
            fail("Exception when ensuring\n" + e.getMessage());
        }
    }

}

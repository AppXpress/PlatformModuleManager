package com.gtnexus.appxpress.platform.module.model.platformmodule;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class PlatformModuleTest {
	
	@Test
	public void testUnmarshall() throws FileNotFoundException, JAXBException {
		File file = new File("src/test/resources/PlatformModule.xml");
		FileReader reader = new FileReader(file);
        JAXBContext context = JAXBContext.newInstance(PlatformModuleXml.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        PlatformModuleXml mod = (PlatformModuleXml)unmarshaller.unmarshal(reader);
        assertNotNull(mod);
	}

}

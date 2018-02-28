package com.gtnexus.appxpress.platform.module.model.platformmodule;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.gtnexus.pmm.platform.module.model.platformmodule.PlatformModuleXml;

public class PlatformModuleTest {

    @Test
    public void testUnmarshall() throws FileNotFoundException, JAXBException {
	File file = new File("src/test/resources/PlatformModule.xml");
	FileReader reader = new FileReader(file);
	JAXBContext context = JAXBContext.newInstance("com.gtnexus.appxpress.platform.module.model.platformmodule");
	Unmarshaller unmarshaller = context.createUnmarshaller();
	PlatformModuleXml platformMod = (PlatformModuleXml) JAXBIntrospector.getValue(unmarshaller.unmarshal(reader));
	assertNotNull(platformMod);
    }

}

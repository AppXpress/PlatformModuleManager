package com.gtnexus.appxpress.pmdocgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;
import com.gtnexus.appxpress.platform.module.model.typeextension.TypeExtensionD1;

public class ModuleUnmarshaller {

	public ModuleUnmarshaller() {

	}

	public ModuleVO unmarshall(CollectedDocGenVO vo) throws AppXpressException {
		ModuleVO.Builder builder = new ModuleVO.Builder();
		try {
			builder.setPlatformModuleXml(unmarshallPlatMod(vo.getPlatformModuleXml()));
			for (File f : vo.getTypeExtensions()) {
				builder.addTypeExtension(unmarshallTE(f));
			}
			for(File f : vo.getDesigns()) {
				builder.addDesign(unmarshallCustomObjectDesignV110(f));
			}
		} catch (JAXBException | IOException e ) {
			throw new AppXpressException("Failed to unmarshall the platform module", e);
		}
		return builder.build();
	}

	private PlatformModuleXml unmarshallPlatMod(File f) throws JAXBException, FileNotFoundException, IOException {
		return unmarshall(PlatformModuleXml.class, f);
	}

	private TypeExtensionD1 unmarshallTE(File f) throws JAXBException, IOException {
		return unmarshall(TypeExtensionD1.class, f);
	}

	private CustomObjectDesignV110 unmarshallCustomObjectDesignV110(File f)
			throws JAXBException, IOException {
		return unmarshall(CustomObjectDesignV110.class, f);
	}

	private <X> X unmarshall(Class<X> type, File f) throws JAXBException, FileNotFoundException, IOException {
		try (FileReader reader = new FileReader(f)) {
			JAXBContext context = JAXBContext.newInstance(type); //we can check if we've created this context type before. 
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return type.cast(unmarshaller.unmarshal(reader));
		}
	}

}

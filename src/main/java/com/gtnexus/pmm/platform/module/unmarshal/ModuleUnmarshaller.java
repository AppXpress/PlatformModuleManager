package com.gtnexus.pmm.platform.module.unmarshal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.platform.module.ModuleModelPointer;
import com.gtnexus.pmm.platform.module.ModuleVO;
import com.gtnexus.pmm.platform.module.model.customaction.CustomActionD1;
import com.gtnexus.pmm.platform.module.model.customlink.CustomLinkD1;
import com.gtnexus.pmm.platform.module.model.design.CustomObjectDesignV110;
import com.gtnexus.pmm.platform.module.model.platformmodule.PlatformModuleXml;
import com.gtnexus.pmm.platform.module.model.template.TemplateMediaV110;
import com.gtnexus.pmm.platform.module.model.typeextension.TypeExtensionD1;

public class ModuleUnmarshaller {

    public ModuleUnmarshaller() {

    }

    public ModuleVO unmarshall(ModuleModelPointer vo) throws SubCommandException {
	ModuleVO.Builder builder = new ModuleVO.Builder();
	try {
	    builder.setPlatformModuleXml(unmarshallPlatMod(vo.getPlatformModuleXml()));
	    for (File f : vo.getCustomActions()) {
		builder.addCustomAction(unmarshallCA(f));
	    }
	    for (File f : vo.getCustomLinks()) {
		builder.addCustomLink(unmarshallCL(f));
	    }
	    for (File f : vo.getTypeExtensions()) {
		builder.addTypeExtension(unmarshallTE(f));
	    }
	    for (File f : vo.getTemplates()) {
		builder.addTemplate(unmarshallTM(f));
	    }
	    for (File f : vo.getDesigns()) {
		builder.addDesign(unmarshallCustomObjectDesignV110(f));
	    }
	} catch (JAXBException | IOException e) {
	    throw new SubCommandException("Failed to unmarshall the platform module", e);
	}
	return builder.build();
    }

    private PlatformModuleXml unmarshallPlatMod(File f) throws JAXBException, FileNotFoundException, IOException {
	try (FileReader reader = new FileReader(f)) {
	    JAXBContext context = JAXBContext.newInstance("com.gtnexus.appxpress.platform.module.model.platformmodule");
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    PlatformModuleXml platformMod = (PlatformModuleXml) JAXBIntrospector
		    .getValue(unmarshaller.unmarshal(reader));
	    return platformMod;
	}
    }

    private CustomActionD1 unmarshallCA(File f) throws JAXBException, IOException {
	return unmarshall(CustomActionD1.class, f);
    }

    private CustomLinkD1 unmarshallCL(File f) throws JAXBException, IOException {
	return unmarshall(CustomLinkD1.class, f);
    }

    private TypeExtensionD1 unmarshallTE(File f) throws JAXBException, IOException {
	return unmarshall(TypeExtensionD1.class, f);
    }

    private TemplateMediaV110 unmarshallTM(File f) throws JAXBException, IOException {
	return unmarshall(TemplateMediaV110.class, f);
    }

    private CustomObjectDesignV110 unmarshallCustomObjectDesignV110(File f) throws JAXBException, IOException {
	return unmarshall(CustomObjectDesignV110.class, f);
    }

    private <X> X unmarshall(Class<X> type, File f) throws JAXBException, FileNotFoundException, IOException {
	try (FileReader reader = new FileReader(f)) {
	    JAXBContext context = JAXBContext.newInstance(type); // we can check if we've created this context type
								 // before.
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    return type.cast(unmarshaller.unmarshal(reader));
	}
    }

}

package com.gtnexus.appxpress.pmdocgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.appxpress.commons.command.Command;
import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;
import com.gtnexus.appxpress.platform.module.model.typeextension.TypeExtensionD1;
import com.gtnexus.appxpress.pmdocgen.excel.ChangeLogRenderer;

public class PMDocGenCommand implements Command {

	String[] sheets = { "Change Log", };

	@Override
	public void execute() {
		// read in args
		// find files of interest in the module
		// resolve the necessary renderers
		// do rendering.
		try {
			newWorkBook();
		} catch (IOException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void newWorkBook() throws IOException, JAXBException {
		XSSFWorkbook wb = new XSSFWorkbook();
		PlatformModuleXml mod = getDummyPlatMod();
		TypeExtensionD1 te = getDummyTE();
		CustomObjectDesignV110 design = getDummyDesign();
		populate(wb, mod);
		populate(wb, te);
		populate(wb, design);
		FileOutputStream fileOut = new FileOutputStream("workbook.xlsx");
		wb.write(fileOut);
		fileOut.close();
	}

	private PlatformModuleXml getDummyPlatMod() throws FileNotFoundException, JAXBException {
		File file = new File("src/test/resources/PlatformModule.xml");
		FileReader reader = new FileReader(file);
		JAXBContext context = JAXBContext.newInstance(PlatformModuleXml.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		PlatformModuleXml mod = (PlatformModuleXml) unmarshaller.unmarshal(reader);
		return mod;
	}
	
	private TypeExtensionD1 getDummyTE() throws FileNotFoundException, JAXBException {
		File file = new File("src/test/resources/PackingManifest__Rank-10.xml");
		FileReader reader = new FileReader(file);
		JAXBContext context = JAXBContext.newInstance(TypeExtensionD1.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		TypeExtensionD1 te = (TypeExtensionD1) unmarshaller.unmarshal(reader);
		return te;
	}
	
	private CustomObjectDesignV110 getDummyDesign() throws FileNotFoundException, JAXBException {
		File file = new File("src/test/resources/Design_ParcelTrackerS1.xml");
		FileReader reader = new FileReader(file);
		JAXBContext context = JAXBContext.newInstance(CustomObjectDesignV110.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CustomObjectDesignV110 design = (CustomObjectDesignV110) unmarshaller.unmarshal(reader);
		return design;
	}

	protected void populate(XSSFWorkbook wb, PlatformModuleXml mod) {
		new ChangeLogRenderer(wb).render(mod);
	}
	
	protected void populate(XSSFWorkbook wb, TypeExtensionD1 te) {
		new TypeExtensionDocRenderer(wb).render(Arrays.asList(te));
	}
	
	protected void populate(XSSFWorkbook wb, CustomObjectDesignV110 design) {
		new CustomObjectDesignDocRenderer(wb).render(design);
	}
	
	

}

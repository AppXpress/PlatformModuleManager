package com.gtnexus.appxpress.pmdocgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.appxpress.commons.command.Command;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;
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
		File file = new File("src/test/resources/PlatformModule.xml");
		FileReader reader = new FileReader(file);
		JAXBContext context = JAXBContext.newInstance(PlatformModuleXml.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		PlatformModuleXml mod = (PlatformModuleXml) unmarshaller.unmarshal(reader);
		populate(wb, mod);
		FileOutputStream fileOut = new FileOutputStream("workbook.xlsx");
		wb.write(fileOut);
		fileOut.close();
	}

	protected void populate(XSSFWorkbook wb, PlatformModuleXml mod) {
		new ChangeLogRenderer(wb).render(mod);
//		new TypeExtensionDocRenderer(wb).render(mod);
	}
	

}

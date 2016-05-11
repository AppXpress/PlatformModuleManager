package com.gtnexus.appxpress.pmdocgen;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;
import com.gtnexus.appxpress.pmdocgen.renderer.excel.ChangeLogRenderer;
import com.gtnexus.appxpress.pmdocgen.renderer.excel.CustomObjectDesignDocRenderer;
import com.gtnexus.appxpress.pmdocgen.renderer.excel.TypeExtensionDocRenderer;

public class WorkbookRenderer {

	public WorkbookRenderer() {
	}

	public XSSFWorkbook render(ModuleVO vo) {
		XSSFWorkbook wb = new XSSFWorkbook();
		new ChangeLogRenderer(wb).render(vo.getPlatformModuleXml());
		new TypeExtensionDocRenderer(wb).render(vo.getTypeExtensions());
		for(CustomObjectDesignV110 design : vo.getDesigns()) {
			new CustomObjectDesignDocRenderer(wb).render(design);
		}
		return wb;
	}

}

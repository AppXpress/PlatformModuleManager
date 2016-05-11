package com.gtnexus.appxpress.pmdocgen;

import java.util.Collections;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.appxpress.platform.module.ModuleVO;
import com.gtnexus.appxpress.platform.module.interpretation.CustomObjectDesignV110Comparator;
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
		List<CustomObjectDesignV110> designs = vo.getDesigns();
		Collections.sort(designs, new CustomObjectDesignV110Comparator());
		for(CustomObjectDesignV110 design : vo.getDesigns()) {
			new CustomObjectDesignDocRenderer(wb).render(design);
		}
		return wb;
	}

}

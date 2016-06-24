package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.platformmodule.CustomUiConfiguration;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;
import com.gtnexus.appxpress.pmdocgen.adapter.CustomUiConfigurationDisplayAdapter;

public class UserInterfacesRenderer extends BaseSheetRenderer<PlatformModuleXml> {
	
	private final static Map<String, Function<CustomUiConfiguration, String>> TABLE_COLS = 
			new ImmutableMap.Builder<String, Function<CustomUiConfiguration, String>>()
			.put("Global Object Type", CustomUiConfigurationDisplayAdapter.GLOBAL_OBJECT_TYPE_FN)
			.put("Resource Bundle Directory", CustomUiConfigurationDisplayAdapter.RESOURCE_DIRECTORY_FN)
			.put("User Interface", CustomUiConfigurationDisplayAdapter.UI_TYPE_FN)
			.build();
	
	private final static String SHEET_NAME = "User Interfaces";
	private final static int MAX_WIDTH = TABLE_COLS.size() - 1; 
	
	public UserInterfacesRenderer(XSSFWorkbook wb) {
		super(wb,SHEET_NAME, MAX_WIDTH);
	}
	
	
	@Override
	public void render(PlatformModuleXml mod) {
		renderHeader();
		for(CustomUiConfiguration cui : mod.getCustomUiConfiguration()) {
			render(cui);
		}
		autofit();
	}
	
	private void renderHeader() {
		traverser.nextRow();
		for(String k : TABLE_COLS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(k);
			cell.setCellStyle(styleProvider.getHeaderStyle());
		}
	}
	
	private void render(CustomUiConfiguration cui) {
		traverser.nextRow();
		for(String key : TABLE_COLS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(TABLE_COLS.get(key).apply(cui));
		}
	}
}

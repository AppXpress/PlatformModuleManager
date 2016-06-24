package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Collection;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.customaction.CustomActionD1;
import com.gtnexus.appxpress.pmdocgen.adapter.CustomActionD1DisplayAdapter;

public class CustomActionDocRenderer extends BaseSheetRenderer<Collection<CustomActionD1>> {

	private final static String SHEET_NAME="Custom Actions";
	
	private final static Map<String, Function<CustomActionD1, String>> TABLE_COLS = 
			new ImmutableMap.Builder<String, Function<CustomActionD1, String>>()
			.put("Document Type", CustomActionD1DisplayAdapter.DOC_TYPE_FN)
			.put("API Version", CustomActionD1DisplayAdapter.API_VERSION_FN)
			.put("Name", CustomActionD1DisplayAdapter.NAME_FN)
			.put("Roles", CustomActionD1DisplayAdapter.ROLES_FN)
			.put("OQL Filter", CustomActionD1DisplayAdapter.OQL_FN)
			.put("Action Availability Function Name", CustomActionD1DisplayAdapter.HAS_ACTION_FN_NAME_FN)
			.put("Execution Function Name", CustomActionD1DisplayAdapter.EXECUTION_FN_NAME_FN)
			.build();
	
	private static final int MAX_WIDTH = TABLE_COLS.size() - 1;
	
	public CustomActionDocRenderer(XSSFWorkbook wb) {
		super(wb, SHEET_NAME, MAX_WIDTH);
	}
	
	@Override
	public void render(Collection<CustomActionD1> source) {
		renderHeader();
		for(CustomActionD1 ca : source) {
			render(ca);
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
	
	private void render(CustomActionD1 ca) {
		traverser.nextRow();
		for(String key : TABLE_COLS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(TABLE_COLS.get(key).apply(ca));
		}
	}

}

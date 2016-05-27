package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Collection;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.typeextension.TypeExtensionD1;
import com.gtnexus.appxpress.pmdocgen.adapter.TypeExtensionD1DisplayAdapter;

public class TypeExtensionDocRenderer extends BaseSheetRenderer<Collection<TypeExtensionD1>> {

	private final static String SHEET_NAME="Type Extensions";
	
	private final static Map<String, Function<TypeExtensionD1, String>> TABLE_COLS = 
			new ImmutableMap.Builder<String, Function<TypeExtensionD1, String>>()
			.put("Document Type", TypeExtensionD1DisplayAdapter.DOC_TYPE_FN)
			.put("API Version", TypeExtensionD1DisplayAdapter.API_VERSION_FN)
			.put("Rank", TypeExtensionD1DisplayAdapter.RANK_FN)
			.put("Event", TypeExtensionD1DisplayAdapter.EVENT_FN)
			.put("Role", TypeExtensionD1DisplayAdapter.ROLE_FN)
			.put("Function Name", TypeExtensionD1DisplayAdapter.FN_NAME_FN)
			.build();
	
	private static final int MAX_WIDTH = TABLE_COLS.size() - 1;
	
	public TypeExtensionDocRenderer(XSSFWorkbook wb) {
		super(wb, SHEET_NAME, MAX_WIDTH);
	}
	
	@Override
	public void render(Collection<TypeExtensionD1> source) {
		renderHeader();
		for(TypeExtensionD1 te : source) {
			render(te);
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
	
	private void render(TypeExtensionD1 te) {
		traverser.nextRow();
		for(String key : TABLE_COLS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(TABLE_COLS.get(key).apply(te));
		}
	}

}

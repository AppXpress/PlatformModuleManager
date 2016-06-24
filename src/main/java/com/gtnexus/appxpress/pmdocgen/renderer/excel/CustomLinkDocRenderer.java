package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Collection;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.customlink.CustomLinkD1;
import com.gtnexus.appxpress.pmdocgen.adapter.CustomLinkD1DisplayAdapter;

public class CustomLinkDocRenderer extends BaseSheetRenderer<Collection<CustomLinkD1>> {

	private final static String SHEET_NAME="Links";
	
	private final static Map<String, Function<CustomLinkD1, String>> TABLE_COLS = 
			new ImmutableMap.Builder<String, Function<CustomLinkD1, String>>()
			.put("Link ID", CustomLinkD1DisplayAdapter.LINK_ID_FN)
			.put("Name", CustomLinkD1DisplayAdapter.NAME_FN)
			.put("Status", CustomLinkD1DisplayAdapter.STATUS_FN)
			.put("Localization Key", CustomLinkD1DisplayAdapter.LOCALIZATION_KEY_FN)
			.put("Link Type", CustomLinkD1DisplayAdapter.LINK_TYPE_FN)
			.put("Display Location", CustomLinkD1DisplayAdapter.DISPLAY_LOCATION_FN)
			.build();
	
	private static final int MAX_WIDTH = TABLE_COLS.size() - 1;
	
	public CustomLinkDocRenderer(XSSFWorkbook wb) {
		super(wb, SHEET_NAME, MAX_WIDTH);
	}
	
	@Override
	public void render(Collection<CustomLinkD1> source) {
		renderHeader();
		for(CustomLinkD1 cl : source) {
			render(cl);
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
	
	private void render(CustomLinkD1 ca) {
		traverser.nextRow();
		for(String key : TABLE_COLS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(TABLE_COLS.get(key).apply(ca));
		}
	}

}
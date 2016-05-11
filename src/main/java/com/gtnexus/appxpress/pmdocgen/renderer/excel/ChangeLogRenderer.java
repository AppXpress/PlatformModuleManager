package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;

public class ChangeLogRenderer extends BaseSheetRenderer<PlatformModuleXml> {
		
	private final Map<String, Function<PlatformModuleXml, String>> HEADER_LVL_DESCRIPTORS_MAP = 
			new ImmutableMap.Builder<String, Function<PlatformModuleXml, String>>()
			.put("Module Name", PlatformModuleXml.NAME_FN)
			.put("API Version", PlatformModuleXml.API_VERSION_FN)
			.put("Description", PlatformModuleXml.DESCRIPTION_FN)
			.build();
	
	private final static List<String> CHANGE_LOG_COL_LABELS = new ImmutableList.Builder<String>()
			.add("Jira #")
			.add("Version")
			.add("Date Requested")
			.add("Person Requesting")
			.add("Changes")
			.add("Business Reason")
			.build();
	
	private final static String SHEET_NAME = "Change Log";
	private final String HEADER = "GTNexus Custom Object Specification";
	private final static int MAX_WIDTH = CHANGE_LOG_COL_LABELS.size() - 1; 
	
	public ChangeLogRenderer(XSSFWorkbook wb) {
		super(wb,SHEET_NAME, MAX_WIDTH);
	}
	
	@Override
	public void render(PlatformModuleXml mod) {
		renderTitle(mod);
		renderHeaderLevelDescriptors(mod);
		renderChangeLogDetails();
		autofit();
	}
	
	private void renderTitle(PlatformModuleXml mod) {
		final XSSFRow titleRow = traverser.nextRow();
		final XSSFCell titleCell = traverser.nextCell();
		titleCell.setCellValue(HEADER);
		titleCell.setCellStyle(styleProvider.getHeaderStyle());
		CellRangeAddress headerRegion = new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, MAX_WIDTH);
		sheet.addMergedRegion(headerRegion);
	}
	
	private void renderHeaderLevelDescriptors(PlatformModuleXml mod) {
		for(String descriptor : HEADER_LVL_DESCRIPTORS_MAP.keySet()) {
			traverser.nextRow();
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(descriptor);
			cell.setCellStyle(styleProvider.getSecondaryHeaderStyle());
			cell = traverser.nextCell();
			sheet.addMergedRegion(new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), MAX_WIDTH));
			cell.setCellValue(HEADER_LVL_DESCRIPTORS_MAP.get(descriptor).apply(mod));
		}
	}
	
	private void renderChangeLogDetails() {
		traverser.nextRow();
		renderChangeLogDetailsHeader();
		renderEmptyLogSpace();
	}
	
	private void renderChangeLogDetailsHeader() {
		for(String colLabel : CHANGE_LOG_COL_LABELS) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(colLabel);
			cell.setCellStyle(styleProvider.getHeaderStyle());
		}
	}
	
	private void renderEmptyLogSpace() {
		for(int i = 0; i < 10; i++) {
			traverser.nextRow();
			for(int j = 0; j <= MAX_WIDTH; j++) {
				XSSFCell cell = traverser.nextCell();
				cell.setCellStyle(styleProvider.getAllBordersStyle());
			}
		}
	}

}

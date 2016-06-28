package com.gtnexus.appxpress.pmdocgen.renderer.excel.style;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GtnxStyleProviderImpl implements StyleProvider {
	
	private final XSSFWorkbook workBook;
	
	public GtnxStyleProviderImpl(XSSFWorkbook workBook) {
		this.workBook = workBook;
	}

	@Override
	public XSSFCellStyle getHeaderStyle() {
		XSSFCellStyle style = workBook.createCellStyle();
		style.setFillForegroundColor(GtnxColors.DARK_BLUE);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont createFont = workBook.createFont();
		createFont.setColor(GtnxColors.WHITE);
		style.setFont(createFont);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}

	@Override
	public XSSFCellStyle getSecondaryHeaderStyle() {
		XSSFCellStyle style = workBook.createCellStyle();
		style.setFillForegroundColor(GtnxColors.GREEN);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}

	@Override
	public XSSFCellStyle getAllBordersStyle() {
		XSSFCellStyle style = workBook.createCellStyle();
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		return style;
	}

}

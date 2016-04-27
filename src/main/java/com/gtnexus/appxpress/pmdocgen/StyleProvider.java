package com.gtnexus.appxpress.pmdocgen;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public interface StyleProvider {
	public XSSFCellStyle getHeaderStyle();
	public XSSFCellStyle getSecondaryHeaderStyle();
	public XSSFCellStyle getAllBordersStyle();
}

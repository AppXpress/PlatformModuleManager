package com.gtnexus.pmm.pmdocgen.renderer.excel.style;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public interface StyleProvider {
    public XSSFCellStyle getHeaderStyle();

    public XSSFCellStyle getSecondaryHeaderStyle();

    public XSSFCellStyle getAllBordersStyle();
}

package com.gtnexus.pmm.docgen.renderer.excel.style;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public interface StyleProvider {
    public XSSFCellStyle getHeaderStyle();

    public XSSFCellStyle getSecondaryHeaderStyle();

    public XSSFCellStyle getAllBordersStyle();
}

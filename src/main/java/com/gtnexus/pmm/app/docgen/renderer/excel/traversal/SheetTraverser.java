package com.gtnexus.pmm.app.docgen.renderer.excel.traversal;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

public interface SheetTraverser {
    public XSSFRow nextRow();

    public XSSFRow previousRow();

    public XSSFCell nextCell();

    public XSSFCell previousCell();
}

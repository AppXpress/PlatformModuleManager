package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.appxpress.pmdocgen.renderer.excel.style.GtnxStyleProviderImpl;
import com.gtnexus.appxpress.pmdocgen.renderer.excel.style.StyleProvider;
import com.gtnexus.appxpress.pmdocgen.renderer.excel.traversal.SheetTraverser;
import com.gtnexus.appxpress.pmdocgen.renderer.excel.traversal.SheetTraverserImpl;

public abstract class BaseSheetRenderer<X> implements SheetRenderer<X> {
	
	protected final XSSFWorkbook workBook;
	protected final XSSFSheet sheet;
	protected final SheetTraverser traverser;
	protected final StyleProvider styleProvider;
	
	protected BaseSheetRenderer(XSSFWorkbook workBook, String sheetName) {
		this.workBook = workBook;
		this.sheet = workBook.createSheet(sheetName);
		this.traverser = new SheetTraverserImpl(sheet);
		this.styleProvider = new GtnxStyleProviderImpl(workBook);
	}
	
	public abstract int getMaxWidth();
	public abstract void render(X source);
	
	public XSSFSheet getSheet() {
		return this.sheet;
	}
	
	protected void autofit() {
		for(int i = 0; i <= getMaxWidth(); i++) {
			sheet.autoSizeColumn(i);	
		}
	}
}













































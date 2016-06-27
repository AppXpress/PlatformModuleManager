package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.appxpress.pmdocgen.adapter.DisplayAdapter;
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
	
	@Override
	public final void render(X source) {
		if (source == null) {
			return;
		}
		renderNonNull(source);
	}
	
	public abstract void renderNonNull(X source);
	
	public int getMaxWidth() {
		return this.maxWidth;
	}
	
	public XSSFSheet getSheet() {
		return this.sheet;
	}
	
	protected void autofit() {
		for(int i = 0; i <= getMaxWidth(); i++) {
			sheet.autoSizeColumn(i);	
		}
	}
	
	protected <T> void renderLabelValueSectionHeader(DisplayAdapter<T> adapter) {
		renderLabelValueSectionHeader(adapter.iterator());
	}
	
	protected void renderLabelValueSectionHeader(Iterator<String> labels) {
		traverser.nextRow();
		while(labels.hasNext()) {
			XSSFCell nextCell = traverser.nextCell();
			nextCell.setCellValue(labels.next());
			nextCell.setCellStyle(styleProvider.getHeaderStyle());
		}
	}
	
	protected <T> void renderLabelValueSection(T target, DisplayAdapter<T> adapter) {
		renderLabelValueSection(target, adapter, adapter.size());
	}
	
	protected <T> void renderLabelValueSection(T target, DisplayAdapter<T> adapter, int width) {
		Iterator<String> iterator = adapter.iterator();
		while (iterator.hasNext()) {
			traverser.nextRow();
			renderLabelValueRow(target, adapter, width, iterator);
		}
	}
	
	protected <T> void renderLabelValueRow(T target, DisplayAdapter<T> adapter, int width, Iterator<String> iterator) {
		for (int i = 0; i < width; i++) {
			String key = iterator.next();
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(adapter.display(target, key));
		}
	}
}













































package com.gtnexus.pmm.docgen.renderer.excel;

import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.pmm.docgen.adapter.DisplayAdapter;
import com.gtnexus.pmm.docgen.renderer.excel.style.GtnxStyleProviderImpl;
import com.gtnexus.pmm.docgen.renderer.excel.style.StyleProvider;
import com.gtnexus.pmm.docgen.renderer.excel.traversal.SheetTraverser;
import com.gtnexus.pmm.docgen.renderer.excel.traversal.SheetTraverserImpl;

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

    @Override
    public final void render(X source) {
	if (source == null) {
	    return;
	}
	renderNonNull(source);
    }

    public final void renderStandardNonNull(X source, DisplayAdapter<X> adapter) {
	renderHeaderRow(adapter);
	renderValueRows(source, adapter);
    }

    public abstract void renderNonNull(X source);

    public abstract int getMaxWidth();

    public XSSFSheet getSheet() {
	return this.sheet;
    }

    protected void autofit() {
	for (int i = 0; i <= getMaxWidth(); i++) {
	    sheet.autoSizeColumn(i);
	}
    }

    protected <T> void renderHeaderRow(DisplayAdapter<T> adapter) {
	renderHeaderRow(adapter.iterator());
    }

    private void renderHeaderRow(Iterator<String> labels) {
	traverser.nextRow();
	while (labels.hasNext()) {
	    XSSFCell nextCell = traverser.nextCell();
	    nextCell.setCellValue(labels.next());
	    nextCell.setCellStyle(styleProvider.getHeaderStyle());
	}
    }

    protected <T> void renderValueRows(T target, DisplayAdapter<T> adapter) {
	Iterator<String> iterator = adapter.iterator();
	while (iterator.hasNext()) {
	    traverser.nextRow();
	    renderValueRow(target, adapter, adapter.size(), iterator);
	}
    }

    protected <T> void renderValueRow(T target, DisplayAdapter<T> adapter, int width, Iterator<String> iterator) {
	for (int i = 0; i < width; i++) {
	    String key = iterator.next();
	    XSSFCell cell = traverser.nextCell();
	    cell.setCellValue(adapter.display(target, key));
	}
    }
}
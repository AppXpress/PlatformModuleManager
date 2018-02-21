package com.gtnexus.appxpress.pmdocgen.renderer.excel.traversal;

import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.google.common.base.Preconditions;

/**
 * Some comment about how POI skips cells rows that aren't created.
 * 
 * We want to generally create anythign that does not exist as we go.
 */
public class SheetTraverserImpl implements SheetTraverser {

    private final XSSFSheet sheet;
    private final Coord currentCoord;

    public SheetTraverserImpl(XSSFSheet sheet) {
	Preconditions.checkNotNull(sheet);
	this.sheet = sheet;
	this.currentCoord = new Coord(-1, -1);
    }

    @Override
    public XSSFRow nextRow() {
	currentCoord.incrementRow();
	currentCoord.setCol(-1);
	XSSFRow row = currentRow();
	return row;
    }

    @Override
    public XSSFCell nextCell() {
	currentCoord.incrementCol();
	XSSFCell cell = currentRow().getCell(currentCoord.getCol());
	if (cell == null) {
	    cell = currentRow().createCell(currentCoord.getCol());
	}
	return cell;
    }

    private XSSFRow currentRow() {
	XSSFRow row = sheet.getRow(currentCoord.getRow());
	if (row == null) {
	    row = sheet.createRow(currentCoord.getRow());
	}
	return row;
    }

    // ***********************************************************************
    // ***********************************************************************
    // ***********************************************************************

    public Iterator<XSSFRow> traverseRows() {
	throw new RuntimeException("unimplemented");
    }

    public Iterator<XSSFCell> traverseCells(XSSFRow row) {
	throw new RuntimeException("unimplemented");
    }

    public Iterator<XSSFCell> traverseCells(int rowIndex) {
	throw new RuntimeException("unimplemented");
    }

    @Override
    public XSSFRow previousRow() {
	throw new RuntimeException("unimplemented");
    }

    @Override
    public XSSFCell previousCell() {
	throw new RuntimeException("unimplemented");
    }

}

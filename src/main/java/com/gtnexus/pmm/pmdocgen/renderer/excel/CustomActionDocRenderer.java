package com.gtnexus.pmm.pmdocgen.renderer.excel;

import java.util.Collection;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.pmm.platform.module.model.customaction.CustomActionD1;
import com.gtnexus.pmm.pmdocgen.adapter.CustomActionD1DisplayAdapter;

public class CustomActionDocRenderer extends BaseSheetRenderer<Collection<CustomActionD1>> {

    private final CustomActionD1DisplayAdapter customActionD1DisplayAdapter;

    private final static String SHEET_NAME = "Custom Actions";
    private static final int MAX_WIDTH = 7;

    public CustomActionDocRenderer(XSSFWorkbook wb) {
	super(wb, SHEET_NAME);
	this.customActionD1DisplayAdapter = new CustomActionD1DisplayAdapter();
    }

    @Override
    public int getMaxWidth() {
	return MAX_WIDTH;
    }

    @Override
    public void renderNonNull(Collection<CustomActionD1> source) {
	if (source.isEmpty()) {
	    return;
	} else {
	    for (CustomActionD1 ca : source) {
		renderCustomAction(ca);
		traverser.nextRow();
	    }
	    autofit();
	}
    }

    private void renderCustomAction(CustomActionD1 ca) {
	renderHeaderRow(customActionD1DisplayAdapter);
	renderValueRows(ca, customActionD1DisplayAdapter);
    }

}

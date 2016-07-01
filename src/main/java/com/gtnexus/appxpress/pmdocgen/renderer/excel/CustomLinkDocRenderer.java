package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Collection;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.appxpress.platform.module.model.customlink.CustomLinkD1;
import com.gtnexus.appxpress.pmdocgen.adapter.CustomLinkD1DisplayAdapter;

public class CustomLinkDocRenderer extends BaseSheetRenderer<Collection<CustomLinkD1>> {

	private final CustomLinkD1DisplayAdapter customLinkD1DisplayAdapter;
	
	private final static String SHEET_NAME="Links";
	private static final int MAX_WIDTH = 6;
	
	public CustomLinkDocRenderer(XSSFWorkbook wb) {
		super(wb, SHEET_NAME);
		this.customLinkD1DisplayAdapter = new CustomLinkD1DisplayAdapter();
	}
	
	@Override
	public int getMaxWidth() {
		return MAX_WIDTH;
	}
	
	@Override
	public void renderNonNull(Collection<CustomLinkD1> source) {
		if (source.isEmpty()) {
			return;
		} else {
			for(CustomLinkD1 cl : source) {
				renderCustomLink(cl);
				traverser.nextRow();
			}
			autofit();
		}
	}
	
	private void renderCustomLink(CustomLinkD1 cl) {
		renderHeaderRow(customLinkD1DisplayAdapter);
		renderValueRows(cl, customLinkD1DisplayAdapter);
	}
}
package com.gtnexus.appxpress.pmdocgen;

import java.util.Collection;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.typeextension.TypeExtensionD1;
import com.gtnexus.appxpress.pmdocgen.excel.SheetRenderer;
import com.gtnexus.appxpress.pmdocgen.excel.SheetTraverser;
import com.gtnexus.appxpress.pmdocgen.excel.SheetTraverserImpl;

/**
 * https://gtnexus.sharepoint.com/sites/PSO/_layouts/15/WopiFrame.aspx?sourcedoc={AEBA293C-B50E-4C05-B4B6-D1608680E370}&file=GT%20Nexus%20Custom%20Object%20Technical%20Specification.xlsx&action=default
 */
public class TypeExtensionDocRenderer implements SheetRenderer<Collection<TypeExtensionD1>> {

	private final String SHEET_NAME="Type Extensions";
	
	private final static Map<String, Function<TypeExtensionD1, String>> TABLE_COLS = 
			new ImmutableMap.Builder<String, Function<TypeExtensionD1, String>>()
			.put("Document Type", TypeExtensionD1.DOC_TYPE_FN)
			.put("API Version", TypeExtensionD1.API_VERSION_FN)
			.put("Rank", TypeExtensionD1.RANK_FN)
			.put("Event", TypeExtensionD1.EVENT_FN)
			.put("Role", TypeExtensionD1.ROLE_FN)
			.put("Function Name", TypeExtensionD1.FN_NAME_FN)
			.build();
	
	private static final int MAX_WIDTH = TABLE_COLS.size() - 1;
	
	private final XSSFWorkbook workBook;
	private final XSSFSheet sheet;
	private final SheetTraverser traverser;
	private final StyleProvider styleProvider;
	
	public TypeExtensionDocRenderer(XSSFWorkbook wb) {
		this.workBook = wb;
		this.sheet = wb.createSheet(SHEET_NAME);
		this.traverser = new SheetTraverserImpl(sheet, MAX_WIDTH);
		this.styleProvider = new GtnxStyleProviderImpl(workBook);
	}
	
	@Override
	public void render(Collection<TypeExtensionD1> source) {
		renderHeader();
		for(TypeExtensionD1 te : source) {
			render(te);
		}
	}
	
	private void renderHeader() {
		for(String k : TABLE_COLS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(k);
			cell.setCellStyle(styleProvider.getHeaderStyle());
		}
	}
	
	private void render(TypeExtensionD1 te) {
		
	}

}

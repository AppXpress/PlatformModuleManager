package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Collection;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.gtnexus.appxpress.platform.module.model.typeextension.TypeExtensionD1;
import com.gtnexus.appxpress.pmdocgen.adapter.TypeExtensionD1DisplayAdapter;

public class TypeExtensionDocRenderer extends BaseSheetRenderer<Collection<TypeExtensionD1>> {

	private final TypeExtensionD1DisplayAdapter typeExtensionD1DisplayAdapter;

	private final static String SHEET_NAME="Type Extensions";
	private static final int MAX_WIDTH = 6;
	
	public TypeExtensionDocRenderer(XSSFWorkbook wb) {
		super(wb, SHEET_NAME);
		this.typeExtensionD1DisplayAdapter = new TypeExtensionD1DisplayAdapter();
	}
	
	@Override
	public int getMaxWidth() {
		return MAX_WIDTH;
	}
	
	@Override
	public void renderNonNull(Collection<TypeExtensionD1> source) {
		if (source.isEmpty()) {
			return;
		} else {
		for(TypeExtensionD1 te : source) {
				renderTypeExtension(te);
				traverser.nextRow();
		}
		autofit();
	}
	}
	
	private void renderTypeExtension(TypeExtensionD1 te) {
		renderLabelValueSectionHeader(typeExtensionD1DisplayAdapter);
		renderLabelValueSection(te, typeExtensionD1DisplayAdapter);
	}

}

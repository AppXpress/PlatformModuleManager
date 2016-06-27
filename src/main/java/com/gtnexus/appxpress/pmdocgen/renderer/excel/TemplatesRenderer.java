package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Collection;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.gtnexus.appxpress.platform.module.model.template.TemplateMediaV110;
import com.gtnexus.appxpress.pmdocgen.adapter.TemplateDisplayAdapter;

public class TemplatesRenderer extends BaseSheetRenderer<Collection<TemplateMediaV110>> {
	
	private final TemplateDisplayAdapter templateDisplayAdapter;
	
	private final static String SHEET_NAME = "Templates";
	private final static int MAX_WIDTH = 5; 
	
	public TemplatesRenderer(XSSFWorkbook workBook) {
		super(workBook, SHEET_NAME, MAX_WIDTH);
		this.templateDisplayAdapter = new TemplateDisplayAdapter();
	}
	
	@Override
	public void renderNonNull(Collection<TemplateMediaV110> source) {
		if (source.isEmpty()) {
			return;
		} else {
			for (TemplateMediaV110 template : source) {
				renderTemplate(template);
				traverser.nextRow();
			}
			autofit();
		}
	}
	
	private void renderTemplate(TemplateMediaV110 template) {
		renderLabelValueSectionHeader(templateDisplayAdapter);
		renderLabelValueSection(template, templateDisplayAdapter);
	}
}
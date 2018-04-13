package com.gtnexus.pmm.app.docgen.renderer.excel;

import java.util.Collection;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.pmm.app.docgen.adapter.TemplateDisplayAdapter;
import com.gtnexus.pmm.app.platform.module.model.template.TemplateMediaV110;

public class TemplatesRenderer extends BaseSheetRenderer<Collection<TemplateMediaV110>> {

    private final TemplateDisplayAdapter templateDisplayAdapter;

    private final static String SHEET_NAME = "Templates";
    private final static int MAX_WIDTH = 5;

    public TemplatesRenderer(XSSFWorkbook workBook) {
	super(workBook, SHEET_NAME);
	this.templateDisplayAdapter = new TemplateDisplayAdapter();
    }

    @Override
    public int getMaxWidth() {
	return MAX_WIDTH;
    }

    @Override
    public void renderNonNull(Collection<TemplateMediaV110> source) {
	for (TemplateMediaV110 template : source) {
	    renderTemplate(template);
	    traverser.nextRow();
	}
	autofit();
    }

    private void renderTemplate(TemplateMediaV110 template) {
	renderHeaderRow(templateDisplayAdapter);
	renderValueRows(template, templateDisplayAdapter);
    }
}
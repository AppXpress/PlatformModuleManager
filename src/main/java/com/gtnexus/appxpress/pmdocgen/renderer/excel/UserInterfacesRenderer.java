package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtnexus.appxpress.platform.module.model.platformmodule.CustomUiConfiguration;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;
import com.gtnexus.appxpress.pmdocgen.adapter.CustomUiConfigurationDisplayAdapter;

public class UserInterfacesRenderer extends BaseSheetRenderer<PlatformModuleXml> {

    private final CustomUiConfigurationDisplayAdapter customUiConfigurationDisplayAdapter;

    private final static String SHEET_NAME = "User Interfaces";
    private final static int MAX_WIDTH = 3;

    public UserInterfacesRenderer(XSSFWorkbook wb) {
	super(wb, SHEET_NAME);
	this.customUiConfigurationDisplayAdapter = new CustomUiConfigurationDisplayAdapter();
    }

    @Override
    public int getMaxWidth() {
	return MAX_WIDTH;
    }

    @Override
    public void renderNonNull(PlatformModuleXml mod) {
	if (mod.getCustomUiConfiguration() == null || mod.getCustomUiConfiguration().isEmpty()) {
	    return;
	} else {
	    for (CustomUiConfiguration cui : mod.getCustomUiConfiguration()) {
		renderCustomUiConfiguration(cui);
		traverser.nextRow();
	    }
	    autofit();
	}
    }

    private void renderCustomUiConfiguration(CustomUiConfiguration cui) {
	renderHeaderRow(customUiConfigurationDisplayAdapter);
	renderValueRows(cui, customUiConfigurationDisplayAdapter);
    }
}

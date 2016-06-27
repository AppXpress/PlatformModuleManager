package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.gtnexus.appxpress.platform.module.interpretation.role.security.NormalizedRoleSecurity;
import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;
import com.gtnexus.appxpress.platform.module.model.design.WorkflowFeature;
import com.gtnexus.appxpress.pmdocgen.adapter.AttachmentFeatureDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.CustomObjectDesignV110DisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.DisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.EdgeDescriptorDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.EmbeddedFieldsDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.EventsDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.ExtensionPointDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.IdentificationAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.IntegrationFeatureDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.NavFeatureDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.PdfFeatureDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.ReportingFeatureDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.RoleSecurityDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.RuntimeSettingsAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.ScalarFieldDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.ScriptingFeatureDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.WorkflowFeatureDisplayAdapter;

public class CustomObjectDesignDocRenderer extends BaseSheetRenderer<CustomObjectDesignV110>{
	
	private final CustomObjectDesignV110DisplayAdapter identificationDisplayAdapter;
	private final CustomObjectDesignV110DisplayAdapter runtimeSettingsDisplayAdapter;
	private final ScalarFieldDisplayAdapter scalarFieldDisplayAdapter;
	private final EmbeddedFieldsDisplayAdapter embeddedFieldsDisplayAdapter;
	private final EventsDisplayAdapter eventsDisplayAdapter;
	private final RoleSecurityDisplayAdapter roleSecurityDisplayAdapter;
	private final NavFeatureDisplayAdapter navFeatureDisplayAdapter;
	private final ScriptingFeatureDisplayAdapter scriptingFeatureDisplayAdapter;
	private final AttachmentFeatureDisplayAdapter attachmentFeatureDisplayAdapter;
	private final PdfFeatureDisplayAdapter pdfFeatureDisplayAdapter;
	private final ReportingFeatureDisplayAdapter reportingFeatureDisplayAdapter;
	private final IntegrationFeatureDisplayAdapter integrationFeatureDisplayAdapter;
	private final WorkflowFeatureDisplayAdapter workflowFeatureDisplayAdapter;
	private final ExtensionPointDisplayAdapter extensionPointDisplayAdapter;
	
	private final Map<String, String> truncatedNames;
	
	private static final String SHEET_NAME = "Custom Object Design";
	private static final String PRIMARY = "PRIMARY";
	
	public CustomObjectDesignDocRenderer(XSSFWorkbook workBook, Map<String, String> namesWithoutPrefix) {
		super(workBook, SHEET_NAME);
		this.identificationDisplayAdapter = new IdentificationAdapter();
		this.runtimeSettingsDisplayAdapter = new RuntimeSettingsAdapter();
		this.scalarFieldDisplayAdapter = new ScalarFieldDisplayAdapter();
		this.embeddedFieldsDisplayAdapter = new EmbeddedFieldsDisplayAdapter();
		this.eventsDisplayAdapter = new EventsDisplayAdapter();
		this.roleSecurityDisplayAdapter = new RoleSecurityDisplayAdapter();
		this.navFeatureDisplayAdapter = new NavFeatureDisplayAdapter();
		this.scriptingFeatureDisplayAdapter = new ScriptingFeatureDisplayAdapter();
		this.attachmentFeatureDisplayAdapter = new AttachmentFeatureDisplayAdapter();
		this.pdfFeatureDisplayAdapter = new PdfFeatureDisplayAdapter();
		this.reportingFeatureDisplayAdapter = new ReportingFeatureDisplayAdapter();
		this.integrationFeatureDisplayAdapter = new IntegrationFeatureDisplayAdapter();
		this.workflowFeatureDisplayAdapter = new WorkflowFeatureDisplayAdapter();
		this.extensionPointDisplayAdapter = new ExtensionPointDisplayAdapter();
		this.truncatedNames = namesWithoutPrefix;
	}
	
	@SuppressWarnings("unchecked")
	public CustomObjectDesignDocRenderer(XSSFWorkbook workBook) {
		this(workBook, Collections.EMPTY_MAP);
	}

	@Override
	public void render(CustomObjectDesignV110 design) {
		renderSheetLevelDetails(design);
		renderIdentification(design);
		renderRuntimeSettings(design);
		renderScalarFields(design);
		renderEmbeddedFields(design);
		
		if (PRIMARY.equals(design.getDesignType())) {
			renderEvents(design);
			renderRolePermissions(design);
			renderNavigation(design);
			renderScripting(design);
			renderAttachment(design);
			renderPDFFeature(design);
			renderReporting(design);
			renderIntegration(design);
			renderWorkflow(design);
			renderExtensionPoints(design);
		}
		autofit();
	}
	
	@Override
	public int getMaxWidth() {
		return maximumWidth(allAdapters());
	}
	
	private Integer maximumWidth(Collection<DisplayAdapter<?>> adapters) {
		return Collections.max(Collections2.transform(adapters, new Function<DisplayAdapter<?>, Integer>() {
			@Override
			public Integer apply(DisplayAdapter<?> input) {
				return input.size();
			}
		}));
	}
	
	private Collection<DisplayAdapter<?>> allAdapters() {
		return Arrays.asList(new DisplayAdapter[]{
				identificationDisplayAdapter,
				runtimeSettingsDisplayAdapter,
				scalarFieldDisplayAdapter,
				embeddedFieldsDisplayAdapter,
				eventsDisplayAdapter,
				roleSecurityDisplayAdapter,
				navFeatureDisplayAdapter,
				scriptingFeatureDisplayAdapter,
				attachmentFeatureDisplayAdapter,
				pdfFeatureDisplayAdapter,
				reportingFeatureDisplayAdapter,
				integrationFeatureDisplayAdapter,
				workflowFeatureDisplayAdapter,
				extensionPointDisplayAdapter,
		});
	}
	
	
	private void renderSheetLevelDetails(CustomObjectDesignV110 source) {
		workBook.setSheetName(workBook.getSheetIndex(sheet), getName(source));
		short color = getTabColor(source);
		sheet.setTabColor(color);
	}
	
	private String getName(CustomObjectDesignV110 design) {
		if(truncatedNames.isEmpty()) {
			return design.getName();
		}
		return truncatedNames.get(design.getName());
	}
	
	private short getTabColor(CustomObjectDesignV110 source) {
		if("PRIMARY".equals(source.getDesignType())) {
			return IndexedColors.DARK_BLUE.getIndex();
		} 
		return IndexedColors.LAVENDER.getIndex();
	}
	
	private void renderIdentification(CustomObjectDesignV110 design) {
		final int fieldCount = 3;
		renderLabelValueSectionHeader("Identification", fieldCount);
		renderLableValueSection(design, identificationDisplayAdapter, fieldCount);
	}
	
	private void renderRuntimeSettings(CustomObjectDesignV110 design) {
		traverser.nextRow();
		final int fieldCount = 2;
		renderLabelValueSectionHeader("Runtime Settings", fieldCount);
		renderLableValueSection(design, runtimeSettingsDisplayAdapter, fieldCount);
	}
	
	private void renderScalarFields(CustomObjectDesignV110 design) {
		traverser.nextRow();
		renderSectionHeader("Scalar Fields", scalarFieldDisplayAdapter);
		renderTableHeader(scalarFieldDisplayAdapter);
		renderTableBody(design.getScalarField(), scalarFieldDisplayAdapter);
	}
	
	private void renderEmbeddedFields(CustomObjectDesignV110 design) {
		traverser.nextRow();
		renderSectionHeader("Embedded Fields", embeddedFieldsDisplayAdapter);
		renderTableHeader(embeddedFieldsDisplayAdapter);
		renderTableBody(design.getLinkField(), embeddedFieldsDisplayAdapter);
	}
	
	private void renderEvents(CustomObjectDesignV110 design) {
		if (design.getEventField() == null || design.getEventField().isEmpty()) {
			return;
		}
		traverser.nextRow();
		renderSectionHeader("Events", eventsDisplayAdapter);
		renderTableHeader(eventsDisplayAdapter);
		renderTableBody(design.getEventField(), eventsDisplayAdapter);
	}
	
	private void renderRolePermissions(CustomObjectDesignV110 design) {
		if (design.getRoleSecurity() == null || design.getRoleSecurity().isEmpty()) {
			return;
		}
		List<NormalizedRoleSecurity> normalizedRoleSecurities = NormalizedRoleSecurity.normalizeRoleSecurities(design.getRoleSecurity());
		traverser.nextRow();
		renderSectionHeader("Role Permissions", roleSecurityDisplayAdapter);
		renderTableHeader(roleSecurityDisplayAdapter);
		renderTableBody(normalizedRoleSecurities, roleSecurityDisplayAdapter);
	}
	
	private void renderNavigation(CustomObjectDesignV110 design) {
		traverser.nextRow();
		renderLabelValueSectionHeader("Navigation Feature", navFeatureDisplayAdapter);
		renderLableValueSection(design.getNavFeature(), navFeatureDisplayAdapter);
	}
	
	private void renderScripting(CustomObjectDesignV110 design) {
		traverser.nextRow();
		renderLabelValueSectionHeader("Scripting", scriptingFeatureDisplayAdapter);
		renderLableValueSection(design.getScriptingFeature(), scriptingFeatureDisplayAdapter);
	}
	
	private void renderAttachment(CustomObjectDesignV110 design) {
		traverser.nextRow();
		renderLabelValueSectionHeader("Attachment", attachmentFeatureDisplayAdapter);
		renderLableValueSection(design.getAttachmentFeature(), attachmentFeatureDisplayAdapter);
	}

	private void renderPDFFeature(CustomObjectDesignV110 design) {
		traverser.nextRow();
		renderLabelValueSectionHeader("PDF Feature", pdfFeatureDisplayAdapter);
		renderLableValueSection(design.getPdfFeature(), pdfFeatureDisplayAdapter);
	}
	
	private void renderReporting(CustomObjectDesignV110 design) {
		traverser.nextRow();
		renderLabelValueSectionHeader("Reporting", reportingFeatureDisplayAdapter);
		renderLableValueSection(design.getReportingFeature(), reportingFeatureDisplayAdapter);
	}
	
	private void renderIntegration(CustomObjectDesignV110 design) {
		traverser.nextRow();
		renderLabelValueSectionHeader("Integration", integrationFeatureDisplayAdapter);
		renderLableValueSection(design.getIntegrationFeature(), integrationFeatureDisplayAdapter);
	}

	private void renderWorkflow(CustomObjectDesignV110 design) {
		traverser.nextRow();
		WorkflowFeature wff = design.getWorkflowFeature();
		if(wff == null) {
			return;
		}
		renderLabelValueSectionHeader("Workflow Design", 3);
		renderLableValueSection(wff, workflowFeatureDisplayAdapter, 3);
		List<EdgeDescriptorDisplayAdapter> edgeDesc = EdgeDescriptorDisplayAdapter.createDescriptors(wff.getWorkflow());
		DisplayAdapter<EdgeDescriptorDisplayAdapter> adapter = edgeDesc.get(0);
		renderSectionHeader("Workflow Steps", adapter);
		renderTableHeader(adapter); //TODO: null check the descs
		renderTableBody(edgeDesc, adapter);
	}
	
	private void renderExtensionPoints(CustomObjectDesignV110 design) {
		if (design.getCodExtensionPoint() == null || design.getCodExtensionPoint().isEmpty()) {
			return;
		}
		traverser.nextRow();
		renderSectionHeader("Extension Points", extensionPointDisplayAdapter);
		renderTableHeader(extensionPointDisplayAdapter);
		renderTableBody(design.getCodExtensionPoint(), extensionPointDisplayAdapter);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------
	private <X> void renderLabelValueSectionHeader(String sectionName, DisplayAdapter<X> adapter) {
		int cellWidth = (adapter.size() * 2) - 1;
		renderSectionHeader(sectionName, cellWidth);
	}
	
	private void renderLabelValueSectionHeader(String sectionName, int fieldAndLabelPairCount) {
		int cellWidth = (fieldAndLabelPairCount * 2) - 1;
		renderSectionHeader(sectionName, cellWidth);
	}
	
	
	private <X> void renderSectionHeader(String sectionName, DisplayAdapter<X> adapter) {
		renderSectionHeader(sectionName, adapter.size() - 1);
	}
	
	/**
	 * 
	 * @param sectionName
	 * @param cellWidth how many cells wide this section is.
	 */
	private void renderSectionHeader(String sectioName, int cellWidth) {
		XSSFRow idHeaderRow = traverser.nextRow();
		XSSFCell idHeaderCell = traverser.nextCell();
		idHeaderCell.setCellValue(sectioName);
		idHeaderCell.setCellStyle(styleProvider.getHeaderStyle());
		CellRangeAddress headerRegion = new CellRangeAddress(idHeaderRow.getRowNum(), idHeaderRow.getRowNum(), 0, cellWidth);
		sheet.addMergedRegion(headerRegion);
	}
	
	private <X> void renderLableValueSection(X target, DisplayAdapter<X> adapter) {
		renderLableValueSection(target, adapter, adapter.size());
	}
	
	private <X> void renderLableValueSection(X target, DisplayAdapter<X> adapter, int width) {
		Iterator<String> iterator = adapter.iterator();
		while (iterator.hasNext()) {
			traverser.nextRow();
			renderLabelValueRow(target, adapter, width, iterator);
		}
	}
	
	private <X> void renderLabelValueRow(X target, DisplayAdapter<X> adapter, int width,
			Iterator<String> iterator) {
		for (int i = 0; i < width; i++) {
			if (!iterator.hasNext()) {
				XSSFCell cell = traverser.nextCell();
				cell.setCellStyle(styleProvider.getSecondaryHeaderStyle());
				traverser.nextCell();
				continue;
			}
			String key = iterator.next();
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(key);
			cell.setCellStyle(styleProvider.getSecondaryHeaderStyle());
			cell = traverser.nextCell();
			cell.setCellValue(adapter.display(target, key));
		}
	}
	
	private void renderTableHeader(Iterable<String> labels) {
		traverser.nextRow();
		for(String label : labels) {
			XSSFCell nextCell = traverser.nextCell();
			nextCell.setCellValue(label);
			nextCell.setCellStyle(styleProvider.getSecondaryHeaderStyle());
		}
	}
	
	private <X> void renderTableBody(Collection<X> entries, DisplayAdapter<X> adapter) {
		for(X entry : entries) {
			traverser.nextRow();
			for(String k : adapter) {
				XSSFCell cell = traverser.nextCell();
				cell.setCellValue(adapter.display(entry, k));
			}
		}
	}

}

package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.platformmodule.NotificationConfiguration;
import com.gtnexus.appxpress.platform.module.model.platformmodule.NotificationTopic;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;
import com.gtnexus.appxpress.pmdocgen.adapter.NotificationConfigurationDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.NotificationTopicDisplayAdapter;

public class TopicsRenderer extends BaseSheetRenderer<PlatformModuleXml> {
	
	private final static Map<String, Function<NotificationTopic, String>> PARENT_TABLE_COL_LABELS = 
			new ImmutableMap.Builder<String, Function<NotificationTopic, String>>()
			.put("Topic Name", NotificationTopicDisplayAdapter.TOPIC_NAME_FN)
			.build();
	
	private final static Map<String, Function<NotificationConfiguration, String>> CHILD_TABLE_COL_LABELS = 
			new ImmutableMap.Builder<String, Function<NotificationConfiguration, String>>()
			.put("Delivery Method", NotificationConfigurationDisplayAdapter.NOTIFICATION_DELIVERY_METHOD_FN)
			.put("Template", NotificationConfigurationDisplayAdapter.TEMPLATE_FN)
			.put("Locale", NotificationConfigurationDisplayAdapter.LOCALE_FN)
			.put("Recipient Role", NotificationConfigurationDisplayAdapter.ROLE_FN)
			.put("Data Function", NotificationConfigurationDisplayAdapter.DATA_FUNCTION_FN)
			.put("API Version", NotificationConfigurationDisplayAdapter.API_VERSION_FN)
			.put("Target Type", NotificationConfigurationDisplayAdapter.TARGET_TYPE_FN)
			.build();
	
	private final static String SHEET_NAME = "Topics";
	private final static int MAX_WIDTH = CHILD_TABLE_COL_LABELS.size() - 1; 
	
	public TopicsRenderer(XSSFWorkbook wb) {
		super(wb,SHEET_NAME, MAX_WIDTH);
	}
	
	@Override
	public void render(PlatformModuleXml mod) {
		for(NotificationTopic nt : mod.getNotificationTopic()) {
			renderParentHeader();
			renderNotificationTopic(nt);
			renderChildHeader();
			renderNotificationConfiguration(nt.getNotificationConfiguration());
		}
		autofit();
	}
	
	private void renderParentHeader() {
		traverser.nextRow();
		for(String k : PARENT_TABLE_COL_LABELS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(k);
			cell.setCellStyle(styleProvider.getHeaderStyle());
		}
	}
	
	private void renderNotificationTopic(NotificationTopic nt) {
		traverser.nextRow();
		for(String key : PARENT_TABLE_COL_LABELS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(PARENT_TABLE_COL_LABELS.get(key).apply(nt));
		}
	}
	
	private void renderChildHeader() {
		traverser.nextRow();
		for(String k : CHILD_TABLE_COL_LABELS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(k);
			cell.setCellStyle(styleProvider.getHeaderStyle());
		}
	}
	
	private void renderNotificationConfiguration(NotificationConfiguration nc) {
		traverser.nextRow();
		for(String key : CHILD_TABLE_COL_LABELS.keySet()) {
			XSSFCell cell = traverser.nextCell();
			cell.setCellValue(CHILD_TABLE_COL_LABELS.get(key).apply(nc));
		}
		traverser.nextRow();
	}
}

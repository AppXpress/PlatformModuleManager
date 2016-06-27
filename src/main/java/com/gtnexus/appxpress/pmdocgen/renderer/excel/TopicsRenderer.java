package com.gtnexus.appxpress.pmdocgen.renderer.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.gtnexus.appxpress.platform.module.model.platformmodule.NotificationConfiguration;
import com.gtnexus.appxpress.platform.module.model.platformmodule.NotificationTopic;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;
import com.gtnexus.appxpress.pmdocgen.adapter.NotificationConfigurationDisplayAdapter;
import com.gtnexus.appxpress.pmdocgen.adapter.NotificationTopicDisplayAdapter;

public class TopicsRenderer extends BaseSheetRenderer<PlatformModuleXml> {
	
	private final NotificationTopicDisplayAdapter notificationTopicDisplayAdapter;
	private final NotificationConfigurationDisplayAdapter notificationConfigurationDisplayAdapter;
	
	private final static String SHEET_NAME = "Topics";
	private final static int MAX_WIDTH = 8; 
	
	public TopicsRenderer(XSSFWorkbook wb) {
		super(wb,SHEET_NAME, MAX_WIDTH);
		this.notificationTopicDisplayAdapter = new NotificationTopicDisplayAdapter();
		this.notificationConfigurationDisplayAdapter = new NotificationConfigurationDisplayAdapter();
	}
	
	@Override
	public void renderNonNull(PlatformModuleXml mod) {
		if (mod.getNotificationTopic() == null || mod.getNotificationTopic().isEmpty()) {
			return;
		} else {
			for(NotificationTopic nt : mod.getNotificationTopic()) {
				renderNotificationTopic(nt);
				renderNotificationConfiguration(nt.getNotificationConfiguration());
				traverser.nextRow();
			}
			autofit();
		}
	}
	
	private void renderNotificationTopic(NotificationTopic nt) {
		renderLabelValueSectionHeader(notificationTopicDisplayAdapter);
		renderLabelValueSection(nt, notificationTopicDisplayAdapter);
	}
	
	private void renderNotificationConfiguration(NotificationConfiguration nc) {
		renderLabelValueSectionHeader(notificationConfigurationDisplayAdapter);
		renderLabelValueSection(nc, notificationConfigurationDisplayAdapter);
	}
}

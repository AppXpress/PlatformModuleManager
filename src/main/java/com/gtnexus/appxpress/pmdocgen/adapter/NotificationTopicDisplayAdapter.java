package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.platformmodule.NotificationTopic;

public class NotificationTopicDisplayAdapter extends DisplayAdapter<NotificationTopic>{

	private static final Function<NotificationTopic, String> TOPIC_NAME_FN = new PrimitiveDisplayFunction<NotificationTopic>() {
		@Override
		public String applyToNonNull(NotificationTopic nt) {
			return nt.getTopicName();
		}
	};

	private static final Map<String, Function<NotificationTopic, String>> adapterMap = new ImmutableMap.Builder<String, Function<NotificationTopic, String>>()
			.put("Topic Name", TOPIC_NAME_FN)
			.build();
	
	public NotificationTopicDisplayAdapter() {
		super(adapterMap);
	}
}
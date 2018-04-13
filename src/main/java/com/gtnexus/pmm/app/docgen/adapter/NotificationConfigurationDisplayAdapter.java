package com.gtnexus.pmm.app.docgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.app.platform.module.model.platformmodule.NotificationConfiguration;

public class NotificationConfigurationDisplayAdapter extends DisplayAdapter<NotificationConfiguration> {

    private static final Function<NotificationConfiguration, String> TARGET_TYPE_FN = new PrimitiveDisplayFunction<NotificationConfiguration>() {
	@Override
	public String applyToNonNull(NotificationConfiguration nc) {
	    return nc.getTargetType();
	}
    };

    // TODO
    private static final Function<NotificationConfiguration, String> TEMPLATE_FN = new PrimitiveDisplayFunction<NotificationConfiguration>() {
	@Override
	public String applyToNonNull(NotificationConfiguration nc) {
	    return null;
	}
    };

    // TODO
    private static final Function<NotificationConfiguration, String> LOCALE_FN = new PrimitiveDisplayFunction<NotificationConfiguration>() {
	@Override
	public String applyToNonNull(NotificationConfiguration nc) {
	    return null;
	}
    };

    // TODO
    private static final Function<NotificationConfiguration, String> DATA_FUNCTION_FN = new PrimitiveDisplayFunction<NotificationConfiguration>() {
	@Override
	public String applyToNonNull(NotificationConfiguration nc) {
	    return null;
	}
    };

    private static final Function<NotificationConfiguration, String> API_VERSION_FN = new PrimitiveDisplayFunction<NotificationConfiguration>() {
	@Override
	public String applyToNonNull(NotificationConfiguration nc) {
	    return nc.getApiVersion() == null ? "" : nc.getApiVersion().toString();
	}
    };

    private static final Function<NotificationConfiguration, String> NOTIFICATION_DELIVERY_METHOD_FN = new PrimitiveDisplayFunction<NotificationConfiguration>() {
	@Override
	public String applyToNonNull(NotificationConfiguration nc) {
	    return nc.getNotificationDeliveryMethod();
	}
    };

    private static final Function<NotificationConfiguration, String> SUPPRESSIBLE_FN = new BooleanDisplayFunction<NotificationConfiguration>() {
	@Override
	public String applyToNonNull(NotificationConfiguration nc) {
	    return Boolean.toString(nc.isSuppressible());
	}
    };

    private static final Function<NotificationConfiguration, String> ROLE_FN = new PrimitiveDisplayFunction<NotificationConfiguration>() {
	@Override
	public String applyToNonNull(NotificationConfiguration nc) {
	    return nc.getRole();
	}
    };

    private static final Map<String, Function<NotificationConfiguration, String>> adapterMap = new ImmutableMap.Builder<String, Function<NotificationConfiguration, String>>()
	    .put("Delivery Method", NOTIFICATION_DELIVERY_METHOD_FN).put("Template", TEMPLATE_FN)
	    .put("Locale", LOCALE_FN).put("Recipient Role", ROLE_FN).put("Data Function", DATA_FUNCTION_FN)
	    .put("API Version", API_VERSION_FN).put("Target Type", TARGET_TYPE_FN).put("Suppressible", SUPPRESSIBLE_FN)
	    .build();

    public NotificationConfigurationDisplayAdapter() {
	super(adapterMap);
    }
}
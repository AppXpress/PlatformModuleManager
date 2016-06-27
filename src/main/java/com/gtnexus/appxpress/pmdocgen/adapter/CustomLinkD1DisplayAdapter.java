package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.customlink.CustomLinkD1;

public class CustomLinkD1DisplayAdapter extends DisplayAdapter<CustomLinkD1>{

	private static final Function<CustomLinkD1, String> LINK_ID_FN = new PrimitiveDisplayFunction<CustomLinkD1>() {
		@Override
		public String applyToNonNull(CustomLinkD1 cl) {
			return cl.getUid() == null ? "" : cl.getUid().toString();
		}
	};

	private static final Function<CustomLinkD1, String> NAME_FN = new PrimitiveDisplayFunction<CustomLinkD1>() {
		@Override
		public String applyToNonNull(CustomLinkD1 cl) {
			return cl.getName();
		}
	};
	
	private static final Function<CustomLinkD1, String> STATUS_FN = new PrimitiveDisplayFunction<CustomLinkD1>() {
		@Override
		public String applyToNonNull(CustomLinkD1 cl) {
			return cl.getStatus();
		}
	};
	
	private static final Function<CustomLinkD1, String> LOCALIZATION_KEY_FN = new PrimitiveDisplayFunction<CustomLinkD1>() {
		@Override
		public String applyToNonNull(CustomLinkD1 cl) {
			return cl.getLocalizationKey();
		}
	};
	
	private static final Function<CustomLinkD1, String> LINK_TYPE_FN = new PrimitiveDisplayFunction<CustomLinkD1>() {
		@Override
		public String applyToNonNull(CustomLinkD1 cl) {
			return cl.getLinkType();
		}
	};
	
	private static final Function<CustomLinkD1, String> DISPLAY_LOCATION_FN = new PrimitiveDisplayFunction<CustomLinkD1>() {
		@Override
		public String applyToNonNull(CustomLinkD1 cl) {
			return cl.getLocation();
		}
	};
	
	private static final Map<String, Function<CustomLinkD1, String>> adapterMap = new ImmutableMap.Builder<String, Function<CustomLinkD1, String>>()
			.put("Link ID", LINK_ID_FN)
			.put("Name", NAME_FN)
			.put("Status", STATUS_FN)
			.put("Localization Key", LOCALIZATION_KEY_FN)
			.put("Link Type", LINK_TYPE_FN)
			.put("Display Location", DISPLAY_LOCATION_FN)
			.build();
	
	public CustomLinkD1DisplayAdapter() {
		super(adapterMap);
	}
}


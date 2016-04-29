package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.design.NavFeature;

public class NavFeatureDisplayAdapter extends DisplayAdapter<NavFeature>{

	private static final  Function<NavFeature, String> IS_ENABLED_FN = new Function<NavFeature, String>() {
		@Override
		public String apply(NavFeature nav) {
			return Boolean.toString(nav.isEnabled());
		}
	};
	
	private static final Function<NavFeature, String> NAV_HEADING_FN = new Function<NavFeature, String>() {
		@Override
		public String apply(NavFeature nav) {
			return nav.getNavHeading();
		}
	};
	
	private static final Map<String, Function<NavFeature, String>> adapterMap = new ImmutableMap.Builder<String, Function<NavFeature, String>>()
			.put("Enabled", IS_ENABLED_FN)
			.put("Navigation Heading", NAV_HEADING_FN)
			.build();
	
	
	public NavFeatureDisplayAdapter() {
		super(adapterMap);
	}

}

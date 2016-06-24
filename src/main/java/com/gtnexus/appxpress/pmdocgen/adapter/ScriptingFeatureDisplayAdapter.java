package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.design.ScriptingFeature;

public class ScriptingFeatureDisplayAdapter extends DisplayAdapter<ScriptingFeature>{

	private static final  Function<ScriptingFeature, String> IS_ENABLED_FN = new BooleanDisplayFunction<ScriptingFeature>() {
		@Override
		public String applyToNonNull(ScriptingFeature sf) {
			return Boolean.toString(sf.isEnabled());
		}
	};
	
	private static final Function<ScriptingFeature, String> FILE_NAME_FN = new PrimitiveDisplayFunction<ScriptingFeature>() {
		@Override
		public String applyToNonNull(ScriptingFeature sf) {
			return sf.getFileName();
		}
	};
	
	private static final Map<String, Function<ScriptingFeature, String>> adapterMap = new ImmutableMap.Builder<String, Function<ScriptingFeature, String>>()
			.put("Filename", FILE_NAME_FN)
			.put("Enabled", IS_ENABLED_FN)
			.build();
	
	
	public ScriptingFeatureDisplayAdapter() {
		super(adapterMap);
	}

}

package com.gtnexus.pmm.docgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.platform.module.model.platformmodule.CustomUiConfiguration;

public class CustomUiConfigurationDisplayAdapter extends DisplayAdapter<CustomUiConfiguration> {

    private static final Function<CustomUiConfiguration, String> GLOBAL_OBJECT_TYPE_FN = new PrimitiveDisplayFunction<CustomUiConfiguration>() {
	@Override
	public String applyToNonNull(CustomUiConfiguration cui) {
	    return cui.getGlobalObjectType();
	}
    };

    private static final Function<CustomUiConfiguration, String> RESOURCE_DIRECTORY_FN = new PrimitiveDisplayFunction<CustomUiConfiguration>() {
	@Override
	public String applyToNonNull(CustomUiConfiguration cui) {
	    return cui.getDirectoryName();
	}
    };

    private static final Function<CustomUiConfiguration, String> UI_TYPE_FN = new PrimitiveDisplayFunction<CustomUiConfiguration>() {
	@Override
	public String applyToNonNull(CustomUiConfiguration cui) {
	    return cui.getUiType();
	}
    };

    private static final Map<String, Function<CustomUiConfiguration, String>> adapterMap = new ImmutableMap.Builder<String, Function<CustomUiConfiguration, String>>()
	    .put("Global Object Type", GLOBAL_OBJECT_TYPE_FN).put("Resource Bundle Directory", RESOURCE_DIRECTORY_FN)
	    .put("User Interface", UI_TYPE_FN).build();

    public CustomUiConfigurationDisplayAdapter() {
	super(adapterMap);
    }
}

package com.gtnexus.pmm.app.docgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.app.platform.module.model.platformmodule.Metadata;
import com.gtnexus.pmm.app.platform.module.model.platformmodule.PlatformModuleXml;

public class PlatformModuleXmlDisplayAdapter extends DisplayAdapter<PlatformModuleXml> {

    public static final Function<PlatformModuleXml, String> NAME_FN = new PrimitiveDisplayFunction<PlatformModuleXml>() {
	@Override
	public String applyToNonNull(PlatformModuleXml mod) {
	    return mod.getName();
	}
    };

    public static final Function<PlatformModuleXml, String> API_VERSION_FN = new PrimitiveDisplayFunction<PlatformModuleXml>() {
	@Override
	public String applyToNonNull(PlatformModuleXml mod) {
	    Metadata metadata = mod.getMetadata();
	    if (metadata != null && metadata.getApiVersion() != null) {
		return metadata.getApiVersion().toString();
	    }
	    return null;
	}
    };

    public static final Function<PlatformModuleXml, String> DESCRIPTION_FN = new PrimitiveDisplayFunction<PlatformModuleXml>() {
	@Override
	public String applyToNonNull(PlatformModuleXml mod) {
	    return mod.getDescription();
	}
    };

    private static final Map<String, Function<PlatformModuleXml, String>> adapterMap = new ImmutableMap.Builder<String, Function<PlatformModuleXml, String>>()
	    .put("Module Name", NAME_FN).put("API Version", API_VERSION_FN).put("Description", DESCRIPTION_FN).build();

    protected PlatformModuleXmlDisplayAdapter() {
	super(adapterMap);
    }
}

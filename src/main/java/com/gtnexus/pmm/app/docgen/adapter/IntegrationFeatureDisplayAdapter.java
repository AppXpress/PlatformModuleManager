package com.gtnexus.pmm.app.docgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.app.platform.module.model.design.IntegrationFeature;

public class IntegrationFeatureDisplayAdapter extends DisplayAdapter<IntegrationFeature> {

    private static final Function<IntegrationFeature, String> IS_ENABLED_FN = new BooleanDisplayFunction<IntegrationFeature>() {
	@Override
	public String applyToNonNull(IntegrationFeature intf) {
	    return Boolean.toString(intf.isEnabled());
	}
    };

    private static final Function<IntegrationFeature, String> DATA_DOWNLOAD_FN = new BooleanDisplayFunction<IntegrationFeature>() {
	@Override
	public String applyToNonNull(IntegrationFeature intf) {
	    return Boolean.toString(intf.isDownloadRuntimeEnabled());
	}
    };

    private static final Function<IntegrationFeature, String> DATA_UPLOAD_FN = new BooleanDisplayFunction<IntegrationFeature>() {
	@Override
	public String applyToNonNull(IntegrationFeature intf) {
	    return Boolean.toString(intf.isUploadRuntimeEnabled());
	}
    };

    private static final Map<String, Function<IntegrationFeature, String>> adapterMap = new ImmutableMap.Builder<String, Function<IntegrationFeature, String>>()
	    .put("Enabled", IS_ENABLED_FN).put("Data Download", DATA_DOWNLOAD_FN).put("Data Upload", DATA_UPLOAD_FN)
	    .build();

    public IntegrationFeatureDisplayAdapter() {
	super(adapterMap);
    }

}
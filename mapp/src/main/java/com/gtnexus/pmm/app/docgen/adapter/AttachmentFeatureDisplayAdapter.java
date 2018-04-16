package com.gtnexus.pmm.app.docgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.app.platform.module.model.design.AttachmentFeature;

public class AttachmentFeatureDisplayAdapter extends DisplayAdapter<AttachmentFeature> {
    private static final Function<AttachmentFeature, String> IS_ENABLED_FN = new BooleanDisplayFunction<AttachmentFeature>() {
	@Override
	public String applyToNonNull(AttachmentFeature af) {
	    return Boolean.toString(af.isEnabled());
	}
    };

    private static final Map<String, Function<AttachmentFeature, String>> adapterMap = new ImmutableMap.Builder<String, Function<AttachmentFeature, String>>()
	    .put("Enabled", IS_ENABLED_FN).build();

    public AttachmentFeatureDisplayAdapter() {
	super(adapterMap);
    }

}

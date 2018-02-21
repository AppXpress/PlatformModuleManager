package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.design.VersioningFeature;

public class VersioningFeatureDisplayAdapter extends DisplayAdapter<VersioningFeature> {

    private static final Function<VersioningFeature, String> IS_ENABLED_FN = new BooleanDisplayFunction<VersioningFeature>() {
	@Override
	public String applyToNonNull(VersioningFeature vf) {
	    return Boolean.toString(vf.isEnabled());
	}
    };

    private static final Function<VersioningFeature, String> DRAFT_COMPLETION_STATE_FN = new PrimitiveDisplayFunction<VersioningFeature>() {
	@Override
	public String applyToNonNull(VersioningFeature vf) {
	    return vf.getDraftCompletionState();
	}
    };

    private static final Function<VersioningFeature, String> ACTIVE_COMPLETION_STATE_FN = new PrimitiveDisplayFunction<VersioningFeature>() {
	@Override
	public String applyToNonNull(VersioningFeature vf) {
	    return vf.getActiveCompletionState();
	}
    };

    private static final Function<VersioningFeature, String> TRANSITION_FOR_NEW_DRAFT_FN = new PrimitiveDisplayFunction<VersioningFeature>() {
	@Override
	public String applyToNonNull(VersioningFeature vf) {
	    return vf.getTransitionForNewDraft();
	}
    };

    private static final Function<VersioningFeature, String> VERSION_FIELD_FN = new PrimitiveDisplayFunction<VersioningFeature>() {
	@Override
	public String applyToNonNull(VersioningFeature vf) {
	    return vf.getVersionField();
	}
    };

    private static final Map<String, Function<VersioningFeature, String>> adapterMap = new ImmutableMap.Builder<String, Function<VersioningFeature, String>>()
	    .put("Enabled", IS_ENABLED_FN).put("Draft Version Completion State", DRAFT_COMPLETION_STATE_FN)
	    .put("Active Version Completion State", ACTIVE_COMPLETION_STATE_FN)
	    .put("Transition for New Draft", TRANSITION_FOR_NEW_DRAFT_FN).put("Version Field", VERSION_FIELD_FN)
	    .build();

    public VersioningFeatureDisplayAdapter() {
	super(adapterMap);
    }
}

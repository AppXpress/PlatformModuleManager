package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.design.WorkflowFeature;

public class WorkflowFeatureDisplayAdapter extends WorkflowDisplayAdapter {

    private static final Map<String, Function<WorkflowFeature, String>> adapterMap = new ImmutableMap.Builder<String, Function<WorkflowFeature, String>>()
	    .put("Workflow Enabled", ENABLED_FN).put("Initial State", INITIAL_STATE_FN)
	    .put("Workflow State Field", WORKFLOW_STATE_FIELD_FN).build();

    public WorkflowFeatureDisplayAdapter() {
	super(adapterMap);
    }

}

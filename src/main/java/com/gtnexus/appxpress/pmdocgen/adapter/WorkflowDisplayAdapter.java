package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.gtnexus.appxpress.platform.module.model.design.Workflow;
import com.gtnexus.appxpress.platform.module.model.design.WorkflowFeature;
import com.gtnexus.appxpress.pmdocgen.WorkflowGraph;
import com.gtnexus.appxpress.pmdocgen.WorkflowGraph.Node;

public abstract class WorkflowDisplayAdapter extends DisplayAdapter<WorkflowFeature>{

	protected static final Function<WorkflowFeature, String> ENABLED_FN = new Function<WorkflowFeature, String>() {

		@Override
		public String apply(WorkflowFeature feature) {
			return Boolean.toString(feature.isEnabled());
		}
		
	};
	
	protected static final Function<WorkflowFeature, String> INITIAL_STATE_FN = new Function<WorkflowFeature, String>() {

		@Override
		public String apply(WorkflowFeature feature) {
			Workflow workflow = feature.getWorkflow();
			return workflow == null ? "" : workflow.getInitialStatus();
		}
		
	};
	
	protected static final Function<WorkflowFeature, String> WORKFLOW_STATE_FIELD_FN = new Function<WorkflowFeature, String>() {

		@Override
		public String apply(WorkflowFeature feature) {
			return feature.getBusinessStatusField();
		}
		
	};
	
	protected static final Function<WorkflowGraph.Node, String> FN = new Function<WorkflowGraph.Node, String> () {

		@Override
		public String apply(Node input) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
	protected WorkflowDisplayAdapter(Map<String, Function<WorkflowFeature, String>> adapterMap) {
		super(adapterMap);
	}

}

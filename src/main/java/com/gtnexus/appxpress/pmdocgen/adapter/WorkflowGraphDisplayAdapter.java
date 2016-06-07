package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.gtnexus.appxpress.platform.module.interpretation.workflow.WorkflowGraph;
import com.gtnexus.appxpress.platform.module.interpretation.workflow.WorkflowGraph.Node;

public class WorkflowGraphDisplayAdapter extends DisplayAdapter<WorkflowGraph.Node> {

	protected WorkflowGraphDisplayAdapter(Map<String, Function<Node, String>> adapterMap) {
		super(adapterMap);
		// TODO Auto-generated constructor stub
	}

	
	protected static final Function<WorkflowGraph.Node, String> FN = new Function<WorkflowGraph.Node, String> () {

		@Override
		public String apply(Node n) {
			return null;
		}
		
	};
}

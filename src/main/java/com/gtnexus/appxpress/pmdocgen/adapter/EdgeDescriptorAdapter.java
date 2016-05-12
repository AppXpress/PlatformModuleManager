package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.gtnexus.appxpress.commons.NullSafeStringEntryImmutableMapBuilder;
import com.gtnexus.appxpress.platform.module.interpretation.workflow.WorkflowGraph;
import com.gtnexus.appxpress.platform.module.model.design.Transition;
import com.gtnexus.appxpress.platform.module.model.design.Workflow;

public class EdgeDescriptorAdapter extends DisplayAdapter<EdgeDescriptorAdapter>{
	
	private static final Map<String, String> DUMMY_DESC = new ImmutableMap.Builder<String, String>()
			.put("State", "")
			.put("Edit Role", "")
			.put("Action", "")
			.put("To State", "")
			.put("Action Roles", "")
			.put("Precondition Fn", "")
			.put("Post Transition Fn", "")
			.build();
	
	public static List<EdgeDescriptorAdapter> createDescriptors(Workflow wf) {
		return createDescriptors(WorkflowGraph.constructGraph(wf));
	}

	public static List<EdgeDescriptorAdapter> createDescriptors(WorkflowGraph graph) {
		List<EdgeDescriptorAdapter> descs = Lists.newLinkedList();
		if(graph.isEmpty()) {
			 descs.add(dummyDescriptor());
			 return descs;
		}
		for(WorkflowGraph.Node n : graph.getNodes()) {
			descs.addAll(descriptorsFor(n));
		}
		return descs;
	}

	private static List<EdgeDescriptorAdapter>  descriptorsFor(WorkflowGraph.Node n) {
		List<EdgeDescriptorAdapter> descs = Lists.newLinkedList();
		List<Transition> trans = (n.getTransitions() == null || n.getTransitions().isEmpty()) ? Lists.newArrayList(new Transition()) :  n.getTransitions();
		for(Transition t : trans) {
			Map<String, String> desc = new NullSafeStringEntryImmutableMapBuilder<String>()
					.put("State", n.getState())
					.put("Edit Role", n.getEditRoles())
					.put("Action", t.getAction())
					.put("To State", t.getToState())
					.put("Action Roles", t.getRoles())
					.put("Precondition Fn", t.getPreconditionFn())
					.put("Post Transition Fn", t.getPostTransitionFn())
					.build();
			descs.add(new EdgeDescriptorAdapter(desc));
		}
		return descs;
	}
	
	
	public static EdgeDescriptorAdapter  dummyDescriptor() {
		return new EdgeDescriptorAdapter(DUMMY_DESC);
	}
	
	private final Map<String, String> contents;
	
	private EdgeDescriptorAdapter(Map<String, String> contents) {
		super(null);
		this.contents = contents;
	}
	
	@Override public String display(EdgeDescriptorAdapter target, String key) {
		return target.get(key);
	}
	
	public String get(String key) {
		return this.contents.get(key);
	}
	
	@Override
	public Iterator<String> iterator() {
		return contents.keySet().iterator();
	}
	
	@Override
	public int size() {
		return contents.size();
	}
	
}

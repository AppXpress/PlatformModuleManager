package com.gtnexus.appxpress.platform.module.interpretation.workflow;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.gtnexus.appxpress.commons.NullSafeStringEntryImmutableMapBuilder;
import com.gtnexus.appxpress.platform.module.model.design.Transition;
import com.gtnexus.appxpress.platform.module.model.design.Workflow;
import com.gtnexus.appxpress.pmdocgen.adapter.DisplayAdapter;

public class EdgeDescriptor extends DisplayAdapter<EdgeDescriptor>{
	
	public static List<EdgeDescriptor> createDescriptors(Workflow wf) {
		return createDescriptors(WorkflowGraph.constructGraph(wf));
	}

	public static List<EdgeDescriptor> createDescriptors(WorkflowGraph graph) {
		List<EdgeDescriptor> descs = Lists.newLinkedList();
		for(WorkflowGraph.Node n : graph.getNodes()) {
			for(Transition t : n.getTransitions()) {
				Map<String, String> desc = new NullSafeStringEntryImmutableMapBuilder<String>()
						.put("State", n.getState())
						.put("Edit Role", n.getEditRoles())
						.put("Action", t.getAction())
						.put("To State", t.getToState())
						.put("Action Roles", t.getRoles())
						.put("Precondition Fn", t.getPreconditionFn())
						.put("Post Transition Fn", t.getPostTransitionFn())
						.build();
				descs.add(new EdgeDescriptor(desc));
			}
		}
		return descs;
	}
	
	private final Map<String, String> contents;
	
	private EdgeDescriptor(Map<String, String> contents) {
		super(null);
		this.contents = contents;
	}
	
	@Override public String display(EdgeDescriptor target, String key) {
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

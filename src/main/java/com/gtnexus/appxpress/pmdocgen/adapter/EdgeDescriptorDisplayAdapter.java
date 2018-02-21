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

public class EdgeDescriptorDisplayAdapter extends DisplayAdapter<EdgeDescriptorDisplayAdapter> {

    private static final Map<String, String> DUMMY_DESC = new ImmutableMap.Builder<String, String>().put("State", "")
	    .put("Edit Role", "").put("Action", "").put("To State", "").put("Action Roles", "")
	    .put("Precondition Fn", "").put("Post Transition Fn", "").build();

    public static List<EdgeDescriptorDisplayAdapter> createDescriptors(Workflow wf) {
	return createDescriptors(WorkflowGraph.constructGraph(wf));
    }

    public static List<EdgeDescriptorDisplayAdapter> createDescriptors(WorkflowGraph graph) {
	List<EdgeDescriptorDisplayAdapter> descs = Lists.newLinkedList();
	if (graph.isEmpty()) {
	    descs.add(dummyDescriptor());
	    return descs;
	}
	for (WorkflowGraph.Node n : graph.getNodes()) {
	    descs.addAll(descriptorsFor(n));
	}
	return descs;
    }

    private static List<EdgeDescriptorDisplayAdapter> descriptorsFor(WorkflowGraph.Node n) {
	List<EdgeDescriptorDisplayAdapter> descs = Lists.newLinkedList();
	List<Transition> trans = (n.getTransitions() == null || n.getTransitions().isEmpty())
		? Lists.newArrayList(new Transition())
		: n.getTransitions();
	for (Transition t : trans) {
	    Map<String, String> desc = new NullSafeStringEntryImmutableMapBuilder<String>().put("State", n.getState())
		    .put("Edit Role", n.getEditRoles()).put("Action", t.getAction()).put("To State", t.getToState())
		    .put("Action Roles", t.getRoles()).put("Precondition Fn", t.getPreconditionFn())
		    .put("Post Transition Fn", t.getPostTransitionFn()).build();
	    descs.add(new EdgeDescriptorDisplayAdapter(desc));
	}
	return descs;
    }

    public static EdgeDescriptorDisplayAdapter dummyDescriptor() {
	return new EdgeDescriptorDisplayAdapter(DUMMY_DESC);
    }

    private final Map<String, String> contents;

    private EdgeDescriptorDisplayAdapter(Map<String, String> contents) {
	super(null);
	this.contents = contents;
    }

    @Override
    public String display(EdgeDescriptorDisplayAdapter target, String key) {
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

package com.gtnexus.appxpress.pmdocgen;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtnexus.appxpress.platform.module.model.design.Step;
import com.gtnexus.appxpress.platform.module.model.design.Transition;

public class WorkflowGraph {
	
	public static WorkflowGraph constructGraph(Collection<Step> steps, Collection<Transition> transitions) {
		Map<String, Node> nodeMap = Maps.uniqueIndex(toNodes(steps), new Function<Node, String>() {
			@Override
			public String apply(Node n) {
				return n.getState();
			}
		});
		for(Transition transition: transitions) {
			String fromState = transition.getFromState();
			Node n = nodeMap.get(fromState);
			n.addTransition(transition);
		}
		return new WorkflowGraph(nodeMap.values());
	}
	
	private final Collection<Node> nodes;
	
	public WorkflowGraph(Collection<Node> nodes) {
		this.nodes = nodes;
	}
	
	public Collection<Node> getNodes() {
		return this.nodes;
	}
	
	public void fn(Node n) {
		for(Transition t : n.getTransitions()) {
			n.getState();
			n.getEditRoles();
			t.getAction();
			t.getToState();
			t.getRoles();
			t.getPreconditionFn();
//			t.getValidtionFn();
			t.getPostTransitionFn();
//			t.getOnStateFn();
		}
	}
	
	private static List<Node> toNodes(Collection<Step> steps) {
		List<Node> nodes = Lists.newLinkedList();
		for(Step step : steps) {
			nodes.add(new Node(step));
		}
		return nodes;
	}
	
	public static class Node {
		private  Step step;
		private List<Transition> transitions;
		
		public Node(Step step) {
			this(step, new LinkedList<Transition>());
		}
		
		public String getEditRoles() {
			return step.getEditRoles();
		}
		
		public Node(Step step, List<Transition> transitions) {
			this.step = step;
			this.transitions = transitions;
		}
		
		public String getState() {
			return step.getState();
		}
		
		public void addTransition(Transition t) {
			this.transitions.add(t);
		}
		
		public List<Transition> getTransitions() {
			return this.transitions;
		}
		
	}

}

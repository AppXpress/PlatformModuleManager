package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.design.Step;

public class WorkflowStepDisplayAdapter extends DisplayAdapter<Step>{
	
	protected static final Function<Step, String> EXTENSION_PT_FN = new PrimitiveDisplayFunction<Step>() {

		@Override
		public String applyToNonNull(Step step) {
			return step.getState();
		}
		
	};
	
	protected static final Function<Step, String> EDIT_ROLES_FN = new PrimitiveDisplayFunction<Step>() {

		@Override
		public String applyToNonNull(Step step) {
			return step.getEditRoles();
		}
		
	};
	
	protected static final Function<Step, String> ACTION_FN = new PrimitiveDisplayFunction<Step>() {

		@Override
		public String applyToNonNull(Step step) {
			return null;
		}
		
	};
	
	private static final Map<String, Function<Step, String>> adapterMap = new ImmutableMap.Builder<String, Function<Step,String>>()
			.put("State", null)
			.put("Edit Roles", null)
			.put("Action", null)
			.put("To State", null)
			.put("Action Roles", null)
			.put("Pre Condition Fn", null)
			.put("Validtion Fn", null)
			.put("Post Transition Fn", null)
			.put("On State Fn", null)
			.build();
	
	protected WorkflowStepDisplayAdapter() {
		super(adapterMap);
		// TODO Auto-generated constructor stub
	}
	

}

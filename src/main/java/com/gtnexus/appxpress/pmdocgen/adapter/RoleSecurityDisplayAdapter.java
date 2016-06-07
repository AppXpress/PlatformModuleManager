package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.design.RoleSecurity;

public class RoleSecurityDisplayAdapter extends DisplayAdapter<RoleSecurity> {

	//	private static final Map<String, Function<CustomObjectDesignV110, String>> ROLE_SECURITY = 
	//	new ImmutableMap.Builder<String, Function<CustomObjectDesignV110,String>>()
	//	.put("Role", null)
	//	.put("Action", null)
	//	.build();
	private static final Map<String, Function<RoleSecurity, String>> adapterMap = new ImmutableMap.Builder<String, Function<RoleSecurity, String>>()
			.build();
	
	//TODO there is a gap between how the xml and how we want to display.
	// we need to do some preprocessing on the List<RoleSecurity>.
	// there should be a single entry for each role. with a boolean flag for each
	// modify/create.
	public RoleSecurityDisplayAdapter() {
		super(adapterMap);
	}

}

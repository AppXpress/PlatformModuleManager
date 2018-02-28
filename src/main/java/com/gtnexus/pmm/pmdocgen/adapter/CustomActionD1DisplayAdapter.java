package com.gtnexus.pmm.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.platform.module.model.customaction.CustomActionD1;

public class CustomActionD1DisplayAdapter extends DisplayAdapter<CustomActionD1> {

    private static final Function<CustomActionD1, String> DOC_TYPE_FN = new PrimitiveDisplayFunction<CustomActionD1>() {
	@Override
	public String applyToNonNull(CustomActionD1 ca) {
	    return ca.getDocType();
	}
    };

    private static final Function<CustomActionD1, String> API_VERSION_FN = new PrimitiveDisplayFunction<CustomActionD1>() {
	@Override
	public String applyToNonNull(CustomActionD1 ca) {
	    return ca.getApiVersion() == null ? "" : ca.getApiVersion().toString();
	}
    };

    private static final Function<CustomActionD1, String> NAME_FN = new PrimitiveDisplayFunction<CustomActionD1>() {
	@Override
	public String applyToNonNull(CustomActionD1 ca) {
	    return ca.getName();
	}
    };

    private static final Function<CustomActionD1, String> ROLES_FN = new PrimitiveDisplayFunction<CustomActionD1>() {
	@Override
	public String applyToNonNull(CustomActionD1 ca) {
	    return ca.getRoles();
	}
    };

    private static final Function<CustomActionD1, String> OQL_FN = new PrimitiveDisplayFunction<CustomActionD1>() {
	@Override
	public String applyToNonNull(CustomActionD1 ca) {
	    return ca.getOql();
	}
    };

    private static final Function<CustomActionD1, String> HAS_ACTION_FN_NAME_FN = new PrimitiveDisplayFunction<CustomActionD1>() {
	@Override
	public String applyToNonNull(CustomActionD1 ca) {
	    return ca.getHasActionFn();
	}
    };

    private static final Function<CustomActionD1, String> EXECUTION_FN_NAME_FN = new PrimitiveDisplayFunction<CustomActionD1>() {
	@Override
	public String applyToNonNull(CustomActionD1 ca) {
	    return ca.getExecuteFn();
	}
    };

    private static final Map<String, Function<CustomActionD1, String>> adapterMap = new ImmutableMap.Builder<String, Function<CustomActionD1, String>>()
	    .put("Document Type", DOC_TYPE_FN).put("API Version", API_VERSION_FN).put("Name", NAME_FN)
	    .put("Roles", ROLES_FN).put("OQL Filter", OQL_FN)
	    .put("Action Availability Function Name", HAS_ACTION_FN_NAME_FN)
	    .put("Execution Function Name", EXECUTION_FN_NAME_FN).build();

    public CustomActionD1DisplayAdapter() {
	super(adapterMap);
    }
}

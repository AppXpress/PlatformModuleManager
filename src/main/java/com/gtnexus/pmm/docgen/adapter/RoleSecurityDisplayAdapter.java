package com.gtnexus.pmm.docgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.platform.module.interpretation.role.security.NormalizedRoleSecurity;

public class RoleSecurityDisplayAdapter extends DisplayAdapter<NormalizedRoleSecurity> {

    private static final Function<NormalizedRoleSecurity, String> ROLE_FN = new PrimitiveDisplayFunction<NormalizedRoleSecurity>() {

	@Override
	public String applyToNonNull(NormalizedRoleSecurity nrs) {
	    return nrs.getRole();
	}
    };

    private static final Function<NormalizedRoleSecurity, String> CREATE_RIGHTS_FN = new BooleanDisplayFunction<NormalizedRoleSecurity>() {

	@Override
	public String applyToNonNull(NormalizedRoleSecurity nrs) {
	    return Boolean.toString(nrs.getHasCreateRights());
	}
    };

    private static final Function<NormalizedRoleSecurity, String> MODIFY_RIGHTS_FN = new BooleanDisplayFunction<NormalizedRoleSecurity>() {

	@Override
	public String applyToNonNull(NormalizedRoleSecurity nrs) {
	    return Boolean.toString(nrs.getHasModifyRights());
	}
    };

    private static final Map<String, Function<NormalizedRoleSecurity, String>> adapterMap = new ImmutableMap.Builder<String, Function<NormalizedRoleSecurity, String>>()
	    .put("Role", ROLE_FN).put("Create (Role in Community)", CREATE_RIGHTS_FN)
	    .put("Modify (Role on Transaction)", MODIFY_RIGHTS_FN).build();

    public RoleSecurityDisplayAdapter() {
	super(adapterMap);
    }

}

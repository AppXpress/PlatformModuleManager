package com.gtnexus.appxpress.platform.module.interpretation.role.security;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gtnexus.appxpress.platform.module.model.design.RoleSecurity;

public class NormalizedRoleSecurity {

    public static RoleSecurity standardRoleSecurity() {
	RoleSecurity objectCreatorRole = new RoleSecurity();
	objectCreatorRole.setRole(OBJECT_CREATOR);
	objectCreatorRole.setAction(CREATE);
	return objectCreatorRole;
    }

    public static List<NormalizedRoleSecurity> normalizeRoleSecurities(List<RoleSecurity> roleSecurities) {
	Map<String, NormalizedRoleSecurity> normalizedRoleSecuritiesMap = new TreeMap<String, NormalizedRoleSecurity>();
	roleSecurities.add(standardRoleSecurity());
	for (RoleSecurity roleSecurity : roleSecurities) {
	    String role = roleSecurity.getRole();
	    if (normalizedRoleSecuritiesMap.containsKey(role)) {
		NormalizedRoleSecurity normalizedRoleSecurity = normalizedRoleSecuritiesMap.get(role);
		normalizedRoleSecurity.applyActionRightsFrom(roleSecurity);
		normalizedRoleSecuritiesMap.put(role, normalizedRoleSecurity);
	    } else {
		normalizedRoleSecuritiesMap.put(role, new NormalizedRoleSecurity(roleSecurity));
	    }
	}
	return new LinkedList<NormalizedRoleSecurity>(normalizedRoleSecuritiesMap.values());
    }

    private final String role;
    private boolean hasCreateRights;
    private boolean hasModifyRights;

    private static final String OBJECT_CREATOR = "ObjectCreator";

    private static final String CREATE = "create";
    private static final String MODIFY = "modify";

    public NormalizedRoleSecurity(String role, boolean hasCreateRights, boolean hasModifyRights) {
	super();
	this.role = role;
	this.hasCreateRights = hasCreateRights;
	this.hasModifyRights = hasModifyRights;
    }

    public NormalizedRoleSecurity(RoleSecurity roleSecurity) {
	super();
	this.role = roleSecurity.getRole();
	this.hasCreateRights = getCreateRightsFrom(roleSecurity);
	this.hasModifyRights = getModifyRightsFrom(roleSecurity);
    }

    public String getRole() {
	return role;
    }

    public boolean getHasCreateRights() {
	return hasCreateRights;
    }

    private void setHasCreateRights(boolean hasCreateRights) {
	this.hasCreateRights = hasCreateRights;
    }

    public boolean getHasModifyRights() {
	return hasModifyRights;
    }

    private void setHasModifyRights(boolean hasModifyRights) {
	this.hasModifyRights = hasModifyRights;
    }

    private void applyActionRightsFrom(RoleSecurity roleSecurity) {
	if (getCreateRightsFrom(roleSecurity)) {
	    this.setHasCreateRights(true);
	}
	if (getModifyRightsFrom(roleSecurity)) {
	    this.setHasModifyRights(true);
	}
    }

    private static boolean getCreateRightsFrom(RoleSecurity roleSecurity) {
	return CREATE.equals(roleSecurity.getAction());
    }

    private static boolean getModifyRightsFrom(RoleSecurity roleSecurity) {
	return MODIFY.equals(roleSecurity.getAction());
    }
}

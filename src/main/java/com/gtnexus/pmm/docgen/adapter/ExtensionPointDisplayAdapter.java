package com.gtnexus.pmm.docgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.platform.module.model.design.CodExtensionPoint;

public class ExtensionPointDisplayAdapter extends DisplayAdapter<CodExtensionPoint> {

    private static final Function<CodExtensionPoint, String> EXTENSION_POINT_CODE_FN = new PrimitiveDisplayFunction<CodExtensionPoint>() {
	@Override
	public String applyToNonNull(CodExtensionPoint ep) {
	    return ep.getCode();
	}
    };

    private static final Function<CodExtensionPoint, String> ROLES_FN = new PrimitiveDisplayFunction<CodExtensionPoint>() {
	@Override
	public String applyToNonNull(CodExtensionPoint ep) {
	    return ep.getRoles();
	}
    };

    private static final Function<CodExtensionPoint, String> QUERY_FN = new PrimitiveDisplayFunction<CodExtensionPoint>() {
	@Override
	public String applyToNonNull(CodExtensionPoint ep) {
	    return ep.getObjectQueryEnabled();
	}
    };

    private static final Function<CodExtensionPoint, String> MUTATE_SELF_FN = new PrimitiveDisplayFunction<CodExtensionPoint>() {
	@Override
	public String applyToNonNull(CodExtensionPoint ep) {
	    return ep.getMutateSelfEnabled();
	}
    };

    private static final Function<CodExtensionPoint, String> MUTATE_OTHERS_FN = new PrimitiveDisplayFunction<CodExtensionPoint>() {
	@Override
	public String applyToNonNull(CodExtensionPoint ep) {
	    return ep.getMutateOthersEnabled();
	}
    };

    private static final Function<CodExtensionPoint, String> FIELD_POSITION_FN = new PrimitiveDisplayFunction<CodExtensionPoint>() {
	@Override
	public String applyToNonNull(CodExtensionPoint ep) {
	    return ep.getFieldPosition() == null ? "" : ep.getFieldPosition().toString();
	}
    };

    private static final Map<String, Function<CodExtensionPoint, String>> adapterMap = new ImmutableMap.Builder<String, Function<CodExtensionPoint, String>>()
	    .put("Extension Point", EXTENSION_POINT_CODE_FN).put("Roles", ROLES_FN).put("Query", QUERY_FN)
	    .put("Mutate Self", MUTATE_SELF_FN).put("Mutate Others", MUTATE_OTHERS_FN)
	    .put("Run Order", FIELD_POSITION_FN).build();

    public ExtensionPointDisplayAdapter() {
	super(adapterMap);
    }

}
package com.gtnexus.appxpress.pmdocgen.adapter;

import java.math.BigInteger;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.typeextension.Callbacks;
import com.gtnexus.appxpress.platform.module.model.typeextension.TypeExtensionD1;

public class TypeExtensionD1DisplayAdapter extends DisplayAdapter<TypeExtensionD1>{

	public static final Function<TypeExtensionD1, String> DOC_TYPE_FN = new Function<TypeExtensionD1, String>() {
		@Override
		public String apply(TypeExtensionD1 te) {
			return te.getDocumentType();
		}
	};

	public static final Function<TypeExtensionD1, String> API_VERSION_FN = new Function<TypeExtensionD1, String>() {
		@Override
		public String apply(TypeExtensionD1 te) {
			BigInteger apiVersion = te.getApiVersion();
			return apiVersion == null ? "" : apiVersion.toString();
		}
	};

	public static final Function<TypeExtensionD1, String> RANK_FN = new Function<TypeExtensionD1, String>() {
		@Override
		public String apply(TypeExtensionD1 te) {
			BigInteger rank = te.getRank();
			return rank == null ? "" : rank.toString();
		}
	};

	public static final Function<TypeExtensionD1, String> EVENT_FN = new Function<TypeExtensionD1, String>() {
		@Override
		public String apply(TypeExtensionD1 te) {
			Callbacks cb = te.getCallbacks();
			return cb == null ? "" : cb.getEvent();
		}
	};

	public static final Function<TypeExtensionD1, String> ROLE_FN = new Function<TypeExtensionD1, String>() {
		@Override
		public String apply(TypeExtensionD1 te) {
			Callbacks cb = te.getCallbacks();
			return cb == null ? "" : cb.getRole();
		}
	};

	public static final Function<TypeExtensionD1, String> FN_NAME_FN = new Function<TypeExtensionD1, String>() {
		@Override
		public String apply(TypeExtensionD1 te) {
			Callbacks cb = te.getCallbacks();
			return cb == null ? "" : cb.getFunctionName();
		}
	};
	
	private static final Map<String, Function<TypeExtensionD1, String>> adapterMap = new ImmutableMap.Builder<String, Function<TypeExtensionD1, String>>()
			.put("Document Type", DOC_TYPE_FN)
			.put("API Version", API_VERSION_FN)
			.put("Rank", RANK_FN)
			.put("Event", EVENT_FN)
			.put("Role", ROLE_FN)
			.put("Function Name", FN_NAME_FN)
			.build();
	
	public TypeExtensionD1DisplayAdapter() {
		super(adapterMap);
	}
}

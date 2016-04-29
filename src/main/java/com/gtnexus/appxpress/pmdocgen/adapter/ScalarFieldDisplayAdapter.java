package com.gtnexus.appxpress.pmdocgen.adapter;

import java.math.BigInteger;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.design.ScalarField;

public class ScalarFieldDisplayAdapter  extends DisplayAdapter<ScalarField> {
	
	protected static Function<ScalarField, String> NAME_FN = new Function<ScalarField, String>() {

		@Override
		public String apply(ScalarField field) {
			return field.getFieldName();
		}
	};
	
	protected static Function<ScalarField, String> DATA_TYPE_FN = new Function<ScalarField, String>() {

		@Override
		public String apply(ScalarField field) {
			String dataType = field.getDataType();
			if("COMPLEX".equals(dataType)) {
				return field.getDetailedDataType();
			}
			return dataType;
		}
	};
	
	protected static Function<ScalarField, String> INDEXED_FN = new Function<ScalarField, String>() {

		@Override
		public String apply(ScalarField field) {
			return Boolean.toString(field.isIndexed());
		}
	};
	
	protected static Function<ScalarField, String> RENDER_ON_UI_FN = new Function<ScalarField, String>() {

		@Override
		public String apply(ScalarField field) {
			return field.getRenderedOnUi();
		}
	};
	
	protected static Function<ScalarField, String> MAX_LENGTH_FN = new Function<ScalarField, String>() {

		@Override
		public String apply(ScalarField field) {
			BigInteger maxLength = field.getMaxLength();
			return maxLength == null ? "" : maxLength.toString();
		}
	};
	
	protected static Function<ScalarField, String> FIELD_POSITION_FN = new Function<ScalarField, String>() {

		@Override
		public String apply(ScalarField field) {
			BigInteger fieldPosition = field.getFieldPosition();
			return fieldPosition == null ? "" : fieldPosition.toString();
		}
	};
	
	private static final Map<String, Function<ScalarField, String>> adapterMap = new ImmutableMap.Builder<String, Function<ScalarField,String>>()
			.put("Field Name", NAME_FN)
			.put("Data Type", DATA_TYPE_FN)
			.put("Indexed?", INDEXED_FN)
			.put("Render On UI", RENDER_ON_UI_FN)
			.put("Max Length", MAX_LENGTH_FN)
			.put("field Position", FIELD_POSITION_FN)
			.build();
	
	public ScalarFieldDisplayAdapter() {
		super(adapterMap);
	}

}

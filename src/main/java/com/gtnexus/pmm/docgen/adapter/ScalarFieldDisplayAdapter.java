package com.gtnexus.pmm.docgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.platform.module.model.design.ScalarField;

public class ScalarFieldDisplayAdapter extends DisplayAdapter<ScalarField> {

    protected static Function<ScalarField, String> NAME_FN = new PrimitiveDisplayFunction<ScalarField>() {

	@Override
	public String applyToNonNull(ScalarField field) {
	    return field.getFieldName();
	}
    };

    protected static Function<ScalarField, String> DATA_TYPE_FN = new PrimitiveDisplayFunction<ScalarField>() {

	@Override
	public String applyToNonNull(ScalarField field) {
	    String dataType = field.getDataType();
	    if ("COMPLEX".equals(dataType)) {
		return field.getDetailedDataType();
	    }
	    return dataType;
	}
    };

    protected static Function<ScalarField, String> INDEXED_FN = new PrimitiveDisplayFunction<ScalarField>() {

	@Override
	public String applyToNonNull(ScalarField field) {
	    return Boolean.toString(field.isIndexed());
	}
    };

    protected static Function<ScalarField, String> SUMMARY_FIELD_FN = new PrimitiveDisplayFunction<ScalarField>() {

	@Override
	public String applyToNonNull(ScalarField field) {
	    return Boolean.toString(field.isSummaryField());
	}
    };

    protected static Function<ScalarField, String> RENDER_ON_UI_FN = new PrimitiveDisplayFunction<ScalarField>() {

	@Override
	public String applyToNonNull(ScalarField field) {
	    return field.getRenderedOnUi();
	}
    };

    protected static Function<ScalarField, String> MAX_LENGTH_FN = new PrimitiveDisplayFunction<ScalarField>() {

	@Override
	public String applyToNonNull(ScalarField field) {
	    return field.getMaxLength() == null ? "" : field.getMaxLength().toString();
	}
    };

    protected static Function<ScalarField, String> FIELD_POSITION_FN = new PrimitiveDisplayFunction<ScalarField>() {

	@Override
	public String applyToNonNull(ScalarField field) {
	    return field.getFieldPosition() == null ? "" : field.getFieldPosition().toString();
	}
    };

    private static final Map<String, Function<ScalarField, String>> adapterMap = new ImmutableMap.Builder<String, Function<ScalarField, String>>()
	    .put("Field Name", NAME_FN).put("Data Type", DATA_TYPE_FN).put("Indexed?", INDEXED_FN)
	    .put("Summary Field", SUMMARY_FIELD_FN).put("Render On UI", RENDER_ON_UI_FN)
	    .put("Max Length", MAX_LENGTH_FN).put("field Position", FIELD_POSITION_FN).build();

    public ScalarFieldDisplayAdapter() {
	super(adapterMap);
    }

}

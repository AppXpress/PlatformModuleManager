package com.gtnexus.appxpress.pmdocgen.adapter;

import java.math.BigInteger;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.design.LinkField;

public class EmbeddedFieldsDisplayAdapter extends DisplayAdapter<LinkField>{
	
	private static final Function<LinkField, String> NAME_FN = new PrimitiveDisplayFunction<LinkField>() {
		
		@Override
		public String applyToNonNull(LinkField field) {
			return field.getFieldName();
		}
	};
	
	private static final Function<LinkField, String> POSITION_FN = new PrimitiveDisplayFunction<LinkField>() {
		
		@Override
		public String applyToNonNull(LinkField field) {
			BigInteger fieldPosition = field.getFieldPosition();
			return fieldPosition == null ? "" : fieldPosition.toString();
		}
	};
	
	private static final Function<LinkField, String> DESIGN_FN = new PrimitiveDisplayFunction<LinkField>() {
		
		@Override
		public String applyToNonNull(LinkField field) {
			return field.getLinkToDesign();
		}
	};
	
	private static final Function<LinkField, String> RENDER_ON_UI_FN = new PrimitiveDisplayFunction<LinkField>() {
		
		@Override
		public String applyToNonNull(LinkField field) {
			return field.getRenderedOnUi();
		}
	};
	
	private static final Function<LinkField, String> IS_COLLECTION_FN = new PrimitiveDisplayFunction<LinkField>() {
		
		@Override
		public String applyToNonNull(LinkField field) {
			return Boolean.toString(field.isIsCollection());
		}
	};
	
	private final static Map<String, Function<LinkField, String>> adapterMap =
			new ImmutableMap.Builder<String, Function<LinkField,String>>()
			.put("Field Name", NAME_FN)
			.put("Field Position", POSITION_FN)
			.put("Design", DESIGN_FN)
			.put("Render on UI", RENDER_ON_UI_FN)
			.put("Is Collection?", IS_COLLECTION_FN)
			.build();

	public EmbeddedFieldsDisplayAdapter() {
		super(adapterMap);
	}

}

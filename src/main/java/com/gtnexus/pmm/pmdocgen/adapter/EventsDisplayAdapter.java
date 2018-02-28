package com.gtnexus.pmm.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.platform.module.model.design.EventField;

public class EventsDisplayAdapter extends DisplayAdapter<EventField> {

    private static final Function<EventField, String> FIELD_NAME_FN = new PrimitiveDisplayFunction<EventField>() {

	@Override
	public String applyToNonNull(EventField field) {
	    return field.getFieldName();
	}
    };

    private static final Function<EventField, String> DESCRIPTION_FN = new PrimitiveDisplayFunction<EventField>() {

	@Override
	public String applyToNonNull(EventField field) {
	    return field.getDescription();
	}
    };

    private static final Function<EventField, String> FIELD_POSITION_FN = new PrimitiveDisplayFunction<EventField>() {

	@Override
	public String applyToNonNull(EventField field) {
	    return field.getFieldPosition() == null ? "" : field.getFieldPosition().toString();
	}
    };

    private static final Function<EventField, String> EVENT_TYPE_FN = new PrimitiveDisplayFunction<EventField>() {

	@Override
	public String applyToNonNull(EventField field) {
	    return field.getEventType();
	}
    };

    private static final Function<EventField, String> FUNCTION_NAME_FN = new PrimitiveDisplayFunction<EventField>() {

	@Override
	public String applyToNonNull(EventField field) {
	    return field.getFunctionName();
	}
    };

    private final static Map<String, Function<EventField, String>> adapterMap = new ImmutableMap.Builder<String, Function<EventField, String>>()
	    .put("Field Name", FIELD_NAME_FN).put("Description", DESCRIPTION_FN).put("Event Type", EVENT_TYPE_FN)
	    .put("Function Name", FUNCTION_NAME_FN).put("Run Order", FIELD_POSITION_FN).build();

    public EventsDisplayAdapter() {
	super(adapterMap);
    }

}

package com.gtnexus.appxpress.pmbuilder.cli;

import com.gtnexus.appxpress.cli.AppXpressOption;
import com.gtnexus.appxpress.cli.OptionMessageProvider;
import com.gtnexus.appxpress.cli.ValidityProvider;

public enum  BuilderOption implements AppXpressOption {
	HELP("help", String.class, "Display usage for this tool", false, false, null),
	CUSTOMER("customer", String.class, "The customer who owns this module.",true, true, null ),
	PLATFORM("platform", String.class, "The name of the platform.", true, true, null),
	LOCAL_DIR("localDir", String.class, "Relative Path of GIT staging folder",true, true, null),
	;
	
	private final String name;
	private final Class<?> type;
	private final boolean hasArg;
	private final boolean isMandatory;
	private final String defaultValue;
	private final String description;
	private static final OptionMessageProvider msgProvider = new OptionMessageProvider();
	private static final ValidityProvider validityProvider = new ValidityProvider();
	
	private BuilderOption(String name, Class<?> type, String description,
			boolean hasArg, boolean isMandatory, String defaulValue) {
		this.name = name;
		this.type = type;
		this.hasArg = hasArg;
		this.description = description;
		this.isMandatory = isMandatory;
		this.defaultValue = defaulValue;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean hasArg() {
		return hasArg;
	}

	@Override
	public String getMessage() {
		return msgProvider.getMessage(type, name);
	}

	@Override
	public boolean isAppXpressMandatory() {
		return isMandatory;
	}

	@Override
	public boolean shouldBeOmitted() {
		return !isMandatory && defaultValue == null;
	}

	@Override
	public boolean isValid(String val) {
		return validityProvider.isValid(val, type);
	}

	@Override
	public String getDefaultValue() {
		if (this.isMandatory) {
			throw new UnsupportedOperationException(this.name
					+ " is a mandatory field, and must come from "
					+ "user args or properties. There is no default value.");
		}
		return defaultValue;
	}

}

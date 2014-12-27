package com.gtnexus.appxpress.pmbuilder.cli;

import com.gtnexus.appxpress.cli.asker.ValidityProvider;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.cli.option.OptionMessageProvider;

public enum  BuilderOption implements AppXpressOption {
	HELP("h", "help", String.class, "Display usage for this too.l", false, false, null),
	CUSTOMER("c", "customer", String.class, "The customer who owns this module.",true, true, null ),
	PLATFORM("p", "platform", String.class, "The name of the platform.", true, true, null),
	LOCAL_DIR("ld", "localDir", String.class, "Relative Path of GIT staging folder.",true, true, null),
	SELECT_PlATFORM("sp", "selectPlatform", Integer.class, "Select platform from the folders present in the cwd.", false, false, null)
	;
	
	private final String flag;
	private final String name;
	private final Class<?> type;
	private final boolean hasArg;
	private final boolean isMandatory;
	private final String defaultValue;
	private final String description;
	private static final OptionMessageProvider msgProvider = new OptionMessageProvider();
	private static final ValidityProvider validityProvider = new ValidityProvider();
	
	private BuilderOption(String flag, String name, Class<?> type, String description,
			boolean hasArg, boolean isMandatory, String defaulValue) {
		this.flag = flag;
		this.name = name;
		this.type = type;
		this.hasArg = hasArg;
		this.description = description;
		this.isMandatory = isMandatory;
		this.defaultValue = defaulValue;
	}

	@Override
	public String getLongName() {
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

	@Override
	public String getFlag() {
		return flag;
	}
	
	@Override
	public boolean isStoreableProperty() {
		if(this == LOCAL_DIR) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isHelpFlag() {
		return this.equals(HELP);
	}

}

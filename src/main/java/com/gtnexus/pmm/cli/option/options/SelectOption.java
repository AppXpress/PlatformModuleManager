package com.gtnexus.pmm.cli.option.options;

import com.gtnexus.pmm.cli.asker.ValidityProvider;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.cli.option.OptionMessageProvider;

public class SelectOption implements CLICommandOption {

    private final OptionMessageProvider msgProvider = new OptionMessageProvider();
    private final ValidityProvider validityProvider = new ValidityProvider();

    @Override
    public String getLongName() {
	return "select";
    }

    @Override
    public String getFlag() {
	return "s";
    }

    @Override
    public Class<?> getType() {
	return Integer.class;
    }

    @Override
    public String getDescription() {
	return "Select module from the folders present in the cwd.";
    }

    @Override
    public boolean hasArg() {
	return false;
    }

    @Override
    public boolean isHelpFlag() {
	return false;
    }

    @Override
    public String getMessage() {
	return msgProvider.getMessage(this.getType(), this.getLongName());
    }

    @Override
    public boolean isAppXpressMandatory() {
	return false;
    }

    @Override
    public boolean shouldBeOmitted() {
	return true;
    }

    @Override
    public boolean isValid(String val) {
	return validityProvider.isValid(val, this.getType());
    }

    @Override
    public String getDefaultValue() {
	return null;
    }

    @Override
    public boolean isStoreableProperty() {
	return false;
    }

}

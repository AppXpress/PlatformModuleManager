package com.gtnexus.pmm.cli.option.options;

import com.gtnexus.pmm.cli.option.AbstractCommandOption;
import com.gtnexus.pmm.cli.option.CommandOption;
import com.gtnexus.pmm.cli.option.OptionMessageProvider;
import com.gtnexus.pmm.cli.prompt.ValidityProvider;

public class LocalDirOption  extends AbstractCommandOption implements CommandOption {
    
    public static final String  NAME = "localDir";
    public static final String  FLAG = "ld";

    private final OptionMessageProvider msgProvider = new OptionMessageProvider();
    private final ValidityProvider validityProvider = new ValidityProvider();

    @Override
    public String getLongName() {
	return NAME;
    }

    @Override
    public String getFlag() {
	return FLAG;
    }

    @Override
    public Class<?> getType() {
	return String.class;
    }

    @Override
    public String getDescription() {
	return "Relative path of git staging folder.";
    }

    @Override
    public boolean hasArg() {
	return true;
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
	return true;
    }

    @Override
    public boolean shouldBeOmitted() {
	return false;
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
	return true;
    }

}

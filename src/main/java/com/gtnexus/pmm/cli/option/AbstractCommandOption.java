package com.gtnexus.pmm.cli.option;

public abstract class AbstractCommandOption implements CommandOption {
    
    @Override
    public int hashCode() {
	return this.getLongName().hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
	if(other == null) {
	    return false;
	}
	if(other == this) {
	    //same instance
	    return true;
	}
	if(other instanceof CommandOption) {
	    return this.getLongName().equals(((CommandOption)other).getLongName());
	}
	return false;
    }


}

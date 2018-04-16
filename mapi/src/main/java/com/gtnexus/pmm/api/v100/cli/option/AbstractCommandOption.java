package com.gtnexus.pmm.api.v100.cli.option;

public abstract class AbstractCommandOption implements CommandOption {

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("(")
	.append("--")
	.append(this.getLongName())
	.append(", ")
	.append("-")
	.append(this.getFlag())
	.append(")");
	return sb.toString();
    }

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

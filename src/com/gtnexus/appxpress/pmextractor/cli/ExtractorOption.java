package com.gtnexus.appxpress.pmextractor.cli;

import com.gtnexus.appxpress.cli.asker.ValidityProvider;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.cli.option.OptionMessageProvider;

/**
 * Enumeration of options that can be stored in the Properties file or read in
 * from the CLI.
 */
public class ExtractorOption implements AppXpressOption {

	public enum Opt implements AppXpressOption {
		HELP("h", "help", String.class, "Display usage for this tool", false,
				false, null), PLATFORM_ZIP("pz", "platformZip", String.class,
				"Exported Platform Module Name", true, true, null), LOCAL_DIR(
				"ld", "localDir", String.class,
				"Relative Path of GIT staging folder", true, true, null), CUSTOMER(
				"c", "customer", String.class, "Customer of Platform Module",
				true, true, null), SELECT("s", "select", Integer.class,
				"Select platform from the folders present in the cwd.", false,
				false, null), PLATFORM("p", "platform", String.class,
				"Platform Module that is being exported", true, true, null), OVERWRITE_SCRIPTS(
				"os", "overwriteScripts", Boolean.class,
				"If Y -> overwriteScripts = true", true, false, "N"), OVERWRITE_FEF(
				"of", "overwriteFef", Boolean.class,
				"If Y -> overwriteFEF = true", true, false, "N");

		private final String flag;
		private final String longName;
		private final Class<?> type;
		private final boolean hasArg;
		private final boolean isMandatory;
		private final String defaultValue;
		private final String description;
		private static final OptionMessageProvider msgProvider = new OptionMessageProvider();
		private static final ValidityProvider validityProvider = new ValidityProvider();

		/**
		 * 
		 * @param longName
		 *            The name of this ExtractorOption.
		 * @param type
		 *            The ExtractorOption type.
		 * @param description
		 * @param isMandatory
		 * @param defaulValue
		 */
		private Opt(String flag, String longName, Class<?> type,
				String description, boolean hasArg, boolean isMandatory,
				String defaulValue) {
			this.flag = flag;
			this.longName = longName;
			this.type = type;
			this.hasArg = hasArg;
			this.description = description;
			this.isMandatory = isMandatory;
			this.defaultValue = defaulValue;
		}

		@Override
		public String toString() {
			return longName;
		}

		@Override
		public String getLongName() {
			return longName;
		}

		@Override
		public Class<?> getType() {
			return type;
		}

		@Override
		public String getMessage() {
			return msgProvider.getMessage(type, longName);
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
		public boolean isAppXpressMandatory() {
			return isMandatory;
		}

		@Override
		public boolean shouldBeOmitted() {
			return !isMandatory && defaultValue == null;
		}

		/**
		 * @return the default value for non-mandatory options.
		 * @throws java.lang.UnsupportedOperationException
		 *             if this method is called on a mandatory option.
		 */
		@Override
		public String getDefaultValue() {
			if (this.isMandatory) {
				throw new UnsupportedOperationException(this.longName
						+ " is a mandatory field, and must come from "
						+ "user args or properties. There is no default value.");
			}
			return defaultValue;
		}

		/**
		 * Checks to see if the val supplies is a valid argument according to
		 * this option's type.
		 *
		 * @param val
		 * @return
		 */
		@Override
		public boolean isValid(String val) {
			return validityProvider.isValid(val, type);
		}

		@Override
		public String getFlag() {
			return flag;
		}

		@Override
		public boolean isStoreableProperty() {
			if (this == LOCAL_DIR) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isHelpFlag() {
			return this.equals(HELP);
		}

	}

	// Aliases-----------------------------------
	public static final ExtractorOption HELP = wrap(Opt.HELP);
	public static final ExtractorOption PLATFORM_ZIP = wrap(Opt.PLATFORM_ZIP);
	public static final ExtractorOption LOCAL_DIR = wrap(Opt.LOCAL_DIR);
	public static final ExtractorOption CUSTOMER = wrap(Opt.CUSTOMER);
	public static final ExtractorOption SELECT = wrap(Opt.SELECT);
	public static final ExtractorOption PLATFORM = wrap(Opt.PLATFORM);
	public static final ExtractorOption OVERWRITE_SCRIPTS = wrap(Opt.OVERWRITE_SCRIPTS);
	public static final ExtractorOption OVERWRITE_FEF = wrap(Opt.OVERWRITE_FEF);

	private final Opt opt;

	private ExtractorOption(Opt opt) {
		this.opt = opt;
	}

	private static ExtractorOption wrap(Opt o) {
		return new ExtractorOption(o);
	}

	@Override
	public String getLongName() {
		return opt.getLongName();
	}

	@Override
	public String getFlag() {
		return opt.getFlag();
	}

	@Override
	public Class<?> getType() {
		return opt.getType();
	}

	@Override
	public String getDescription() {
		return opt.getDescription();
	}

	@Override
	public boolean hasArg() {
		return opt.hasArg();
	}

	@Override
	public boolean isHelpFlag() {
		return opt.isHelpFlag();
	}

	@Override
	public String getMessage() {
		return opt.getMessage();
	}

	@Override
	public boolean isAppXpressMandatory() {
		return opt.isAppXpressMandatory();
	}

	@Override
	public boolean shouldBeOmitted() {
		return opt.shouldBeOmitted();
	}

	@Override
	public boolean isValid(String val) {
		return opt.isValid(val);
	}

	@Override
	public String getDefaultValue() {
		return opt.getDefaultValue();
	}

	@Override
	public boolean isStoreableProperty() {
		return opt.isStoreableProperty();
	}
}

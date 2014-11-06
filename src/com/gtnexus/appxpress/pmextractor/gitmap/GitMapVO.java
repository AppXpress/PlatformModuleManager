package com.gtnexus.appxpress.pmextractor.gitmap;

import static com.gtnexus.appxpress.AppXpressConstants.PLATFORM_MODULE_UNZIP_NAME;

import java.io.File;
import java.util.Map;

import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

public class GitMapVO {

	private File platformZip;
	private File localDir;
	private String customer;
	private String platform;
	private File customerDir;
	private File platformDir;
	private File unzipDir;
	private boolean overwriteScripts;
	private boolean overwriteFef;
	

	public GitMapVO(Map<ExtractorOption, String> optionMap) {
		this.localDir = new File(optionMap.get(ExtractorOption.LOCAL_DIR));
		this.customer = optionMap.get(ExtractorOption.CUSTOMER);
		this.platform = optionMap.get(ExtractorOption.PLATFORM);
		this.customerDir = localDir.toPath().resolve(customer).toFile();
		this.platformDir = customerDir.toPath().resolve(platform).toFile();
		this.platformZip = new File(optionMap.get(ExtractorOption.PLATFORM_ZIP));
		this.unzipDir = new File(PLATFORM_MODULE_UNZIP_NAME);
		this.overwriteScripts = optionMap
				.get(ExtractorOption.OVERWRITE_SCRIPTS).equalsIgnoreCase("y");
		this.overwriteFef = optionMap.get(ExtractorOption.OVERWRITE_FEF)
				.equalsIgnoreCase("y");
	}
	
	public File getPlatformZip() {
		return platformZip;
	}

	public File getLocalDir() {
		return localDir;
	}

	public String getCustomer() {
		return customer;
	}

	public String getPlatform() {
		return platform;
	}

	public File getCustomerDir() {
		return customerDir;
	}

	public File getPlatformDir() {
		return platformDir;
	}
	
	public File getUnzipDir() {
		return unzipDir;
	}

	public boolean isOverwriteScripts() {
		return overwriteScripts;
	}

	public boolean isOverwriteFef() {
		return overwriteFef;
	}

}

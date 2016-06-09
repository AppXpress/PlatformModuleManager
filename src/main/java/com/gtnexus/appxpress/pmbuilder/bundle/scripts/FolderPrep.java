package com.gtnexus.appxpress.pmbuilder.bundle.scripts;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_LINK_D1;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_OBJECT_MODULE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_UI;
import static com.gtnexus.appxpress.AppXpressConstants.DESIGN_;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTS;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.TYPE_EXTENSION_D1;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.gtnexus.appxpress.commons.HasPrerequisite;
import com.gtnexus.appxpress.commons.Preparation;
import com.gtnexus.appxpress.commons.file.FileService;
import com.gtnexus.appxpress.commons.file.filter.ChainedAnd;
import com.gtnexus.appxpress.commons.file.filter.FileFilterChain;
import com.gtnexus.appxpress.commons.file.filter.FileFilterFactory;
import com.gtnexus.appxpress.exception.AppXpressException;

public class FolderPrep implements Preparation<File> {

	private final FileService fs;

	public FolderPrep() {
		fs = new FileService();
	}

	/**
	 * Renames directories, and handle's special cases for specific directories.
	 */
	@Override
	public void prepare(File root) throws AppXpressException {
		for (File dir : root.listFiles(FileFilterFactory.directoriesOnly())) {
			try {
				route(dir);
			} catch (IOException e) {
				throw new AppXpressException("Exception when preparing "
						+ dir.toString() + ".", e);
			}
		}
	}

	private void route(File dir) throws IOException {
		// TODO can we do better than this?
		String directoryName = dir.getName();
		if (directoryName.endsWith(CUSTOM_LINK_D1)) {
			fs.renameFile(dir,  $ + CUSTOM_LINK_D1);
		} else if (directoryName.endsWith(TYPE_EXTENSION_D1)) {
			fs.renameFile(dir, $ + TYPE_EXTENSION_D1);
		} else if (directoryName.endsWith(CUSTOM_UI)) {
			fs.prependToName(Arrays.asList(dir.listFiles()), $);
		} else if (directoryName.endsWith(CUSTOM_OBJECT_MODULE)) {
			fixCustomObjectModule(dir);
		}
	}

	/**
	 * 
	 * @param directory
	 */
	private void fixCustomObjectModule(File directory) throws IOException {
		// TODO refactor me! But I am better than before :)
		File designFolder = new File(directory.getAbsolutePath()
				+ File.separator + "designs");
		if (designFolder.exists()) {
			renameWhenNecessary(designFolder.listFiles(), DESIGN_, DESIGN_ + $);
		}
		File scriptFolder = directory.toPath().resolve(SCRIPTS).toFile();
		if (scriptFolder.exists()) {
			FileFilterChain directoriesWithoutSigil = new ChainedAnd(FileFilterFactory.directoriesOnly(), 
					FileFilterFactory.fileNameDoesNotContain("_$"));
			File[] files = scriptFolder.listFiles(directoriesWithoutSigil);
			fs.renameFile(Arrays.asList(files), SCRIPT_DESIGN, SCRIPT_DESIGN + $);
		}
		File xsdFolder = directory.toPath().resolve("xsd").toFile();
		if (xsdFolder.exists()) {
			prependWhenNecessary(xsdFolder.listFiles(), $);
		}
	}

	private void renameWhenNecessary(final File[] files,
			final String toReplace, final String replacement)
			throws IOException {
		HasPrerequisite<File> precondition = doesNotStartWith(replacement);
		fs.renameFile(Arrays.asList(files), toReplace, replacement,
				precondition);
	}

	private void prependWhenNecessary(final File[] files, final String prepend)
			throws IOException {
		fs.prependToName(Arrays.asList(files), prepend, doesNotStartWith(prepend));
	}

	private HasPrerequisite<File> doesNotStartWith(final String string) {
		return new HasPrerequisite<File>() {

			@Override
			public boolean isMet(File f) {
				return !f.getName().startsWith(string);
			}
		};
	}

}

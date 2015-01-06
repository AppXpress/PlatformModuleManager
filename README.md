![alt text](http://developer.gtnexus.com/sites/default/files/GTNexusDeveloperNetwork.jpg "GTNexus Developer Network")
Using `pmbuilder` and `pmextractor`
============
## About

`PlatformModuleBuilder` is a set of tools to make importing and exporting Modules from AppXpress easy and painless!

For more information about AppXpress, [visit The Developer Network.](http://developer.gtnexus.com/)

### Use Case

``pmbuilder`` consumes a specifically structured platform module, and zips it up into a correctly formatted importable zip file.

``pmextractor`` takes an exported zip file and merges it with your
current local git working directory. ``pmextractor`` performs the opposite function
as ``pmbuilder``, and maps the exported module to a human readable file structure.

### Directory Overview and Git Structure

The local working directory should have the same structure as the one found in stash under pso/platform. Specifically,
the repository should have a `customer/` and `lib/` folder. The `customer/` folder
contains folders for different customers, under which there are custom object
folders. The lib folder contains reusable javascript files.

```
├── platform/
│  ├── customer/
│  │  ├── aCustomer/
│  │  │   ├── aModule
│  │  │   │   ├── commonScript
│  │  │   │   ├── CustomObjectModule
│  │  │   │   │   ├── designs
│  │  │   │   │   ├── xsd
│  │  │   │   ├── PlatformLocalization
│  │  │   │   ├── TypeExtension
│  ├── lib/
```

## Getting Started
[Pick up the latest release here.](https://github.com/AppXpress/PlatformModuleBuilder/releases/latest) Or build from source!

### Installing
After downloading the latest release, `pmbuilder` and `pmextractor` need to be added to your path, or put in a directory that is in already on your path.

e.g.:

```bash
mv pmbuilder ~/bin
mv pmextractor ~/bin
```

The scripts also require that a `$APPXPRESS_HOME` environment variable be declared. This variable should point to the parent directory of pmbuilder.jar

e.g:
```bash
echo "APPXPRESS_HOME='/path/to/parentDir/'" >> ~/.profile
```

### Building the .jar

If your current java version is not 1.7, you might run into
some problems running the the jars. You can always check your java version by
running the following:

```bash
$ java -version
```

If your versions is below 1.7, it is safest to build your own jar using the source code found
in the src/PM_Builder and src/PM_extractor folders respectively. Steps to build a jar in eclipse follow.

#### Building From Eclipse

After importing project to eclipse:
1. File -> Export
2. Java -> Runnable jar
3. Select "Extract required libraries into generated JAR".
4. Finish

## Running `pmbuilder` and `pmextractor`

Running either tool is easy. For example:

```bash
$ `pmbuilder`
```
If this is your first time running the tool, it will create a `~/.appxpress` directory for you. Properties files for each tool are stored in this directory. Some temp files will also be stored here while the tool is running.

When running either tool without and command line options, first the properties files are read.
If any necessary values are missing you will be prompted for them. It is important
that you are careful when running the tool like this.

Command line options **always** take precedence over the properties file. To display what options
a tool supports, pass the `--help` option.

```bash
$ pmbuilder -help
```

### Understanding the Options
The options that are common between `pmextractor` and `pmbuilder` adhere to the aforementioned git structure, and can be used to build the absolute path to a module.
```
${localDir}/${customer}/${platform}
```

If either tool is ran within a customer folder, then the option is inferred and does not need to be passed to the tool. Of course, you can still specify the `--customer` option to override this behavior.

### Selecting a Platform to Build or extract
`pmbuilder` and `pmextractor` can also select the platform for you! This cuts down on typos, and typing out full file names. To use this option, navigate to your customer's directory and run `pmbuilder` with the `-s` option.

```bash
$ pmbuilder -sp
```

### Using PM Builder's Import Utility

The `pmbuilder.jar` can add common files to any folder in your file structure. Use :

```javascript
!import commonFile.js
```

to import a file from the **lib** folder in your repository.

The following examples will import correctly:

```javascript
// !import commonFile.js

// !import commonFile1.js,commonFile2,js,etc.js

// !import x.js y.js z.js

/*

  !import example.js

*/
```

This following example will *not* import the file correctly ->

```javascript
/*
    Top comment
*/

function myFunction () {
  // code
  //!import alpha.js
}
//!import example.js
```

<b>*Note:*</b> The import utility does not continue to look for `!import` statement
once the top comment has ended in order to run more efficiently. Therefore,
any text not in a comment will cause the scanner to end looking at a
particular script.

Contributing
============

To contribute or ask a question, please open an issue with the appropriate tag,
or feel free to submit a pull request!

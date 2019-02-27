Using `pmm`
============

![TravisCI Buil Status](https://travis-ci.org/AppXpress/PlatformModuleManager.svg?branch=master)


## About

`PlatformModuleManager`, or `pmm` for short, is a set of tools for working with AppXpress Modules. It plays nicely with [axus](https://github.com/AppXpress/axus), by allowing you to keep your modules under source control. Once extracted from the platform, new iterations of the module can be tested, built, and reuploaded to the platform.

For more information about AppXpress, [visit The Developer Network.](http://developer.gtnexus.com/)

### Use Cases

* `pmm build` consumes a structured platform module, and zips it up into a correctly formatted importable zip file.
* `pmm extract` takes an exported zip file and merges it with your defined local directory. `pmm extract` performs the opposite function as `pmm build`, and maps the exported module to a human readable file structure.

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
Please build from source.

### Installing

*NOTE*: The releases page is outdated, and new releases will not be published. Please build from source.

From the project root (where the PMM repo has been cloned to):

```bash
$ ./gradlew fatjar
```

After building, the `pmm` script file (located inside `scripts` folder) needs to be added to your path, or put in a directory that is in already on your path.:

e.g.:

```bash
# REPLACE 'PATHTOPMM' WITH PATH TO YOUR PMM FILES
$ cp /PATHTOPMM/scripts/pmm ~/usr/local/bin
```

And `$APPXPRESS_HOME` environment variable must be declared. This variable should point to the parent directory of `pmm-all.jar`. `./gradlew fatjar` will deposit the `pmm-all.jar` in `build/libs`.

e.g: 
```bash
# REPLACE 'PATHTOPMM' WITH PATH TO YOUR PMM FILES
$ echo "export APPXPRESS_HOME='/PATHTOPMM/build/libs'" >> ~/.profile
```

**OR** add the following line to your `~/.bash_profile`

```script
# REPLACE 'PATHTOPMM' WITH PATH TO YOUR PMM FILES
APPXPRESS_HOME='/PATHTOPMM/build/libs'
```

**AND** run the following command from terminal

```bash
$ mkdir ~/.appxpress
$ echo "localDir='/code/gtnexus/platform/customer'" >> ~/.appxpress/pmm.properties
```

## Running `pmm build` and `pmm extract`

Running either tool is easy. For example:

```bash
$ `pmm build`
```
If this is your first time running the tool, it will create a `~/.appxpress` directory for you. Properties files for each tool are stored in this directory. Some temp files will also be stored here while the tool is running.

When running either tool without and command line options, first the properties files are read.
If any necessary values are missing you will be prompted for them. It is important
that you are careful when running the tool like this.

Command line options **always** take precedence over the properties file. To display what options
a tool supports, pass the `--help` option.

```bash
$ pmm build -help
```

### Understanding the Options
The options that are common to the `pmm` tool set adhere to the aforementioned git structure, and can be used to build the absolute path to a module.

```
${localDir}/${customer}/${platform}
```

If either tool is ran within a customer folder, then the option is inferred and does not need to be passed to the tool. Of course, you can still specify the `--customer` option to override this behavior.

### Selecting a Platform to Build or extract
`pmm build` and `pmm extract` can also select the platform for you! This cuts down on typos, and typing out full file names. To use this option, navigate to your customer's directory and run `pmm build` with the `-s` option.

```bash
$ pmm build -s
```

### Using PMBuilder's Import Utility

The build tool can add common scripts to any folder in your file structure. Use :

```javascript
!import commonFile.js
```

To import a file from the **lib/** folder in your repository.

The following examples will import correctly:

```javascript
// !import commonFile.js

// !import commonFile1.js,commonFile2,js,etc.js

// !import x.js y.js z.js

/*

  !import example.js

*/
```

This following example will *not* import the file correctly.

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

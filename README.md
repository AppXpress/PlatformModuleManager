Using `pmbuilder` and `pmextractor`
============
__*NOTE*:This README is a work in progress.__

## About

`PlatformModuleBuilder` is a set of tools to make importing and exporting Modules from AppXpress easy and painless!

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
Pick up the latest release here.

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


## Running `pmbuilder`

`pmbuilder` should be run in its own folder. This folder should contain `pmbuilder`Util.
`pmbuilder`Util.jar is found in this repository. `pmbuilder` uses Git, so Git must be
installed but no knowledge of Git is required to run the script. It is important to note
that `pmbuilder` should not be run in the same folder as your local git working directory, as
it could cause conflicts. The script is standalone, and will re pull changes from git every
time it runs.

```bash
$ `pmbuilder`
```



### Using PM Builder's Import Utility

The `pmbuilder`Util.jar can add common files to any folder in your file structure. Use ->

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

*Note:* The import utility does not continue to look for `!import` statement
once the top comment has ended in order to run more efficiently. Therefore,
any text not in a comment will cause the scanner to end looking at a
particular script.

### Options

The following options can be set to run with `pmbuilder`

```
-h		displays options on how to set the property file

-f		sets platform module folder

-c 		set customer

-b 		set branch to pull from repository

```

## Running `pmextractor`

`pmextractor` is a bash that automates the process of exporting a custom module
and merging with your local working directory. `pmextractor` backs up the current
working directory before the merge and supplies two options, overWrite Fef and
overWrite scripts. Currently, the bash script will prompt you for a **y** or **n**
to both options. `pmextractor` must be run in a place where it can access your
local directory and exported platform module.

`pmextractor` similarly has a property file that stores the working directory, the
name of the exported platform module, the platform module folder, and the customers
name. `pmextractor` is following the same structure as the one found in stash pso/platform.
Enter `bash `pmextractor` -h` to view various options in setting or clearing the property
file.

### Options

The following options can be set to run with `pmextractor`

```
-h		displays options on how to set the property file

-o		sets the script to overwrite FEF and scripts

-f		sets the script to overwrite FEF

-s		sets the script to overwrite scripts

-i 		sets the script to not overwrite FEF or scripts

-g		set git local directory

-p      set platform folder name

-c      set customer folder name

-z      set exported zip folder name

```

## `pmbuilder` and `pmextractor's` Properties File

Both scripts allow for quick reuse by supplying a property file. The extractor's
property file can store exported folder name, local directory name, customer name,
and platform module name. The builder's property file can store repository url, repository
branch, customer name, and platform module name. The property files are called builder.properties
and extractor.properties and can be found in your home /.platformtools folder. For each ,
Run -> `bash `pmbuilder` -help` or `bash `pmextractor` -help` to view the
different options for each respective script.

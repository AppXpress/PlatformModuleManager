Using `pmbuilder` and `pmextractor`
============

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
3. Ensure that "Extract required libraries into generated JAR" is selected.
4. Finish

## Running `pmbuilder` and `pmexractor`

Running either tool is easy. For example:

```bash
$ `pmbuilder`
```
When running either tool as above, first the properties files are read.
If any necessary values are missing you will be prompted for them. It is important
that you are careful when running the tool like this.

Command line options take precedence over the properties file. To display what options
a tool takes pass the `-help` option.

```bash
$ pmbuilder -help
```

### Understanding the Options
The options that are common between `pmextractor` and `pmbuilder` adhere to the aforementioned git structure, and can be used to build the absolute path to a module.
```
${localDir}/${customer}/${platform}
```

### Using PM Builder's Import Utility

The `pmbuilder`Util.jar can add common files to any folder in your file structure. Use :

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

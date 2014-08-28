Using pmbuilder and pmextractor
============

Bash script pmbuilder allows you to zip up a specifically structured GIT
repository and zip it up so it is ready to import in one step.

Bash script pmextractor allows you to unzip an exported platform module
and merge it with your local working directory. The script will not push these
changes to Git, this must be done manually. 

## Git Structure

The Git should be structured found in stash under pso/platform. Specifically,
the repository should have a customer and lib folder. The customer folder 
contains folders for different customers, under which there is custom object
folders. The lib folder contains reusable javascript files. 

## Building Jar from Eclipse

Both of the jar executables, pmBuilderUtil and pmExtractorUtil, are built
with *JRE 1.8*. If your current java version is not JRE 1.8, you might run into
some problems running the the jars. Check your java -version by entering in
the following command ->

`java -version` 

If it is not 1.8, it is safest to build your own jar using the source code found
in the src/PM_Builder and src/PM_extractor respectively. Steps to build
a jar in eclipse follow.

* Open a new java project by going to File->Java Project->

* Drag the source code found in src/PM_Builder/src into the src folder of your new project

* Right click on your project name , go into Build Path->Configure Build Path and navigate to
the libraries tab

* Add the jar that is found in the src/Libraries folder in this Git Repository, called zip4j_1.3.2.jar

* Press ok

* Now, right click again on your project name, click on export then under java click on **runnable jar file**

* For the pm_builder, make the launch config **PlatformModuleBuilder** and export it with the name pmBuilderUtil.

* For the pm_extractor, make the launch config **GitMap** and export it with the name pmExtractorUtil.

* Underneath the location and the launch config you will see three radio buttons. Make sure the middle one, Packaged
required libraries into generated JAR, is selected.

* Place each new jar with its corresponding bash script and it should run fine. 

## Running pmbuilder 

pmbuilder should be run in its own folder. This folder should contain pmBuilderUtil.
pmBuilderUtil.jar is found in this repository. pmbuilder uses Git, so Git must be
installed but no knowledge of Git is required to run the script.

`bash pmbuilder `

## PM Builder's & PM Extractor's Property File

Both scripts allow for quick reuse by supplying a property file. The extractor's
property file can store exported folder name, local directory name, customer name,
and platform module name. The builder's property file can store repository url, repository
branch, customer name, and platform module name. The property files are called builder.properties
and extractor.properties and can be found in your home /.platformtools folder. For each , 
Run -> `bash pmbuilder -help` or `bash pmextractor -help` to view the 
different options for each respective script.

## Using PM Builder's Import Utility

The pmBuilderUtil.jar can add common files to any folder in your file structure. Use ->

!import commonFile.js

to import a file from the **lib** folder in your repository. The import
statement must be contained in the comment or comment block at the beginning
of your program. The following examples will import correctly:

`// !import commonFile.js`

`// !import commonFile1.js,commonFile2,js,etc.js`

`// !import x.js y.js z.js`

```
/*

  !import example.js
  
*/
```

This following example will not import the file correctly ->

``` 
/*
    Top comment 
*/

function myFunction () {
  // code
  //!import alpha.js
}
//!import example.js
```

The import utility does not continue to look for !import statement
once the top comment has ended in order to run more efficiently. Therefore,
any text not in a comment will cause the scanner to end looking at a 
particular script. 

## Running pmextractor

pmextractor is a bash that automates the process of exporting a custom module
and merging with your local working directory. pmextractor backs up the current
working directory before the merge and supplies two options, overWrite Fef and 
overWrite scripts. Currently, the bash script will prompt you for a **y** or **n** 
to both options. pmextractor must be run in a place where it can access your
local directory and exported platform module.

pmextractor similarly has a property file that stores the working directory, the
name of the exported platform module, the platform module folder, and the customers
name. pmextractor is following the same structure as the one found in stash pso/platform.
Enter `bash pmextractor -h` to view various options in setting or clearing the property
file. 

### Options 

The following options can be set to run with pmextractor

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

### Src Folder

The src folder contains the source code of the two executable jars.


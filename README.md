Using PM_Builder and PM_Extracter
============

Bash script PM_Builder allows you to zip up a specifically structured GIT
repository and zip it up so it is ready to import in one step.

Bash script PM_Extracter allows you to unzip an exported platform module
and merge it with your local working directory. The script will not push these
changes to Git, this must be done manually. 

## Git Structure

The Git should be structured found in stash under pso/platform. Specifically,
the repository should have a customer and lib folder. The customer folder 
contains folders for different customers, under which there is custom object
folders. The lib folder contains reusable javascript files. 


## Running PM_Builder 

Marvin should be run in its own folder. This folder should contain PM_Builder Util.
PM_Builder_Util.jar is found in this repository. PM_Builder uses Git, so Git must be
installed but no knowledge of Git is required to run the script.

`bash PM_Builder `

## PM_Builder's Property File

This script allows for quick reuse by allowing the user to set up a property
file that can be called to re-run the same commands over and over again.
The property file has properties for repository URL, repository branch,
customer name, and custom object name. The property file can be set
by adding the changes directly to the file or by running the different
options available to the PM_Builder. Run -> `bash PM_Builder -help` to view the 
different options.

## Using PM_Builder's Import Utility

The PM_Builder_util.jar can add common files to any folder in your file structure. Use ->

@!import commonFile.js

to import a file from the **lib** folder in your repository. The import
statement must be contained in the comment or comment block at the beginning
of your program. The following examples will import correctly:

`// @!import commonFile.js`

`// @!import commonFile1.js,commonFile2,js,etc.js`

`// @!import x.js y.js z.js`

```
/*

  @!import example.js
  
*/
```

This following example will not import the file correctly ->

``` 
/*
    Top comment 
*/

function myFunction () {
  // code
  //@!import alpha.js
}
//@!import example.js
```

The import utility does not continue to look for @!import statement
once the top comment has ended in order to run more efficiently. Therefore,
any text not in a comment will cause the scanner to end looking at a 
particular script. 

## Running PM_Extracter

PM_Extracter is a bash that automates the process of exporting a custom module
and merging with your local working directory. PM_Extracter backs up the current
working directory before the merge and supplies two options, overWrite Fef and 
overWrite scripts. Currently, the bash script will prompt you for a **y** or **n** 
to both options. PM_Extracter must be run in a place where it can access your
local directory and exported platform module.

PM_Extracter similarly has a property file that stores the working directory, the
name of the exported platform module, the platform module folder, and the customers
name. PM_Extracter is following the same structure as the one found in stash pso/platform.
Enter `bash PM_Extracter -help` to view various options in setting or clearing the property
file. 

### Options 

The following options can be set to run with PM_Extracter

```
-h		displays options on how to set the property file

-o		sets the script to overwrite FEF and scripts

-f		sets the script to overwrite FEF

-s		sets the script to overwrite scripts

-i 		sets the script to not overwrite FEF or scripts 

```

### Src Folder

The src folder contains the source code of the two executable jars.


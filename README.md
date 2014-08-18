Using Marvin
============

Bash script Marvin allows you to zip up a specifically structured GIT
repository and zip it up so it is ready to import in one step.

## Git Structure

The Git should be structured found in stash under pso/platform. Specifically,
the repository should have a customer and lib folder. The customer folder 
contains folders for different customers, under which there is custom object
folders. The lib folder contains reusable javascript files. 


## Running Marvin

Marvin should be run in its own folder. This folder should contain the two
jar executables that Marvin calls. These are the jar files found in this repository,
Marvin1.07.jar and MarvinImportUtil.jar.

`bash Marvin`

## Marvin's Property File

Marvin allows for quick reuse by allowing the user to set up a property
file that can be called to re-run the same commands over and over again.
The property file has properties for repository URL, repository branch,
customer name, and custom object name. The property file can be set
by adding the changes directly to the file or by running the different
options available to Marvin. Run -> `bash Marvin -help` to view the 
different options.

## Using Marvin's Import Utility

Marvin can add common files to any folder in your file structure. Use ->

@!import commonFile.js

to import a file from the **lib** folder in your repository. The import
statement must be contained in the comment or comment block at the beginning
of your program. The following examples will import correctly:

`// @!import commonFile.js`

`// @!import commonFile1.js,commonFile2,js,etc.js`

`// @!import x.js y.js z.js`

`/*

  @!import example.js
  
*/`

This following example will not import the file correctly ->

` /*
    Top comment 
*/

function myFunction () {
  // code
  //@!import alpha.js
}
//@!import example.js
`
The import utility does not continue to look for @!import statement
once the top comment has ended in order to run more efficiently. Therefore,
any text not in a comment will cause the scanner to end looking at a 
particular script. 





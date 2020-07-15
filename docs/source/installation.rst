.. _installation:

Download and installation



  java -jar /path/to/LCAParse.jar -help

We also provide a script that executes the jar. This can be found in the bin directory. At the top of it can be found the line::

  JARFILE=/Users/leggettr/Documents/github/LCAParse/target/LCAParse.jar

You should change this to point to the location of your LCAParse.jar file. You can then place the lcaparse script in a directory pointed to by your PATH variable, so that it is easily available without having to specify the full path. 

Alternatively, add the bin directory to your path variable. On Linux, you would typically do this by adding the following command to your .bash_profile (or .profile on Ubuntu) or 'source' script::

  lcaparse -help 

For parsing accession IDs (if tax IDs are not in the Blast output), LCAParse also requires  the nucl_wgs.accession2taxid file from the accession2taxid directory of the NCBI Taxonomy FTP site above.
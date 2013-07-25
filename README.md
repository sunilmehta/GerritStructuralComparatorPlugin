GerritStructuralComparatorPlugin
================================

Introduction
============

JavaStructuralComparator
------------------------
 Structural Comparator, no matter what the order of property/procedure is, compares a property /procedure with the matching one based on the name of property/method. 

        

Benefit of JavaStructuralComparator over Line-by-Line Comparator
* General line by line comparator does not use any sought of semantics for the comparison. It checks for the string pattern in a particular line. On the other hand Comparing Java files (which has syntax and semantics) on basis of string pattern alone does not help .  For a java file reordering of 2 procedures (method) does not make any difference for JavaStructuralComparator.
* If the java code is formatted by a formatter plugin or default eclipse code formatter, it will show changes in Line-by-Line comparator while it shows no difference in JavaStructuralComparator
* Structural Comparator will not show any changes, until no change to the code. 
* In case of method reordering along with changes in reordered method, it will only show the difference for the code changed.
* It will compare modified module instead of whole class file. So User require to navigate through changes.


Building
--------
In order to build GerritPlugin you will need the following applications:

* Java JDK 6 (http://www.oracle.com/technetwork/java/javase/downloads/index.html).
* Apache Maven 3.x (http://maven.apache.org/)

To clone the GerritPlugin repository, use git clone:

<pre>
 git clone https://github.com/SpringSource/wavemaker.git
</pre>

The resultant jar file can be copied to your testSite/Plugins folder.


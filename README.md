#Gerrit-Compare-Plugin
Diff tool designed for "Structural Programming"

-------------------------

##Introduction

Gerrit-Compare-Plugin makes reviews easier by showing changes by selected property/Method name in a side-by-side display, and allowing inline comments to be added by any reviewer. This plugin show changes by comparing Property/Method with the matching one, no matter what the order of property/Method is.


##Features

Following are some of the main features of Gerrit-Compare-Plugin:

- Refactor detection. It can find renamed, moved, reordered, wrapped, lifted, combined or fragmented code. Structured Difference allows focusing on the actual changes because it ignores location. Compare this for example with the regular file based difference that reports the entire method as new and deleted and therefore making the actual changes untraceable.
- Format insensitivity. The comparison result will not be affected by linebreaks or whitespaces.
- Navigation tree. The Navigation tree shows a hierarchal overview of changed, added and removed entities. This allows fast navigation to inspect change details.
- Comprehensible output. The interactive UI helps users to navigate and understand changes efficiently.


##Supported Languages

Currently, Gerrit-Compare-Plugin can parse only Java language. We will include parser for other Structural Programming language in future.


##Bugs
If you find an issue, let us know [here](https://github.com/sans-sense/GerritStructuralComparatorPlugin/issues?page=1&state=open) 

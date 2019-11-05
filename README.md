# Factory for Muscat MarcXML records

## Overview
This plugin creates MarcXML-files directly from the Muscat configuration with all tags and subfields. 
If there is a txt-file with content in the config/ folder, this subfield content is taken for building the MarcXML. Otherwise there will be a placeholder-string with tag and code plus timestamp.
All exported files are created in the output/ folder.    

## Background
For testing purposes it's necessary to have a dataset with all marc fields from Muscat. Additionally all (source-)templates should be considered, so after executing the jar we have in the output folder MarcXML-files with this set. To improve readability and showing of dependencies the field content is taken from the config folder, but the linkage for now is loose since this needs cross-references in all the content files. These cross-references has to be added manually, an overview dependency schema would be one step to improve this.    

## Tutorial

### Prerequisits
- Un*x (due to folder structure)
- Java 8 or higher
- Synchronized Muscat repository as submodule (eg. "git submodule update")


### Execution
With command line:

```bash
user@host> java -jar bin/factory.jar
```

### Build

```bash
user@host> mvn clean compile assembly:single
```

## Todo
Better Field content in xxx.txt




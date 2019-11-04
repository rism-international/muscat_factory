## Factory for Muscat MarcXML records

### Overview
This plugin creates MarcXML-files directly from the Muscat configuration with all tags and subfields. 
If there is a txt-file with content in the config/ folder, this subfield content is taken for building the MarcXML. Otherwise there will be a placeholder-string with tag and code plus timestamp.
All exported files are created in the output/ folder.    

### Tutorial

##### Prerequisits
- Un*x (due to folder structure)
- Java 8 or higher
- Synchronized Muscat repository as submodule (eg. "git submodule update")

##### Build

```bash
user@host> mvn clean compile assembly:single
```

##### Execution
With command line:

```bash
user@host> java -jar bin/factory.jar
```

#### Todo
Better Field content in xxx.txt




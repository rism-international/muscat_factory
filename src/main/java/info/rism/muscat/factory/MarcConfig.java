package info.rism.muscat.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class MarcConfig {
	
	private final Map<String, Object> configuration;
	private final SortedMap<String, Object> tags;
	private String model;
	
	public MarcConfig(String model) throws IOException{
		this.model = model;
		YamlParser yamlParser = new YamlParser();
		String filename = "muscat/config/marc/tag_config_" + model + ".yml";
		this.configuration = yamlParser.load_file(filename);		
		this.tags = tags_with_subfields();
	}

	/**
	 * After loading Muscat Marc-configuration this 
	 * returns a sorted map with {tag=[subfield_list,...],tag...} 
	 * @return SortedMap
	 */
	@SuppressWarnings("unchecked")
	public SortedMap<String, Object> tags_with_subfields() {
		SortedMap<String, Object> resultMap = new TreeMap<String, Object>();
		LinkedHashMap<String, Object> tags = (LinkedHashMap<String, Object>) this.configuration.get(":tags");
		for (Map.Entry<String, Object> entry : tags.entrySet()) {
			List<String> subfields = new ArrayList<>();
		    String key = entry.getKey();
		    LinkedHashMap<String, Object> value = (LinkedHashMap<String, Object>) entry.getValue();
		    List<Object> fields = (List<Object>) value.get(":fields");
		    for (Object element : fields) {
		    	List<Object> cfgList = (List<Object>) element;
		    	subfields.add((String) cfgList.get(0));		    			    	
		    }		        
		    java.util.Collections.sort(subfields);			
		    if (!key.contentEquals("000")) {
		    	resultMap.put(key, subfields);
		    }		    
		}
		return resultMap;
	}
	
	public SortedMap<String, Object> getTags() {
		return this.tags;
	}
	
	public SortedMap<String, Object> removeTags(List<String> excluded_tags){
		for (String tag : excluded_tags) {
			this.tags.remove(tag);
		}		
		return this.tags;		
	}
	
	/**
	 * These methods are storing/restoring the MarcConfig. 
	 * Sometimes later it would be possible to show any changes in the log-folder by comparing the actual MarcConfig with the dump-object  
	 * @throws IOException
	 */
	public void dump() throws IOException {
		FileOutputStream fout = new FileOutputStream("tmp/"+model+".ser");
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(this.tags);
		oos.close();		
	}
	
	public SortedMap<String, Object> restore() throws IOException, ClassNotFoundException{
		 FileInputStream fis = new FileInputStream("tmp/"+model+".ser");
		 ObjectInputStream ois = new ObjectInputStream(fis);
		 return (SortedMap<String, Object>) ois.readObject();
	}

	public void compare() throws ClassNotFoundException, IOException {
		
		List<String> log = new ArrayList<String>();
		SortedMap<String, Object> dumpMap = restore();
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		/**
		 * TODO this should be done also with removing tags and subfields
		 */
		for (String tag : this.tags.keySet()) {
			if (!dumpMap.containsKey(tag)){
				log.add("[" + time + "] " + "New tag " + model + " " + tag);
			}
			else {
				List<String> subfields = (List<String>) this.tags.get(tag);
				List<String> dumpsubfields = (List<String>) dumpMap.get(tag);
				for (String code : subfields) {
					if (!dumpsubfields.contains(code)) {
						log.add("[" + time + "] " + "New subfield " + model + " " + tag + "$" + code);
					}									
				}
			}
		}
		if (!log.isEmpty()) {
			String filename= "log/marctags.log";
			String content = new String(Files.readAllBytes(Paths.get(filename)));			
		    FileWriter fw = new FileWriter(filename,false);
			for (String e : log) {
				fw.write(e + "\n");
			}
			fw.write(content);
			fw.close();
			dump();
		} else {
			dump();
		}
		
	}
	
}

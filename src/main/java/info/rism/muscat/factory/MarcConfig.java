package info.rism.muscat.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MarcConfig {
	
	private final Map<String, Object> configuration;
	private final SortedMap<String, Object> tags;
	
	public MarcConfig(String filename) throws IOException{
		YamlParser yamlParser = new YamlParser();
		this.configuration = yamlParser.load_file(filename);
		this.tags = tags_with_subfields();
	}

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
		    //System.out.println(key);
		    if (!key.contentEquals("000")) {
		    //if (subfields.get(0) != null && !subfields.get(0).isEmpty()) {
		    	resultMap.put(key, subfields);
		    }		    
		}
		return resultMap;
	}
	
	public SortedMap<String, Object> getTags() {
		return this.tags;
	}

	
}

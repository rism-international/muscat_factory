package info.rism.muscat.factory;

import java.io.IOException;

import java.util.Map;


public class Template {
	private final Map<String, Object> configuration;
	
	public Template() throws IOException {
		this.configuration = this.all_templates(); 
	}
	
	public Map<String, Object> all_templates() throws IOException {		
		YamlParser yamlParser = new YamlParser();
		Map<String, Object> confMap = yamlParser.load_file("/config/leader.yml");		
		return confMap;
	}

	public Map<String, Object> getConfiguration() {
		return this.configuration;
	}
	
	public String getLeader(String template) {
		String leader =  this.configuration.get(template).toString();
		return leader;
	}
	
	
	
}

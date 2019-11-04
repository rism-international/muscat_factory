package info.rism.muscat.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Template {
	
	private String name;
	private String leader;
	
	public Template(String template) throws IOException {		
		if (all_templates().contains(template)) {
			this.name = template; 
			this.leader = config().get(template).toString();
			} 
	}
	
	public static Map<String, Object> config() throws IOException {		
		YamlParser yamlParser = new YamlParser();
		Map<String, Object> confMap = yamlParser.load_file("/config/leader.yml");		
		return confMap;
	}

	public static List<String> all_templates() throws IOException {	
		List<String> array = Arrays.asList(config().keySet().stream().toArray(String[]::new));
		java.util.Collections.sort(array);
		return array;
	}

	public List<String> excluded_tags() throws IOException{
		List<String> tags = new ArrayList<String>();		
		String dirString = System.getProperty("user.dir");
    	String filename = "/config/sources.yml";
		YamlParser yamlParser = new YamlParser();		
		Map<String, Object> confMap = yamlParser.load_file(filename);
		Map<String, List<String>> tag_exclude = (Map<String, List<String>>) confMap.get("tag_exclude"); 
		Map<String, List<String>> group_exclude = (Map<String, List<String>>) confMap.get("group_exclude"); 
		Map<String, List<String>> groups = (Map<String, List<String>>) confMap.get("groups"); 
		
		if (tag_exclude.containsKey(this.name)) {
			tags = tag_exclude.get(this.name);
		}
		
		if (group_exclude.containsKey(this.name)) {
			List<String> res = new ArrayList<String>();
			List<String> gx = group_exclude.get(this.name);
			gx.stream().forEach( s ->	{ 
				Map<String, List<String>> all_tags = (Map<String, List<String>>) groups.get(s); 
				all_tags.get("all_tags").stream().forEachOrdered(res::add);
				});
			res.stream().forEachOrdered(tags::add);
		}
		java.util.Collections.sort(tags);
		return tags;
	}
	
	public String getLeader() {
		return this.leader;
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * Utility to copy the (ruby) yaml to a readably file for snakeyaml
	 * @throws IOException
	 */
	public static void repair_yaml() throws IOException {
    	String dirString = System.getProperty("user.dir");
    	String ifile = dirString + "/muscat/config/editor_profiles/default/configurations/SourceLayoutDefault.yml";
		String ofile = dirString + "/config/sources.yml";		
		File file = new File(ofile);
		FileWriter fr = new FileWriter(file);		
		BufferedReader reader = new BufferedReader(new FileReader(ifile));
		 reader.readLine();
		 String line1=null;
		 while ((line1 = reader.readLine()) != null){
			 fr.write(line1 + "\n");
			 
		 }
		 reader.close();
		 fr.close();

	}
	
	
}

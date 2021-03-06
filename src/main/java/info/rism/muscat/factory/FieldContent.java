package info.rism.muscat.factory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FieldContent {
	
	private final String filename;	
	private final HashMap<String, HashMap<String, String>> tags;
	
	public FieldContent(String filename) throws FileNotFoundException, IOException {
		String dirString = System.getProperty("user.dir");
		this.filename = dirString + "/config/marc/" + filename;
		this.tags = parse();	 
	}

	/**
	 * This will use content from the xxx.txt files in the config folder instead of timestamp values
	 * returning a map with field content from the config-folder
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public HashMap<String, HashMap<String, String>> parse() throws FileNotFoundException, IOException {
		HashMap<String, HashMap<String, String>> hash = new HashMap<String, HashMap<String, String>>();
		try(BufferedReader br = new BufferedReader(new FileReader(this.filename))) {
			for(String line; (line = br.readLine()) != null; ) {
				String tag = line.split("\\s+")[0].replace("=", "");
				List<String> sfList = Arrays.asList(line.split("\\$"));
				HashMap<String, String> sfHashMap = new HashMap<String, String>();

				if (tag.startsWith("00")) {
					String contentString = line.split("\\s+")[1];
					sfHashMap.put(null, contentString);
					hash.put(tag, sfHashMap);
				}
				
				else {
					for (String e : sfList) {					
						if (e.matches("^[0-9a-z].*")){
							String subfieldcode = e.substring(0, 1);
							String content = e.substring(1, e.length());
							sfHashMap.put(subfieldcode, content);
						}					
					}  
					hash.put(tag, sfHashMap);
				}
			}
		}
		return hash;
	}
	
	public HashMap<String, HashMap<String, String>> getTags() {
		return this.tags;
	}
	
	/**
	 * Return the content of a datafield (df, code) or controlfield (df, null)
	 * @param tag
	 * @param subfield
	 * @return
	 */
	public String getContent(String tag, String subfield) {		
		if (this.tags.get(tag) == null) {
			return null;
		}		
		HashMap<String, String> subHashMap = this.tags.get(tag);
		if (subfield==null) {
			return subHashMap.get(null);
		}
		if (subHashMap.get(subfield) == null) {
			return null;
		}
		return subHashMap.get(subfield);
	}
	
}

package info.rism.muscat.factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlParser {
	public Map<String, Object> load_file(String file) throws IOException  {
		String dirString = System.getProperty("user.dir");
		String filename = dirString + "/" + file; 
		
		Yaml yaml = new Yaml();
		try {
			InputStream in = new FileInputStream(filename); 
			Map<String, Object> obj = yaml.load(in);
			return obj;
		}
		catch (IOException e) {
	        System.out.println("file not found: " + filename);    
	    }
		return null;
	}
}

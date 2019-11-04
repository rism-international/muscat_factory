package info.rism.muscat.factory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	Template.repair_yaml();
    	
    	String dirString = System.getProperty("user.dir");
    	File folder = new File(dirString + "/muscat/config/marc/");
    	File[] listOfFiles = folder.listFiles();
    	ArrayList<String> models = new ArrayList<String>();
    	for (File file : listOfFiles) {
    		String filenameString = file.getName();
    		if (filenameString.indexOf("tag_config_") >= 0) {
    			models.add(file.getName().replace("tag_config_", "").replace(".yml", ""));
    		}    		
    	}
    	for (String model : models) {
    		
    		if (model.equals("source")) {
    			for (String tp : Template.all_templates()) {
    				MarcConfig marcConfig = new MarcConfig("muscat/config/marc/tag_config_" + model + ".yml");
    				Template template = new Template(tp);
    				List<String> excluded_tags = template.excluded_tags(); 
    				marcConfig.removeTags(excluded_tags);
    				FieldContent fieldContent = new FieldContent(tp + ".txt");
    				MarcxmlBuilder marcxmlBuilder = new MarcxmlBuilder("output/sources/"+ tp + ".xml");        
        			marcxmlBuilder.build(marcConfig, fieldContent, template.getLeader());    				    				
    			}
    			
    		}
    		else {
    			MarcConfig marcConfig = new MarcConfig("muscat/config/marc/tag_config_" + model + ".yml");
    			FieldContent fieldContent = new FieldContent(model + ".txt");
    			MarcxmlBuilder marcxmlBuilder = new MarcxmlBuilder("output/"+ model + ".xml");        
    			marcxmlBuilder.build(marcConfig, fieldContent, "00000cam a2200000 a 4500");
    		}
    	}
    	
    	System.out.println("Completed!");
    }
}

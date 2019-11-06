package info.rism.muscat.factory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class
 *
 */
public class Factory 
{
    public static void main( String[] args ) throws IOException, ClassNotFoundException
    {
    	System.out.println("Building MarcXML-files in output/ folder...");
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
    			MarcConfig marcsourceConfig = new MarcConfig(model);
    			marcsourceConfig.compare();
    			for (String tp : Template.all_templates()) {
    				MarcConfig marcConfig = new MarcConfig(model);
    				Template template = new Template(tp);
    				List<String> excluded_tags = template.excluded_tags(); 
    				marcConfig.removeTags(excluded_tags);
    				FieldContent fieldContent = new FieldContent(tp + ".txt");
    				MarcxmlBuilder marcxmlBuilder = new MarcxmlBuilder("output/sources/"+ tp + ".xml");
    				if (excluded_tags.contains("852")) {
    					marcxmlBuilder.build(marcConfig, fieldContent, template.getLeader(), true);
    				} else {
    					marcxmlBuilder.build(marcConfig, fieldContent, template.getLeader(), false);
    				}    				
    			}   
    			MarcxmlBuilder.merge();
    		}
    		else {
    			if (!model.equals("holding")) {
    				MarcConfig marcConfig = new MarcConfig(model);
    				marcConfig.compare();
    				FieldContent fieldContent = new FieldContent(model + ".txt");
    				MarcxmlBuilder marcxmlBuilder = new MarcxmlBuilder("output/"+ model + ".xml");        
    				marcxmlBuilder.build(marcConfig, fieldContent, "00000cam a2200000 a 4500", false);
    			}
    			else {
    				MarcConfig marcConfig = new MarcConfig(model);
    				marcConfig.compare();
    			}
    		}
    	}
    	
    	System.out.println("Completed!");
    }
}

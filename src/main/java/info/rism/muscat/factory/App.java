package info.rism.muscat.factory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main class
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
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
    		MarcConfig marcConfig = new MarcConfig("muscat/config/marc/tag_config_" + model + ".yml");
    		FieldContent fieldContent = new FieldContent(model + ".txt");
    		MarcxmlBuilder marcxmlBuilder = new MarcxmlBuilder("output/"+ model + ".xml");        
    		marcxmlBuilder.build(marcConfig, fieldContent, "00000cam a2200000 a 4500");
    	}
    }
}

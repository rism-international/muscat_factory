package info.rism.muscat.factory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Tagcheck {
	  public static void main( String[] args ) throws IOException, ClassNotFoundException
	    {
		  	System.out.println("Checking changes in MarcConfig...");		  
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
	    			MarcConfig marcsourceConfig = new MarcConfig(model);
	    			marcsourceConfig.compare();
	    		
	    	}
	    	System.out.println("Completed!");
	    }
}

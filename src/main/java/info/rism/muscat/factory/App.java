package info.rism.muscat.factory;

import java.io.IOException;

/**
 * Main class
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	MarcConfig marcConfig = new MarcConfig("muscat/config/marc/tag_config_work.yml");
    	FieldContent fieldContent = new FieldContent("work.txt");
        MarcxmlBuilder marcxmlBuilder = new MarcxmlBuilder("output/work.xml");
        
        marcxmlBuilder.build(marcConfig, fieldContent, "00000cam a2200000 a 4500");
    }
}

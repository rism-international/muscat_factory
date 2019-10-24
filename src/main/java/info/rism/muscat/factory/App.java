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
        System.out.println( marcConfig.tags_with_subfields() );
        MarcxmlBuilder marcxmlBuilder = new MarcxmlBuilder();
        marcxmlBuilder.build();
    }
}

package info.rism.muscat.factory;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marc4j.MarcReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.DataField;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;

public class MarcxmlBuilder {

	private final String filename;	
	
	public MarcxmlBuilder(String filename) {
		String dirString = System.getProperty("user.dir");
		this.filename = dirString + "/" + filename;
	}

	public void build(MarcConfig marcConfig, FieldContent fieldContent, String leader, Boolean withHolding) throws IOException {

		MarcFactory factory = MarcFactory.newInstance();
		Record record = factory.newRecord(leader);
		//record.addVariableField(factory.newControlField("001", "12883376"));
		for(Map.Entry<String,Object> entry : marcConfig.getTags().entrySet()  ) {
			String keyString = entry.getKey();			
			DataField df = factory.newDataField(keyString, '1', '0');
			@SuppressWarnings("unchecked")
			ArrayList<String> valObject = (ArrayList<String>) entry.getValue();
			String content = new String();
			if (keyString.startsWith("00")) {
				String contentString = fieldContent.getContent(keyString, null);
				if (contentString!=null) {
					record.addVariableField(factory.newControlField(keyString, contentString));
				}
				else {
					String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
					content = String.format("### tag %s at %s ###", keyString, time);
					record.addVariableField(factory.newControlField(keyString, content));
				}
			}
			else {
				for (String subfield : valObject) {
					char code = subfield.charAt(0);
					String contentString = fieldContent.getContent(keyString, String.valueOf(code));
					if (contentString != null) {
						content = contentString;
					} else {
						String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
						content = String.format("### tag %s-code %s at %s ###", keyString, code, time);
					}				 
					df.addSubfield(factory.newSubfield(code, content));
				}
				record.addVariableField(df);
			}
			
		}
		if (withHolding) {
			List<DataField> hx = holding();
			for (DataField dField : hx) {
				record.addVariableField(dField);
			}
		}
		OutputStream out = new FileOutputStream(this.filename);		
		MarcWriter writer = new MarcXmlWriter(out, true);		
		writer.write(record);
		writer.close();		 

	}
	
	/**
	 * This method for practical reason does not read the holding configuration, but only the FieldContent 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<DataField> holding() throws FileNotFoundException, IOException{
		MarcFactory factory = MarcFactory.newInstance();
		List<DataField> res = new ArrayList<DataField>();
		FieldContent fieldContent = new FieldContent("holding.txt");
		Map<String, HashMap<String, String>> fx = fieldContent.getTags();
		for (String key : fieldContent.getTags().keySet()) {
			if (!key.equals("001")) {
				DataField df = factory.newDataField(key, '1', '0');				
				Map<String, String> line = fx.get(key);
				for (String tag : line.keySet()) {
					df.addSubfield(factory.newSubfield(tag.charAt(0), line.get(tag)));										
				}
				res.add(df);
			}			
		}
		return res;		
	}
	
	/**
	 * Merging all templates from sources folder to one sources.xml-file
	 * @throws FileNotFoundException 
	 */
	public static void merge() throws FileNotFoundException {
		String dirString = System.getProperty("user.dir");
		String ofile = dirString + "/output/sources.xml"; 
		OutputStream out = new FileOutputStream(ofile);		
		MarcWriter writer = new MarcXmlWriter(out, true);
    	File folder = new File(dirString + "/output/sources/");
    	File[] listOfFiles = folder.listFiles();
    	
    	for (File file : listOfFiles) {
    		InputStream in = new FileInputStream(file);
            MarcReader reader = new MarcXmlReader(in);
            while (reader.hasNext()) {
    	        Record record = reader.next();
    		writer.write(record);
                }                
            }
    	writer.close();	
	}    		
    	
		
	

}

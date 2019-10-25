package info.rism.muscat.factory;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.DataField;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;

public class MarcxmlBuilder {
	
	public void build(MarcConfig marcConfig, FieldContent fieldContent, String leader) {
		
		MarcFactory factory = MarcFactory.newInstance();
		Record record = factory.newRecord(leader);
		record.addVariableField(factory.newControlField("001", "12883376"));
		//System.out.println(marcConfig.getTags().getClass().getSimpleName());
		for(Map.Entry<String,Object> entry : marcConfig.getTags().entrySet()  ) {
			String keyString = entry.getKey();
			
			DataField df = factory.newDataField(keyString, '1', '0');
			ArrayList<String> valObject = (ArrayList<String>) entry.getValue();
			for (String subfield : valObject) {
				char code = subfield.charAt(0);
				String content = new String();
				//System.out.println(keyString + "$" +  String.valueOf(code));
				String contentString = fieldContent.getContent(keyString, String.valueOf(code));
				//System.out.println(contentString);
				if (contentString != null) {
					content = contentString;
				} else {
					String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
					content = String.format("tag %s-code %s at %s", keyString, code, time);
				}
				 
				df.addSubfield(factory.newSubfield(code, content));
				
			}
			record.addVariableField(df);
		}
		
		
		
		
		//df.addSubfield(factory.newSubfield('c', "Michael Chabon."));
		
				
		MarcWriter writer = new MarcXmlWriter(System.out, true);
		
		writer.write(record);
		writer.close();
		 
				
	}

}

package info.rism.muscat.factory;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

	private final String filename;	

	public MarcxmlBuilder(String filename) {
		String dirString = System.getProperty("user.dir");
		this.filename = dirString + "/" + filename; 
	}

	public void build(MarcConfig marcConfig, FieldContent fieldContent, String leader) throws FileNotFoundException {

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
		OutputStream out = new FileOutputStream(this.filename);		
		MarcWriter writer = new MarcXmlWriter(out, true);		
		writer.write(record);
		writer.close();		 

	}

}

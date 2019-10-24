package info.rism.muscat.factory;


import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.DataField;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;

public class MarcxmlBuilder {
	
	public void build() {
		
		MarcFactory factory = MarcFactory.newInstance();
		Record record = factory.newRecord("00000cam a2200000 a 4500");
		record.addVariableField(factory.newControlField("001", "12883376"));
		DataField df = factory.newDataField("245", '1', '0');
		df.addSubfield(factory.newSubfield('a', "Summerland /"));
		df.addSubfield(factory.newSubfield('c', "Michael Chabon."));
		record.addVariableField(df);
				
		MarcWriter writer = new MarcXmlWriter(System.out, true);
		
		writer.write(record);
		writer.close();
		 
				
	}

}

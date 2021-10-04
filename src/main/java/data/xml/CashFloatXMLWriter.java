package data.xml;

import models.CashFloat;

import java.io.IOException;
import java.util.ArrayList;

/**
 * CashFloatXMLWriter handles writing Cash Float objects to an XML file
 */
public class CashFloatXMLWriter extends XMLWriter {

	/**
	 * Default constructor for CashFloatXMLWriter
	 * @param exportDirPath Path to be exported to
	 * @param exportFileName Filename to be exported to
	 */
	public CashFloatXMLWriter(String exportDirPath, String exportFileName) {
		super(exportDirPath, exportFileName);
	}

	/**
	 * Write a list of Cash Float objects to XML file
	 * @param o Object to be parsed into XML
	 * @throws IOException
	 */
	@Override
	public void writeToXML(Object o) throws IOException {
		writeHeaderTag("CashFloat");
		writeOpeningTag("wad", null);
		writeSimpleTagContent("description", "Cash Float XML File");
		ArrayList<CashFloat> wad = (ArrayList<CashFloat>) o;
		for (CashFloat i : wad) {
			writeSingleCashFloat(i);
		}
		writeClosingTag("wad");
		finishWriting();
	}

	/**
	 * Write a single Cash Float object into XML file
	 * @param o Cash Float object
	 * @throws IOException
	 */
	public void writeSingleCashFloat(Object o) throws IOException {
		if (!(o instanceof CashFloat)) { return; }
		CashFloat i = (CashFloat) o;

		writeOpeningTag("cash", null);
		writeSimpleTagContent("type", i.getType());
		writeSimpleTagContent("denomination", i.getDenomination());
		writeSimpleTagContent("valueInCents", i.getValueInCents());
		writeSimpleTagContent("initialQuantity", i.getQuantity());
		writeClosingTag("cash");

	}

}

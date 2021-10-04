package data.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * XML Writer provides general functions to write XML files
 */
public class XMLWriter {

	/**
	 * Path to be exported to
	 */
	private String exportDirPath;

	/**
	 * XML Filename to be exported to
	 */
	private String exportFileName;

	/**
	 * Writer object that handles all the writing functionalities
	 */
	private FileWriter writer;

	/**
	 * Default constructor for XMLWriter
	 * @param exportDirPath Path where it will be exported to
	 * @param exportFileName Filename where it wil exported to
	 */
	public XMLWriter(String exportDirPath, String exportFileName) {
		this.exportDirPath = exportDirPath;
		this.exportFileName = exportFileName;
		try {
			writer = createXMLFile();
		} catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * Write an object to XML syntax
	 * @param o Object to be parsed into XML
	 * @throws IOException
	 */
	public void writeToXML(Object o) throws IOException { };

	/**
	 * Create an XML file
	 * @return FileWriter object that points to the file
	 * @throws IOException
	 */
	public FileWriter createXMLFile() throws IOException {
		File dir = new File(this.exportDirPath);
		if (!dir.exists())
			dir.mkdirs();

		File file = new File(this.exportDirPath + "/" + this.exportFileName);
		if (file.exists())
			file.delete();

		file.createNewFile();

		return new FileWriter(file);
	}

	/**
	 * Creates an opening tag in XML syntax
	 * @param tagName Name of the tag
	 * @param attributes Attributes of the tag
	 * @throws IOException
	 */
	public void writeOpeningTag(String tagName, HashMap<String, String> attributes) throws IOException {
		String openingTag = "<" + tagName;
		if (attributes != null && !attributes.isEmpty()) {
			for (String attribute : attributes.keySet()) {
				String attrValue = attributes.get(attribute);
				openingTag += " " + attribute + "=\"" + attrValue + "\" ";
			}
		}
		openingTag += ">";
		writer.write(openingTag);
	}

	/**
	 * Writes the content of the tag
	 * @param tagContent Content of the tag
	 * @throws IOException
	 */
	public void writeTagContent(String tagContent) throws IOException {
		writer.write(tagContent);
	}

	/**
	 * Write a simple tag with contents
	 * @param tagName Name of the tag
	 * @param tagContent Content of the tag
	 * @throws IOException
	 */
	public void writeSimpleTagContent(String tagName, Integer tagContent) throws  IOException {
		writeOpeningTag(tagName, null);
		writeTagContent(String.valueOf(tagContent));
		writeClosingTag(tagName);
	}

	/**
	 * Write a simple tag with contents
	 * @param tagName Name of the tag
	 * @param tagContent Content of the tag
	 * @throws IOException
	 */
	public void writeSimpleTagContent(String tagName, String tagContent) throws  IOException {
		writeOpeningTag(tagName, null);
		writeTagContent(tagContent);
		writeClosingTag(tagName);
	}

	/**
	 * Write the closing tag with XML syntax
	 * @param tagName Name of the tag
	 * @throws IOException
	 */
	public void writeClosingTag(String tagName) throws IOException {
		String closingTag = "</" + tagName + ">\n";
		writer.write(closingTag);
	}

	/**
	 * This function should be called when we are done with writing the XML file
	 * @throws IOException
	 */
	public void finishWriting() throws IOException {
		writer.close();
	}

	/**
	 * Write a header tag for the XML file
	 * the DTD file differs depending on the object type passed
	 * @param objectType Name of the object
	 * @throws IOException
	 */
	public void writeHeaderTag(String objectType) throws IOException {
		String PluralName = "";
		String DTDFile = "";
		switch (objectType) {
			case "Ingredient":
				DTDFile = "ingredient.dtd";
				PluralName = "ingredients";
				break;
			case "MenuItem":
				DTDFile = "menu.dtd";
				PluralName = "menus";
				break;
			case "CashFloat":
				DTDFile = "Cash.dtd";
				PluralName = "wad";
				break;
		}

		writer.write(
		"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
			"<!DOCTYPE " + PluralName + " SYSTEM \"" + DTDFile +"\">\n"
		);
	}

}

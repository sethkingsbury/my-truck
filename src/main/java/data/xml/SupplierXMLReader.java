package data.xml;

import models.Supplier;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * SupplierXMLReader provides functions to interact with Supplier XML files
 */
public class SupplierXMLReader extends XMLReader {

	/**
	 * Default constructor for Supplier XML Reader
	 * @param path Path to xml file
	 */
	public SupplierXMLReader(String path) {
		super(path);
	}

	/**
	 * Parses Supplier XML file, returning all the data encoded
	 * @return ArrayList of all Supplier in the file
	 */
	public ArrayList<Supplier> parseXML() {
		String docType = "supplier";
		ArrayList<Supplier> suppliers = new ArrayList<>();

		NodeList nodes = getDocument().getElementsByTagName(docType);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				String name = eElement.getElementsByTagName("name").item(0).getTextContent();
				String contact = eElement.getElementsByTagName("contact").item(0).getTextContent();
				Supplier s = new Supplier(name, contact);
				suppliers.add(s);
			}
		}

		return suppliers;
	}
}

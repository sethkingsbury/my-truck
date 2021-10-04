package data.xml;

import models.CashFloat;
import models.Ingredient;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * CashFloatXMLReader provides functions to interact with CashFloat XML files
 */
public class CashFloatXMLReader extends XMLReader {

	/**
	 * Default constructor for Cash Float XML Reader
	 * @param path Path to xml file
	 */
	public CashFloatXMLReader(String path) {
		super(path);
	}

	/**
	 * Parses Cash Float XML file, returning all the data encoded
	 * @return ArrayList of all Cash in the file
	 */
	public ArrayList<CashFloat> parseXML() {
		String docType = "cash";
		ArrayList<CashFloat> cashFloat = new ArrayList<>();

		NodeList nodes = getDocument().getElementsByTagName(docType);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
                cashFloat.add(parseOneCashFloat(node));
			}
		}

		return cashFloat;
	}

    /**
     * Parses one CashFloat from the XML file
     * @param node Node containing CashFloat data
     * @return CashFloat
     */
    public CashFloat parseOneCashFloat(Node node) {
        Element eElement = (Element) node;
        String type = eElement.getElementsByTagName("type").item(0).getTextContent();
        Integer denomination = Integer.parseInt(eElement.getElementsByTagName("denomination").item(0).getTextContent());
        Integer valueInCents = Integer.parseInt(eElement.getElementsByTagName("valueInCents").item(0).getTextContent());
        Integer initialQuantity = Integer.parseInt(eElement.getElementsByTagName("initialQuantity").item(0).getTextContent());
        CashFloat cf = new CashFloat(type, denomination, valueInCents, initialQuantity);
        return new CashFloat(type, denomination, valueInCents, initialQuantity);
    }

}

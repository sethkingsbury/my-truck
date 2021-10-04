package data.xml;

import models.Ingredient;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * IngredientXMLReader provides functions to interact with Ingredient XML files
 */
public class IngredientXMLReader extends XMLReader {

	/**
	 * Default constructor for Ingredient XML Reader
	 * @param path Path to xml file
	 */
	public IngredientXMLReader(String path) {
		super(path);
	}

	/**
	 * Parses all ingredients found in XML file
	 * @return ArrayList of all ingredients in the file
	 */
	public ArrayList<Ingredient> parseXML() {
		String docType = "ingredient";
		ArrayList<Ingredient> result = new ArrayList<>();

		NodeList nodes = getDocument().getElementsByTagName(docType);
		for(int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Element.ELEMENT_NODE) {
				result.add(parseOneIngredient(node));
			}
		}
		return result;
	}

    /**
     * Parses one ingredient from the XML file
     * @param node Node containing ingredient data
     * @return Ingredient object
     */
	public Ingredient parseOneIngredient(Node node) {
        Element eElement = (Element) node;
        boolean isGF = eElement.getAttribute("isgf").equals("yes") ? true : false;
        boolean isVeg = eElement.getAttribute("isveg").equals("yes") ? true : false;
        boolean isVegan = eElement.getAttribute("isvegan").equals("yes") ? true : false;
        String name = eElement.getElementsByTagName("name").item(0).getTextContent();
        String code = eElement.getElementsByTagName("code").item(0).getTextContent();
		String amount = eElement.getElementsByTagName("amount").item(0).getTextContent();
        String measurement = eElement.getAttribute("unit");
        String price = eElement.getElementsByTagName("price").item(0).getTextContent();
        return new Ingredient(code, name, measurement, isGF, isVeg, isVegan, Integer.parseInt(amount), Integer.parseInt(price));
    }

}

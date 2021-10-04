package data.xml;

import models.MenuItem;
import models.Money;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MenuItemXMLReader provides functions to interact with MenuItem XML files
 */
public class MenuItemXMLReader extends XMLReader {

	/**
	 * Default constructor for Menu XML Reader
	 * @param path
	 */
	public MenuItemXMLReader(String path) {
		super(path);
	}

	/**
	 * Parses MenuItem XML file, returning all the data encoded
	 * @return ArrayList of all menu item in the file
	 */
	public ArrayList<MenuItem> parseXML() {
		String docType = "menu";
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

		NodeList nodes = getDocument().getElementsByTagName(docType);
		for(int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Element.ELEMENT_NODE) {
				menuItems.add(parseOneMenuItem(node));
			}
		}

		return menuItems;
	}

    /**
     * Parses one MenuItem from the XML file
     * @param node Node containing MenuItem data
     * @return MenuItem object
     */
    public MenuItem parseOneMenuItem(Node node) {
        Element eElement = (Element) node;
        String name = eElement.getElementsByTagName("name").item(0).getTextContent();
        String code = eElement.getElementsByTagName("code").item(0).getTextContent();
        String category = eElement.getElementsByTagName("category").item(0).getTextContent();

		NodeList ingredientNodes = eElement.getElementsByTagName("ingredient");
		Money price = new Money(Integer.valueOf(eElement.getElementsByTagName("price").item(0).getTextContent()));
		MenuItem m = new MenuItem(code, name, price, category);
		m.setIngredientsByCode(getmenuItemIngredients(ingredientNodes));

        return m;
    }

	/**
	 * Get the Ingredients associated with a Menu Item
	 * @param nodes ingredient nodes
	 * @return Map of Menu Item codes and their amount
	 */
	public HashMap<String, Integer> getmenuItemIngredients(NodeList nodes) {
		HashMap<String, Integer> ingredients = new HashMap<>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Element.ELEMENT_NODE) {
				Element eElement = (Element) node;
				String code = eElement.getElementsByTagName("code").item(0).getTextContent();
				int amount = Integer.valueOf(eElement.getElementsByTagName("amount").item(0).getTextContent());
				ingredients.put(code, amount);
			}
		}
		return ingredients;
	}



}

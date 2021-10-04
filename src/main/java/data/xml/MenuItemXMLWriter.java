package data.xml;

import models.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * MenuItemXMLWriter handles writing MenuItem objects to an XML file
 */
public class MenuItemXMLWriter extends XMLWriter {

	/**
	 * Default constructor for MenuItemXMLWriter
	 * @param exportDirPath Path to be exported to
	 * @param exportFileName Filename to be exported to
	 */
	public MenuItemXMLWriter(String exportDirPath, String exportFileName) {
		super(exportDirPath, exportFileName);
	}

	/**
	 * Write list of Menu Item objects to XML
	 * @param o Object to be parsed into XML
	 * @throws IOException
	 */
	@Override
	public void writeToXML(Object o) throws IOException {
		writeHeaderTag("MenuItem");
		writeOpeningTag("menus", null);
		writeSimpleTagContent("description", "Menu Item XML File");
		ArrayList<MenuItem> menuItems = (ArrayList<MenuItem>) o;
		for (MenuItem i : menuItems) {
			writeSingleMenuItem(i);
		}
		writeClosingTag("menus");
		finishWriting();
	}

	/**
	 * Write a single Menu Item object into XML syntax
	 * @param o Menu Item to be exported
	 * @throws IOException
	 */
	public void writeSingleMenuItem(Object o) throws IOException {
		if (!(o instanceof MenuItem)) { return; }
		MenuItem i = (MenuItem) o;

		i.setIngredientsToIngredientByCode();
		HashMap<String, Integer> ingredients = i.getIngredientsByCode();

		writeOpeningTag("menu", null);
		writeSimpleTagContent("code", i.getCode());
		writeSimpleTagContent("name", i.getName());
		writeSimpleTagContent("price", i.getPrice().getAsCents());
		writeSimpleTagContent("category", i.getCategory());
		writeOpeningTag("ingredients", null);
		writeMenuItemIngredients(ingredients);
		writeClosingTag("ingredients");
		writeClosingTag("menu");

	}

	/**
	 * Write Ingredients associated with a Menu Item to XML syntax
	 * @param ingredients Ingredients of Menu Item
	 * @throws IOException
	 */
	private void writeMenuItemIngredients(HashMap<String, Integer> ingredients) throws IOException {
		for (String code : ingredients.keySet()) {
			writeOpeningTag("ingredient", null);
			writeSimpleTagContent("code", code);
			writeSimpleTagContent("amount", ingredients.get(code));
			writeClosingTag("ingredient");
		}
	}

}

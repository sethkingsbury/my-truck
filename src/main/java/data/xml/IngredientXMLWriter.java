package data.xml;

import models.Ingredient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * IngredientXMLWriter handles writing Ingredient objects to an XML file
 */
public class IngredientXMLWriter extends XMLWriter {

	/**
	 * Default constructor for IngredientXMLWriter
	 * @param exportDirPath Path to be exported to
	 * @param exportFileName Filename to be exported to
	 */
	public IngredientXMLWriter(String exportDirPath, String exportFileName) {
		super(exportDirPath, exportFileName);
	}

	/**
	 * Write a list of Ingredient objects to XML file
	 * @param o Object to be parsed into XML
	 * @throws IOException
	 */
	@Override
	public void writeToXML(Object o) throws IOException {
		writeHeaderTag("Ingredient");
		writeOpeningTag("ingredients", null);
		writeSimpleTagContent("description", "Ingredients XML File");
		ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) o;
		for (Ingredient i : ingredients) {
			writeSingleIngredient(i);
		}
		writeClosingTag("ingredients");
		finishWriting();
	}

	/**
	 * Write a single Ingredient to XML syntax
	 * @param o Ingredient object
	 * @throws IOException
	 */
	public void writeSingleIngredient(Object o) throws IOException {
		if (!(o instanceof Ingredient)) { return; }
		Ingredient i = (Ingredient) o;

		HashMap<String, String> attrs = new HashMap<>();
		if (i.getGlutenFree())
			attrs.put("isgf", "yes");

		if (i.getVegan())
			attrs.put("isvegan", "yes");

		if (i.getVegetarian())
			attrs.put("isveg", "yes");

		attrs.put("unit", i.getQuantityMeasuredIn());

		writeOpeningTag("ingredient", attrs);
		writeSimpleTagContent("code", i.getCode());
		writeSimpleTagContent("name", i.getName());
		writeSimpleTagContent("amount", i.getAmount());
		writeSimpleTagContent("price", i.getPrice().getAsCents());
		writeClosingTag("ingredient");

	}

}

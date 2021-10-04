package data.xml;

import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

/**
 * XML Reader provides general functions to interact with XML files
 */
public class XMLReader {

	private Document document;

	private DocumentBuilderFactory factory;

    private DocumentBuilder builder;

    /**
     * Path to the XML file
     */
	private String path;

    /**
     * Flag for document readability
     */
    private Boolean documentIsParseable = true;

	/**
	 * Default constructor for XMLReader
	 * @param path Location of xml file
	 */
	public XMLReader(String path) {
		this.path = path;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println(e);
		}

        builder.setErrorHandler(new XMLErrorHandler(System.err));
        try {
            document = builder.parse(new File(this.path));
            document.getDocumentElement().normalize();
        } catch (IOException | SAXException e) {
            this.documentIsParseable = false;
        }
	}

	/**
	 * Get the top element tag name, Useful for determining the
	 * content of xml file
	 * @return Top element tag name
	 */
	public String getDocumentContent() {
		Element root = document.getDocumentElement();
		String docType = root.getTagName();
		return docType;
	}

	public Document getDocument() {
		return document;
	}

	/**
	 * Parses XML file and gets all the data inside element `tagname`
     * @param document
	 * @return List of all the data found
	 */
	public ArrayList parseXML(Document document) {
		return null;
	}

    /**
     * Get the filetype of XML
     * @return
     */
	public String getFileType() {
        return document.getDocumentElement().getTagName();
    }

	/**
	 * Checks if document throws any error when read
	 * @return True if any error was thrown, False otherwise
	 */
	public Boolean getDocumentIsParseable() { return documentIsParseable; }
}

package data.xml;

import models.User;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.Password;

import java.util.ArrayList;

/**
 * UserXMLReader provides functions to interact with User XML files
 */
public class UserXMLReader extends XMLReader {

    /**
     * Default constructor for User XML Reader
     * @param path Path to xml file
     */
    public UserXMLReader(String path) {
        super(path);
    }

    /**
     * Parses User XML file, returning all the data encoded
     * @return ArrayList of all User in the file
     */
    public ArrayList<User> parseXML() {
        String docType = "user";
        ArrayList<User> users = new ArrayList<>();

        NodeList nodes = getDocument().getElementsByTagName(docType);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                users.add(parseOneUser(node));
            }
        }

        return users;
    }

    /**
     * Parses one User from the XML file
     * @param node Node containing User data
     * @return User object
     */
    public User parseOneUser(Node node) {
        Element eElement = (Element) node;
        String username = eElement.getElementsByTagName("username").item(0).getTextContent();
        String plaintext = eElement.getElementsByTagName("password").item(0).getTextContent();
        int accountType = Integer.parseInt(eElement.getAttribute("accountType"));
        return new User(username, plaintext, accountType);
    }
}

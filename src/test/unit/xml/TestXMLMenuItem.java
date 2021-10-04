package unit.xml;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.xml.MenuItemXMLReader;
import models.MenuItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestXMLMenuItem {
    @Test
    void testReadSupplierXML() {
        MenuItemXMLReader m = new MenuItemXMLReader("src/main/resources/data/testing/testMenuItems.xml");
        ArrayList<MenuItem> menuItems = m.parseXML();
        MenuItem m1 = menuItems.get(0);
        assertEquals(m1.getName(), "Beef Burger");
        assertEquals(m1.getCode(),"BBurger");
        assertEquals(m1.getPrice().toString(), "$0.6");
        assertEquals(m1.getCategory(), "Main Course");
    }

}

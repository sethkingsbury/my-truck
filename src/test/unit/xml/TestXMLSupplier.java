package unit.xml;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.xml.SupplierXMLReader;
import models.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestXMLSupplier {
    @Test
    void testReadSupplierXML() {
        SupplierXMLReader s = new SupplierXMLReader("src/main/resources/data/testing/testSuppliers.xml");
        ArrayList<Supplier> suppliers = s.parseXML();
        Supplier s1 = suppliers.get(0);
        assertEquals(s1.getName(), "Josh");
        assertEquals(s1.getContact(), "0800838383");
    }

}

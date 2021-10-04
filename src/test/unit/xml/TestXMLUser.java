package unit.xml;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.xml.UserXMLReader;
import models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestXMLUser {
    @Test
    void testReadUserXML() {
        UserXMLReader u = new UserXMLReader("src/main/resources/data/testing/testUsers.xml");
        ArrayList<User> users = u.parseXML();
        User u1 = users.get(0);
        assertEquals(u1.getUsername(), "ownerbob");
        assertNotEquals(u1.getPassword(), "owner");
    }
}

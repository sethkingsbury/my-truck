package unit;


import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import org.junit.AfterClass;
import org.junit.BeforeClass;


public class BaseExam {
    public static ConnectionSource connection;

    @BeforeClass
    public static void setup() {
        setupDBConnection();
    }

    @AfterClass
    public static void tearDown() {

    }

    private static void setupDBConnection() {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        System.setProperty("com.j256.ormlite.logger.level", "ERROR");
    }

}
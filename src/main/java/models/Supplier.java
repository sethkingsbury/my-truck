package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Model for Supplier
 */
@DatabaseTable(tableName = "SUPPLIER")
public class Supplier {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false, unique = true)
    private String contact;

    /**
     * Empty constructor to keep ORMLite happy.
     */
    public Supplier() {
    }

    /**
     * The constructor for suppliers
     * @param name of the supplier
     * @param contact the contact phone number for the supplier
     */
    public Supplier(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * toString method for Supplier
     * @return String representation for Supplier
     */
    public String toString() {
        String template = "";
        template += "Supplier name: " + this.name + "\n";
        template += "Supplier contact: " + this.contact + "\n";
        return template;
    }

    public void setId(int id) {
        this.id = id;
    }
}

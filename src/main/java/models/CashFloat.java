package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Model for Cash Float
 */
@DatabaseTable(tableName = "CASHFLOAT")
public class CashFloat {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField (uniqueCombo = true)
    private String type;

    @DatabaseField (uniqueCombo = true)
    private int denomination;

    @DatabaseField()
    private int valueInCents;

    @DatabaseField()
    private int quantity;


    /**
     * Empty constructor to keep ORMLite happy.
     */
    public CashFloat() { }

    /**
     * Constructor for the CashFloat class
     * @param type of currency, either a "Coin" or "Note"
     * @param denomination the amount on the cash object
     * @param valueInCents the value of the cash object in cents
     * @param quantity the number of cash object in the cash float/register
     */
    public CashFloat(String type, int denomination, int valueInCents, int quantity) {
        this.type = type;
        this.denomination = denomination;
        this.valueInCents = valueInCents;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public int getValueInCents() {
        return valueInCents;
    }

    public void setValueInCents(int valueInCents) {
        this.valueInCents = valueInCents;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

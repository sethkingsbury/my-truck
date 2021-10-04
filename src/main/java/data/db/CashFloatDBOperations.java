package data.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.CashFloat;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handles operations related to Cash Float database table
 */
public class CashFloatDBOperations {

    /**
     * Connection to the database
     */
    private ConnectionSource connectionSource;

    /**
     * Interface that handles queries to the CASHFLOAT table
     */
    private Dao<CashFloat, Integer> cashFloatDao;

    /**
     * Default constructor for CashFloatDBOperations
     * @param connectionSource connection to the database
     */
    public CashFloatDBOperations(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            cashFloatDao = DaoManager.createDao(connectionSource, CashFloat.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the total amount of money into cents, this is done by adding every quantity * the vlaue in cents
     * to a tally, which returns the full amount of cash stored in the database in an integer tally
     * @return returns the total amount in cents
     * @throws SQLException -
     */
    public Integer getFloatCents() throws SQLException {
        ArrayList<CashFloat> cashFloats = new ArrayList<>(cashFloatDao.queryForAll());
        int tally = 0;
        for (CashFloat c: cashFloats) {
            tally += c.getQuantity() * c.getValueInCents();
        }
        return tally;
    }


    /**
     * get Quantity returns an integer of how many of a specific type/denomination are stored in the float
     * this is used to determine if there is the cash on hand for whatever reason.
     * @param type of money (coin/note)
     * @param denomination the value of the type
     * @return
     * @throws SQLException
     */
    public Integer getQuantity(String type, Integer denomination) throws SQLException {
        CashFloat quantitytype = cashFloatDao.queryBuilder()
                .where()
                .eq("type", type)
                .and()
                .eq("denomination", denomination)
                .queryForFirst();
        return quantitytype.getQuantity();
    }

    /**
     * setQuantity updates the quantity, according to the value and amount inputted. this will
     * replace the quantity of whatever denominaton is specified (through valueInCents), by the Integer given
     *
     * @param valueInCents the unique identifier of the money were looking
     * @param amount how much were adding
     * @throws SQLException -
     */
    public void setQuantity(String valueInCents, Integer amount) throws SQLException {
        UpdateBuilder<CashFloat, Integer> updateBuilder = cashFloatDao.updateBuilder();
        updateBuilder.updateColumnValue("quantity", amount);
        updateBuilder.where().eq("valueInCents", valueInCents);
        updateBuilder.update();
    }

    /**
     * getFloatCents, returns the specific cent value of whatever denomination is specified, through the type and
     * denomination given. this is used for change calculations we are doing operations on money in an
     * integer (displayed by cents, as opposed to dollars)
     * @param type of money (coin/note)
     * @param denomination the value of the type
     * @return returns the value
     * @throws SQLException -
     */
    public Integer getFloatCents(String type, Integer denomination) throws SQLException {
        CashFloat floatCent = cashFloatDao.queryBuilder()
                .where()
                .eq("type", type)
                .and()
                .eq("denomination", denomination)
                .queryForFirst();
        return floatCent.getValueInCents();
    }

    /**
     *
     *  Checks if CashFloat already existed in the database
     *
     * @param cashFloat takes the float
     * @return returns true if != null
     * @throws SQLException -
     */
    public boolean checkDuplicateCashFloat(CashFloat cashFloat) throws SQLException {
        return cashFloatDao.queryBuilder()
                .where()
                .eq("type", cashFloat.getType())
                .and()
                .eq("denomination", cashFloat.getDenomination())
                .queryForFirst() != null;
    }

    /**
     * AddCashToCashFloat creates a new cashFloat if it hasnt been done before
     * @param cashFloat takes the cashFloat
     * @throws SQLException -
     */
    public void addCashToCashFloat(CashFloat cashFloat) throws SQLException {
        if (!checkDuplicateCashFloat(cashFloat))
            cashFloatDao.create(cashFloat);
    }

    /**
     * iterates through every item in cashwad, adding using AddCashToCashFloat
     * @param wad all of the floats, from the xml reader
     * @throws SQLException -
     */
    public void addWadToCashFloat(Iterable<CashFloat> wad) throws SQLException {
        for (CashFloat cashFloat : wad) {
            addCashToCashFloat(cashFloat);
        }
    }

    /**
     * increaseQuantity takes the type, denomination and a set quantity, will find the correct cash item
     * through the type and denomination and add the specified amount accordingly.
     * @param type of money (coin/note)
     * @param denomination the value of the type
     * @param quantityInc how much are we increasing the quantity by
     * @throws SQLException -
     */
    public void increaseQuantity(String type, Integer denomination, Integer quantityInc) throws SQLException {
        if (quantityInc < 0) {
            throw new SQLException("increase amount must be at least 0");
        }

        UpdateBuilder<CashFloat, Integer> quantityUpdater = cashFloatDao.updateBuilder();
        quantityUpdater.where().eq("type", type).and().eq("denomination", denomination);
        quantityUpdater.updateColumnValue("quantity", getQuantity(type, denomination) + quantityInc);
        quantityUpdater.update();
    }

    /**
     *using type of money and denomination, figure out what items we will remove, then remove the quantity specified
     * off the quantity of the cash item found
     * @param type of money (coin/note)
     * @param denomination the value of the type
     * @param quantityDec amount we are taking off the quantity
     * @throws SQLException
     */
    public void decreaseQuantity(String type, Integer denomination, Integer quantityDec) throws SQLException {
        if (quantityDec > 0) {
            throw new SQLException("decrease amount must be less than 0");
        }

        UpdateBuilder<CashFloat, Integer> quantityUpdater = cashFloatDao.updateBuilder();
        quantityUpdater.where().eq("type", type).and().eq("denomination", denomination);
        quantityUpdater.updateColumnValue("quantity", getQuantity(type, denomination) + quantityDec);
        quantityUpdater.update();
    }


    /**
     * Get all denominations stored in the database
     * @return List of all denominations in the DB
     * @throws SQLException
     */
    public ArrayList<CashFloat> getAllDenom() throws SQLException {
        ArrayList<CashFloat> showAll = new ArrayList<>(cashFloatDao.queryForAll());
        return showAll;
    }
}

package data.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * RecipeIngredientDBOperations provides functions to interact with
 * the RECIPEINGREDIENT table
 */
public class SupplierDBOperations {

    /**
     * Connection to the database
     */
    private ConnectionSource connectionSource;

    /**
     * Interface that handles queries to the SUPPLIER table
     */
    private Dao<Supplier, Integer> supplierDao;

    /**
     * Default constructor for SupplierDBOperations
     * @param connectionSource
     */
    public SupplierDBOperations(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            supplierDao = DaoManager.createDao(connectionSource, Supplier.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a single supplier into database
     * @param supplier Supplier
     * @throws SQLException
     */
    public void insertSupplier(Supplier supplier) throws SQLException {
        supplierDao.create(supplier);
    }

    /**
     * Updates the currently found Supplier in the database with new fields
     * @param supplier Queried supplier from the database that needs to be changed
     * @throws SQLException
     */
    public void updateSupplier(Supplier supplier) throws SQLException {
        UpdateBuilder<Supplier, Integer> updateBuilder = supplierDao.updateBuilder();
        updateBuilder.updateColumnValue("name", supplier.getName());
        updateBuilder.updateColumnValue("contact", supplier.getContact());
        updateBuilder.where().eq("id", supplier.getId());
        updateBuilder.update();
    }

    /**
     * Updates the currently found Supplier in database with new name
     * @param name Old supplier name
     * @param newName New supplier name
     * @throws SQLException
     */
    public void updateSupplierNameByName(String name, String newName) throws SQLException {
        UpdateBuilder<Supplier, Integer> updateBuilder = supplierDao.updateBuilder();
        updateBuilder.updateColumnValue("name", newName);
        updateBuilder.where().eq("name", name);
        updateBuilder.update();
    }

    /**
     * Updates the currently found Supplier in database with new contact
     * @param name Name of supplier
     * @param contact New supplier contact
     * @throws SQLException
     */
    public void updateSupplierContactByName(String name, String contact) throws SQLException {
        UpdateBuilder<Supplier, Integer> updateBuilder = supplierDao.updateBuilder();
        updateBuilder.updateColumnValue("contact", contact);
        updateBuilder.where().eq("name", name);
        updateBuilder.update();
    }

    /**
     * Get all suppliers in database
     * @return ArrayList of all suppliers in database
     * @throws SQLException
     */
    public ArrayList<Supplier> getAllSuppliers() throws SQLException {
        ArrayList<Supplier> suppliers = new ArrayList<Supplier>(supplierDao.queryForAll());
        return suppliers;
    }

    /**
     * Get a single supplier by their name
     * @param name Name of supplier
     * @return Supplier found
     * @throws SQLException
     */
    public Supplier getSupplier(String name) throws SQLException {
        ArrayList<Supplier> suppliers = new ArrayList<Supplier>(supplierDao.queryBuilder().where().eq("name", name).query());
        if (suppliers.size() != 0)
            return suppliers.get(0);

        return null;
    }

    /**
     * Returns an Supplier object from the database by its ID
     * @param id ID of the supplier
     * @return Supplier associated with that ID
     * @throws SQLException
     */
    public Supplier getSupplierbyID(Integer id) throws SQLException {
        ArrayList<Supplier> suppliers = new ArrayList<Supplier>(supplierDao.queryBuilder().where().eq("id", id).query());
        if (suppliers.size() != 0)
            return suppliers.get(0);

        return null;
    }



    /**
     * Remove a single supplier by name
     * @param name Name of supplier
     * @throws SQLException
     */
    public void removeSupplierByName(String name) throws SQLException {
        DeleteBuilder<Supplier, Integer> deleteBuilder = supplierDao.deleteBuilder();
        deleteBuilder.where().eq("name", name);
        deleteBuilder.delete();
    }

    /**
     * Checks if supplier already exists in database
     * @param supplier Supplier
     * @return True if exists, false otherwise
     * @throws SQLException
     */
    public boolean checkDuplicateSupplier(Supplier supplier) throws SQLException {
        ArrayList<Supplier> dupSuppliers = new ArrayList<>(supplierDao.queryForEq("contact", supplier.getContact()));

        return dupSuppliers.size() > 0;
    }

    public boolean checkDuplicateSupplier(Integer id) throws SQLException {
        return supplierDao.queryForId(id) != null;
    }

    public void insertOrUpdateSupplier(Supplier supplier) throws SQLException {
        if (checkDuplicateSupplier(supplier.getId())) {
            updateSupplier(supplier);
        } else {
            insertSupplier(supplier);
        }
    }

    /**
     * Remove Supplier from database by its id
     * @param id Id to be deleted
     * @throws SQLException
     */
    public void deleteSupplier(Integer id) throws SQLException {
        supplierDao.deleteById(id);
    }

    /**
     * Returns an ArrayList list of supplier names
     * @throws SQLException
     */
    public ArrayList<String> getAllSupplierNames() throws SQLException {
        ArrayList<Supplier> results = new ArrayList(supplierDao.queryBuilder()
                .distinct()
                .selectColumns("name").query());

        ArrayList<String> codes = new ArrayList<>();
        results.forEach( (result) -> codes.add(result.getName()));
        return codes;
    }


}

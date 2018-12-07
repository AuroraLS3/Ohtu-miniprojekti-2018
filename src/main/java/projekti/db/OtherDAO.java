package projekti.db;

import projekti.domain.Other;
import projekti.domain.Other.Properties;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projekti.domain.Property;

/**
 * Data Access Object for Others.
 *
 * @author Rsl1122
 */
public class OtherDAO implements Dao<Other, Integer> {

    private static final String TABLE_NAME = "RECOMMENDATION";

    private final DatabaseManager databaseManager;

    /**
     * Constructor.
     *
     * @param databaseManager Required DatabaseManager for getting Connections.
     */
    public OtherDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Fetch all others from the database.
     *
     * @return List of others, empty if none are found.
     */
    @Override
    public List<Other> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE='OTHER'";
        try (Connection connection = databaseManager.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()) {
            return readOthersFrom(results);
        } catch (SQLException e) {
            // Unchecked exception is thrown if SQL error occurs during closing or execution.
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Read others from database using ResultSet.
     *
     * @param results the ResultSet to be used
     * @return a list of others defined in the given ResultSet.
     */
    private List<Other> readOthersFrom(ResultSet results) throws SQLException {
        List<Other> others = new ArrayList<>();
        while (results.next()) {
            Integer id = results.getInt("ID");
            Other other = new Other(
                    results.getString("NAME"),
                    results.getString("URL"),
                    results.getString("DESCRIPTION")
            );
            other.setID(id);
            others.add(other);
        }
        return others;
    }

    /**
     * Add a new other to the database.
     *
     * @param other the given other
     * @return a new Other fetched from the database
     */
    @Override
    public Other create(Other other) throws SQLException {
        int otherId = -1;

        if (!tooLongPropertyFound(other)) {

            String sql = "INSERT INTO " + TABLE_NAME + " (NAME, URL, TYPE, DESCRIPTION) "
                    + "VALUES (?, ?, ?, ?)";
            try (Connection connection = databaseManager.connect()) {
                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, other.getProperty(Properties.TITLE).orElse(null));
                stmt.setString(2, other.getProperty(Properties.URL).orElse(null));
                stmt.setString(3, other.getType());
                stmt.setString(4, other.getProperty(Properties.DESCRIPTION).orElse(""));
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    otherId = rs.getInt(1);
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                // Unchecked exception is thrown if SQL error occurs during closing or execution.
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return findOne(otherId);
    }

    /**
     * Searches for a specific other from the database by the given key/id.
     *
     * @param key the other's primary key
     * @return a new Other object fetched from the database based on the key
     */
    @Override
    public Other findOne(Integer key) throws SQLException {
        try (Connection conn = databaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT ID, NAME, URL, DESCRIPTION FROM " + TABLE_NAME + " WHERE ID = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();

            if (!result.next()) {
                return null;
            }
            Integer id = result.getInt("ID");
            Other other = new Other(
                    result.getString("NAME"),
                    result.getString("URL"),
                    result.getString("DESCRIPTION")
            );
            other.setID(id);
            return other;
        }
    }

    /**
     * Update other information in the database
     *
     * @param object the other that is updated
     * @return the other
     * @throws SQLException
     */
    @Override
    public boolean update(Other object) throws SQLException {
        if (!tooLongPropertyFound(object)) {

            try (Connection conn = databaseManager.connect()) {
                String statementString = "UPDATE RECOMMENDATION "
                        + "SET NAME = ?, "
                        + "URL = ?, "
                        + "TYPE = ?, "
                        + "DESCRIPTION = ? "
                        + "WHERE RECOMMENDATION.ID = ? ;";
                PreparedStatement stmnt = conn.prepareStatement(statementString);
                stmnt.setString(1, object.getProperty(Properties.TITLE).orElse(null));
                stmnt.setString(2, object.getProperty(Properties.URL).orElse(null));
                stmnt.setString(3, object.getType());
                stmnt.setString(4, object.getProperty(Properties.DESCRIPTION).orElse(""));
                stmnt.setInt(5, object.getProperty(Properties.ID).orElse(null));
                int count = stmnt.executeUpdate();
                if (count == 0) {
                    Logger.getGlobal().log(Level.WARNING, "No matches for update in the db");
                    return false;
                }

                return true;
            }
        }
        return false;
    }

    /*
     * Deletes a specific other from the database by the given key/id.
     *
     * @param key the other's primary key
     */
    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = databaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE ID = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            stmt.close();
        }
    }

    /**
     * Checks if property content is too long considering column sizes.
     *
     * @param the name and content of the property to be checked
     * @return true if property is too long, false if note
     * @throws SQLException
     */
    public boolean propertyTooLong(String name, String property) throws SQLException {
        boolean tooLong = false;
        try (Connection conn = databaseManager.connect()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet column = meta.getColumns(null, null, "RECOMMENDATION", name);
            while (column.next()) {
                if (property.length() > column.getInt("COLUMN_SIZE")) {
                    tooLong = true;
                    System.out.println(name + " should not be more than " + column.getInt("COLUMN_SIZE") + " characters long");
                }
            }
            column.close();
        }
        return tooLong;
    }

    /**
     * Goes through a recommendation's properties and checks if any of the
     * properties' content is too long considering column sizes.
     *
     * @param the recommendation object to be checked
     * @return true if any of the properties in the property list is too long,
     * false if not
     * @throws SQLException
     */
    @Override
    public boolean tooLongPropertyFound(Other other) throws SQLException {
        List<Property> props = other.getProperties();
        boolean tooLong = false;
        for (int i = 0; i < props.size(); i++) {
            Property p = props.get(i);
            if (!p.getName().equals("ID") && propertyTooLong(p.getName(), other.getProperty(p).toString())) {
                tooLong = true;
            }
        }
        return tooLong;
    }

}

package projekti.db;

import projekti.domain.Book;
import projekti.domain.Book.Properties;
import projekti.domain.CommonProperties;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projekti.domain.Blog;
import projekti.domain.Other;
import projekti.domain.Property;
import projekti.domain.Recommendation;

/**
 * Data Access Object for Books.
 *
 * @author Rsl1122
 */
public class RecommendationDAO implements Dao<Recommendation, Integer> {

    private static final String TABLE_NAME = "RECOMMENDATION";

    private final DatabaseManager databaseManager;

    /**
     * Constructor.
     *
     * @param databaseManager Required DatabaseManager for getting Connections.
     */
    public RecommendationDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Fetch all recommendations of certain type from the database.
     *
     * @return List of recommendations of given type, empty if none are found.
     */
    @Override
    public List<Recommendation> findAll(String type) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE='" + type + "'";
        try (Connection connection = databaseManager.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()) {
            return readRecommendationsFrom(results);
        } catch (SQLException e) {
            // Unchecked exception is thrown if SQL error occurs during closing or execution.
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private Recommendation readARecommendationFrom(ResultSet result) throws SQLException {
        Integer id = result.getInt("ID");
        switch (result.getString("TYPE")) {
            case "BOOK":
                Book book = newBook(result);
                book.setID(id);
                return book;
            case "BLOG":
                Blog blog = newBlog(result);
                blog.setID(id);
                return blog;
            case "OTHER":
                Other other = newOther(result);
                other.setID(id);
                return other;
            default:
                return null;
        }
    }

    private Book newBook(ResultSet result) throws SQLException {
        return new Book(
                result.getString("AUTHOR"),
                result.getString("NAME"),
                result.getString("ISBN"),
                result.getString("URL"),
                result.getString("DESCRIPTION")
        );
    }

    private Blog newBlog(ResultSet result) throws SQLException {
        return new Blog(
                result.getString("NAME"),
                result.getString("URL"),
                result.getString("DESCRIPTION")
        );
    }

    private Other newOther(ResultSet result) throws SQLException {
        return new Other(
                result.getString("NAME"),
                result.getString("URL"),
                result.getString("DESCRIPTION")
        );
    }

    /**
     * Read recommendations from database using ResultSet.
     *
     * @param results the ResultSet to be used
     * @return a list of recommendations defined in the given ResultSet.
     */
    private List<Recommendation> readRecommendationsFrom(ResultSet results) throws SQLException {
        List<Recommendation> recommendations = new ArrayList<>();
        while (results.next()) {
            Recommendation r = readARecommendationFrom(results);
            recommendations.add(r);
        }
        return recommendations;
    }

    /**
     * Add a new recommendation to the database.
     *
     * @param recommendation the given recommendation
     * @return a new Recommendation fetched from the database
     */
    @Override
    public Recommendation create(Recommendation r) throws SQLException {
        int id = -1;

        if (!tooLongPropertyFound(r)) {

            String sql = "INSERT INTO " + TABLE_NAME + " (AUTHOR, NAME, ISBN, TYPE, URL, DESCRIPTION) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection connection = databaseManager.connect()) {
                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, r.getProperty(Properties.AUTHOR).orElse(null));
                stmt.setString(2, r.getProperty(Properties.TITLE).orElse(null));
                stmt.setString(3, r.getProperty(Properties.ISBN).orElse(null));
                stmt.setString(4, r.getType());
                stmt.setString(5, r.getProperty(Properties.URL).orElse(null));
                stmt.setString(6, r.getProperty(Properties.DESCRIPTION).orElse(""));
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                // Unchecked exception is thrown if SQL error occurs during closing or execution.
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return findOne(id);
    }

    /**
     * Searches for a specific recommendation from the database by the given
     * key/id.
     *
     * @param key the recommendation's primary key
     * @return a new Recommendation object fetched from the database based on
     * the key
     */
    @Override
    public Recommendation findOne(Integer key) throws SQLException {
        try (Connection conn = databaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?");
            stmt.setInt(1, key);
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            Recommendation one = readARecommendationFrom(result);
            result.close();
            return one;
        }
    }

    /**
     * Update recommendation information in the database
     *
     * @param object the recommendation that is updated
     * @return the recommendation
     * @throws SQLException
     */
    @Override
    public boolean update(Recommendation object) throws SQLException {
        if (!tooLongPropertyFound(object)) {
            try (Connection conn = databaseManager.connect()) {
                String statementString = "UPDATE RECOMMENDATION "
                        + "SET AUTHOR = ?, "
                        + "NAME = ?, "
                        + "ISBN = ?, "
                        + "TYPE = ?, "
                        + "URL = ?, "
                        + "DESCRIPTION = ? "
                        + "WHERE RECOMMENDATION.ID = ? ;";
                PreparedStatement stmnt = conn.prepareStatement(statementString);
                stmnt.setString(1, object.getProperty(Properties.AUTHOR).orElse(null));
                stmnt.setString(2, object.getProperty(Properties.TITLE).orElse(null));
                stmnt.setString(3, object.getProperty(Properties.ISBN).orElse(null));
                stmnt.setString(4, object.getType());
                stmnt.setString(5, object.getProperty(Properties.URL).orElse(null));
                stmnt.setString(6, object.getProperty(Properties.DESCRIPTION).orElse(""));
                stmnt.setInt(7, object.getProperty(Properties.ID).orElse(null));
                int count = stmnt.executeUpdate();
                stmnt.close();
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
     * Deletes a specific recommendation from the database by the given key/id.
     *
     * @param key the recommendation's primary key
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
     * @param the recommendation to be checked
     * @return true if any of the properties in the property list is too long,
     * false if not
     * @throws SQLException
     */
    @Override
    public boolean tooLongPropertyFound(Recommendation r) throws SQLException {
        List<Property> props = r.getProperties();
        boolean tooLong = false;
        for (int i = 0; i < props.size(); i++) {
            Property p = props.get(i);
            if (!p.getName().equals("ID") && propertyTooLong(p.getName(), r.getProperty(p).toString())) {
                tooLong = true;
            }
        }
        return tooLong;
    }

}

package projekti.db;

import projekti.domain.Blog;
import projekti.domain.Blog.Properties;

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
 * Data Access Object for Blogs.
 *
 * @author Rsl1122
 */
public class BlogDAO implements Dao<Blog, Integer> {

    private static final String TABLE_NAME = "RECOMMENDATION";

    private final DatabaseManager databaseManager;

    /**
     * Constructor.
     *
     * @param databaseManager Required DatabaseManager for getting Connections.
     */
    public BlogDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Fetch all blogs from the database.
     *
     * @return List of blogs, empty if none are found.
     */
    @Override
    public List<Blog> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE='BLOG'";
        try (Connection connection = databaseManager.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()) {
            return readBlogsFrom(results);
        } catch (SQLException e) {
            // Unchecked exception is thrown if SQL error occurs during closing or execution.
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private Blog readABlogFrom(ResultSet results) throws SQLException {
        Integer id = results.getInt("ID");
        Blog blog = new Blog(
                results.getString("NAME"),
                results.getString("URL"),
                results.getString("DESCRIPTION")
        );
        blog.setID(id);
        return blog;
    }

    /**
     * Read blogs from database using ResultSet.
     *
     * @param results the ResultSet to be used
     * @return a list of blogs defined in the given ResultSet.
     */
    private List<Blog> readBlogsFrom(ResultSet results) throws SQLException {
        List<Blog> blogs = new ArrayList<>();
        while (results.next()) {
            Blog blog = readABlogFrom(results);
            blogs.add(blog);
        }
        return blogs;
    }

    /**
     * Add a new blog to the database.
     *
     * @param blog the given blog
     * @return a new Blog fetched from the database
     */
    @Override
    public Blog create(Blog blog) throws SQLException {
        int blogId = -1;

        if (!tooLongPropertyFound(blog)) {

            String sql = "INSERT INTO " + TABLE_NAME + " (NAME, URL, TYPE, DESCRIPTION) "
                    + "VALUES (?, ?, ?, ?)";
            try (Connection connection = databaseManager.connect()) {
                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, blog.getProperty(Properties.TITLE).orElse(null));
                stmt.setString(2, blog.getProperty(Properties.URL).orElse(null));
                stmt.setString(3, blog.getType());
                stmt.setString(4, blog.getProperty(Properties.DESCRIPTION).orElse(""));
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    blogId = rs.getInt(1);
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                // Unchecked exception is thrown if SQL error occurs during closing or execution.
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return findOne(blogId);
    }

    /**
     * Searches for a specific blog from the database by the given key/id.
     *
     * @param key the blog's primary key
     * @return a new Blog object fetched from the database based on the key
     */
    @Override
    public Blog findOne(Integer key) throws SQLException {
        try (Connection conn = databaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT ID, NAME, URL, DESCRIPTION FROM " + TABLE_NAME + " WHERE ID = ?");
            stmt.setInt(1, key);
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            Blog blog = readABlogFrom(result);
            result.close();
            return blog;
        }
    }

    /**
     * Update blog information in the database
     *
     * @param object the blog that is updated
     * @return the blog
     * @throws SQLException
     */
    @Override
    public boolean update(Blog object) throws SQLException {
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
     * Deletes a specific blog from the database by the given key/id.
     *
     * @param key the blog's primary key
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
     * Goes through a blog's properties and checks if any of the properties'
     * content is too long considering column sizes.
     *
     * @param the blog to be checked
     * @return true if any of the properties in the property list is too long,
     * false if not
     * @throws SQLException
     */
    @Override
    public boolean tooLongPropertyFound(Blog blog) throws SQLException {
        List<Property> props = blog.getProperties();
        boolean tooLong = false;
        for (int i = 0; i < props.size(); i++) {
            Property p = props.get(i);
            if (!p.getName().equals("ID") && propertyTooLong(p.getName(), blog.getProperty(p).toString())) {
                tooLong = true;
            }
        }
        return tooLong;
    }

}

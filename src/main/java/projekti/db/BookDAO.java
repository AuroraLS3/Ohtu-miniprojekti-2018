package projekti.db;

import projekti.domain.Book;
import projekti.domain.Book.Properties;

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
 * Data Access Object for Books.
 *
 * @author Rsl1122
 */
public class BookDAO implements Dao<Book, Integer> {

    private static final String TABLE_NAME = "RECOMMENDATION";

    private final DatabaseManager databaseManager;

    /**
     * Constructor.
     *
     * @param databaseManager Required DatabaseManager for getting Connections.
     */
    public BookDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Fetch all books from the database.
     *
     * @return List of books, empty if none are found.
     */
    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE='BOOK'";
        try (Connection connection = databaseManager.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()) {
            return readBooksFrom(results);
        } catch (SQLException e) {
            // Unchecked exception is thrown if SQL error occurs during closing or execution.
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private Book readABookFrom(ResultSet result) throws SQLException {
        Integer id = result.getInt("ID");
        Book book = new Book(
                result.getString("AUTHOR"),
                result.getString("NAME"),
                result.getString("ISBN"),
                result.getString("URL"),
                result.getString("DESCRIPTION")
        );
        book.addProperty(Properties.ID, id);
        return book;
    }

    /**
     * Read books from database using ResultSet.
     *
     * @param results the ResultSet to be used
     * @return a list of books defined in the given ResultSet.
     */
    private List<Book> readBooksFrom(ResultSet results) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (results.next()) {
            Book book = readABookFrom(results);
            books.add(book);
        }
        return books;
    }

    /**
     * Add a new book to the database.
     *
     * @param book the given book
     * @return a new Book fetched from the database
     */
    @Override
    public Book create(Book book) throws SQLException {
        int bookId = -1;

        if (!tooLongPropertyFound(book)) {

            String sql = "INSERT INTO " + TABLE_NAME + " (AUTHOR, NAME, ISBN, TYPE, URL, DESCRIPTION) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection connection = databaseManager.connect()) {
                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, book.getProperty(Properties.AUTHOR).orElse(null));
                stmt.setString(2, book.getProperty(Properties.TITLE).orElse(null));
                stmt.setString(3, book.getProperty(Properties.ISBN).orElse(null));
                stmt.setString(4, book.getType());
                stmt.setString(5, book.getProperty(Properties.URL).orElse(null));
                stmt.setString(6, book.getProperty(Properties.DESCRIPTION).orElse(""));
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    bookId = rs.getInt(1);
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                // Unchecked exception is thrown if SQL error occurs during closing or execution.
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return findOne(bookId);
    }

    /**
     * Searches for a specific book from the database by the given key/id.
     *
     * @param key the book's primary key
     * @return a new Book object fetched from the database based on the key
     */
    @Override
    public Book findOne(Integer key) throws SQLException {
        try (Connection conn = databaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?");
            stmt.setInt(1, key);
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            Book one = readABookFrom(result);
            result.close();
            return one;
        }
    }

    /**
     * Update book information in the database
     *
     * @param object the book that is updated
     * @return the book
     * @throws SQLException
     */
    @Override
    public boolean update(Book object) throws SQLException {
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
     * Deletes a specific book from the database by the given key/id.
     *
     * @param key the book's primary key
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
     * Goes through a book's properties and checks if any of the properties'
     * content is too long considering column sizes.
     *
     * @param the book to be checked
     * @return true if any of the properties in the property list is too long,
     * false if not
     * @throws SQLException
     */
    @Override
    public boolean tooLongPropertyFound(Book book) throws SQLException {
        List<Property> props = book.getProperties();
        boolean tooLong = false;
        for (int i = 0; i < props.size(); i++) {
            Property p = props.get(i);
            if (!p.getName().equals("ID") && propertyTooLong(p.getName(), book.getProperty(p).toString())) {
                tooLong = true;
            }
        }
        return tooLong;
    }

}

package projekti.db;

import projekti.domain.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projekti.domain.Book.Properties;

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

    /**
     * Read books from database using ResultSet.
     *
     * @param results the ResultSet to be used
     * @return a list of books defined in the given ResultSet.
     */
    private List<Book> readBooksFrom(ResultSet results) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (results.next()) {
            Integer id = results.getInt("ID");
            Book book = new Book(
                    results.getString("AUTHOR"),
                    results.getString("NAME"),
                    results.getString("ISBN"),
                    results.getString("DESCRIPTION")
            );
            book.setID(id);
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
        
        String sql = "INSERT INTO " + TABLE_NAME + " (AUTHOR, NAME, ISBN, TYPE, DESCRIPTION) "
                + "VALUES (?, ?, ?, ?,?)";
        try (Connection connection = databaseManager.connect()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            
            stmt.setString(1, book.getProperty(Properties.AUTHOR).orElse(null));
            stmt.setString(2, book.getProperty(Properties.TITLE).orElse(null));
            stmt.setString(3, book.getProperty(Properties.ISBN).orElse(null));
            stmt.setString(4, book.getType());
            stmt.setString(5, book.getProperty(Properties.DESCRIPTION).orElse(""));
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
            PreparedStatement stmt = conn.prepareStatement("SELECT ID, AUTHOR, NAME, ISBN, DESCRIPTION FROM " + TABLE_NAME + " WHERE ID = ?");
            stmt.setInt(1, key);
            
            ResultSet result = stmt.executeQuery();
            
            if (!result.next()) {
                return null;
            }
            Integer id = result.getInt("ID");
            Book book = new Book(result.getString("AUTHOR"), result.getString("NAME"),
                    result.getString("ISBN"),
                    result.getString("DESCRIPTION"));
            book.setID(id);
            return book;
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
    public Book update(Book object) throws SQLException {
        try (Connection conn = databaseManager.connect()) {
            String statementString = "UPDATE RECOMMENDATION "
                    + "SET AUTHOR = ? "
                    + "SET NAME = ? "
                    + "SET ISBN = ? "
                    + "SET TYPE = ? "
                    + "SET DESCRPIION = ? "
                    + "WHERE RECOMMENDATION.ID = ? ;";
            PreparedStatement stmnt = conn.prepareStatement(statementString);
            stmnt.setString(1, object.getProperty(Properties.AUTHOR).orElse(null));
            stmnt.setString(2, object.getProperty(Properties.TITLE).orElse(null));
            stmnt.setString(3, object.getProperty(Properties.ISBN).orElse(null));
            stmnt.setString(4, object.getType());
            stmnt.setString(5, object.getProperty(Properties.DESCRIPTION).orElse(""));
            stmnt.setInt(6, object.getProperty(Properties.ID).orElse(null));
            int count = stmnt.executeUpdate();
            if (count == 0) {
                Logger.getGlobal().log(Level.WARNING, "No matches for update in the db");
            }
            return object;
        }
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
    
}

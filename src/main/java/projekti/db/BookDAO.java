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
        try (
                Connection connection = databaseManager.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery();) {
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
     *
     * @return a list of books defined in the given ResultSet.
     */
    private List<Book> readBooksFrom(ResultSet results) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (results.next()) {
            Integer id = results.getInt("ID");
            Book book = new Book(
                    results.getString("AUTHOR"),
                    results.getString("NAME"),
                    results.getString("ISBN")
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
     *
     * @return a new Book fetched from the database
     */
    @Override
    public Book create(Book book) throws SQLException {

        int bookId = -1;

        String sql = "INSERT INTO " + TABLE_NAME + " (AUTHOR, NAME, ISBN, TYPE) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseManager.connect()) {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, book.getAuthor());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getISBN());
            stmt.setString(4, book.getType());
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
     *
     * Searches for a specific book from the database by the given key/id.
     *
     * @param key the book's primary key
     *
     * @return a new Book object fetched from the database based on the key
     *
     */
    @Override
    public Book findOne(Integer key) throws SQLException {
        try (Connection conn = databaseManager.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT AUTHOR, NAME, ISBN FROM " + TABLE_NAME + " WHERE ID = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();

            if (!result.next()) {
                return null;
            }

            return new Book(result.getString("AUTHOR"), result.getString("NAME"), result.getString("ISBN"));
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
                    + "WHERE RECOMMENDATION.ID = ? ;";
            PreparedStatement stmnt = conn.prepareStatement(statementString);
            stmnt.setString(1, object.getAuthor());
            stmnt.setString(2, object.getTitle());
            stmnt.setString(3, object.getISBN());
            stmnt.setString(4, object.getType());
            int count = stmnt.executeUpdate();
            if (count == 0) {
                Logger.getGlobal().log(Level.WARNING, "No matches for update in the db");
            }
            return object;
        }
    }
}

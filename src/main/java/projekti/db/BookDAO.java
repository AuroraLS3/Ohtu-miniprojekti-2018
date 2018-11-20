package projekti.db;

import projekti.domain.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Books.
 *
 * @author Rsl1122
 */
public class BookDAO implements Dao<Book, Integer> {

    private static final String TABLE_NAME = "Recommendation";

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

    private List<Book> readBooksFrom(ResultSet results) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (results.next()) {
            Book book = new Book(
                    results.getString("AUTHOR"),
                    results.getString("NAME"),
                    results.getString("ISBN")
            );
            books.add(book);
        }
        return books;
    }

    /**
     * Add a new book to the database.
     *
     * @return a new Book fetched from the database
     */
    @Override
    public Book create(Book book) throws SQLException {

        int bookId;

        String sql = "INSERT INTO " + TABLE_NAME + " (ID, AUTHOR, NAME, ISBN, TYPE) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = databaseManager.connect()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getTitle());
            stmt.setString(4, book.getISBN());
            stmt.setString(5, book.getType());
            stmt.executeUpdate();

            stmt = connection.prepareStatement("SELECT MAX(ID) AS ID FROM " + TABLE_NAME);
            ResultSet rs = stmt.executeQuery();
            bookId = rs.getInt("ID");
            rs.close();

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
            PreparedStatement stmt = conn.prepareStatement("SELECT AUTHOR, TITLE, ISBN FROM " + TABLE_NAME + " WHERE ID = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();

            if (!result.next()) {
                return null;
            }

            return new Book(result.getString("AUTHOR"), result.getString("NAME"), result.getString("ISBN"));
        }
    }
}

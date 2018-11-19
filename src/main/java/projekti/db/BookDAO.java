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
public class BookDAO {

    private static final String TABLE_NAME = "Recommendation";

    private final DatabaseManager databaseManager;

    public BookDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Fetch all books from the database.
     *
     * @return List of books, empty if none are found.
     */
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE TYPE='BOOK'";
        try (
                Connection connection = databaseManager.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery();
        ) {
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
}

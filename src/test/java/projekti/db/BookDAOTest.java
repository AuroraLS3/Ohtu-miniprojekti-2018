package projekti.db;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import projekti.domain.Book;
import projekti.domain.Property;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class BookDAOTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    private BookDAO underTest;
    private Book testBook;

    @Before
    public void setUp() throws SQLException, IOException {
        // It is assumed that this book has all properties.
        testBook = new Book("Matti Meikäläinen", "Esimerkki-ihmisen arkipäivä", "4332652435", "Description");

        File dbFile = temporaryFolder.newFile();
        DatabaseManager databaseManager = new DatabaseManager("jdbc:h2:" + dbFile.getAbsolutePath(), "sa", "");
        underTest = new BookDAO(databaseManager);
    }

    @Test
    public void emptyListIsReturnedWithNoBooksSaved() {
        assertEquals(Collections.emptyList(), underTest.findAll());
    }

    @Test
    public void newBookIsReturnedByFindAll() throws SQLException {
        addBook();
        assertEquals(Collections.singletonList(testBook), underTest.findAll());
    }

    private void addBook() throws SQLException {
        testBook = underTest.create(testBook);
    }

    @Test
    public void bookIsDeleted() throws SQLException {
        // Create and save new book
        newBookIsReturnedByFindAll();

        Optional<Integer> idProperty = testBook.getProperty(Book.Properties.ID);
        assertTrue(idProperty.isPresent());
        underTest.delete(idProperty.get());

        // Check that database is now empty
        emptyListIsReturnedWithNoBooksSaved();
    }

    @Test
    public void bookIsFoundById() throws SQLException {
        addBook();

        Optional<Integer> idProperty = testBook.getProperty(Book.Properties.ID);
        assertTrue(idProperty.isPresent());
        Book book = underTest.findOne(idProperty.get());
        assertEquals(testBook, book);
    }

    @Test
    public void foundBooksHaveAllProperties() throws SQLException {
        addBook();

        List<Book> found = underTest.findAll();
        assertFalse(found.isEmpty());

        Book book = found.get(0);
        for (Property property : book.getProperties()) {
            assertTrue("Book does not have " + property.getName() + " property when fetched with findAll.", book.getProperty(property).isPresent());
        }

    }

    @Test
    public void foundBookHasAllProperties() throws SQLException {
        addBook();

        Optional<Integer> idProperty = testBook.getProperty(Book.Properties.ID);
        assertTrue(idProperty.isPresent());
        Book book = underTest.findOne(idProperty.get());
        for (Property property : book.getProperties()) {
            assertTrue("Book does not have " + property.getName() + " property when fetched with findOne.", book.getProperty(property).isPresent());
        }

    }

}
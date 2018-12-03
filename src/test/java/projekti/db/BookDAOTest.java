package projekti.db;

import projekti.domain.Book;

import java.io.File;

public class BookDAOTest extends DAOTest<Book> {

    @Override
    protected Dao<Book, Integer> setUpDAO() throws Exception {
        File dbFile = temporaryFolder.newFile();
        DatabaseManager databaseManager = new DatabaseManager("jdbc:h2:" + dbFile.getAbsolutePath(), "sa", "");
        return new BookDAO(databaseManager);
    }

    @Override
    protected Book createTestObject() {
        // It is assumed that this book has all properties.
        return new Book("Matti Meikäläinen", "Esimerkki-ihmisen arkipäivä", "4332652435", "Description");
    }
}
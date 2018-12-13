package projekti.db;

import projekti.domain.Book;
import projekti.domain.Book.Properties;

import java.io.File;
import projekti.domain.Blog;
import projekti.domain.Other;
import projekti.domain.Recommendation;

public class RecommendationDAOTest extends DAOTest<Recommendation> {

    @Override
    protected Dao<Recommendation, Integer> setUpDAO() throws Exception {
        File dbFile = temporaryFolder.newFile();
        DatabaseManager databaseManager = new DatabaseManager("jdbc:h2:" + dbFile.getAbsolutePath(), "sa", "");
        return new RecommendationDAO(databaseManager);
    }

    @Override
    protected Recommendation createTestObject() {
        // It is assumed that this book has all properties.
        return new Book("Matti Meikäläinen", "Esimerkki-ihmisen arkipäivä", "4332652435", "https://www.kirja.fi", "Description");
    }

    protected Recommendation createSecondTestObject() {
        // It is assumed that this blog has all properties.
        return new Blog("Masan blogi", "https://www.blogi.fi", "Description");
    }

    protected Recommendation createThirdTestObject() {
        // It is assumed that this blog has all properties.
        return new Other("Jotain muuta", "https://www.muu.fi", "Description");
    }
    
    @Override
    protected Recommendation updateObjectInSomeWay(Recommendation testObject) {
        Book updatedObject = (Book) createTestObject();
        updatedObject.copyFrom((Book) testObject);

        updatedObject.addProperty(Properties.DESCRIPTION, "Updated Description");
        return (Recommendation) updatedObject;
    }
}

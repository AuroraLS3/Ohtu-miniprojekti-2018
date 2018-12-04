package projekti.db;

import projekti.domain.Blog;

import java.io.File;

/**
 * Tests for {@link BlogDAO}.
 *
 * @author Rsl1122
 */
public class BlogDAOTest extends DAOTest<Blog> {

    @Override
    protected Dao<Blog, Integer> setUpDAO() throws Exception {
        File dbFile = BlogDAOTest.temporaryFolder.newFile();
        DatabaseManager databaseManager = new DatabaseManager("jdbc:h2:" + dbFile.getAbsolutePath(), "sa", "");
        return new BlogDAO(databaseManager);
    }

    @Override
    protected Blog createTestObject() {
        // It is assumed that this blog has all properties.
        return new Blog("Medium", "http://www.medium.com", "Description");
    }

    @Override
    protected Blog updateObjectInSomeWay(Blog testObject) {
        Blog updatedObject = createTestObject();
        updatedObject.copyFrom(testObject);

        updatedObject.setDescription("Updated Description");
        return updatedObject;
    }
}
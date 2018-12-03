package projekti.db;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import projekti.domain.Blog;
import projekti.domain.CommonProperties;
import projekti.domain.Property;
import projekti.domain.Recommendation;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Abstraction of DAOTest classes.
 *
 * Tests that only affect a single DAO should be written in their appropriate classes.
 *
 * @author Rsl1122
 */
public abstract class DAOTest<T extends Recommendation> {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    // Initialize this in 
    protected Dao<T, Integer> underTest;
    protected T testObject;

    
    @Before
    public void setUp() throws Exception {
        underTest = setUpDAO();
        testObject = createTestObject();
    }

    protected abstract Dao<T, Integer> setUpDAO() throws Exception;
    
    protected abstract T createTestObject();

    private void addObject() throws SQLException {
        testObject = underTest.create(testObject);
    }

    @Test
    public void emptyListIsReturnedWithNoBlogsSaved() throws SQLException {
        assertEquals(Collections.emptyList(), underTest.findAll());
    }

    @Test
    public void newBlogIsReturnedByFindAll() throws SQLException {
        addObject();
        assertEquals(Collections.singletonList(testObject), underTest.findAll());
    }

    @Test
    public void objectIsDeleted() throws SQLException {
        // Create and save new object
        newBlogIsReturnedByFindAll();

        Optional<Integer> idProperty = testObject.getProperty(CommonProperties.ID);
        assertTrue(idProperty.isPresent());
        underTest.delete(idProperty.get());

        // Check that database is now empty
        emptyListIsReturnedWithNoBlogsSaved();
    }

    @Test
    public void objectIsFoundById() throws SQLException {
        addObject();

        Optional<Integer> idProperty = testObject.getProperty(Blog.Properties.ID);
        assertTrue(idProperty.isPresent());
        T object = underTest.findOne(idProperty.get());
        assertEquals(testObject, object);
    }

    @Test
    public void foundBlogsHaveAllProperties() throws SQLException {
        addObject();

        List<T> found = underTest.findAll();
        assertFalse(found.isEmpty());

        T object = found.get(0);
        for (Property property : object.getProperties()) {
            assertTrue("does not have " + property.getName() + " property when fetched with findAll.", object.getProperty(property).isPresent());
        }

    }

    @Test
    public void foundBlogHasAllProperties() throws SQLException {
        addObject();

        Optional<Integer> idProperty = testObject.getProperty(CommonProperties.ID);
        assertTrue(idProperty.isPresent());
        T object = underTest.findOne(idProperty.get());
        for (Property property : object.getProperties()) {
            assertTrue("does not have " + property.getName() + " property when fetched with findOne.", object.getProperty(property).isPresent());
        }

    }
}

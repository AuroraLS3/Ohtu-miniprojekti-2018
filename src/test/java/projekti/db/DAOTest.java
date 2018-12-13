package projekti.db;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
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
 * <p>
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
    
    protected abstract T createSecondTestObject();
    
    protected abstract T createThirdTestObject();

    protected abstract T updateObjectInSomeWay(T testObject);

    private void addObject() throws SQLException {
        testObject = underTest.create(testObject);
    }

    @Test
    public void emptyListIsReturnedWithNoObjectsSaved() throws SQLException {
        assertEquals(Collections.emptyList(), underTest.findAll(testObject.getType()));
    }

    @Test
    public void newObjectIsReturnedByFindAll() throws SQLException {
        addObject();
        assertEquals(Collections.singletonList(testObject), underTest.findAll(testObject.getType()));
    }

    @Test
    public void objectIsDeleted() throws SQLException {
        // Create and save new object
        newObjectIsReturnedByFindAll();

        Optional<Integer> idProperty = testObject.getProperty(CommonProperties.ID);
        assertTrue(idProperty.isPresent());
        underTest.delete(idProperty.get());

        // Check that database is now empty
        emptyListIsReturnedWithNoObjectsSaved();
    }

    @Test
    public void objectIsFoundById() throws SQLException {
        addObject();

        Optional<Integer> idProperty = testObject.getProperty(CommonProperties.ID);
        assertTrue(idProperty.isPresent());
        T object = underTest.findOne(idProperty.get());
        assertEquals(testObject, object);
    }

    @Test
    public void foundObjectsHaveAllProperties() throws SQLException {
        addObject();

        List<T> found = underTest.findAll(testObject.getType());
        assertFalse(found.isEmpty());

        T object = found.get(0);
        for (Property property : object.getProperties()) {
            assertTrue("does not have " + property.getName() + " property when fetched with findAll.", object.getProperty(property).isPresent());
        }

    }

    @Test
    public void foundObjectHasAllProperties() throws SQLException {
        addObject();

        Optional<Integer> idProperty = testObject.getProperty(CommonProperties.ID);
        assertTrue(idProperty.isPresent());
        T object = underTest.findOne(idProperty.get());
        for (Property property : object.getProperties()) {
            assertTrue("does not have " + property.getName() + " property when fetched with findOne.", object.getProperty(property).isPresent());
        }
    }

    @Test
    public void updatedObjectIsSaved() throws SQLException {
        addObject();

        T updatedObject = updateObjectInSomeWay(testObject);
        // Check that the object was actually updated
        assertNotEquals(testObject, updatedObject);

        // Update in db
        assertTrue(underTest.update(updatedObject));

        // Check that the updated object was saved
        T found = underTest.findOne(updatedObject.getProperty(CommonProperties.ID).orElse(-1));
        assertNotNull(found);

        assertEquals(updatedObject, found);
    }
}

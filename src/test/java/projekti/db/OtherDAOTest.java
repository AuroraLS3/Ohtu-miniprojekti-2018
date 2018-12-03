package projekti.db;

import projekti.domain.Other;

import java.io.File;

/**
 * Tests for {@link BlogDAO}.
 *
 * @author Rsl1122
 */
public class OtherDAOTest extends DAOTest<Other> {

    @Override
    protected Dao<Other, Integer> setUpDAO() throws Exception {
        File dbFile = OtherDAOTest.temporaryFolder.newFile();
        DatabaseManager databaseManager = new DatabaseManager("jdbc:h2:" + dbFile.getAbsolutePath(), "sa", "");
        return new OtherDAO(databaseManager);
    }

    @Override
    protected Other createTestObject() {
        // It is assumed that this blog has all properties.
        return new Other("WHATEVER", "https://www.google.com", "Search stuff");
    }

}
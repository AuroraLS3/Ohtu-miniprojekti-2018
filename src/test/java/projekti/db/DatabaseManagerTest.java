package projekti.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManagerTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    private DatabaseManager underTest;

    @Before
    public void setUp() throws SQLException, IOException {
        File dbFile = temporaryFolder.newFile();
        underTest = new DatabaseManager("jdbc:h2:" + dbFile.getAbsolutePath(), "sa", "");
    }

    @Test
    public void testConnection() throws SQLException {
        try (Connection conn = underTest.connect()) {
            assertFalse("Connection should open", conn.isClosed());
            underTest.disconnect();
            assertTrue("Connection should close", conn.isClosed());
        }
    }

    @Test
    public void testSchema() throws SQLException {
        try (Connection conn = underTest.connect()) {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            HashSet<String> hs = new HashSet<>();
            while (rs.next()) {
                hs.add(rs.getString(3));
            }
            rs.close();
            underTest.disconnect();
            assertTrue("There should be the recommendation table", hs.contains("RECOMMENDATION"));
        }
    }

}

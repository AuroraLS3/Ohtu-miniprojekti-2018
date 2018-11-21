package projekti.db;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashSet;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManagerTest {

	@Test
	public void testConnection() {
		try {
			DatabaseManager dbm = new DatabaseManager("jdbc:h2:./build/test", "sa", "");
			try (Connection conn = dbm.connect()) {
				assertFalse("Connection should open", conn.isClosed());
				dbm.disconnect();
				assertTrue("Connection should close", conn.isClosed());
			} catch (SQLException e) {
				e.printStackTrace();
				fail("Connect caused an exception");
			}
		} catch (SQLException e) {
			fail("Constructor caused and exception");
		}

	}

	@Test
	public void testSchema() {
		Connection conn;
		DatabaseManager dbm;
		try {
			dbm = new DatabaseManager("jdbc:h2:./build/test", "sa", "");
			try {
				conn = dbm.connect();
				DatabaseMetaData md = conn.getMetaData();
				ResultSet rs = md.getTables(null, null, "%", null);
				HashSet<String> hs = new HashSet<>();
				while (rs.next()) {
					hs.add(rs.getString(3));
				}
				rs.close();
                                dbm.disconnect();
				assertTrue("There should be the recommendation table", hs.contains("RECOMMENDATION"));
			} catch (SQLException e) {
				e.printStackTrace();
				fail("Checking schema caused an exception");
				dbm.disconnect();
			}
		} catch (SQLException e1) {
			fail("Constructor caused an exception");
		}

	}

}

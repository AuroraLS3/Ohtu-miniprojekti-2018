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
            Connection conn = dbm.connect();
            assertFalse(conn.isClosed());
            dbm.disconnect();
            assertTrue(conn.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Connect caused an exception");
        }
    }
    
    @Test
    public void testSchema() {
    	try {
    		DatabaseManager dbm = new DatabaseManager("jdbc:h2:./test", "sa", "");
    		Connection conn = dbm.connect();
    		DatabaseMetaData md = conn.getMetaData();
    		ResultSet rs = md.getTables(null, null, "%", null);
    		HashSet<String> hs = new HashSet<>();
    		while(rs.next()) {
    			hs.add(rs.getString(3));
    		}
    		assertTrue(hs.contains("Recommendation"));
    	}	catch (SQLException e) {
    		e.printStackTrace();
    		fail("Checking schema caused an exception");
    	}
    }
    

}

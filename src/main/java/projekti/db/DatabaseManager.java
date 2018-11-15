package projekti.db;

import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * Is used to manage database configuration.
 * @author vili
 *
 */
class DatabaseManager {
	private String url;
	private String username;
	private String password;
	private Connection conn;
	private final String schemaPath = "sql/schema.sql";
	
	/**
	 * Creates database if it does not exist.
	 * @param url  url for driver
	 * @param username  username for db  "sa"
	 * @param password  password for db ""
	 * @throws SQLException 
	 */
	public DatabaseManager(String url, String username, String password) throws SQLException {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Can't find H2 driver");
		}
		this.url = url;
		this.username = username;
		this.password = password;
		this.conn = null;
		this.setupSchema();
	}
	/**
	 * Make a safe connection. Useful if autocommit is of.
	 * @return Connection.
	 * @throws SQLException
	 */
	public Connection connect() throws SQLException {
		if (this.conn == null || this.conn.isClosed()) {
			this.conn = DriverManager.getConnection(url, username, password);
		}
		return this.conn;
	}

	public void disconnect() throws SQLException {
		this.conn.close();
	}

	/**
	 * Schema is written in "IF EXISTS" format so this is safe to call every time.
	 * 
	 * @throws SQLException if something goes wrong.
	 */
	public void setupSchema() throws SQLException {
		this.connect();
		String schema = readResourceFile(this.schemaPath);
		Statement stmnt = conn.createStatement();
		stmnt.executeUpdate(schema);
		conn.close();
	}

	private String readResourceFile(String filename) {
		InputStreamReader isr = new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(filename));
		BufferedReader br = new BufferedReader(isr);
		String value = br.lines().collect(Collectors.joining("\n"));
		return value;
	}

}

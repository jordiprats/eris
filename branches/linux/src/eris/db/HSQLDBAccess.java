package eris.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HSQLDBAccess extends DBAccess {
	
	//http://www.javablogging.com/embedded-database-in-java-use-of-hsqldb/
	protected Connection conn;
	protected String dbname=null;
	
	
	public void close()
	{
		if(debug) System.err.println("shuting down...");
		try {
			conn.createStatement().execute("shutdown;");
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void connect(String dbname) throws SQLException
	{
		this.dbname=dbname;
		
		conn = DriverManager.getConnection("jdbc:hsqldb:"+dbname,
                "sa",					// username
                "");					// password
		conn.setAutoCommit(true);
		
		if(debug) System.err.println("enlarging ur penis...");
	}
	
	public HSQLDBAccess(String dbname) throws ClassNotFoundException, SQLException
	{		
		Class.forName("org.hsqldb.jdbcDriver");
		
		this.connect(dbname);
	}
	
	public void commit() throws SQLException
	{
		conn.commit();
	}
	
	/**
	 * executa una query i mira si te resultat, retorna true o false
	 * @param query
	 * @return 
	 * @throws SQLException
	 */
	public boolean testquery(String query) throws SQLException
	{
		if(debug) System.err.println(query);
		return conn.createStatement().executeQuery(query).next();
	}
	
	public ResultSet execute(String query) throws SQLException
	{
		if(debug) System.err.println(query);
		return conn.createStatement().executeQuery(query);
	}
	
	public int  update(String query) throws SQLException
	{
		if(debug) System.err.println(query);
		
		return conn.createStatement().executeUpdate(query);
	}
	
}

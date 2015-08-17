package eris.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DBAccess {

	protected boolean debug=false;
	public void setDebug(boolean debug) { this.debug=debug; }
	
	public abstract ResultSet execute(String query)   throws SQLException;
	public abstract int      update(String query)    throws SQLException;
	public abstract boolean   testquery(String query) throws SQLException;
	
	public abstract void      commit()                throws SQLException;
	public abstract void      close();
	
}

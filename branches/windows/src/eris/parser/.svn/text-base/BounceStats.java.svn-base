package eris.parser;

import java.sql.ResultSet;
import java.sql.SQLException;
//1234ach
import eris.db.*;

public class BounceStats 
{
	protected boolean debug=false;
	public void setDebug(boolean debug) { this.debug=debug; }
	
	protected DBAccess dbstats=null;

	public void close()
	{
		System.out.println(this.toString());
	}
	
	public String toString()
	{
		String ret="";

		try {
			ResultSet rs=this.dbstats.execute("select * from bouncestats;");
			
			while(rs.next())
			{
				ret+=rs.getString("bouncedomain")+"="+rs.getString("total")+";\n";
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public BounceStats(boolean debug, String ConnectionID) throws ClassNotFoundException, SQLException
	{
		this.debug=debug;
		
		String hashconnectionid=BounceClassifier.getMD5(ConnectionID);
		if(hashconnectionid==null)
			this.dbstats=new HSQLDBAccess("bouncestats");
		else
			this.dbstats=new HSQLDBAccess("bouncestats."+hashconnectionid);
		
		this.dbstats.setDebug(this.debug);

		// debug only
		//try { if(this.debug) this.dbstats.update("drop table bouncestats;"); } catch (SQLException e) {	}
		
		try
		{
			
			this.dbstats.update(
					"create table bouncestats(" +
						"bouncedomain 	varchar(256), "+
						"total 			int default 1"+
					");");
		} catch (SQLException e) {
			if(debug) System.err.println("taula ja existia");
			//e.printStackTrace();
		}
		
		this.dbstats.close();
		this.dbstats=new HSQLDBAccess("bouncestats");
		this.dbstats.setDebug(this.debug);
		}

	public void incDomain(String domain)
	{
		try
		{
			if(this.dbstats.testquery("select total from bouncestats where bouncedomain='"+domain+"';"))
			{
				//incrementar
				//this.dbstats.execute()
				this.dbstats.update("update bouncestats set total=total+1 where bouncedomain='"+domain+"';");
			}
			else
			{
				//afegir
				this.dbstats.update("insert into bouncestats values('"+domain+"',1);");
			}
			
			//?
			this.dbstats.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		//System.out.println(this.toString());
		
	}
	
	
}

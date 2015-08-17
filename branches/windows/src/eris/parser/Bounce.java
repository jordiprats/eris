package eris.parser;

import java.sql.SQLException;

import eris.db.DBAccess;

public class Bounce {

	protected long runnumber=0;
	public void setRunNumber(long runnumber) { this.runnumber=runnumber; }
	
	protected String reporter=null;
	
	protected boolean isbounce=false;
	protected boolean isspamreport=false;
	protected boolean issoftfail=false;
	
	public boolean isBounce() { return this.isbounce; }
	public boolean isSpamReport() { return this.isspamreport; }
	public boolean isSoftFail() { return this.issoftfail; }
	
	public void setBounce(boolean bounce) { this.isbounce=bounce; }
	public void setSpamReport(boolean spamreport) { this.isspamreport=spamreport; }
	public void setSoftFail(boolean softfail) { this.issoftfail=softfail; }
	
	protected String code=null;
	protected String comment=null;
	protected String messageid=null;
	
	protected String generatorname=null;
	public void setGeneratorName(String name) { this.generatorname=name; }
	public String getGeneratorName() { return this.generatorname;  }
	
	protected String arrivaltime=null;
	public String getArrivalTime() { return this.arrivaltime; }
	public void setArrivalTime(String arrivaltime) { this.arrivaltime=arrivaltime; }
	
	protected String email=null;
	public String getEmail() { return this.email; }
	public void setEmail(String email) { this.email=email; }
	
	public Bounce(boolean isbounce, String generator)
	{
		this.isbounce=isbounce;
		this.generatorname=generator;
	}
	
	public Bounce()
	{
		this.isbounce=true;
	}
	
	public void SQLize(DBAccess dba)
	{

		System.out.println("sqlize "+this.email+"/"+this.generatorname);
		try {
			String query="insert into bouncelist(runnumber, email, arrivaltime,spamreport, bounce, generatorname) " +
			"values(" +
			+this.runnumber+","+
			"'"+this.email+"',"+
			"'"+this.arrivaltime+"',"+
			(this.isspamreport?"true,":"false,")+
			(this.isbounce?"true,":"false,")+
			"'"+this.generatorname+"'"+
			");";
			
			dba.update(query);
			
			System.out.println(query);
			
			dba.update("commit;");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("FUCKFUCKFUCK");
		}
	}
}

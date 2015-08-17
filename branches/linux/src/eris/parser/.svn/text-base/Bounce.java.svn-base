package eris.parser;

import java.sql.SQLException;

import eris.db.DBAccess;

public class Bounce {

	protected long runnumber=0;
	public void setRunNumber(long runnumber) { this.runnumber=runnumber; }
	
	protected String reporter=null;
	
	protected boolean isbouncetypevalid=false;
	protected boolean isbounce=false;
	protected boolean isspamreport=false;
	protected boolean issoftbounce=false;
	protected boolean isdeferral=false;
	protected boolean isoutofoffice=false;
	
	public boolean isDeferral() { return this.isdeferral; }
	public boolean isBounce() { return this.isbounce; }
	public boolean isOutOfOffice() { return this.isoutofoffice; }
	public boolean isSpamReport() { return this.isspamreport; }
	public boolean isSoftBounce() { return this.issoftbounce; }
	public boolean isHardBounce() { return !this.issoftbounce; }
	public boolean isBouceTypeValid() { return this.isbouncetypevalid; }
	
	public void setBounce(boolean bounce) { this.isbounce=bounce; }
	public void setOutOfOffice(boolean isoutofoffice) { this.isoutofoffice=isoutofoffice; }
	public void setSpamReport(boolean spamreport) { this.isspamreport=spamreport; }
	public void setDeferral(boolean deferral) { this.isbouncetypevalid=true; this.setSoftBounce(!deferral); this.isdeferral=deferral; }
	//
	public void setSoftBounce(boolean softfail) { this.isbouncetypevalid=true; this.issoftbounce=softfail; }
	public void setSoftBounce() { this.isbouncetypevalid=true; this.setSoftBounce(true); }
	public void setHardBounce() { this.isbouncetypevalid=true; this.setSoftBounce(false); }
	
	protected String code=null;
	protected String comment=null;
	protected String messageid=null;
	
	protected String reason=null;
	public void setReason(String reason) { this.reason=reason; }
	public String getReason() { return this.reason;  }
	
	protected String generatorname=null;
	public void setGeneratorName(String name) { this.generatorname=name; }
	public String getGeneratorName() { return this.generatorname;  }
	
	protected String functionname=null;
	public void setFunctionName(String name) { this.functionname=name; }
	public String getFunctionName() { return this.functionname;  }
	
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
	
	public String toString()
	{
		return String.format("%55s\t\t"+(this.isbouncetypevalid?(this.issoftbounce?"SB ":this.isdeferral?"DF":"HB"):"?? ")+"\t"+this.generatorname, this.email);
	}
	
	public void SQLize(DBAccess dba)
	{
		System.out.println(this.toString());
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
			
//			System.out.println(query);
			
			dba.update("commit;");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("FUCKFUCKFUCK");
		}
	}
}

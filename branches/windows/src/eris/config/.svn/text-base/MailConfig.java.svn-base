package eris.config;

public class MailConfig {
	
	public static final int TIPUS_IMAP=1;
	public static final int TIPUS_POP3=2;
	
	protected int tipus;
	public String getProvider()
	{
		if(this.tipus==MailConfig.TIPUS_POP3) 
			return "pop3";
		if(this.tipus==MailConfig.TIPUS_IMAP)
			return "imap";
		
		return "fuckyeah";
	}
	
	protected String server; 
	public String getServer() { return this.server; }
	
	protected String username; 
	public String getUsername() { return this.username; }
	
	protected String password;
	public String getPassword() { return this.password; }
	
	protected String folder;
	public String getFolder() { return this.folder; }
	
	public MailConfig(int tipus, String server, String username, String password)
	{
		this.tipus=tipus;
		this.server=server;
		this.username=username;
		this.password=password;
		this.folder="INBOX";
	}

}

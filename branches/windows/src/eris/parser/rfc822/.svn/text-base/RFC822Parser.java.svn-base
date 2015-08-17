package eris.parser.rfc822;

public class RFC822Parser {
	
	protected static final String patterngettopleveldomain="[A-Za-z0-9._%+-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,4})";
	protected static final String patterngetdomain="[A-Za-z0-9._%+-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,4})";
	protected static final String patterngetemail=".*[^A-Za-z0-9._%+-]([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}).*";
	protected static final String patternisemail="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4})$";
	
	public String getTopLevelDomain(String emailfrom)
	{
		return emailfrom.replaceAll(RFC822Parser.patterngettopleveldomain, "$1");
	}
	
	public String getDomain(String emailfrom)
	{
		return emailfrom.replaceAll(RFC822Parser.patterngetdomain, "$1");
	}
	
	//RAWEMAIL>from sparkdixon@btinternet.com to sparkdixon@yahoo.com
	public String getFromForwarderString(String from)
	{
		return from.replaceAll("from "+RFC822Parser.patterngetemail+" to "+RFC822Parser.patterngetemail, "$1");
	}
	
	public String getFrom(String from)
	{
		return from.replaceAll(RFC822Parser.patterngetemail, "$1");
	}
	
	private static RFC822Parser instance = null;
	/**
	 *  soc protected, fuck you! (soc un singleton)
	 */
	protected RFC822Parser() 
	{
		// fuck you!
	}
	
	public static RFC822Parser getInstance() 
	{
		if(instance == null) instance = new RFC822Parser();

		return instance;
	}
	
}

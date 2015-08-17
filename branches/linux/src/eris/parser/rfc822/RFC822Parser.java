package eris.parser.rfc822;

public class RFC822Parser {
	
//	http://en.wikipedia.org/wiki/E-mail_address#RFC_specification
	
	//alternativa:
//	^['_a-z0-9-\+]+(\.['_a-z0-9-\+]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*\.([a-z]{2}|aero|arpa|asia|biz|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|nato|net|org|pro|tel|travel|xxx)$
	
	protected static final String patterngettopleveldomain="[A-Za-z0-9._%+-]+[A-Za-z0-9._%+-\\\\']*@([A-Za-z0-9.-]+\\.[A-Za-z]{2,4})";
	protected static final String patterngetdomain="[A-Za-z0-9._%+-]+[A-Za-z0-9._%+-\\\\']*@([A-Za-z0-9.-]+\\.[A-Za-z]{2,4})";
	protected static final String patterngetemail=".*[^A-Za-z0-9._%+-]([A-Za-z0-9._%+-]+[A-Za-z0-9._%+-\\\\']*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}).*";
	protected static final String patternisemail="[A-Za-z0-9._%+-]+[A-Za-z0-9._%+-\\\\']*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
	
	public boolean isEmail(String email)
	{
		return email.matches("(?i)^"+RFC822Parser.patternisemail);
	}
	
	public boolean isUserEmail(String email)
	{
		if(email==null) return false;
		
		if(
			email.compareTo("")==0 || 
			email.compareTo("null")==0  ||
			!RFC822Parser.getInstance().isEmail(email) ||
			email.matches("(?i)^postmaster@.*") ||
			email.matches("(?i)^quarantine@.*")||
			email.matches("(?i)^Mailer-Daemon@.*")||
			email.matches("(?i).*\\.loca$")||
			email.matches("(?i).*@jobisjob.*")//temporal!!!
				)
			return false;
		else
			return true;
	}
	
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
	
//	public static void main(String[] args) 
//	{
//		String valids[]={
//			    "niceandsimple@example.com",
//			    "very.common@example.com",
//			    "a.little.lengthy.but.fine@dept.example.com",
//			    "disposable.style.email.with+symbol@example.com",
//			    "user@[IPv6:2001:db8:1ff::a0b:dbd0]",
//			    "\"much.more unusual\"@example.com",
//			    "\"very.unusual.@.unusual.com\"@example.com",
//			    "postbox@com",
//			    "!#$%&'*+-/=?^_`{}|~@example.org",
//			    "\"()<>[]:,;@\\\"!#$%&'*+-/=?^_`{}| ~.a\"@example.org",
//			    "\" \"@example.org",
//			    "üñîçøðé@example.com"
//			};
//		
//		String valids_locals[]={
//				"admin@mailserver1"
//			};
//		
//		String invalids[]={
//			    "Abc.example.com",
//			    "A@b@c@example.com",
//			    "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",
//			    "just\"not\"right@example.com",
//			    "this is\\\"not\\allowed@example.com",
//			    "this\\ still\\\"not\\\\allowed@example.com"
//	};
//		
//		
//		
//	}
	
}

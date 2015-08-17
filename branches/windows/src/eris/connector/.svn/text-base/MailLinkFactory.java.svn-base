package eris.connector;

import eris.config.MailConfig;

public class MailLinkFactory {
	
	public static MailLink getMailLink(MailConfig mc)
	{
		if(mc.getProvider().compareTo("pop3")==0)
			return new POP3Link(mc);
		
		if(mc.getProvider().compareTo("imap")==0)
			return new IMAPLink(mc);
		
		return null;
	}

}

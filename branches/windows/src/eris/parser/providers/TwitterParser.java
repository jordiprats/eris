package eris.parser.providers;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class TwitterParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
//		System.out.println("twitter: "+RFC822Parser.getInstance().getDomain(from));
		
//		postmaster.twitter.com
		if(RFC822Parser.getInstance().getDomain(from).matches("(?i)^postmaster\\.twitter\\.com\\s*$"))
			return new Bounce(false, this.getClass().getName());
		
		return null;
	}

}

package eris.parser.providers;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class GTalkParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException,
			MessagingException {
		// talk-noreply@google.com
		
		if(from==null)
			return null;
		
		if(RFC822Parser.getInstance().getFrom(from).matches("(?i)^talk-noreply@google.com"))
			return new Bounce(false, this.getClass().getName());
		
		return null;
	}

}

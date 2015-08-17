package eris.parser.providers;

import java.io.IOException;
import java.util.Enumeration;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;

public class LinkedInParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException,
			MessagingException {
//		X-LinkedIn-Class: INVITE-GUEST
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());
				
				if(h.getName().compareTo("X-LinkedIn-Class")==0)
				{
					if(h.getValue().startsWith("INVITE-GUEST"))
						return new Bounce(false, this.getClass().getName());
				}
					
			}
			
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		}
		
		return null;
	}

}

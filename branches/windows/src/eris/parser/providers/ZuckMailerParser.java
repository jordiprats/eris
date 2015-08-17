package eris.parser.providers;

import java.io.IOException;
import java.util.Enumeration;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;

public class ZuckMailerParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}


	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
//				HEADER> X-Mailer: ZuckMail [version 1.00]
				if(h.getName().compareTo("X-Mailer")==0)
				{
					if(h.getValue().startsWith("ZuckMail"))
					{
						return new Bounce(false,this.getClass().getName());
						//System.out.println("MARSHAL: "+h.getValue());
					}
				}
			}
			
		} catch (MessagingException e1) {
//			e1.printStackTrace();
		}
		return null;
	}

}

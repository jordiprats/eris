package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;

public class MegaMailServersParser implements GenericParser {

	public boolean isRuleBased()
	{
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean ismegamail=false;
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				
				if(h.getName().compareTo("Message-Id")==0)
				{
					if(h.getValue().matches("(?is)^.*megamailservers\\.eu.*"))
						ismegamail=true;
				}
			}
		} catch (MessagingException e1) 
		{
			return null;
		}
		
		if(!ismegamail)
			return null;
		
		//MEGA-MAIL is MEGA-FAIL
		if((new BufferedReader(new InputStreamReader(message.getInputStream()))).readLine().startsWith("<<< No Message Collected >>>"))
			return new Bounce(false,this.getClass().getName());
		
		return null;
	}
	
}

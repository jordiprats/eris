package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class XFailedParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}
	
//	HEADER> X-Failed-Recipients: jobs@centralgestalt.de
//	HEADER> Subject: Delivery Status Notification (Failure)
//	HEADER> Message-ID: <001a11c2259cc7bdef04e40b248a@google.com>
//	HEADER> Date: Fri, 16 Aug 2013 07:00:19 +0000
//	HEADER> Content-Type: text/plain; charset=ISO-8859-1
//	MESS>> Hello info-alert@mails.jobisjob.com,
//	MESS>> 
//	MESS>> We're writing to let you know that the group you tried to contact (jobs) may not exist, or you may not have permission to post messages to the group. A few more details on why you weren't able to post:
//	MESS>> 

	public Bounce Parse(String from, Message message) throws IOException,MessagingException 
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				
				if(h.getName().compareTo("X-Failed-Recipients")==0)
				{
					String mail=RFC822Parser.getInstance().getFrom(h.getValue());
					
//					System.err.println("XF: "+mail);
//					System.err.flush();
					
					if(RFC822Parser.getInstance().isUserEmail(mail))
					{
						ret.setEmail(mail);
					}
					else
					{
						System.err.println("XF NO USER MAIL: "+mail);
						System.err.flush();
						return null;
					}
				}
			}
		} catch (MessagingException e1) 
		{
			return null;
		}
		
		if(ret.getEmail()==null)
			return null;
		
		try 
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read = br.readLine();

			boolean permanent=false;
			
			while(read!=null)
			{
				if(permanent && read.matches("(?is)^.*Original message.*"))
				{
					ret.setHardBounce();
					ret.setReason("Delivery to the following recipient failed permanently");
					return ret;
				}
					
				if(read.matches("(?is)^.*Delivery to the following recipient failed permanently.*"))
					permanent=true;
				
				if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
					return ret;
				
				read = br.readLine();
			}
		}
		catch(Exception e)
		{
			return null;
		}
		
		return null;
	}

}

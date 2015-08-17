package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class SMTP32Parser implements GenericParser {

	protected final static String[] patterns={
		"(?i)^user mailbox[^:]*:\\s+"+GenericParser.emailpattern,
		"(?i)^unknown user[^:]*:\\s+"+GenericParser.emailpattern,
		"(?i)^delivery failed[^:]*:\\s+"+GenericParser.emailpattern,
		"(?i)^delivery userid[^:]*:\\s+"+GenericParser.emailpattern,
		"(?i)^undeliverable\\s+to\\s+"+GenericParser.emailpattern
	};
	
	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean issmtp32=false;
		
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
//		HEADER> X-Mailer: <SMTP32 v12.2.0.235>
		
		try {
			Enumeration<?> headers = message.getAllHeaders();

			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());

				if(h.getName().compareTo("X-Mailer")==0)
				{
					//System.out.println("xfailed");
					//pasem pel parser per si les mosques
					if(h.getValue().startsWith("<SMTP32"))
					{
						issmtp32=true;
						//System.out.println("SMTP32!");
						break;
					}
				}
			}

		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
		
		if(!issmtp32)
			return null;
		
		//cos del mail
		try 
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read=br.readLine();

			while(read != null)
			{
				for(String pattern : SMTP32Parser.patterns)
				{
					if(read.matches(pattern))
					{
						//System.out.println("SMTP32:"+RFC822Parser.getInstance().getFrom(read));
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						break;
					}
				}
				if(ret.getEmail()!=null)
					break;
				
				read=br.readLine();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
			return null;
		
		//System.out.println("SMTP32:"+ret.getEmail());
		return ret;
	}

}

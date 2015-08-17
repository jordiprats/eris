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

public class MessageLabsParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException,MessagingException 
	{
		if(from.compareTo("MAILER-DAEMON@messagelabs.com")!=0)
			return null;

//		HEADER> From: MAILER-DAEMON@messagelabs.com
//		HEADER> To: info-alert@mails.jobisjob.com
//		HEADER> Subject: failure notice
		
		boolean ismessagelabs=false;
		
		try {
			Enumeration<?> headers = message.getAllHeaders();

			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());

				if(h.getName().compareTo("Subject")==0)
				{
					//System.out.println("xfailed");
					//pasem pel parser per si les mosques
					if(h.getValue().startsWith("failure notice"))
					{
						ismessagelabs=true;
						//System.out.println("SMTP32!");
						break;
					}
				}
			}

		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
		
		if(!ismessagelabs)
			return null;
		
//		MESS>> This is the mail delivery agent at messagelabs.com.
//		MESS>> I was not able to deliver your message to the following addresses.
//		MESS>> 
//		MESS>> <kavita.garde@barclays.com>:
//		MESS>> 157.83.125.16 does not like recipient.
//		MESS>> Remote host said: 550 Mailbox unavailable or access denied - <kavita.garde@barclays.com>
		
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
		//cos del mail
		try 
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read=br.readLine();
			boolean getmail=false;

			while(read != null)
			{
				if(getmail)
				{
					if(read.compareTo("")==0)
					{
						read=br.readLine();
						continue;
					}
					else 
					{
						String mail=null;
						//<kavita.garde@barclays.com>:
						if(read.matches("(?i)^<"+GenericParser.emailpattern+">.*"))
						{
							mail=RFC822Parser.getInstance().getFrom(read);
						}
						else return null;
						
//						System.out.println("mail ML:"+mail);
						if(mail!=null)
							ret.setEmail(mail);
						
						getmail=false; //només per si es modifica la logica en un futur
						break;
						
					}
				}
				else if(read.matches("(?i)^I was not able to deliver your message to the following addresses.*"))
				{
					getmail=true;
				}
				
				read=br.readLine();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
			return null;
		
		return ret;
	}

}

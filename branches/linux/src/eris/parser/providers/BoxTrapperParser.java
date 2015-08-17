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

public class BoxTrapperParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

//	HEADER> X-Boxtrapper: 6Tb9hvJ9yAuJH2FCy0gwpCiKxf9HsUyk
//	HEADER> X-Autorespond: 6Tb9hvJ9yAuJH2FCy0gwpCiKxf9HsUyk
//	HEADER> Precedence: auto_reply
//	HEADER> X-Precedence: auto_reply
//	HEADER> From: thabani@nathiafricaps.co.za
//	HEADER> To: info-alert@mails.jobisjob.com
//	HEADER> Subject: Re: 7 New - Management Jobs in Kwazulu Natal from JobisJob South Africa
//	HEADER> Message-Id: <E1UnAj5-002cfC-N2@perth.servershost.net>
//	HEADER> Date: Thu, 13 Jun 2013 11:50:19 -0500
//	HEADER> X-AntiAbuse: This header was added to track abuse, please include it with any abuse report
//	HEADER> X-AntiAbuse: Primary Hostname - perth.servershost.net
//	HEADER> X-AntiAbuse: Original Domain - mails.jobisjob.com
//	HEADER> X-AntiAbuse: Originator/Caller UID/GID - [4253 32007] / [47 12]
//	HEADER> X-AntiAbuse: Sender Address Domain - nathiafricaps.co.za
//	HEADER> X-Get-Message-Sender-Via: perth.servershost.net: authenticated_id: naps/from_h
//	HEADER> X-Source: /usr/local/cpanel/bin/boxtrapper
//	HEADER> X-Source-Args: /usr/local/cpanel/bin/boxtrapper thabani@nathiafricaps.co.za  
//	HEADER> X-Source-Dir: /tmp
//	MESS>> The user thabani@nathiafricaps.co.za does not accept mail from your address.
//	MESS>> 
//	MESS>> The headers of the message sent from your address are shown below:
//	MESS>> 
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean isboxtrapper=false;
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());
				
				if(h.getName().compareTo("X-Boxtrapper")==0)
				{
					isboxtrapper=true;
				}
					
			}
			
		} catch (MessagingException e1) 
		{
			return null;
		}
		
		if(!isboxtrapper)
			return null;
		
		Bounce ret=null;

		ret=this.ParsePlainText(new BufferedReader(new InputStreamReader(message.getInputStream())));
		
		if(ret==null || ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
//			System.out.println("error mail:" +ret.getEmail());
			return null;
		}
		
		return ret;
	}
	
	private Bounce ParsePlainText(BufferedReader br)
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());

//		System.out.println("BT.pl");
		
		try 
		{
			String read = br.readLine();
			
//			MESS>> The user thabani@nathiafricaps.co.za does not accept mail from your address.
			if(read.matches("(?i)^The user "+GenericParser.emailpattern+" does not accept mail from your address.*"))
			{
//				System.out.println("BT:"+ RFC822Parser.getInstance().getFrom(read));
				ret.setEmail(RFC822Parser.getInstance().getFrom(read));
				ret.setHardBounce();
				
				return ret;
			}
//			else System.err.println(read);
		}
		catch(Exception e)
		{
			return null;
		}
		
		return null;
	}

}

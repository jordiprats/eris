package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class IOmartParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

//	HEADER> Return-Path: <>
//	HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 31765 invoked by uid 89); 14 Jun 2013 07:01:18 -0000
//	HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 31763 invoked by uid 89); 14 Jun 2013 07:01:17 -0000
//	HEADER> Received: from glamf12.netintelligence.com (HELO mailfilter.iomart.com) (62.128.193.120)
//	  by retorn.grupointercom.com with SMTP; 14 Jun 2013 07:01:17 -0000
//	HEADER> From: "Mail Filter" <mailfilter@iomart.com>
//	HEADER> To: <info-alert@mails.jobisjob.com>
//	HEADER> Subject: Returned mail. See transcript for details
//	HEADER> Message-ID: <1371193266205.filter@mailfilter.iomart.com>
//	HEADER> MIME-Version: 1.0
//	HEADER> Content-Type: multipart/mixed;
//		boundary="----=NEXT.MAIL.FILTER.mailfilter.iomart.com"
//	MIME>CT> text/plain; charset=iso-8859-1
//	MESS>> From: "Mail Filter" <mailfilter@iomart.com>
//	MESS>> To: <info-alert@mails.jobisjob.com>
//	MESS>> Subject: Returned mail. See transcript for details
//	MESS>> Message-ID: <1371193266205.filter@mailfilter.iomart.com>
//	MESS>> MIME-Version: 1.0
//	MESS>> Content-Type: multipart/mixed;
//	MESS>> 	boundary="----=NEXT.MAIL.FILTER.mailfilter.iomart.com"
//	MESS>> 
//	MESS>> ------=NEXT.MAIL.FILTER.mailfilter.iomart.com
//	MESS>> Content-Type: text/plain; charset="iso-8859-1"
//	MESS>> Content-Transfer-Encoding: 7bit
//	MESS>> 
//	MESS>> An email you sent could not be delivered.
//	MESS>> 	Subject: Reminder to confirm your Job Alert in JobisJob
//	MESS>> 
//	MESS>> Delivery to the following address has failed...
//	MESS>> 	www.shirleyharding1@mypostoffice.co.uk
//	MESS>> 
//	MESS>> 
//	MESS>> Technical information about this permanent failure: 
//	MESS>> 
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean isiomart=false;
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				if(h.getName().compareTo("Received")==0)
				{
					if(h.getValue().matches("(?is)^.*HELO [a-z0-9]+\\.iomart\\.com.*"))
					{
//						System.out.println("IOM.detected!");
						isiomart=true;
						break;
					}
//					else System.out.println(">"+h.getValue());
				}
			}
		} catch (Exception e1) {
			return null;
		}
		
		if(!isiomart)
			return null;
		
		try
		{
			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
				if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
					continue; // not dealing with attachments

				if(bodyPart.getContentType().startsWith("text/plain"))
				{
//					System.out.println("MIME>CT>>"+bodyPart.getContentType());
					Bounce ret=this.TextPlainParser(new BufferedReader(new InputStreamReader(bodyPart.getInputStream())));
					
					if(ret!=null)
						return ret;
				}

			}
		}
		catch(Exception e)
		{
			return this.TextPlainParser(new BufferedReader(new InputStreamReader(message.getInputStream())));
		}
		
		return null;
	}

	private Bounce TextPlainParser(BufferedReader br)
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
//		MESS>> Delivery to the following address has failed...
//		MESS>> 	www.shirleyharding1@mypostoffice.co.uk
		
//		System.out.println("IOM.TP");
		
		try {
			String read = br.readLine();
			
			while(read!=null)
			{
				if(read.startsWith("Delivery to the following address has failed"))
				{
					//seguent linea el mail
					read = br.readLine();
					
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					ret.setHardBounce();
					
					if(!RFC822Parser.getInstance().isEmail(ret.getEmail()))
						return null;
					else return ret;
				}
				read = br.readLine();
			}
		}
		catch(Exception e)
		{
			return null;
		}
		
//		System.out.println("IOM:"+ret.getEmail());
		
		return ret;
	}
	
}

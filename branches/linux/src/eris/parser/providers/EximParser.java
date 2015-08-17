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
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class EximParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

//	HEADER> Return-Path: <>
//	HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 7662 invoked by uid 89); 13 Jun 2013 08:54:14 -0000
//	HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 7660 invoked by uid 89); 13 Jun 2013 08:54:14 -0000
//	HEADER> Received: from mail.pnn.police.uk (212.62.5.146)
//	  by retorn.grupointercom.com with SMTP; 13 Jun 2013 08:54:14 -0000
//	HEADER> Message-ID: <C51b987520000@MAILSWEEP2>
//	HEADER> From: postmaster@northumbria.pnn.police.uk
//	HEADER> To: info-alert@mails.jobisjob.com
//	HEADER> CC: 
//	HEADER> Date: Thu, 13 Jun 2013 09:48:18 +0100
//	HEADER> Subject: Undeliverable Mail: "9 New - School Jobs in Newcastle Upon Tyne from JobisJob United Kingdom"
//	HEADER> MIME-Version: 1.0
//	HEADER> Content-Type: multipart/mixed;
//		boundary="--=7a88634a-62b5-4600-9297-594a66893eb7"
//	HEADER> X-ACL-Warn: X-Virus Scan: F-Secure 9
//	HEADER> X-PNN3-Rtr: dnslookup
//	MIME>CT> text/plain; charset=iso-8859-1
//	MESS>> 
//	MESS>> ----=7a88634a-62b5-4600-9297-594a66893eb7
//	MESS>> Content-Type: text/plain;
//	MESS>> 	charset="iso-8859-1"
//	MESS>> Content-Transfer-Encoding: 7bit
//	MESS>> 
//	MESS>> Your message:
//	MESS>>    From:    info-alert@mails.jobisjob.com
//	MESS>>    Subject: 9 New - School Jobs in Newcastle Upon Tyne from JobisJob United Kingdom
//	MESS>> 
//	MESS>> Could not be delivered because of
//	MESS>> 
//	MESS>> 550 denise.elliott.9036@northumbria.pnn.police.uk... No such user
//	MESS>> 
//	MESS>> The following recipients were affected: 
//	MESS>>     denise.elliott.9036@northumbria.pnn.police.uk
//	MESS>> 
//	MESS>> 
//	MESS>> 
//	MESS>> 
//	MESS>> Additional Information
//	MESS>> ======================
//	MESS>> Original Sender:    <info-alert@mails.jobisjob.com>
//	MESS>> Sender-MTA:         <51.65.225.85>
//	MESS>> Remote-MTA:			<159.147.1.105>
//	MESS>> Reporting-MTA:      <MAILSWEEP2>
//	MESS>> MessageName:        <B51b987520002.000000000001.0001.mml>
//	MESS>> Last-Attempt-Date:  <09:48:18 Thu, 13 June 2013>
//	MESS>> 
//	MESS>> ----=7a88634a-62b5-4600-9297-594a66893eb7--
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean isPoliceUKlike=false;
		
//		Undeliverable Mail
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				if(h.getName().compareTo("Subject")==0)
				{
					if(h.getValue().startsWith("Undeliverable Mail"))
					{
						isPoliceUKlike=true;
					}
				}
			}
			
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(isPoliceUKlike)
		{
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
//						System.out.println("MIME>CT>>"+bodyPart.getContentType());
						Bounce ret=this.PoliceUKlikeTextPlain(new BufferedReader(new InputStreamReader(bodyPart.getInputStream())));
						
						if(ret!=null)
							return ret;
					}

				}
			}
			catch(Exception e)
			{
				return this.PoliceUKlikeTextPlain(new BufferedReader(new InputStreamReader(message.getInputStream())));
			}
		}
		
		return null;
	}
	
	private Bounce PoliceUKlikeTextPlain(BufferedReader br)
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
		try {
			String read = br.readLine();
			boolean getmail=false;
			
			while(read != null)
			{
//				MESS>> Could not be delivered because of
				
				if((getmail)&&(read.compareTo("")==0))
				{
					read = br.readLine();
					continue;
				}
				else if((getmail)&&(read.startsWith("550")))
				{
//					MESS>> 
//					MESS>> 550 denise.elliott.9036@northumbria.pnn.police.uk... No such user
					
//					System.out.println("PoliceUK:"+RFC822Parser.getInstance().getFrom(read));
					
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					
					if(!BounceTypeClassifier.getInstance().ParseReason(read, ret))
					{
						System.out.flush();
						StackTraceElement frame = new Exception().getStackTrace()[0];
						System.err.println("TODO: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+RFC822Parser.getInstance().getFrom(read)+"> identificar tipus FAIL");
						System.err.flush();
						return null;
					}
					
					break;
				}
					
				if(read.startsWith("Could not be delivered because of"))
					getmail=true;
				
				read = br.readLine();
			}
		} 
		catch (Exception e) 
		{
			return null;
		}
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
			return null;
		
//		System.out.println("PoliceUK:<"+ret.getEmail()+">");

		return ret;
	}

}



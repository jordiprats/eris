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

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class B1gMailParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

//	HEADER> X-Mailer: b1gMail/7.2.0
//	HEADER> To: info-alert@mails.jobisjob.com
//	HEADER> From: "Mail Delivery System" <postmaster@hallomail.ch>
//	HEADER> Subject: Returned mail: see error report for details
//	HEADER> Precedence: junk
//	HEADER> Content-Type: multipart/mixed; boundary="--Boundary-=_ca779799419650b6fd3ae98368fd8e8b"
//	HEADER> X-Authenticated-As: post@hallomail.ch
//	MIME>CT> text/plain; charset=us-ascii; report-type=delivery-status
//	MESS>> This is a multi-part message in MIME format.
//	MESS>> 
//	MESS>> ----Boundary-=_ca779799419650b6fd3ae98368fd8e8b
//	MESS>> Content-Type: text/plain; charset="US-ASCII"; report-type="delivery-status"
//	MESS>> Content-Transfer-Encoding: 8bit
//	MESS>> Content-Disposition: inline
//	MESS>> 
//	MESS>> The original message was received at Thu, 13 Jun 2013 09:54:02 +0100.
//	MESS>> 
//	MESS>>    ----- Error description -----
//	MESS>> The message was not delivered to one or more recipients.
//	MESS>> 
//	MESS>>    ----- The message was not delivered to the following addresses -----
//	MESS>> <frenzy77@hallomail.ch>
//	MESS>>    (reason: The user exceeded his storage limit. Please try to send the message again later.)
//	MESS>> 
	
//	HEADER> X-Mailer: b1gMail/7.3.0
//	HEADER> From: "Mail Delivery System" <postmaster@office2go.eu>
//	HEADER> Precedence: junk
//	HEADER> Content-Type: multipart/mixed; boundary="--Boundary-=_f8d3c807af04a78931ba8d69494a07f7"
//	MIME>CT> text/plain; charset=us-ascii; report-type=delivery-status
//	MESS>> This is a multi-part message in MIME format.
//	MESS>> 
//	MESS>> ----Boundary-=_f8d3c807af04a78931ba8d69494a07f7
//	MESS>> Content-Type: text/plain; charset="US-ASCII"; report-type="delivery-status"
//	MESS>> Content-Transfer-Encoding: 8bit
//	MESS>> Content-Disposition: inline
//	MESS>> 
//	MESS>> The original message was received at Fri, 14 Jun 2013 09:13:01 +0200.
//	MESS>> 
//	MESS>>    ----- Error description -----
//	MESS>> No valid recipients for this message could be determined.
//	MESS>> 
//	MESS>> 
//	MESS>> ----Boundary-=_f8d3c807af04a78931ba8d69494a07f7
//	MESS>> Content-Type: message/rfc822; name="returned-message.eml"
//	MESS>> Content-Transfer-Encoding: 8bit
//	MESS>> Content-Disposition: attachment; filename="returned-message.eml"
//	MESS>> 
//	MESS>> Return-Path: <info-alert@mails.jobisjob.com>
//	MESS>> X-Original-To: jil@mybox.de
	
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean isbigmail=false;
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				if(h.getName().compareTo("X-Mailer")==0)
				{
					if(h.getValue().startsWith("b1gMail"))
					{
						isbigmail=true;
					}
				}
			}
		} catch (Exception e1) {
			return null;
		}
		
		if(!isbigmail)
			return null;
		
		System.out.println("bigmail!");
		
		try
		{
			Multipart multipart = (Multipart) message.getContent();
			Bounce ret=null;

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);

				if(bodyPart.getContentType().startsWith("text/plain"))
				{
//					System.out.println("MIME>CT>>"+bodyPart.getContentType());
					ret=this.BigMailTextPlainParser(new BufferedReader(new InputStreamReader(bodyPart.getInputStream())));
				}
				if(bodyPart.getContentType().startsWith("message/rfc822")) //message/rfc822; name="returned-message.eml"
				{
//					System.out.println("MIME>CT>>"+bodyPart.getContentType());
					this.ParseEmail(new BufferedReader(new InputStreamReader(bodyPart.getInputStream())), ret);
				}

			}
			
			if(ret!=null)
				return ret;
			
		}
		catch(Exception e)
		{
			return this.BigMailTextPlainParser(new BufferedReader(new InputStreamReader(message.getInputStream())));
		}
			
		
		return null;
	}
	
	private Bounce BigMailTextPlainParser(BufferedReader br)
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
//		MESS>>    ----- The message was not delivered to the following addresses -----
//		MESS>> <frenzy77@hallomail.ch>
//		MESS>>    (reason: The user exceeded his storage limit. Please try to send the message again later.)
		
		try {
			String read = br.readLine();
			boolean getmail=false;
			
			while(read!=null)
			{
				if(getmail)
				{
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					
					if(!RFC822Parser.getInstance().isEmail(ret.getEmail()))
						return null;
					
					read = br.readLine();
					
					if(read.matches("(?i)^.*The user exceeded his storage limit.*"))
						ret.setSoftBounce(true);
					else
					{
						System.out.flush();
						System.err.println("TODO: "+this.getClass().getName()+"//PlainText: <"+RFC822Parser.getInstance().getFrom(read)+"> identificar tipus FAIL");
						System.err.flush();
						return null;
					}
					
					break;
				}
				
				if(read.matches("(?i)^.*The message was not delivered to the following addresses.*"))
					getmail=true;
				
//				MESS>>    ----- Error description -----
//				MESS>> No valid recipients for this message could be determined.
				if(read.matches("(?is)^[^a-z]+error description[^a-z]+"))
				{
					read = br.readLine();
					if(read.matches("(?is)^.*No valid recipients for this message could be determined.*"))
					{
						ret.setHardBounce();
						return ret; //necesito parsejar el email adjunt
					}
					else
					{
						System.out.flush();
						System.err.println("TODO: "+this.getClass().getName()+"//PlainText: <"+RFC822Parser.getInstance().getFrom(read)+"> bounce curt, no trobo raó");
						System.err.flush();
					}
				}
				
				read = br.readLine();
			}
		}
		catch(Exception e)
		{
			return null;
		}
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
			return null;
		
//		System.out.println("B1gMail:<"+ret.getEmail()+">");
		
		return ret;
	}

	private boolean ParseEmail(BufferedReader br, Bounce ret)
	{
		if(ret==null)
			return false;
		
		try 
		{
			String read = br.readLine();
			
			while(read!=null)
			{
				if(read.matches("(?s)^To: .*") || 
						read.matches("(?s)^X-Original-To: "+GenericParser.emailpattern))
				{
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					
					if(RFC822Parser.getInstance().isEmail(ret.getEmail()))
						return true;
					else
						return false;
				}
				read = br.readLine();
			}
		}
		catch(Exception e)
		{
			return false;
		}
		
		return false;
	}
}

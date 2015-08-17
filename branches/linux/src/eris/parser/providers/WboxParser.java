package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class WboxParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}
	
//	HEADER> Return-Path: <>
//	HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 26515 invoked by uid 89); 9 Aug 2013 05:28:58 -0000
//	HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 26513 invoked by uid 89); 9 Aug 2013 05:28:58 -0000
//	HEADER> Received: from mail.hbl.in (HELO hbl.in) (202.71.156.163)
//	  by retorn.grupointercom.com with SMTP; 9 Aug 2013 05:28:58 -0000
//	HEADER> Received: from exim by hbl.in with local (Exim 4.54 #1)
//		id 1V7fFg-0004Jb-UH
//		for <info-alert@mails.jobisjob.com>; Fri, 09 Aug 2013 10:58:42 +0530
//	HEADER> X-Failed-Recipients: j1334626-43@hbl.in
//	HEADER> Auto-Submitted: auto-generated
//	HEADER> From: Mail Delivery System <Mailer-Daemon@hbl.in>
//	HEADER> To: info-alert@mails.jobisjob.com
//	HEADER> Subject: Your message could not be sent
//	HEADER> Message-Id: <E1V7fFg-0004Jb-UH@hbl.in>
//	HEADER> Date: Fri, 09 Aug 2013 10:58:40 +0530
//	MESS>> 
//	MESS>> This is an automated email generated by Wbox Mail Delivery System.
//	MESS>> You are receiving it because a message that you sent could not be delivered to all of its intended recipients.
//	MESS>> 
//	MESS>> 
//	MESS>> The following address(es) failed:
//	MESS>> 
//	MESS>> 
//	MESS>>   j1334626-43@hbl.in
//	MESS>>     (generated from mygiri@hbl.in)
//	MESS>>     mailbox is full: retry timeout exceeded
//	MESS>> 
//	MESS>> ------ The body of the message is 54151 characters long;
//	MESS>> ------ The message is stripped.
//	MESS>> 

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
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
		
		try 
		{
			String read = br.readLine();
			
			while(read!=null)
			{
				if(read.compareTo("")==0)
				{
					read = br.readLine();
					continue;
				}
				else if(
						read.startsWith("This is an automated email generated by") ||
						read.startsWith("Bardzo nam przykro jednakze nie udalo sie wyslac maila pod wskazane adresy")
						)
				{
					boolean getmail=false;
					boolean getreason=false;
					while(read!=null)
					{
						if(read.compareTo("")==0)
						{
							read = br.readLine();
							continue;
						}
						else if(!getmail)
						{
							if(!getreason)
							{
								if(
									read.startsWith("The following address") ||
									read.startsWith("Przyczyna wskazana jest ponizej")
										)
								{
									getmail=true;
								}
							}
							else
							{
								//getreason
								if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
								{
									return ret;
								}
							}
						}
						else
						{
							//getmail
							String mail=RFC822Parser.getInstance().getFrom(read);
							
							if(RFC822Parser.getInstance().isEmail(mail))
							{
								getmail=false;
								getreason=true;
								ret.setEmail(mail);
							}
						}
						read = br.readLine();
					}
				}
				else return null;
			}
		}
		catch(Exception e)
		{
			return null;
		}
		
		return null;
	}
	
}
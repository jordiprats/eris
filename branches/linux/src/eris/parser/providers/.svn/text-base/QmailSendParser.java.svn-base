package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import eris.parser.Bounce;
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class QmailSendParser implements GenericParser {

//	Return-Path: <root@rathi.com>
//	Delivered-To: noreply-alert@mails.jobisjob.com
//	Received: (qmail 10745 invoked by uid 89); 13 Aug 2013 05:04:55 -0000
//	Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//	Received: (qmail 10742 invoked by uid 89); 13 Aug 2013 05:04:54 -0000
//	Received: from mail.rathi.com (HELO mailserver.rathi.com) (202.87.45.19)
//	  by retorn.grupointercom.com with SMTP; 13 Aug 2013 05:04:54 -0000
//	Received: (qmail 30311 invoked by alias); 13 Aug 2013 10:34:39 +0530
//	Date: 13 Aug 2013 10:34:39 +0530
//	Message-ID: <20130813050439.30310.qmail@mailserver.rathi.com>
//	To: info-alert@mails.jobisjob.com
//	From: postmaster@rathi.com
//	Subject: Recipient does not exist
//
//	Hi,
//
//
//	This is the mailer daemon program at rathi.com.
//
//	I wasn't able to deliver your mail to:sarveshmehta@rathi.com
//
//	This mail ID does not exist in this server.
//
//	Please contact postmaster@rathi.com for more clarifications.
//
//
//	Thanks and Regards,
//	Postmaster

	
//	From: MAILER-DAEMON@bouncehost
//	To: info-alert@mails.jobisjob.com
//	Subject: failure notice
//
//	Hi. This is the qmail-send program.
//	I'm afraid I wasn't able to deliver your message to the following addresses.
//	This is a permanent error; I've given up. Sorry it didn't work out.
//
//	<mireille@danikadairy.com>:
//	Sorry, no mailbox here by that name. (#5.1.1)
//
//	--- Below this line is a copy of the message.

	
//	HEADER> Return-Path: <>
//	HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 30106 invoked by uid 89); 13 Jun 2013 06:27:09 -0000
//	HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 30104 invoked by uid 89); 13 Jun 2013 06:27:09 -0000
//	HEADER> Received: from mx4.wp.pl (212.77.101.8)
//	  by retorn.grupointercom.com with SMTP; 13 Jun 2013 06:27:09 -0000
//	HEADER> Received: (wp-smtpd 5801 invoked for bounce); 13 Jun 2013 08:26:57 +0200
//	HEADER> X-WP-Bounce-Status: Unknown                                                              
//	HEADER> Date: 13 Jun 2013 08:26:57 +0200
//	HEADER> From: MAILER-DAEMON@wp.pl
//	HEADER> To: info-alert@mails.jobisjob.com.
//	HEADER> Subject: problem z dostarczeniem Twojej wiadomosci / failure notice
//	MESS>> Witaj. Ta wiadomosc zostala wygenerowana przez system pocztowy
//	MESS>> Wirtualnej Polski.
//	MESS>> 
//	MESS>> Dostarczenie Twojej wiadomosci okazalo sie niewykonalne, nasz
//	MESS>> serwer pocztowy nie bedzie staral sie wyslac jej ponownie.
//	MESS>> Dokladna przyczyna niedoreczenia listu znajduje sie ponizej.
//	MESS>> ===
//	MESS>> Hi. This message was created automatically by mail system
//	MESS>> at Wirtualna Polska.
//	MESS>> I'm afraid I wasn't able to deliver your message to the following addresses.
//	MESS>> This is a permanent error; I've given up. Sorry it didn't work out.
//	MESS>> 
//	MESS>> <ankabartkowiak@wp.pl>:
//	MESS>> The users mailfolder is over the allowed quota.
	
//	Hi. This is the qmail-send program at p-dc2-pm-email-mbx-a08.ir7.com.br.
//	I'm afraid I wasn't able to deliver your message to the following addresses.
//	This is a permanent error; I've given up. Sorry it didn't work out.
//
//	<fabiroberta@r7.com>:
//	maildrop: Maildir/: Read-only file system

	
	//	This is the mailer daemon program at 
	protected static final String[] qmailsendinitpatterns={
		"(?i)^Hi\\. This is the qmail-send program at.*",
		"(?i)^This is the mailer daemon program at.*",
		"(?i)^Hi\\. This message was created automatically by mail.*"
	};
	
	private Bounce ParseQmailText(InputStream message) throws IOException
	{
		boolean isqmailsend=false;
		
		Bounce ret=null;

		BufferedReader br=new BufferedReader(new InputStreamReader(message));
		String read=br.readLine();

		while(read != null)
		{
//			System.out.println(">"+read);
			if(read.compareTo("")==0)
			{
				read=br.readLine();
				continue;
			}
			
			for(String qmailsendinitpattern : QmailSendParser.qmailsendinitpatterns)
			{
				if(read.matches(qmailsendinitpattern))
					isqmailsend=true;
			}
			
			if(read.matches("(?i)^<"+GenericParser.emailpattern+">:")) //<fabiroberta@r7.com>:
			{
				ret=new Bounce();
				ret.setBounce(true);
				ret.setGeneratorName(this.getClass().getName());
				
				ret.setEmail(RFC822Parser.getInstance().getFrom(read));
				
//				System.err.println("read: "+read);
				
				String readini=read;
				read=br.readLine();
				
				if(!BounceTypeClassifier.getInstance().ParseReason(readini, ret) &&
						!BounceTypeClassifier.getInstance().ParseReason(read, ret))
				{
					String read2=read=br.readLine();
					String read3=read=br.readLine();
					if(!BounceTypeClassifier.getInstance().ParseReason(read2, ret) &&
						!BounceTypeClassifier.getInstance().ParseReason(read3, ret))
					{
						System.out.flush();
						StackTraceElement frame = new Exception().getStackTrace()[0];
						System.err.println("TODO: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+ret.getEmail()+"> identificar tipus FAIL");
						System.err.println("readini: "+readini);
						System.err.println("read   : "+read);
						System.err.println("read2  : "+read2);
						System.err.println("read3  : "+read2);
						System.err.flush();
						ret=null; //per buscar altres raons
					}
				}
				break;
			}
			//I wasn't able to deliver your mail to:sarveshmehta@rathi.com
			
			else if(read.startsWith("I wasn't able to deliver your mail"))
			{
//				Hi,
//
//
//				This is the mailer daemon program at rathi.com.
//
//				I wasn't able to deliver your mail to:sarveshmehta@rathi.com
//
//				This mail ID does not exist in this server.
//
//				Please contact postmaster@rathi.com for more clarifications.
//
//
//				Thanks and Regards,
//				Postmaster
				if(ret==null)
				{
					ret=new Bounce();
					ret.setBounce(true);
					ret.setGeneratorName(this.getClass().getName());
				}
				
				String mail=RFC822Parser.getInstance().getFrom(read);
				
				if(!RFC822Parser.getInstance().isEmail(mail))
				{
					System.out.flush();
					StackTraceElement frame = new Exception().getStackTrace()[0];
					System.err.println("TODO: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+RFC822Parser.getInstance().getFrom(read)+"> no esta detectant el mail on hauria de ser");
					System.err.println("read: "+read);
					System.err.flush();
					return null;
				}
				else ret.setEmail(mail);
				
				read=br.readLine();
				
				while(read!=null)
				{
					if(read.compareTo("")==0)
					{
						read=br.readLine();
						continue;
					}
					else if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
					{
						return ret;
					}
//					System.out.println(read);
					read=br.readLine();
				}
				System.out.flush();
				StackTraceElement frame = new Exception().getStackTrace()[0];
				System.err.println("TODO: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+ret.getEmail()+"> aixo no hauria de pasar");
				System.err.println("read: "+read);
				System.err.flush();
				return null;

			}
			else if(read.matches("(?is)^<.*vacation.*>:"))
			{
//				>Hi. This is the qmail-send program at rediffmail.com.
//				>I'm afraid I wasn't able to deliver your message to the following addresses.
//				>This is a permanent error; I've given up. Sorry it didn't work out.
//				>
//				><bin/vacation "Vacation Mail"@f4mail-234-146.rediffmail.com>:
//				>- Sorry, no mailbox here by that name. (#5.1.1)
				
				//amb un mail vàlid no arriba aquí
				
				return new Bounce(false, this.getClass().getName());
				
			}

			
			String mail=RFC822Parser.getInstance().getFrom(read);
			
			if(read.compareTo(mail)!=0)
			{
				ret=new Bounce();
				ret.setBounce(true);
				ret.setGeneratorName(this.getClass().getName());
				ret.setEmail(mail);
				//System.out.println("MAIL>"+mail);
			}
			
			if(read.startsWith("The users mailfolder is over the allowed quota"))
			{
				ret.setSoftBounce(true);
				break;
			}
			
			if(read.startsWith("Sorry, no mailbox here by that name"))
			{
				ret.setSoftBounce(false);
				break;
			}
			
			
			read=br.readLine();
		}
		
		if(!isqmailsend)
			return null;
		
//		System.out.println(ret.getEmail());s
		return ret;
	}
	
	public Bounce Parse(String from, Message message) throws IOException,MessagingException 
	{
		Bounce ret=null;
//		System.out.println("Parsing with: "+this.getClass().getName());
		
		try
		{
			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
				if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
					continue; // not dealing with attachments
//				System.out.println("MIME>CT>>"+bodyPart.getContentType());
				if(bodyPart.getContentType().compareTo("text/plain")==0)
				{
					ret=this.ParseQmailText(bodyPart.getInputStream());
					
					break;
				}

			}
		}
		catch(Exception e)
		{
			ret=this.ParseQmailText(message.getInputStream());
		}
		
		if(ret==null) //insistim
			ret=this.ParseQmailText(message.getInputStream());
		
		if(ret==null || 
				ret.getEmail()==null || 
				ret.getEmail().compareTo("")==0 )
		{
//			System.out.println("error mail:" +ret.getEmail());
			return null;
		}
		
//		System.out.println(ret.getEmail());
		return ret;
	}

	public boolean isRuleBased() {
		return false;
	}

}

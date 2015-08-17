package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class MailRUParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

//	HEADER> Return-Path: <>
//	HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 22468 invoked by uid 89); 17 Aug 2013 07:22:18 -0000
//	HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 22466 invoked by uid 89); 17 Aug 2013 07:22:17 -0000
//	HEADER> Received: from mx12.mail.ru (94.100.176.138)
//	  by retorn.grupointercom.com with SMTP; 17 Aug 2013 07:22:17 -0000
//	HEADER> Received: from root by mx12.mail.ru with local id 1VAapn-0004lH-2x
//		for info-alert@mails.jobisjob.com; Sat, 17 Aug 2013 11:22:03 +0400
//	HEADER> Auto-Submitted: auto-replied
//	HEADER> From: mailer-daemon@corp.mail.ru
//	HEADER> To: info-alert@mails.jobisjob.com
//	HEADER> Subject: Mail failure - rejected by local scanning code
//	HEADER> Message-Id: <E1VAapn-0004lH-2x@mx12.mail.ru>
//	HEADER> Date: Sat, 17 Aug 2013 11:22:03 +0400
//	MESS>> A message that you sent was rejected by the local scanning code that
//	MESS>> checks incoming messages on this system. The following error was given:
//	MESS>> 
//	MESS>>   Message was not accepted -- invalid mailbox.
//	MESS>>  Local mailbox l_sani@mail.ru is unavailable: user not found
	
	//segon exemple
	
//	MESS>>   Try again later. Error code: FB5DE449B5FABEA688BA676D53225D301BE7C50117610E32F0699BE9115964735A20AFB02E6C45A77BB36056ECD8755A30F0238768B88730.
//	MESS>> 
//	MESS>> ------ This is a copy of your message, including all the headers. ------
//	MESS>> 
//	MESS>> Received: from mail by mx5.mail.ru with local (envelope-from <info-alert@mails.jobisjob.com>)
//	MESS>> 	id 1VCnsO-0005QI-DS
//	MESS>> 	for anastasia.mykhaliova@gmail.com; Fri, 23 Aug 2013 13:41:54 +0400
//	MESS>> X-ResentFrom: <laa0306@mail.ru>
//	MESS>> X-MailRu-Forward: 1
//	MESS>> Return-path: <info-alert@mails.jobisjob.com>
//	MESS>> Authentication-Results: mxs.mail.ru; spf=pass (mx5.mail.ru: domain of mails.jobisjob.com designates 194.116.241.198 as permitted sender) smtp.mailfrom=info-alert@mails.jobisjob.com smtp.helo=push3.jobisjob.com;
//	MESS>> 	 dkim=pass header.i=jobisjob.com
//	MESS>> Received-SPF: pass (mx5.mail.ru: domain of mails.jobisjob.com designates 194.116.241.198 as permitted sender) client-ip=194.116.241.198; envelope-from=info-alert@mails.jobisjob.com; helo=push3.jobisjob.com;
//	MESS>> Received: from [194.116.241.198] (port=54884 helo=push3.jobisjob.com)
//	MESS>> 	by mx5.mail.ru with esmtp (envelope-from <info-alert@mails.jobisjob.com>)
//	MESS>> 	id 1VCnps-0000Ww-8W
//	MESS>> 	for laa0306@mail.ru; Fri, 23 Aug 2013 13:39:18 +0400
//	MESS>> X-Mru-BL: 0:0:1121
//	MESS>> X-Mru-PTR: push3.jobisjob.com
//	MESS>> X-Mru-NR: 1
//	MESS>> X-Mru-OF: Linux (Ethernet or modem)
//	MESS>> X-Mru-RC: ES
//	MESS>> DKIM-Signature: v=1; a=rsa-sha1; c=relaxed; d=jobisjob.com; h=date:from
//	MESS>> 	:reply-to:to:message-id:subject:mime-version:content-type
//	MESS>> 	:list-unsubscribe:list-id; s=stronger; bh=4UzjLKZ0pNTzTCcDJZgdbw
//	MESS>> 	63hpg=; b=Oo4sqBwxFFwx5TR6hO8Fzys2D4EgWBS6hvHWa3IxGLU2uFx/vTHjye
//	MESS>> 	nQHZbMN2ifZlqi33xZgGxWzTpHoL8mY/o0Q2mT0tjuQyWU+LiNzw22VjypqWVaZ+
//	MESS>> 	seOjwbyOb2pkHAghH5Fbrk/tkAnXcGKp5evzDuZLg4m5SHxzmBIVQ=
//	MESS>> Received: (qmail 17819 invoked by uid 503); 23 Aug 2013 09:39:13 -0000
//	MESS>> Received: from unknown (HELO sparrow) (10.12.50.70)
//	MESS>>   by push3.jobisjob.com with SMTP; 23 Aug 2013 09:39:13 -0000
//	MESS>> Date: Fri, 23 Aug 2013 11:39:13 +0200 (CEST)
//	MESS>> From: JobisJob Alert <info-alert@mails.jobisjob.com>
//	MESS>> Reply-To: "JobisJob Alert" <info@jobisjob.com>
//	MESS>> To: "laa0306@mail.ru" <laa0306@mail.ru>
//	MESS>> Message-ID: <1022870085.2760904.1377250753624.JavaMail.jobisjob@sparrow>
//	MESS>> Subject: 6 New - Ukrainian Jobs from JobisJob United Kingdom
//	MESS>> MIME-Version: 1.0
//	MESS>> Content-Type: multipart/alternative; 
//	MESS>> 	boundary="----=_Part_2760902_94922773.1377250753622"
//	MESS>> precedence: bulk
//	MESS>> List-Unsubscribe: <http://www.jobisjob.co.uk/MyAlerts!delete?ref=M7XJXWUDS3TH2>
//	MESS>> List-ID: <info@jobisjob.com>
//	MESS>> Mailing-list: list info-alert@mails.jobisjob.com; contact info@jobisjob.com
//	MESS>> X-Spam: Not detected
//	MESS>> X-Mras: Ok
//	MESS>> X-Mru-Authenticated-Sender: info-alert@mails.jobisjob.com
//	MESS>> X-Spam: Not detected
//	MESS>> X-Mras: TRY_AGAIN
//	MESS>> X-Spam: MrasProbableSpam
//	MESS>> X-Mru-Authenticated-Sender: info-alert@mails.jobisjob.com
	
	
	public Bounce Parse(String from, Message message) throws IOException,MessagingException 
	{
		if(!from.matches("(?is).*[\\.@]mail\\.ru$"))
			return null;
//		else System.out.println("desde RU amb amor");
		
		try 
		{
			Bounce ret=new Bounce();
			ret.setBounce(true);
			ret.setGeneratorName(this.getClass().getName());
			
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read = br.readLine();

			while(read!=null)
			{
				if(read.matches("(?is)^.*Local mailbox "+GenericParser.emailpattern+" is .*"))
				{
					String mail=RFC822Parser.getInstance().getFrom(read);
					
					if(RFC822Parser.getInstance().isUserEmail(mail))
					{
						ret.setEmail(mail);
						
						BounceTypeClassifier.getInstance().ParseReason(read, ret);
						
						return ret;
					}
				}
				else if(read.matches("(?is)^.*Try again later. Error code.*"))
				{
					ret.setReason(read);
					ret.setSoftBounce();
				}
				else if(read.startsWith("To: "))
				{
					String mail=RFC822Parser.getInstance().getFrom(read);
					
					if(RFC822Parser.getInstance().isUserEmail(mail))
					{
						ret.setEmail(mail);
						return ret;
					}
					else return null;
					
				}
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

package eris.parser.providers;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class QuarantineParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

//	HEADER> Return-Path: <noreply@i.ua>
//	HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 26500 invoked by uid 89); 14 Jun 2013 06:18:30 -0000
//	HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 26498 invoked by uid 89); 14 Jun 2013 06:18:30 -0000
//	HEADER> Received: from st07.mi6.kiev.ua (91.198.36.8)
//	  by retorn.grupointercom.com with SMTP; 14 Jun 2013 06:18:30 -0000
//	HEADER> Received: from web07.mi6 ([10.0.0.19] helo=web07.mi6.kiev.ua)
//		by st07.mi6.kiev.ua with smtp (Exim 4.80.1)
//		(envelope-from <noreply@i.ua>)
//		id 1UnNL0-0000V7-23
//		for info-alert@mails.jobisjob.com; Fri, 14 Jun 2013 09:18:18 +0300
//	HEADER> From: quarantine@i.ua
//	HEADER> To: info-alert@mails.jobisjob.com
//	HEADER> Subject: anna1987@ua.fm: =?windows-1251?B?yuDw4O3y6O0=?= (Quarantine)
//	HEADER> Date: Fri Jun 14 09:18:18 2013
//	HEADER> MIME-Version: 1.0
//	HEADER> Content-Type: text/plain; charset="windows-1251"
//	HEADER> Content-Transfer-Encoding: 8bit
//(...)
//	MESS>> 
//	MESS>> ENG
//	MESS>> Your email was placed to the quarantine. If you want your addressee to
//	MESS>> receive it, then press button "Reply" and leave message body unchanged,
//	MESS>> or follow this link:
//	MESS>> http://mail.i.ua/quarantine/?f=info-alert%40mails.jobisjob.com&t=anna1987%40ua.fm&m=51bab5aacf38&c=388598949
//	MESS>> 
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		
		if(from==null)
			return null;
		
		try
		{
			//quarantine@
			if(RFC822Parser.getInstance().getFrom(from).matches("(?i)^quarantine@.*"))
			{
//				System.out.println("Q");
				String email=message.getSubject().replaceAll("(?i)^"+GenericParser.emailmatchpattern+":.*", "$1");
//				System.out.println("Q2:"+email);
				
				if(!RFC822Parser.getInstance().isEmail(email))
					return null;
				else
				{
					Bounce ret=new Bounce();
					ret.setBounce(true);
					ret.setGeneratorName(this.getClass().getName());
					ret.setEmail(email);
					ret.setSoftBounce();
					
					System.out.flush();
					System.err.println("Q:"+(ret.isBouceTypeValid()?"valid":"invalid"));
					System.err.flush();
					
					return ret;
				}
				
			}
		}
		catch(Exception e)
		{
			return null;
		}

		return null;
	}

}

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

public class XGenParser implements GenericParser {
	
	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException,MessagingException 
	{
		boolean isxgen=false;
//		http://www.xgen.in/
		
//		HEADER> Return-Path: <postmaster@hal-india.com>
//		HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//		HEADER> Received: (qmail 3469 invoked by uid 89); 13 Jun 2013 03:34:27 -0000
//		HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//		HEADER> Received: (qmail 3467 invoked by uid 89); 13 Jun 2013 03:34:27 -0000
//		HEADER> Received: from dilmail9.datainfosys.net (202.157.78.21)
//		  by retorn.grupointercom.com with SMTP; 13 Jun 2013 03:34:27 -0000
//		HEADER> Received: From 202.157.83.51[202.157.83.51] by [202.157.78.21] [mx2.datainfosys.com] [mta] with SMTP ID ../InBoxS/20130613/09/26392.704707348392(1371094451965) For Thu Jun 13 09:04:11 IST 2013
//		HEADER> From: postmaster@hal-india.com
//		HEADER> To: <info-alert@mails.jobisjob.com>
//		HEADER> Subject: failure delivery[550 Receipient Not Found] 
//		HEADER> Date: Thu, 13 Jun 2013 09:04:11 +530 (IST)
//		MESS>> 
//		MESS>> Message Undelivered due to following problem
//		MESS>> 
//		MESS>> [550 Receipient Not Found]
//		MESS>> 
//		MESS>> In Response of [RCPT TO: <smimmak.adlko@hal-india.com>]
//		MESS>> 
//		MESS>> 
//		MESS>> 
//		MESS>> 
//		MESS>> Original Message Here ...........
//		MESS>> Rcvd: From[4e54d591576d297aa9b4281a42777882] [194.110.201.70] [smimmak.adlko@hal-india.com] by [202.157.83.51] [mx2.datainfosys.com] [antispam1.7] with SMTP ID 99243.66109401773(1371094398301) For Thu Jun 13 09:03:18 IST 2013
//		MESS>> X_SpamJadoo_Status: User/silent
//		MESS>> X_SpamJadoo_Reason: Friend
//		MESS>> X_SpamJadoo_Jmail: No
//		MESS>> X_SpamJadoo_From: info-alert@mails.jobisjob.com
//		MESS>> X_SpamJadoo_To: smimmak.adlko@hal-india.com
//		MESS>> DKIM-Signature: v=1; a=rsa-sha1; c=relaxed; d=jobisjob.com; h=date:from
//		MESS>> :reply-to:to:message-id:subject:mime-version:content-type
//		MESS>> :list-unsubscribe:list-id; s=stronger; bh=jXBWymxxY8d/hQB9eT1rj/
//		MESS>> oq7vk=; b=TnoTnrwrzdqM2CbQqEbHTZJuIclzIFR5LInHwczxgNQm2ecc92unrW
//		MESS>> tZRqKf2dfkVG+E4JIqCc30GVuVkGd5fyi8B8AGmR8yB2KgS6eJeRf6Tl/CdNGjvT
//		MESS>> 6HO00oqrwN/AchR7SzMDJIrRB1rzSBaXcV1cnbrKEZ3Lxn2y/txZM=
//		MESS>> Received: (qmail 17154 invoked by uid 503); 13 Jun 2013 03:33:13 -0000
//		MESS>> Received: from localhost (HELO sparrow) (127.0.0.1)
//		MESS>> by push2.jobisjob.com with SMTP; 13 Jun 2013 03:33:13 -0000
//		MESS>> Date: Thu, 13 Jun 2013 05:33:13 +0200 (CEST)
//		MESS>> From: JobisJob Alert <info-alert@mails.jobisjob.com>
//		MESS>> Reply-To: "JobisJob Alert" <info@jobisjob.com>
//		MESS>> To: "smimmak.adlko@hal-india.com" <smimmak.adlko@hal-india.com>
//		MESS>> Message-ID: <1692988321.24798281.1371094393486.JavaMail.jobisjob@sparrow>
//		MESS>> Subject: 9 New - Corporate Communication Jobs in Bengaluru from JobisJob
//		MESS>> India
//		MESS>> MIME-Version: 1.0
//		MESS>> Content-Type: multipart/alternative;
//		MESS>> boundary="----=_Part_24798279_1265479359.1371094393484"
//		MESS>> precedence: bulk
//		MESS>> List-Unsubscribe: <http://www.jobisjob.co.in/MyAlerts!delete?ref=P6ZOF2W4PRMB4>
//		MESS>> List-ID: <info@jobisjob.com>
//		MESS>> Mailing-list: list info-alert@mails.jobisjob.com; contact info@jobisjob.com

		try {
			Enumeration<?> headers = message.getAllHeaders();

			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());

//				HEADER> Subject: failure delivery[550 Receipient Not Found] 
				if(h.getName().compareTo("Subject")==0)
				{
					//System.out.println("xfailed");
					//pasem pel parser per si les mosques
					if(h.getValue().startsWith("failure delivery[550 Receipient Not Found]"))
					{
						isxgen=true;
						//System.out.println("SMTP32!");
						break;
					}
				}
			}

		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
		
		if(!isxgen)
			return null;
		
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
		try 
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read=br.readLine();

			int posicio=0;
			while(read != null)
			{
//				MESS>> Message Undelivered due to following problem
//				MESS>> 
//				MESS>> [550 Receipient Not Found]
//				MESS>> 
//				MESS>> In Response of [RCPT TO: <smimmak.adlko@hal-india.com>]

				if((posicio==0)&&(read.startsWith("Message Undelivered due to following problem")))
					posicio=1;
				
				if((posicio==1)&&(read.startsWith("[550 Receipient Not Found]")))
				{
					posicio=2;
					ret.setHardBounce();
				}
				
				if((posicio==2)&&(read.startsWith("In Response of")))
				{
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					break;
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

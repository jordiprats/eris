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

public class MDaemonProParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean ismdaemonpro=false;
		
//		HEADER> Received: from mail.ibisacam.at by mail.ibisacam.at (via RAW) (MDaemon PRO v13.0.5)
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());

				if(h.getName().compareTo("Received")==0)
				{
					if(h.getValue().contains("MDaemon PRO"))
						ismdaemonpro=true;
//					else System.out.println("MDP> "+h.getName() + ": " + h.getValue());
						
				}
			}
			
		} catch (MessagingException e1) {
			return null;
		}
		
		if(!ismdaemonpro)
			return null;
//		else System.out.println("MDaemon PRO");
		
//		HEADER> From: "MDaemon at mail.ibisacam.at" <MDaemon@ibisacam.at>
//		HEADER> Reply-To: MDaemon@ibisacam.at
//		HEADER> Subject: Warning: elisabeth.krueger@ibisacam.at - User unknown!
//		HEADER> To: info-alert@mails.jobisjob.com
//		HEADER> X-MDaemon-Deliver-To: info-alert@mails.jobisjob.com
//		HEADER> Message-ID: <MDAEMON4259201306130918.AA1805044@mail.ibisacam.at>
//		HEADER> Mime-Version: 1.0
//		HEADER> X-Actual-From: MDaemon@ibisacam.at
//		HEADER> X-Return-Path: <>
//		HEADER> Content-Type: multipart/mixed; boundary="0613-0918-05-PART-BREAK"
//		MIME>CT> text/plain; charset=iso-8859-1
//		MIME>CT> message/rfc822; name=pd75000411916.eml
//		MESS>>    The following data may contain sections which represent BASE64 encoded
//		MESS>>    file attachments.  These sections will be unreadable without MIME aware
//		MESS>>    tools.  Seek your system administrator if you need help extracting any
//		MESS>>    files which may be embedded within this message.
//		MESS>> 
//		MESS>> --0613-0918-05-PART-BREAK
//		MESS>> Content-Type: text/plain; charset=iso-8859-1
//		MESS>> Content-Transfer-Encoding: 7bit
//		MESS>> 
//		MESS>> elisabeth.krueger@ibisacam.at - no such user here.
		
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		ret.setFunctionName(new Exception().getStackTrace()[0].getMethodName());
		
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

					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();

					while(read != null)
					{
						if(read.contains(" - no such user here"))
						{
							read=read.replaceAll("^"+GenericParser.emailmatchpattern+".*", "$1");
//							System.out.println(">> "+read);
							ret.setEmail(read);
							ret.setHardBounce();
							
							break;
						}

						read=br.readLine();
					}

					if(ret.getEmail()==null)
						ret=this.ParserMDaemonDSN(new BufferedReader(new InputStreamReader(bodyPart.getInputStream())));
					
					break;
				}

			}
		}
		catch(ClassCastException e)
		{
			return null;
		}
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
			return null;
		}
		
		return ret;
	}
	
	protected Bounce ParserMDaemonDSN(BufferedReader br)
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		ret.setFunctionName(new Exception().getStackTrace()[0].getMethodName());
		
		//

	//TODO:
//	HEADER> Return-Path: <>
//	HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 11968 invoked by uid 89); 11 Sep 2013 10:24:30 -0000
//	HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 11964 invoked by uid 89); 11 Sep 2013 10:24:29 -0000
//	HEADER> Received: from ns3.compusys.co.za (HELO beaconbay.co.za) (196.36.9.155)
//	  by retorn.grupointercom.com with SMTP; 11 Sep 2013 10:24:29 -0000
//	HEADER> X-MDAV-Processed: beaconbay.co.za, Wed, 11 Sep 2013 12:22:08 +0200
//	HEADER> Received: from beaconbay.co.za by beaconbay.co.za (via RAW) (MDaemon PRO v10.0.4)
//		for <info-alert@mails.jobisjob.com>; Wed, 11 Sep 2013 12:22:07 +0200
//	HEADER> Date: Wed, 11 Sep 2013 12:22:07 +0200
//	HEADER> From: "MDaemon at beaconbay.co.za" <MDaemon@beaconbay.co.za>
//	HEADER> Reply-To: noreply@beaconbay.co.za
//	HEADER> Subject: Permanent Delivery Failure
//	HEADER> To: info-alert@mails.jobisjob.com
//	HEADER> X-MDaemon-Deliver-To: info-alert@mails.jobisjob.com
//	HEADER> Message-ID: <MDAEMON9636201309111222.AA2207984@beaconbay.co.za>
//	HEADER> Mime-Version: 1.0
//	HEADER> X-Actual-From: MDaemon@beaconbay.co.za
//	HEADER> X-MDDSN-Message: Yes
//	HEADER> X-Return-Path: <>
//	HEADER> Content-Type: text/plain; charset=iso-8859-1
//	MESS>> --------------------------------------------------------------------------
//	MESS>> MDaemon Delivery Status Notification - http://www.altn.com/dsn/
//	MESS>> --------------------------------------------------------------------------
//	MESS>> 
//	MESS>> The attached message had PERMANENT fatal delivery errors.
//	MESS>> 
//	MESS>> After one or more unsuccessful delivery attempts the attached message has
//	MESS>> been removed from the MDaemon mail queue on this server.  The number and
//	MESS>> frequency of delivery attempts are determined by local configuration.
//	MESS>> 
//	MESS>> --------------------------------------------------------------------------
//	MESS>> YOUR MESSAGE WAS NOT DELIVERED TO ONE OR MORE RECIPIENTS
//	MESS>> --------------------------------------------------------------------------
//	MESS>> 
//	MESS>> --- Session Transcript ---
//	MESS>>  Wed 2013-09-11 12:22:04: Parsing message <xxxxxxxxxxxxxxxxxxxxxxxx\pd50004034668.msg>
//	MESS>>  Wed 2013-09-11 12:22:04: *  From: info-alert@mails.jobisjob.com
//	MESS>>  Wed 2013-09-11 12:22:04: *  To: fiorella@russellinc.co.za
//	MESS>>  Wed 2013-09-11 12:22:04: *  Subject: 10+ new posts in East London created on JobisJob
//	MESS>>  Wed 2013-09-11 12:22:04: *  Size (bytes): 70048
//	MESS>>  Wed 2013-09-11 12:22:04: *  Message-ID: <375702343.10964938.1378894973120.JavaMail.jobisjob@sparrow>
//	MESS>>  Wed 2013-09-11 12:22:04: *  Route slip host: [russell-inc.dyndns.org]
//	MESS>>  Wed 2013-09-11 12:22:04: *  Route slip port: 25
//	MESS>>  Wed 2013-09-11 12:22:04: Attempting SMTP connection to [russell-inc.dyndns.org]
//	MESS>>  Wed 2013-09-11 12:22:04: Attempting SMTP connection to [russell-inc.dyndns.org:25]
//	MESS>>  Wed 2013-09-11 12:22:04: Resolving A record for [russell-inc.dyndns.org] (DNS Server: 168.210.2.2)...
//	MESS>>  Wed 2013-09-11 12:22:05: *  Name server reports domain name unknown
//	MESS>> --- End Transcript ---
	
		return null;
	}

}

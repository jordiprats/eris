package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import eris.parser.Bounce;
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class DeliveryStatusParser implements GenericParser {

	//from: postmaster@hotmail.com
	//MIME>CT>>text/plain; charset=unicode-1-1-utf-7
	//MIME>CT>>message/delivery-status
	//MIME>CT>>message/rfc822

	//from: postmaster@mail.hotmail.com
	protected static final String softfailpattern="(?is)^5\\.0\\.0.*";
	protected static final String hardfailpattern="(?is)^5\\.[1-5]\\.[0-9].*";
	
	protected Bounce ret=null;
	
	protected boolean parse_rfc822=false;
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException
	{
		//if(from.compareToIgnoreCase("postmaster@hotmail.com")==0)
		//System.out.println("Parsing with: "+this.getClass().getName());
		ret=null;
		
		try
		{
			ret=this.PostmasterBounce(message);
		}
		catch(Exception e)
		{
			return null;
		}
		
//		if(ret.isBouceTypeValid())
//		{
//		if(ret.isSoftFail()) System.out.println("DSP-soft");
//		else System.out.println("DSP-hard");
//		}
//		System.out.println("DSP-MAIL:"+ret.getEmail());
//		ret=null;
		
		if(from!=null && ret!=null && from.matches("(?is)^.*@centosoft.de"))
		{
			ret.setEmail(from);
		}
		
		if(ret==null || 
				ret.getEmail()==null || 
				ret.getEmail().compareTo("")==0 || 
				ret.getEmail().compareTo("null")==0 ||
				!RFC822Parser.getInstance().isEmail(ret.getEmail())
				)
			return null;
		else
		{
			if(!RFC822Parser.getInstance().isUserEmail(ret.getEmail()))
			{
				try 
				{
					BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
					String read=br.readLine();

					while(read != null)
					{
						String header=read.replaceAll(GenericParser.headerpattern, "$1");
						String value=read.replaceAll(GenericParser.headerpattern, "$2");
						
						if(header.compareTo("To")==0)
						{
							ret.setEmail(RFC822Parser.getInstance().getFrom(value));
							if(RFC822Parser.getInstance().isUserEmail(ret.getEmail()))
							{
								ret.setFunctionName(new Exception().getStackTrace()[0].getMethodName());
								return ret;
							}
						}
						read=br.readLine();
					}
				}
				catch(Exception e)
				{
					return null;
				}
			}
			return ret;
		}
	}
	
//	Content-Type: text/plain; charset=utf-8
//	Content-Disposition: inline
//	Content-Transfer-Encoding: 8bit
//	
//	Your message was automatically rejected by Dovecot Mail Delivery Agent.
//	
//	The following reason was given:
//	Quota exceeded for gentijan@a1.net
	private Bounce ParserDovecotMDA(Message message)
	{
//		System.out.println("DOVECOT");
//		MESS>> Quota exceeded for hbsusi1@aon.at
		
		boolean isdovecot=false;
		
		//potser mirar el:
//		HEADER> Message-ID: <dovecot-1371108033-927580-0@email.aon.at>
//		si no conté dovecot return null?
		
		boolean isdovecotwoat=false;
		
		try 
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read=br.readLine();

			while(read != null)
			{
				
				//TODO: fixa't q ha canviat la @ per un punt
//				MESS>> Your message to <doylgeri.ath.forthnet.gr> was automatically rejected:
//				MESS>> Quota exceeded (mailbox for user is full)
				if(read.matches("(?i)^Your message to <"+GenericParser.emailpattern+"> was automatically rejected.*"))
				{
					//TODO: acabar!
					//System.out.println("MAIL>"+RFC822Parser.getInstance().getFrom(read));
				}
				
//				[A-Za-z0-9._%+-]
				if(read.matches("(?i)^Your message to <[A-Za-z0-9._%+-]+> was automatically rejected.*"))
				{
					isdovecotwoat=true;
				}
				
				if((isdovecotwoat)&&(read.matches("(?i)^To: .*"+GenericParser.emailpattern+".*")))
				{
//					System.out.println("MAILwoAT>"+RFC822Parser.getInstance().getFrom(read));
					
					if(ret==null)
					{
						ret=new Bounce();
						ret.setBounce(true);
						ret.setGeneratorName(this.getClass().getName());
						ret.setFunctionName(new Exception().getStackTrace()[0].getMethodName());
					}
					
					ret.setSoftBounce(true);
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					return ret;
				}
				
				if(read.matches(".*Dovecot Mail Delivery Agent.*"))
					isdovecot=true;
				
//				if(isdovecot) System.out.println(read);
				
				if((isdovecot)&&(read.startsWith("Quota exceeded for")))
				{
					if(ret==null)
					{
						ret=new Bounce();
						ret.setBounce(true);
						ret.setGeneratorName(this.getClass().getName());
						ret.setFunctionName(new Exception().getStackTrace()[0].getMethodName());
					}
					
					ret.setSoftBounce(true);
					
//					System.out.println("MAIL>"+RFC822Parser.getInstance().getFrom(read));
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					return ret;
				}
				read=br.readLine();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
		return null;
	}
	
	private void ParsePart(InputStream bodyPart, Bounce ret) throws IOException, MessagingException
	{	
		BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart));
		String read=br.readLine();

		while(read != null)
		{
			String header=read.replaceAll(GenericParser.headerpattern, "$1");
			String value=read.replaceAll(GenericParser.headerpattern, "$2");
			
//			if(parse_rfc822 ||(ret.getEmail()!=null && !RFC822Parser.getInstance().isUserEmail(ret.getEmail())) )
			if(parse_rfc822)
			{
				if(header.compareTo("To")==0)
				{
//					System.out.println("PR:TO:"+read+" was:"+ret.getEmail());
					if(!RFC822Parser.getInstance().isEmail(ret.getEmail()))
					{
						ret.setEmail(RFC822Parser.getInstance().getFrom(value));
//						System.out.println("PR:TO:FINAL"+ret.getEmail());
						parse_rfc822=false;
						break;
					}
				}
			}
			
//			System.out.println("DELIVERY>"+header+": "+value);
			//(header.compareTo("Diagnostic-Code")==0) )
			//if(header.compareTo("Diagnostic-Code")==0)
			//	System.out.println("DC>"+value);
			if(header.compareTo("Diagnostic-Code")==0)
			{
				BounceTypeClassifier.getInstance().ParseReason(value, ret);
			}
			
			//Status
			//Status: 5.0.0 (Over quota)
			//Status: 5.2.2
			
			if(header.compareTo("Status")==0)
			{
//				System.out.println("Status>"+header+": "+value);
				if(!BounceTypeClassifier.getInstance().ParseReason(value, ret))
				{
				
				if(value.matches(DeliveryStatusParser.softfailpattern))
				{
//					System.out.println("soft");
					ret.setSoftBounce();
				}
				else if(value.matches(DeliveryStatusParser.hardfailpattern))
				{
//					System.out.println("hard");
					ret.setHardBounce();
				}
//				else System.out.println("???");
				} 
//				System.out.println("resultat: "+ret.toString());
			}
			
			if(header.compareTo("X-bcc")==0)
			{
				//System.out.println("xbccRAWEMAIL>"+value);
				//System.out.println("xbccEMAIL>"+RFC822Parser.getInstance().getFrom(value));
				
				//pasem pel parser per si de cas
				ret.setEmail(RFC822Parser.getInstance().getFrom(value));
				ret.setFunctionName(new Exception().getStackTrace()[0].getMethodName());
			}
			
			if(     (header.compareTo("Final-Recipient")==0)||
					(header.compareTo("Final-recipient")==0)||
					(header.compareTo("Original-Recipient")==0)||
					(header.compareTo("Original-recipient")==0)
					)
			{
				//rfc822;Franck.LeGall@exelisvis.com
				//RFC822; <mgeddie1@nc.rr.com>
				
//				System.out.println("RAWEMAIL>"+value);
//				System.out.println("EMAIL>"+RFC822Parser.getInstance().getFrom(value));
				
//				RAWEMAIL>RFC822; <mgeddie1@nc.rr.com>
//				EMAIL>mgeddie1@nc.rr.com
				if(ret.getEmail()==null || !RFC822Parser.getInstance().isEmail(ret.getEmail()))
				{
					ret.setEmail(RFC822Parser.getInstance().getFrom(value));
					if(!RFC822Parser.getInstance().isEmail(ret.getEmail()))
					{
						//TODO:???
//						MESS>> Final-Recipient: rfc822; kalamidas2.ath.forthnet.gr
						parse_rfc822=true;
//						System.out.println(ret.getEmail());
					}
				}
				
//				System.out.println("BOUNCEMAIL>"+ret.getEmail());
			}
			
			read=br.readLine();
		}
	}
	

	private Bounce PostmasterBounce(Message message) throws IOException, MessagingException
	{
		if(ret==null)
		{
			ret=new Bounce();
			ret.setBounce(true);
			ret.setGeneratorName(this.getClass().getName());
			ret.setFunctionName(new Exception().getStackTrace()[0].getMethodName());
		}
		
		//no existeix
		//		MIME>CT>>message/delivery-status
		//		MIME>> Reporting-MTA: dns;SNT0-MC2-F6.Snt0.hotmail.com
		//		MIME>> Received-From-MTA: dns;jobisjob.com
		//		MIME>> Arrival-Date: Thu, 14 Jun 2012 00:04:15 -0700
		//		MIME>> 
		//		MIME>> Final-Recipient: rfc822;scboekhoudt@hotmail.com
		//		MIME>> Action: failed
		//		MIME>> Status: 5.5.0
		//		MIME>> Diagnostic-Code: smtp;550 Requested action not taken: mailbox unavailable (-949396564:3735:-2147467259)

		//bustia plena
		//		MIME>> Reporting-MTA: dns;BAY0-MC1-F1.Bay0.hotmail.com
		//		MIME>> Received-From-MTA: dns;jobisjob.com
		//		MIME>> Arrival-Date: Thu, 11 Oct 2012 00:54:13 -0700
		//		MIME>> 
		//		MIME>> Final-Recipient: rfc822;kokobar4@hotmail.com
		//		MIME>> Action: failed
		//		MIME>> Status: 5.2.2
		//		MIME>> Diagnostic-Code: smtp;552 5.2.2 This message is larger than the current system limit or the recipient's mailbox is full. Create a shorter message body or remove attachments and try sending it again.

		//DC>smtp;550 Requested action not taken: mailbox unavailable (1998570073:3844:-2147467259)
		
		//Dovecot:
//		Content-Type: message/disposition-notification
//		
//		Reporting-UA: smtp.a1.net; Dovecot Mail Delivery Agent
//		Final-Recipient: rfc822; popuser
//		Original-Message-ID: <1298061531.891463.1371625625855.JavaMail.jobisjob@sparrow>
//		Disposition: automatic-action/MDN-sent-automatically; deleted

		
//		HEADER> Message-ID: <dovecot-1371113114-791653-0@mx-mstr-06.forthnet.prv>
//		HEADER> Date: Thu, 13 Jun 2013 11:45:14 +0300
//		HEADER> From: Mail Delivery Subsystem <postmaster@forthnet.gr>
//		HEADER> To: <info-alert@mails.jobisjob.com>
//		HEADER> MIME-Version: 1.0
//		HEADER> Content-Type: multipart/report; report-type=disposition-notification;
//			boundary="22862/mx-mstr-06.forthnet.prv"
//		HEADER> Subject: Rejected: 2 Neue Jobs - Rezeptionist Empfangsmitarbeiter von JobisJob? Deutschland
//		HEADER> Auto-Submitted: auto-replied (rejected)
//		HEADER> Precedence: bulk
//		MIME>CT> text/plain; charset=utf-8
//		MIME>CT> message/disposition-notification
//		MIME>CT> message/rfc822
//		MESS>> This is a MIME-encapsulated message
//		MESS>> 
//		MESS>> --22862/mx-mstr-06.forthnet.prv
//		MESS>> Content-Type: text/plain; charset=utf-8
//		MESS>> Content-Disposition: inline
//		MESS>> Content-Transfer-Encoding: 8bit
//		MESS>> 
//		MESS>> Your message to <doylgeri.ath.forthnet.gr> was automatically rejected:
//		MESS>> Quota exceeded (mailbox for user is full)
//		MESS>> --22862/mx-mstr-06.forthnet.prv

		boolean isdovecot=false;
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());
				
				if(h.getName().compareTo("Message-ID")==0)
				{
					if(h.getValue().matches("(?i)[^<]*<dovecot-.*"))
						isdovecot=true;
				}
			}
		}
		catch (Exception e)
		{
			isdovecot=false;
		}
		
		try
		{
			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
				//if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
				//	continue; // not dealing with attachments

//				System.out.println("MPDSP>CT>"+bodyPart.getContentType());
				
				//multipart/report; report-type=delivery-status;
				//message/delivery-status; name="deliverystatus.txt"
				//message/delivery-status
				if(
						(bodyPart.getContentType().compareTo("message/rfc822")==0)||
						(bodyPart.getContentType().startsWith("multipart/report; report-type=delivery-status;"))||
						(bodyPart.getContentType().startsWith("message/delivery-status; name=deliverystatus.txt"))||
						(bodyPart.getContentType().startsWith("message/disposition-notification"))||
						(bodyPart.getContentType().compareTo("message/delivery-status")==0)
				)
				{
//					System.out.println("DSP>CT>>"+bodyPart.getContentType());

					///////////////
					this.ParsePart(bodyPart.getInputStream(), ret);
				}
				else if(bodyPart.getContentType().startsWith("text/plain") ||
						bodyPart.getContentType().startsWith("multipart/report")
						)
				{
					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();
					
					while(read!=null)
					{
						BounceTypeClassifier.getInstance().ParseReason(read, ret);
						read=br.readLine();
//						System.out.println("GR>>"+read+" "+ret.getReason());
					}
				}

			}
		}
		catch(Exception e)
		{
//			System.out.println("MPDSP>NO SOC MULTIPART");
			this.ParsePart(message.getInputStream(), ret);
//			System.out.println("MPDSP>NO SOC MULTIPART");
		}
		
		if(!ret.isBouceTypeValid())
		{
			this.ParseReason(message.getInputStream(), ret);
		}
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
			//ultim intent
			this.ParsePart(message.getInputStream(), ret);
			
			/*
			if((ret!=null)&&(!(ret.getEmail().matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"))))
			{
				ret=this.ParserDovecotMDA(message);
				
				if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
					return null;
				
				return null;
			}
			*/
			
			if(isdovecot)
				return this.ParserDovecotMDA(message);
			else if(ret==null || 
					ret.getEmail()==null || 
					ret.getEmail().compareTo("")==0 ||
					ret.getEmail().startsWith("rfc822; ")
					) 
				return this.ParserDovecotMDA(message);
		}

		return ret;
	}

	private void ParseReason(InputStream bodyPart, Bounce ret) throws IOException, MessagingException
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart));
		String read=br.readLine();

		boolean getreason=false;
		while(read != null)
		{
			if(getreason)
			{
				if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
				{
					getreason=false;
					break;
				}
			}
			else if(read.matches("[^a-z]+Transcript of session follows[^a-z]+"))
			{
				getreason=true;
			}
			read=br.readLine();
		}
	}
	
	public boolean isRuleBased() {
		return false;
	}

}

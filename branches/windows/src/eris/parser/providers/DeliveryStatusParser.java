package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class DeliveryStatusParser implements GenericParser {

	//from: postmaster@hotmail.com
	//MIME>CT>>text/plain; charset=unicode-1-1-utf-7
	//MIME>CT>>message/delivery-status
	//MIME>CT>>message/rfc822

	//from: postmaster@mail.hotmail.com
	protected static final String softfailpattern="^5\\.0\\.0.*";
	protected static final String hardfailpattern="^5\\.5\\.0.*";
	

	public Bounce Parse(String from, Message message) throws IOException, MessagingException
	{
		//if(from.compareToIgnoreCase("postmaster@hotmail.com")==0)
		//System.out.println("Parsing with: "+this.getClass().getName());
		
		return this.PostmasterBounce(message);
	}
	
	private void ParsePart(InputStream bodyPart, Bounce ret) throws IOException, MessagingException
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart));
		String read=br.readLine();

		while(read != null)
		{
			String header=read.replaceAll(GenericParser.headerpattern, "$1");
			String value=read.replaceAll(GenericParser.headerpattern, "$2");

			//System.out.println("DELIVERY>"+header+": "+value);
			//TODO: (header.compareTo("Diagnostic-Code")==0) )
			//if(header.compareTo("Diagnostic-Code")==0)
			//	System.out.println("DC>"+value);
			
			//Status
			//Status: 5.0.0 (Over quota)
			//Status: 5.2.2

			if(header.compareTo("Status")==0)
				if(value.matches(DeliveryStatusParser.softfailpattern))
				{
					ret.setSoftFail(true);
					//System.out.println("TYPE>SoftKittyWarmKitty");
				}
			
			if(header.compareTo("X-bcc")==0)
			{
				//System.out.println("xbccRAWEMAIL>"+value);
				//System.out.println("xbccEMAIL>"+RFC822Parser.getInstance().getFrom(value));
				
				//pasem pel parser per si de cas
				ret.setEmail(RFC822Parser.getInstance().getFrom(value));
			}
			
			if((header.compareTo("Final-Recipient")==0)||
					(header.compareTo("Final-recipient")==0)||
					(header.compareTo("Original-Recipient")==0))
			{
				//rfc822;Franck.LeGall@exelisvis.com
				//RFC822; <mgeddie1@nc.rr.com>
				
				//System.out.println("RAWEMAIL>"+value);
				//System.out.println("EMAIL>"+RFC822Parser.getInstance().getFrom(value));
				
//				RAWEMAIL>RFC822; <mgeddie1@nc.rr.com>
//				EMAIL>mgeddie1@nc.rr.com
				
				ret.setEmail(RFC822Parser.getInstance().getFrom(value));
				
				//System.out.println("BOUNCEMAIL>"+ret.getEmail());
			}

			read=br.readLine();
		}
	}
	

	private Bounce PostmasterBounce(Message message) throws IOException, MessagingException
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
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
		

		try
		{
			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
				//if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
				//	continue; // not dealing with attachments

				//System.out.println("MPDSP>CT>"+bodyPart.getContentType());
				
				//multipart/report; report-type=delivery-status;
				//message/delivery-status; name="deliverystatus.txt"
				//message/delivery-status
				if(
						(bodyPart.getContentType().compareTo("message/rfc822")==0)||
						(bodyPart.getContentType().startsWith("multipart/report; report-type=delivery-status;"))||
						(bodyPart.getContentType().startsWith("message/delivery-status; name=deliverystatus.txt"))||
						(bodyPart.getContentType().compareTo("message/delivery-status")==0)
				)
				{
//					System.out.println("DSP>CT>>"+bodyPart.getContentType());

					///////////////
					this.ParsePart(bodyPart.getInputStream(), ret);
				}

			}
		}
		catch(ClassCastException e)
		{
//			System.out.println("MPDSP>NO SOC MULTIPART");
			this.ParsePart(message.getInputStream(), ret);
//			System.out.println("MPDSP>NO SOC MULTIPART");
		}
		
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
			//ultim intent
			this.ParsePart(message.getInputStream(), ret);
			
			if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
				return null;
		}

		return ret;
	}

	public boolean isRuleBased() {
		return false;
	}

}

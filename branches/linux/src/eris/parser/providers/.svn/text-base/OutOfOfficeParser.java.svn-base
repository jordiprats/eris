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

public class OutOfOfficeParser implements GenericParser {

	protected Bounce outofoffice=null;
	
	public OutOfOfficeParser()
	{
		this.outofoffice=new Bounce(false, this.getClass().getName());
		this.outofoffice.setOutOfOffice(true);
	}
	
	public boolean isRuleBased() {
		return false;
	}

//	HEADER> From: donotreply@reedmac.co.uk
//	HEADER> To: info-alert@mails.jobisjob.com
//	HEADER> Date: Thu, 13 Jun 2013 07:32:25 +0100 (BST)
//	HEADER> Subject: RE: 4 New - Carpet Fitter Jobs from JobisJob United Kingdom
//	HEADER> MIME-Version: 1.0
//	HEADER> Content-Type: multipart/mixed; boundary="2456457.25.162"
//	MIME>CT> text/plain; charset=us-ascii
//	MIME>CT> message/rfc822
//	MESS>> 
//	MESS>> --2456457.25.162
//	MESS>> Content-Type: text/plain; charset=US-ASCII
//	MESS>> 
//	MESS>> _________________________________________
//	MESS>> 
//	MESS>> info-alert@mails.jobisjob.com :
//	MESS>> 
//	MESS>> Please Note
//	MESS>> 
//	MESS>> This mailbox has now been closed.
//	MESS>> If you need some assistance,
//	MESS>> please contact us on +44 (0) 20 7246 3333
//	MESS>> 
//	MESS>> _________________________________________
//	MESS>> 
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		this.outofoffice.setEmail(from);
		
		boolean isdonotreplyautoresponse=false;
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());
				
				if(h.getName().compareTo("Subject")==0)
				{
//					Subject: Holiday reply
					if(
						(h.getValue().compareTo("Holiday reply")==0)||
						(h.getValue().matches("(?is)^.*Out of Office.*"))||
						(h.getValue().matches("(?is)^.*Automatic Answer.*"))||
						(h.getValue().startsWith("Vacation reply"))||
						(h.getValue().startsWith("Thank you for your email"))||
						(h.getValue().startsWith("[autoresponder]"))||
						(h.getValue().matches("(?is)^.*Respuesta_autom=E1tica.*"))||
						(h.getValue().matches("(?is)^.*out of the office.*"))||
						(h.getValue().matches("(?is)^.*Resposta_Autom=E1tica.*"))||
						(h.getValue().matches("(?is)^.*Abwesenheitsnotiz.*"))||
						(h.getValue().matches("(?is)^.*COMPANY CLOSED.*"))
//						||(h.getValue().matches("(?i)^.*out[ \\t]+of[ \\t]+the[ \\t]+office"))
						)
						return this.outofoffice;
//					else System.out.println("OoO-X:"+h.getValue());
				}
				else if((h.getName().compareTo("Auto-Submitted")==0) ||
						(h.getName().compareTo("Precedence")==0))
				{
//					HEADER> Auto-Submitted: auto-replied (vacation)
//					Precedence: Vacation
					if(h.getValue().matches("(?is)^.*vacation.*"))
						return this.outofoffice;
				}

					

					
			}
			
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		}
		
//		MESS>> I am away from the office with no access to emails.
//		MESS>>  
//		MESS>> Should you need any assistance please contact my colleague Emma on 0161 838
//		MESS>> 5717 or email emma@taggedresources.com
//		MESS>>  
//		MESS>> Thank you.
		
		try
		{
			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
//				if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
//					continue; // not dealing with attachments

//				System.out.println("OOF-MIME>CT>>"+bodyPart.getContentType());
				
				if((isdonotreplyautoresponse)&&
						(bodyPart.getContentType().startsWith("message/rfc822")))
				{
					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();

					while(read != null)
					{
						if(read.startsWith("To: "))
						{
//							System.out.println(RFC822Parser.getInstance().getFrom(read));
							Bounce ret=new Bounce();
							ret.setBounce(true);
							ret.setGeneratorName(this.getClass().getName());
							ret.setEmail(RFC822Parser.getInstance().getFrom(read));
							ret.setHardBounce();
							
							if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
								return null;
							
							return ret;
						}
						read=br.readLine();
					}
				}
				
				if(bodyPart.getContentType().startsWith("text/plain"))
				{


					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();

					while(read != null)
					{
//						This mailbox has now been closed.
						if((from !=null)&&(from.matches("(?i)^.*donotreply@.*")))
						{
							if(read.startsWith("This mailbox has now been closed."))
								isdonotreplyautoresponse=true;
						}
						
						read=br.readLine();
					}
					
					if(!isdonotreplyautoresponse)
						break;
					else
						this.PlainTextParser(new BufferedReader(new InputStreamReader(bodyPart.getInputStream())));
						
				}

			}
		}
		catch(Exception e)
		{
			return this.PlainTextParser(new BufferedReader(new InputStreamReader(message.getInputStream())));
		}
		
		return null;
	}

	private Bounce PlainTextParser(BufferedReader br)
	{
		try
		{
		String read=br.readLine();

		while(read != null)
		{
			
			if(read.matches("(?is)^.*out of office.*"))
				return this.outofoffice;
//			na razie mnie nie ma (polish: por ahora estoy fuera)
			else if((read.matches("(?i)^.*na razie mnie nie ma.*")))
				return this.outofoffice;
			
			else if(read.matches("(?i)^.* away from the office with no access to email.*$"))
				return this.outofoffice;
					
			read=br.readLine();
		}
		}
		catch(Exception e)
		{
			return null;
		}
		return null;
	}
	
}

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

public class OutOfOfficeParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean isoutlook=false;
		
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
						(h.getValue().startsWith("Out of Office"))
						)
						return new Bounce(false, this.getClass().getName());
				}
//				HEADER> X-Mailer: Microsoft Outlook 14.0
				else if(h.getName().compareTo("X-Mailer")==0)
				{
					if(h.getValue().startsWith("Microsoft Outlook"))
						isoutlook=true;
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
				if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
					continue; // not dealing with attachments

				if(bodyPart.getContentType().startsWith("text/plain"))
				{
//					System.out.println("MIME>CT>>"+bodyPart.getContentType());

					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();

					while(read != null)
					{
//						MESS>> I am away from the office with no access to emails.
//						System.out.println(read);
						if((read.matches("(?i)^.* away from the office with no access to email.*$"))
								&&isoutlook)
							return new Bounce(false, this.getClass().getName());
//							System.out.println(">>>>>OOF");
						
						read=br.readLine();
					}
					
					break;
				}

			}
		}
		catch(ClassCastException e)
		{
			
		}
		
		return null;
	}

}

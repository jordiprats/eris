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
import eris.parser.BounceTypeClassifier;

public class GordanoParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean isGordano=false;
		
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
//				HEADER> X-Mailer: Gordano Messaging Services v18.00.4260
				if(h.getName().compareTo("X-Mailer")==0)
				{
					if(h.getValue().startsWith("Gordano Messaging Services"))
					{
						//System.out.println("MARSHAL: "+h.getValue());
						isGordano=true;
					}
				}
			}
			
		} catch (MessagingException e1) {
//			e1.printStackTrace();
		}
		
		if(!isGordano)
			return null;
		
//		MESS>> --==_11594060719397/mailserver.goldcircle.co.za==_
//		MESS>> Content-Type: text/plain; charset="us-ascii"
//		MESS>> Content-Transfer-Encoding: 7bit
//		MESS>> Content-Disposition: inline
//		MESS>> 

		
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

					boolean getmail=false;
					while(read != null)
					{
//						System.out.println("MIME-Gordano>> "+read);
						
//						MESS>> The requested destination was:
//						MESS>> 
//						MESS>> 	alseiay@goldcircle.co.za
						if(getmail)
						{
							if(read.compareTo("")==0)
							{
								read=br.readLine();
								continue;
							}
							else
							{
								String mail=null;
								if(read.matches("^\\s*"+GenericParser.emailpattern+".*"))
								{
									mail=read.replaceAll("^\\s*"+GenericParser.emailmatchpattern+".*", "$1");
								}
//								else if(read.matches("^\\s*"+GenericParser.userpattern+"\\s+.*"))
//								{
////									MESS>> 	mbathal (User not on post office)
//									mail=read.replaceAll("^\\s*"+GenericParser.usermatchpattern+"\\s+.*", "$1");
//									mail+="@"+domainfrom;
//								}
								else
									return null;
								//System.out.println("mail novell:"+mail);
								if(mail!=null)
									ret.setEmail(mail);
								
								getmail=false; //només per si es modifica la logica en un futur
								break;
							}
						}
						else if(read.startsWith("The requested destination was:"))
						{
//							System.out.println("gordano bounce!");
							getmail=true;
						}
						else BounceTypeClassifier.getInstance().ParseReason(read, ret);
	
						read=br.readLine();
					}
				}

			}
		}
		catch(ClassCastException e)
		{
			return null;
		}
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
			return null;
		
//		System.out.println("LaGorda:<"+ret.getEmail()+">");
		
		return ret;
	}

}

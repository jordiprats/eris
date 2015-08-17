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
import eris.parser.rfc822.RFC822Parser;

public class NovellGroupWiseParser implements GenericParser {

//	X-Mailer: Novell GroupWise Internet Agent 7.0.1
//	X-Mailer: Novell GroupWise Internet Agent 5.5.3.1
//	X-Mailer: NTMail v4.30.0012
//	X-Mailer: Internet Mail Service (5.5.2653.19)
	protected final static String[] xmailerpatterns={
		"Novell GroupWise Internet Agent",
		"Internet Mail Service",
		"NTMail"
	};
	
	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean isnovell=false;
		String domainfrom=null;
		
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
		try
		{
			//podria fer servir el parametre from, pero tindre excepcio igualment
			domainfrom = message.getFrom()[0].toString();
			domainfrom=RFC822Parser.getInstance().getDomain(domainfrom);
			//System.out.println("novell!"+domainfrom);
		}
		catch(Exception e)
		{
			return null;
		}
		
		try {
			Enumeration<?> headers = message.getAllHeaders();

			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());
				
				if(h.getName().compareTo("X-Mailer")==0)
				{					
					for(String pattern : NovellGroupWiseParser.xmailerpatterns)
					{
						if(h.getValue().startsWith(pattern))
						{
							isnovell=true;
							//System.out.println("novell!"+h.getValue());
							break;
						}
					}
				}
				if(isnovell)
					break;
			}

		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
		
		if(!isnovell)
			return null;
		
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

					//System.out.println("MIME>CT>>"+bodyPart.getContentType());

					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();

					boolean getmail=false;
					while(read != null)
					{
//						System.out.println("MIME-NOVELL>> "+read);
						
//						MESS>> The message that you sent was undeliverable to the following:=20
//						MESS>> 
//						MESS>> 	mbathal (User not on post office)

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
								if(read.matches("^\\s*"+GenericParser.emailpattern+"\\s+.*"))
								{
									//codi no comprobat!
									mail=read.replaceAll("^\\s*"+GenericParser.emailmatchpattern+"\\s+.*", "$1");
								}
								else if(read.matches("^\\s*"+GenericParser.userpattern+"\\s+.*"))
								{
//									MESS>> 	mbathal (User not on post office)
									mail=read.replaceAll("^\\s*"+GenericParser.usermatchpattern+"\\s+.*", "$1");
									mail+="@"+domainfrom;
								}
								else
									return null;
								//System.out.println("mail novell:"+mail);
								if(mail!=null)
								{
									ret.setEmail(mail);
									BounceTypeClassifier.getInstance().ParseReason(read, ret);
								}
								
								getmail=false; //només per si es modifica la logica en un futur
								break;
							}
						}

						if(read.startsWith("The message that you sent was undeliverable to the following:"))
						{
							getmail=true;
						}
	
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
		
		return ret;
	}

}

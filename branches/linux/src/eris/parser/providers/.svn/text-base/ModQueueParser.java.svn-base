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

//HEADER> X-MX-Bounce: mod_queue/warn
//MIME>CT> text/plain
//MIME>CT> text/plain
//MESS>> This is a MIME-formatted message.
//MESS>> Portions of this message may be unreadable without a MIME-capable mail program.
//MESS>> 
//MESS>> --Q=_2088973871-1377628904-p01c11m097.mxlogic.net
//MESS>> Content-Type: text/plain
//MESS>> 
//MESS>>       ===============================================
//MESS>>               THIS IS A WARNING MESSAGE ONLY         
//MESS>>           YOU DO NOT NEED TO RESEND YOUR MESSAGE     
//MESS>>       ===============================================
//MESS>> 
//MESS>> A temporary error occurred while delivering to the following address(es):
//MESS>> 
//MESS>>   <pdelorme@eastrock.com>: 452 Validating Sender: 4.3.1 Insufficient system resources

public class ModQueueParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean ismodqueue=false;
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				if(h.getName().compareTo("X-MX-Bounce")==0)
				{
					if(h.getValue().startsWith("mod_queue"))
					{
						ismodqueue=true;
					}
				}
			}
			
		} catch (Exception e) {
		}

		if(!ismodqueue) return null;
		
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
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
						if(read.compareTo("")==0)
						{
							read=br.readLine();
							continue;
						}
						if(getmail)
						{
							String mail=RFC822Parser.getInstance().getFrom(read);
							
							
							
							if(RFC822Parser.getInstance().isUserEmail(mail))
							{
								ret.setEmail(mail);
								BounceTypeClassifier.getInstance().ParseReason(read, ret);
								
								return ret;
							}
							else return null;
							
						}
						else if(read.startsWith("A temporary error occurred while delivering to the following address"))
						{
							getmail=true;
							read=br.readLine();
							continue;
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
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0 ||
				!RFC822Parser.getInstance().isEmail(ret.getEmail()))
		return null;

		return ret;
	}

}

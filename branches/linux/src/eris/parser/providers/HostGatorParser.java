package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import eris.parser.Bounce;
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class HostGatorParser implements GenericParser {

	public boolean isRuleBased() { return false; }

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
		int ishostgator=0;
		
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
						(bodyPart.getContentType().startsWith("text/plain"))
				)
				{
					ishostgator++;
					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();
					
					while(read!=null)
					{
						
						if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
							break;
						
						read=br.readLine();
					}
				}
				else if(
						(bodyPart.getContentType().startsWith("message/rfc822"))
						)
				{
					ishostgator++;
					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();

					boolean to_nextline=false;
					while(read!=null)
					{
						String header=read.replaceAll(GenericParser.headerpattern, "$1");
						String value=read.replaceAll(GenericParser.headerpattern, "$2");

						if(to_nextline)
						{
							ret.setEmail(RFC822Parser.getInstance().getFrom(value));
							if(!RFC822Parser.getInstance().isEmail(ret.getEmail()))
							{
								return null;
							}
							break;
						}
						else if(header.compareTo("To")==0)
						{
//							System.out.println("TO:"+read);
							ret.setEmail(RFC822Parser.getInstance().getFrom(value));
							if(!RFC822Parser.getInstance().isEmail(ret.getEmail()))
							{
								to_nextline=true;
								read=br.readLine();
								continue;
							}
							break;
						}
						
						read=br.readLine();
					}
				}
//				MESS>> Content-Description: Undelivered Message
//				MESS>> Content-Type: message/rfc822

			}
		}
		catch(Exception e)
		{
			return null;
		}
		
		if(ishostgator!=2)
			return null;
		
		if(ret==null || 
				ret.getEmail()==null || 
				ret.getEmail().compareTo("")==0 || 
				ret.getEmail().compareTo("null")==0 ||
				!RFC822Parser.getInstance().isEmail(ret.getEmail())
				)
			return null;
		
//		System.out.println("HG: "+ret.getEmail());
		
		return ret;
	}

}

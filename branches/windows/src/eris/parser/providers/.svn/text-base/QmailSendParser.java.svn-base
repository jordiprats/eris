package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class QmailSendParser implements GenericParser {

	//	This is the mailer daemon program at 
	protected static final String[] qmailsendinitpatterns={
		"(?i)^Hi\\. This is the qmail-send program at.*",
		"(?i)^This is the mailer daemon program at.*"
	};
	
	private Bounce ParseQmailText(InputStream message) throws IOException
	{
		boolean isqmailsend=false;
		
		Bounce ret=null;

		BufferedReader br=new BufferedReader(new InputStreamReader(message));
		String read=br.readLine();

		while(read != null)
		{
			for(String qmailsendinitpattern : QmailSendParser.qmailsendinitpatterns)
			{
				if(read.matches(qmailsendinitpattern))
					isqmailsend=true;
			}
			
			String mail=RFC822Parser.getInstance().getFrom(read);
			
			if(read.compareTo(mail)!=0)
			{
				ret=new Bounce();
				ret.setBounce(true);
				ret.setGeneratorName(this.getClass().getName());
				ret.setEmail(mail);
				//System.out.println("MAIL>"+mail);
				break;
			}
			
			read=br.readLine();
		}
		
		if(!isqmailsend)
			return null;
		
		return ret;
	}
	
	public Bounce Parse(String from, Message message) throws IOException,MessagingException 
	{
		Bounce ret=null;
//		System.out.println("Parsing with: "+this.getClass().getName());
		
		try
		{
			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
				if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
					continue; // not dealing with attachments

				if(bodyPart.getContentType().compareTo("text/plain")==0)
				{
//					System.out.println("MIME>CT>>"+bodyPart.getContentType());

					ret=this.ParseQmailText(bodyPart.getInputStream());
					
					break;
				}

			}
		}
		catch(ClassCastException e)
		{
			//System.out.println("NO SOC MULTIPART");
		}
		
		ret=this.ParseQmailText(message.getInputStream());
		
		if(ret==null || ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
//			System.out.println("error mail:" +ret.getEmail());
			return null;
		}

//		System.out.println(ret.getEmail());
		return ret;
	}

	public boolean isRuleBased() {
		return false;
	}

}

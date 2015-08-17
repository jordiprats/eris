package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import eris.parser.Bounce;


public class XHmXmrParser implements GenericParser {
	
//  from: Yahoo! Mail AntiSpam Feedback <feedback@arf.mail.yahoo.com>
//	Subject: Yahoo! Mail AntiSpam Feedback <feedback@arf.mail.yahoo.com>(Tue Jan 25 09:22:57 CET 2011)
//	MIME>CT>>text/plain; charset="US-ASCII"
//	MIME>CT>>message/feedback-report
//	MIME>CT>>message/rfc822
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException
	{
		//System.out.println("Parsing with: "+this.getClass().getName());
		
		//String from: per si hem de fer-ne algo;
		return this.ReportSpam(message);
	}
	
	private Bounce ReportSpam(Message message) throws MessagingException, IOException
	{
		Bounce ret=new Bounce();
		ret.setSpamReport(true);
		ret.setGeneratorName(this.getClass().getName());
		
		
		
		//(from.compareToIgnoreCase("staff@hotmail.com")==0)||
		try
		{

			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
				if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
					continue; // not dealing with attachments

				if(bodyPart.getContentType().compareTo("message/rfc822")==0)
				{

					//System.out.println("MIME>CT>>"+bodyPart.getContentType());

					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();

					while(read != null)
					{
						//System.out.println("MIME>> "+read);

						if(read.matches(GenericParser.headerpattern))
						{
							//X-HmXmrOriginalRecipient
							//X-OriginalArrivalTime
							String header=read.replaceAll(GenericParser.headerpattern, "$1");
							String value=read.replaceAll(GenericParser.headerpattern, "$2");

							if(header.compareTo("X-HmXmrOriginalRecipient")==0) ret.setEmail(value);
							if(header.compareTo("X-OriginalArrivalTime")==0) ret.setArrivalTime(value);

						}
						read=br.readLine();
					}
				}

			}
		}
		catch(ClassCastException e)
		{
//			System.out.println("NO SOC MULTIPART");
			return null;
		}

		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
			return null;
		
		return ret;
	}

	public boolean isRuleBased() {
		return false;
	}

}

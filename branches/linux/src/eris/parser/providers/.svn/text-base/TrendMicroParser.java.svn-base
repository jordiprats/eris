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
import eris.parser.rfc822.RFC822Parser;

public class TrendMicroParser implements GenericParser {

//	MESS>> The following recipient(s) could not be reached: dck@samicroloan.co.za. 
	protected static final String messpattern="(?i)^The following recipient[^:]*:\\s+"+
									GenericParser.emailpattern+"\\.?.*";
	
	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
//		HEADER> From: no-reply@emailsecurity.trendmicro.com
		String emailfrom=null;
		
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
		try
		{
			emailfrom = RFC822Parser.getInstance().getFrom(message.getFrom()[0].toString());
			//System.out.println("trendmicro!"+emailfrom);
		}
		catch(Exception e)
		{
			return null;
		}
		
		if((emailfrom==null)||(emailfrom.compareTo("no-reply@emailsecurity.trendmicro.com")!=0))
			return null;
		
		//TODO: cmpletar
		
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

					while(read != null)
					{
//						System.out.println("MIME-TrendMicro>>"+read);
//						System.out.println("     regex>>"+TrendMicroParser.messpattern);
						
						if(read.matches(TrendMicroParser.messpattern))
						{
//							System.out.println("MATCH-TrendMicro-MIME!>> "+RFC822Parser.getInstance().getFrom(read));
							ret.setEmail(RFC822Parser.getInstance().getFrom(read));
							break;
						}
						
						read=br.readLine();
					}
				}
				
				if(ret.getEmail()!=null && ret.getEmail().compareTo("")!=0)
					break;
				
			}
		}
		catch(ClassCastException e)
		{
			return null;
		}
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
			return null;
		
		//System.out.println("TrendMicro-resultat: "+ret.getEmail());
		return ret;
	}

}

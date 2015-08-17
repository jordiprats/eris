package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class PostmasterParser implements GenericParser {
	
	protected static final String inidobledash="^-- .*";

	public Bounce Parse(String from, Message message) throws IOException,MessagingException 
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
//		System.out.println("Parsing with: "+this.getClass().getName());
		
		String subject=null;
		try
		{
			subject=message.getHeader("Subject")[0];
		}
		catch(Exception e)
		{
			subject=null;
		}
		if((subject==null) || (subject.compareTo("[Postmaster] Email Delivery Failure")!=0))
		{
			//System.out.println("PRECOND> FAIL!");
			return null;
		}
		
//		HEADER> Subject: [Postmaster] Email Delivery Failure
		
		BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
		String read=br.readLine();

		while(read != null)
		{
			String mail=RFC822Parser.getInstance().getFrom(read);
			if(read.matches(PostmasterParser.inidobledash))
			{
				if(read.compareTo(mail)!=0)
				{
					ret.setEmail(mail);
//					System.out.println("MAIL>"+mail);
					break;
				}	
			}
			
			read=br.readLine();
		}
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
//			System.out.println("error mail:" +ret.getEmail());
			return null;
		}
		
		return ret;
	}

	public boolean isRuleBased() {
		return false;
	}

}

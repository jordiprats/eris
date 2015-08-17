package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class PineAppParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	@Override
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean ispineapp=false;
		Bounce ret=new Bounce();
		ret.setSpamReport(true);
		ret.setGeneratorName(this.getClass().getName());
		
//		HEADER> X-PineApp-Mail-Rcpt-To: info-alert@mails.jobisjob.com
		
		try {
			Enumeration<?> headers = message.getAllHeaders();

			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());

				if(h.getName().compareTo("X-PineApp-Mail-Rcpt-To")==0)
				{
					//System.out.println("xfailed");
					//pasem pel parser per si les mosques
					ispineapp=true;

					//System.out.println("PineApp!");
				}
			}

		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
		
		if(!ispineapp)
			return null;

		//LINEA A LINEA
		try 
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read=br.readLine();

//			MESS>> The message you have sent to valerie@weaversworld.co.za was Quarantined.
			while(read != null)
			{
				if(read.matches("The message you have sent to "+GenericParser.emailpattern+" was Quarantined."))
				{
					ret.setEmail(
							RFC822Parser.getInstance().getFrom(
							read.replaceAll("The message you have sent to "+GenericParser.emailmatchpattern+" was Quarantined.", "$1")
							)
							);
					
				}
				read=br.readLine();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
			return null;
		
		//System.out.println("PINEAPP: "+ret.getEmail());
		return ret;
	}

}

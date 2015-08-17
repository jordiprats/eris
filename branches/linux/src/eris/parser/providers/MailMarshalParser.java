package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class MailMarshalParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean isMailMarshal=false;
		
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
//		Received: from MailMarshal.Sender ([127.0.0.1]) by asmmin.bcxisp.co.za with MailMarshal (v7,0,2,4629)
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("MMHEADER> "+h.getName() + ": " + h.getValue());
				if(h.getName().compareTo("Received")==0)
				{
					if(h.getValue().contains("MailMarshal"))
					{
						//System.out.println("MARSHAL: "+h.getValue());
						isMailMarshal=true;
					}
				}
			}
			
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(!isMailMarshal)
			return null;
		
//		MESS>> The following recipients were affected: 
//		MESS>>     nmhlobo@edcon.co.za
		//&&
//		MESS>> The following message was not delivered:
//		MESS>> 
//		MESS>>    From:    info-alert@mails.jobisjob.com
//		MESS>>    To:      thandeka.nkosi@lanxess.com
//		MESS>>    Subject: New jobs have been added in Richards Bay since your last search	
		
//		MESS>> Please take note that pmtwazi@autopage.altech.co.za does not exist in the=
//		MESS>> =20Autopage email directory.
//		MESS>> For assistance please contact us on 0861 23 24 24
		
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read=br.readLine();
			
			boolean getmail=false;
			boolean getreason=false;
			while(read != null)
			{
				if(getreason)
				{
//					System.out.println("MM:"+read);
					if(read.compareTo("")==0) //em salto lineas en blanc
					{
						read=br.readLine();
						continue;
					}
					else if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
					{
						getreason=false;
					}
					else if(read.startsWith("Delivery Failed"))
					{
						ret.setHardBounce();
					}
					
				}
				
				if(getmail)
				{
//					System.out.println("MM:"+read);
					if(read.compareTo("")==0) //em salto lineas en blanc
					{
						read=br.readLine();
						continue;
					}
					else if(read.matches("(?i)^\\s*to:\\s*"+GenericParser.emailpattern+".*"))
					{
//						System.out.println("MailMarshall LanXess: "+RFC822Parser.getInstance().getFrom(read));
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						getmail=false;
						getreason=true;
						continue;
					}
					else if(read.matches(GenericParser.emailpattern))
					{
						//
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						getmail=false;
						getreason=true;
						continue;
					}
					else if(read.matches("(?i)^[ \\t]*"+GenericParser.emailpattern))
					{
						//
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						getmail=false;
						getreason=true;
						continue;
					}
					else
					{
						//si no es res, ignoro la linea
						read=br.readLine();
						continue;
					}
				}
				
				if(read.matches("(?is)^Please take note that "+GenericParser.emailpattern+" does not exist.*"))
				{
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					ret.setHardBounce();
					break;
				}
				
//				System.out.println("MM>> "+read);
				else if(read.startsWith("The following recipients were affected:"))
				{
					getmail=true;
				}
				
//				MESS>> The following message was not delivered:
				else if(read.startsWith("The following message was not delivered:"))
				{
					getmail=true;
				}
//				else System.out.println("--"+read);
				
//				MESS>> Could not be delivered because of
//				MESS>> 
//				MESS>> 550 5.1.1 User unknown
				if(read.startsWith("Could not be delivered because"))
					getreason=true;
				
				read=br.readLine();
			}
			
			
			} catch (Exception e) {
				//e.printStackTrace();
			}
		
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
			//System.out.println("error mail:" +ret.getEmail());
			return null;
		}
		
//		if(ret.isSoftFail())
//			System.out.println("MM-soft");
//		else System.out.println("MM-hard");
//		System.out.println("MM-MAIL:"+ret.getEmail());
	
		if(isMailMarshal) return ret;
		else return null;
	}

}

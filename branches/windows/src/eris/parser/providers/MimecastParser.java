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

public class MimecastParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		boolean ismimecast=false;

		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());

		//		X-MC-System
		try {
			Enumeration<?> headers = message.getAllHeaders();

			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());

				if(h.getName().compareTo("X-MC-System")==0)
				{
					//System.out.println("xfailed");
					//pasem pel parser per si les mosques
					ismimecast=true;

//					System.out.println("MIMECAST!");
				}
			}

		} catch (MessagingException e1) {
			e1.printStackTrace();
		}

		if(!ismimecast)
			return null;

		//LINEA A LINEA
		try 
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read=br.readLine();

			//		MESS>> This is a delivery failure notification message indicating that
			//		MESS>> an email you addressed to email address :
			//		MESS>> -- knaiker@eqstra.co.za

			//		MESS>> Additional information follows :
			//		MESS>> -- 550 5.1.1 User unknown
			boolean getmail=false;
			while(read != null)
			{
				if(getmail)
				{
					getmail=false;
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					//System.out.println("MIMECAST>EMAIL> "+ret.getEmail());
				}
				else if(read.compareTo("an email you addressed to email address :")==0)
				{
					getmail=true;
				}
				//System.out.println("MIMECAST>> "+read);

				read=br.readLine();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 

		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
//			System.out.println("error mail:" +ret.getEmail());
			return null;
		}

		return ret;
	}

}

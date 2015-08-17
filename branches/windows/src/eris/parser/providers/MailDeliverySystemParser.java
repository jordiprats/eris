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
import eris.parser.rfc822.RFC822Parser;

public class MailDeliverySystemParser implements GenericParser {

	public static final String[] frompattern={
		"(?i)^Mail Delivery System <MAILER-DAEMON@.*",
		"(?i)^Mail Delivery Subsystem <mailer-daemon@.*",
		"(?i)^Mailer-Daemon@.*"
	};
	//from: Mail Delivery Subsystem <MAILER-DAEMON@mailrelay12.libero.it>
	//from: Mail Delivery Subsystem <MAILER-DAEMON@mailrelay30.libero.it>
	//      Mail Delivery Subsystem <mailer-daemon@googlemail.com>
	//      Mail Delivery System <Mailer-Daemon@venga.parys.co.za>
	//      Mailer-Daemon@
	//and so on...
	
//	MESS>> Content-Type: text/plain
//	MESS>> X-MXL-NoteHash: 50ed8db749174fb9-e724db91437801c3b505aec5f0c8862eb5245b5d
//	MESS>> 
//	MESS>> This message was created automatically by mail delivery software.
//	MESS>> 
//	MESS>> A message that you have sent could not be delivered to one or more
//	MESS>> recipients.  This is a permanent error.  The following address failed:
//	MESS>> 
//	MESS>>   <mw957k@att.com>: 550 5.1.1 <mw957k@att.com>... User unknown
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException
	{
		boolean isfailurenotice=false;
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
//		System.out.println("Parsing with: "+this.getClass().getName());
		
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());
				
				if(h.getName().compareTo("X-Failed-Recipients")==0)
				{
					//System.out.println("xfailed");
					//pasem pel parser per si les mosques
					ret.setEmail(h.getValue());
					
					//System.out.println("xfailed: "+ ret.getEmail());
				}
				
				if(h.getName().compareTo("Subject")==0)
				{
					if(h.getValue().compareTo("Message Status - Delivered")==0)
					{
						ret.setBounce(false);
						return ret;
					}
					else if(h.getValue().compareTo("failure notice")==0)
						isfailurenotice=true;
				}
			}
			
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
					//System.out.println("MIME>CT>>"+bodyPart.getContentType());

					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();

					while(read != null)
					{
						String mail=RFC822Parser.getInstance().getFrom(read);
						
						if(read.compareTo(mail)!=0)
							ret.setEmail(mail);
							//System.out.println("MAIL>"+mail);
						
						read=br.readLine();
					}
					
					break;
				}

			}
		}
		catch(ClassCastException e)
		{
			//			System.out.println("MDS: NO SOC MULTIPART");

			//			MESS>> Your mail message to the following address(es) could not be delivered. This
			//			MESS>> is a permanent error. Please verify the addresses and try again. If you are
			//			MESS>> still having difficulty sending mail to these addresses, please contact
			//			MESS>> Customer Support at 480-624-2500.
			//			MESS>> 
			//			MESS>> <jobs@expinfo.com>:
			//			MESS>> child status 100...The e-mail message could not be delivered because the user's mailfolder is full.
			if(isfailurenotice)
			{
				BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
				String read=br.readLine();

				boolean getmail=false;
				while(read != null)
				{
					if(getmail)
					{
						if(read.compareTo("")==0) //em salto lineas en blanc
						{
							read=br.readLine();
							continue;
						}
						else if(read.matches("(?i)^<"+GenericParser.emailpattern+">:.*"))
						{
//							MESS>> <jobs@expinfo.com>:
//							System.out.println("MDS>>>>"+RFC822Parser.getInstance().getFrom(read));
							ret.setEmail(RFC822Parser.getInstance().getFrom(read));
							getmail=false;
							break;
						}
						else
						{
							//si no es res, ignoro la linea
							read=br.readLine();
							continue;
						}
					}

					//				Your mail message to the following address(es) could not be delivered.
					if(read.matches("(?i)^Your mail message to the following address\\(es\\) could not be delivered\\..*"))
					{
						getmail=true;
					}

					//				MESS>> <jobs@expinfo.com>:

					read=br.readLine();
				}
			}


		}
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
//			System.out.println("error mail:" +ret.getEmail());
			return null;
		}
		
		return ret;
	}

	public boolean isRuleBased() {
		return true;
	}
	
}

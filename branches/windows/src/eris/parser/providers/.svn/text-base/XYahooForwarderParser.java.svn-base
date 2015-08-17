package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.mail.Message;
import javax.mail.MessagingException;
import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class XYahooForwarderParser implements GenericParser {

	@Override
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
//		System.out.println("Parsing with: "+this.getClass().getName());

		return this.XYahooForwarder(message);
	}

	private Bounce XYahooForwarder(Message message) throws IOException, MessagingException
	{
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());


		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
			String read=br.readLine();

			while(read != null)
			{
				String header=read.replaceAll(GenericParser.headerpattern, "$1");
				String value=read.replaceAll(GenericParser.headerpattern, "$2");

				//System.out.println("DELIVERY>"+header+": "+value);
				//TODO: (header.compareTo("Diagnostic-Code")==0) )
				//if(header.compareTo("Diagnostic-Code")==0)
				//	System.out.println("DC>"+value);

				//Status
				//Status: 5.0.0 (Over quota)
				//Status: 5.2.2

				if(header.compareTo("Status")==0)
					if(value.matches(DeliveryStatusParser.softfailpattern))
					{
						ret.setSoftFail(true);
						//System.out.println("TYPE>SoftKittyWarmKitty");
					}

				if((header.compareTo("X-Yahoo-Forwarded")==0)||
						(header.compareTo("X-Yahoo-ForwardOnly")==0))
				{
//					RAWEMAIL>from ianbosch@ymail.com to ian@lynxsecurity.co.za
//					ens hem de quedar amb: ianbosch@ymail.com

					if(value.matches("(?i)^from "+GenericParser.emailpattern+
											" to "+GenericParser.emailpattern))
					{
						String mail=null;
						mail=value.replaceAll("(?i)^from ("+GenericParser.emailpattern+
											") to "+GenericParser.emailpattern, "$1");
//						System.out.println("RAWEMAIL>"+value);
//						System.out.println("EMAIL>"+RFC822Parser.getInstance().getFrom(mail));



						ret.setEmail(RFC822Parser.getInstance().getFrom(mail));
					}
					if((ret.getEmail()!=null)&&(ret.getEmail().compareTo("")!=0))
						break;
					//System.out.println("BOUNCEMAIL>"+ret.getEmail());
				}

				read=br.readLine();
			}
		}
		catch(ClassCastException e)
		{
//			System.out.println("NO SOC MULTIPART");
		}

		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
			//System.out.println("error mail:" +ret.getEmail());
			return null;
		}

//		System.out.println("X-YAHOO-FORWARDED-verificar mail:" +ret.getEmail());
		return ret;
	}

	public boolean isRuleBased() {
		return false;
	}

}

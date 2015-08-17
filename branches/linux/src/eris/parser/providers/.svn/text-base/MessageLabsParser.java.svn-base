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

import eris.parser.Bounce;
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class MessageLabsParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException,MessagingException 
	{
		if(from==null)
			return null;

		if(from.compareTo("MAILER-DAEMON@messagelabs.com")!=0)
			return null;

		//		HEADER> From: MAILER-DAEMON@messagelabs.com
		//		HEADER> To: info-alert@mails.jobisjob.com
		//		HEADER> Subject: failure notice

		//		HEADER> Message-ID: <20130613071232.22109.qmail@server-4.tower-39.messagelabs.com>
		//		HEADER> Subject: Your email was not delivered
		//		MESS>> Please note that this email address is no longer valid and your email has not been delivered. Following the transfer of services to Walsall Council, please resend your email to the same recipient name, but use ‘@edu.walsall.gov.uk’ instead of ‘@walsallcs.serco.com’. If you are still unsuccessful, please contact edcreception@edu.walsall.gov.uk

		boolean ismessagelabs=false;

		try {
			Enumeration<?> headers = message.getAllHeaders();

			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());

				if(h.getName().compareTo("Message-ID")==0)
				{
					if(h.getValue().matches("(?i)^.*messagelabs.com>$"))
					{
						//						HEADER> Message-ID: <20130613071232.22109.qmail@server-4.tower-39.messagelabs.com>
						//						System.out.println("ML-fuckyou");
						ismessagelabs=true;
						break;
					}
				}
				else if(h.getName().compareTo("Subject")==0)
				{
					//System.out.println("xfailed");
					//pasem pel parser per si les mosques
					if(h.getValue().startsWith("failure notice"))
					{
						ismessagelabs=true;
						break;
					}
				}
				else if(h.getName().compareTo("X-Msg-Ref")==0)
				{
					//					X-Msg-Ref: server-14.tower-49.messagelabs.com!1380610529!23650946!1
					if(h.getValue().matches("(?i)^.*\\.messagelabs.com!.*"))
					{
						ismessagelabs=true;
						break;
					}
				}
			}

		} catch (MessagingException e1) {
			e1.printStackTrace();
		}

		if(!ismessagelabs)
			return null;

		//		MESS>> This is the mail delivery agent at messagelabs.com.
		//		MESS>> I was not able to deliver your message to the following addresses.
		//		MESS>> 
		//		MESS>> <kavita.garde@barclays.com>:
		//		MESS>> 157.83.125.16 does not like recipient.
		//		MESS>> Remote host said: 550 Mailbox unavailable or access denied - <kavita.garde@barclays.com>

		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());

//		

		try
		{
			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
				if(
						(bodyPart.getContentType().startsWith("text/rfc822-headers")) ||
						(bodyPart.getContentType().startsWith("message/delivery-status"))
				)
				{
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
						else if(
								(header.compareTo("Diagnostic-Code")==0) ||
								(header.compareTo("Diagnostic-code")==0)
								)
						{
							System.out.println("DC:"+value);
							if(BounceTypeClassifier.getInstance().ParseReason(value, ret))
								break;
							
						}
						else System.out.println(read);

						read=br.readLine();
					}
				}
			}
		}
		catch(Exception e) { /* si excepció, continuo */ }
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0 ||
				!RFC822Parser.getInstance().isEmail(ret.getEmail())
		)
		{

//			System.out.println("Hola MESSAGELABS - text plain");
			//cos del mail
			try 
			{
				BufferedReader br=new BufferedReader(new InputStreamReader(message.getInputStream()));
				String read=br.readLine();
				boolean getmail=false;

				while(read != null)
				{
					if(getmail)
					{
						if(read.compareTo("")==0)
						{
							read=br.readLine();
							continue;
						}
						else 
						{
							String mail=null;
							//<kavita.garde@barclays.com>:
							if(read.matches("(?i)^<"+GenericParser.emailpattern+">.*"))
							{
								mail=RFC822Parser.getInstance().getFrom(read);
							}
							else return null;

							//						System.out.println("mail ML:"+mail);
							if(mail!=null)
								ret.setEmail(mail);
							
							String reason_previ=read;
							read=br.readLine();
							
							if(!BounceTypeClassifier.getInstance().ParseReason(read, ret))
							{
								BounceTypeClassifier.getInstance().ParseReason(reason_previ, ret);
							}

							getmail=false; //només per si es modifica la logica en un futur
							break;

						}
					}
					else if(read.matches("(?i)^I was not able to deliver your message to the following addresses.*"))
					{
						getmail=true;
					}

					read=br.readLine();
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			} 

		}
		
		if(ret.getEmail()==null || ret.getEmail().compareTo("")==0 ||
				!RFC822Parser.getInstance().isEmail(ret.getEmail())
						)
		return null;


		return ret;
	}

}

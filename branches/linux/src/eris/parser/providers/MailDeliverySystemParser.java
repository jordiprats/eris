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

public class MailDeliverySystemParser implements GenericParser {

	public static final String[] frompattern={
		"(?is)^Mail Delivery System <MAILER-DAEMON@.*",
		"(?is)^Mail Delivery Subsystem <mailer-daemon@.*",
		"(?is)^Mailer-Daemon@.*",
		"(?is)^.*Delivery Subsystem.*<postmaster@.*",
		"(?is)^Mail Delivery Subsystem <postmaster@.*",
		"(?is)^Mail Administrator [^<]*<MAILER-DAEMON[^>]*>.*"
	};
//	HEADER> From: Mail Delivery Subsystem <postmaster@forthnet.gr>
	//From: Mail Delivery System <Mailer-Daemon@mk-delivery-2.b2b.uk.tiscali.com>
	//
	//from: Mail Delivery Subsystem <MAILER-DAEMON@mailrelay12.libero.it>
	//from: Mail Delivery Subsystem <MAILER-DAEMON@mailrelay30.libero.it>
	//      Mail Delivery Subsystem <mailer-daemon@googlemail.com>
	//      Mail Delivery System <Mailer-Daemon@venga.parys.co.za>
	//      Mailer-Daemon@
	//HEADER> From: "Delivery Subsystem" <postmaster@xsinfoways.com>
//	HEADER> From: MAILER-DAEMON@mx05.mdcs.at
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
	
	
//	MESS>> This message was created automatically by mail delivery software.
//	MESS>> 
//	MESS>> A message that you sent could not be delivered to one or more of its
//	MESS>> recipients. This is a permanent error. The following address(es) failed:
//	MESS>> 
//	MESS>>   an undisclosed address
//	MESS>>     (generated from mgillingham@toucansurf.com)
	
//	HEADER> From: "Delivery Subsystem" <postmaster@xsinfoways.com>
//	HEADER> To: <info-alert@mails.jobisjob.com>
//	HEADER> Subject: Message Delivery Failure
//	HEADER> Date: Thu, 13 Jun 2013 11:55:30 +0530
//	HEADER> Message-ID: <0EE6ECEAA23244A5A88DB98AD589354B.MAI@777networks.com>
//	HEADER> X-MEFilter: 1
//	HEADER> Precedence: bulk
//	MESS>> 
//	MESS>> MailEnable: Message could not be delivered to some recipients.
//	MESS>> The following recipient(s) could not be reached:
//	MESS>> 
//	MESS>> 	Recipient: [SMTP:dsharma@xsinfosol.com]
//	MESS>> 	Reason: Remote SMTP Server Returned: 550 SPF: 67.227.236.64 is not allowed to send mail from mails.jobisjob.com
//	MESS>> 
//	MESS>> 
	
//	HEADER> Return-Path: <MAILER-DAEMON@mx05.mdcs.at>
//	HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 5379 invoked by uid 89); 13 Jun 2013 07:14:03 -0000
//	HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//	HEADER> Received: (qmail 5376 invoked by uid 89); 13 Jun 2013 07:14:03 -0000
//	HEADER> Received: from mx05.mdcs.at (217.13.183.17)
//	  by retorn.grupointercom.com with SMTP; 13 Jun 2013 07:14:03 -0000
//	HEADER> Subject: Mail Delivery Notification
//	HEADER> Message-Id: <20130613071351.8B2CA2F6BF3@mx05.mdcs.at>
//	HEADER> Date: Thu, 13 Jun 2013 09:13:51 +0200 (CEST)
//	HEADER> From: MAILER-DAEMON@mx05.mdcs.at
//	HEADER> To: undisclosed-recipients:;
//	MESS>> This is the Postfix program at host mx05.mdcs.at.
//	MESS>> I'm sorry to have to inform you that the message returned
//	MESS>> below could not be delivered to one or more destinations.
//	MESS>> 
//	MESS>> The following recipients are unknown:
//	MESS>> 
//	MESS>> 	christoph.koerner@uniqa.at
//	MESS>> 
//	MESS>> For further assistance, please send mail to <postmaster>.
//	MESS>> If you do so, please include this problem report. You can
//	MESS>> delete your own text from the message returned below.
	
//	HEADER> From: <MAILER-DAEMON@fullertonindia.com>
//	HEADER> To: <info-alert@mails.jobisjob.com>
//	HEADER> Subject: failure notice
//	HEADER> Return-Path: <>
//	HEADER> Message-ID: <FCWM01uaq5BzC533iJ0000276f6@smtp.fullertonindia.com>
//	HEADER> X-OriginalArrivalTime: 13 Jun 2013 07:32:24.0435 (UTC) FILETIME=[25D95430:01CE6808]
//	HEADER> MIME-Version: 1.0
//	HEADER> Content-Type: text/plain
//	MESS>>  Delivery has failed to these recipients or groups .
//	MESS>> 
//	MESS>> <penta.banka.rout@fullertonindia.com>:
//	MESS>> Sorry, no mailbox here by that name. (#5.1.1)
	
//	HEADER> Subject: Mail delivery failed: returning message to sender
//	MESS>> This message was created automatically by mail delivery software.
//	MESS>> 
//	MESS>> A message that you sent could not be delivered to one or more of its
//	MESS>> recipients. This is a permanent error. The following address(es) failed:
//	MESS>> 
//	MESS>>   save to inbox
//	MESS>>     generated by andreajaehne@01019freenet.de
//	MESS>>     mailbox is full: retry timeout exceeded
	
//	MESS>> |------------------------- Failed addresses follow: ---------------------|
//	MESS>>  <edith-samland@t-online.de>
//	MESS>>    552 5.2.2 <599002574263-0001> Quota exceeded (mailbox for user is full)
//	MESS>> 
	
	
//	MESS>> This message was created automatically by mail delivery software.
//	MESS>> 
//	MESS>> A message that you sent could not be delivered to one or more of its
//	MESS>> recipients. This is a permanent error. The following address(es) failed:
//	MESS>> 
//	MESS>>   an undisclosed address
//	MESS>>     (generated from 56hill@uwclub.net)
//	MESS>> 
//	MESS>> The following text was generated during the delivery attempt:
//	MESS>> 
//	MESS>> ------ an undisclosed address
//	MESS>>        (generated from 56hill@uwclub.net) ------
//	MESS>> 
//	MESS>> Mail quota exceeded.
//	MESS>> 
//	MESS>> ------ This is a copy of the message, including all the headers. ------
	
//	HEADER> From: Mail Delivery Subsystem <mailer-daemon@googlemail.com>
//	HEADER> To: info-alert@mails.jobisjob.com
//	HEADER> X-Failed-Recipients: lucinda.love@eu.averydennison.com
//	HEADER> Subject: Delivery Status Notification (Failure)
//	HEADER> Message-ID: <001a1133046cdc989a04e36cfe4a@google.com>
//	HEADER> Date: Thu, 08 Aug 2013 10:19:38 +0000
//	HEADER> Content-Type: text/plain; charset=ISO-8859-1
//	HEADER> Content-Transfer-Encoding: quoted-printable
//	MESS>> Delivery to the following recipient failed permanently:
//	MESS>> 
//	MESS>>      lucinda.love@eu.averydennison.com
//	MESS>> 
//	MESS>> Technical details of permanent failure: 
//	MESS>> Google tried to deliver your message, but it was rejected by the server for the recipient domain cluster9.us.messagelabs.com by mail190.messagelabs.com. [216.82.249.51].
//	MESS>> 
//	MESS>> The error that the other server returned was:
//	MESS>> 550-Invalid recipient <lucinda.love@eu.averydennison.com>
//	MESS>> 550 (#5.1.1)
	
//	MESS>> Delivery to the following recipient has been delayed:
//		MESS>> 
//		MESS>>      karolinbeaver@spamfree24.org
//		MESS>> 
//		MESS>> Message will be retried for 2 more day(s)
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException
	{
		boolean isplaintextmessage=false;
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
					
//					System.out.println("xfailed: "+ ret.getEmail());
				}

				if(h.getName().compareTo("Subject")==0)
				{
					if(h.getValue().compareTo("Message Status - Delivered")==0)
					{
						ret.setBounce(false);
						return ret;
					}
					else if(h.getValue().compareTo("failure notice")==0)
						isplaintextmessage=true;
					else if(h.getValue().startsWith("Mail delivery failed: returning message to sender"))
						isplaintextmessage=true;
					else if(h.getValue().compareTo("Message Delivery Failure")==0)
						isplaintextmessage=true;
//					Subject: Mail Delivery Notification
					else if(h.getValue().startsWith("Mail Delivery Notification"))
						isplaintextmessage=true;
					else if(h.getValue().startsWith("Delivery failure"))
						isplaintextmessage=true;
					else if(h.getValue().startsWith("Warning: message delayed 2 days"))
						isplaintextmessage=true;
				}
			}
			
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(isplaintextmessage)
		{
//			System.out.println("MDS: PT");
			ret=this.ParsePlainText(new BufferedReader(new InputStreamReader(message.getInputStream())));
		}
		else
		{
			try
			{
				Multipart multipart = (Multipart) message.getContent();

				for (int i = 0; i < multipart.getCount(); i++)
				{
					BodyPart bodyPart = multipart.getBodyPart(i);
//					if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
//						continue; // not dealing with attachments

					if(bodyPart.getContentType().compareTo("text/plain")==0)
					{
						//System.out.println("MIME>CT>>"+bodyPart.getContentType());

						BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
						
						ret=this.ParsePlainText(br);

						if(!(ret==null || ret.getEmail()==null || ret.getEmail().compareTo("")==0))
							break;
					}

				}
			}
			catch(Exception e)
			{
//				System.out.println("MDS: NO SOC MULTIPART");
				ret=this.ParsePlainText(new BufferedReader(new InputStreamReader(message.getInputStream())));
			}
		}
		
//		System.out.println("ret mail:" +ret.getEmail());
		
		if(ret==null || ret.getEmail()==null || ret.getEmail().compareTo("")==0)
		{
//			System.out.println("error mail:" +ret.getEmail());
			return null;
		}

		//		System.out.println(ret.getEmail());
		return ret;
	}

	public boolean isRuleBased() {
		return true;
	}
	
	private Bounce ParsePlainText(BufferedReader br)
	{
//		System.out.println("MDS-PT-parser");
		//		MESS>> Your mail message to the following address(es) could not be delivered. This
		//			MESS>> is a permanent error. Please verify the addresses and try again. If you are
		//			MESS>> still having difficulty sending mail to these addresses, please contact
		//			MESS>> Customer Support at 480-624-2500.
		//			MESS>> 
		//			MESS>> <jobs@expinfo.com>:
		//			MESS>> child status 100...The e-mail message could not be delivered because the user's mailfolder is full.

		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());

		try 
		{
			String read = br.readLine();

			boolean permanentError=false;
			
			boolean getmail=false;
			boolean getreason=false;
			boolean getmailwithreason=false;
			while(read != null)
			{
//				System.out.println(">>"+read);
				if(getmail)
				{
//					System.out.println(">>"+read);
					if(read.compareTo("")==0) //em salto lineas en blanc
					{
						read=br.readLine();
						getmail=true;
						continue;
					}
					else if(read.matches("(?is)^.*Message contents follow.*"))
					{
						read=br.readLine();
						while(read != null)
						{
							String header=read.replaceAll(GenericParser.headerpattern, "$1");
							String value=read.replaceAll(GenericParser.headerpattern, "$2");
							
							if(header.compareTo("To")==0)
							{
								String mailretornat=RFC822Parser.getInstance().getFrom(value);
								
								if(RFC822Parser.getInstance().isEmail(mailretornat))
								{
									ret.setEmail(mailretornat);
									break;
								}
							}
							
							read=br.readLine();
						}
					}
					else if(read.matches("(?i)^.*The following address[^a-z]*es[^a-z] failed.*"))
					{
//						System.out.println("FOLLAR");
						
//						recipients. This is a permanent error. The following address(es) failed:
						if(read.matches("(?i)^.*This is a permanent error.*"))
						{
							permanentError=true;
						}
						
						getmail=true; getmailwithreason=true;
					}
					else if(read.matches("(?i).*Recipient: .SMTP:"+GenericParser.emailpattern+".*"))
					{
						//						MESS>> 	Recipient: [SMTP:dsharma@xsinfosol.com]
						//System.out.println(">>"+RFC822Parser.getInstance().getFrom(read));
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						getmail=false;
						//						break; --> ho comento pq pot no ser el mail q toca (haver-hi un fwd pel mig)

						boolean returnedmailfound=false;
						read=br.readLine();
						while(read != null)
						{
							if(read.matches("(?is)^.*Message contents follow.*"))
							{
								read=br.readLine();
								while(read != null)
								{
									String header=read.replaceAll(GenericParser.headerpattern, "$1");
									String value=read.replaceAll(GenericParser.headerpattern, "$2");

									if(header.compareTo("To")==0)
									{
										String mailretornat=RFC822Parser.getInstance().getFrom(value);
										returnedmailfound=true;

										if(RFC822Parser.getInstance().isEmail(mailretornat))
										{
											ret.setEmail(mailretornat);
											break;
										}
									}

									read=br.readLine();
								}
							}
							else
							{
								BounceTypeClassifier.getInstance().ParseReason(read, ret);
							}
							
							if(returnedmailfound) break;
							else read=br.readLine();
						}
						break;
					}
					else if(read.matches("(?i)^[ \\t]*<"+GenericParser.emailpattern+">.*"))
					{
						//						MESS>> <jobs@expinfo.com>:
						//						System.out.println("MDS>>>>"+RFC822Parser.getInstance().getFrom(read));
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						getmail=false;
						
						if(getmailwithreason)
						{
							read=br.readLine();
							if(!BounceTypeClassifier.getInstance().ParseReason(read, ret))
							{
								StackTraceElement frame = new Exception().getStackTrace()[0];
								System.out.println("TODO: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+RFC822Parser.getInstance().getFrom(read)+"> identificar tipus FAIL");
								return null;
							}
//							System.out.println(read);
						}
						else if(!BounceTypeClassifier.getInstance().ParseReason(read, ret))
						{
							read=br.readLine();
							while(read!=null)
							{
								if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
									break;
								read=br.readLine();
							}
							break;
						}
						
//						System.out.println(":)");
						
						break;
					}
					else if(read.matches("(?i)^[ \\t]*\\(generated from "+GenericParser.emailpattern+"\\).*"))
					{
						//						MESS>>   an undisclosed address
						//						MESS>>     (generated from mgillingham@toucansurf.com)
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						getmail=false;
						getreason=true;
//						System.out.println("XXX");
					}
					else if(read.matches("(?i)^[ \\t]*generated by "+GenericParser.emailpattern+".*"))
					{
						//						MESS>>   an undisclosed address
						//						MESS>>     (generated from mgillingham@toucansurf.com)
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						getmail=false;
						
						read=br.readLine();
						if(!BounceTypeClassifier.getInstance().ParseReason(read, ret))
						{
							ret=null; //per buscar altres raons
//							StackTraceElement frame = new Exception().getStackTrace()[0];
//							System.err.println("TODO: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+RFC822Parser.getInstance().getFrom(read)+"> identificar tipus FAIL - "+read);
//							System.err.flush();
						}
						
						break;
					}
					else if(read.matches("(?i)^[ \\t]*"+GenericParser.emailpattern+":"))
					{
						
						ret.setEmail(read.replaceAll("^"+GenericParser.emailmatchpattern+":", "$1"));
						
						if(!RFC822Parser.getInstance().isEmail(ret.getEmail()))
						{
							System.out.flush();
							StackTraceElement frame = new Exception().getStackTrace()[0];
							System.err.println("AQUI UNA CAGADA: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+ret.getEmail()+"> identificar tipus FAIL");
							System.err.println("Lectura: "+read);
							System.err.flush();
							return null;
						}
						
						read=br.readLine();
						
						if(!BounceTypeClassifier.getInstance().ParseReason(read, ret))
						{
							ret=null; //per buscar altres raons
							StackTraceElement frame = new Exception().getStackTrace()[0];
							System.out.flush();
							System.err.println("TODO: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+RFC822Parser.getInstance().getFrom(read)+"> identificar tipus FAIL");
							System.err.flush();
						}
						
						getmail=false;
						break;
					}
					else if(read.matches("(?i)^[ \\t]*"+GenericParser.emailpattern))
					{
						//					System.out.println("MDS>>>>"+RFC822Parser.getInstance().getFrom(read));
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						
						if(!RFC822Parser.getInstance().isUserEmail(ret.getEmail()))
						{
							read=br.readLine();
							getmail=true;
							continue;
						}
							
						
						if(!RFC822Parser.getInstance().isEmail(ret.getEmail()))
						{
							System.out.flush();
							StackTraceElement frame = new Exception().getStackTrace()[0];
							System.err.println("AQUI UNA CAGADA: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+ret.getEmail()+"> identificar tipus FAIL");
							System.err.println("Lectura: "+read);
							System.err.flush();
							return null;
						}
						
						getreason=true;
						
						getmail=false;
						continue;
					}
					else if(read.matches("(?is)^[ \\t]*\\(ultimately generated from "+GenericParser.emailpattern+".*"))
					{//(ultimately generated from mo.klose@utanet.at)
//						System.out.println("MDS>>>>"+RFC822Parser.getInstance().getFrom(read));
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						getmail=false;
						getreason=true;
					}
					else if(read.matches("(?is)^\""+GenericParser.emailpattern+"\".*"))
					{
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						getmail=false;
						getreason=true;
					}
					else
					{
//						System.out.println("--"+read);
						//si no es res, ignoro la linea
						read=br.readLine();
						continue;
					}
				}

				//				Your mail message to the following address(es) could not be delivered.
				if(read.matches("(?i)^Your mail message to the following address\\(es\\) could not be delivered\\..*"))
					getmail=true;
				//				This is a permanent error. The following address(es) failed:
				else if(read.matches("(?i)^.*This is a permanent error\\. The following address\\(es\\) failed.*"))
					getmail=true;

				else if(read.matches("(?i)^The following recipient\\(s\\) could not be reached.*"))
					getmail=true;

				else if(read.startsWith("The following recipients are unknown:"))
					getmail=true;
				
				else if(read.matches("(?i)^[ \\t]*Delivery to the following recipient failed permanently.*"))
					getmail=true;

				else if(read.matches("(?i)^[ \\t]*Delivery has failed to these recipients or groups.*"))
					getmail=true;

				else if(read.matches("(?i)^[ \\t]*This message was created automatically by .*"))
					getmail=true;
				
				else if(read.matches("(?is)^.*Delivery to the following recipient has been delayed.*"))
					getmail=true;
				
				else if(read.matches("(?i)^.*Failed addresses follow.*"))
				{
					getmail=true; getmailwithreason=true;
//					System.out.println("FAF");
				}				
				else if(read.matches("(?i)^The mailbox "+GenericParser.emailpattern+" doesn't exist.*"))
				{
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					ret.setHardBounce();
//					System.out.println(">>--"+ret.getEmail());
					break;
				}
//				Attached message can not be delivered to irene.hodder@newhamhealth.nhs.uk.
				else if(read.matches("(?i)^[ \\t]*Attached message can not be delivered to "+GenericParser.emailpattern+".*"))
				{
//					System.out.println(">>--"+RFC822Parser.getInstance().getFrom(read));
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					getreason=true;
					read=br.readLine();
					continue;
				}
				else if(read.matches("(?i)^5.1.0 - .*"))
//					5.1.0 - Unknown address error 550-'german.ariasecheverri@tcs.com... No such user'
				{
					if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
					{
						ret.setEmail(RFC822Parser.getInstance().getFrom(read));
						break;
					}
				}
				else if(read.matches("(?i)^Your message to <"+GenericParser.emailpattern+"> was automatically rejected.*"))
				{
//					System.out.println(">>--"+RFC822Parser.getInstance().getFrom(read));
					ret.setEmail(RFC822Parser.getInstance().getFrom(read));
					getreason=true;
					read=br.readLine();
					continue;
				}
				else if(read.matches("(?is)^.*The following text was generated during the delivery attempt.*"))
				{
//					System.out.println("HELLOWORLD");
					read=br.readLine();
					while(read!=null)
					{
						if(read.matches("(?i)^[ \\t]*\\(generated from "+GenericParser.emailpattern+"\\).*"))
						{
//							System.out.println(">"+read);
							ret.setEmail(RFC822Parser.getInstance().getFrom(read));
//							System.out.println(">>"+ret.getEmail());
						}
						else if(read.matches("(?is)^Mail.*"))
						{
//							System.out.println(">"+read);
							if(!BounceTypeClassifier.getInstance().ParseReason(read, ret))
							{
								StackTraceElement frame = new Exception().getStackTrace()[0];
								System.out.flush();
								System.err.println("TODO: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+RFC822Parser.getInstance().getFrom(read)+"> identificar tipus FAIL");
								System.err.flush();
								ret=null; //per buscar altres raons
								return ret;
							}
							else return ret;
						}
//						else System.out.println(">"+read);
						read=br.readLine();
					}
				}
				else if(read.startsWith("Requested action not taken"))
				{
					if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
						break;
				}
//				else System.out.println("--"+read);

				if(getreason)
				{
					while(read!=null)
					{
						if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
						{
							return ret;
						}
//						------ This is a copy of the message, including all the headers. ------
						else if(read.matches("(?is)^[^a-zA-Z]*This is a copy of the message, including all the headers[^a-zA-Z]*"))
						{
							if(permanentError)
							{
								ret.setReason("This is a permanent error");
								ret.setHardBounce();
								return ret;
							}
							else return null;
						}
							
						read=br.readLine();
					}
					ret=null; //per buscar altres raons
					StackTraceElement frame = new Exception().getStackTrace()[0];
					System.out.flush();
					System.err.println("TODO: "+this.getClass().getName()+"//"+frame.getLineNumber()+": <"+RFC822Parser.getInstance().getFrom(read)+"> identificar tipus FAIL");
					System.err.flush();
					return ret;
				}
				
				//				MESS>> <jobs@expinfo.com>:

				read=br.readLine();
			}
		} 
		catch (Exception e) 
		{
			return null;
		}

//		System.out.println("MDS-broken-arrow: "+ret.getEmail());
		
		if(ret==null || ret.getEmail()==null || ret.getEmail().compareTo("")==0)
			return null;
		
//		System.out.println("MDS-broken-arrow: "+ret.getEmail());
		return ret;
	}

}

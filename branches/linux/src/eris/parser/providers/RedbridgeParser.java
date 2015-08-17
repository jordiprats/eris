package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;
import eris.parser.rfc822.RFC822Parser;

public class RedbridgeParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

//	HEADER> From: <ict.servicedesk@redbridge.gov.uk>
//	HEADER> To: <info-alert@mails.jobisjob.com>
//	HEADER> Subject: =?utf-8?B?QXV0b05vdGlmeTogMjEgTmV3IC0gV2VzdGZpZWxkIEpvYnMgaW4gTG9uZG9uIGZyb20gSm9iaXNKb2IgVW5pdGVkIEtpbmdkb20=?=
//	HEADER> Content-Type: multipart/mixed;boundary="----=_NextPart_SEF_2013_08_08_11_01_54_23865"
//	HEADER> X-SEF-Processed: 7_3_0_1200__2013_08_08_11_01_54
//	MESS>> ------=_NextPart_SEF_2013_08_08_11_01_54_23865
//	MESS>> Content-Type: text/plain;charset="utf-8"
//	MESS>> Content-Transfer-Encoding: quoted-printable
//	MESS>> 
//	MESS>> Dear Sir/Madam=0D=0A=0D=0AYour e-mail from info-alert@mails.jobisjob.com se=
//	MESS>> nt to hameeda.saeed@redbridge.gov.uk has been quarantined by the London Bor=
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		if(from==null)
			return null;
		
		if(!from.matches("(?is)^.*@redbridge.gov.uk.*"))
			return null;

		return this.PlainTextParser(new BufferedReader(new InputStreamReader(message.getInputStream())));
	}

	private Bounce PlainTextParser(BufferedReader br)
	{
		try
		{
			String read=br.readLine();

			while(read != null)
			{
//				nt to hameeda.saeed@redbridge.gov.uk has been quarantined by the London Bor=
				if(read.matches("(?is)^.*has been quarantined.*"))
				{
					String mail=RFC822Parser.getInstance().getFrom(read);
					
					if(RFC822Parser.getInstance().isEmail(mail))
					{
						Bounce ret=new Bounce();
						ret.setBounce(true);
						ret.setGeneratorName(this.getClass().getName());
						ret.setEmail(mail);
						ret.setSoftBounce();
						
						return ret;
					}
					else return null;
				}

				read=br.readLine();
			}
		}
		catch(Exception e)
		{
			return null;
		}
		return null;
	}
	
}

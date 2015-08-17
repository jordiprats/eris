package eris.parser.providers;

import java.io.IOException;
import java.util.Enumeration;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;

public class MordentParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

//	HEADER> Return-Path: <mailer-daemon@send006.mrsend.it>
//	HEADER> Delivered-To: news@mail.niumba.com
//	HEADER> Received: (qmail 1256 invoked by uid 89); 17 Jun 2013 15:47:59 -0000
//	HEADER> Received: from send006.mrsend.it (213.92.8.142)
//	  by retorn.grupointercom.com with SMTP; 17 Jun 2013 15:47:59 -0000
//	HEADER> Date: Mon, 17 Jun 2013 17:47:48 +0200
//	HEADER> From: Invitacion personalizada <infoes@mordent.ch>
//	HEADER> To: news@mail.niumba.com
//	HEADER> Subject: =?iso-8859-15?Q?Gastar=20menos=20para=20tu=20ropa=20es=20posible=20con?=
//	 =?iso-8859-15?Q?=20Privalia?=
//	HEADER> MIME-Version: 1.0
//	HEADER> Content-Type: multipart/alternative; boundary="_----------=_1371481355302270"
//	HEADER> List-unsubscribe: <http://mordent.ch/unsubscribefr.php?em=news@mail.niumba.com>
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		try {
			Enumeration<?> headers = message.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				//System.out.println("HEADER> "+h.getName() + ": " + h.getValue());
				
				if(h.getName().compareTo("List-unsubscribe")==0)
				{
					if(h.getValue().contains("http://mordent.ch/unsubscribe"))
						return new Bounce(false, this.getClass().getName());
				}
					
			}
			
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		}
		
		return null;
	}

}

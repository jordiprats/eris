package eris.parser.providers;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;

public class TopFaceParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

//	HEADER> Return-Path: <noreply@topface.com>
//	HEADER> Delivered-To: news@mail.niumba.com
//	HEADER> Received: (qmail 21352 invoked by uid 89); 20 Jun 2013 11:25:53 -0000
//	HEADER> Received: from omx12.topface.com (91.236.181.93)
//	  by retorn.grupointercom.com with SMTP; 20 Jun 2013 11:25:53 -0000
//	HEADER> To: news@mail.niumba.com
//	HEADER> DKIM-Signature: v=1; a=rsa-sha256; c=relaxed/simple; d=topface.com;
//		s=smtpapi; t=1371727541;
//		bh=3+WoZXG4vfZ8uLThZlWKDsDOiXFGQSGIChxdCKeBj04=;
//		h=Subject:From:List-Unsubscribe:Date;
//		b=Bg9j/N0fm9DuRPBzpdpwu9h5SjNPn/5EOkmLZbTvVK4gjdz+4NKfXSQ7ZCdgXsLmE
//		 gK82piOJ8t+XyetBHKQWXGZ4/p3sJFVJRqg9cpUT+Vo+py9Qi78Xvxv9cjn+aa6JE3
//		 c78dEcvT3KUtRQnBUsWu4A6oVv6M+C8d5gf6Fe7Q=
//	HEADER> Subject: =?utf-8?B?4p2kIE1hbnVlbCBoYSBkZWphZG8gdW4gcmVnYWxvIHBhcmEgdGkgZW4gVG9wZmFjZS4=?=
//	HEADER> Mime-Version: 1.0
//	HEADER> From: Topface <noreply@topface.com>
//	HEADER> List-Unsubscribe: <mailto:unsubscribe_29917652_1371727538_8a90a47_invite@topface.com?subject=VW5zdWJzY3JpYmUgbWUh>
	
	@Override
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		try
		{
//			noreply@topface.com
			if(message.getFrom()[0].toString().contains("noreply@topface.com"))
				return new Bounce(false, this.getClass().getName());
				
		}
		catch(Exception e)
		{
			return null;
		}
			                  
		return null;
	}

}

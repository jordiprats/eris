package eris.parser.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import eris.parser.Bounce;
import eris.parser.BounceTypeClassifier;
import eris.parser.rfc822.RFC822Parser;

public class DeliveryNotificationHTMLParser implements GenericParser {

	public boolean isRuleBased() {
		return false;
	}

	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{	
		Bounce ret=new Bounce();
		ret.setBounce(true);
		ret.setGeneratorName(this.getClass().getName());
		
		try
		{
			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
				//if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
				//	continue; // not dealing with attachments

//				System.out.println("MPDSP>CT>"+bodyPart.getContentType());
				
				//multipart/report; report-type=delivery-status;
				//message/delivery-status; name="deliverystatus.txt"
				//message/delivery-status
				if(
						(bodyPart.getContentType().startsWith("text/html"))&&
						(bodyPart.getDescription().startsWith("Notification"))
				)
				{
					//parsejar el HTML
					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();
					
					while(read!=null)
					{
						read=read.replaceAll("\\<[^>]*>","").replaceAll("[^\\x00-\\x7F]", "");
						
//						System.out.println("DNHTML>>"+read);
						
						if(BounceTypeClassifier.getInstance().ParseReason(read, ret))
							break;
						
						read=br.readLine();
					}
				}
				else if(
						(bodyPart.getContentType().startsWith("message/rfc822"))&&
						(bodyPart.getDescription().startsWith("Undelivered Message"))
						)
				{
					BufferedReader br=new BufferedReader(new InputStreamReader(bodyPart.getInputStream()));
					String read=br.readLine();

					while(read!=null)
					{
						String header=read.replaceAll(GenericParser.headerpattern, "$1");
						String value=read.replaceAll(GenericParser.headerpattern, "$2");

						if(header.compareTo("To")==0)
						{
//							System.out.println("TO:"+read);
							ret.setEmail(RFC822Parser.getInstance().getFrom(value));
							break;
						}
						read=br.readLine();
					}
				}
//				MESS>> Content-Description: Undelivered Message
//				MESS>> Content-Type: message/rfc822

			}
		}
		catch(Exception e)
		{
			return null;
		}
		
		if(ret==null || 
				ret.getEmail()==null || 
				ret.getEmail().compareTo("")==0 || 
				ret.getEmail().compareTo("null")==0 ||
				!RFC822Parser.getInstance().isEmail(ret.getEmail())
				)
			return null;	
//			System.out.println("email:"+ret.getEmail());
		
		
		return ret;
	}

}

//BEGIN>>MessageToString
//HEADER> Return-Path: <MAILER-DAEMON@corp.21cn.com>
//HEADER> Delivered-To: noreply-alert@mails.jobisjob.com
//HEADER> Received: (qmail 8110 invoked by uid 89); 9 Aug 2013 01:24:03 -0000
//HEADER> Delivered-To: mails.jobisjob.com-info-alert@mails.jobisjob.com
//HEADER> Received: (qmail 8108 invoked by uid 89); 9 Aug 2013 01:24:01 -0000
//HEADER> Received: from corp.forptr.21cn.com (HELO corp.21cn.com) (121.14.129.89)
//  by retorn.grupointercom.com with SMTP; 9 Aug 2013 01:24:01 -0000
//HEADER> Received: by corp.21cn.com (HERMES)
//	id B13145CC00E; Fri,  9 Aug 2013 09:23:42 +0800 (CST)
//HEADER> HMM_ATTACHE_NUM: 0000
//HEADER> Date: Fri,  9 Aug 2013 09:23:42 +0800 (CST)
//HEADER> HMM_SOURCE_IP: ent2.408195538.402050304
//HEADER> Hmm-Notify: bounce
//HEADER> From: "=?gb2312?B?z7XNs7ncwO3UsQ==?=" <Postmaster@corp.21cn.com>
//HEADER> Subject: =?gb2312?B?z7XNs83L0MU=?=
//HEADER> To: info-alert@mails.jobisjob.com
//HEADER> Mime-Version: 1.0
//HEADER> Content-Type: multipart/report; report-type=delivery-status;
//	boundary="8FB995CC009.1376011422/corp.21cn.com"
//HEADER> Message-Id: <20130809012342.B13145CC00E@corp.21cn.com>
//MIME>CT> text/html; charset=gbk
//MIME>CT> message/rfc822
//MESS>> This is a MIME-encapsulated message.
//MESS>> 
//MESS>> --8FB995CC009.1376011422/corp.21cn.com
//MESS>> Content-Description: Notification
//MESS>> Content-Type: text/html;
//MESS>> 	charset="gbk"
//MESS>> Content-Transfer-Encoding: quoted-printable
//MESS>> 
//MESS>> <!DOCTYPE=20html=20PUBLIC=20"-//W3C//DTD=20XHTML=201.0=20Trans=
//MESS>> itional//EN"=20"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transit=
//MESS>> ional.dtd">=0A<html=20xmlns=3D"http://www.w3.org/1999/xhtml">=0A=
//MESS>> <head>=0A<meta=20http-equiv=3D"Content-Type"=20content=3D"text=
//MESS>> /html;=20charset=3Dutf-8"=20/>=0A<title>=CF=B5=CD=B3=CD=CB=D0=C5=
//MESS>> </title>=0A<style=20type=3D"text/css">=0A.INtable=20{=20font:1=
//MESS>> 2px=20Verdana;color:#383838}=0A.INtable=20a=20{color:#d63228;=20=
//MESS>> text-decoration:underline;}=0A.INtable=20a:hover=20{color:#F60=
//MESS>> ;=20text-decoration:none;}=0A.INtable=20table=20{border-collap=
//MESS>> se:collapse;background-color:#f5fbff}=0A.INtable=20td=20{=20bo=
//MESS>> rder:1px=20#b3daf0=20solid;=20font:12px=20Verdana;}=0A</style>=
//MESS>> =0A</head>=0A=0A<body>=0A<div=20class=3D"INtable">=0A<strong>=BA=
//MESS>> =DC=B1=A7=C7=B8=B5=D8=CD=A8=D6=AA=C4=FA=A3=AC=D3=CA=BC=FE=CE=DE=
//MESS>> =B7=A8=B7=A2=CB=CD=B5=BD=D6=B8=B6=A8=CA=D5=BC=FE=C8=CB=A3=AC=C7=
//MESS>> =EB=CF=C8=CD=A8=B9=FD=C6=E4=CB=FC=B7=BD=CA=BD=C1=AA=CF=B5=CA=D5=
//MESS>> =BC=
//MESS>>  FE=C8=CB=A3=AC=D2=D4=C3=E2=B5=A2=CE=F3=A1=A3<br/></strong>=
//MESS>> =0ASorry,we=20were=20unable=20to=20deliver=20your=20message=20=
//MESS>> to=20the=20address,please=20contact=20the=20recipient=20by=20o=
//MESS>> ther=20way,=20so=20as=20not=20to=20delay.=0A<br/><br/>=0A<tabl=
//MESS>> e=20border=3D"0"=20cellspacing=3D"0"=20cellpadding=3D"3"=20sty=
//MESS>> le=3D"width:900px;">=0A=20=20<tr>=0A=20=20=20=20<td=20align=3D=
//MESS>> "center"=20style=3D"background-color:#ebf6fc;=20width:140px;">=
//MESS>> <strong>=B1=BB=CD=CB=BB=D8=D3=CA=BC=FE</strong><br/>=0A=A3=A8R=
//MESS>> eturned=20Mail=A3=A9</td>=0A=20=20=20=20<td=20style=3D"backgro=
//MESS>> und-color:#f5fbff;=20line-height:150%;=20padding:10px=205px;">=
//MESS>> =D6=F7=CC=E2=A3=A8Subject=A3=A9=A3=BA10+=20new=20=E6=96=87=E5=93=
//MESS>> =A1=20posts=20created=20on=20JobisJob<br/>=0A=20=20=20=20=C8=D5=
//MESS>> =C6=DA=A3=A8Date=A3=A9=A3=BAFri,=2009=20Aug=202013=2009:23:30=20=
//MESS>> +0800=20<br/>=0A=20=20=20=20=B4=F3=D0=A1=A3=A8Size=A3=A9=A3=BA=
//MESS>> 392=0A=20=20=20=20</td>=0A=20=20</tr>=0A</table><br/>=0A<table=
//MESS>> =20border=3D"
//MESS>>  0"=20cellspacing=3D"0"=20cellpadding=3D"3"=20styl=
//MESS>> e=3D"width:900px;">=0A<tr>=0A=20=20=20=20<td=20align=3D"left"=20=
//MESS>> colspan=3D"2"=20style=3D"background-color:#d7eefa;=20line-heig=
//MESS>> ht:150%;=20height:30px;">=0A<strong>=CE=DE=B7=A8=B7=A2=CB=CD=B5=
//MESS>> =BD</strong>=A3=A8Unable=20to=20send=A3=A9=A3=BA<font=20style=3D=
//MESS>> "color:#d63228;"><strong><=20doris.li@htel.com.hk=20></strong>=
//MESS>> </font></td>=0A=20=20</tr>=0A=20<tr>=0A=20=20=20=20<td=20align=
//MESS>> =3D"center"=20style=3D"background-color:#ebf6fc;=20width:140px=
//MESS>> ;"><strong>=CD=CB=D0=C5=D4=AD=D2=F2</strong><br/>=0A=A3=A8The=20=
//MESS>> Reasons=20For=20Bounce=A3=A9</td>=0A=20=20=20=20<td=20align=3D=
//MESS>> "left"=20=20style=3D"background-color:#f5fbff;=20line-height:1=
//MESS>> 50%;=20padding:10px=205px;">=CA=D5=BC=FE=C8=CB=BE=DC=BE=F8=BD=D3=
//MESS>> =CA=D5=C4=FA=B7=A2=CB=CD=B5=C4=D3=CA=BC=FE=A1=A3<br/>host=2012=
//MESS>> 7.0.0.1[127.0.0.1]=20said:=20550=20the=20recipient=20refuses=20=
//MESS>> to=20accept=20this=20letter=20(in=20reply=20to=20end=20of=20DA=
//MESS>> TA=20command)</td
//MESS>>  >=0A=20=20</tr>=0A=20=20=20<tr>=0A=20=20=20=20=
//MESS>> <td=20align=3D"center"=20style=3D"background-color:#ebf6fc"><s=
//MESS>> trong>=BD=E2=BE=F6=BD=A8=D2=E9</strong><br/>=0A=A3=A8Solutions=
//MESS>> =A3=A9=A3=BA</td>=0A=20=20=20=20<td=20align=3D"left"=20style=3D=
//MESS>> "background-color:#f5fbff;=20line-height:150%;=20padding:10px=20=
//MESS>> 5px;">=BD=A8=D2=E9=C4=FA=CD=A8=B9=FD=C6=E4=CB=FB=B7=BD=CA=BD=C1=
//MESS>> =AA=CF=B5=CA=D5=BC=FE=B7=BD=A3=AC=B2=A2=CD=A8=D6=AA=C6=E4=D3=CA=
//MESS>> =CF=E4=BE=DC=CA=D5=C1=CB=C4=FA=B5=C4=D3=CA=BC=FE=A1=A3=0A<br/>=
//MESS>> We=20recommend=20that=20you=20contact=20the=20recipient=20by=20=
//MESS>> other=20means,=20and=20inform=20their=20mailboxes=20had=20reje=
//MESS>> cted=20your=20mail.=0A</td>=0A=20=20</tr>=0A=20=20</table><br/=
//MESS>> ><p>=B8=FC=B6=E0=B9=D8=D3=DA=CD=CB=D0=C5=B5=C4=B0=EF=D6=FA=D0=C5=
//MESS>> =CF=A2=C7=EB<a=20href=3D"http://agent.21cn.com/corphelp/?id=3D=
//MESS>> tuixin"=20target=3D"_blank">=B5=E3=BB=F7=D5=E2=C0=EF</a>=A1=A3=
//MESS>> </p>=0A</div>=0A</body>=0A</html>
//MESS>> 
//MESS>> --8FB995CC009.1376011422/corp.21cn.com
//MESS>> Content-Description: Undelivered Message
//MESS>> Content-Type: message/rfc822
//MESS>> 
//MESS>> HMM_SOURCE_IP:10.27.101.10:57571.1072166355
//MESS>> HMM_ATTACHE_NUM:0000
//MESS>> HMM_SOURCE_TYPE:SMTP
//MESS>> Received: from ip?194.116.241.198? (unknown [10.27.101.10])
//MESS>> 	by corp.21cn.com (HERMES) with ESMTP id 8FB995CC009
//MESS>> 	for <doris.li@htel.com.hk>; Fri,  9 Aug 2013 09:23:30 +0800 (CST)
//MESS>> Received: from ip<194.116.241.198> ([194.116.241.19])
//MESS>> 	by 21CN-entas10(MEDUSA 10.27.101.10) with ESMTP id 1376011401.17009 for doris.li@htel.com.hk
//MESS>> 	;
//MESS>> 	Fri Aug  9 09:23:42 2013
//MESS>> 0/X-Total-Score: 60:
//MESS>> 3/X-Brightmail-Tracker:AAAAAA==
//MESS>> X-FILTER-SCORE: to=<8590938a944f8d8a618995868d4f84908e4f898c>, score=<1376011422CHNCbNCtNHbt4uC7a6NCNNSzuSGuS3uzG3h0SPjBuSuu>  
//MESS>> X-REAL-FROM:info-alert@mails.jobisjob.com
//MESS>> X-Receive-IP:194.116.241.19 info-alert@mails.jobisjob.com
//MESS>> DKIM-Signature: v=1; a=rsa-sha1; c=relaxed; d=jobisjob.com; h=date:from
//MESS>> 	:reply-to:to:message-id:subject:mime-version:content-type
//MESS>> 	:list-unsubscribe:list-id; s=stronger; bh=6ozAbg62cuWoBIpxlAn4KH
//MESS>> 	0U8Tc=; b=rAj7modEIUQ4ssbhfT96maPez1fOtD0yOdsH2WTRYeaP1wNp38HODo
//MESS>> 	ul7g27yVsjpNQpBK3T5GqwubMNc6JCzZNIitwRzaH0Ss6fkUpgBYVUbBiOSnEz+c
//MESS>> 	5a/VLF39TmTrvINEk+OHM4DXWtP9r2hVZDc/07BYd8IBww+2J8yuI=
//MESS>> Received: (qmail 4074 invoked by uid 503); 9 Aug 2013 01:23:18 -0000
//MESS>> Received: from unknown (HELO sparrow) (10.12.50.70)
//MESS>>   by push3.jobisjob.com with SMTP; 9 Aug 2013 01:23:18 -0000
//MESS>> Date: Fri, 9 Aug 2013 03:23:18 +0200 (CEST)
//MESS>> From: JobisJob Alert <info-alert@mails.jobisjob.com>
//MESS>> Reply-To: "JobisJob Alert" <info@jobisjob.com>
//MESS>> To: "doris.li@htel.com.hk" <doris.li@htel.com.hk>
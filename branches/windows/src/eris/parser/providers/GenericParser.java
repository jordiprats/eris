package eris.parser.providers;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;

//Mail Delivery System <MAILER-DAEMON (Mail Delivery System)>=60;

//from: postmaster@mobileemail.vodafonesa.co.za
//MIME>CT>>text/plain
//MIME>CT>>message/delivery-status
//from: postmaster@blackberry.orange.co.uk
//MIME>CT>>text/plain
//MIME>CT>>message/delivery-status
//from: Mail Delivery System <MAILER-DAEMON@broadbandscope.net>
//MIME>CT>>text/plain; charset=us-ascii
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: Mail Delivery Subsystem <MAILER-DAEMON@dirac.goulburn.net.au>
//MIME>CT>>text/plain
//MIME>CT>>message/delivery-status
//MIME>CT>>text/rfc822-headers
//from: Mail Delivery Subsystem <MAIL-DAEMON@clear.net.nz>
//MIME>CT>>text/plain; charset=us-ascii
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: Mail Administrator <Postmaster@bigpond.com>
//MIME>CT>>text/plain
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: postmaster@hotmail.com
//MIME>CT>>text/plain; charset=unicode-1-1-utf-7
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: postmaster@hotmail.com
//MIME>CT>>text/plain; charset=unicode-1-1-utf-7
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: postmaster@maxnetwork.com.au
//MIME>CT>>multipart/alternative; differences=Content-Type;
//	boundary="1532881c-66dc-49ba-9809-50f9f6432984"
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: postmaster@CSG350.onmicrosoft.com
//MIME>CT>>multipart/alternative; differences=Content-Type;
//	boundary="f32f0168-b72a-465a-b758-47f6531d3f30"
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: postmaster@new.implats.co.za
//MIME>CT>>multipart/alternative; differences=Content-Type;
//	boundary="1b20e103-6ce1-4062-a098-4efde7af422f"
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: postmaster@hyde-housing.co.uk
//MIME>CT>>multipart/alternative; differences=Content-Type;
//	boundary="12bb069b-dff6-488f-954e-325c7b4c85ca"
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: Mail Delivery Subsystem <MAILER-DAEMON@inmx23.olympus.co.jp>
//MIME>CT>>text/plain
//MIME>CT>>message/delivery-status
//MIME>CT>>text/rfc822-headers
//from: Mail Delivery System <MAILER-DAEMON@broadbandscope.net>
//MIME>CT>>text/plain; charset=us-ascii
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: Mail Delivery System <MAILER-DAEMON@broadbandscope.net>
//MIME>CT>>text/plain; charset=us-ascii
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: Mail Delivery System <MAILER-DAEMON@broadbandscope.net>
//MIME>CT>>text/plain; charset=us-ascii
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: Mail Delivery System <MAILER-DAEMON@broadbandscope.net>
//MIME>CT>>text/plain; charset=us-ascii
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: Jeca Correia <jecacorreia@yahoo.com>
//MIME>CT>>text/plain; charset=us-ascii
//MIME>CT>>text/html; charset=us-ascii
//from: Mail Delivery System <MAILER-DAEMON@edtmtds.thinktel.ca>
//MIME>CT>>text/plain; charset=us-ascii
//MIME>CT>>message/delivery-status
//MIME>CT>>text/rfc822-headers
//from: Jeca Correia <jecacorreia@yahoo.com>
//MIME>CT>>text/plain; charset=us-ascii
//MIME>CT>>text/html; charset=us-ascii
//from: Naresh  Mittal <nareshmittalji@rediffmail.com>
//MIME>CT>>multipart/alternative;
//	boundary="=_d360ad6cd4542b5a44e0292a0ff83d28"
//from: postmaster@blackberry.orange.co.uk
//MIME>CT>>text/plain
//MIME>CT>>message/delivery-status
//MIME>CT>>message/rfc822
//from: Mail Delivery System <Mailer-Daemon@mk-delivery-4.b2b.uk.tiscali.com>

public interface GenericParser {
	
	public static final String headerpattern="^([^:]*): (.*$)";
	public static final String emailpattern="[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
	public static final String emailmatchpattern="("+GenericParser.emailpattern+")";
	public static final String userpattern="[A-Za-z0-9._%+-]+";
	public static final String usermatchpattern="("+GenericParser.userpattern+")";
	
	public boolean isRuleBased();
	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException;

}

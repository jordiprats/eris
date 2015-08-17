package eris.parser;

import eris.parser.providers.GenericParser;

public class BounceTypeClassifier 
{
//	MESS>> <pitny52@wp.pl>:
//	MESS>> bin/qmail-local-wp: error while loading shared libraries: libm.so.6: cannot open shared object file: Error 23

	
	public boolean ParseReason(String read, Bounce ret)
	{
		
		
		if(
			(read.matches("(?is).*mailbox is full.*")) ||
			(read.matches("(?is).*SMTP error from remote mail server after RCPT TO.*")) ||
			(read.matches("(?is).*mailbox full.*")) ||
			(read.matches("(?is).*5\\.2\\.2.*")) || //mailbox full
			(read.matches("(?is).*4\\.4\\.7.*")) || //delayed
			(read.matches("(?is).*4\\.3\\.0.*")) || //delayed
			(read.matches("(?is).*4\\.0\\.0.*")) || //Diagnostic-Code: smtp;mail receiving disabled
			(read.matches("(?is).*4\\.4\\.1.*")) || //No valid hosts (error segurament temporal del server)
			(read.matches("(?is).*4\\.3\\.1 Insufficient.*")) || //insuficients recursos (espai potser?)
			(read.matches("(?is)^.*Quota exceeded.*")) ||
			(read.matches("(?is)^.*Relay Access Denied.*")) ||
			(read.matches("(?is)^.*error from remote mail server after end of data.*")) ||
			(read.matches("(?is)^.*Read-only file system.*")) ||
			(read.matches("(?is)^.*over quota.*")) ||
			(read.matches("(?is)^.*Generic file I/O error.*")) || //dubtos
			(read.matches("(?is)^.*mailfolder is over [ a-z]*quota.*")) ||
			(read.matches("(?is)^.*transmission failure has occurred.*")) ||
			(read.matches("(?is)^.*The e-mail message could not be delivered because the user's mailfolder is full.*")) ||
			(read.matches("(?is)^.*Connection timed out.*")) ||
			(read.matches("(?is)^.*connection to mail exchanger failed.*")) ||
			(read.matches("(?is)^.*this message has been in the queue too long.*")) ||
			(read.matches("(?is)^.*unreachable for too long.*")) ||
			(read.matches("(?is)^.*Mailbox quota exceeded.*")) ||
			(read.matches("(?is)^.*452 Space shortage.*")) ||
			(read.matches("(?is)^.*mailbox for user is full.*")) ||
			(read.matches("(?is)^.*server is out of disk space.*")) ||
			(read.matches("(?is)^.*that contained an executable or media file.*")) ||
			(read.matches("(?is)^.*Sorry, this user .* is out of disk space.*")) ||
			(read.matches("(?is)^.*Because the maildir contains [0-9]+ bytes.*")) ||
			(read.matches("(?is)^.*Unable to deliver message within specified time.*")) ||
			(read.matches("(?is)^.*delivery to host .* timed out.*")) ||
			(read.matches("(?is)^.*disk full.*")) ||
			(read.matches("(?is)^.*Inbox is full.*")) ||
			(read.matches("(?is)^.*En email afsendt med din emailadresse kunne ikke leveres.*")) || //falta espai
			(read.matches("(?is)^.*users mailbox contains too much data.*")) ||
			(read.matches("(?is)^.*The email account that you tried to reach is disabled.*")) ||
			(read.matches("(?is)^.*User mailbox exceeds allowed.*"))
			)
		{
			ret.setReason(read);
			ret.setSoftBounce();
			return true;
		}
		else if(
				(read.matches("(?is)^.*local delivery failed.*")) ||
				(read.matches("(?is)^.*User unknown.*")) ||
				(read.matches("(?is)^.*I am no longer employed.*")) ||
				(read.matches("(?is)^.*group you tried to contact .* may not exist.*")) ||
				(read.matches("(?is)^.*User account is expired.*")) ||
				(read.matches("(?is).*5\\.1\\.1.*")) || //no existeix
				(read.matches("(?is).*5\\.5\\.0.*")) || //Receipient Not Found
				(read.matches("(?is).*5\\.7\\.1.*")) || //blocked
				(read.matches("(?is).*[ \\t]550[ \\t].*")) ||
				(read.matches("(?is).*550 the recipient refuses to accept this letter.*")) ||
				(read.matches("(?is).*The email account that you tried to reach does not exist.*")) ||
				(read.matches("(?is)^.*suspended: lost connection.*")) ||
				(read.matches("(?is)^.*retry time not reached for any host after a long failure period.*")) ||
				(read.matches("(?is)^.*Recipient address rejected.*")) ||
				(read.matches("(?is)^.*User .* is not listed in .*")) ||
				(read.matches("(?is)^.*No such user.*")) ||
				(read.matches("(?is)^.*Unknown user.*")) ||
				(read.matches("(?is)^.*no longer accepts mail.*")) ||
				(read.matches("(?is)^.*no longer an employee.*")) ||
				(read.matches("(?is)^.*no longer on server.*")) ||
				(read.matches("(?is)^.*no longer valid.*")) ||
				(read.matches("(?is)^.*This account has been temporarily suspended.*")) ||
				(read.matches("(?is)^.*data format error\\. Command output.*")) ||
				(read.matches("(?is)^.*mailaddress account.*Status is Disabled in the auth database: bouncing.*")) ||
				(read.matches("(?is)^.*Undeliverable Message.*")) ||
				(read.matches("(?is)^.*No such person at this address.*")) ||
				(read.matches("(?is)^.*550 5\\.1\\.1 User unknown.*")) ||
				(read.matches("(?is)^.*User not found.*")) ||
				(read.matches("(?is)^.*User not on post office.*")) ||
				(read.matches("(?is)^.*does not like recipient.*")) ||
				(read.matches("(?is)^.*but sender was rejected.*")) ||
				(read.matches("(?is)^.*mailbox unavailable.*")) ||
				(read.matches("(?is)^.*Unrouteable address.*")) ||
				(read.matches("(?is)^.*Unknown local user.*")) ||
				(read.matches("(?is)^.*no mailbox here by that name.*")) ||
				(read.matches("(?is)^.*You have been blocked by the recipient.*")) ||
				(read.matches("(?is)^.*network is on our block list.*")) ||
				(read.matches("(?is)^.*employee is not working with us.*")) ||
				(read.matches("(?is)^.*Mailbox not found.*")) ||
				(read.matches("(?is)^.*Address rejected.*")) ||
				(read.matches("(?is)^.*Recipient address rejected.*")) ||
				(read.matches("(?is)^.*user doesn't have a [a-z]+\\.[a-z]+ account.*")) ||
				(read.matches("(?is)^.*I can't handle a message with a Mailing-List header.*")) ||
				(read.matches("(?is)^.*it was rejected by the server.*")) ||
				(read.matches("(?is)^.*invalid address.*")) ||
				(read.matches("(?is)^.*This user doesn't have a .* account.*")) ||
				(read.matches("(?is)^.*delivery not authorized, message refused.*")) ||
				(read.matches("(?is)^.*Your message can't be delivered because delivery to this address is restric.*")) ||
				(read.matches("(?is)^.*No valid route for inbound email.*")) ||
				(read.matches("(?is)^.*The mailbox "+GenericParser.emailpattern+" doesn't exist.*")) ||
				(read.matches("(?is)^.*Command rejected.*")) ||
				(read.matches("(?is)^.*The email account that you tried to reach is disabled.*")) ||
				(read.matches("(?is)^.*no valid IP address could be found for recipient domain.*")) ||
				(read.matches("(?is)^.*No MX or A records for.*")) ||
				(read.matches("(?is)^.*no MXs for this domain could be reached.*")) ||
				(read.matches("(?is)^.*recipient domain does not exist in DNS.*")) ||
				(read.matches("(?is)^.*Username not known for this domain.*")) ||
				(read.matches("(?is)^.*This mail ID does not exist in this server.*")) ||
				(read.matches("(?is)^.*This mailbox has been blocked due to inactivity.*")) ||
				(read.matches("(?is)^.*This account has been disabled or discontinued.*")) ||
				(read.matches("(?is)^.*The e-mail address you entered couldn't be found.*")) ||
				(read.matches("(?is)^.*RecipNotFound.*")) ||
				(read.matches("(?is)^.*Host or domain name not found.*")) ||
				(read.matches("(?is)^.*Host not found.*")) ||
				(read.matches("(?is)^.*there are no users here by that name.*")) ||
				(read.matches("(?is)^.*Relay access denied.*")) //segurament un domini apuntant a uns MX que ja no accepten el domini
				)
		{
			ret.setReason(read);
			ret.setHardBounce();
			return true;
		}
//		DEFERRAL:
//		
//		Temporary error
		else if(
				(read.matches("(?is)^.*Temporary error.*")) ||
				(read.matches("(?is)^.*message is looping.*")) ||
				(read.matches("(?is)^.*Connect timeout for domain.*")) ||
				(read.matches("(?is)^.*excessive recursion.*")) ||
				(read.matches("(?is)^.*Hop count exceeded.*")) ||
				(read.matches("(?is)^.*loop detected.*")) ||
				(read.matches("(?is)^.*delivery temporarily suspended.*")) ||
				(read.matches("(?is)^.*Message expired for domain.*")) ||
				(read.matches("(?is)^.*Timed out while establishing SMTP connection.*")) ||
				(read.matches("(?is)^.*retry timeout exceeded.*")) ||
				(read.matches("(?is)^.*Service refused, please try later.*")) ||
				(read.matches("(?is)^.*Message will be retried for [0-9]* more.*")) ||
				(read.matches("(?is)^.*Database Error in HiveMail.*")) ||
				(read.matches("(?is)^.*Insufficient system resources.*")) ||
				(read.matches("(?is)^.*Can.t create output.*")) ||
				(read.matches("(?is).*5\\.3\\.0.*")) || //Can.t create output
				
				(read.matches("(?is)^.*error while loading shared libraries.*")) ||
				(read.matches("(?is)^.*Unable to run qmail-local.*")) ||
				(read.matches("(?is)^.*cannot open shared object files.*")) ||
				(read.matches("(?is).*4\\.7\\.0.*")) || //delayed
				(read.matches("(?is).*4\\.2\\.0.*")) || //delayed
				(read.matches("(?is).*message still undelivered after [0-9]+ hours.*")) || 
				(read.matches("(?is).*unreachable for too long.*")) || 
				
				(read.matches("(?is)^.*not rfc compliant.*")) ||
				(read.matches("(?is)^.*Unknown reason.*")) ||
				(read.matches("(?is)^.*host name lookup failure.*")) ||
				(read.matches("(?is)^.*lost connection.*")) ||
				(read.matches("(?is)^.*unreachable for too long.*")) ||
				
				(read.matches("(?is)^.*authorization failure.*")) || //possible error de config
				(read.matches("(?is)^.*timeout while waiting for reply to DATA reply.*")) ||
				(read.matches("(?is)^.*transaction failed.*")) ||
				(read.matches("(?is)^.*Connection refused from.*")) ||
				(read.matches("(?is)^.*555 Connect Failed.*")) ||
				(read.matches("(?is)^.*Unable to establish SMTP connection.*")) ||
				(read.matches("(?is)^.*Timeout waiting for client input.*")) ||
				(read.matches("(?is)^.*I wasn't able to establish an SMTP connection.*")) ||
				(read.matches("(?is)^.*554 message does not conform to standards.*")) ||
				(read.matches("(?is)^.*message was forwarded more than the maximum allowed times.*")) ||
				//quota string '99999999999999' not parseable
				(read.matches("(?is)^.*451 Requested action aborted: local error in processing.*")) ||
				(read.matches("(?is)^.*quota string .* not parseable.*"))
				)
		{
			ret.setReason(read);
			ret.setDeferral(true);
			return true;
		}
		//
		// REPUTACIÓ
		//
		else if (
				(read.matches("(?is)^.*blackholed by uribl.com.*")) ||
				(read.matches("(?is)^.*blacklisted by surbl.*")) ||
				(read.matches("(?is)^.*blocked by filter.*")) ||
				(read.matches("(?is)^.*blocked by kbas system.*")) ||
				(read.matches("(?is)^.*blocked by our content filter.*")) ||
				(read.matches("(?is)^.*blocked by spamsssassin.*")) ||
				(read.matches("(?is)^.*blocked using uceprotect.*")) ||
				(read.matches("(?is)^.*is rejected by See .*spamcop.net.*")) ||
				(read.matches("(?is)^.*is in a block list at bl\\.spamcop\\.net.*")) ||
				(read.matches("(?is)^.*550 Blocked - see .*www.spamcop.net.*")) ||
				(read.matches("(?is)^.*domain is banned.*")) ||
				(read.matches("(?is)^.*email abuse detected.*")) ||
				(read.matches("(?is)^.*high on spam scale.*")) ||
				(read.matches("(?is)^.*listed in multi.surbl.org.*")) ||
				(read.matches("(?is)^.*mail rejected by windows live hotmail for policy reasons.*")) ||
				(read.matches("(?is)^.*message has too high spam probability.*")) ||
				(read.matches("(?is)^.*message is blacklisted.*")) ||
				(read.matches("(?is)^.*sending MTA's poor reputation.*")) ||
				(read.matches("(?is)^.*rejected by surbl.*")) ||
				(read.matches("(?is)^.*temporarily deferred.*")) ||
				(read.matches("(?is)^.*Temporary local problem.*")) ||
				(read.matches("(?is)^.*Unable to process message at this time.*")) ||
				(read.matches("(?is)^.*Our system has detected an unusual rate of.*")) ||
				(read.matches("(?is)^.*spam rejection.*")) ||
				(read.matches("(?is)^.*Junk mail received.*")) ||
				(read.matches("(?is)^.*Your message was automatically rejected by Sieve.*")) ||
				(read.matches("(?is).*5\\.7\\.0.*")) || //antispam segurament
				(read.matches("(?is).*5\\.7\\.1.*")) || //antispam segurament
				(read.matches("(?is).*Blocked address.*")) ||
				(read.matches("(?is).*said: 554.*")) ||//554 No SMTP service - IP address is black listed
				
				//http://postmaster.aol.com/Postmaster.Errors.php
				(read.matches("(?is)^.*said:.*\\(DYN:T1.*")) || //AOL: rate limited
				(read.matches("(?is)^.*said:.*\\(DYN:T2.*"))  //AOL: error per part seva
				)
		{
			ret.setReason(read);
			ret.setDeferral(true);
			ret.setSpamReport(true);
			return true;
		}
		//ALTRES:
		//
		else if(
				(read.matches("(?is)^.*couldn't find any host named.*")) ||
				//servers mal configurats (antispams cloud que despres el server final es queixa del spf de l'origen)
				//per aixo deferral
				(read.matches("(?is)^.*is not allowed to send mail from.*")) ||
				(read.matches("(?is)^.*SMTP error from remote mail server after end of data.*")) 
				)
		{
			ret.setReason(read);
			ret.setDeferral(true);
			return true;
		}
		//
		// Out of Office
		//
		else if(
				(read.matches("(?is)^.*Vacation Mail.*")) ||
				(read.matches("(?is)^.*bin/vacation.*")) 
				)
		{
			ret.setOutOfOffice(true);
			ret.setBounce(false);
			ret.setReason(read);
			return true;
		}
		else return false;
	}
	
	private static BounceTypeClassifier instance = null;
	/**
	 *  soc protected, fuck you! (soc un singleton)
	 */
	protected BounceTypeClassifier() 
	{
		// fuck you!
	}
	
	public static BounceTypeClassifier getInstance() 
	{
		if(instance == null) instance = new BounceTypeClassifier();

		return instance;
	}
	
	
	
}


/* deferral
 * .*_spam_.*
.*550 5.7.1.*
.*550 access denied.*
.*550 no thanks.*
.*550 spam.*
.*554 5.7.0.*
.*554 5.7.1.*
.*554 denied.*
.*554 message does not conform to standards.*
.*554 message refused.*
.*571 message Refused.*
.*activated my anti-spam features.*
.*administrative prohibition.*
.*appears to contain uce/spam.*
.*appears to be spam.*
.*appears to be unsolicited.*
.*banned for spamming.*
.*blackholed by uribl.com.*
.*blacklisted by surbl.*
.*blocked by filter.*
.*blocked by kbas system.*
.*blocked by our content filter.*
.*blocked by spamsssassin.*
.*blocked by the recipient.*
.*blocked by user\'s personal blacklist.*
.*blocked due to spam like qualities.*
.*blocked using spam pattern.*
.*blocked using uceprotect.*
.*body part contains disallowed string.*
.*classified as **spam**.*
.*content blacklist.*
.*content filter rejected the message.*
.*consider the message to be spam.*
.*considered spam.*
.*contains spam.*
.*content filter rejection.*
.*content rejected.*
.*denied by policy.*
.*detected your message as spam.*
.*does not accept UCE.*
.*domain is banned.*
.*email abuse detected.*
.*error 553.*
.*help_spam_16.htm.*
.*high on spam scale.*
.*http://www.google.com/mail/help/bulk_mail.html.*
.*http://www.surbl.org/lists.html.*
.*identified as spam.*
.*identified your message as spam.*
.*listed in multi.surbl.org.*
.*looks like spam.*
.*mail rejected by windows live hotmail for policy reasons.*
.*message bounced by administrator.*
.*message content rejected, ube.*
.*message contains a virus or other harmful content.*
.*message has too high spam probability.*
.*message is blacklisted.*
.*message refused.*
.*no spam here.*
.*not accepted here.*
.*not in the whitelist.*
.*not rfc compliant.*
.*permanently rejected message.*
.*policy violation.*
.*refused mail service.*
.*reject spam mail.*
.*reject the mail.*
.*rejected by filter.*
.*rejected as spam.*
.*rejected by 35 antiSpam system.*
.*rejected by antispam system.*
.*rejected by cloudmark anti-spam.*
.*rejected by our Spam Filter.*
.*rejected by spam filtering.*
.*rejected by surbl.*
.*rejected by the anti-spam system.*
.*rejected due to security policies.*
.*rejected for policy reasons.*
.*rejected for spam.*
.*requires that you verify.*
.*scored as spam.*
.*sender address verification.*
.*sender was rejected.*
.*spam detected by spamassassin.*
.*spam filter pattern.*
.*spam-like characteristics.*
.*spam mail detected.*
.*spam mail refused.*
.*spam message was blocked.*
.*spam rejected.*
.*spam score too high.*
.*status: 5.7.1.*
.*support.proofpoint.com.*
.*suspected spam.*
.*suspicious url in message body.*
.*temporarily deferred.*
.*transaction failed.*
.*unacceptable content.*
.*unacceptable mail content.*
.*unsolicited bulk e-mail.*
.*unsolicited bulk email.*
.*unsolicited email is refused.*
.*validate recipient email.*
.*will be deleted from queue.*
/<(\S+@\S+\w)>.*\n?.*\n?.*Resources temporarily unavailable/i
/sender.*not/is
/Message refused/is
/No permit/is
/domain isn't in.*allowed rcpthost/is
/AUTH FAILED/is
/Transaction.*fail/is
/Invalid data/is
/not.*permit.*to/is
/Content reject/is
/MIME\/REJECT/is
/MIME error/is
Mail data refused.*AISP/is
/Host unknown/is
/System.*busy/is
/Resources temporarily unavailable/is
/sender is rejected/is
/Client host rejected/is
/MAIL FROM(.*)mismatches client IP/is
/denyip/is
/client host.*blocked/is
/mail.*reject/is
/spam.*detect/is
/reject.*spam/is
/Verify mailfrom failed/is
/MAIL.*FROM.*mismatch/is
/spam scale/is
/junk mail/is
/message filtered/is
/subject.*consider.*spam/is
/Deferred.*Connection (?:refused|reset)/i
 */


/* hard boucne

.*var/mail/nobody.*
.*550 5.1.1.*
.*account expired.*
.*account inactive.*
.*account suspended.*
.*address is administratively disabled.*
.*address is not recognized.*
.*address is rejected.*
.*address not recognized.*
.*address rejected.*
.*bad destination email address.*
.*cname lookup failed.*
.*connection refused.*
.*couldn\'t find any host.*
.*could not be found.*
.*deactivated mailbox.*
.*delivery time expired.*
.*destination server not responding.*
.*disabled or discontinued.*
.*does not exist.*
.*does not like recipient.*
.*doesn\'t have a yahoo.com account.*
.*doesn\'t have a yahoo.com.sg account.*
.*domain is for sale.*
.*find a mail exchanger.*
.*find any host named.*
.*following address(es) failed.*
.*host unknown.*
.*i couldn\'t find any host by that name.*
.*illegal user.*
.*in the queue too long.*
.*inactive recipient.*
.*inactive user.*
.*incorrectly addressed.*
.*invalid recipient.*
.*invalid user.*
.*isn\'t in my control/locals file.*
.*mailbox not available.*
.*mailbox not found.*
.*mailbox unavailable.*
.*mail is looping.*
.*message is looping.*
.*name is not recognized.*
.*name not found.*
.*name not recognized.*
.*no local mailbox.*
.*no longer accepts mail.*
.*no longer an employee.*
.*no longer on server.*
.*no longer valid.*
.*no mailbox.*
.*no route found to domain.*
.*no such user.*
.*none of the mail servers for the destination domain has so far responded.*
.*no valid host.*
.*no valid recipients.*
.*not a valid mailbox.*
.*not listed.*
.*not listed in domino.*
.*possible mail loop.*
.*recipient cannot be verified.*
.*recipient unknown.*
.*recipients are invalid.*
.*recipnotfound.*
.*relaying denied.*
.*several matches found in domino.*
.*status: 5.1.1.*
.*status: 5.1.3.*
.*status: 5.1.4.*
.*status: 5.1.6.*
.*status: 5.1.7.*
.*this user doesn\'t have a yahoo.*
.*too many hops.*
.*unable to deliver.*
.*unable to relay.*
.*unknown address.*
.*unknown recipient.*
.*unknown user.*
.*unrouteable address.*
.*user doesn\'t have.*
.*user unknown.*
.*x-notes; 550 5.1.1.*
.*x-notes; recipient\'s domino directory.*
5.0.0^h^Other undefined status
5.1.0^h^Other address status
5.1.1^h^Bad destination mailbox address
5.1.2^h^Bad destination system address
5.1.3^h^Bad destination mailbox address syntax
5.1.4^h^Destination mailbox address ambiguous
5.1.5^h^Destination mailbox address invalid (source: Microsoft)
5.1.6^h^Mailbox has moved
5.1.7^h^Bad sender's mailbox address syntax
5.1.8^h^Bad sender's system address
5.2.0^h^Other or undefined mailbox status
5.2.1^h^Mailbox disabled, not accepting messages
5.2.2^f^Mailbox full
5.2.3^l^Message length exceeds administrative limit.
5.2.4^h^Mailing list expansion problem
5.3.0^h^Other or undefined mail system status
5.3.1^s^Mail system full
5.3.2^h^System not accepting network messages
5.3.3^h^System not capable of selected features
5.3.4^l^Message too big for system
5.3.5^h^System incorrectly configured
5.4.0^h^Other or undefined network or routing status
5.4.1^h^No answer from host
5.4.2^h^Bad connection
5.4.3^h^Routing server failure
5.4.4^h^Unable to route
5.4.5^h^Network congestion
5.4.6^h^Routing loop detected
5.4.7^h^Delivery time expired
5.4.8^h^Loop detected, check recipient policy. (Source: Microsoft)
5.5.0^h^Other or undefined protocol status
5.5.1^h^Invalid command
5.5.2^h^Syntax error
5.5.3^h^Too many recipients
5.5.4^h^Invalid command arguments
5.5.5^h^Wrong protocol version
5.6.0^b^Other or undefined media error
5.6.1^b^Media not supported
5.6.2^h^Conversion required and prohibited
5.6.3^h^Conversion required but not supported
5.6.4^h^Conversion with loss performed
5.6.5^h^Conversion failed
5.7.0^h^Other or undefined security status
5.7.1^b^Delivery not authorized, message refused
5.7.2^h^Mailing list expansion prohibited
5.7.3^h^Security conversion required but not possible
5.7.4^h^Security features not supported
5.7.5^b^Cryptographic failure
5.7.6^b^Cryptographic algorithm not supported
5.7.7^b^Message integrity failure
/(\S+@\S+\w).*\n?.*no such address here/i
/<(\S+@\S+\w)>.*\n?.*no mailbox/i
/(\S+@\S+\w)<br>.*\n?.*\n?.*can't find.*mailbox/i
/Can't create output.*\n?.*<(\S+@\S+\w)>/i
/(\S+@\S+\w).*=D5=CA=BA=C5=B2=BB=B4=E6=D4=DA/i
/(\S+@\S+\w).*\n?.*Unrouteable address/i
/delivery[^\n\r]+failed\S*\s+(\S+@\S+\w)\s/is
/(\S+@\S+\w).*\n?.*unknown local-part/i
/Invalid.*(?:alias|account|recipient|address|email|mailbox|user).*<(\S+@\S+\w)>/i
/\s(\S+@\S+\w).*No such.*(?:alias|account|recipient|address|email|mailbox|user)>/i
/<(\S+@\S+\w)>.*\n?.*(?:alias|account|recipient|address|email|mailbox|user).*no.*accept.*mail>/i
/<(\S+@\S+\w)>.*\n?.*input\/output error/i
/<(\S+@\S+\w)>.*\n?.*can not open new email file/i
/<(\S+@\S+\w)>.*\n?.*does not accept[^\r\n]*non-Western/i
/(?:alias|account|recipient|address|email|mailbox|user)(.*)not(.*)list/is
/user path no exist/is
/Relay.*(?:denied|prohibited)/is
/no.*valid.*(?:alias|account|recipient|address|email|mailbox|user)/is
/Invalid.*(?:alias|account|recipient|address|email|mailbox|user)/is
/(?:alias|account|recipient|address|email|mailbox|user).*(?:disabled|discontinued)/is
/user doesn't have.*account/is
/(?:unknown|illegal).*(?:alias|account|recipient|address|email|mailbox|user)/is
/(?:alias|account|recipient|address|email|mailbox|user).*(?:un|not\s+)available/is
/no (?:alias|account|recipient|address|email|mailbox|user)/is
/(?:alias|account|recipient|address|email|mailbox|user).*unknown/is
/(?:alias|account|recipient|address|email|mailbox|user).*disabled/is
/No such (?:alias|account|recipient|address|email|mailbox|user)/is
/(?:alias|account|recipient|address|email|mailbox|user).*NOT FOUND/is
/deactivated (?:alias|account|recipient|address|email|mailbox|user)/is
/(?:alias|account|recipient|address|email|mailbox|user).*reject/is
/bounce.*administrator/is
/<.*>.*disabled/is
/not our customer/is
/Wrong (?:alias|account|recipient|address|email|mailbox|user)/is
/(?:unknown|bad).*(?:alias|account|recipient|address|email|mailbox|user)/is
/(?:alias|account|recipient|address|email|mailbox|user).*not OK/is
/Access.*Denied/is
/(?:alias|account|recipient|address|email|mailbox|user).*lookup.*fail/is
/(?:recipient|address|email|mailbox|user).*not.*member of domain/is
/(?:alias|account|recipient|address|email|mailbox|user).*cannot be verified/is
/Unable to relay/is
/(?:alias|account|recipient|address|email|mailbox|user).*(?:n't|not) exist/is
/not have an account/is
/(?:alias|account|recipient|address|email|mailbox|user).*is not allowed/is
/inactive.*(?:alias|account|recipient|address|email|mailbox|user)/is
/(?:alias|account|recipient|address|email|mailbox|user).*Inactive/is
/(?:alias|account|recipient|address|email|mailbox|user) closed due to inactivity/is
/(?:alias|account|recipient|address|email|mailbox|user) not activated/is
/(?:alias|account|recipient|address|email|mailbox|user).*(?:suspend|expire)/is
/(?:alias|account|recipient|address|email|mailbox|user).*no longer exist/is
/(?:forgery|abuse)/is
/(?:alias|account|recipient|address|email|mailbox|user).*restrict/is
/(?:alias|account|recipient|address|email|mailbox|user).*locked/is
/(?:alias|account|recipient|address|email|mailbox|user) refused/is
/Host unknown/is
/SpamTrap/is
/(?:alias|account|recipient|address|email|mailbox|user)(.*)invalid/i
/Deferred.*No such.*(?:file|directory)/i
/mail receiving disabled/i
/bad.*(?:alias|account|recipient|address|email|mailbox|user)/i
*/


/* soft bounce
.*databytes limit.*
.*exceeded dropfile size.*
.*exceeded email quota.*
.*exceeded storage.*
.*exceeding receiving limits.*
.*exceeds the maximum size.*
.*folder is full.*
.*mail system full.*
.*mailbox exceeds allowed size.*
.*mailbox full.*
.*mailbox has exceeded the limit.*
.*mailbox is full.*
.*mail box full.*
.*out of disk space.*
.*out of diskspace.*
.*over disk quota.*
.*over quota.*
.*over the allowed quota.*
.*problem with the recipient\'s mailbox.*
.*quota exceeded.*
.*quota violation.*
.*space has been used up.*
.*space not enough.*
.*status: 5.2.1.*
.*status: 5.2.2.*
.*status: 5.2.3.*
.*status: 5.3.3.*
4.0.0^s^Other undefined status
4.1.0^s^Other address status
4.1.4^s^Destination mailbox address ambiguous
4.1.5^s^Destination mailbox address valid
4.1.7^s^Bad sender's mailbox address syntax
4.1.8^s^Bad sender's system address
4.2.0^s^Other or undefined mailbox status
4.2.1^s^Mailbox disabled, not accepting messages
4.2.2^f^Mailbox full
4.2.4^s^Mailing list expansion problem
4.3.0^s^Other or undefined mail system status
4.3.1^s^Mail system full
4.3.2^s^System not accepting network messages
4.3.3^s^System not capable of selected features
4.3.5^s^System incorrectly configured
4.4.0^s^Other or undefined network or routing status
4.4.1^s^No answer from host
4.4.2^s^Bad connection
4.4.3^s^Routing server failure
4.4.4^s^Unable to route
4.4.5^s^Network congestion
4.4.6^s^Routing loop detected
4.4.7^s^Delivery time expired
4.5.0^s^Other or undefined protocol status
4.5.3^s^Too many recipients
4.5.5^s^Wrong protocol version
4.6.0^s^Other or undefined media error
4.6.2^s^Conversion required and prohibited
4.6.3^s^Conversion required but not supported
4.6.4^s^Conversion with loss performed
4.6.5^s^Conversion failed
4.7.0^s^Other or undefined security status
4.7.5^s^Cryptographic failure
4.7.6^s^Cryptographic algorithm not supported
4.7.7^s^Message integrity failure
.0.0^s^Other undefined status
.1.0^s^Other address status
.1.1^h^Bad destination mailbox address
.1.2^h^Bad destination system address
.1.3^h^Bad destination mailbox address syntax
.1.4^h^Destination mailbox address ambiguous
.1.5^k^Destination mailbox address valid
.1.6^h^Mailbox has moved
.1.7^s^Bad sender's mailbox address syntax
.1.8^s^Bad sender's system address
.2.0^s^Other or undefined mailbox status
.2.1^h^Mailbox disabled, not accepting messages
.2.2^f^Mailbox full
.2.3^l^Message length exceeds administrative limit.
.2.4^s^Mailing list expansion problem
.3.0^s^Other or undefined mail system status
.3.1^s^Mail system full
.3.2^s^System not accepting network messages
.3.3^s^System not capable of selected features
.3.4^l^Message too big for system
.3.5^s^System incorrectly configured
.4.0^s^Other or undefined network or routing status
.4.1^s^No answer from host
.4.2^s^Bad connection
.4.3^s^Routing server failure
.4.4^s^Unable to route
.4.5^s^Network congestion
.4.6^s^Routing loop detected
.4.7^s^Delivery time expired
.5.0^s^Other or undefined protocol status
.5.1^h^Invalid command
.5.2^h^Syntax error
.5.3^s^Too many recipients
.5.4^h^Invalid command arguments
.5.5^s^Wrong protocol version
.6.0^s^Other or undefined media error
.6.1^s^Media not supported
.6.2^s^Conversion required and prohibited
.6.3^s^Conversion required but not supported
.6.4^s^Conversion with loss performed
.6.5^s^Conversion failed
.7.0^s^Other or undefined security status
.7.1^b^Delivery not authorized, message refused
.7.2^h^Mailing list expansion prohibited
.7.3^h^Security conversion required but not possible
.7.4^h^Security features not supported
.7.5^s^Cryptographic failure
.7.6^s^Cryptographic algorithm not supported
.7.7^s^Message integrity failure
/<(\S+@\S+\w)>.*\n?.*\n?.*over.*quota/i
/quota exceeded.*\n?.*<(\S+@\S+\w)>/i
/<(\S+@\S+\w)>.*\n?.*quota exceeded/i
/\s(\S+@\S+\w)\s.*\n?.*mailbox.*full/i
/The message to (\S+@\S+\w)\s.*bounce.*Quota exceed/i
/(\S+@\S+\w)<br>.*\n?.*\n?.*user is inactive/i
/(\S+@\S+\w).*inactive account/i
/quota exceed.*<(\S+@\S+\w)>/is
/over.*quota/is
/exceed.*quota/is
/(?:alias|account|recipient|address|email|mailbox|user).*full/is
/Insufficient system storage/is
/File too large/is
/larger than.*limit/is
/Temporary local problem/is
/system config error/is
/over.*quota/i
/quota.*exceeded/i
/exceed.*\n?.*quota/i
/(?:alias|account|recipient|address|email|mailbox|user).*full/i
/space.*not.*enough/i

*/
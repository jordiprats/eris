package eris.parser.providers;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import eris.parser.Bounce;

public class IdiotParser implements GenericParser {

	public boolean isRuleBased() {
		return true;
	}

//	[root@retorno cur]# for i in $(grep "Sent from my Nokia" * -R | cut -f 1-2 -d. | sort | uniq); do rm $i*; done
//	[root@retorno cur]# for i in $(grep "Sent via Nokia Email" * -R | cut -f 1-2 -d. | sort | uniq); do rm $i*; done
//	[root@retorno cur]# for i in $(grep "Sent from my HTC" *  | cut -f1-2 -d. | sort | uniq ); do rm $i*; done
//	[root@retorno cur]# for i in $(grep "Sent via my BlackBerry" *  | cut -f1-2 -d. | sort | uniq ); do rm $i*; done
//	[root@retorno cur]# for i in $(grep "Sent from my BlackBerry" *  | cut -f1-2 -d. | sort | uniq ); do rm $i*; done

	
	public Bounce Parse(String from, Message message) throws IOException, MessagingException 
	{
		return null;
	}

}

package eris.connector;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.search.FlagTerm;

import eris.config.MailConfig;

//http://alvinalexander.com/blog/post/java/i-need-get-all-of-my-email-addresses-out-of-imap-mailbox

public class IMAPLink extends MailLink {

	protected Folder inbox=null;
	
	public String getConnectionID() { return mc.getUsername()+"@"+mc.getServer(); }
	
	public IMAPLink(MailConfig mc)
	{
		this.mc=mc;
	}

	public void connect() throws MessagingException
	{
		props = new Properties();

		//Connect to the server
		session = Session.getDefaultInstance(props, null);
		store = session.getStore(mc.getProvider());
		store.connect(mc.getServer(), mc.getUsername(), mc.getPassword());
	}

	@Deprecated
	public Message[] getMessages(Flag flag, boolean status) throws MessagingException
	{
		inbox = store.getFolder(mc.getFolder());
		inbox.open(Folder.READ_WRITE);

		Flags seen = new Flags(flag);
		FlagTerm unseenFlagTerm = new FlagTerm(seen, status);
		
		return inbox.search(unseenFlagTerm);
	}
	
	public Message[] getMessages() throws MessagingException
	{
		//open the inbox folder
		inbox = store.getFolder(mc.getFolder());
		inbox.open(Folder.READ_WRITE);

		/*
		 *     Flags seen = new Flags(Flags.Flag.SEEN);
		 *     FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
		 *     Message messages[] = inbox.search(unseenFlagTerm);
		 */
		
		// get a list of javamail messages as an array of messages
		return inbox.getMessages();
	}
	
	public void close()
	{
		try
		{
			if(inbox!=null) inbox.close(true); //true per purgar misatges eliminats
			if(store!=null) store.close();
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
		}
		
	}


}

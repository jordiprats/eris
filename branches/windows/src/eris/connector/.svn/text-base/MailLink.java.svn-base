package eris.connector;

import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import eris.config.MailConfig;

public abstract class MailLink {

	protected MailConfig mc=null;
	
	protected Properties props=null;
	protected Session session=null;
	protected Store store=null;
	
	public abstract String getConnectionID();
	public abstract void connect() throws MessagingException;
	public abstract Message[] getMessages() throws MessagingException;
	public abstract Message[] getMessages(Flag flag, boolean status) throws MessagingException;
	public abstract void close();
	
}

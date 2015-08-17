package eris.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import eris.connector.HTTPLink;
import eris.db.DBAccess;
import eris.db.HSQLDBAccess;
import eris.parser.providers.DeliveryStatusParser;
import eris.parser.providers.GenericParser;
import eris.parser.providers.MailDeliverySystemParser;
import eris.parser.providers.QmailSendParser;
import eris.parser.providers.XHmXmrParser;
import eris.parser.rfc822.RFC822Parser;

public class BounceClassifier {
	
	protected Bounce bounce=null;
	public Bounce getLastBounce() { return bounce; }
	
	protected boolean debug=false;
	protected boolean CSV=false;
	
	public void setDebug(boolean debug) { this.debug=debug; }
	
	protected String hashconnectionid=null;
	
	protected long runnumber=0;
	protected long parsedcounter=0;
	public long getParsedCounter() { return this.parsedcounter; }
	
	//protected BounceStats stats=null;
	protected DBAccess bounces=null;
	
	//rule based:
	protected DeliveryStatusParser dsparser=null;
	protected XHmXmrParser xhmxmrparser=null;
	protected MailDeliverySystemParser mdsparser=null;
	protected QmailSendParser qmsparser=null;
	
	protected FileWriter fstream=null;
	protected BufferedWriter csv=null;
	
	public static String getMD5(String in)
	{
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		m.reset();
		m.update(in.getBytes());
		return new BigInteger(1,m.digest()).toString(16);
	}
	
	
	public void setCSV(boolean CSV)
	{
		if(CSV&&!this.CSV)
		{
		    try {
				fstream = new FileWriter("mails.txt", true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    csv = new BufferedWriter(fstream);
		    this.CSV=CSV;
		}
		else if(this.CSV&&!CSV)
		{
			try {
				csv.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setRunNumber(long runnumber) 
	{
		this.runnumber=runnumber;
		
		if(this.bounces==null) //no hauria
		{
			try {
				if(hashconnectionid==null)
					this.bounces=new HSQLDBAccess("bouncelist");
				else
					this.bounces=new HSQLDBAccess("bouncelist."+hashconnectionid);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.bounces.setDebug(this.debug);
		}
		
		try {
			this.bounces.update("insert into runnumbers(runnumber) values("+this.runnumber+");");
			//System.out.println("insert into runnumbers(runnumber) values("+this.runnumber+");");
		} catch (SQLException e) {
			System.err.println("error insertant nou runnumber");
			e.printStackTrace();
		}
		
		System.out.println("stats:");
		try {
			ResultSet rs=this.bounces.execute("select count(*) as countrunnumbers from runnumbers;");
			rs.next();
			System.out.println("total runs: "+rs.getString("countrunnumbers"));
			
			rs=this.bounces.execute("select count(*) as countbounces from bouncelist;");
			rs.next();
			System.out.println("total bounces: "+rs.getString("countbounces"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String dumpDB()
	{
		String ret="";
		
		try {
			this.bounces.commit();
			
			//"select distinct(email),"
			String query="select * " +
			"from bouncelist " +
			";";
			
			ResultSet rs=this.bounces.execute(query);
			
			System.out.println("dumpingdb");
			while(rs.next())
			{
				System.out.print(".");
				ret+=rs.getString("EMAIL")+"\n";
			}
			System.out.println();
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public String dumpBounces() { return this.dumpBounces(5); }
	public String dumpBounces(long days)
	{
		String ret="";
		long maxrepes=(days>2)?days-2:1;
		
		//HASH([EMAIL,RUNNUMBER])=COUNT
		//TODO: canviar a:
		//HASH([EMAIL,DIA_REBUT])=COUNT
		//
		HashMap<String, HashMap<String, Integer>> hm=new HashMap<String,HashMap<String, Integer>>();
		
		long today=(System.currentTimeMillis()/1000)+3600;
		long initime=(today-(60*60*24*days))-3600;
		
		try {
			//"select distinct(email),"
			String query="select EMAIL," +
			"RUNNUMBER, arrivaltime "+
			"from bouncelist " +
			"where runnumber>="+initime+" and " +
				"runnumber<="+today+" "+
			";";
			
			//query="select * from bouncelist;";

			
			System.out.println(query);
			
			ResultSet rs=this.bounces.execute(query);

			long totalcount=0;
			while(rs.next())
			{
				totalcount++;
				String pk=rs.getString("EMAIL");
//				String sk=rs.getString("RUNNUMBER");
				String sk=rs.getString("ARRIVALTIME");
				
//				Fri Aug 09 10:15:30 CEST 2013
				DateFormat df=new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
				try {
					Date d=df.parse(sk);
//					System.out.println(d);
//					
					Calendar cal = Calendar.getInstance();
				    cal.setTime(d);
				    
				    sk=""+cal.get(Calendar.DAY_OF_MONTH)+"0"+cal.get(Calendar.MONTH)+"0"+cal.get(Calendar.YEAR);
				    
				} catch (ParseException e) {
					sk=":)";
//					System.out.println(sk);
				}
				
				
				/*
				
				{
					ret+=rs.getString("EMAIL")+"/"+rs.getString("RUNS")+"\n";
				}
				*/
				HashMap<String, Integer> sh=hm.get(pk);
				
				if(sh==null)
				{
					sh=new HashMap<String, Integer>();
					sh.put(sk, new Integer(1));
				}
				else
				{
					Integer count=(Integer) sh.get(sk);
					if(count==null)
						count=new Integer(1);
					else
						count++;
					sh.put(pk, count);
				}
				hm.put(pk, sh);
			}
			
			rs.close();
			ret+="EMAILs\n";
			
			
			Iterator<String> itpk=hm.keySet().iterator();
			
			while(itpk.hasNext())
			{
				String mail=itpk.next();
//				System.out.println("zz>"+hm.get(mail).size());
				if(hm.get(mail).size()>=maxrepes)
				{
//					ret+=mail+"/"+hm.get(mail).size()+" vs "+(days-2)+"\n";
					//TODO: fer genèric
					
					HTTPLink link=new HTTPLink();
					try 
					{
						String result=link.getURL("http://www.jobisjob.co.uk/admin-area/DisableAlerts?email="+mail+"&type=softhard");
						ret+=mail+": "+result;
						
					} 
					catch (IOException e) 
					{
					}
				}
			}
			
			ret+="/EMAILs\n";
			ret+="total: "+totalcount+"\n";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public BounceClassifier(String ConnectionID) throws ClassNotFoundException, SQLException
	{
		
//		this.stats=new BounceStats(this.debug, ConnectionID);
		
		this.dsparser=new DeliveryStatusParser();
		this.xhmxmrparser=new XHmXmrParser();
		this.mdsparser=new MailDeliverySystemParser();
		this.qmsparser=new QmailSendParser();
		
		hashconnectionid=BounceClassifier.getMD5(ConnectionID);
		
		if(hashconnectionid==null)
			this.bounces=new HSQLDBAccess("bouncelist");
		else
			this.bounces=new HSQLDBAccess("bouncelist."+hashconnectionid);
			
		
		this.bounces.setDebug(this.debug);
		
		//ho trec pq ja em sembla perillos
		//try { if(this.debug) this.bounces.update("drop table bouncelist;"); } catch (SQLException e) {	}
		//try { if(this.debug) this.bounces.update("drop table runnumbers;"); } catch (SQLException e) {	}
		
		try
		{
			this.bounces.update(
					"create table bouncelist(" +
						"runnumber 		bigint, "+
						"email			varchar(256),"+
						"arrivaltime 	varchar(256),"+ //TODO: pasar a date
						"spamreport		boolean,"+
						"bounce			boolean,"+
						"generatorname	varchar(256)"+
					");");
		} catch (SQLException e) {
			if(debug) System.err.println("taula bouncelist ja existia");
			//e.printStackTrace();
		}
		
		try
		{
			this.bounces.update(
					"create table runnumbers(" +
						"id INTEGER IDENTITY,"+
						"runnumber 		bigint "+
					");");
		} catch (SQLException e) {
			if(debug) System.err.println("taula bouncelist ja existia");
			//e.printStackTrace();
		}
		
		this.bounces.close();
		
		if(hashconnectionid==null)
			this.bounces=new HSQLDBAccess("bouncelist");
		else
			this.bounces=new HSQLDBAccess("bouncelist."+hashconnectionid);
		
		this.bounces.setDebug(this.debug);
	}
	
//	public String getStats()
//	{
//		return stats.toString();
//	}
	
	public void close()
	{
		if(this.CSV)
		{
			try {
				csv.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private Class[] getClasses(String packageName)
	throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}
	
    @SuppressWarnings("rawtypes")
	private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

	
	public boolean Parse(Message message) throws IOException, MessagingException
	{		
		//http://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
		//http://rafinguer.blogspot.com.es/2006/10/reflexin-en-java_24.html
		//eris.parser.providers
		
		String from=null;
		
		try
		{
			from = message.getFrom()[0].toString();
		}
		catch(Exception e)
		{
			from=null;
		}

		String emailfrom=null;

		if ( from!=null) 
		{
			for(int i=0; i<MailDeliverySystemParser.frompattern.length; i++)
			{
				//System.out.println(MailDeliverySystemParser.frompattern[i]+" vs "+from);
				if(from.matches(MailDeliverySystemParser.frompattern[i]))
					  bounce=this.mdsparser.Parse(emailfrom, message);
			}
			
			try
			{
				emailfrom=RFC822Parser.getInstance().getFrom(from);
			}
			catch(Exception e)
			{
				emailfrom="";
			}

			if(emailfrom==null)
				emailfrom="";

//			String domain=RFC822Parser.getInstance().getTopLevelDomain(emailfrom);
//
//			if(emailfrom.compareTo("")==0)
//			{
//				//domain="NOPARSEJAT";
//				domain="ERROR-PARSING";
//			}

			//basura
			if(emailfrom.compareTo("stargate.grupointercom.com@stargate.grupointercom.com")==0)
				bounce=new Bounce(false,this.getClass().getName());
			
			//putospam
			if(emailfrom.compareTo("rolfherzog@hotmail.com")==0)
				bounce=new Bounce(false,this.getClass().getName());
			
//			this.stats.incDomain(domain);

			try
			{
				Multipart multipart = (Multipart) message.getContent();

				for (int i = 0; i < multipart.getCount(); i++)
				{
					BodyPart bodyPart = multipart.getBodyPart(i);
					if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
					{
						//application/msword;
						if(bodyPart.getContentType().startsWith("application/msword;"))
							bounce=new Bounce(false,this.getClass().getName());
						else if(bodyPart.getContentType().startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
							bounce=new Bounce(false,this.getClass().getName());
						else continue;
						//continue; // not dealing with attachments
					}
					
					if(bodyPart.getContentType().compareTo("message/delivery-status")==0)
					{
						bounce=this.dsparser.Parse(emailfrom, message);
						if(bounce!=null) break;
					}
					
					if(bodyPart.getContentType().compareTo("message/rfc822")==0)
					{
						bounce=this.xhmxmrparser.Parse(emailfrom, message);
						if(bounce!=null) break;
						
						bounce=this.qmsparser.Parse(emailfrom, message);
						if(bounce!=null) break;
					}
				}
			}
			catch(Exception e) {}
		}
		
			@SuppressWarnings("rawtypes")
			Class[] parsers;
			try {
				parsers = this.getClasses("eris.parser.providers");
				//System.out.println("count: "+parsers.length);
				
				for(@SuppressWarnings("rawtypes") Class parser : parsers)
				{
					if(parser.getInterfaces().length>0)
					{
						try
						{
							GenericParser gp=(GenericParser)parser.newInstance();
							if((bounce==null)&&(!gp.isRuleBased()))
								bounce=gp.Parse(emailfrom, message);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				parsers=null;
				System.gc();
				
				if(bounce==null)
					bounce=this.mdsparser.Parse(emailfrom, message);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(bounce!=null)
			{
				bounce.setRunNumber(this.runnumber);

				//if(bounce.isSoftFail())
				//tratem per igual soft de hard
				
//				System.out.println("FOUND: "+bounce.getEmail() +" //"+ bounce.getGeneratorName());
				
				if(bounce.isBounce())
				{
					if(!bounce.isBouceTypeValid())
					{
						System.out.flush();
						System.err.println("NOREASEON: "+bounce);
						System.err.flush();
						
						message.setFlag(Flags.Flag.FLAGGED, true);
						return false;
					}
					//data rebut - message.getReceivedDate()
					bounce.setArrivalTime(message.getReceivedDate().toString());
					
					//TODO: spf
//	                // Check if we should set a costum default explanation
//	                if (defaultExplanation != null) {
//	                    spf.setDefaultExplanation(defaultExplanation);
//	                }
//
//	                // Check if we should use best guess
//	                if (useBestGuess == true) {
//	                    spf.setUseBestGuess(true);
//	                }
//	                
//	                if (useTrustedForwarder == true) {
//	                    spf.setUseTrustedForwarder(true);
//	                }
//
//	                SPFResult result = spf.checkSPF(ip, sender, helo);
				
					//fer alguna comprovació
					//null, sigui un mail, no sigui el de jij, no sigui postmaster@...
					String email=bounce.getEmail();
					
					boolean assertmailfailed=false;
					if(!RFC822Parser.getInstance().isUserEmail(email)) 
						assertmailfailed=true;
					else
					{
						for(String mdspattern : MailDeliverySystemParser.frompattern)
						{
							if(email.matches(mdspattern))
								assertmailfailed=true;
						}
					}
					
					if(assertmailfailed)
					{
						System.out.flush();
						System.err.println("ASSERT FAILED: "+bounce.getGeneratorName()+ " email=<"+email+">");
						if(email!=null && !RFC822Parser.getInstance().isEmail(email))
							System.err.println("NOT A VALID EMAIL");
						if(emailfrom!=null)
							System.err.println("from: "+emailfrom);
						System.err.println("Subject: "+message.getSubject());
						System.err.flush();
						
						return false;
					}
					
					if(!this.CSV)
						bounce.SQLize(this.bounces);
					else
						csv.write(bounce.getEmail());
				}

				//eliminació/marcatge de mails ja parsejats
				message.setFlag(Flags.Flag.DELETED, true);
				message.setFlag(Flags.Flag.SEEN, true);
				this.parsedcounter++;
				
				return true;
			}

//		}
//		else
//			System.out.println("ERROR LLEGINT FROM "+message.getFileName());

		return false;
	}

	public String MessageToString(Message mess)
	{
		//TODO: this message is a mess!
		String ret="";
		
		System.out.println("BEGIN>>MessageToString");
		
		//HEADERS
		try {
			Enumeration<?> headers = mess.getAllHeaders();
			
			while(headers.hasMoreElements())
			{
				Header h = (Header) headers.nextElement();
				System.out.println("HEADER> "+h.getName() + ": " + h.getValue());
			}
			
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//MULTIPARTS
		try
		{
			Multipart multipart = (Multipart) mess.getContent();

			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart bodyPart = multipart.getBodyPart(i);
				if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) 
					continue; // not dealing with attachments
				
				System.out.println("MIME>CT> "+bodyPart.getContentType());
			}
		}
		catch(Exception e) 
		{ //si no soc multipart, a la merda
		}
		
		//LINEA A LINEA
		try {
		BufferedReader br=new BufferedReader(new InputStreamReader(mess.getInputStream()));
		String read=br.readLine();
		
		while(read != null)
		{
			System.out.println("MESS>> "+read);
			
			read=br.readLine();
		}
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("BEGIN>>MessageToString");
		
		return ret;
	}

}

package eris.main;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.mail.Flags;
import javax.mail.Message;

import eris.config.MailConfig;
import eris.connector.HTTPLink;
import eris.connector.MailLink;
import eris.connector.MailLinkFactory;
import eris.parser.Bounce;
import eris.parser.BounceClassifier;

public class Eris {


	/**
	 * llistat de parametres 
	 */
	protected static List<MailConfig> lmc=new Vector<MailConfig>();

	protected static MailLink ml=null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		long timeinit;
		long timeout;
		long runnumber=0;
		long fails=0;
		long totalbounces=0;
		
		boolean stepbystep=true;
		boolean dumpdb=true;
		boolean flagged=false;
		boolean faileds=false;
		
		//deprecated
		boolean getcsv=false;
		
		lmc.add(new MailConfig(
				MailConfig.TIPUS_IMAP,
				"10.12.16.73",
				"noreply-alert@mails.jobisjob.com",
				"Intercom2009")
				);
		
		/*
		lmc.add(new MailConfig(
				MailConfig.TIPUS_IMAP,
				"10.12.16.73",
				"news@mail.niumba.com",
				"8m1l1s3gund0s")
				);
		*/

		ml=MailLinkFactory.getMailLink(lmc.iterator().next());

		BounceClassifier bc=null;

		try {
			
			bc=new BounceClassifier(ml.getConnectionID());
			bc.setDebug(false);

			System.out.println("hashid: "+BounceClassifier.getMD5(ml.getConnectionID()));
			
			System.out.print("Conectant...");
			System.out.flush();
			ml.connect();
			System.out.println(" OK");
			System.out.flush();

//			stepbystep=true;
			Message[] mess=null;
			if(flagged)
				mess=ml.getMessages(Flags.Flag.FLAGGED, true);
			else if(faileds)
				mess=ml.getMessages(Flags.Flag.SEEN, true);
			else if(!stepbystep)
				mess=ml.getMessages(Flags.Flag.SEEN, false);
			else
				mess=ml.getMessages(Flags.Flag.SEEN, true);
//			stepbystep=false;

			totalbounces=mess.length;
			
			System.out.println("Total:"+totalbounces);
			System.out.flush();
			
			if(totalbounces==0)
			{
				System.out.println("res a fer!");
				return;
			}

			runnumber=timeinit=System.currentTimeMillis()/1000;

			System.out.println("run eris:"+runnumber);

			bc.setRunNumber(runnumber);
			if(getcsv) bc.setCSV(true);

			for(int i = 0; i < mess.length; i++)
			{
				//PER SALTAR: 
				//mess[i].setFlag(Flags.Flag.SEEN, true); if(2>1) break;
				
//				"bounce@rediffmail.com"
//				String from=mess[i].getFrom()[0].toString();
//				if(from ==null || !from.matches("(?is)^.*bounce@rediffmail.com.*"))
//					continue;
				
//				if((i%10==0)&&(i>0))
				if(i%10==0)
				System.out.println("parsejant MAIL #"+i+" de "+mess.length);
				
				//System.out.println("Size: "+mess[i].getSize());
				try
				{
					if(mess[i].getSize()>2400000)
					{
						System.out.flush();
						System.err.println("Missatge massa gros ("+mess[i].getSize()+")");
						System.err.println("Subject: "+mess[i].getSubject());
						//					System.err.println("FN: "+mess[i].getFileName());
						System.err.flush();
						mess[i].setFlag(Flags.Flag.DELETED, true);
						continue;
					}
				}
				catch(Exception e) { }
				
				if((i%1000==0)&&(i>0))
				{
					timeout=System.currentTimeMillis()/1000;
					System.out.println(i+"/"+mess.length+" - "+1000/(timeout-timeinit)+" mails/s");
					//System.out.println("@stats: "+bc.getStats());
					timeinit=timeout;
					//GC
					System.gc();
				}

				try {
					if(bc.Parse(mess[i])==false)
					{
						fails++;
						//escupim mail i a la merda per analitzar el mail a mà
						//System.out.println("Total parsejat: "+bc.getParsedCounter());

						if(stepbystep)
						{
							bc.MessageToString(mess[i]);
							//if(fails!=1)
							break;
						}
					}
					else
					{
						//MAIL PARSEJAT OK
						Bounce bounce=bc.getLastBounce();
						
						HTTPLink link=new HTTPLink();
						if(bounce.isHardBounce()&&!bounce.isDeferral())
						{ //Hardbounce, pero no deferral
							try 
							{
								link.getURL("http://www.jobisjob.co.uk/admin-area/DisableAlerts?email="+bounce.getEmail()+"&type=hard");
							} 
							catch (IOException e) {}
						}
						else if(bounce.isSoftBounce())
						{ //Softbounce
							try 
							{
								link.getURL("http://www.jobisjob.co.uk/admin-area/DisableAlerts?email="+bounce.getEmail()+"&type=soft");
							} 
							catch (IOException e) {}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if(dumpdb)
			{
				System.out.println("<busca emails");
				System.out.println(bc.dumpBounces());
				System.out.println(">busca emails");
			}

			System.out.println("run# "+runnumber);
			System.out.println("faileds: "+fails+"/"+bc.getParsedCounter()+" de un total de: "+totalbounces);
			System.out.println("%faileds: "+((double)fails/totalbounces)*100);
			
			bc.close();
			ml.close();

			System.out.println("FIPARSER");

		} catch (Exception e) {
			System.out.println();
			if(bc!=null) bc.close();
			if(ml!=null) ml.close();
			e.printStackTrace();
		} 

	}



}

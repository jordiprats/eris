package eris.main;

import java.util.List;
import java.util.Vector;

import javax.mail.Flags;
import javax.mail.Message;

import eris.config.MailConfig;
import eris.connector.MailLink;
import eris.connector.MailLinkFactory;
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
		
		boolean stepbystep=false;
		boolean getcsv=false;
		boolean dumpdb=true;
		
		/* 
		lmc.add(new MailConfig(
				MailConfig.TIPUS_IMAP,
				"10.12.16.73",
				"noreply-alert@mails.jobisjob.com",
				"Intercom2009")
				);
		*/
		
		lmc.add(new MailConfig(
				MailConfig.TIPUS_IMAP,
				"10.12.16.73",
				"news@mail.niumba.com",
				"8m1l1s3gund0s")
				);

		ml=MailLinkFactory.getMailLink(lmc.iterator().next());

		BounceClassifier bc=null;

		try {

			bc=new BounceClassifier(ml.getConnectionID());
			bc.setDebug(false);

			System.out.println("hashid: "+BounceClassifier.getMD5(ml.getConnectionID()));
			
			System.out.print("Conectant...");
			ml.connect();
			System.out.println(" OK");

			Message[] mess=null;
			if(!stepbystep)
				mess=ml.getMessages(Flags.Flag.SEEN, false);
			else
				mess=ml.getMessages(Flags.Flag.SEEN, true);

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
				System.out.println("parsejant MAIL #"+i+" de "+mess.length);
				if((i%1000==0)&&(i>0))
				{
					timeout=System.currentTimeMillis()/1000;
					System.out.println(i+"/"+mess.length+" - "+1000/(timeout-timeinit)+" mails/s");
					//System.out.println("@stats: "+bc.getStats());
					timeinit=timeout;
				}

				//merda per netejar
				//mess[i].setFlag(Flags.Flag.SEEN, true);
				//if(i<mess.length/30) continue;
				//else if(!(i<mess.length/30)) continue;

				/*String from = mess[i].getFrom()[0].toString();
		        String date = mess[i].getSentDate().toString();

		        if ( from!=null) 
		        {
		        	if(from.compareToIgnoreCase("staff@hotmail.com")!=0)
		        		System.out.println("Subject: "+from+"("+date+")");
		        	else continue;
		        }*/

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
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if(dumpdb)
			{
				System.out.println("tots els mails de la DB:");
				System.out.println(bc.dumpDB());
			}
			else
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

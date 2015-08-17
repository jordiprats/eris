package eris.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPLink 
{
	private static final String USER_AGENT = "eris JordiParser/1.0";
	
	protected int httpresponse=0;
	public int getLastHTTPResponse() { return this.httpresponse; }
	
	public String getURL(String url) throws IOException
	{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		httpresponse = con.getResponseCode();
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String read;
		StringBuffer response = new StringBuffer();
 
		while ((read = in.readLine()) != null) {
			response.append(read);
		}
		in.close();
 
		return response.toString();
	}
}

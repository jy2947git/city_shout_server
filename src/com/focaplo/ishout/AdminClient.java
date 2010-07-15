package com.focaplo.ishout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class AdminClient {
	String server="http://localhost:8080/serve?token=" + ishoutServlet.token;
	public String checkListeners(){
		//command=status
		String urlString = server + "&command=status";
		String result = this.doGet(urlString);
		return result;
	}

	public String checkQueue(String queuekey){
		//pass to server command=startCount&locationId=queueKey
		String url1 = server + "&command=startCount&locationId=" + queuekey;
		String res1 = this.doGet(url1);
		long sleep = 10*1000;//sleep 10 seconds
		try{
			Thread.sleep(sleep);
		}catch(InterruptedException e){
			System.out.println("interrupted " + e.getMessage());
		}
		//pass to server command=stopCount&locationId=queueKey
		String url2 = server + "&command=stopCount&locationId=" + queuekey;
		String res2 = this.doGet(url2);
		return res1 + " " + res2 + " " + sleep;
	}
	
	private String doGet(String urlString){
		StringBuffer buf = new StringBuffer();
		try {
			System.out.println("sending request:" + urlString);
			URL url = new URL(urlString);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			
			while((line=br.readLine())!=null){
				System.out.println(line);
				buf.append(line);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
	
	public static void main(String[] args){

		AdminClient client = new AdminClient();
		if(args.length==0 || args[0].equalsIgnoreCase("checkListeners")){
			System.out.println(client.checkListeners());
		}else if(args[0].equalsIgnoreCase("checkQueue") && args.length==2){
			System.out.println(client.checkQueue(args[1]));
		}else{
			System.out.println(client.checkListeners());
		}
	}
}

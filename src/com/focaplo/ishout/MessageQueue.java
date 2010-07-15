package com.focaplo.ishout;

import java.util.logging.Logger;



public class MessageQueue {
	private static final Logger log = Logger.getLogger(MessageQueue.class.getName());
	public static int defaultSize = 1000;
	public long totalMessage=0;
	
	

	public long getTotalMessage() {
		return totalMessage;
	}

	public void setTotalMessage(long totalMessage) {
		this.totalMessage = totalMessage;
	}

	public int getDefaultSize() {
		return defaultSize;
	}

	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}

	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("--------------- queue content p= " + p + "----------");
		for(int i=0;i<storage.length;i++){
			if(storage[i]!=null && !storage[i].equals("")){
				buf.append("\n" + i+"="+storage[i]);
			}
		}
		buf.append("\n------------ end ------------");
		return buf.toString();
	}


	public int p = 0;
	//public List<String> storage = new ArrayList<String>(2000);
	String[] storage;
	
	public String[] getStorage() {
		return storage;
	}

	public void setStorage(String[] storage) {
		this.storage = storage;
	}

	public MessageQueue(int storage){
		super();
		this.init();
	}
	
	private void init(){
		storage = new String[defaultSize];
		for(int i=0; i<storage.length;i++){
			storage[i] = "";
		}
	}
	
	public void increaseQueueSite(int newSize){
		String[] newStorage = new String[newSize];
		for(int i=0;i<this.storage.length;i++){
			newStorage[i] = this.storage[i];
		}
		this.storage=newStorage;
	}
	public  String addMessage(String msg){
		synchronized(this){
			storage[p++] = msg;
			//p point to the next "space"
			p%= defaultSize; //[0-1999]
		}
		//
		this.totalMessage++;
		return "";
	}
	public  String addMessagePowerBall(String msg){
		for(int i=0;i<10;i++){
			this.addMessage(msg);
		}
		return "";
	}
	public String getMessages(int lastId){
		int currentp = 0;
		synchronized(this){
			currentp=p;
		}
		try{
			if(lastId==99999){
				//first req after listener starts, will return 20 messages from p (p-20->p)
				if(currentp>=20){
					lastId=currentp-21;
				}else{
					lastId=-1;
				}
			}
			if(currentp-1==lastId || currentp==lastId){
				//no message?
				return "SUCCESS|"+((currentp-1)>=0?(currentp-1):99999);
			}

			int candidates = 0;
			if(currentp>lastId){
				//pick from lastId+1 to p-1
				candidates = currentp-lastId-1;
			}else{
				candidates = defaultSize - lastId -1 + currentp;
			}
			if(candidates<0){
				log.warning("something wrong,should not happen! candidates<0 with lastId=" + lastId + " currentp="+currentp);
				return "SUCCESS|" +((currentp-1)>=0?(currentp-1):0);
			}
			int maxReturnMessages = 100;
			if(candidates<100){
				maxReturnMessages=100;
			}else if(candidates<300){
				maxReturnMessages=120;
			}else{
				maxReturnMessages=120;
			}
			int totalReturnMessage=0;

			StringBuffer buf = new StringBuffer();
			if(currentp>lastId){
				int possibleMsgNum = candidates<maxReturnMessages?candidates:maxReturnMessages;
				//try to get latest message instead of starting from last-id
				int start = p-possibleMsgNum;
				if(start<0){
					start=0;
				}
				
				totalReturnMessage = totalReturnMessage + this.getMessages(buf, start, currentp);
				

			}else if(currentp<lastId){
				int possibleMsgNum = candidates<maxReturnMessages?candidates:maxReturnMessages;
				int start=p-possibleMsgNum;
				if(start>0){
					//grab from start->p
					totalReturnMessage = totalReturnMessage + this.getMessages(buf, start, currentp);
				}else{
					//2 rounds
					//grab from 0->p
					totalReturnMessage = totalReturnMessage + this.getMessages(buf, 0, currentp);
					//grab from defaultSize+start->defaultSize (start<0)
					totalReturnMessage = totalReturnMessage + this.getMessages(buf, defaultSize+start, defaultSize);
					
				}
			}
			return "SUCCESS|" + ((currentp-1)>=0?(currentp-1):0) + "|" +  buf.toString();
		}catch(Exception e){
			log.warning("error:" + e.getMessage());
			return "SUCCESS|"+currentp;
		}
	}
	
	private int getMessages(StringBuffer buf, int start, int end){
		int totalReturnMessage = 0;
		boolean first=true;
		
		for(int i=start;i<end;i++){
				totalReturnMessage++;
				if(first){
					first=false;
					buf.append(storage[i]);
				}else{
					buf.append("^" + storage[i]);
				}
			
			
		}
		return totalReturnMessage;
	}
}

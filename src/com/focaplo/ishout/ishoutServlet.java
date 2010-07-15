package com.focaplo.ishout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.mortbay.log.Log;



@SuppressWarnings("serial")
public class ishoutServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(ishoutServlet.class.getName());
	public static String token="fopa032939478201020929";
	public static long messageId=12392;
	public static long lastCleaningMs = 0;
	public static long cleanIntervalMs = 2000; //every 2s
	public static boolean cleaning=false;
	public static long defaultQueueSize=1000;
	
	@SuppressWarnings("unchecked")
	public static Map allMessages = new HashMap();
	@SuppressWarnings("unchecked")
	public static Map listenerMap = new HashMap();
	
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		//set up the queue for each city???
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String token = req.getParameter("token");
		if(token==null || !token.equalsIgnoreCase(token)){
			return;
		}
//		log.warning(req.getQueryString());
		serve(req,resp);
	}
	
	private void serve(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String command=req.getParameter("command");
		if(command==null){
			return;
		}
		if(command.equalsIgnoreCase("upload")){
			this.upload(req,resp);
			
			
		}else if(command.equalsIgnoreCase("download")){
			this.download(req, resp);
			
		}else if(command.equalsIgnoreCase("register")){
			this.register(req, resp);
			
		}else if(command.equalsIgnoreCase("status")){
			this.checkStatus(req,resp);
		}else if(command.equalsIgnoreCase("startCount")){
			this.startCount(req,resp);
		}else if(command.equalsIgnoreCase("stopCount")){
			this.stopCount(req,resp);
		}else if(command.equalsIgnoreCase("increaseSize")){
			this.increaseSize(req,resp);
		}
	}
	private void increaseSize(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String locationId = req.getParameter("locationId");
		MessageQueue queue = (MessageQueue) this.allMessages.get(locationId);
		String result="";
		if(queue==null){
			result="queue is null!";
		}else{
			queue.increaseQueueSite(Integer.parseInt(req.getParameter("newSize")));
			result="success";
		}
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(result);
        resp.getWriter().flush();
	}
	private void stopCount(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String locationId = req.getParameter("locationId");
		MessageQueue queue = (MessageQueue) this.allMessages.get(locationId);
		if(queue!=null){
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().println("count:" + queue.getTotalMessage()+ " queue size:" + queue.getStorage().length);
	        resp.getWriter().flush();
		}
	}
	private void startCount(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String locationId = req.getParameter("locationId");
		MessageQueue queue = (MessageQueue) this.allMessages.get(locationId);
		if(queue!=null){
			queue.setTotalMessage(0);
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().println("count:" + queue.getTotalMessage() + " queue size:" + queue.getStorage().length);
	        resp.getWriter().flush();
		}
		
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	private void checkStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		StringBuffer buf = new StringBuffer();
		buf.append("\n listener map:");
		Iterator ite = this.listenerMap.entrySet().iterator();
		while(ite.hasNext()){
			Map.Entry<String, Integer> entry = (Entry<String, Integer>) ite.next();
			buf.append("\n" + entry.getKey() + ":" + entry.getValue());
		}
		
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(buf.toString());
        resp.getWriter().flush();
		return ;
	}
	@SuppressWarnings({ "static-access", "unchecked" })
	private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String locationId = req.getParameter("locationId");
		String oldLocationId = req.getParameter("replaceLocationId");
		if("99999".equals(locationId)){
			//this is a remove-listener request
		}else{
			Integer locationListener = (Integer) this.listenerMap.get(locationId);
			this.listenerMap.put(locationId, locationListener==null?1:locationListener+1);
		}
		if("99999".equals(oldLocationId)){
			//first time
		}else{
			Integer locationListener = (Integer) this.listenerMap.get(oldLocationId);
			this.listenerMap.put(oldLocationId, (locationListener==null||locationListener==1)?0:locationListener-1);
		}
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println("SUCCESS");
        resp.getWriter().flush();
	}
	@SuppressWarnings("unchecked")
	private void download(HttpServletRequest req,HttpServletResponse resp)  throws IOException{

		String locationId=req.getParameter("locationId");
		String lastMessageId = req.getParameter("lastMessageId");

		//
		MessageQueue queue = ((MessageQueue)allMessages.get(locationId));
		if(queue==null){
			queue = new MessageQueue(MessageQueue.defaultSize);
			allMessages.put(locationId, queue);
		}
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(queue.getMessages(Integer.parseInt(lastMessageId)));
        resp.getWriter().flush();
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	private void upload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String msg = req.getParameter("msg");
		String locationId = req.getParameter("locationId");

		MessageQueue queue = ((MessageQueue)allMessages.get(locationId));
		if(queue==null){
			queue = new MessageQueue(MessageQueue.defaultSize);
			allMessages.put(locationId, queue);
		}
		if(req.getParameter("powerBall")==null){
			queue.addMessage(msg);
		}else{
			queue.addMessagePowerBall(msg);
		}
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		Integer listenerNum = (Integer) this.listenerMap.get(locationId);
        resp.getWriter().println((listenerNum==null?0:listenerNum.intValue()) + "SUCCESS");
        resp.getWriter().flush();
	}
	
	
}

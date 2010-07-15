package com.focaplo.ishout;

import org.junit.Test;



public class MessageQueueTest{
	@Test
	public void testUploadMessage(){
		MessageQueue queue = new MessageQueue(10);

		//display
		System.out.println(queue.toString());
		queue.addMessage("1this is good message b");
		queue.addMessage("1this is good message c");
		queue.addMessage("4this is rude message d");
		queue.addMessage("5this is dirty message e");
		queue.addMessage("1this is good message f");
		System.out.println(queue);
		queue.addMessage("7this is terrible message g");
		queue.addMessage("2this is good message h");
		queue.addMessage("8this is racist message j");
		queue.addMessage("1this is good message k");
		System.out.println(queue);
		queue.addMessage("8this is racist message l");
		queue.addMessage("1this is good message m");
		queue.addMessage("1this is good message n");
		System.out.println(queue);
		queue.addMessage("1this is good message o");
		queue.addMessage("1this is good message p");
		System.out.println(queue);
		
	}
	
	public void testDownloadMessages(){
		MessageQueue queue = new MessageQueue(10);
		queue.addMessage("1this is good message b");
		queue.addMessage("1this is good message c");
		queue.addMessage("4this is rude message d");
		queue.addMessage("5this is dirty message e");
		queue.addMessage("1this is good message f");
		System.out.println("-----download-----");
		System.out.println(queue.getMessages(9999));
		queue.addMessage("7this is terrible message g");
		queue.addMessage("2this is good message h");
		queue.addMessage("8this is racist message j");
		queue.addMessage("1this is good message k");
		queue.addMessage("8this is racist message l");
		System.out.println("-----download-----");
		System.out.println(queue);
		System.out.println(queue.getMessages(5));
		queue.addMessage("1this is good message m");
		queue.addMessage("1this is good message n");
		queue.addMessage("1this is good message o");
		queue.addMessage("1this is good message p");
		System.out.println(queue);
		System.out.println("-----download-----");
		System.out.println(queue.getMessages(7));
		System.out.println("-----download-----");
		System.out.println(queue.getMessages(3));
		System.out.println("-----download-----");
		System.out.println(queue.getMessages(9));
	}
	
	public void testDirty(){
		MessageQueue queue = new MessageQueue(10);
		queue.addMessage("1this is good message 1");
		queue.addMessage("2this is good message 2");
		queue.addMessage("3this is good message 3");
		queue.addMessage("4this is good message 4");
		queue.addMessage("5this is good message 5");
		queue.addMessage("6this is good message 5");
		queue.addMessage("7this is good message 5");
		queue.addMessage("8this is good message 5");
		queue.addMessage("9this is good message 5");
		System.out.println("-----download-----");
		System.out.println(queue.getMessages(99999));
		System.out.println(queue.getMessages(99999));
	}
}

package com.focaplo.ishout;

public class ShoutMessage {
	long uploadedMs;
	String msg;
	long id;
	long numberOfHeard;
	long checkStatusDelayMs;

	public long getCheckStatusDelayMs() {
		return checkStatusDelayMs;
	}
	public void setCheckStatusDelayMs(long checkStatusDelayMs) {
		this.checkStatusDelayMs = checkStatusDelayMs;
	}
	public long getNumberOfHeard() {
		return numberOfHeard;
	}
	public void setNumberOfHeard(long numberOfHeard) {
		this.numberOfHeard = numberOfHeard;
	}
	public long getUploadedMs() {
		return uploadedMs;
	}
	public void setUploadedMs(long uploadedMs) {
		this.uploadedMs = uploadedMs;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}

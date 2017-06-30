package com.datacomo.mc.spider.android.net.been;

public class ChatInfoBean {
	private String date;
	private String content;
	private boolean isIn;
	
	public ChatInfoBean(String date, String content, boolean isIn) {
		setDate(date);
		setContent(content);
		setIsIn(isIn);
	}

	public boolean isIn() {
		return isIn;
	}

	public void setIsIn(boolean isIn) {
		this.isIn = isIn;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

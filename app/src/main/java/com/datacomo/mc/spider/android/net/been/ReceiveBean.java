package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class ReceiveBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -804927258087367486L;
	/**
	 * 接收者Id
	 */
	private int id;
	/**
	 * 接收者名字
	 */
	private String name;

	/**
	 * 接收邮箱
	 */
	private String mail;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}

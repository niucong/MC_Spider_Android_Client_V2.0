package com.datacomo.mc.spider.android.net.been;

public class InviteRegisterBean {
	private String PHONE;
	private String HEADURL;
	private String MEMBERID;
	private String HEADPATH;
	private String REGISTER_TIME;
	private String STATUS;
	private String MEMBERNAME;

	/*
	 * @return List Map STATUS MEMBERID MEMBERNAME PHONE 0 0 "" "" （系统异常） 1 0 ""
	 * * 手机号码 （未注册） 2 社员编号 社员昵称 手机号码 （已注册,非好友） 3 社员编号 社员昵称 手机号码 （已注册，好友） 4 0
	 * "" 手机号码 （手机号码是自己的） 5 0 "" "" （手机号码参数信息为空） 6 0 "" "" （操作者不存在）
	 */
	public InviteRegisterBean(){}
	
	public String getPHONE() {
		return PHONE;
	}

	public void setPHONE(String PHONE) {
		this.PHONE = PHONE;
	}

	public String getHEADURL() {
		return HEADURL;
	}

	public void setHEADURL(String HEADURL) {
		this.HEADURL = HEADURL;
	}

	public String getMEMBERID() {
		return MEMBERID;
	}

	public void setMEMBERID(String MEMBERID) {
		this.MEMBERID = MEMBERID;
	}

	public String getHEADPATH() {
		return HEADPATH;
	}

	public void setHEADPATH(String HEADPATH) {
		this.HEADPATH = HEADPATH;
	}

	public String getREGISTER_TIME() {
		return REGISTER_TIME;
	}

	public void setREGISTER_TIME(String REGISTER_TIME) {
		this.REGISTER_TIME = REGISTER_TIME;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String STATUS) {
		this.STATUS = STATUS;
	}

	public String getMEMBERNAME() {
		return MEMBERNAME;
	}

	public void setMEMBERNAME(String MEMBERNAME) {
		this.MEMBERNAME = MEMBERNAME;
	}

}

package com.datacomo.mc.spider.android.net.been;

public class NewNoticeBean {

	private boolean EXIST_NEW_NOTICE;
	private MessageNoticeBean NOTICE_OBJECT;
	private String NOTICE_TYPE;

	public boolean getEXIST_NEW_NOTICE() {
		return EXIST_NEW_NOTICE;
	}

	public void setEXIST_NEW_NOTICE(boolean eXIST_NEW_NOTICE) {
		EXIST_NEW_NOTICE = eXIST_NEW_NOTICE;
	}

	public MessageNoticeBean getNOTICE_OBJECT() {
		return NOTICE_OBJECT;
	}

	public void setNOTICE_OBJECT(MessageNoticeBean nOTICE_OBJECT) {
		NOTICE_OBJECT = nOTICE_OBJECT;
	}

	public String getNOTICE_TYPE() {
		return NOTICE_TYPE;
	}

	public void setNOTICE_TYPE(String nOTICE_TYPE) {
		NOTICE_TYPE = nOTICE_TYPE;
	}

}

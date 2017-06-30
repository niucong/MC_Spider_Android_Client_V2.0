package com.datacomo.mc.spider.android.net.been;

public class LoginJustByPhoneBean {

	// private int FRIEND_TREND_NUM;
	// private int LOGIN_STATE;
	// private int MEMBER_STATE;
	private String MEMBER_NAME;
	// private int FRIEND_NUM;
	private String MEMBER_HEAD_URL;
	private String PASSWORD;
	private String session_key;
	private String MEMBER_HEAD_PATH;
	private int MEMBER_ID;

	// private int LOAD_NUM;
	public String getMEMBER_NAME() {
		return MEMBER_NAME;
	}

	public void setMEMBER_NAME(String mEMBER_NAME) {
		MEMBER_NAME = mEMBER_NAME;
	}

	public String getMEMBER_HEAD_URL() {
		return MEMBER_HEAD_URL;
	}

	public void setMEMBER_HEAD_URL(String mEMBER_HEAD_URL) {
		MEMBER_HEAD_URL = mEMBER_HEAD_URL;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getMEMBER_HEAD_PATH() {
		return MEMBER_HEAD_PATH;
	}

	public void setMEMBER_HEAD_PATH(String mEMBER_HEAD_PATH) {
		MEMBER_HEAD_PATH = mEMBER_HEAD_PATH;
	}

	public int getMEMBER_ID() {
		return MEMBER_ID;
	}

	public void setMEMBER_ID(int mEMBER_ID) {
		MEMBER_ID = mEMBER_ID;
	}
}

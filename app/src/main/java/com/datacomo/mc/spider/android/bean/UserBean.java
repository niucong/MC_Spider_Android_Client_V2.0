package com.datacomo.mc.spider.android.bean;

public class UserBean {

	private String username; // 用户名__手机号
	private String password; // 密码
	private String mark; // 用户登录标志 yes-为当前登录账号 no-为没有登录的账户
	private String session_key;

	private String accountType = "0";
	private String openId;
	private String name;
	private String sex;
	private String headUrlPath;
	private String oauth_token;
	private String access_Token;
	private String access_Token_secret;
	
	private String memberId;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public UserBean(){
		
	}
	
	public UserBean(String username, String password, String session_key,
			String mark) {
		this.username = username;
		this.password = password;
		this.mark = mark;

		setSession_key(session_key);
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getMark() {
		return mark;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getHeadUrlPath() {
		return headUrlPath;
	}

	public void setHeadUrlPath(String headUrlPath) {
		this.headUrlPath = headUrlPath;
	}

	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	public String getOauth_token() {
		return oauth_token;
	}

	public String getAccess_Token() {
		return access_Token;
	}

	public void setAccess_Token(String access_Token) {
		this.access_Token = access_Token;
	}

	public String getAccess_Token_secret() {
		return access_Token_secret;
	}

	public void setAccess_Token_secret(String access_Token_secret) {
		this.access_Token_secret = access_Token_secret;
	}

}

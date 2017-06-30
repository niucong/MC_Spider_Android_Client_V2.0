package com.datacomo.mc.spider.android.net.been;

public class ThirdInfoBean {

	/**
	 * 社员标识号
	 */
	private int memberId;
	/**
	 * 第三方类型 1：新浪 2：腾讯 3：人人 4：开心 5: 搜狐 6: 网易 7: 豆瓣
	 */
	private int member_source;
	/**
	 * 可能是第三方Id 或者openid
	 */
	private String openid;
	/**
	 * oauth_token
	 */
	private String oauth_token;
	/**
	 * oauth_token_secret
	 */
	private String oauth_token_secret;
	private String creatTime;

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getMember_source() {
		return member_source;
	}

	public void setMember_source(int member_source) {
		this.member_source = member_source;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOauth_token() {
		return oauth_token;
	}

	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	public String getOauth_token_secret() {
		return oauth_token_secret;
	}

	public void setOauth_token_secret(String oauth_token_secret) {
		this.oauth_token_secret = oauth_token_secret;
	}

}

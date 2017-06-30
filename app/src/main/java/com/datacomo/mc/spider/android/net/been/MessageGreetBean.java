package com.datacomo.mc.spider.android.net.been;

public class MessageGreetBean {

	/**
	 * 打招呼通知表主键
	 */
	private int noticeGreetId;
	/**
	 * 打招呼人编号
	 */
	private int sendMemberId;
	/**
	 * 打招呼人名字
	 */
	private String sendMemberName;
	/**
	 * 打招呼人头像
	 */
	private MemberHeadBean headInfo;
	/**
	 * 接收者编号
	 */
	private int receiveMemberId;
	/**
	 * 发送方式\n1：web\n2：wap\n3：iphone\n4：android\n5：SMS
	 */
	private String sendWay;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 招呼图片地址
	 */
	private String greetImgUrl;
	/**
	 * 招呼图片路径
	 */
	private String greetImgPath;
	/**
	 * 招呼Id
	 */
	private int greetId;
	/**
	 * 招呼名字
	 */
	private String greetName;
	/**
	 * 招呼类型
	 */
	private String greetType;
	/**
	 * 招呼值
	 */
	private String greetValue;
	/**
	 * 回复招呼Id
	 */
	private int reGreetId;
	/**
	 * 回复招呼值
	 */
	private String reGreegValue;

	public int getNoticeGreetId() {
		return noticeGreetId;
	}

	public void setNoticeGreetId(int noticeGreetId) {
		this.noticeGreetId = noticeGreetId;
	}

	public int getSendMemberId() {
		return sendMemberId;
	}

	public void setSendMemberId(int sendMemberId) {
		this.sendMemberId = sendMemberId;
	}

	public String getSendMemberName() {
		return sendMemberName;
	}

	public void setSendMemberName(String sendMemberName) {
		this.sendMemberName = sendMemberName;
	}

	public MemberHeadBean getHeadInfo() {
		return headInfo;
	}

	public void setHeadInfo(MemberHeadBean headInfo) {
		this.headInfo = headInfo;
	}

	public int getReceiveMemberId() {
		return receiveMemberId;
	}

	public void setReceiveMemberId(int receiveMemberId) {
		this.receiveMemberId = receiveMemberId;
	}

	public String getSendWay() {
		return sendWay;
	}

	public void setSendWay(String sendWay) {
		this.sendWay = sendWay;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getGreetImgUrl() {
		return greetImgUrl;
	}

	public void setGreetImgUrl(String greetImgUrl) {
		this.greetImgUrl = greetImgUrl;
	}

	public String getGreetImgPath() {
		return greetImgPath;
	}

	public void setGreetImgPath(String greetImgPath) {
		this.greetImgPath = greetImgPath;
	}

	public int getGreetId() {
		return greetId;
	}

	public void setGreetId(int greetId) {
		this.greetId = greetId;
	}

	public String getGreetName() {
		return greetName;
	}

	public void setGreetName(String greetName) {
		this.greetName = greetName;
	}

	public String getGreetType() {
		return greetType;
	}

	public void setGreetType(String greetType) {
		this.greetType = greetType;
	}

	public String getGreetValue() {
		return greetValue;
	}

	public void setGreetValue(String greetValue) {
		this.greetValue = greetValue;
	}

	public int getReGreetId() {
		return reGreetId;
	}

	public void setReGreetId(int reGreetId) {
		this.reGreetId = reGreetId;
	}

	public String getReGreegValue() {
		return reGreegValue;
	}

	public void setReGreegValue(String reGreegValue) {
		this.reGreegValue = reGreegValue;
	}

}

package com.datacomo.mc.spider.android.net.been;

public class MailContactBean {

	/**
	 * 记录Id
	 */
	private int recordId;
	/**
	 * 关系Id
	 */
	private int leaguerId;
	/**
	 * 发送者Id
	 */
	// private int sendMemberId;
	/**
	 * 发送者名字
	 */
	// private String sendMemberName;
	/**
	 * 发送者路径
	 */
	// private String sendMemberUrl;
	/**
	 * 发送者的path
	 */
	// private String sendMemberPath;
	/**
	 * 发送者邮箱
	 */
	// private String sendMemberMail;
	// private List<ReceiveBean> receivers;
	/**
	 * 接收者Id
	 */
	private int relationMemberId;

	/**
	 * 接收者名字 邮箱地址 或者社员名字'
	 */
	private String relationMemberName;
	/**
	 * 接收者路径
	 */
	private String relationMemberUrl;
	/**
	 * 接收者的path
	 */
	private String relationMemberPath;
	/**
	 * 接收邮箱
	 */
	// private String relationMemberMail;
	/**
	 * 此邮件共发送个的邮箱或id 接收人邮箱|名字的集合，以分号分隔
	 */
	// private String receiveMembers;

	/**
	 * 简单邮件内容
	 */
	// private String simpleMailContent;
	// private String mailHtmlContent;
	/**
	 * 邮件标题
	 */
	private String mailSubject;
	/**
	 * 已读未读状态 n1：未读\n2：已读
	 */
	private int isRead;
	/**
	 * 发送接收状态 n1：发送\n2：接收
	 */
	private int srStatus;
	/**
	 * 最后联系时间{联系人记录列表}
	 */
	private String lastContactTime;
	/**
	 * 邮件来往记录产生时间{我与某人的联系记录列表}
	 */
	private String contactTime;
	/**
	 * 最后联系邮件Id
	 */
	private int mailId;
	/**
	 * 总数量
	 */
	private int totalNum;
	/**
	 * 新数量
	 */
	private int newNum;
	/**
	 * 联系者类型 n1：社员编号\n2：邮箱地址'
	 */
	// private int relationType;

	// public String getMailHtmlContent() {
	// return mailHtmlContent;
	// }
	//
	// public void setMailHtmlContent(String mailHtmlContent) {
	// this.mailHtmlContent = mailHtmlContent;
	// }

	// public List<ReceiveBean> getReceivers() {
	// return receivers;
	// }
	//
	// public void setReceivers(List<ReceiveBean> receivers) {
	// this.receivers = receivers;
	// }

	/**
	 * 附件数量
	 */
	private int attachmentNum;

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public String getContactTime() {
		return contactTime;
	}

	public void setContactTime(String contactTime) {
		this.contactTime = contactTime;
	}

	public int getAttachmentNum() {
		return attachmentNum;
	}

	public void setAttachmentNum(int attachmentNum) {
		this.attachmentNum = attachmentNum;
	}

	// public int getRelationType() {
	// return relationType;
	// }
	//
	// public void setRelationType(int relationType) {
	// this.relationType = relationType;
	// }

	public int getLeaguerId() {
		return leaguerId;
	}

	public void setLeaguerId(int leaguerId) {
		this.leaguerId = leaguerId;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getNewNum() {
		return newNum;
	}

	public void setNewNum(int newNum) {
		this.newNum = newNum;
	}

	public String getLastContactTime() {
		return lastContactTime;
	}

	public void setLastContactTime(String lastContactTime) {
		this.lastContactTime = lastContactTime;
	}

	// public String getSendMemberMail() {
	// return sendMemberMail;
	// }
	//
	// public void setSendMemberMail(String sendMemberMail) {
	// this.sendMemberMail = sendMemberMail;
	// }

	// public int getSendMemberId() {
	// return sendMemberId;
	// }
	//
	// public void setSendMemberId(int sendMemberId) {
	// this.sendMemberId = sendMemberId;
	// }

	// public String getSendMemberName() {
	// return sendMemberName;
	// }
	//
	// public void setSendMemberName(String sendMemberName) {
	// this.sendMemberName = sendMemberName;
	// }

	// public String getSendMemberUrl() {
	// return sendMemberUrl;
	// }
	//
	// public void setSendMemberUrl(String sendMemberUrl) {
	// this.sendMemberUrl = sendMemberUrl;
	// }

	// public String getSendMemberPath() {
	// return sendMemberPath;
	// }
	//
	// public void setSendMemberPath(String sendMemberPath) {
	// this.sendMemberPath = sendMemberPath;
	// }

	// public String getSimpleMailContent() {
	// return simpleMailContent;
	// }
	//
	// public void setSimpleMailContent(String simpleMailContent) {
	// this.simpleMailContent = simpleMailContent;
	// }

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getSrStatus() {
		return srStatus;
	}

	public void setSrStatus(int srStatus) {
		this.srStatus = srStatus;
	}

	public int getMailId() {
		return mailId;
	}

	public void setMailId(int mailId) {
		this.mailId = mailId;
	}

	public int getRelationMemberId() {
		return relationMemberId;
	}

	public void setRelationMemberId(int relationMemberId) {
		this.relationMemberId = relationMemberId;
	}

	public String getRelationMemberName() {
		return relationMemberName;
	}

	public void setRelationMemberName(String relationMemberName) {
		this.relationMemberName = relationMemberName;
	}

	public String getRelationMemberUrl() {
		return relationMemberUrl;
	}

	public void setRelationMemberUrl(String relationMemberUrl) {
		this.relationMemberUrl = relationMemberUrl;
	}

	public String getRelationMemberPath() {
		return relationMemberPath;
	}

	public void setRelationMemberPath(String relationMemberPath) {
		this.relationMemberPath = relationMemberPath;
	}

	// public String getRelationMemberMail() {
	// return relationMemberMail;
	// }
	//
	// public void setRelationMemberMail(String relationMemberMail) {
	// this.relationMemberMail = relationMemberMail;
	// }

	// public String getReceiveMembers() {
	// return receiveMembers;
	// }
	//
	// public void setReceiveMembers(String receiveMembers) {
	// this.receiveMembers = receiveMembers;
	// }

}

package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;
import java.util.List;

public class MailBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7450430752395731799L;
	/**
	 * 邮件Id
	 */
	private int mailId;
	/**
	 * messageId
	 */
	private String messageId;
	/**
	 * 发送者Id
	 */
	private int sendMemberId;
	/**
	 * 发送者名字
	 */
	private String sendMemberName;
	/**
	 * 发送者路径
	 */
	private String sendMemberUrl;
	/**
	 * 发送者的path
	 */
	private String sendMemberPath;

	/**
	 * 发送邮箱
	 */
	private String sendMemberMail;
	/**
	 * 接收邮箱
	 */
	private List<ReceiveBean> receivers;
	/**
	 * 简单邮件内容
	 */
	private String mailContent;
	/**
	 * 带标签的邮件内容
	 */
	private String mailHtmlContent;

	/**
	 * 邮件标题
	 */
	private String mailSubject;
	/**
	 * 邮件创建时间
	 */
	private String createTime;
	/**
	 * 抄送人
	 */
	private List<ReceiveBean> carbonCopy;
	/**
	 * 密送人
	 */
	private List<ReceiveBean> blindCarbonCopy;
	/**
	 * 附件
	 */
	private List<AttachMent> attachMent;

	/**
	 * 附件数量
	 */
	private int attachmentNum;
	/**
	 * 转发次数
	 */
	private int relayNum;
	/**
	 * 回复次数
	 */
	private int replyNum;

	/**
	 * 数据类型 1 邮件 2 草稿
	 */
	private int dateType;
	/**
	 * 数据ID 如果为邮件则为邮件recordId 如果为草稿则为draftId
	 */
	private int dateId;
	/**
	 * 是否已读
	 */
	private int isRead;
	/**
	 * 删除时间
	 */
	private String deleteDate;

	/**
	 * 发送接收状态
	 */
	private int srStatus;

	/**
	 * 上一封邮件Id
	 */
	private int previousEmailId;

	/**
	 * 下一封邮件Id
	 */
	private int nextEmailId;

	/**
	 * 上一封邮件的recordId
	 */
	private int previousEmailRecordId;

	/**
	 * 下一封邮件的recordId
	 */
	private int nextEmailRecordId;
	
	private int isDeleted;

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getPreviousEmailRecordId() {
		return previousEmailRecordId;
	}

	public void setPreviousEmailRecordId(int previousEmailRecordId) {
		this.previousEmailRecordId = previousEmailRecordId;
	}

	public int getNextEmailRecordId() {
		return nextEmailRecordId;
	}

	public void setNextEmailRecordId(int nextEmailRecordId) {
		this.nextEmailRecordId = nextEmailRecordId;
	}

	public int getPreviousEmailId() {
		return previousEmailId;
	}

	public void setPreviousEmailId(int previousEmailId) {
		this.previousEmailId = previousEmailId;
	}

	public int getNextEmailId() {
		return nextEmailId;
	}

	public void setNextEmailId(int nextEmailId) {
		this.nextEmailId = nextEmailId;
	}

	public int getMailId() {
		return mailId;
	}

	public void setMailId(int mailId) {
		this.mailId = mailId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
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

	public String getSendMemberUrl() {
		return sendMemberUrl;
	}

	public void setSendMemberUrl(String sendMemberUrl) {
		this.sendMemberUrl = sendMemberUrl;
	}

	public String getSendMemberPath() {
		return sendMemberPath;
	}

	public void setSendMemberPath(String sendMemberPath) {
		this.sendMemberPath = sendMemberPath;
	}

	public String getSendMemberMail() {
		return sendMemberMail;
	}

	public void setSendMemberMail(String sendMemberMail) {
		this.sendMemberMail = sendMemberMail;
	}

	public List<ReceiveBean> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<ReceiveBean> receivers) {
		this.receivers = receivers;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getMailHtmlContent() {
		return mailHtmlContent;
	}

	public void setMailHtmlContent(String mailHtmlContent) {
		this.mailHtmlContent = mailHtmlContent;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<ReceiveBean> getCarbonCopy() {
		return carbonCopy;
	}

	public void setCarbonCopy(List<ReceiveBean> carbonCopy) {
		this.carbonCopy = carbonCopy;
	}

	public List<ReceiveBean> getBlindCarbonCopy() {
		return blindCarbonCopy;
	}

	public void setBlindCarbonCopy(List<ReceiveBean> blindCarbonCopy) {
		this.blindCarbonCopy = blindCarbonCopy;
	}

	public List<AttachMent> getAttachMent() {
		return attachMent;
	}

	public void setAttachMent(List<AttachMent> attachMent) {
		this.attachMent = attachMent;
	}

	public int getAttachmentNum() {
		return attachmentNum;
	}

	public void setAttachmentNum(int attachmentNum) {
		this.attachmentNum = attachmentNum;
	}

	public int getRelayNum() {
		return relayNum;
	}

	public void setRelayNum(int relayNum) {
		this.relayNum = relayNum;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public int getDateId() {
		return dateId;
	}

	public void setDateId(int dateId) {
		this.dateId = dateId;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(String deleteDate) {
		this.deleteDate = deleteDate;
	}

	public int getSrStatus() {
		return srStatus;
	}

	public void setSrStatus(int srStatus) {
		this.srStatus = srStatus;
	}

}

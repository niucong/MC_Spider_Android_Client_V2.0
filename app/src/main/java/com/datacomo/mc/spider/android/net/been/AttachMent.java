package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class AttachMent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6708184739072998799L;
	/*
	 * 附件编号
	 */
	private int attachmentId;
	/*
	 * 附件编号
	 */
	private int mailId;
	/*
	 * 格式编号
	 */
	private int formatId;
	/*
	 * 附件格式
	 */
	private String formatName;
	/*
	 * 附件格式路径
	 */
	private String formatUrl;
	private String formatPath;
	/*
	 * 附件名字
	 */
	private String name;
	/*
	 * 附件大小
	 */
	private int size;
	/*
	 * 附件路径
	 */
	private String attachmentUrl;
	private String path;
	/*
	 * 附件本地路径
	 */
	private String attachmentLocal;
	private String createTime;

	public int getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(int attachmentId) {
		this.attachmentId = attachmentId;
	}

	public int getMailId() {
		return mailId;
	}

	public void setMailId(int mailId) {
		this.mailId = mailId;
	}

	public int getFormatId() {
		return formatId;
	}

	public void setFormatId(int formatId) {
		this.formatId = formatId;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public String getFormatUrl() {
		return formatUrl;
	}

	public void setFormatUrl(String formatUrl) {
		this.formatUrl = formatUrl;
	}

	public String getFormatPath() {
		return formatPath;
	}

	public void setFormatPath(String formatPath) {
		this.formatPath = formatPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAttachmentLocal() {
		return attachmentLocal;
	}

	public void setAttachmentLocal(String attachmentLocal) {
		this.attachmentLocal = attachmentLocal;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

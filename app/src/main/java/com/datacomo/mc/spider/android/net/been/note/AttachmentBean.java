package com.datacomo.mc.spider.android.net.been.note;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

/**
 * 附件BEAN
 * 
 * @author liuhang
 * 
 */
public class AttachmentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020961999411376859L;
	/**
	 * 附件编号
	 */
	private int adjunctId;
	/**
	 * 笔记编号
	 */
	private int noteId;
	/**
	 * 附件类型 1:照片，2:视频，3:文件，4:音频
	 */
	private int adjunctType;
	/**
	 * 附件名字
	 */
	private String adjunctName;
	/**
	 * 附件前缀
	 */
	private String adjunctUrl;
	/**
	 * 附件路径
	 */
	private String adjunctPath;
	/**
	 * 附件本地路径
	 */
	private String adjuctLocal;
	/**
	 * 附件大小
	 */
	private int adjunctSize;
	/**
	 * 文件格式编号
	 */
	private int formatId;
	/**
	 * 文件格式名字
	 */
	private String formatName;
	/**
	 * 文件格式图片路径前缀
	 */
	private String formatUrl;
	/**
	 * 文件格式图片路径
	 */
	private String formatPath;
	/**
	 * 上传时间
	 */
	private Date uploadTime;
	/**
	 * 附件后缀名
	 */
	private String suffixName;
	/**
	 * 附件Url和Path的拼串
	 */
	private String adjunctUrlAndPath;
	/**
	 * 附件Url和Path的拼串的List
	 */
	private List<String> urlAndPath;

	public int getAdjunctId() {
		return adjunctId;
	}

	public void setAdjunctId(int adjunctId) {
		this.adjunctId = adjunctId;
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public int getAdjunctType() {
		return adjunctType;
	}

	public void setAdjunctType(int adjunctType) {
		this.adjunctType = adjunctType;
	}

	public String getAdjunctName() {
		return adjunctName;
	}

	public void setAdjunctName(String adjunctName) {
		this.adjunctName = adjunctName;
	}

	public String getAdjunctUrl() {
		return adjunctUrl;
	}

	public void setAdjunctUrl(String adjunctUrl) {
		this.adjunctUrl = adjunctUrl;
	}

	public String getAdjunctPath() {
		return adjunctPath;
	}

	public void setAdjunctPath(String adjunctPath) {
		this.adjunctPath = adjunctPath;
	}

	public String getAdjuctLocal() {
		return adjuctLocal;
	}

	public void setAdjuctLocal(String adjuctLocal) {
		this.adjuctLocal = adjuctLocal;
	}

	public int getAdjunctSize() {
		return adjunctSize;
	}

	public void setAdjunctSize(int adjunctSize) {
		this.adjunctSize = adjunctSize;
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

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}

	public String getAdjunctUrlAndPath() {
		return adjunctUrlAndPath;
	}

	public List<String> getUrlAndPath() {
		return urlAndPath;
	}

	public void setUrlAndPath(List<String> urlAndPath) {
		this.urlAndPath = urlAndPath;
	}

	public void setAdjunctUrlAndPath(String adjunctUrlAndPath) {
		this.adjunctUrlAndPath = adjunctUrlAndPath;
	}

	public String toJsonString() {
		JSONObject json = new JSONObject();
		try {
			json.put("adjunctId", adjunctId);
			json.put("noteId", noteId);
			json.put("adjunctType", adjunctType);
			json.put("adjunctName", adjunctName);
			json.put("adjunctUrl", adjunctUrl);
			json.put("adjunctPath", adjunctPath);
			json.put("adjuctLocal", adjuctLocal);
			json.put("adjunctSize", adjunctSize);
			json.put("formatId", formatId);
			json.put("formatName", formatName);
			json.put("formatUrl", formatUrl);
			json.put("formatPath", formatPath);
			json.put("uploadTime", null);
			json.put("suffixName", suffixName);
			json.put("adjunctUrlAndPath", "");
			json.put("urlAndPath", "[]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
}

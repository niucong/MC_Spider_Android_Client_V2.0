package com.datacomo.mc.spider.android.net.been.note;

import java.io.Serializable;
import java.util.Date;

/**
 * 笔记本Bean
 * 
 * @author liuhang
 * 
 */
public class NoteBookBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3865277781007517138L;

	/**
	 * 笔记本编号
	 */
	private int noteBookId;
	/**
	 * 社员编号
	 */
	private int memberId;
	/**
	 * 笔记本标题
	 */
	private String title;

	/**
	 * 笔记本封面颜色
	 */
	private String coverColor;
	/**
	 * 笔记本封面照片Path
	 */
	private String coverPhotoPath;
	/**
	 * 笔记本封面照片Url
	 */
	private String coverPhotoUrl;
	/**
	 * 笔记数量
	 */
	private int notesNum;
	/**
	 * 来源类型 CREATE 创建 SHARE 分享
	 */
	private String sourceType;
	/**
	 * 是否已读 1：未读 2：已读
	 */
	private int isRead;
	/**
	 * 是否被删除 1：未删除 2：已删除
	 */
	private int isDelete;
	/**
	 * 笔记本密码
	 */
	private String noteBookPwd;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 分享社员编号
	 */
	private int shareMemberId;
	/**
	 * 分享社员名字
	 */
	private String shareMemberName;
	/**
	 * 分享社员Url
	 */
	private String shareMemberUrl;
	/**
	 * 分享社员Path
	 */
	private String shareMemberPath;
	/**
	 * 分享社员数量
	 */
	private int shareMemberNum;
	/**
	 * 分享附言
	 */
	private String shareRemark;
	/**
	 * ip
	 */
	private String ip;
	/**
	 * way
	 */
	private String way;
	/**
	 * source
	 */
	private String source;

	public int getNotesNum() {
		return notesNum;
	}

	public void setNotesNum(int notesNum) {
		this.notesNum = notesNum;
	}

	public int getNoteBookId() {
		return noteBookId;
	}

	public void setNoteBookId(int noteBookId) {
		this.noteBookId = noteBookId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCoverColor() {
		return coverColor;
	}

	public void setCoverColor(String coverColor) {
		this.coverColor = coverColor;
	}

	public String getCoverPhotoPath() {
		return coverPhotoPath;
	}

	public void setCoverPhotoPath(String coverPhotoPath) {
		this.coverPhotoPath = coverPhotoPath;
	}

	public String getCoverPhotoUrl() {
		return coverPhotoUrl;
	}

	public void setCoverPhotoUrl(String coverPhotoUrl) {
		this.coverPhotoUrl = coverPhotoUrl;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getNoteBookPwd() {
		return noteBookPwd;
	}

	public void setNoteBookPwd(String noteBookPwd) {
		this.noteBookPwd = noteBookPwd;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getShareMemberId() {
		return shareMemberId;
	}

	public void setShareMemberId(int shareMemberId) {
		this.shareMemberId = shareMemberId;
	}

	public int getShareMemberNum() {
		return shareMemberNum;
	}

	public void setShareMemberNum(int shareMemberNum) {
		this.shareMemberNum = shareMemberNum;
	}

	public String getShareRemark() {
		return shareRemark;
	}

	public void setShareRemark(String shareRemark) {
		this.shareRemark = shareRemark;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getShareMemberName() {
		return shareMemberName;
	}

	public void setShareMemberName(String shareMemberName) {
		this.shareMemberName = shareMemberName;
	}

	public String getShareMemberUrl() {
		return shareMemberUrl;
	}

	public void setShareMemberUrl(String shareMemberUrl) {
		this.shareMemberUrl = shareMemberUrl;
	}

	public String getShareMemberPath() {
		return shareMemberPath;
	}

	public void setShareMemberPath(String shareMemberPath) {
		this.shareMemberPath = shareMemberPath;
	}

}

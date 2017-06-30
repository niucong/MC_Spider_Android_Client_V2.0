package com.datacomo.mc.spider.android.net.been.note;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 笔记Bean
 * 
 * @author wang chuanhai
 * @date 2013-7-11 下午15:43:22
 * @version v1.0.0
 */
public class NoteInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8009092756651735778L;

	/**
	 * serialVersionUID
	 */

	/**
	 * 记事表主键
	 */
	private int noteId;

	/**
	 * 笔记所有者
	 */
	private int ownerMemberId;

	/**
	 * 笔记标题
	 */
	private String noteTitle;

	/**
	 * 记事内容
	 */
	private String noteContent;

	/**
	 * 分享社员ID
	 */
	private int shareMemberId;

	/**
	 * 来源类型：CREATE ,SHARE(web 分享 ，android 分享，touch 分享.......)
	 */
	private String sourceType;

	/**
	 * 是否星标,1：是，2：否
	 */
	private int isStarTarget;

	/**
	 * 是否删除,1：是，2：否
	 */
	private int isDelete;

	/**
	 * 附件数量
	 */
	private int adjunctNum;

	/**
	 * 附件列表
	 */
	private List<AttachmentBean> attachmentList;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 更新时间
	 */
	private String updateTime;

	// /**
	// * 创建IP
	// */
	// private String createIp;
	//
	// /**
	// * 创建方式
	// */
	// private String createWay;

	/**
	 * 笔记本编号，当为0时，该笔记不属于任何一个笔记本，在首页显示
	 */
	private int notebookId;

	/**
	 * 笔记密码，当为空时，无密码
	 */
	private String notePwd;

	/**
	 * 系统来源:\nMC_WEB : 公社web\nMC_WAP : 公社wap\nMC_I/A :公社iphone/android\nMC_SMS
	 * :公社短信\nHUBEI_WEB :湖北web\nHUNAN_WEB :湖南web'
	 */
	private String noteSource;

	/**
	 * 分享的社员总数量
	 */
	private int shareMemberNum;

	/**
	 * 分享附言
	 */
	private String shareRmark;
	/**
	 * 是否已读 1：未读 2：已读
	 */
	private int isRead;

	/**
	 * 第一张附件照片前缀
	 */
	private String firstPhotoUrl;

	/**
	 * 第一张附件照片路径
	 */
	private String firstPhotoPath;

	// /**
	// * 第一张附件照片本地路径
	// */
	// private String firstPhotoLocal;

	/**
	 * 分享社员姓名
	 */
	private String shareMemberName;

	/**
	 * 分享社员头像URL
	 */
	private String shareMemberHeadUrl;

	/**
	 * 分享社员头像PATH
	 */
	private String shareMemberHeadPath;

	/**
	 * 所在笔记本ID
	 */
	private int noteBookId;

	/**
	 * 所在笔记本名称
	 */
	private String noteBookName;

	/**
	 * 笔记地理位置
	 */
	private String noteLocation;
	/**
	 * 更新地点
	 */
	private String updateAddress;

	/**
	 * 分享交流圈数量
	 */
	private int shareGroupNum;

	public int getShareGroupNum() {
		return shareGroupNum;
	}

	public void setShareGroupNum(int shareGroupNum) {
		this.shareGroupNum = shareGroupNum;
	}

	public String getUpdateAddress() {
		return updateAddress;
	}

	public void setUpdateAddress(String updateAddress) {
		this.updateAddress = updateAddress;
	}

	public String getShareMemberName() {
		return shareMemberName;
	}

	public void setShareMemberName(String shareMemberName) {
		this.shareMemberName = shareMemberName;
	}

	public String getShareMemberHeadUrl() {
		return shareMemberHeadUrl;
	}

	public void setShareMemberHeadUrl(String shareMemberHeadUrl) {
		this.shareMemberHeadUrl = shareMemberHeadUrl;
	}

	public String getShareMemberHeadPath() {
		return shareMemberHeadPath;
	}

	public void setShareMemberHeadPath(String shareMemberHeadPath) {
		this.shareMemberHeadPath = shareMemberHeadPath;
	}

	public String getFirstPhotoUrl() {
		return firstPhotoUrl;
	}

	public void setFirstPhotoUrl(String firstPhotoUrl) {
		this.firstPhotoUrl = firstPhotoUrl;
	}

	public String getFirstPhotoPath() {
		if (firstPhotoPath == null)
			firstPhotoPath = "";
		return firstPhotoPath;
	}

	public void setFirstPhotoPath(String firstPhotoPath) {
		this.firstPhotoPath = firstPhotoPath;
	}

	// public String getFirstPhotoLocal() {
	// return firstPhotoLocal;
	// }
	//
	// public void setFirstPhotoLocal(String firstPhotoLocal) {
	// this.firstPhotoLocal = firstPhotoLocal;
	// }

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getShareRmark() {
		return shareRmark;
	}

	public void setShareRmark(String shareRmark) {
		this.shareRmark = shareRmark;
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public int getOwnerMemberId() {
		return ownerMemberId;
	}

	public void setOwnerMemberId(int ownerMemberId) {
		this.ownerMemberId = ownerMemberId;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public String getNoteContent() {
		if (noteContent == null)
			noteContent = "";
		return noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	public int getShareMemberId() {
		return shareMemberId;
	}

	public void setShareMemberId(int shareMemberId) {
		this.shareMemberId = shareMemberId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public int getIsStarTarget() {
		return isStarTarget;
	}

	public void setIsStarTarget(int isStarTarget) {
		this.isStarTarget = isStarTarget;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public int getAdjunctNum() {
		return adjunctNum;
	}

	public void setAdjunctNum(int adjunctNum) {
		this.adjunctNum = adjunctNum;
	}

	public List<AttachmentBean> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<AttachmentBean> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	// public String getCreateIp() {
	// return createIp;
	// }
	//
	// public void setCreateIp(String createIp) {
	// this.createIp = createIp;
	// }
	//
	// public String getCreateWay() {
	// return createWay;
	// }
	//
	// public void setCreateWay(String createWay) {
	// this.createWay = createWay;
	// }

	public int getNotebookId() {
		return notebookId;
	}

	public void setNotebookId(int notebookId) {
		this.notebookId = notebookId;
	}

	public String getNotePwd() {
		return notePwd;
	}

	public void setNotePwd(String notePwd) {
		this.notePwd = notePwd;
	}

	public String getNoteSource() {
		return noteSource;
	}

	public void setNoteSource(String noteSource) {
		this.noteSource = noteSource;
	}

	public int getShareMemberNum() {
		return shareMemberNum;
	}

	public void setShareMemberNum(int shareMemberNum) {
		this.shareMemberNum = shareMemberNum;
	}

	public int getNoteBookId() {
		return noteBookId;
	}

	public void setNoteBookId(int noteBookId) {
		this.noteBookId = noteBookId;
	}

	public String getNoteBookName() {
		return noteBookName;
	}

	public void setNoteBookName(String noteBookName) {
		this.noteBookName = noteBookName;
	}

	public String getNoteLocation() {
		return noteLocation;
	}

	public void setNoteLocation(String noteLocation) {
		this.noteLocation = noteLocation;
	}

}

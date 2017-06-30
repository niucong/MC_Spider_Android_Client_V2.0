package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

/**
 * 记事本bean
 * 
 * @author cailikun
 * @date 2011-7-12 下午05:16:18
 * @version v1.0.0
 */
public class DiaryInfoBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3034817031169514171L;

	/**
	 * 记事本表主键
	 */
	private int diaryId;

	/**
	 * 记事本所有者编号
	 */
	private int memberId;

	/**
	 * 记事标题
	 */
	private String title;

	/**
	 * 记事内容
	 */
	private String content;

	/**
	 * 记事时间
	 */
	private String createTime;

	/**
	 * 编辑时间
	 */
	private String editTime;
	/**
	 * 来源
	 */
	private String source;

	/**
	 * ua
	 */
	private String ua;

	/**
	 * ip
	 */
	private String ip;
	/**
	 * 笔记本附言
	 */
	private String diaryRemark;

	/**
	 * 上传方式
	 */
	private String way;
	/**
	 * 是否已读
	 */
	private int isRead;
	/**
	 * 分享次数
	 */
	private int shareAllNum;
	/**
	 * 分享人数
	 */
	private int shareMemberNum;
	/**
	 * 笔记本类型
	 */
	private int diaryType;
	/**
	 * 分享的社员编号
	 */
	private int shareMemberId;
	/**
	 * 分享的社员名字
	 */
	private String shareMemberName;
	/**
	 * 来源笔记本编号
	 */
	private int sourceDiaryId;

	public String getDiaryRemark() {
		return diaryRemark;
	}

	public void setDiaryRemark(String diaryRemark) {
		this.diaryRemark = diaryRemark;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getShareAllNum() {
		return shareAllNum;
	}

	public void setShareAllNum(int shareAllNum) {
		this.shareAllNum = shareAllNum;
	}

	public int getShareMemberNum() {
		return shareMemberNum;
	}

	public void setShareMemberNum(int shareMemberNum) {
		this.shareMemberNum = shareMemberNum;
	}

	public int getDiaryType() {
		return diaryType;
	}

	public void setDiaryType(int diaryType) {
		this.diaryType = diaryType;
	}

	public int getShareMemberId() {
		return shareMemberId;
	}

	public void setShareMemberId(int shareMemberId) {
		this.shareMemberId = shareMemberId;
	}

	public String getShareMemberName() {
		if(null == shareMemberName || "null".equals(shareMemberName)){
			return "";
		}
		return shareMemberName;
	}

	public void setShareMemberName(String shareMemberName) {
		this.shareMemberName = shareMemberName;
	}

	public int getSourceDiaryId() {
		return sourceDiaryId;
	}

	public void setSourceDiaryId(int sourceDiaryId) {
		this.sourceDiaryId = sourceDiaryId;
	}

	public int getDiaryId() {
		return diaryId;
	}

	public void setDiaryId(int diaryId) {
		this.diaryId = diaryId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
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

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
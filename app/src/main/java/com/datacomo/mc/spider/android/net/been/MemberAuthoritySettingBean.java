package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberAuthoritySettingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 查看我发布的内容是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(
	 * 可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewSendContentSet;

	/**
	 * 查看标记了我的照片/视频是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见
	 * (可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewMarkPhotoVideoSet;

	/**
	 * 查看我的动态墙上的留言是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见
	 * (可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewGuestbookSet;

	/**
	 * 查看我的家人列表是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(
	 * 可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewFamilyListSet;

	/**
	 * 查看我的情感状况是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(
	 * 可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewEmotionSet;

	/**
	 * 查看我的生日是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(可指定不可见的朋友
	 * )；6：指定朋友可见；7：自己可见)
	 */
	private int viewBirthdaySet;

	/**
	 * 查看我的兴趣对象是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(
	 * 可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewInterestObjectSet;

	/**
	 * 社员自我介绍是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(可指定不可见的朋友
	 * )；6：指定朋友可见；7：自己可见)
	 */
	private int viewMemberIntroductionSet;

	/**
	 * 查看我的手机号是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(可指定不可见的朋友
	 * )；6：指定朋友可见；7：自己可见)
	 */
	private int viewMemberPhoneSet;

	/**
	 * 查看我的即时通讯账号是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见
	 * (可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewCommunicationSet;

	/**
	 * 查看我的电子邮箱是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(
	 * 可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewMemberMailSet;

	/**
	 * 查看我的登录地点是否显示
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(
	 * 可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewLastLoadAddressSet;

	/**
	 * 是否可以评论我发布的内容
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(
	 * 可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int commentSendContentSet;

	/**
	 * 是否可以在我的动态留言
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(可指定不可见的朋友
	 * )；6：指定朋友可见；7：自己可见)
	 */
	private int guestbookSet;

	/**
	 * 设置是否可以在公社中搜索你
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见
	 * (可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int searchSet;

	/**
	 * 向您发送朋友请求
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(可指定不可见的朋友
	 * )；6：指定朋友可见；7：自己可见)
	 */
	private int sendApplyFriendSet;

	/**
	 * 设置谁可以向你发送站内短信
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见
	 * (可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int sendMessageSet;

	/**
	 * 谁可以查看你的朋友列表
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见(可指定不可见的朋友
	 * )；6：指定朋友可见；7：自己可见)
	 */
	private int viewFriendListSet;

	/**
	 * 设置谁可以查看你的学历和工作信息
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见
	 * (可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewSchoolCompanySet;

	/**
	 * 设置谁可以查看你的居住地和出生地
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见
	 * (可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewResidenceHometownSet;

	/**
	 * 设置谁可以查看你的兴趣爱好
	 * (1：所有社员可见；2：朋友的朋友可见；3：朋友可见；4：自定义朋友的朋友可见(可指定不可见的朋友)；5：自定义朋友可见
	 * (可指定不可见的朋友)；6：指定朋友可见；7：自己可见)
	 */
	private int viewHobbySet;

	public int getViewSendContentSet() {
		return viewSendContentSet;
	}

	public void setViewSendContentSet(int viewSendContentSet) {
		this.viewSendContentSet = viewSendContentSet;
	}

	public int getViewMarkPhotoVideoSet() {
		return viewMarkPhotoVideoSet;
	}

	public void setViewMarkPhotoVideoSet(int viewMarkPhotoVideoSet) {
		this.viewMarkPhotoVideoSet = viewMarkPhotoVideoSet;
	}

	public int getViewGuestbookSet() {
		return viewGuestbookSet;
	}

	public void setViewGuestbookSet(int viewGuestbookSet) {
		this.viewGuestbookSet = viewGuestbookSet;
	}

	public int getViewFamilyListSet() {
		return viewFamilyListSet;
	}

	public void setViewFamilyListSet(int viewFamilyListSet) {
		this.viewFamilyListSet = viewFamilyListSet;
	}

	public int getViewEmotionSet() {
		return viewEmotionSet;
	}

	public void setViewEmotionSet(int viewEmotionSet) {
		this.viewEmotionSet = viewEmotionSet;
	}

	public int getViewBirthdaySet() {
		return viewBirthdaySet;
	}

	public void setViewBirthdaySet(int viewBirthdaySet) {
		this.viewBirthdaySet = viewBirthdaySet;
	}

	public int getViewInterestObjectSet() {
		return viewInterestObjectSet;
	}

	public void setViewInterestObjectSet(int viewInterestObjectSet) {
		this.viewInterestObjectSet = viewInterestObjectSet;
	}

	public int getViewMemberIntroductionSet() {
		return viewMemberIntroductionSet;
	}

	public void setViewMemberIntroductionSet(int viewMemberIntroductionSet) {
		this.viewMemberIntroductionSet = viewMemberIntroductionSet;
	}

	public int getViewMemberPhoneSet() {
		return viewMemberPhoneSet;
	}

	public void setViewMemberPhoneSet(int viewMemberPhoneSet) {
		this.viewMemberPhoneSet = viewMemberPhoneSet;
	}

	public int getViewCommunicationSet() {
		return viewCommunicationSet;
	}

	public void setViewCommunicationSet(int viewCommunicationSet) {
		this.viewCommunicationSet = viewCommunicationSet;
	}

	public int getViewMemberMailSet() {
		return viewMemberMailSet;
	}

	public void setViewMemberMailSet(int viewMemberMailSet) {
		this.viewMemberMailSet = viewMemberMailSet;
	}

	public int getViewLastLoadAddressSet() {
		return viewLastLoadAddressSet;
	}

	public void setViewLastLoadAddressSet(int viewLastLoadAddressSet) {
		this.viewLastLoadAddressSet = viewLastLoadAddressSet;
	}

	public int getCommentSendContentSet() {
		return commentSendContentSet;
	}

	public void setCommentSendContentSet(int commentSendContentSet) {
		this.commentSendContentSet = commentSendContentSet;
	}

	public int getGuestbookSet() {
		return guestbookSet;
	}

	public void setGuestbookSet(int guestbookSet) {
		this.guestbookSet = guestbookSet;
	}

	public int getSearchSet() {
		return searchSet;
	}

	public void setSearchSet(int searchSet) {
		this.searchSet = searchSet;
	}

	public int getSendApplyFriendSet() {
		return sendApplyFriendSet;
	}

	public void setSendApplyFriendSet(int sendApplyFriendSet) {
		this.sendApplyFriendSet = sendApplyFriendSet;
	}

	public int getSendMessageSet() {
		return sendMessageSet;
	}

	public void setSendMessageSet(int sendMessageSet) {
		this.sendMessageSet = sendMessageSet;
	}

	public int getViewFriendListSet() {
		return viewFriendListSet;
	}

	public void setViewFriendListSet(int viewFriendListSet) {
		this.viewFriendListSet = viewFriendListSet;
	}

	public int getViewSchoolCompanySet() {
		return viewSchoolCompanySet;
	}

	public void setViewSchoolCompanySet(int viewSchoolCompanySet) {
		this.viewSchoolCompanySet = viewSchoolCompanySet;
	}

	public int getViewResidenceHometownSet() {
		return viewResidenceHometownSet;
	}

	public void setViewResidenceHometownSet(int viewResidenceHometownSet) {
		this.viewResidenceHometownSet = viewResidenceHometownSet;
	}

	public int getViewHobbySet() {
		return viewHobbySet;
	}

	public void setViewHobbySet(int viewHobbySet) {
		this.viewHobbySet = viewHobbySet;
	}

}

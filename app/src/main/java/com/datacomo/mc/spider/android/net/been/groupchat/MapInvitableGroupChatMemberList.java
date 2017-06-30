package com.datacomo.mc.spider.android.net.been.groupchat;

import java.util.List;

public class MapInvitableGroupChatMemberList {

	// 获取可以邀请加入圈聊的成员列表
	private List<MemberSimpleBean> INVITABLEGROUPCHATMEMBERLIST;
	// 根据手机号或者名字（精确或者拼音模糊）搜索可以邀请加入圈聊的成员列表
	private List<MemberSimpleBean> INVITABLE_GROUPCHAT_MEMBER;

	public List<MemberSimpleBean> getINVITABLEGROUPCHATMEMBERLIST() {
		return INVITABLEGROUPCHATMEMBERLIST;
	}

	public void setINVITABLEGROUPCHATMEMBERLIST(
			List<MemberSimpleBean> iNVITABLEGROUPCHATMEMBERLIST) {
		INVITABLEGROUPCHATMEMBERLIST = iNVITABLEGROUPCHATMEMBERLIST;
	}

	public List<MemberSimpleBean> getINVITABLE_GROUPCHAT_MEMBER() {
		return INVITABLE_GROUPCHAT_MEMBER;
	}

	public void setINVITABLE_GROUPCHAT_MEMBER(
			List<MemberSimpleBean> iNVITABLE_GROUPCHAT_MEMBER) {
		INVITABLE_GROUPCHAT_MEMBER = iNVITABLE_GROUPCHAT_MEMBER;
	}

}

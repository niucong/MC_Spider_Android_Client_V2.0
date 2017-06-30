package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.datacomo.mc.spider.android.util.PinYin4JCn;

public class FriendSimpleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 朋友所有者编号
	 */
	// private int ownerMemberId;
	/**
	 * 朋友编号
	 */
	private int memberId;
	/**
	 * 朋友名字
	 */
	private String memberName;
	/**
	 * 朋友名字拼音
	 */
	private String memberNamePY;
	/**
	 * 朋友名字简拼
	 */
	private String memberNameJP;

	/**
	 * 朋友头像前缀
	 */
	private String memberHeadUrl;
	/**
	 * 朋友头像路径
	 */
	private String memberHeadPath;
	/**
	 * 性别：1：男 2：女
	 */
	private int memberSex;
	// private int sex;

	/**
	 * 朋友心情
	 */
	private String memberMood;
	/**
	 * 手机号码
	 */
	private String memberPhone;
	/**
	 * 朋友备注
	 */
	private String friendName;
	/**
	 * 朋友备注拼音
	 */
	private String friendNamePY;
	/**
	 * 朋友备注简拼
	 */
	private String friendNameJP;
	/**
	 * 朋友分组编号
	 */
	private String friendGroupId;

	/**
	 * 动作状态 1.插入或修改，2.删除
	 */
	// private int actionStatus;

	// public int getOwnerMemberId() {
	// return ownerMemberId;
	// }
	//
	// public void setOwnerMemberId(int ownerMemberId) {
	// this.ownerMemberId = ownerMemberId;
	// }

	public FriendSimpleBean() {

	}

	/**
	 * 是否最近访问
	 */
	private boolean isRecentCheck;

	public boolean isRecentCheck() {
		return isRecentCheck;
	}

	public void setRecentCheck(boolean isRecentCheck) {
		this.isRecentCheck = isRecentCheck;
	}

	// private boolean flag;
	//
	// public FriendSimpleBean(boolean flag) {
	// this.flag = flag;
	// }

	public FriendSimpleBean(int id, String name) {
		memberId = id;
		memberName = name;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberNamePY() {
		if (memberNamePY == null || "".equals(memberNamePY)) {
			if (memberName != null && !"".equals(memberName)) {
				try {
					memberNamePY = PinYin4JCn.convertPy("leaguer", memberName);
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {

				}
			}
		}
		if (memberNamePY == null || "".equals(memberNamePY))
			memberNamePY = "#";
		// if (!flag) {
		// if (memberNamePY != null && memberNamePY.contains(",")) {
		// memberNamePY = memberNamePY
		// .substring(memberNamePY.indexOf(",") + 1)
		// + ","
		// + memberNamePY.substring(0, memberNamePY.indexOf(","));
		// }
		// }
		return memberNamePY.toLowerCase();
	}

	public void setMemberNamePY(String memberNamePY) {
		this.memberNamePY = memberNamePY;
	}

	public String getMemberNameJP() {
		if (memberNameJP == null || "".equals(memberNameJP)) {
			if (memberName != null && !"".equals(memberName)) {
				try {
					memberNameJP = PinYin4JCn.convertJp("leaguer", memberName);
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {

				}
			}
		}
		if (memberNameJP == null || "".equals(memberNameJP))
			memberNameJP = "#";
		return memberNameJP.toLowerCase();
	}

	public void setMemberNameJP(String memberNameJP) {
		this.memberNameJP = memberNameJP;
	}

	public String getMemberHeadUrl() {
		return memberHeadUrl;
	}

	public void setMemberHeadUrl(String memberHeadUrl) {
		this.memberHeadUrl = memberHeadUrl;
	}

	public String getMemberHeadPath() {
		return memberHeadPath;
	}

	public void setMemberHeadPath(String memberHeadPath) {
		this.memberHeadPath = memberHeadPath;
	}

	public int getMemberSex() {
		return memberSex;
	}

	public void setMemberSex(int memberSex) {
		this.memberSex = memberSex;
	}

	public void setSex(int sex) {
		this.memberSex = sex;
	}

	public String getMemberMood() {
		return memberMood;
	}

	public void setMemberMood(String memberMood) {
		this.memberMood = memberMood;
	}

	public String getFriendName() {
		if (friendName == null || "".equals(memberName))
			friendName = memberName;
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getFriendNamePY() {
		if (friendNamePY == null || "".equals(friendNamePY)) {
			if (friendName != null && !"".equals(friendName)) {
				try {
					friendNamePY = PinYin4JCn.convertPy("leaguer", friendName);
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {

				}
			}
		}
		if (friendNamePY == null || "".equals(friendNamePY))
			friendNamePY = memberNamePY;
		if (friendNamePY == null || "".equals(friendNamePY))
			friendNamePY = "#";
		// if (friendNamePY != null && friendNamePY.contains(",")) {
		// friendNamePY = friendNamePY
		// .substring(friendNamePY.indexOf(",") + 1)
		// + ","
		// + friendNamePY.substring(0, friendNamePY.indexOf(","));
		// }
		return friendNamePY.toLowerCase();
	}

	public void setFriendNamePY(String friendNamePY) {
		this.friendNamePY = friendNamePY;
	}

	public String getFriendNameJP() {
		if (friendNameJP == null || "".equals(friendNameJP)) {
			if (friendName != null && !"".equals(friendName)) {
				try {
					friendNameJP = PinYin4JCn.convertJp("leaguer", friendName);
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {

				}
			}
		}
		if (friendNameJP == null || "".equals(friendNameJP))
			friendNameJP = memberNameJP;
		if (friendNameJP == null || "".equals(friendNameJP))
			friendNameJP = "#";
		return friendNameJP.toLowerCase();
	}

	public void setFriendNameJP(String friendNameJP) {
		this.friendNameJP = friendNameJP;
	}

	public String getFriendGroupId() {
		return friendGroupId;
	}

	public void setFriendGroupId(String friendGroupId) {
		this.friendGroupId = friendGroupId;
	}

	public static LinkedHashMap<String, FriendSimpleBean> listToMap(
			List<FriendSimpleBean> list) {
		LinkedHashMap<String, FriendSimpleBean> map = new LinkedHashMap<String, FriendSimpleBean>();
		for (FriendSimpleBean bean : list) {
			map.put("" + bean.getMemberId(), bean);
		}
		return map;
	}

	public static List<FriendSimpleBean> mapToArray(
			LinkedHashMap<String, FriendSimpleBean> datas) {
		List<FriendSimpleBean> list = new ArrayList<FriendSimpleBean>();
		for (FriendSimpleBean bean : datas.values()) {
			list.add(bean);
		}
		return list;
	}

}

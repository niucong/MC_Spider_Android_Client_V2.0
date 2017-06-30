package com.datacomo.mc.spider.android.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.util.PinYin4JCn;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

/**
 * 圈子信息
 * 
 * @author datacomo-160
 * 
 */
public class GroupEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;// 圈子id
	private String name;// 圈子名字
	private String num;// 圈子成员数
	private String owner;// 圈主
	// private String joinGroupStatus;//
	// 加入状态：GROUP_OWNER圈主、GROUP_MANAGER管理、GROUP_LEAGUER加入
	private boolean type;// 圈子类型：我管理的true、我加入的false

	/**
	 * 圈子私密类型 1 - 公开 2 - 私密 3 - 自定义
	 */
	private String openStatus;
	/**
	 * 圈子类型：1-兴趣圈 2-企业圈 3-开放主页 4-外部社区(对应数据库中的groupType字段)
	 */
	private int groupProperty;

	/**
	 * 圈子属性: 1 - 普通圈子 2 - 校园圈子 3 - 企业圈子 4 - 其他
	 */
	private int groupType;

	private boolean isTitle;
	private String head;
	/**
	 * 分享中选中状态 0为未选择，1为选择 仅在交流圈成员列表以及交流圈列表中使用
	 */
	private int isSelect;

	/**
	 * 圈子名字拼音(全拼)
	 */
	private String groupNamePy;

	/**
	 * 圈子名字简拼
	 */
	private String groupNameJp;
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

	/**
	 * 
	 * @param id
	 * @param name
	 * @param num
	 * @param owner
	 * @param type
	 *            圈子类型：我管理的true、我加入的false
	 * @param isTitle
	 */
	public GroupEntity(String id, String name, String head, String groupNamePy,
			String groupNameJp, String openStatus) {
		this.id = id;
		this.name = name;
		this.head = head;
		this.groupNamePy = groupNamePy;
		this.groupNameJp = groupNameJp;
		this.openStatus = openStatus;
	}

	public GroupEntity(String id, String name, String num, String owner,
			boolean type, boolean isTitle) {
		this.id = id;
		this.name = name;
		this.num = num;
		this.owner = owner;
		// this.joinGroupStatus = joinGroupStatus;
		this.type = type;
		this.isTitle = isTitle;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNum() {
		return num;
	}

	public String getOwner() {
		return owner;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	// public String getJoinGroupStatus() {
	// return joinGroupStatus;
	// }

	public int getGroupProperty() {
		return groupProperty;
	}

	public void setGroupProperty(int groupProperty) {
		this.groupProperty = groupProperty;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public boolean getType() {
		return type;
	}

	public int getGroupType() {
		return groupType;
	}

	public boolean getIsTitle() {
		return isTitle;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getHead() {
		return head;
	}

	public String getThumbnailHead() {
		return ThumbnailImageUrl.getThumbnailPostUrl(head,
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
	}

	public int isSelect() {
		return isSelect;
	}

	public void setSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	public String getFullHeadPath() {
		return head;
	}

	public String getGroupNamePy() {
		// if (groupNamePy == null || "".equals(groupNamePy)) {
		// if (name != null && !"".equals(name)) {
		// try {
		// groupNamePy = PinYin4JCn.convertPy("group", name);
		// } catch (Exception e) {
		// e.printStackTrace();
		// } catch (NoClassDefFoundError e) {
		//
		// }
		// }
		// }
		// if (groupNamePy == null || "".equals(groupNamePy))
		// groupNamePy = "#";
		return groupNamePy;
	}

	public void setGroupNamePy(String groupNamePy) {
		this.groupNamePy = groupNamePy;
	}

	public String getGroupNameJp() {
		if (groupNameJp == null || "".equals(groupNameJp)) {
			if (name != null && !"".equals(name)) {
				try {
					groupNameJp = PinYin4JCn.convertJp("group", name);
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {

				}
			}
		}
		if (groupNameJp == null || "".equals(groupNameJp))
			groupNameJp = "#";
		return groupNameJp;
	}

	public void setGroupNameJp(String groupNameJp) {
		this.groupNameJp = groupNameJp;
	}

	public static LinkedHashMap<String, GroupEntity> listToMap(
			List<GroupEntity> list) {
		LinkedHashMap<String, GroupEntity> map = new LinkedHashMap<String, GroupEntity>();
		for (GroupEntity bean : list) {
			map.put("" + bean.getId(), bean);
		}
		return map;
	}

	public static List<GroupEntity> mapToArray(
			LinkedHashMap<String, GroupEntity> datas) {
		List<GroupEntity> list = new ArrayList<GroupEntity>();
		for (GroupEntity bean : datas.values()) {
			list.add(bean);
		}
		return list;
	}
}

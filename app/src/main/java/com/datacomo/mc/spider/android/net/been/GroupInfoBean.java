package com.datacomo.mc.spider.android.net.been;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupInfoBean {

	/**
	 * 圈子背景
	 */
	private String GROUP_BG;
	/**
	 * 开放主页信息
	 */
	private String OPEN_PAGE;

	/**
	 * 是否存在开放主页(若不存在，是否有创建权限) 1:该圈子已经开通过开放主页 2:圈子是认证圈子并且是顶级圈子，操作者为管理员（可以创建开放主页）
	 * 3:圈子是认证圈子并且是顶级圈子，操作者不是管理员（不满足创建开放主页权限） 4:此圈子不满足认证并且是顶级圈子，（不能创建开放主页）
	 * 5:圈子不存在 6:该圈子的顶级圈子已经开通过开放主页 7:该圈子满足认证并且不是顶级圈子，（不能创建开放主页）
	 * 8:该圈子既不满足认证并且也不是顶级圈子，（不能创建开放主页）
	 */
	private int OPENPAGE_PERMISSION;

	/**
	 * 开放主页id
	 */
	private int OPEN_PAGE_ID;

	/**
	 * 圈子设置信息
	 */
	private GroupSetBean GROUP_SET;
	/**
	 * 圈子信息
	 */
	private GroupBean GROUP_INFO;

	public String getGROUP_BG() {
		return GROUP_BG;
	}

	public void setGROUP_BG(String gROUP_BG) {
		GROUP_BG = gROUP_BG;
	}

	public String getOPEN_PAGE() {
		return OPEN_PAGE;
	}

	public void setOPEN_PAGE(String oPEN_PAGE) {
		OPEN_PAGE = oPEN_PAGE;
		if (OPEN_PAGE != null && !"".equals(OPEN_PAGE)) {
			try {
				JSONObject json = new JSONObject(OPEN_PAGE);
				setOPENPAGEPERMMISSION(json.getInt("RESULT"));
				setOPEN_PAGE_ID(json.getInt("OPEN_PAGE_ID"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public int getOPENPAGEPERMMISSION() {
		return OPENPAGE_PERMISSION;
	}

	public void setOPENPAGEPERMMISSION(int rESULT) {
		OPENPAGE_PERMISSION = rESULT;
	}

	public int getOPEN_PAGE_ID() {
		return OPEN_PAGE_ID;
	}

	public void setOPEN_PAGE_ID(int oPEN_PAGE_ID) {
		OPEN_PAGE_ID = oPEN_PAGE_ID;
	}

	public GroupSetBean getGROUP_SET() {
		return GROUP_SET;
	}

	public void setGROUP_SET(GroupSetBean gROUP_SET) {
		GROUP_SET = gROUP_SET;
	}

	public GroupBean getGROUP_INFO() {
		return GROUP_INFO;
	}

	public void setGROUP_INFO(GroupBean gROUP_INFO) {
		GROUP_INFO = gROUP_INFO;
	}

}

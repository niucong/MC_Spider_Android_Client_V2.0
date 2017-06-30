package com.datacomo.mc.spider.android.bean;

import java.io.Serializable;
import java.util.HashMap;

public class ChooseGroupBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7078231708721203505L;

	/**
	 * dialog title
	 */
	private String mTitle;
	/**
	 * member name
	 */
	private String mName;
	/**
	 * member id
	 */
	private int mMemberId = 0;
	/**
	 * save chosen memberInfo;
	 */
	private HashMap<String, Object> mMap_ChosenGroup;

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getName() {
		if (mName == null) {
			mName = "";
		}
		return mName;
	}

	public void setName(String[] names, String phone) {
		if (null != names) {
			if (names.length == 1 && !"".equals(names[0])) {
				mName = names[0];
				if (names.length == 2 && !"".equals(names[1]))
					mName = mName + "(优优工作圈:" + names[1] + ")";
			}
		}
		if ("".equals(names) && null != phone && !"".equals(phone))
			mName = phone;
	}

	public int getMemberId() {
		return mMemberId;
	}

	public void setMemberId(int memberId) {
		mMemberId = memberId;
	}

	public HashMap<String, Object> getChosenGroupMap() {
		if (null == mMap_ChosenGroup) {
			mMap_ChosenGroup = new HashMap<String, Object>();
		}
		return mMap_ChosenGroup;
	}

	public void setChosenGroupMap(HashMap<String, Object> map_ChosenGroup) {
		mMap_ChosenGroup = map_ChosenGroup;
	}

}

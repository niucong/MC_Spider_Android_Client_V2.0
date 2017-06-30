package com.datacomo.mc.spider.android.bean;

public class ContactGroupEntity {
	private static final String TAG = "ContactGroupEntity";
	
	private String mGroupId;
	
	private String mGroupName;
	
	private String mAccount_Name;
	
	private String mAccount_Type;
	
	private boolean mIsDelete;
	
	public ContactGroupEntity(){}
	
	public ContactGroupEntity(String groupId,String groupName){
		mGroupId=groupId;
		mGroupName=groupName;
	}
	
	public String getGroupId() {
		return mGroupId;
	}

	public void setGroupId(String groupId) {
		this.mGroupId = groupId;
	}

	public String getGroupName() {
		return mGroupName;
	}

	public void setGroupName(String groupName) {
		this.mGroupName = groupName;
	}

	public String getmAccount_Name() {
		return mAccount_Name;
	}

	public void setmAccount_Name(String mAccount_Name) {
		this.mAccount_Name = mAccount_Name;
	}

	public String getmAccount_Type() {
		return mAccount_Type;
	}

	public void setmAccount_Type(String mAccount_Type) {
		this.mAccount_Type = mAccount_Type;
	}

	public String isDelete() {
		String deleteType="0";
		if (mIsDelete) {
			deleteType="1";
		}
		return deleteType;
	}

	public void setDelete(String deleteType) {
		if ("1".equals(deleteType)) 
			mIsDelete=true;
		else
			mIsDelete=false;
	}
	
	
}

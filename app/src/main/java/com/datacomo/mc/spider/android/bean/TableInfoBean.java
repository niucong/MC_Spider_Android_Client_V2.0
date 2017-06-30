package com.datacomo.mc.spider.android.bean;

import java.util.ArrayList;
import java.util.List;

import com.datacomo.mc.spider.android.net.been.FriendGroupBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceGreatBean;
import com.datacomo.mc.spider.android.net.been.ResourceVisitBean;
import com.datacomo.mc.spider.android.util.TypeUtil;

public class TableInfoBean {
	//访客
	private List<ResourceVisitBean> beans_ResourceVisit;
	//赞
	private List<ResourceGreatBean> beans_ResourceGreat;
	//图片
	private List<ObjectInfoBean> beans_ObjectInfo;
	//朋友圈
	private List<FriendGroupBean> beans_FriendGroup;

	public TableInfoBean(String type) {
		if (TypeUtil.type_Visit.equals(type)) {
			beans_ResourceVisit = new ArrayList<ResourceVisitBean>();
		} else if (TypeUtil.type_Praise.equals(type)) {
			beans_ResourceGreat = new ArrayList<ResourceGreatBean>();
		} else if (TypeUtil.type_Photo.equals(type)) {
			beans_ObjectInfo = new ArrayList<ObjectInfoBean>();
		}else if (TypeUtil.type_Friend.equals(type)) {
			beans_FriendGroup = new ArrayList<FriendGroupBean>();
		}
	}

	public List<ResourceVisitBean> getBeans_ResourceVisit() {
		return beans_ResourceVisit;
	}



	public void setBeans_ResourceVisit(List<ResourceVisitBean> beans_ResourceVisit) {
		this.beans_ResourceVisit = beans_ResourceVisit;
	}



	public List<ResourceGreatBean> getBeans_ResourceGreat() {
		return beans_ResourceGreat;
	}



	public void setBeans_ResourceGreat(List<ResourceGreatBean> beans_ResourceGreat) {
		this.beans_ResourceGreat = beans_ResourceGreat;
	}



	public List<ObjectInfoBean> getBeans_ObjectInfo() {
		return beans_ObjectInfo;
	}

	public void setBeans_ObjectInfo(ObjectInfoBean bean_ObjectInfo) {
		this.beans_ObjectInfo.add(bean_ObjectInfo);
	}

	public List<FriendGroupBean> getBeans_FriendGroup() {
		return beans_FriendGroup;
	}

	public void setBeans_FriendGroup(FriendGroupBean bean_FriendGroup) {
		this.beans_FriendGroup.add(bean_FriendGroup);
	}
	
}

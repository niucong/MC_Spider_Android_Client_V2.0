package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;
import java.util.List;

public class ResultMessageBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7782057552695082900L;
	/**
	 * indexname所有值： group_topic_info,//话题 group_topic_comment_info,//话题评论
	 * group_file_info,//文件 group_file_comment_info,//文件评论 group_photo_info,//照片
	 * group_photo_comment_info,//照片评论 notepad_diary_info,//个人云笔记
	 * member_file_info,//个人文件 ms_message_info,//个人私信 member_guestbook_info,//留言
	 * member_guestbook_comment_info//留言评论
	 */
	private String indexname = "";//
	private String id = "";//
	private String parentid = "";//
	private String userid = "";//
	private String groupid = "";//
	private String grouptype = "";//
	private String title = "";//
	private String content = "";//
	private String type = "";//
	private String filepath = "";//
	private String date = "";//
	private String hot = "";//
	private String tag = "";//
	private String hittype = "";//
	private String openstatus = "";//
	private String groupname = "";
	private String description = "";//
	private String createway = "";//
	private String otheruserid = "";
	private String datastatus = "";//
	private MembercacheGPInfo MGPInfo = null;
	private MembercacheMInfo MMInfo = null;
	private MembercacheMInfo OMMInfo = null;// otheruserid返回的信息
	private List<ResultMessageBean> childList = null;//
	private String longitude;// 经度 GPS正值代表东，负值代表西//
	private String latitude;// 维度 GPS正值代表北，负值代表南//

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getIndexname() {
		return indexname;
	}

	public void setIndexname(String indexname) {
		this.indexname = indexname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGrouptype() {
		return grouptype;
	}

	public void setGrouptype(String grouptype) {
		this.grouptype = grouptype;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHot() {
		return hot;
	}

	public void setHot(String hot) {
		this.hot = hot;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getHittype() {
		return hittype;
	}

	public void setHittype(String hittype) {
		this.hittype = hittype;
	}

	public String getOpenstatus() {
		return openstatus;
	}

	public void setOpenstatus(String openstatus) {
		this.openstatus = openstatus;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateway() {
		return createway;
	}

	public void setCreateway(String createway) {
		this.createway = createway;
	}

	public String getOtheruserid() {
		return otheruserid;
	}

	public void setOtheruserid(String otheruserid) {
		this.otheruserid = otheruserid;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public MembercacheGPInfo getMGPInfo() {
		return MGPInfo;
	}

	public void setMGPInfo(MembercacheGPInfo mGPInfo) {
		this.MGPInfo = mGPInfo;
	}

	public MembercacheMInfo getMMInfo() {
		return MMInfo;
	}

	public void setMMInfo(MembercacheMInfo mMInfo) {
		this.MMInfo = mMInfo;
	}

	public MembercacheMInfo getOMMInfo() {
		return OMMInfo;
	}

	public void setOMMInfo(MembercacheMInfo oMMInfo) {
		this.OMMInfo = oMMInfo;
	}

	public List<ResultMessageBean> getChildList() {
		return childList;
	}

	public void setChildList(List<ResultMessageBean> childList) {
		this.childList = childList;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}

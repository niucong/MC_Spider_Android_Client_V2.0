package com.datacomo.mc.spider.android.bean;

import java.io.Serializable;

public class CommentSendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8105614113257083146L;
	private long time;
	private String session_key;
	private int id;
	private String name;
	private String head;
	private int gid;
	private String gname;
	// /**
	// * 评论类型：0、圈子资源，1、心情，2、留言
	// */
	// private int cType;
	/**
	 * 资源类型 OBJ_SEND_QUUBO发圈博、
	 * OBJ_MEMBER_RES_MOOD评论心情、OBJ_MEMBER_RES_LEAVEMESSAGE评论个人留言、
	 * OBJ_OPEN_PAGE_LEAVEMESSAGE评论开放主页留言、评论圈博
	 */
	private String mType;
	private int rid;
	private String rname;
	private String content;
	/**
	 * @某些人ids:id0#id1#id2
	 */
	private String at;

	private String atidname;// id#name&id#name

	private String gidname;// id#name&id#name
	private String filePaths;// [{"path":"","type":"0"},{"path":"","type":"1"},{"path":"","type":"2"}]0:图片，1:文件,2:云文件
	private String fileTemps;//

	/**
	 * 圈博标题、内容
	 */
	private String title;

	/**
	 * 评论状态：0、成功，-1、评论中，-2、失败
	 */
	private int sendStatus;

	public String getGidname() {
		return gidname;
	}

	public void setGidname(String gidname) {
		this.gidname = gidname;
	}

	public String getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(String filePaths) {
		this.filePaths = filePaths;
	}

	public String getFileTemps() {
		return fileTemps;
	}

	public void setFileTemps(String fileTemps) {
		this.fileTemps = fileTemps;
	}

	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getAtidname() {
		return atidname;
	}

	public void setAtidname(String atidname) {
		this.atidname = atidname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	// public int getcType() {
	// return cType;
	// }
	//
	// public void setcType(int cType) {
	// this.cType = cType;
	// }

	public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

}

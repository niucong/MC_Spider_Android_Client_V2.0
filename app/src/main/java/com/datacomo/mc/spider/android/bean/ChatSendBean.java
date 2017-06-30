package com.datacomo.mc.spider.android.bean;

public class ChatSendBean {

	/**
	 * 发送时间（删除时唯一键值）
	 */
	private long time;

	/**
	 * 发送者session_key
	 */
	private String session_key;

	/**
	 * 接收者Id
	 */
	private int id;

	/**
	 * 接收者名字
	 */
	private String name;

	/**
	 * 接收者头像
	 */
	private String head;

	/**
	 * 聊天类型：0、私信，1、圈聊
	 */
	private int cType;
	/**
	 * 消息类型：OBJ_TEXT-文本,OBJ_FILE-文件,OBJ_PHOTO-照片,OBJ_VOICE-语音
	 */
	private String mType;
	/**
	 * 消息内容或本地路径
	 */
	private String content;
	/**
	 * 音频或者视频此字段存放时长
	 */
	private long tLong;

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getcType() {
		return cType;
	}

	public void setcType(int cType) {
		this.cType = cType;
	}

	public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long gettLong() {
		return tLong;
	}

	public void settLong(long tLong) {
		this.tLong = tLong;
	}

}

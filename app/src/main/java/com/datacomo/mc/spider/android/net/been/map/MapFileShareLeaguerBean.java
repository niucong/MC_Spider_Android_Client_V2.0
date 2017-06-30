package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.FileShareLeaguerBean;

public class MapFileShareLeaguerBean {

	/**
	 * shareFilesByMember按人获取分享文件人列表
	 */
	private List<FileShareLeaguerBean> FILESHARELIST;
	/**
	 * fileShareMembers获取某个文件的分享社员列表
	 */
	private List<FileShareLeaguerBean> SHARE_LEAGUERS;

	/**
	 * shareFilesByMember
	 */
	private int LISTSIZE;
	/**
	 * fileShareMembers
	 */
	private int SHARE_LEAGUERS_NUM;

	public List<FileShareLeaguerBean> getSHARE_LEAGUERS() {
		return SHARE_LEAGUERS;
	}

	public void setSHARE_LEAGUERS(List<FileShareLeaguerBean> sHARE_LEAGUERS) {
		SHARE_LEAGUERS = sHARE_LEAGUERS;
	}

	public int getSHARE_LEAGUERS_NUM() {
		return SHARE_LEAGUERS_NUM;
	}

	public void setSHARE_LEAGUERS_NUM(int sHARE_LEAGUERS_NUM) {
		SHARE_LEAGUERS_NUM = sHARE_LEAGUERS_NUM;
	}

	public List<FileShareLeaguerBean> getFILESHARELIST() {
		return FILESHARELIST;
	}

	public void setFILESHARELIST(List<FileShareLeaguerBean> fILESHARELIST) {
		FILESHARELIST = fILESHARELIST;
	}

	public int getLISTSIZE() {
		return LISTSIZE;
	}

	public void setLISTSIZE(int lISTSIZE) {
		LISTSIZE = lISTSIZE;
	}

}

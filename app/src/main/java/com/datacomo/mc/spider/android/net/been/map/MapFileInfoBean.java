package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.FileInfoBean;

public class MapFileInfoBean {

	/**
	 * 
	 */
	private List<FileInfoBean> FILE_LIST;
	
	private List<FileInfoBean> RECORD_LIST;

	public List<FileInfoBean> getRECORD_LIST() {
		return RECORD_LIST;
	}

	public void setRECORD_LIST(List<FileInfoBean> rECORD_LIST) {
		RECORD_LIST = rECORD_LIST;
	}

	/**
	 * 
	 */
	private int TOTAL_NUM;

	public List<FileInfoBean> getFILE_LIST() {
		return FILE_LIST;
	}

	public void setFILE_LIST(List<FileInfoBean> fILE_LIST) {
		FILE_LIST = fILE_LIST;
	}

	public int getTOTAL_NUM() {
		return TOTAL_NUM;
	}

	public void setTOTAL_NUM(int tOTAL_NUM) {
		TOTAL_NUM = tOTAL_NUM;
	}

}

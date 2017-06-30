package com.datacomo.mc.spider.android.net.been;

import java.util.List;

public class ResultAll {

	/**
	 * 搜索结果
	 */
	private List<ResultMessageBean> rmList;

	/**
	 * 针对全局第一页搜索时searchRange=0 actionName=‘sa’，，存放朋友公开的留言等，
	 */
	private List<ResultMessageBean> gbList = null;

	/**
	 * 当前页面
	 */
	private int pageNo = 1;
	/**
	 * 搜索结果总数量
	 */
	private int countNum = 0;
	/**
	 * 分页大小
	 */
	private int pageSize = 10;
	/**
	 * 搜索使用时间
	 */
	private String useTime = "";
	/**
	 * 错误信息
	 */
	private String msg = "";

	public List<ResultMessageBean> getRmList() {
		return rmList;
	}

	public void setRmList(List<ResultMessageBean> rmList) {
		this.rmList = rmList;
	}

	public List<ResultMessageBean> getGbList() {
		return gbList;
	}

	public void setGbList(List<ResultMessageBean> gbList) {
		this.gbList = gbList;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getCountNum() {
		return countNum;
	}

	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}

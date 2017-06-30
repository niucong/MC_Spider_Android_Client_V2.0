package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 搜索资源
 * 
 * @author datacomo-160
 * 
 */
public class SearchResourceParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param keyword
	 * @param GroupID
	 * @param pageNo
	 * @param pageSize
	 * @param action
	 * @param searchRange
	 */
	public SearchResourceParams(Context context, String keyword,
			String GroupID, int pageNo, int pageSize, String action,
			String searchRange,String MemberID) {
		super(context);
		setVariable(keyword, GroupID, pageNo, pageSize, action, searchRange, MemberID);
	}

	/**
	 * 设置参数
	 * 
	 * @param keyword
	 * @param GroupID
	 * @param pageNo
	 * @param pageSize
	 * @param action
	 * @param searchRange
	 */
	private void setVariable(String keyword, String GroupID, int pageNo,
			int pageSize, String action, String searchRange,String MemberID) {
		paramsMap.put("keyword", keyword);
		if (GroupID != null && !GroupID.equals("")) {
			paramsMap.put("GroupID", GroupID);
		}
		
		if (MemberID != null && !MemberID.equals("")) {
			paramsMap.put("MemberID", MemberID);
		}
		// 功能名称 对应搜索引擎配置功能名称 必须
		paramsMap.put("action", action);
		// 搜索范围 必选值（0/1/2/3） 0 全局搜索、1 局部搜索、2 个人搜索、3 朋友公开信息 全局：(开放的圈子) OR (圈子ID)
		// OR (个人ID) 局部：(开放的圈子/圈子ID) and (个人ID) 个人：(个人ID)
		paramsMap.put("searchRange", searchRange);
		paramsMap.put("pageNo", pageNo + "");
		paramsMap.put("pageSize", pageSize + "");
		paramsMap.put("method", "search");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}

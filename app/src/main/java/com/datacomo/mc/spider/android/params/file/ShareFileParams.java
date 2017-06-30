package com.datacomo.mc.spider.android.params.file;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 云文件的分享到朋友
 * 
 * @author datacomo-287
 *
 */
public class ShareFileParams extends BasicParams {

	/**
	 * 设置参数
	 * 
	 * @param context
	 * @param fileId
	 * @param receiveMemberIds
	 */
	public ShareFileParams(Context context,int fileId,String[] receiveMemberIds,String shareWord) {
		super(context);
		setVariable(fileId, receiveMemberIds,shareWord);
	}

	/**
	 * 设置参数
	 * 
	 * @param fileId
	 * @param receiveMemberIds
	 */
	private void setVariable(int fileId,String[] receiveMemberIds,String shareWord){
		paramsMap.put("fileId", String.valueOf(fileId));
		mHashMap = new HashMap<String, String[]>();
		if (receiveMemberIds != null && receiveMemberIds.length > 0) {
			mHashMap.put("memberIds", receiveMemberIds);
			paramsMap.put("memberIds", "");
		}
		if (null!=shareWord&&!"".equals(shareWord)) {
			paramsMap.put("shareWord", shareWord);
		}
		paramsMap.put("method", "shareFileToMembers");

		super.setVariable(true);
	}
	
	@Override
	public String getParams() {
		return strParams;
	}

}

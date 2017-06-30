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
public class ShareFileToGroupParams extends BasicParams {

	/**
	 * 设置参数
	 * 
	 * @param context
	 * @param fileId
	 * @param receiveMemberIds
	 */
	public ShareFileToGroupParams(Context context,int fileId,String[] receiveGroupIds,String shareWord) {
		super(context);
		setVariable(fileId, receiveGroupIds,shareWord);
	}

	/**
	 * 设置参数
	 * 
	 * @param fileId
	 * @param receiveMemberIds
	 */
	private void setVariable(int fileId,String[] receiveGroupIds,String shareWord){
		paramsMap.put("fileId", String.valueOf(fileId));
		mHashMap = new HashMap<String, String[]>();
		if (receiveGroupIds != null && receiveGroupIds.length > 0) {
			mHashMap.put("groupIds", receiveGroupIds);
			paramsMap.put("groupIds", "");
		}
		if (null!=shareWord&&!"".equals(shareWord)) {
			paramsMap.put("shareWord", shareWord);
		}
		paramsMap.put("method", "shareMemberFileToGroup");

		super.setVariable(true);
	}
	
	@Override
	public String getParams() {
		return strParams;
	}

}

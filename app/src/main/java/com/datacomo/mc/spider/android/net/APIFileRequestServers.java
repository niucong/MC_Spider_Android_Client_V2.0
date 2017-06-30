package com.datacomo.mc.spider.android.net;

import android.content.Context;

import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.enums.FileListTypeEnum;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapFileInfoBean;
import com.datacomo.mc.spider.android.net.been.map.MapFileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapShareLeaguerRecord;
import com.datacomo.mc.spider.android.params.file.FileDeleteParams;
import com.datacomo.mc.spider.android.params.file.FileEditParams;
import com.datacomo.mc.spider.android.params.file.FileInfoParams;
import com.datacomo.mc.spider.android.params.file.FileListsParam;
import com.datacomo.mc.spider.android.params.file.FilePathParams;
import com.datacomo.mc.spider.android.params.file.FileShareMembersParam;
import com.datacomo.mc.spider.android.params.file.SearchAllFileListParam;
import com.datacomo.mc.spider.android.params.file.SearchShareRelationMembersParam;
import com.datacomo.mc.spider.android.params.file.ShareFileParams;
import com.datacomo.mc.spider.android.params.file.ShareFileToGroupParams;
import com.datacomo.mc.spider.android.params.file.ShareFilesByMemberIdParam;
import com.datacomo.mc.spider.android.params.file.ShareFilesByMemberParam;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.StreamUtil;

public class APIFileRequestServers {
	private static final String TAG = "APIRequestServers";

	/**
	 * get file resource
	 * 
	 * @param context
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult getFileList(Context context,
			FileListTypeEnum fileListType, int startRecord, int maxResult,
			boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new FileListsParam(context, fileListType, startRecord,
				maxResult, noPaging).getParams();
		L.i(TAG, "getFileList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getFileList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String content = mcResult.getResult().toString();
		MapFileInfoBean mapBean = (MapFileInfoBean) JsonParseTool
				.dealComplexResult(content, MapFileInfoBean.class);
		L.i(TAG, "getFileList mapBean.TOTALNUM=" + mapBean.getTOTAL_NUM());
		mcResult.setResult(mapBean);
		if (startRecord == 0 && mapBean != null) {
			if (FileListTypeEnum.ALL_FILE == fileListType) {
				LocalDataService.getInstense().save(context,
						LocalDataService.TXT_FILE, content);
			} else if (FileListTypeEnum.MY_FILE == fileListType) {
				LocalDataService.getInstense().save(context,
						LocalDataService.TXT_FILE_MY, content);
			}
		}
		return mcResult;
	}

	/**
	 * get file info
	 * 
	 * @param context
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public static MCResult fileInfo(Context context, int fileId)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new FileInfoParams(context, fileId).getParams();
		L.i(TAG, "fileInfo url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "fileInfo", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		Object object = mcResult.getResult();
		if (object != null) {
			FileInfoBean infoBean = (FileInfoBean) JsonParseTool
					.dealComplexResult(mcResult.getResult().toString(),
							FileInfoBean.class);
			mcResult.setResult(infoBean);
		}
		return mcResult;
	}

	/**
	 * edit file info
	 * 
	 * @param context
	 * @param fileId
	 * @param fileName
	 * @param fileDesc
	 * @return
	 * @throws Exception
	 */
	public static MCResult editFile(Context context, int fileId,
			String fileName, String fileDesc, String[] tags) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new FileEditParams(context, fileId, fileName, fileDesc,
				tags).getParams();
		L.i(TAG, "editFile url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "editFile", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		return mcResult;
	}

	/**
	 * delete file
	 * 
	 * @param context
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public static MCResult deleteFile(Context context, int fileId)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new FileDeleteParams(context, fileId).getParams();
		L.i(TAG, "deleteFile url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteFile", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		return mcResult;
	}

	/**
	 * share file to member
	 * 
	 * @param context
	 * @param fileId
	 * @param receiveMemberIds
	 * @param shareWord
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareFile(final Context context, int fileId,
			final String[] receiveMemberIds, String shareWord) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new ShareFileParams(context, fileId, receiveMemberIds,
				shareWord).getParams();
		L.i(TAG, "shareFile url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareFile", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		new Thread() {
			public void run() {
				FriendListService.getService(context).saveContactTime(
						receiveMemberIds);
			};
		}.start();
		return mcResult;
	}

	/**
	 * share file to group
	 * 
	 * @param context
	 * @param fileId
	 * @param receiveGroupIds
	 * @param shareWord
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareFileToGroup(final Context context, int fileId,
			final String[] receiveGroupIds, String shareWord) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new ShareFileToGroupParams(context, fileId,
				receiveGroupIds, shareWord).getParams();
		L.i(TAG, "shareFileToGroup url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareFileToGroup", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		new Thread() {
			public void run() {
				GroupListService.getService(context).saveContactTime(
						receiveGroupIds);
			};
		}.start();
		return mcResult;
	}

	/**
	 * 按人获取分享文件人列表
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareFilesByMember(Context context, int startRecord,
			int maxResult, boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new ShareFilesByMemberParam(context, startRecord,
				maxResult, noPaging).getParams();
		L.i(TAG, "shareFilesByMember url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareFilesByMember", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String content = mcResult.getResult().toString();
		MapFileShareLeaguerBean mapBean = (MapFileShareLeaguerBean) JsonParseTool
				.dealComplexResult(content, MapFileShareLeaguerBean.class);
		mcResult.setResult(mapBean);
		if (startRecord == 0 && mapBean != null) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_FILE_MEMBER, content);
		}
		return mcResult;
	}

	/**
	 * 获取我与某人分享的文件列表
	 * 
	 * @param context
	 * @param memberId
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareFilesByMemberId(Context context,
			String memberId, FileListTypeEnum fileListType, int startRecord,
			int maxResult, boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new ShareFilesByMemberIdParam(context, memberId,
				fileListType, startRecord, maxResult, noPaging).getParams();

		L.i(TAG, "shareFilesByMemberId url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareFilesByMemberId", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapShareLeaguerRecord mapBean = (MapShareLeaguerRecord) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapShareLeaguerRecord.class);
		L.i(TAG,
				"shareFilesByMemberId mapBean.TOTALNUM="
						+ mapBean.getTOTAL_NUM());
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 搜索文件列表
	 * 
	 * @param context
	 * @param searchContent
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchAllFileList(Context context,
			String searchContent, FileListTypeEnum fileListType,
			int startRecord, int maxResult, boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new SearchAllFileListParam(context, searchContent,
				fileListType, startRecord, maxResult, noPaging).getParams();

		L.i(TAG, "searchAllFileList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchAllFileList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapFileInfoBean mapBean = (MapFileInfoBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapFileInfoBean.class);
		L.i(TAG, "searchAllFileList mapBean.TOTALNUM=" + mapBean.getTOTAL_NUM());
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 根据社员名字搜索有文件分享关系的社员列表
	 * 
	 * @param context
	 * @param searchContent
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchShareRelationMembers(Context context,
			String searchContent, int startRecord, int maxResult,
			boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new SearchShareRelationMembersParam(context,
				searchContent, startRecord, maxResult, noPaging).getParams();

		L.i(TAG, "searchShareRelationMembers url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchShareRelationMembers", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapShareLeaguerBean mapBean = (MapShareLeaguerBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapShareLeaguerBean.class);
		L.i(TAG,
				"searchShareRelationMembers mapBean.TOTALNUM="
						+ mapBean.getTOTAL_NUM());
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 获取某个文件的分享社员列表
	 * 
	 * @param context
	 * @param fileId
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult fileShareMembers(Context context, String fileId,
			int startRecord, int maxResult, boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new FileShareMembersParam(context, fileId, startRecord,
				maxResult, noPaging).getParams();

		L.i(TAG, "fileShareMembers url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "fileShareMembers", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapFileShareLeaguerBean mapBean = (MapFileShareLeaguerBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapFileShareLeaguerBean.class);
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * get file path
	 * 
	 * @param context
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public static MCResult getFilePath(Context context, int fileId)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FILE_JSON;
		String params = new FilePathParams(context, fileId).getParams();
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getFilePath", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		return mcResult;
	}

}

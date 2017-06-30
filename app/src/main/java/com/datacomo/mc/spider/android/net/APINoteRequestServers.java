package com.datacomo.mc.spider.android.net;

import java.util.ArrayList;

import android.content.Context;

import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.db.NoteInfoService;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.note.MapNoteBookBean;
import com.datacomo.mc.spider.android.net.been.note.MapShareLeaguerInfoBean;
import com.datacomo.mc.spider.android.net.been.note.ShareGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.note.ShareLeaguerInfoBean;
import com.datacomo.mc.spider.android.params.note.AddOrDeleteStarMarkParams;
import com.datacomo.mc.spider.android.params.note.CloudNoteBookListParams;
import com.datacomo.mc.spider.android.params.note.CloudNoteListParams;
import com.datacomo.mc.spider.android.params.note.CreateCloudNoteBookParams;
import com.datacomo.mc.spider.android.params.note.CreateCloudNoteParams;
import com.datacomo.mc.spider.android.params.note.CreateDiaryParams;
import com.datacomo.mc.spider.android.params.note.DeleteCloudNoteParams;
import com.datacomo.mc.spider.android.params.note.DeleteNoteBookParams;
import com.datacomo.mc.spider.android.params.note.EditCloudNoteParams;
import com.datacomo.mc.spider.android.params.note.EditDiaryParams;
import com.datacomo.mc.spider.android.params.note.EditNoteBookParams;
import com.datacomo.mc.spider.android.params.note.MoveNoteToOtherNoteBookParams;
import com.datacomo.mc.spider.android.params.note.NoteInfoParams;
import com.datacomo.mc.spider.android.params.note.SaveToAppFileParams;
import com.datacomo.mc.spider.android.params.note.SearchCloudNoteBookListParams;
import com.datacomo.mc.spider.android.params.note.SearchCloudNoteParams;
import com.datacomo.mc.spider.android.params.note.SearchShareCloudNoteListParams;
import com.datacomo.mc.spider.android.params.note.SearchShareFriendListParams;
import com.datacomo.mc.spider.android.params.note.ShareCloudNoteListParams;
import com.datacomo.mc.spider.android.params.note.ShareDiaryToFriendParams;
import com.datacomo.mc.spider.android.params.note.ShareDiaryToGroupParams;
import com.datacomo.mc.spider.android.params.note.ShareFriendListParams;
import com.datacomo.mc.spider.android.params.note.ShareGroupListParams;
import com.datacomo.mc.spider.android.params.note.ShareMemberListParams;
import com.datacomo.mc.spider.android.params.note.UploadAttachmentFileParams;
import com.datacomo.mc.spider.android.params.note.UploadAttachmentImgeParams;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.StreamUtil;

public class APINoteRequestServers {
	private static final String TAG = "APIRequestServers";

	/**
	 * 获取云笔记列表
	 * 
	 * @param context
	 * @param noteType
	 *            1:自己创建的、2:社员分享、3:第三方同步过来的 如 “有道”等、4:所有的（创建+分享+第三方同步）、5:星标笔记
	 * @param isNoteBook
	 *            是否获取某个笔记本下的所有笔记 true：是（获取某个笔记本下的笔记）false：否（获取所有笔记）
	 * @param noteBookId
	 *            笔记本id 如果isNoteBook为true，则笔记本id不能为空，此查询就为查询某个笔记本下的所有笔记
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult cloudNoteList(final Context context,
			final String noteType, String isNoteBook, String noteBookId,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new CloudNoteListParams(context, noteType, isNoteBook,
				noteBookId, startRecord, maxResults).getParams();
		L.i(TAG, "cloudNoteList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "cloudNoteList", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean map = (com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean) JsonParseTool
				.dealComplexResult(
						trends,
						com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean.class);
		mcResult.setResult(map);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord)) && map.getALLNOTENUM() > 0) {
			if ("4".equals(noteType)) {
				LocalDataService.getInstense().save(context,
						LocalDataService.TXT_ALLDIARY, trends);
			} else if ("5".equals(noteType)) {
				LocalDataService.getInstense().save(context,
						LocalDataService.TXT_OTHERDIARY, trends);
			}
		}

		return mcResult;
	}

	/**
	 * 搜索云笔记
	 * 
	 * @param context
	 * @param noteType
	 *            1:自己创建的、2:社员分享、3:第三方同步过来的 如 “有道”等、4:所有的（创建+分享+第三方同步）、5:星标笔记
	 * @param isNoteBook
	 *            是否获取某个笔记本下的所有笔记 true：是（获取某个笔记本下的笔记）false：否（获取所有笔记）
	 * @param noteBookId
	 *            笔记本id 如果isNoteBook为true，则笔记本id不能为空，此查询就为查询某个笔记本下的所有笔记
	 * @param noteTitle
	 *            笔记标题 按笔记标题进行模糊搜索
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchCloudNote(final Context context,
			final String noteType, String isNoteBook, String noteBookId,
			String noteTitle, String startRecord, String maxResults)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new SearchCloudNoteParams(context, noteType,
				isNoteBook, noteBookId, noteTitle, startRecord, maxResults)
				.getParams();
		L.i(TAG, "searchCloudNote url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchCloudNote", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean map = (com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean) JsonParseTool
				.dealComplexResult(
						trends,
						com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean.class);
		mcResult.setResult(map);

		return mcResult;
	}

	/**
	 * 获取笔记本列表（不包括未放入笔记本的）
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult cloudNoteBookList(final Context context,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new CloudNoteBookListParams(context, "", startRecord,
				maxResults).getParams();
		L.i(TAG, "cloudNoteBookList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "cloudNoteBookList", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		MapNoteBookBean map = (MapNoteBookBean) JsonParseTool
				.dealComplexResult(trends, MapNoteBookBean.class);
		mcResult.setResult(map);
		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& map != null
				&& map.getNOTEBOOKNUM() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_MYBOOK, trends);
		}

		return mcResult;
	}

	/**
	 * 搜索笔记本（不包括未放入笔记本的）
	 * 
	 * @param context
	 * @param notebookName
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchCloudNoteBookList(final Context context,
			String notebookName, String startRecord, String maxResults)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new SearchCloudNoteBookListParams(context,
				notebookName, startRecord, maxResults).getParams();
		L.i(TAG, "searchCloudNoteBookList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchCloudNoteBookList", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		MapNoteBookBean map = (MapNoteBookBean) JsonParseTool
				.dealComplexResult(trends, MapNoteBookBean.class);
		mcResult.setResult(map);
		return mcResult;
	}

	/**
	 * 获取笔记分享社员列表
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareFriendList(final Context context,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new ShareFriendListParams(context, startRecord,
				maxResults).getParams();
		L.i(TAG, "shareFriendList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareFriendList", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				MapShareLeaguerInfoBean.class);
		mcResult.setResult(objectList);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& objectList != null
				&& objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_MYDIARY, trends);
		}

		return mcResult;
	}

	/**
	 * 搜索笔记分享社员列表
	 * 
	 * @param context
	 * @param content
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchShareFriendList(final Context context,
			String content, String startRecord, String maxResults)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new SearchShareFriendListParams(context, content,
				startRecord, maxResults).getParams();
		L.i(TAG, "searchShareFriendList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchShareFriendList", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				MapShareLeaguerInfoBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 获取我与某个人的分享笔记列表
	 * 
	 * @param context
	 * @param memberId
	 *            分享笔记联系人
	 * @param allNotes
	 *            是否是所有笔记 true 是，此时查询我与某一好友的所有来往笔记 false
	 *            否，此时根据需要传参，我分享出去的/别人分享给我的
	 * @param shareType
	 *            当allNotes为true时不起作用，该字段传0，我分享给别人的传1，别人分享给我的传2
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareCloudNoteList(final Context context,
			String memberId, String allNotes, String shareType,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new ShareCloudNoteListParams(context, memberId,
				allNotes, shareType, startRecord, maxResults).getParams();
		L.i(TAG, "shareCloudNoteList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareCloudNoteList", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean map = (com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean) JsonParseTool
				.dealComplexResult(
						trends,
						com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean.class);
		mcResult.setResult(map);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord)) && map.getALLNOTENUM() > 0) {
			// TODO
		}

		return mcResult;
	}

	/**
	 * 搜索我与某个人的分享笔记列表
	 * 
	 * @param context
	 * @param memberId
	 *            分享笔记联系人
	 * @param content
	 *            搜索内容，根据笔记标题搜索
	 * @param allNotes
	 *            是否是所有笔记 true 是，此时查询我与某一好友的所有来往笔记 false
	 *            否，此时根据需要传参，我分享出去的/别人分享给我的
	 * @param shareType
	 *            当allNotes为true时不起作用，该字段传0，我分享给别人的传1，别人分享给我的传2
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchShareCloudNoteList(final Context context,
			String memberId, String content, String allNotes, String shareType,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new SearchShareCloudNoteListParams(context, memberId,
				content, allNotes, shareType, startRecord, maxResults)
				.getParams();
		L.i(TAG, "shareCloudNoteList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareCloudNoteList", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean map = (com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean) JsonParseTool
				.dealComplexResult(
						trends,
						com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean.class);
		mcResult.setResult(map);

		return mcResult;
	}

	/**
	 * 获取笔记详情
	 * 
	 * @param context
	 * @param groupId
	 * @param quuboId
	 * @return
	 * @throws Exception
	 */
	public static MCResult noteInfo(Context context, String noteId)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new NoteInfoParams(context, noteId).getParams();
		L.i(TAG, "noteInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "noteInfo", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		String content = mcResult.getResult().toString();
		// 解析mapNoteInfoBean
		com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean mapNoteInfoBean = (com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean) JsonParseTool
				.dealComplexResult(
						content,
						com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean.class);
		mcResult.setResult(mapNoteInfoBean);

		NoteInfoService.getService(context).save(Integer.valueOf(noteId),
				content);

		return mcResult;
	}

	/**
	 * 添加、取消星标
	 * 
	 * @param context
	 * @param noteId
	 * @param isAddStarMark
	 *            1 添加星标 2 取消星标
	 * @return 0 失败 1 成功 2 用户未注册 3 笔记不存在
	 * 
	 * @throws Exception
	 */
	public static MCResult addOrDeleteStarMark(Context context, String noteId,
			String isAddStarMark) throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new AddOrDeleteStarMarkParams(context, noteId,
				isAddStarMark).getParams();
		L.i(TAG, "addOrDeleteStarMark url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "addOrDeleteStarMark", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 创建笔记本
	 * 
	 * @param context
	 * @param noteBookName
	 * @return 0：异常 1：成功 2：笔记本名称为空 3：用户未注册 4：存在同名笔记本
	 * 
	 * @throws Exception
	 */
	public static MCResult createCloudNoteBook(Context context,
			String noteBookName) throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new CreateCloudNoteBookParams(context, noteBookName,
				"FROM_CREATE").getParams();
		L.i(TAG, "createCloudNoteBook url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "createCloudNoteBook", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 创建笔记本
	 * 
	 * @param context
	 * @param notebookId
	 * @param noteBookName
	 * @return 0：异常 1：成功 2：笔记本名称为空 3：用户未注册 4：存在同名笔记本
	 * 
	 * @throws Exception
	 */
	public static MCResult editNoteBook(Context context, String notebookId,
			String noteBookName) throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new EditNoteBookParams(context, notebookId,
				noteBookName).getParams();
		L.i(TAG, "editNoteBook url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "editNoteBook", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 删除笔记本
	 * 
	 * @param context
	 * @param notebookId
	 * @return 1：成功 2：操作者不存在 3：笔记删除失败 4：笔记本已删除 5：笔记附件删除失败
	 * 
	 * @throws Exception
	 */
	public static MCResult deleteNoteBook(Context context, String notebookId)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new DeleteNoteBookParams(context, notebookId)
				.getParams();
		L.i(TAG, "deleteNoteBook url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteNoteBook", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 移动笔记到一个笔记本
	 * 
	 * @param context
	 * @param noteId
	 * @param notebookId
	 * @return 0：失败 1：成功 2：操作者不存在 3：笔记已删除 4：笔记本不存在 5：笔记本更新时间修改失败
	 * 
	 * 
	 * @throws Exception
	 */
	public static MCResult moveNoteToOtherNoteBook(Context context,
			String noteId, String notebookId) throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new MoveNoteToOtherNoteBookParams(context, noteId,
				notebookId).getParams();
		L.i(TAG, "moveNoteToOtherNoteBook url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "moveNoteToOtherNoteBook", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 转存附件到云文件
	 * 
	 * @param context
	 * @param adjunctPath
	 *            附件path
	 * @return 0系统异常 1处理成功 2操作者不存在 3附件不存在 4操作者无操作权限
	 * 
	 * @throws Exception
	 */
	public static MCResult saveToAppFile(Context context, String adjunctPath)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new SaveToAppFileParams(context, adjunctPath)
				.getParams();
		L.i(TAG, "saveToAppFile url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "saveToAppFile", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取笔记分享者列表
	 * 
	 * @param context
	 * @param noteId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * 
	 * @throws Exception
	 */
	public static MCResult shareMemberList(Context context, String noteId,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new ShareMemberListParams(context, noteId, startRecord,
				maxResults).getParams();
		L.i(TAG, "shareMemberList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareMemberList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				ShareLeaguerInfoBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 获取笔记分享圈子列表
	 * 
	 * @param context
	 * @param noteId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * 
	 * @throws Exception
	 */
	public static MCResult shareGroupList(Context context, String noteId,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new ShareGroupListParams(context, noteId, startRecord,
				maxResults).getParams();
		L.i(TAG, "shareGroupList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareGroupList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				ShareGroupInfoBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 创建云笔记
	 * 
	 * @param context
	 * @param oweToNoteBook
	 *            是否属于笔记本
	 * @param noteBookId
	 *            笔记本Id,当oweToNoteBook为false时，该字段为默认笔记本，即所谓的不放入笔记本，
	 *            如果为true时传笔记本的Id
	 * @param title
	 *            笔记标题
	 * @param content
	 *            笔记内容
	 * @param attachments
	 *            上传返回JSON 附件：视频、音频、文件、图片
	 * @param statTarget
	 *            星标记： 1、是星标记 2、不 是星标记
	 * @param updateAddress
	 *            更新地点
	 * @param firstPhotoPath
	 *            第一张图片地址
	 * @return 1、title与content不能同时为空 2、title为空时title=content.substring(0, 11)
	 *         3、content为空时content=title
	 * @throws Exception
	 */
	public static MCResult createCloudNote(final Context context,
			String oweToNoteBook, String noteBookId, String title,
			String content, String[] attachments, String statTarget,
			String updateAddress, String firstPhotoPath) throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new CreateCloudNoteParams(context, oweToNoteBook,
				noteBookId, title, content, "FROM_CREATE", attachments,
				statTarget, updateAddress, firstPhotoPath).getParams();
		L.i(TAG, "createCloudNote url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "createCloudNote", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 编辑云笔记
	 * 
	 * @param context
	 * @param noteId
	 *            笔记Id
	 * @param oweToNoteBook
	 *            是否属于笔记本
	 * @param noteBookId
	 *            笔记本Id,当oweToNoteBook为false时，该字段为默认笔记本，即所谓的不放入笔记本，
	 *            如果为true时传笔记本的Id
	 * @param title
	 *            笔记标题
	 * @param content
	 *            笔记内容
	 * @param attachments
	 *            上传返回JSON 附件：视频、音频、文件、图片
	 * @param statTarget
	 *            星标记： 1、是星标记 2、不 是星标记
	 * @param updateAddress
	 *            更新地点
	 * @param firstPhotoPath
	 *            第一张图片地址
	 * @return 1、title与content不能同时为空 2、title为空时title=content.substring(0, 11)
	 *         3、content为空时content=title
	 * @throws Exception
	 */
	public static MCResult editCloudNote(final Context context, String noteId,
			String oweToNoteBook, String noteBookId, String title,
			String content, String[] attachments,
			String statTarget, String updateAddress, String firstPhotoPath)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new EditCloudNoteParams(context, noteId, oweToNoteBook,
				noteBookId, title, content, "FROM_CREATE", attachments,
				statTarget, updateAddress, firstPhotoPath).getParams();
		L.i(TAG, "editCloudNote url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "editCloudNote", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 上传附件
	 * 
	 * @param context
	 * @param upload
	 *            文件file
	 * @param uploadName
	 *            文件名
	 * @return
	 * @throws Exception
	 */
	public static MCResult uploadAttachmentFile(final Context context,
			String upload, String uploadName) throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new UploadAttachmentFileParams(context, upload,
				uploadName).getParams();
		L.i(TAG, "uploadAttachmentFile url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "uploadAttachmentFile", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 上传图片
	 * 
	 * @param context
	 * @param upload
	 *            文件file
	 * @param uploadName
	 *            文件名
	 * @return
	 * @throws Exception
	 */
	public static MCResult uploadAttachmentImge(final Context context,
			String upload, String uploadName) throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new UploadAttachmentImgeParams(context, upload,
				uploadName).getParams();
		L.i(TAG, "uploadAttachmentImge url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "uploadAttachmentImge", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 创建笔记
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @return 1、title与content不能同时为空 2、title为空时title=content.substring(0, 11)
	 *         3、content为空时content=title
	 * @throws Exception
	 */
	public static MCResult createDiary(final Context context, String title,
			String content) throws Exception {
		// 拼接请求参数
		String url = URLProperties.DIARY_JSON;
		String params = new CreateDiaryParams(context, title, content)
				.getParams();
		L.i(TAG, "createDiary url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "createDiary", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 编辑笔记
	 * 
	 * @param context
	 * @param diaryId
	 * @param title
	 * @param content
	 * @return
	 * @throws Exception
	 *             1、title与content不能同时为空 2、title为空时title=content.substring(0,
	 *             11) 3、content为空时content=title
	 */
	public static MCResult editDiary(final Context context, String diaryId,
			String title, String content) throws Exception {
		// 拼接请求参数
		String url = URLProperties.DIARY_JSON;
		String params = new EditDiaryParams(context, diaryId, title, content)
				.getParams();
		L.i(TAG, "editDiary url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "editDiary", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 删除笔记
	 * 
	 * @param context
	 * @param noteId
	 * @return
	 * @throws Exception
	 */
	public static MCResult deleteCloudNote(final Context context, String noteId)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new DeleteCloudNoteParams(context, noteId).getParams();
		L.i(TAG, "deleteCloudNote url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteCloudNote", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 分享笔记给朋友
	 * 
	 * @param context
	 * @param diaryId
	 * @param memberIds
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareDiary(final Context context, String diaryId,
			final String[] memberIds) throws Exception {
		// 拼接请求参数
		String url = URLProperties.CLOUD_NOTE_JSON;
		String params = new ShareDiaryToFriendParams(context, diaryId,
				memberIds, "").getParams();
		L.i(TAG, "shareDiary url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareDiary", result);
		new Thread() {
			public void run() {
				FriendListService.getService(context)
						.saveContactTime(memberIds);
			};
		}.start();
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 分享笔记到圈子
	 * 
	 * @param context
	 * @param diaryId
	 * @param groupIds
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareDiaryToGroup(final Context context,
			String diaryId, final String[] groupIds) throws Exception {
		// 拼接请求参数
		String url = URLProperties.DIARY_JSON;
		String params = new ShareDiaryToGroupParams(context, diaryId, groupIds)
				.getParams();
		L.i(TAG, "shareDiaryToGroup url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareDiaryToGroup", result);
		new Thread() {
			public void run() {
				GroupListService.getService(context).saveContactTime(groupIds);
			};
		}.start();
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

}

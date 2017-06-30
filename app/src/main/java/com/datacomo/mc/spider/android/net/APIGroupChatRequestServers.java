package com.datacomo.mc.spider.android.net;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;

import com.datacomo.mc.spider.android.db.ChatGroupMessageBeanService;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MessageContacterBean;
import com.datacomo.mc.spider.android.net.been.groupchat.ChatLeaguerUnreadNumberBean;
import com.datacomo.mc.spider.android.net.been.groupchat.MapGroupChatMemberList;
import com.datacomo.mc.spider.android.net.been.groupchat.MapGroupChatMessage;
import com.datacomo.mc.spider.android.net.been.groupchat.MapInvitableGroupChatMemberList;
import com.datacomo.mc.spider.android.net.been.map.MapGroupChatCurrentStatus;
import com.datacomo.mc.spider.android.params.groupchat.ChatLeaguerUnreadNumberParams;
import com.datacomo.mc.spider.android.params.groupchat.ContactMembersForGroupChatParams;
import com.datacomo.mc.spider.android.params.groupchat.DeleteGroupChatMessageByMemberParams;
import com.datacomo.mc.spider.android.params.groupchat.ExitGroupChatParams;
import com.datacomo.mc.spider.android.params.groupchat.GetGroupChatCurrentStatusParams;
import com.datacomo.mc.spider.android.params.groupchat.GroupChatMemberListParams;
import com.datacomo.mc.spider.android.params.groupchat.GroupChatMessageParams;
import com.datacomo.mc.spider.android.params.groupchat.InvitableGroupChatMemberList;
import com.datacomo.mc.spider.android.params.groupchat.InviteLeaguerEnterChat;
import com.datacomo.mc.spider.android.params.groupchat.OtherMemberNewMessageParams;
import com.datacomo.mc.spider.android.params.groupchat.RemoveMemberFromGroupChatParams;
import com.datacomo.mc.spider.android.params.groupchat.SearchInvitableGroupChatMemberListParams;
import com.datacomo.mc.spider.android.params.groupchat.SendGroupMessageParams;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.StreamUtil;

public class APIGroupChatRequestServers {
	private static final String TAG = "APIGroupChatRequestServers";

	/**
	 * 获取圈聊当前状态
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult getGroupChatCurrentStatus(final Context context,
			final String groupId) throws Exception {
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new GetGroupChatCurrentStatusParams(context, groupId)
				.getParams();
		L.i(TAG, "getGroupChatCurrentStatus url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getGroupChatCurrentStatus", result);

		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String rContent = mcResult.getResult().toString();
		// map VISITSTATUS 1、在圈聊中2、不在圈聊中 VISITROLE1、圈主 2、管理员 3、4、普通成员
		// 5、申请成为普通成员 MEMBERNUM当前圈聊人数
		MapGroupChatCurrentStatus bean = (MapGroupChatCurrentStatus) JsonParseTool
				.dealComplexResult(rContent, MapGroupChatCurrentStatus.class);
		mcResult.setResult(bean);
		new Thread() {
			public void run() {
				GroupListService.getService(context).saveContactTime(
						new String[] { groupId });
			};
		}.start();

		return mcResult;
	}

	// /**
	// * 加入圈聊
	// *
	// * @param context
	// * @param groupId
	// * @return
	// * @throws Exception
	// */
	// public static MCResult enterGroupChat(Context context, String groupId)
	// throws Exception {
	// MCResult mcResult = null;
	// String url = URLProperties.GROUP_CHAT_JSON;
	// String params = new EnterGroupChatParams(context, groupId).getParams();
	// L.i(TAG, "enterGroupChat url=" + url + ",params=" + params);
	// String result = StreamUtil.readData(new HttpRequestServers()
	// .getRequest(url + "?" + params));
	// L.getLongLog(TAG, "enterGroupChat", result);
	// mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
	// MCResult.class);
	// if (mcResult == null || mcResult.getResultCode() != 1) {
	// return mcResult;
	// }
	// String rContent = mcResult.getResult().toString();
	// MapEnterGroupChat bean = (MapEnterGroupChat) JsonParseTool
	// .dealComplexResult(rContent, MapEnterGroupChat.class);
	// mcResult.setResult(bean);
	// return mcResult;
	// }

	/**
	 * 获取群聊内容
	 * 
	 * @param context
	 * @param chatId
	 *            圈聊Id
	 * @param messageId
	 *            群聊消息ID[此为游标，如果传0则取出最新的N条，N为下面的maxResults参数]
	 * @param newOrOld
	 *            获取游标之前/之后的数据[1:之前2:之后]
	 * @param maxResults
	 * @param isPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult getGroupChatMessage(Context context, int chatId,
			int messageId, int newOrOld, int maxResults, boolean isPaging)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new GroupChatMessageParams(context, chatId, messageId,
				newOrOld, maxResults, isPaging).getParams();
		L.i(TAG, "getGroupChatMessage url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getGroupChatMessage", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String rContent = mcResult.getResult().toString();
		MapGroupChatMessage chatmemberList = (MapGroupChatMessage) JsonParseTool
				.dealComplexResult(rContent, MapGroupChatMessage.class);
		if (chatmemberList.getCOUNT() > 0) {
			ChatGroupMessageBeanService.getService(context).save(
					chatmemberList.getLIST(), messageId + "", chatId + "");
		}
		mcResult.setResult(chatmemberList);
		return mcResult;
	}

	/**
	 * 获取未读群聊内容
	 * 
	 * @param context
	 * @param chatId
	 *            圈聊Id
	 * @param messageId
	 *            群聊消息ID[此为游标，如果传0则取出最新的N条，N为下面的maxResults参数]
	 * @param newOrOld
	 *            获取游标之前/之后的数据[1:之前2:之后]
	 * @param maxResults
	 * @param isPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult otherMemberNewMessage(Context context, int groupId,
			int messageId, int startRecord, int maxResults, boolean noPaging)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new OtherMemberNewMessageParams(context, groupId,
				messageId, startRecord, maxResults, noPaging).getParams();
		L.i(TAG, "otherMemberNewMessage url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "otherMemberNewMessage", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String rContent = mcResult.getResult().toString();
		MapGroupChatMessage chatmemberList = (MapGroupChatMessage) JsonParseTool
				.dealComplexResult(rContent, MapGroupChatMessage.class);
		if (chatmemberList.getCOUNT() > 0) {
			ChatGroupMessageBeanService.getService(context).save(
					chatmemberList.getLIST(), messageId + "", groupId + "");
		}
		mcResult.setResult(chatmemberList);
		return mcResult;
	}

	/**
	 * 发送消息
	 * 
	 * @param context
	 * @param groupIds
	 * @param isPlainText
	 *            是否是文本 true or false
	 * @param content
	 *            文本内容
	 * @param objectInfos
	 *            附件（照片、文件、声音、视频等）
	 * @param memberName
	 * @param chatName
	 * @param type
	 * @param uploadName
	 * @param l
	 * @param uri
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static MCResult sendGroupMessage(Context context, String[] groupIds,
			boolean isPlainText, String content, String[] objectInfos,
			String memberName, String chatName, String type, String uploadName,
			String l, String uri, String path) throws Exception {
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new SendGroupMessageParams(context, groupIds,
				isPlainText, content, objectInfos, memberName, chatName, type,
				uploadName, l, uri, path).getParams();
		L.i(TAG, "sendGroupMessage url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "sendGroupMessage", result);
		MCResult mc = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		return mc;
	}

	/**
	 * 退出圈聊
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult exitGroupChat(Context context, int groupId)
			throws Exception {
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new ExitGroupChatParams(context, groupId).getParams();
		L.i(TAG, "exitGroupChat url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "exitGroupChat", result);
		MCResult mc = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		// TODO
		// if (mc != null && mc.getResultCode() == 1) {
		// try {
		// if ((Integer) mc.getResult() == 1) {
		// XMPPAPI.getXmppapi().leaveGroup(groupId + "", context);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		return mc;
	}

	/**
	 * 踢出圈聊成员
	 * 
	 * @param context
	 * @param groupId
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public static MCResult removeMemberFromGroupChat(Context context,
			int groupId, int memberId) throws Exception {
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new RemoveMemberFromGroupChatParams(context, groupId,
				memberId).getParams();
		L.i(TAG, "removeMemberFromGroupChat url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "removeMemberFromGroupChat", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 新版私信联系人列表（包括圈聊）
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResults
	 * @param isPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult getContactMembersForGroupChat(Context context,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new ContactMembersForGroupChatParams(context,
				startRecord, maxResults, true).getParams();
		L.i(TAG, "getContactMembersForGroupChat url=" + url + ",params="
				+ params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getContactMembersForGroupChat", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String content = mcResult.getResult().toString();
		List<Object> objectList = JsonParseTool.dealListResult(content,
				MessageContacterBean.class);
		mcResult.setResult(objectList);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& objectList != null
				&& objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_PLETTER, content);
		}

		return mcResult;
	}

	/**
	 * 获取圈聊成员列表
	 * 
	 * @param context
	 * @param chatId
	 *            圈聊Id
	 * @param startRecord
	 * @param maxResults
	 * @param isPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult getGroupChatMemberList(Context context, int chatId,
			int startRecord, int maxResults, boolean isPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new GroupChatMemberListParams(context, chatId,
				startRecord, maxResults, isPaging).getParams();
		L.i(TAG, "groupChatMemberList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "groupChatMemberList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String rContent = mcResult.getResult().toString();
		MapGroupChatMemberList chatmemberList = (MapGroupChatMemberList) JsonParseTool
				.dealComplexResult(rContent, MapGroupChatMemberList.class);
		mcResult.setResult(chatmemberList);
		return mcResult;
	}

	/**
	 * 邀请圈子成员加入圈聊
	 * 
	 * @param context
	 * @param memberIds
	 * @param chatId
	 *            圈聊Id
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult inviteLeaguerEnterChat(Context context,
			String[] memberIds, int chatId, int groupId) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new InviteLeaguerEnterChat(context, memberIds, chatId,
				groupId).getParams();
		L.i(TAG, "inviteLeaguerEnterChat url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "inviteLeaguerEnterChat", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		JSONObject jsonObject = (JSONObject) mcResult.getResult();
		Integer intResult = jsonObject.getInt("RESULT");
		mcResult.setResult(intResult);
		return mcResult;
	}

	/**
	 * 获取可以邀请加入圈聊的成员列表
	 * 
	 * @param context
	 * @param chatId
	 *            圈聊Id
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @param isPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult getInvitableGroupChatMemberList(Context context,
			int chatId, int groupId, int startRecord, int maxResults,
			boolean isPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new InvitableGroupChatMemberList(context, chatId,
				groupId, startRecord, maxResults, isPaging).getParams();
		L.i(TAG, "getInvitableGroupChatMemberList url=" + url + ",params="
				+ params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getInvitableGroupChatMemberList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String rContent = mcResult.getResult().toString();
		MapInvitableGroupChatMemberList chatmemberList = (MapInvitableGroupChatMemberList) JsonParseTool
				.dealComplexResult(rContent,
						MapInvitableGroupChatMemberList.class);
		mcResult.setResult(chatmemberList);
		return mcResult;
	}

	/**
	 * 根据手机号或者名字（精确或者拼音模糊）搜索可以邀请加入圈聊的成员列表
	 * 
	 * @param context
	 * @param chatId
	 *            圈聊Id
	 * @param groupId
	 * @param nameOrPhone
	 * @param startRecord
	 * @param maxResults
	 * @param isPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchInvitableGroupChatMemberList(Context context,
			int chatId, int groupId, String nameOrPhone, int startRecord,
			int maxResults, boolean isPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new SearchInvitableGroupChatMemberListParams(context,
				chatId, groupId, nameOrPhone, startRecord, maxResults, isPaging)
				.getParams();
		L.i(TAG, "searchInvitableGroupChatMemberList url=" + url + ",params="
				+ params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchInvitableGroupChatMemberList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String content = mcResult.getResult().toString();
		MapInvitableGroupChatMemberList chatmemberList = (MapInvitableGroupChatMemberList) JsonParseTool
				.dealComplexResult(content,
						MapInvitableGroupChatMemberList.class);
		mcResult.setResult(chatmemberList);
		return mcResult;
	}

	/**
	 * 获取群聊成员未读数量
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult getChatLeaguerUnreadNumber(Context context,
			String groupId) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new ChatLeaguerUnreadNumberParams(context, groupId)
				.getParams();
		L.i(TAG, "ChatLeaguerUnreadNumberParams url=" + url + ",params="
				+ params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "ChatLeaguerUnreadNumberParams", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String rContent = mcResult.getResult().toString();
		ChatLeaguerUnreadNumberBean bean = (ChatLeaguerUnreadNumberBean) JsonParseTool
				.dealSingleResult(rContent, ChatLeaguerUnreadNumberBean.class);
		mcResult.setResult(bean);
		return mcResult;
	}

	/**
	 * 删除圈聊信息
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult deleteGroupChatMessageByMember(Context context,
			String groupId, String chatMessageId) throws Exception {
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new DeleteGroupChatMessageByMemberParams(context,
				groupId, chatMessageId).getParams();
		L.i(TAG, "deleteGroupChatMessageByMember url=" + url + ",params="
				+ params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteGroupChatMessageByMember", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

}

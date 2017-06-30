package com.datacomo.mc.spider.android.net;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.datacomo.mc.spider.android.bean.BackupContactsInfo;
import com.datacomo.mc.spider.android.bean.ContactEntity;
import com.datacomo.mc.spider.android.bean.ContactInfo;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.db.ChatMessageBeanService;
import com.datacomo.mc.spider.android.db.ContactsBookService;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.db.QuuBoCommentService;
import com.datacomo.mc.spider.android.db.QuuBoInfoService;
import com.datacomo.mc.spider.android.db.QuuBoPraiseService;
import com.datacomo.mc.spider.android.db.QuuBoVisitService;
import com.datacomo.mc.spider.android.enums.ClientWelcomePicEnum;
import com.datacomo.mc.spider.android.enums.GroupAtlasEnum;
import com.datacomo.mc.spider.android.enums.GroupLeaguerManageEnum;
import com.datacomo.mc.spider.android.enums.GroupResourceTypeEnum;
import com.datacomo.mc.spider.android.enums.NewNumTypeEnum;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.net.been.DonateBean;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.FriendGroupBean;
import com.datacomo.mc.spider.android.net.been.GroupBasicBean;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.GroupInfoBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerPermissionBean;
import com.datacomo.mc.spider.android.net.been.InviteRegisterBean;
import com.datacomo.mc.spider.android.net.been.LoginBean;
import com.datacomo.mc.spider.android.net.been.LoginJustByPhoneBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MessageBean;
import com.datacomo.mc.spider.android.net.been.MessageContacterBean;
import com.datacomo.mc.spider.android.net.been.MessageGreetBean;
import com.datacomo.mc.spider.android.net.been.NewNoticesNumBean;
import com.datacomo.mc.spider.android.net.been.QuuboInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.ResultAll;
import com.datacomo.mc.spider.android.net.been.map.MapFriendBean;
import com.datacomo.mc.spider.android.net.been.map.MapFriendSimpleBean;
import com.datacomo.mc.spider.android.net.been.map.MapGroupBasicBean;
import com.datacomo.mc.spider.android.net.been.map.MapGroupFileBean;
import com.datacomo.mc.spider.android.net.been.map.MapGroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapGroupPhotoBean;
import com.datacomo.mc.spider.android.net.been.map.MapGroupSimpleBean;
import com.datacomo.mc.spider.android.net.been.map.MapGroupTopicBean;
import com.datacomo.mc.spider.android.net.been.map.MapMemberBean;
import com.datacomo.mc.spider.android.net.been.map.MapMessageNoticeBean;
import com.datacomo.mc.spider.android.net.been.map.MapRecommendFriendBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceCommentBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceGreatBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceVisitBean;
import com.datacomo.mc.spider.android.net.been.map.MapThirdInfoListBean;
import com.datacomo.mc.spider.android.params.*;
import com.datacomo.mc.spider.android.params.msg.ContactMemberMessagesParams;
import com.datacomo.mc.spider.android.params.msg.DeleteGreetParams;
import com.datacomo.mc.spider.android.params.msg.DeleteMessageContactersParams;
import com.datacomo.mc.spider.android.params.msg.DeleteMessagesParams;
import com.datacomo.mc.spider.android.params.msg.DeleteNoticesParams;
import com.datacomo.mc.spider.android.params.msg.GetNoticeParams;
import com.datacomo.mc.spider.android.params.msg.GreetInfoListParams;
import com.datacomo.mc.spider.android.params.msg.GreetMemberParams;
import com.datacomo.mc.spider.android.params.msg.MyGroupChatListParams;
import com.datacomo.mc.spider.android.params.msg.MyMessageListParams;
import com.datacomo.mc.spider.android.params.msg.NewNoticeParams;
import com.datacomo.mc.spider.android.params.msg.NewNumParams;
import com.datacomo.mc.spider.android.params.msg.NoticeListParams;
import com.datacomo.mc.spider.android.params.msg.SearchGroupChatParams;
import com.datacomo.mc.spider.android.params.msg.SearchMessageParams;
import com.datacomo.mc.spider.android.params.msg.SearchNoticesParams;
import com.datacomo.mc.spider.android.params.msg.SendMessageParams;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.service.UploadFileThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;
import com.datacomo.mc.spider.android.util.StreamUtil;
import com.datacomo.mc.spider.android.util.VersionUtil;

public class APIRequestServers {
	private static final String TAG = "APIRequestServers";

	/**
	 * 客户端开机画面
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static MCResult bootScreenImage(Context context) throws Exception {
		String url = URLProperties.SPIDER_JSON;
		String params = new BootScreenImageParams(context).getParams();
		L.i(TAG, "bootScreenImage url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "bootScreenImage str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static MCResult clientWelcomePic(Context context,
			ClientWelcomePicEnum method, int groupId) throws Exception {
		String url = URLProperties.MEMBER_JSON;
		String params = new ClientWelcomePicInfoParams(context, method, groupId)
				.getParams();
		L.i(TAG, "clientWelcomePic url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "clientWelcomePic str=" + str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		return mcResult;
	}

	/**
	 * 注册
	 * 
	 * @param context
	 * @param email
	 * @param password
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public static MCResult register(Context context, String email,
			String password, String phone) throws Exception {
		String url = URLProperties.SPIDER_JSON;
		String params = new RegisterParams(context, email, password, phone)
				.getParams();
		L.i(TAG, "register url=" + url + ",params=" + params);
		// http://192.168.1.108:8081/MC_Spider_API_V1.0/spider.json,
		// params=app_key=app_api_beta&call_id=1324518830512&client=ANDROID&email=nc693623533%40gmail.com&
		// method=registerBefore&password=693623533&phone=18610041095&sig=36ce73a33795e1d24f68d5c4b19e7458&
		// v=v1.0
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "register str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 激活码验证
	 * 
	 * @param context
	 * @param phone
	 * @param verifyCode
	 * @return
	 * @throws Exception
	 */
	public static MCResult verifyCodeCheck(Context context, String phone,
			String verifyCode) throws Exception {
		String url = URLProperties.SPIDER_JSON;
		String params = new RegisterVerifyCodeParams(context, phone, verifyCode)
				.getParams();
		L.i(TAG, "verifyCodeCheck url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "verifyCodeCheck str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 新用户引导魔币数量获取API
	 * 
	 * @param context
	 * @param guideType
	 * @return
	 * @throws Exception
	 */
	public static MCResult goldNumByGuideType(Context context, String guideType)
			throws Exception {
		String url = URLProperties.MEMBER_JSON;
		String params = new GuideCurrencyNumsParams(context,
				"goldNumByGuideType", guideType).getParams();
		L.i(TAG, "goldNumByGuideType url=" + url + "?" + params);

		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "goldNumByGuideType str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 改名
	 * 
	 * @param context
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static MCResult resetName(Context context, String name)
			throws Exception {
		String url = URLProperties.MEMBER_JSON;
		String params = new RenameParams(context, name).getParams();
		L.i(TAG, "resetName url=" + url + "?" + params);

		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "resetName str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 第三方帐号注册、登录
	 * 
	 * @param context
	 * @param phone
	 * @param verifyCode
	 * @return
	 * @throws Exception
	 */
	public static MCResult registerByThirdParty(Context context, String openId,
			String thirdPartyType, String name, String sex, String headUrlPath,
			String oauth_token, String oauth_token_secret) throws Exception {
		String url = URLProperties.SPIDER_JSON;
		String params = new RegisterByThirdPartyParams(context, openId,
				thirdPartyType, name, sex, headUrlPath, oauth_token,
				oauth_token_secret).getParams();
		L.i(TAG, "registerByThirdParty url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "registerByThirdParty str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 登录
	 * 
	 * @param context
	 * @param loadName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static MCResult login(Context context, String loadName,
			String password) throws Exception {
		String url = URLProperties.LOGIN_JSON;
		String params = new LoginParams(context, loadName, password)
				.getParams();
		L.i(TAG, "login url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "login str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 获取找回密码短号
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static MCResult getShortNumForPWD(Context context) throws Exception {
		String url = URLProperties.SPIDER_JSON;
		String params = new ShortNumForPWDParams(context).getParams();
		L.i(TAG, "getShortNumForPWD url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "getShortNumForPWD str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 退出登录
	 * 
	 * @param context
	 * @param loadName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static MCResult logout(Context context) throws Exception {
		String url = URLProperties.MEMBER_JSON;
		String params = new LogoutParams(context).getParams();
		L.i(TAG, "logout url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "logout str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 用户登录
	 * 
	 * @param context
	 * @param loadName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static MCResult memberLogin(Context context, String loadName,
			String password) throws Exception, SocketTimeoutException {
		String url = URLProperties.MEMBER_JSON;
		String params = new MemberLoginParams(context, loadName, password)
				.getParams();
		L.i(TAG, "memberLogin url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "memberLogin str=" + str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult.getResultCode() == 1) {
			LoginBean loginBean = (LoginBean) JsonParseTool.dealSingleResult(
					mcResult.getResult().toString(), LoginBean.class);
			mcResult.setResult(loginBean);
		}
		return mcResult;
	}

	/**
	 * 获取社员基本信息
	 * 
	 * @param context
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public static MCResult getMemberBasicInfo(final Context context,
			final String memberId) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.MEMBER_JSON;
		String params = new GetMemberBasicInfoParams(context, memberId)
				.getParams();
		L.i(TAG, "getMemberBasicInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getMemberBasicInfo", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		// 解析MemberBean
		MemberBean memberBean = (MemberBean) JsonParseTool.dealComplexResult(
				mcResult.getResult().toString(), MemberBean.class);
		L.i(TAG,
				"getMemberBasicInfo memberBean.MemberId="
						+ memberBean.getMemberId());
		mcResult.setResult(memberBean);
		return mcResult;
	}

	/**
	 * 编辑基本信息
	 * 
	 * @param context
	 * @param name
	 * @param sex
	 *            1或2
	 * @param birthday
	 *            yyyy-MM-dd
	 * @param introduction
	 *            简介
	 * @return
	 * @throws Exception
	 */
	public static MCResult editeMyData(Context context, String name, int sex,
			String birthday, String introduction) throws Exception {
		// 拼接请求参数
		String url = URLProperties.MEMBER_JSON;
		String params = new EditeMyDataParams(context, name, sex, birthday,
				introduction).getParams();
		L.i(TAG, "editeMyData url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "editeMyData", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 写心情
	 * 
	 * @param context
	 * @param moodContent
	 * @return
	 * @throws Exception
	 */
	public static MCResult createMoodContent(Context context, String moodContent)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.MEMBER_JSON;
		String params = new CreateMoodContentParams(context, moodContent)
				.getParams();
		L.i(TAG, "createMoodContent url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "createMoodContent", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取新数量-通知、秘信
	 * 
	 * @param context
	 * @param moodContent
	 * @return
	 * @throws Exception
	 */
	public static MCResult newNum(Context context, NewNumTypeEnum newNumTypeEnum)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = "";
		String params = "";
		String result = "";
		switch (newNumTypeEnum) {
		case NEWNOTICESNUM:// 消息
			url = URLProperties.NOTICE_JSON;
			params = new NewNumParams(context, "newNoticesNum", null)
					.getParams();
			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
			L.i(TAG, "newNum NEWNOTICESNUM url=" + url + ",params=" + params);
			mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
					MCResult.class);
			L.getLongLog(TAG, "newNum NEWNOTICESNUM", result);
			JSONObject jsonObject = new JSONObject(mcResult.getResult()
					.toString());
			mcResult.setResult(new int[] { jsonObject.getInt("ALL_NUM"),
					jsonObject.getInt("MAIL_NUM") });
			break;
		case NEWMESSAGENUM:// 私信
			url = URLProperties.MESSAGE_JSON;
			params = new NewNumParams(context, "newMessageNum", null)
					.getParams();
			L.i(TAG, "newNum NEWMESSAGENUM url=" + url + ",params=" + params);

			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
			L.getLongLog(TAG, "newNum NEWMESSAGENUM", result);
			mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
					MCResult.class);
			break;
		// case NEWMAILNUM:// 邮件
		// url = URLProperties.MAIL_JSON;
		// params = new NewNumParams(context, "newMailNum", null).getParams();
		// L.i(TAG, "newNum NEWMAILNUM url=" + url + ",params=" + params);
		//
		// result = StreamUtil.readData(new HttpRequestServers().getRequest(url
		// + "?" + params));
		// L.getLongLog(TAG, "newNum NEWMAILNUM", result);
		// mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
		// MCResult.class);
		// break;
		case NEWALLNUM:// 所有
			url = URLProperties.NOTICE_JSON;
			params = new NewNumParams(context, "newNoticesAllNum", null)
					.getParams();
			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
			L.i(TAG, "newNum NEWALLNUM url=" + url + ",params=" + params);
			mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
					MCResult.class);
			L.getLongLog(TAG, "newNum NEWALLNUM", result);
			if (mcResult != null && mcResult.getResultCode() == 1) {
				NewNoticesNumBean bean = (NewNoticesNumBean) JsonParseTool
						.dealSingleResult(mcResult.getResult().toString(),
								NewNoticesNumBean.class);
				mcResult.setResult(bean);
			}
			break;
		default:
			break;
		}
		return mcResult;
	}

	/**
	 * 单点登录信息
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static MCResult singleInfo(Context context) throws Exception {
		// 拼接请求参数
		String url = URLProperties.NOTICE_JSON;
		String params = new SingleInfoParams(context).getParams();
		L.i(TAG, "singleInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "singleInfo", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取公开圈子的动态——随便看看
	 * 
	 * @param context
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult openGroupResourceTrends(final Context context,
			String trendId, String maxResults, boolean noPaging,
			@SuppressWarnings("rawtypes") AsyncTask a) throws Exception {
		String url = URLProperties.TREND_JSON;
		String params = new OpenGroupResourceTrendsParams(context, trendId,
				maxResults, noPaging).getParams();
		L.i(TAG, "openGroupResourceTrends url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");
		L.getLongLog(TAG, "openGroupResourceTrends", result);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");
		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				ResourceTrendBean.class);
		if (objectList != null)
			L.d(TAG, "openGroupResourceTrends size=" + objectList.size());
		mcResult.setResult(objectList);

		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");
		return mcResult;
	}

	/**
	 * 获取某一类型的收藏列表
	 * 
	 * @param context
	 * @param resourceTypeEnum
	 *            ：OBJ_GROUP_QUUBO、OBJ_GROUP_FILE、OBJ_GROUP_PHOTO
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult collectionResourceList(final Context context,
			ResourceTypeEnum resourceTypeEnum, String startRecord,
			String maxResults, boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = null;
		String type = "";
		switch (resourceTypeEnum) {
		case OBJ_GROUP_QUUBO:
			params = new CollectionResourceListParams(context,
					"OBJ_GROUP_QUUBO", startRecord, maxResults, noPaging)
					.getParams();
			type = "3";
			break;
		case OBJ_GROUP_FILE:
			params = new CollectionResourceListParams(context,
					"OBJ_GROUP_FILE", startRecord, maxResults, noPaging)
					.getParams();
			type = "4";
			break;
		case OBJ_GROUP_PHOTO:
			params = new CollectionResourceListParams(context,
					"OBJ_GROUP_PHOTO", startRecord, maxResults, noPaging)
					.getParams();
			type = "5";
			break;
		default:
			break;
		}

		L.i(TAG, "collectionResourceList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "collectionResourceList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		MapResourceBean mapBean = (MapResourceBean) JsonParseTool
				.dealComplexResult(trends, MapResourceBean.class);
		L.i(TAG,
				"collectionResourceList mapBean.TOTALNUM="
						+ mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);

		final String types = type;
		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& mapBean != null
				&& mapBean.getTOTALNUM() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_STAR + types, trends);
		}
		return mcResult;
	}

	/**
	 * 获取最新动态的数量
	 * 
	 * @param context
	 * @param memberId
	 *            某个人的Id:isCertainMemberId为false时不起作用
	 * @param isOnlyActionMember
	 *            此人是否为发起者
	 * @param groupId
	 *            某些特定圈子Id:isCertainGroup为false时不起作用
	 * @param isCertainGroup
	 *            是否查看某些圈子的动态
	 * @param objectType
	 *            对象类型:isFilterByType为 false时不起作用
	 * @param isFilterByType
	 *            是否按类型查看 是：true 否：false
	 * @param lastRefershTime
	 *            上一次刷新的时间
	 */
	public static MCResult groupResourceTrendsOfNewNum(Context context,
			String memberId, boolean isOnlyActionMember, String groupId,
			boolean isCertainGroup, String objectType, boolean isFilterByType,
			long lastRefershTime) throws Exception {
		String url = URLProperties.TREND_JSON;
		String params = new GroupResourceTrendsOfNewNumParams(context,
				memberId, isOnlyActionMember, groupId, isCertainGroup,
				objectType, isFilterByType, lastRefershTime).getParams();
		L.i(TAG, "getAllGroupTrendList url=" + url + ",params=" + params);

		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getAllGroupTrendList", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取所优优工作圈子动态墙
	 * 
	 * @param context
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult getAllGroupTrendList(final Context context,
			String trendId, String maxResults, boolean noPaging,
			@SuppressWarnings("rawtypes") AsyncTask a) throws Exception {
		String url = URLProperties.TREND_JSON;
		String params = new GetAllGroupTrendListParams(context, trendId,
				maxResults, noPaging).getParams();
		L.i(TAG, "getAllGroupTrendList url=" + url + ",params=" + params);

		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");
		L.getLongLog(TAG, "getAllGroupTrendList", result);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				ResourceTrendBean.class);
		mcResult.setResult(objectList);

		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");

		if ((trendId == null || "".equals(trendId) || "0".equals(trendId))
				&& objectList != null && objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_INFOWALL, trends);
		}
		L.i(TAG, "getAllGroupTrendList trendId=" + trendId);
		return mcResult;
	}

	/**
	 * 获取朋友圈动态墙
	 * 
	 * @param context
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult myFriendsTrends(final Context context,
			String trendId, String maxResults, String createTime,
			boolean noPaging, @SuppressWarnings("rawtypes") AsyncTask a)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = new MyFriendsTrendsParams(context, trendId, maxResults,
				createTime, noPaging).getParams();
		L.i(TAG, "myFriendsTrends url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");
		L.getLongLog(TAG, "myFriendsTrends", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");
		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				ResourceTrendBean.class);
		if (objectList != null)
			L.d(TAG, "myFriendsTrends size=" + objectList.size());
		mcResult.setResult(objectList);

		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");
		return mcResult;
	}

	/**
	 * 获取圈博详情
	 * 
	 * @param context
	 * @param groupId
	 * @param quuboId
	 * @return
	 * @throws Exception
	 */
	public static MCResult quuboInfo(Context context, String groupId,
			String quuboId) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.TREND_JSON;
		String params = new QuuboInfoParams(context, groupId, quuboId)
				.getParams();
		L.i(TAG, "quuboInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "quuboInfo", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		String content = mcResult.getResult().toString();
		// 解析QuuboInfoBean
		QuuboInfoBean quuboInfoBean = (QuuboInfoBean) JsonParseTool
				.dealComplexResult(content, QuuboInfoBean.class);
		mcResult.setResult(quuboInfoBean);
		QuuBoInfoService.getService(context).save(Integer.valueOf(groupId),
				Integer.valueOf(quuboId), content);
		return mcResult;
	}

	/**
	 * 资源详情
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @return
	 * @throws Exception
	 */
	public static MCResult resourceInfo(Context context, String groupId,
			String objectId, String objectType) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.TREND_JSON;
		String params = new ResourceInfoParams(context, groupId, objectId,
				objectType).getParams();
		L.i(TAG, "resourceInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "resourceInfo", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		// 解析resourceBean
		ResourceBean resourceBean = (ResourceBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						ResourceBean.class);
		mcResult.setResult(resourceBean);
		return mcResult;
	}

	/**
	 * 分享圈内资源到圈子成员
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @param isAllMember
	 * @param receiveMemberIds
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareResource(final Context context, String groupId,
			String objectId, String objectType, boolean isAllMember,
			final String[] receiveMemberIds) throws Exception {
		// 拼接请求参数
		String url = URLProperties.TREND_JSON;
		String params = new ShareResourceParams(context, groupId, objectId,
				objectType, isAllMember, receiveMemberIds).getParams();
		L.i(TAG, "shareResource url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareResource", result);
		if (isAllMember) {
			new Thread() {
				public void run() {
					FriendListService.getService(context).saveContactTime(
							receiveMemberIds);
				};
			}.start();
		}
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 分享资源到某个圈子
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @param receiveGroupIds
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareResourceToGroup(final Context context,
			String groupId, String objectId, String objectType,
			final String[] receiveGroupIds) throws Exception {
		// 拼接请求参数
		String url = URLProperties.TREND_JSON;
		String params = new ShareResourceToGroupParams(context, groupId,
				objectId, objectType, receiveGroupIds).getParams();
		L.i(TAG, "shareResourceToGroup url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "shareResourceToGroup", result);
		new Thread() {
			public void run() {
				GroupListService.getService(context).saveContactTime(
						receiveGroupIds);
			};
		}.start();
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 删除资源
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @return
	 * @throws Exception
	 */
	public static MCResult deleteResource(Context context, String groupId,
			String objectId, String objectType) throws Exception {
		// 拼接请求参数
		String url = URLProperties.TREND_JSON;
		String params = new DeleteResourceParams(context, groupId, objectId,
				objectType).getParams();
		L.i(TAG, "deleteResource url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteResource", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取圈子详情
	 * 
	 * @param context
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupInfo(final Context context, final String groupId)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new GroupInfoParams(context, groupId).getParams();
		L.i(TAG, "groupInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "groupInfo", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		// 解析GroupBean
		GroupBean groupBean = (GroupBean) JsonParseTool.dealComplexResult(
				mcResult.getResult().toString(), GroupBean.class);
		mcResult.setResult(groupBean);
		return mcResult;
	}

	/**
	 * 获取圈子详情
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult detailGroupInfo(final Context context,
			final String groupId) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new DetailGroupInfoParams(context, groupId).getParams();
		L.i(TAG, "detailGroupInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "detailGroupInfo", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		// 解析GroupBean
		GroupInfoBean groupBean = (GroupInfoBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						GroupInfoBean.class);
		mcResult.setResult(groupBean);
		return mcResult;
	}

	/**
	 * 向圈子捐赠圈币
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	// TODO
	public static MCResult presentGoldtoGroup(Context context, String groupId,
			String goldNum) throws Exception {
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new PresentGoldtoGroupParams(context, groupId, goldNum)
				.getParams();
		L.i(TAG, "presentGoldtoGroup url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "presentGoldtoGroup", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 圈币捐赠记录
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	// TODO
	public static MCResult presentRecord(Context context, String groupId,
			String startRecord, String maxResult, boolean noPaging)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new PresentRecordParams(context, groupId, startRecord,
				maxResult, noPaging).getParams();
		L.i(TAG, "presentRecord url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "presentRecord", result);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), DonateBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 是否存在开放主页
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult existOpenPage(Context context, String groupId)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new LeaguerPermissionInfoParams(context,
				"existOpenPage", groupId).getParams();
		L.i(TAG, "existOpenPage url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "existOpenPage", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		return mcResult;
	}

	/**
	 * 分享资源到开放主页
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @param openPageId
	 * @return
	 * @throws Exception
	 */
	public static MCResult forwardToOpenPage(final Context context,
			String groupId, String objectId, String objectType,
			String openPageId) throws Exception {
		// 拼接请求参数
		String url = URLProperties.GROUP_TOPIC_JSON;
		String params = new ForwardToOpenPageParams(context, groupId, objectId,
				objectType, openPageId).getParams();
		L.i(TAG, "forwardToOpenPage url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "forwardToOpenPage", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 访问者和圈子关系
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult leaguerPermissionInfo(Context context, String groupId)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new LeaguerPermissionInfoParams(context,
				"leaguerPermissionInfo", groupId).getParams();
		L.i(TAG, "leaguerPermissionInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "leaguerPermissionInfo", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		GroupLeaguerPermissionBean bean = (GroupLeaguerPermissionBean) JsonParseTool
				.dealSingleResult(mcResult.getResult().toString(),
						GroupLeaguerPermissionBean.class);
		mcResult.setResult(bean);
		return mcResult;
	}

	/**
	 * 圈子图谱——上级、下级、合作
	 * 
	 * @param context
	 * @param groupAtlasEnum
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupAtlas(Context context,
			GroupAtlasEnum groupAtlasEnum, String groupId) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = "";
		String result = "";
		switch (groupAtlasEnum) {
		case FATHERGROUPS:// 上级
			params = new GroupAtlasParams(context, "getFatherGroups", groupId,
					null, null, null).getParams();
			L.i(TAG, "groupAtlas 上级 url=" + url + ",params=" + params);
			// 获取数据
			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
			L.getLongLog(TAG, "groupAtlas 上级", result);
			// 解析MCResult
			mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
					MCResult.class);
			if (mcResult == null || mcResult.getResultCode() != 1) {
				return mcResult;
			}
			// 解析GroupBean
			GroupBasicBean groupBean = (GroupBasicBean) JsonParseTool
					.dealComplexResult(mcResult.getResult().toString(),
							GroupBasicBean.class);
			mcResult.setResult(groupBean);
			break;
		case SUBGROUPS:// 下级
			params = new GroupAtlasParams(context, "getSubGroups", groupId,
					null, null, null).getParams();
			L.i(TAG, "groupAtlas 下级 url=" + url + ",params=" + params);
			// 获取数据
			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
			L.getLongLog(TAG, "groupAtlas 下级", result);
			// 解析MCResult
			mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
					MCResult.class);
			if (mcResult == null || mcResult.getResultCode() != 1) {
				return mcResult;
			}
			// 解析GroupBean
			ArrayList<Object> objectList = (ArrayList<Object>) JsonParseTool
					.dealListResult(mcResult.getResult().toString(),
							GroupBasicBean.class);
			mcResult.setResult(objectList);
			break;
		case COLLABORATEGROUPS:// 合作
			params = new GroupAtlasParams(context, "getCollaborateGroups",
					groupId, null, null, null).getParams();
			L.i(TAG, "groupAtlas 合作 url=" + url + ",params=" + params);
			// 获取数据
			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
			L.getLongLog(TAG, "groupAtlas 合作 ", result);
			// 解析MCResult
			mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
					MCResult.class);
			if (mcResult == null || mcResult.getResultCode() != 1) {
				return mcResult;
			}
			// 解析GroupBean
			MapGroupBasicBean mapGroupBean = (MapGroupBasicBean) JsonParseTool
					.dealComplexResult(mcResult.getResult().toString(),
							MapGroupBasicBean.class);
			mcResult.setResult(mapGroupBean);
			break;
		default:
			break;
		}
		return mcResult;
	}

	/**
	 * 创建圈子
	 * 
	 * @param context
	 * @param groupName
	 * @param groupDesc
	 * @param tags
	 * @param privacySetting
	 *            圈子类型：1 - 公开;2 - 私密
	 * @return
	 * @throws Exception
	 */
	public static MCResult createGroup(Context context, String groupName,
			String groupDesc, String tags, String privacySetting)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new CreateGroupParams(context, groupName, groupDesc,
				tags, privacySetting).getParams();
		L.i(TAG, "createGroup url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "createGroup", result);
		// {"GROUP_ID":8091,"CREATE_RESULT":4,......}
		// 0(创建异常) 1(创建成功) 2(创建失败：操作者不存在) 3(创建失败：圈子名为空) 4(该圈子已存在)
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 圈人（邀请社员加入圈子）
	 * 
	 * @param context
	 * @param groupId
	 * @param friendIds
	 * @param phones
	 * @return
	 * @throws Exception
	 */
	public static MCResult addLeaguer(Context context, String groupId,
			String[] friendIds, String[] phones) throws Exception {
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new AddLeaguerParams(context, groupId, friendIds,
				phones).getParams();
		L.i(TAG, "addLeaguer url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "addLeaguer", result);
		// "SUCCEED_PHONE"——成功加入圈子的社员
		// ”REGISTER_PHONE_LIST“——以加入圈子的手机号
		// “UNREGIST_PHONE”——未注册的手机号
		// “PHONE_NULL”——输入的手机号为空
		// “PERMISSION”——操作者没有权限 "RESULT"——0的时候失败
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 编辑圈子基本信息
	 * 
	 * @param context
	 *            param groupName 圈子名称
	 * @param isUpdateName
	 *            是否需要更新圈子名称：true-需要；false-不需要
	 * @param groupDesc
	 *            圈子描述
	 * @param isUpdateDesc
	 *            是否需要更新圈子描述：true-需要；false-不需要
	 * @param tags
	 *            圈子标签
	 * @param isUpdateTags
	 *            是否需要更新圈子标签：true-需要；false-不需要
	 * @param privacySetting
	 *            圈子隐私设置：1 - 公开;2 - 私密
	 * @param isUpdatePrivacy
	 *            是否需要更新圈子隐私设置：true-需要；false-不需要
	 * @return
	 * @throws Exception
	 */
	public static MCResult editGroupBasicInfo(final Context context,
			final String groupId, String groupName, boolean isUpdateName,
			String groupDesc, boolean isUpdateDesc, String tags,
			boolean isUpdateTags, String privacySetting, boolean isUpdatePrivacy)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new EditGroupBasicInfoParams(context, groupId,
				groupName, isUpdateName, groupDesc, isUpdateDesc, tags,
				isUpdateTags, privacySetting, isUpdatePrivacy).getParams();
		L.i(TAG, "editGroupBasicInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "editGroupBasicInfo", result);
		new Thread() {
			public void run() {
				GroupListService.getService(context).saveContactTime(
						new String[] { groupId });
			};
		}.start();
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 赞成员列表
	 * 
	 * @param context
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult praiseRecords(Context context, String groupId,
			String quuboId, String objectType, String noPaging,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = new PraiseRecordsParams(context, groupId, quuboId,
				objectType, noPaging, startRecord, maxResults).getParams();
		L.i(TAG, "praiseRecords url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "praiseRecords", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		String content = mcResult.getResult().toString();
		if ("0".equals(startRecord))
			QuuBoPraiseService.getService(context)
					.save(Integer.valueOf(groupId), Integer.valueOf(quuboId),
							content);

		MapResourceGreatBean mapBean = (MapResourceGreatBean) JsonParseTool
				.dealComplexResult(content, MapResourceGreatBean.class);
		L.i(TAG, "praiseRecords mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 访问成员列表
	 * 
	 * @param context
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult resourceVisitList(Context context, String groupId,
			String quuboId, String objectType, String noPaging,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = new ResourceVisitParams(context, groupId, quuboId,
				objectType, noPaging, startRecord, maxResults).getParams();
		L.i(TAG, "resourceVisitList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "resourceVisitList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		String content = mcResult.getResult().toString();
		if ("0".equals(startRecord))
			QuuBoVisitService.getService(context)
					.save(Integer.valueOf(groupId), Integer.valueOf(quuboId),
							content);

		MapResourceVisitBean mapBean = (MapResourceVisitBean) JsonParseTool
				.dealComplexResult(content, MapResourceVisitBean.class);
		L.i(TAG, "resourceVisitList mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 评论列表
	 * 
	 * @param context
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult commentRecords(Context context, String groupId,
			String quuboId, String objectType, String noPaging,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = new CommentRecordsParams(context, groupId, quuboId,
				objectType, noPaging, startRecord, maxResults).getParams();
		L.i(TAG, "commentRecords url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "commentRecords", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		String content = mcResult.getResult().toString();
		if ("0".equals(startRecord))
			QuuBoCommentService.getService(context)
					.save(Integer.valueOf(groupId), Integer.valueOf(quuboId),
							content);

		MapResourceCommentBean mapBean = (MapResourceCommentBean) JsonParseTool
				.dealComplexResult(content, MapResourceCommentBean.class);
		L.i(TAG, "commentRecords mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 赞资源
	 * 
	 * @param context
	 * @param groupId
	 *            圈子id
	 * @param objectid
	 *            资源 id
	 * @param objectType
	 *            资源类型
	 * @return
	 * @throws Exception
	 */
	public static MCResult praiseResource(Context context, String groupId,
			String objectid, String objectType) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = new PraiseResourceParams(context, groupId, objectid,
				objectType).getParams();
		L.i(TAG, "praiseResource url=" + url + ",params=" + params);
		String result = null;
		try {
			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
		} catch (Exception e) {
			Thread.sleep(10 * 1000);
			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
			L.d(TAG, "praiseResource request url=" + url + ",params=" + params);
		}
		L.getLongLog(TAG, "praiseResource", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		L.i(TAG, "praiseRecords ResultCode=" + mcResult.getResultCode());
		return mcResult;
	}

	/**
	 * 收藏、取消收藏资源
	 * 
	 * @param context
	 * @param flag
	 *            true：收藏、false：取消收藏
	 * @param groupId
	 *            圈子id
	 * @param objectid
	 *            资源 id
	 * @param objectType
	 *            资源类型
	 * @return
	 * @throws Exception
	 */
	public static MCResult collectResource(Context context, boolean flag,
			String groupId, String objectid, String objectType)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;

		String method = "";
		if (flag) {
			method = "collectResource";
		} else {
			method = "cancelCollectResource";
		}

		String params = new CollectResourceParams(context, method, groupId,
				objectid, objectType).getParams();
		L.i(TAG, "collectResource url=" + url + ",params=" + params);
		String result = null;
		try {
			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
		} catch (Exception e) {
			Thread.sleep(10 * 1000);
			result = StreamUtil.readData(new HttpRequestServers()
					.getRequest(url + "?" + params));
			L.d(TAG, "collectResource request url=" + url + ",params=" + params);
		}
		L.getLongLog(TAG, "collectResource", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		L.i(TAG, "collectResource ResultCode=" + mcResult.getResultCode());
		return mcResult;
	}

	/**
	 * 评论资源
	 * 
	 * @param context
	 * @param groupId
	 * @param quuboId
	 * @param objectType
	 * @param commentContent
	 * @param receiveReplyIds
	 * @return
	 * @throws Exception
	 */
	public static MCResult commentResource(Context context, String groupId,
			String quuboId, String objectType, String commentContent,
			String[] receiveReplyIds) throws Exception {
		String url = URLProperties.TREND_JSON;
		String params = new CommentResourceParams(context, groupId, quuboId,
				objectType, commentContent, receiveReplyIds).getParams();
		L.i(TAG, "commentResource url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));

		L.getLongLog(TAG, "commentResource", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 删除评论资源
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param commentId
	 * @param objectType
	 * @return 1、删除资源成功 2、用户不存在 3、资源不存在 4、已经评论过此资源
	 *         5、操作者不是圈子成员，没有对此圈子资源进行操作的权限或传入圈子编号为0 6、评论内容非本人、非管理员、非圈主
	 * @throws Exception
	 */
	public static MCResult deleteCommentResource(Context context,
			String groupId, String objectId, String commentId, String objectType)
			throws Exception {
		String url = URLProperties.TREND_JSON;
		String params = new DeleteCommentResourceParams(context, groupId,
				objectId, commentId, objectType).getParams();
		L.i(TAG, "deleteCommentResource url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "deleteCommentResource", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 最近访问的人列表
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult visitFriendListForIPhone(Context context,
			String memberId, String startRecord, String maxResults)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FRIEND_JSON;
		String params = new VisitFriendListForIPhoneParams(context, memberId,
				startRecord, maxResults).getParams();
		L.i(TAG, "visitFriendListForIPhone url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "visitFriendListForIPhone", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), FriendBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 获得成员照片列表
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult getMemberPhotosLists(Context context,
			String memberId, String startRecord, String maxResults,
			boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = new SingleResourceListParams(context, memberId,
				"OBJ_GROUP_PHOTO", false, "", startRecord, maxResults, noPaging)
				.getParams();
		L.i(TAG, "getMemberPhotosLists url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "getMemberPhotosLists", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapResourceBean mapBean = (MapResourceBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapResourceBean.class);
		L.i(TAG,
				"getMemberPhotosLists mapBean.TOTALNUM="
						+ mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);

		return mcResult;
	}

	/**
	 * 获得成员资源列表：照片、文件
	 * 
	 * @param context
	 * @param groupId
	 * @param resourceTypeEnum
	 *            OBJ_GROUP_PHOTO、OBJ_GROUP_FILE
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult singleResourceList(Context context, String memberId,
			ResourceTypeEnum resourceTypeEnum, String startRecord,
			String maxResults, boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = null;
		switch (resourceTypeEnum) {
		case OBJ_GROUP_PHOTO:
			params = new SingleResourceListParams(context, memberId,
					"OBJ_GROUP_PHOTO", false, null, startRecord, maxResults,
					noPaging).getParams();
			break;
		case OBJ_GROUP_FILE:
			params = new SingleResourceListParams(context, memberId,
					"OBJ_GROUP_FILE", false, null, startRecord, maxResults,
					noPaging).getParams();
			break;
		default:
			break;
		}

		L.i(TAG, "singleResourceList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "singleResourceList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapResourceBean mapBean = (MapResourceBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapResourceBean.class);
		L.i(TAG, "singleResourceList mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);

		return mcResult;
	}

	/**
	 * 获得圈子资源列表：照片、文件
	 * 
	 * @param context
	 * @param groupId
	 * @param resourceTypeEnum
	 *            OBJ_GROUP_PHOTO、OBJ_GROUP_FILE
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupResourceLists(Context context, String groupId,
			ResourceTypeEnum resourceTypeEnum, String startRecord,
			String maxResults, boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = null;
		switch (resourceTypeEnum) {
		case OBJ_GROUP_QUUBO:
			params = new SingleResourceListForGroupParams(context, groupId,
					true, "OBJ_GROUP_QUUBO", startRecord, maxResults, noPaging)
					.getParams();
			break;
		case OBJ_GROUP_PHOTO:
			params = new SingleResourceListForGroupParams(context, groupId,
					true, "OBJ_GROUP_PHOTO", startRecord, maxResults, noPaging)
					.getParams();
			break;
		case OBJ_GROUP_FILE:
			params = new SingleResourceListForGroupParams(context, groupId,
					true, "OBJ_GROUP_FILE", startRecord, maxResults, noPaging)
					.getParams();
			break;
		case OBJ_OPEN_PAGE_PHOTO:
			params = new SingleResourceListForGroupParams(context, groupId,
					true, "OBJ_OPEN_PAGE_PHOTO", startRecord, maxResults,
					noPaging).getParams();
			break;
		case OBJ_OPEN_PAGE_FILE:
			params = new SingleResourceListForGroupParams(context, groupId,
					true, "OBJ_OPEN_PAGE_FILE", startRecord, maxResults,
					noPaging).getParams();
			break;
		default:
			break;
		}

		L.i(TAG, "groupResourceLists url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "groupResourceLists", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapResourceBean mapBean = (MapResourceBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapResourceBean.class);
		L.i(TAG, "groupResourceLists mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);

		return mcResult;
	}

	/**
	 * 获得圈子资源列表：照片、文件
	 * 
	 * @param context
	 * @param groupId
	 * @param resourceTypeEnum
	 *            OBJ_GROUP_PHOTO、OBJ_GROUP_FILE
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchGroupResourceLists(Context context,
			String groupId, String objectName,
			ResourceTypeEnum resourceTypeEnum, String startRecord,
			String maxResults, boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = null;
		switch (resourceTypeEnum) {
		case OBJ_GROUP_QUUBO:
			params = new SingleResourceListForGroupSearchParams(context,
					groupId, objectName, "OBJ_GROUP_QUUBO", startRecord,
					maxResults, noPaging).getParams();
			break;
		case OBJ_GROUP_PHOTO:
			params = new SingleResourceListForGroupSearchParams(context,
					groupId, objectName, "OBJ_GROUP_PHOTO", startRecord,
					maxResults, noPaging).getParams();
			break;
		case OBJ_GROUP_FILE:
			params = new SingleResourceListForGroupSearchParams(context,
					groupId, objectName, "OBJ_GROUP_FILE", startRecord,
					maxResults, noPaging).getParams();
			break;
		case OBJ_OPEN_PAGE_PHOTO:
			params = new SingleResourceListForGroupSearchParams(context,
					groupId, objectName, "OBJ_OPEN_PAGE_PHOTO", startRecord,
					maxResults, noPaging).getParams();
			break;
		case OBJ_OPEN_PAGE_FILE:
			params = new SingleResourceListForGroupSearchParams(context,
					groupId, objectName, "OBJ_OPEN_PAGE_FILE", startRecord,
					maxResults, noPaging).getParams();
			break;
		default:
			break;
		}

		L.i(TAG, "searchGroupResourceLists url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "searchGroupResourceLists", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapResourceBean mapBean = (MapResourceBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapResourceBean.class);
		L.i(TAG, "groupResourceLists mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);

		return mcResult;
	}

	/**
	 * 获得某人在某圈子发布的话题列表
	 * 
	 * @param context
	 * @param memberId
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult getGroupQuuBoLists(Context context, String memberId,
			String groupId, String startRecord, String maxResults,
			boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		// method=singleResourceList&memberId=44815&objectType=OBJ_GROUP_QUUBO&isGroup=true&
		// groupId=7492&noPaging=true&startRecord=0&maxResults=10
		String params = new SingleResourceListParams(context, memberId,
				"OBJ_GROUP_QUUBO", true, groupId, startRecord, maxResults,
				noPaging).getParams();
		L.i(TAG, "getGroupQuuBoLists url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "getGroupQuuBoLists", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapResourceBean mapBean = (MapResourceBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapResourceBean.class);
		L.i(TAG, "getGroupQuuBoLists mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);

		return mcResult;
	}

	/**
	 * 我的动态列表
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult myMemberTrends(Context context, String trendId,
			ResourceTypeEnum resourceTypeEnum, String maxResults,
			boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String objectType = null;
		switch (resourceTypeEnum) {
		case OBJ_GROUP_ALL:
			objectType = "OBJ_GROUP_ALL";
			break;
		case OBJ_GROUP_QUUBO:
			objectType = "OBJ_GROUP_QUUBO";
			break;
		case OBJ_GROUP_FILE:
			objectType = "OBJ_GROUP_FILE";
			break;
		case OBJ_GROUP_PHOTO:
			objectType = "OBJ_GROUP_PHOTO";
			break;
		case OBJ_MEMBER_RES_MOOD:
			objectType = "OBJ_MEMBER_RES_MOOD";
			break;
		case OBJ_MEMBER_RES_LEAVEMESSAGE:
			objectType = "OBJ_MEMBER_RES_LEAVEMESSAGE";
			break;
		default:
			break;
		}
		String params = new MyMemberTrendsParams(context, trendId, objectType,
				maxResults, noPaging).getParams();
		L.i(TAG, "myMemberTrends url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "myMemberTrends", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), ResourceTrendBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 朋友的动态列表
	 * 
	 * @param context
	 * @param memberId
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult otherMemberTrends(Context context, String memberId,
			ResourceTypeEnum resourceTypeEnum, String trendId,
			String createTime, String maxResults, boolean noPaging)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = null;
		String objectType = null;
		String isFilterByType = "true";
		switch (resourceTypeEnum) {
		case OBJ_GROUP_ALL:
			objectType = "OBJ_GROUP_ALL";
			isFilterByType = "false";
			break;
		case OBJ_GROUP_QUUBO:
			objectType = "OBJ_GROUP_QUUBO";
			break;
		case OBJ_GROUP_FILE:
			objectType = "OBJ_GROUP_FILE";
			break;
		case OBJ_GROUP_PHOTO:
			objectType = "OBJ_GROUP_PHOTO";
			break;
		case OBJ_MEMBER_RES_MOOD:
			objectType = "OBJ_MEMBER_RES_MOOD";
			break;
		case OBJ_MEMBER_RES_LEAVEMESSAGE:
			objectType = "OBJ_MEMBER_RES_LEAVEMESSAGE";
			break;
		default:
			break;
		}
		params = new OtherMemberTrendsParams(context, memberId, objectType,
				isFilterByType, noPaging, trendId, createTime, maxResults)
				.getParams();
		L.i(TAG, "otherMemberTrends url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "otherMemberTrends", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), ResourceTrendBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 某个圈子动态列表
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult singleGroupResourceTrends(Context context,
			String groupId, String trendId, String maxResults, boolean noPaging)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = new SingleGroupResourceTrendsParams(context, groupId,
				trendId, maxResults, noPaging).getParams();
		L.i(TAG, "singleGroupResourceTrends url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "singleGroupResourceTrends", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), ResourceTrendBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 交流圈成员列表
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupLeaguerList(Context context, String groupId,
			String startRecord, String maxResult, boolean noPaging)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_JSON;
		String params = new GroupLeaguerListParams(context, groupId,
				startRecord, maxResult, noPaging).getParams();
		L.i(TAG, "groupLeaguerList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "groupLeaguerList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), GroupLeaguerBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 搜索圈子中的社员
	 * 
	 * @param context
	 * @param groupId
	 * @param searchContent
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupSearchMember(Context context, String groupId,
			String searchContent, String startRecord, String maxResult,
			boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_JSON;
		String params = new GroupSearchMemberParams(context, groupId,
				searchContent, startRecord, maxResult, noPaging).getParams();
		L.i(TAG, "groupLeaguerList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "groupLeaguerList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), GroupLeaguerBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 圈子成员管理
	 * 
	 * @param context
	 * @param groupResourceTypeEnum
	 * @param groupId
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupLeaguerManage(Context context,
			GroupLeaguerManageEnum groupLeaguerManageEnum, String groupId,
			String memberId) throws Exception {
		String url = URLProperties.GROUP_JSON;
		String params = new GroupLeaguerManageParams(context,
				groupLeaguerManageEnum.toString(), groupId, memberId)
				.getParams();
		L.i(TAG, "groupLeaguerManage url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "groupLeaguerManage", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 退出圈子
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult exitGroup(Context context, String groupId)
			throws Exception {
		String url = URLProperties.GROUP_JSON;
		String params = new ExitGroupParams(context, groupId).getParams();
		L.i(TAG, "exitGroup url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "exitGroup", result);
		// 0:异常 1：成功 2：是圈主或者管理员，无法退出圈子 3:社员不在圈子内
		MCResult mc = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		// if (mc != null && mc.getResultCode() == 1) {
		// try {
		// if ((Integer) mc.getResult() == 1) {
		// XMPPAPI.leaveGroup(groupId + "");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		return mc;
	}

	/**
	 * 获取邀请社员列表
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult inviteMemberList(Context context, String groupId,
			String startRecord, String maxResults, boolean noPaging)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_JSON;
		String params = new InviteMemberListParams(context, groupId,
				startRecord, maxResults, noPaging).getParams();
		L.i(TAG, "inviteMemberList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "inviteMemberList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), GroupLeaguerBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 搜索邀请社员列表
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchInviteMemberListForAndroid(Context context,
			String groupId, String content, String startRecord,
			String maxResults, boolean noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_JSON;
		String params = new SearchInviteMemberListParams(context, groupId,
				content, startRecord, maxResults, noPaging).getParams();
		L.i(TAG, "inviteMemberList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "inviteMemberList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), GroupLeaguerBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 获取圈子管理员列表
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupManagerList(Context context, String groupId,
			String startRecord, String maxResult, boolean noPaging)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_JSON;
		String params = new GroupManagerListParams(context, groupId,
				startRecord, maxResult, noPaging).getParams();
		L.i(TAG, "groupManagerList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "groupManagerList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), GroupLeaguerBean.class);
		L.d(TAG, objectList.size() + "");
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 创建圈子话题-快速发布
	 * 
	 * @param context
	 * @param groupIds
	 * @param fileTemps
	 * @param photoTemps
	 * @param fileIds
	 * @param topicTitle
	 * @param content
	 * @param tags
	 * @param fileInfos
	 * @param photoInfos
	 * @return
	 * @throws Exception
	 * @throws SocketTimeoutException
	 */
	public static MCResult createGroupTopic(final Context context,
			final String[] groupIds, String[] fileTemps, String[] photoTemps,
			String[] fileIds, String topicTitle, String content, String tags,
			String[] fileInfos, String[] photoInfos) throws Exception,
			SocketTimeoutException {
		String url = URLProperties.GROUP_TOPIC_JSON;
		String params = new CreateGroupTopicParams(context, groupIds,
				fileTemps, photoTemps, fileIds, topicTitle, content, tags,
				fileInfos, photoInfos).getParams();
		L.i(TAG, "createGroupTopic urls=" + url + "?" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.d(TAG, "createGroupTopic result=" + result);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		new Thread() {
			public void run() {
				GroupListService.getService(context).saveContactTime(groupIds);
			};
		}.start();
		return mcResult;
	}

	/**
	 * 获取交流圈列表
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static Object[] getCommGroupList(final Context context,
			Handler handler) throws Exception {
		String url = URLProperties.GROUP_JSON;
		// true 获取所有
		String params = new CommGroupListsParams(context, "all", "0", "5",
				false).getParams();
		L.i(TAG, "getCommGroupList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		// L.getLongLog(TAG, "getCommGroupList", result);

		Object[] objects = new Object[2];

		JSONObject jsonObject = new JSONObject(result);
		int resultCode = jsonObject.getInt("resultCode");
		L.d(TAG, "getCommGroupList : resultCode = " + resultCode);
		objects[0] = resultCode;
		if (resultCode != 1) {
			return objects;
		}

		JSONArray jsonArray = jsonObject.getJSONArray("result");
		if (jsonArray != null) {
			final ArrayList<GroupEntity> list = new ArrayList<GroupEntity>();
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
				String id = jsonObject2.getString("groupId");
				String name = jsonObject2.getString("groupName");
				String num = jsonObject2.getString("leaguerNum");
				String owner = jsonObject2.getString("memberName");
				String joinGroupStatus = jsonObject2
						.getString("joinGroupStatus");
				String head = jsonObject2.getString("groupPosterUrl")
						+ jsonObject2.getString("groupPosterPath");
				String openStatus = jsonObject2.getString("openStatus");

				String groupNamePy = null;
				String groupNameJp = null;
				try {
					groupNamePy = jsonObject2.getString("groupNamePy")
							.toLowerCase(Locale.getDefault());
					groupNameJp = jsonObject2.getString("groupNameJp")
							.toLowerCase(Locale.getDefault());
				} catch (Exception e) {
					e.printStackTrace();
				}
				// L.i(TAG, "getCommGroupList " + i + " id=" + id + ",name="
				// + name + ",num=" + num + ",owner=" + owner
				// + ",joinGroupStatus=" + joinGroupStatus
				// + ",openStatus=" + openStatus);
				// L.i(TAG, "getCommGroupList head=" + head);
				boolean type = "GROUP_OWNER".equals(joinGroupStatus)
						|| "GROUP_MANAGER".equals(joinGroupStatus);
				GroupEntity groupEntity = new GroupEntity(id, name, num, owner,
						type, false);
				groupEntity.setHead(head);
				groupEntity.setOpenStatus(openStatus);
				groupEntity.setGroupNamePy(groupNamePy);
				groupEntity.setGroupNameJp(groupNameJp);
				groupEntity.setRecentCheck(true);
				list.add(groupEntity);
			}
			objects[1] = list;
			// new Thread() {
			// public void run() {
			// try {
			// GroupListService service = GroupListService.getService(context);
			// service.delete();
			// service.save(list, handler);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
		// }.start();
		// }
		return objects;
	}

	/**
	 * 获取系统推荐的未加入的圈子列表（随机排列）
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static Object[] recommendGroupList(final Context context)
			throws Exception {
		String url = URLProperties.GROUP_JSON;
		// true 获取所有
		String params = new RecommendGroupListParams(context).getParams();
		L.i(TAG, "recommendGroupList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.d(TAG, "recommendGroupList result=" + result);

		Object[] objects = new Object[2];

		JSONObject jsonObject = new JSONObject(result);
		int resultCode = jsonObject.getInt("resultCode");
		L.d(TAG, "recommendGroupList : resultCode = " + resultCode);
		objects[0] = resultCode;
		if (resultCode != 1) {
			return objects;
		}

		JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray(
				"LIST");
		if (jsonArray != null) {
			final ArrayList<GroupEntity> list = new ArrayList<GroupEntity>();
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
				String id = jsonObject2.getString("groupId");
				String name = jsonObject2.getString("groupName");
				String head = jsonObject2.getString("groupPosterUrl")
						+ jsonObject2.getString("groupPosterPath");

				// L.i(TAG, "recommendGroupList " + i + " id=" + id + ",name="
				// + name + ",head=" + head);
				GroupEntity groupEntity = new GroupEntity(id, name, null, null,
						true, false);
				groupEntity.setHead(head);
				list.add(groupEntity);
			}
			objects[1] = list;
		}
		return objects;
	}

	/**
	 * 交流圈列表
	 * 
	 * @param context
	 * @param searchType
	 *            查询范围【myJoinedGroups(我加入的)、getMyManagedGroups(我管理的)、all 默认为所有】
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 *            false:分页、true不分页
	 * @return
	 * @throws Exception
	 */
	public static Object[] commGroupList(Context context, String searchType,
			String startRecord, String maxResult, boolean noPaging)
			throws Exception {
		String url = URLProperties.GROUP_JSON;
		String params = new CommGroupListsParams(context, searchType,
				startRecord, maxResult, noPaging).getParams();
		L.i(TAG, "commGroupList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "commGroupList", result);
		// mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
		// MCResult.class);
		// if (mcResult == null || mcResult.getResultCode() != 1) {
		// return mcResult;
		// }
		// ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
		// .getResult().toString(), GroupBasicBean.class);
		// mcResult.setResult(objectList);
		// return mcResult;
		Object[] objects = new Object[2];

		JSONObject jsonObject = new JSONObject(result);
		int resultCode = jsonObject.getInt("resultCode");
		L.d(TAG, "commGroupList : resultCode = " + resultCode);
		objects[0] = resultCode;
		if (resultCode != 1) {
			return objects;
		}

		JSONArray jsonArray = jsonObject.getJSONArray("result");
		if (jsonArray != null) {
			boolean type = "getMyManagedGroups".equals(searchType);
			ArrayList<GroupEntity> list = new ArrayList<GroupEntity>();
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
				String id = jsonObject2.getString("groupId");
				String name = jsonObject2.getString("groupName");
				String num = jsonObject2.getString("leaguerNum");
				String owner = jsonObject2.getString("memberName");
				String head = jsonObject2.getString("groupPosterUrl")
						+ jsonObject2.getString("groupPosterPath");
				String openStatus = jsonObject2.getString("openStatus");
				int groupProperty = jsonObject2.getInt("groupProperty");
				int groupType = jsonObject2.getInt("groupType");

				GroupEntity groupEntity = new GroupEntity(id, name, num, owner,
						type, false);
				groupEntity.setHead(head);
				groupEntity.setOpenStatus(openStatus);
				groupEntity.setGroupType(groupType);
				groupEntity.setGroupProperty(groupProperty);
				list.add(groupEntity);
			}
			objects[1] = list;

			if ((startRecord == null || "".equals(startRecord) || "0"
					.equals(startRecord)) && list != null && list.size() > 0) {
				LocalDataService.getInstense().save(context,
						LocalDataService.TXT_GROUP, jsonArray.toString());
			}
		}
		return objects;
	}

	/**
	 * 创建朋友圈
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static MCResult createFriendGroup(Context context, String groupName,
			String[] friendIds) throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new CreateFriendGroupParams(context, groupName,
				friendIds).getParams();
		L.i(TAG, "createFriendGroup url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers().postRequest(
				url, params));
		L.i(TAG, "createFriendGroup str=" + str);
		// result Map
		// STATUS：0创建朋友分组异常、1创建分组成功、2分组名称为空,或者长度大于50、3操作者不存在、4分组名称已经存在、5创建朋友分组失败，朋友数量已达上限
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 朋友圈列表
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult friendGroupList(Context context) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FRIEND_JSON;
		String params = new FriendGroupListsParams(context).getParams();
		L.i(TAG, "friendGroupList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "friendGroupList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), FriendGroupBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 添加到朋友圈
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static MCResult addFriendToGroup(Context context, String friendId,
			String[] groupIds) throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new AddFriendToGroupParams(context, friendId, groupIds)
				.getParams();
		L.i(TAG, "addFriendToGroup url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "addFriendToGroup str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 向指定朋友圈添加朋友
	 * 
	 * @param context
	 * @param friendIds
	 *            社员编号
	 * @param groupIds
	 *            朋友圈编号
	 * @param isForRecommond
	 *            是否在推荐好友通知中点同意时调用（推荐好友通知中点同意会给推荐人发一条回执通知） true:是；false:否
	 * @return
	 * @throws Exception
	 */
	public static MCResult addFriendsToGroup(Context context,
			String[] friendIds, String[] groupIds, String isForRecommond)
			throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new AddFriendsToGroupParams(context, friendIds,
				groupIds, isForRecommond).getParams();
		L.i(TAG, "addFriendToGroup url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "addFriendToGroup str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 推荐朋友圈
	 * 
	 * @param context
	 * @param friendIds
	 *            推荐好友编号
	 * @param groupIds
	 *            推荐朋友圈编号
	 * @param isContainFans
	 *            是否推荐给圈我的人，默认false
	 * @param isContainVisiter
	 *            是否添加自己，默认false
	 * @param receiveFriendIds
	 *            接受者编号
	 * @param receiveGroupIds
	 *            接收朋友圈编号
	 * @param sendSms
	 *            是否发送短信，默认false
	 * @param postscript
	 *            附言
	 * @return 响应信息： 0:系统异常1：成功 2：参数不合法
	 * @throws Exception
	 */
	public static MCResult recommendFriend(Context context, String[] friendIds,
			String[] groupIds, String isContainFans, String isContainVisiter,
			String[] receiveFriendIds, String[] receiveGroupIds,
			String sendSms, String postscript) throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new RecommendFriendParams(context, friendIds, groupIds,
				isContainFans, isContainVisiter, receiveFriendIds,
				receiveGroupIds, sendSms, postscript).getParams();
		L.i(TAG, "recommendFriend url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "recommendFriend str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 接收推荐朋友圈
	 * 
	 * @param context
	 * @param groupName
	 *            朋友圈名字
	 * @param groupId
	 *            推荐朋友圈编号
	 * @param recommendId
	 *            推荐编号
	 * @return STATUS(Integer) GROUPID(Integer) GROUPNAME MESSAGE 0 0 groupName
	 *         创建好友分组异常 1 groupId groupName 创建分组成功 2 0 groupName 分组名称为空,或者长度大于50
	 *         3 0 groupName 操作者不存在 4 0 groupName 分组名称已经存在 5 0 groupName
	 *         创建好友分组失败，好友数量已达上限
	 * @throws Exception
	 */
	public static MCResult acceptRecommended(Context context, String groupName,
			String groupId, String recommendId) throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new AcceptRecommendedParams(context, groupName,
				groupId, recommendId).getParams();
		L.i(TAG, "acceptRecommended url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "acceptRecommended str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 从某个朋友圈移除朋友
	 * 
	 * @param context
	 * @param friendId
	 * @param groupId
	 *            null和0为解除朋友关系
	 * @return MCResult.getResult()=0:系统异常 1：成功 2：visitId与memberId不是朋友
	 * @throws Exception
	 */
	public static MCResult removeFriendFromGroup(Context context,
			String friendId, String groupId) throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new RemoveFriendFromGroupParams(context, friendId,
				groupId).getParams();
		L.i(TAG, "removeFriendFromGroup url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "removeFriendFromGroup str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 朋友个数
	 * 
	 * @param context
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public static MCResult friendNum(Context context, String memberId)
			throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new MemberFriendNumParams(context, memberId)
				.getParams();
		L.i(TAG, "friendNum url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "friendNum str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 朋友列表
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult friendList(Context context, String memberId,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FRIEND_JSON;
		// 排列顺序：1、拼音字母 正序 2、加为朋友时间倒序 3、最近联系时间倒序
		String orderType = "1";
		if ("0".equals(memberId)) {
			orderType = "3";
		}
		String params = new MemberFriendListParams(context, memberId,
				orderType, startRecord, maxResults).getParams();
		L.i(TAG, "friendList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "friendList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String listStr = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(listStr,
				FriendBean.class);
		mcResult.setResult(objectList);
		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& objectList != null
				&& objectList.size() > 0 && !"10".equals(maxResults)) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_FRIEND, listStr);
		}
		return mcResult;
	}

	/**
	 * 朋友列表
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	// public static MCResult recentlyFriendList(Context context, String
	// memberId,
	// String startRecord, String maxResults) throws Exception {
	// MCResult mcResult = null;
	// String url = URLProperties.FRIEND_JSON;
	// // 排列顺序：1、拼音字母 正序 2、加为朋友时间倒序 3、最近联系时间倒序
	// // String orderType = "1";
	// // if ("0".equals(memberId)) {
	// // orderType = "3";
	// // }
	// String params = new MemberFriendListParams(context, memberId, "3",
	// startRecord, maxResults).getParams();
	// L.i(TAG, "friendList url=" + url + ",params=" + params);
	// String result = StreamUtil.readData(new
	// HttpRequestServers().getRequest(url
	// + "?" + params));
	//
	// // L.getLongLog(TAG, "friendList", result);
	// mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
	// MCResult.class);
	// if (mcResult == null || mcResult.getResultCode() != 1) {
	// return mcResult;
	// }
	// ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
	// .getResult().toString(), FriendSimpleBean.class);
	// mcResult.setResult(objectList);
	// return mcResult;
	// }

	/**
	 * 获取朋友有更新的列表
	 * 
	 * @param context
	 * @param startUpdateTime
	 * @param startDeleteTime
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult friendUpdateList(Context context,
			String startUpdateTime, String startDeleteTime, String startRecord,
			String maxResults, String noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FRIEND_JSON;
		String params = new FriendGroupUpdateListParams(context,
				"friendUpdateListForClient", startUpdateTime, startDeleteTime,
				startRecord, maxResults, noPaging, null).getParams();
		L.i(TAG, "friendUpdateList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "friendUpdateList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapFriendSimpleBean map = (MapFriendSimpleBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapFriendSimpleBean.class);
		mcResult.setResult(map);
		return mcResult;
	}

	/**
	 * 获取圈子有更新的列表
	 * 
	 * @param context
	 * @param startUpdateTime
	 * @param startDeleteTime
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupUpdateList(Context context,
			String startUpdateTime, String startDeleteTime, String startRecord,
			String maxResults, String noPaging, String groupIdss)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_JSON;
		String params = new FriendGroupUpdateListParams(context,
				"myUpdateGroupsForClient", startUpdateTime, "0", startRecord,
				maxResults, noPaging, groupIdss).getParams();
		L.i(TAG, "groupUpdateList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "groupUpdateList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapGroupSimpleBean map = (MapGroupSimpleBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapGroupSimpleBean.class);
		mcResult.setResult(map);
		return mcResult;
	}

	/**
	 * 按朋友圈查看朋友列表
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult friendListByGroup(Context context, String groupId,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FRIEND_JSON;
		String params = new FriendListByGroupParams(context, groupId,
				startRecord, maxResults).getParams();
		L.i(TAG, "friendListByGroup url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "friendListByGroup", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), FriendBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 根据手机号/名字搜索朋友圈中的朋友
	 * 
	 * @param context
	 * @param groupId
	 * @param keyWord
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchFriendListByGroup(Context context,
			String groupId, String keyWord, String startRecord,
			String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FRIEND_JSON;
		String params;
		try {
			Integer.valueOf(keyWord);
			params = new SearchFriendListByGroupParams(context,
					"SEARCHFRIENDBYPHONE", groupId, keyWord, startRecord,
					maxResults, "false").getParams();
		} catch (Exception e) {
			params = new SearchFriendListByGroupParams(context,
					"SEARCHFRIENDBYNAME", groupId, keyWord, startRecord,
					maxResults, "false").getParams();
		}
		L.i(TAG, "friendListByGroup url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "friendListByGroup", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), FriendBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 获取推荐的人的列表
	 * 
	 * @param context
	 * @param groupId
	 * @param recommendId
	 * @return
	 * @throws Exception
	 */
	public static MCResult recommendList(Context context, String groupId,
			String recommendId, String startRecord, String maxResults)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FRIEND_JSON;
		String params = new RecommendListParams(context, groupId, recommendId,
				startRecord, maxResults).getParams();
		L.i(TAG, "recommendList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "recommendList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapRecommendFriendBean mapFriendBean = (MapRecommendFriendBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapRecommendFriendBean.class);
		mcResult.setResult(mapFriendBean);
		return mcResult;
	}

	/**
	 * 获取加入或踢出成员列表(动态使用)
	 * 
	 * @param context
	 * @param groupId
	 * @param noPaging
	 * @param type
	 *            区分查询加入或踢出成员列表（0：加入的成员列表 1：踢出成员列表（默认值为：0））
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult leaguresNewOrRemove(Context context, String groupId,
			String noPaging, int type, String startRecord, String maxResults,
			long createTime) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_JSON;
		String params = new LeaguresNewOrRemoveParams(context, groupId,
				noPaging, type, startRecord, maxResults, createTime)
				.getParams();
		L.i(TAG, "leaguresNewOrRemove url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "leaguresNewOrRemove", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapGroupLeaguerBean mapFriendBean = (MapGroupLeaguerBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapGroupLeaguerBean.class);
		mcResult.setResult(mapFriendBean);
		return mcResult;
	}

	/**
	 * 被圈个数
	 * 
	 * @param context
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public static MCResult fansNum(Context context, String memberId)
			throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new FansNumParams(context, memberId).getParams();
		L.i(TAG, "fansNum url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "fansNum str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 圈我的人列表
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult fansList(Context context, String memberId,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.FRIEND_JSON;
		String params = new FansListParams(context, memberId, startRecord,
				maxResults).getParams();
		L.i(TAG, "fansList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "fansList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), FriendBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 加入圈子
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static MCResult joinGroup(Context context, String groupId)
			throws Exception {
		String url = URLProperties.GROUP_JSON;
		String params = new JoinGroupParams(context, groupId).getParams();
		L.i(TAG, "joinGroup url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "joinGroup str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 申请成为管理员
	 * 
	 * @param context
	 * @param groupId
	 * @return 0:异常 1：成功 2：社员不存在 3：圈子不存在 4：已经发过管理员申请 5：已经是有管理圈子权限 6：没有申请管理员资格
	 * @throws Exception
	 */
	public static MCResult applyToBeManager(Context context, String groupId)
			throws Exception {
		String url = URLProperties.GROUP_JSON;
		String params = new ApplyToBeManagerParams(context, groupId)
				.getParams();
		L.i(TAG, "applyToBeManager url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "applyToBeManager str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 搜索成员
	 */
	public static MCResult searchMembersByName(Context context, String name,
			String startRecord, String maxResults) throws Exception {
		String url = URLProperties.MEMBER_JSON;
		String params = new SearchMembersByNameParams(context, name,
				startRecord, maxResults).getParams();
		L.i(TAG, "searchMembersByName url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchMembersByName", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapMemberBean mapBean = (MapMemberBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapMemberBean.class);
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 搜索成员
	 */
	public static MCResult ValidatePhoneForInviteRegister(Context context,
			String phones) throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new ValidatePhoneForInviteRegisterParams(context,
				phones).getParams();
		L.i(TAG, "ValidatePhoneForInviteRegister url=" + url + ",params="
				+ params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "ValidatePhoneForInviteRegister", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		List<Object> inviteRegisterBeans = JsonParseTool.dealListResult(
				mcResult.getResult().toString(), InviteRegisterBean.class);
		mcResult.setResult(inviteRegisterBeans);
		return mcResult;
	}

	/**
	 * 搜索圈子
	 */
	public static MCResult searchGroups(Context context, String searchName,
			String startRecord, String maxResults) throws Exception {
		String url = URLProperties.GROUP_JSON;
		String params = new SearchGroupsParams(context, searchName,
				startRecord, maxResults, false).getParams();
		L.i(TAG, "searchGroups url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchGroups", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), GroupBasicBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 搜索资源
	 * 
	 * @param context
	 * @param keyword
	 * @param pageNo
	 *            当前页码 查询第N页，默认1
	 * @param pageSize
	 *            显示数量 分页显示量，默认值 10
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchResource(Context context, String keyword,
			String GroupID, int pageNo, int pageSize, String action,
			String searchRange, String MemberID) throws Exception {
		String url = URLProperties.SEARCH_JSON;
		String params = new SearchResourceParams(context, keyword, GroupID,
				pageNo, pageSize, action, searchRange, MemberID).getParams();
		L.i(TAG, "searchResource url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers().postRequest(
				url, params));
		L.getLongLog(TAG, "searchResource", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ResultAll resultAll = (ResultAll) JsonParseTool.dealComplexResult(
				mcResult.getResult().toString(), ResultAll.class);
		mcResult.setResult(resultAll);
		return mcResult;
	}

	/**
	 * 搜索圈子：成员 groupSearchMember、话题searchTopics、照片searchPhotos、文件searchFiles
	 */
	public static MCResult searchGroupResource(Context context,
			GroupResourceTypeEnum groupResourceTypeEnum, String groupId,
			String text, String startRecord, String maxResults, String noPaging)
			throws Exception {
		String url = URLProperties.GROUP_JSON;
		String params = "";
		String str = "";
		MCResult mcResult = null;
		switch (groupResourceTypeEnum) {
		case MEMBER:// 成员
			params = new SearchGroupResourceParams(context,
					"groupSearchMember", groupId, text, startRecord,
					maxResults, noPaging).getParams();
			L.i(TAG, "searchGroupResource url=" + url + ",params=" + params);
			str = StreamUtil.readData(new HttpRequestServers().getRequest(url
					+ "?" + params));
			L.getLongLog(TAG, "searchGroupResource", str);
			mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
					MCResult.class);
			if (mcResult == null || mcResult.getResultCode() != 1) {
				return mcResult;
			}
			MapGroupLeaguerBean mapGroupLeaguerBean = (MapGroupLeaguerBean) JsonParseTool
					.dealComplexResult(mcResult.getResult().toString(),
							MapGroupLeaguerBean.class);
			L.i(TAG, "searchGroupResource mapBean.FRIEND_NUM="
					+ mapGroupLeaguerBean.getSEARCHNUM());
			mcResult.setResult(mapGroupLeaguerBean);
			break;
		case TOPIC:// 话题
			params = new SearchGroupResourceParams(context, "searchTopics",
					groupId, text, startRecord, maxResults, noPaging)
					.getParams();
			L.i(TAG, "searchGroupResource url=" + url + ",params=" + params);
			str = StreamUtil.readData(new HttpRequestServers().getRequest(url
					+ "?" + params));
			L.getLongLog(TAG, "searchGroupResource", str);
			mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
					MCResult.class);
			if (mcResult == null || mcResult.getResultCode() != 1) {
				return mcResult;
			}
			MapGroupTopicBean mapGroupTopicBean = (MapGroupTopicBean) JsonParseTool
					.dealComplexResult(mcResult.getResult().toString(),
							MapGroupTopicBean.class);
			L.i(TAG, "searchGroupResource mapBean.FRIEND_NUM="
					+ mapGroupTopicBean.getGROUP_TOPIC_NUM());
			mcResult.setResult(mapGroupTopicBean);
			break;
		case PHOTO:// 照片
			params = new SearchGroupResourceParams(context, "searchPhotos",
					groupId, text, startRecord, maxResults, noPaging)
					.getParams();
			L.i(TAG, "searchGroupResource url=" + url + ",params=" + params);
			str = StreamUtil.readData(new HttpRequestServers().getRequest(url
					+ "?" + params));
			L.getLongLog(TAG, "searchGroupResource", str);
			mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
					MCResult.class);
			if (mcResult == null || mcResult.getResultCode() != 1) {
				return mcResult;
			}
			MapGroupPhotoBean mapGroupPhotoBean = (MapGroupPhotoBean) JsonParseTool
					.dealComplexResult(mcResult.getResult().toString(),
							MapGroupPhotoBean.class);
			L.i(TAG, "searchGroupResource mapBean.FRIEND_NUM="
					+ mapGroupPhotoBean.getTOTALNUM());
			mcResult.setResult(mapGroupPhotoBean);
			break;
		case FILE:// 文件
			params = new SearchGroupResourceParams(context, "searchFiles",
					groupId, text, startRecord, maxResults, noPaging)
					.getParams();
			L.i(TAG, "searchGroupResource url=" + url + ",params=" + params);
			str = StreamUtil.readData(new HttpRequestServers().getRequest(url
					+ "?" + params));
			L.getLongLog(TAG, "searchGroupResource", str);
			mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
					MCResult.class);
			if (mcResult == null || mcResult.getResultCode() != 1) {
				return mcResult;
			}
			MapGroupFileBean mapGroupFileBean = (MapGroupFileBean) JsonParseTool
					.dealComplexResult(mcResult.getResult().toString(),
							MapGroupFileBean.class);
			L.i(TAG, "searchGroupResource mapBean.FRIEND_NUM="
					+ mapGroupFileBean.getFILE_TOTAL_NUM());
			mcResult.setResult(mapGroupFileBean);
			break;
		default:
			break;
		}
		return mcResult;
	}

	/**
	 * 搜索我的朋友
	 */
	public static MCResult searchFriendsForIPhone(Context context, String text,
			String startRecord, String maxResults, String noPaging)
			throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new SearchFriendsForIPhoneParams(context, text,
				startRecord, maxResults, noPaging).getParams();
		L.i(TAG, "searchFriendsForIPhone url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchFriendsForIPhone", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapFriendBean mapBean = (MapFriendBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapFriendBean.class);
		L.i(TAG,
				"searchFriendsForIPhone mapBean.FRIEND_NUM="
						+ mapBean.getFRIEND_NUM());
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 搜索消息
	 */
	public static MCResult searchNotices(Context context, String text,
			String startRecord, String maxResults, String noPaging)
			throws Exception {
		String url = URLProperties.NOTICE_JSON;
		String params = new SearchNoticesParams(context, text, startRecord,
				maxResults, noPaging).getParams();
		L.i(TAG, "searchNotices url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchNotices", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapMessageNoticeBean mapBean = (MapMessageNoticeBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapMessageNoticeBean.class);
		L.i(TAG, "searchNotices mapBean.TOTAL_NUM=" + mapBean.getTOTAL_NUM());
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 搜索圈聊联系人
	 */
	public static MCResult searchGroupChat(Context context, String text,
			String startRecord, String maxResults, String noPaging)
			throws Exception {
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new SearchGroupChatParams(context, text, startRecord,
				maxResults, noPaging).getParams();
		L.i(TAG, "searchMessage url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchMessage", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), MessageContacterBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 搜索私信联系人
	 */
	public static MCResult searchMessage(Context context, String text,
			String startRecord, String maxResults, String noPaging)
			throws Exception {
		String url = URLProperties.MESSAGE_JSON;
		String params = new SearchMessageParams(context, text, startRecord,
				maxResults, noPaging).getParams();
		L.i(TAG, "searchMessage url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchMessage", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), MessageContacterBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

	/**
	 * 获取我的圈聊联系人列表
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult myGroupChatList(final Context context,
			String startRecord, String maxResults, String noPaging)
			throws Exception {
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new MyGroupChatListParams(context, startRecord,
				maxResults, noPaging).getParams();
		L.i(TAG, "myMessageList url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "myMessageList", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				MessageContacterBean.class);
		mcResult.setResult(objectList);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& objectList != null
				&& objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_QUUCHAT, trends);
		}
		return mcResult;
	}

	/**
	 * 获取我的圈聊联系人列表
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult myGroupChatList(final Context context,
			String startRecord, String maxResults, String noPaging,
			long startTime) throws Exception {
		String url = URLProperties.GROUP_CHAT_JSON;
		String params = new MyGroupChatListParams(context, startRecord,
				maxResults, noPaging, startTime).getParams();
		L.i(TAG, "myMessageList url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "myMessageList", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				MessageContacterBean.class);
		mcResult.setResult(objectList);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& objectList != null
				&& objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_QUUCHAT, trends);
		}
		return mcResult;
	}

	/**
	 * 获取我的私信联系人列表
	 */
	public static MCResult myMessageList(final Context context,
			String startRecord, String maxResults) throws Exception {
		String url = URLProperties.MESSAGE_JSON;
		String params = new MyMessageListParams(context, startRecord,
				maxResults).getParams();
		L.i(TAG, "myMessageList url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "myMessageList", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				MessageContacterBean.class);
		mcResult.setResult(objectList);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& objectList != null
				&& objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_PLETTER, trends);
		}
		return mcResult;
	}

	/**
	 * 获取我与某人的私信列表
	 * 
	 * @param context
	 * @param friendId
	 * @param contactLeaguerId
	 * @param startRecord
	 * @return
	 * @throws Exception
	 */
	public static MCResult contactMemberMessages(final Context context,
			final String friendId, String startRecord, String maxResults,
			boolean unRead, String startMessagesId) throws Exception {
		String url = URLProperties.MESSAGE_JSON;
		String method = "";
		if (startMessagesId != null && !"".equals(startMessagesId)
				&& !"0".equals(startMessagesId)) {
			method = "contactMemberHistoryMessages";
		} else {
			if (unRead) {
				// method = "contactMemberUnreadMessages";
				method = "getUnreadMessagesWithMemberId";
			} else {
				// method = "contactMemberMessages";
				method = "getContactMemberMessagesByMember";
			}
		}

		String params = new ContactMemberMessagesParams(context, method,
				friendId, startRecord, maxResults, startMessagesId).getParams();

		L.i(TAG, "contactMemberMessages url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "contactMemberMessages", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				MessageBean.class);
		mcResult.setResult(objectList);

		if (objectList != null && objectList.size() > 0) {
			ChatMessageBeanService.getService(context).save(objectList,
					startMessagesId, unRead);
		}

		return mcResult;
	}

	/**
	 * 发私信
	 * 
	 * @param context
	 * @param friendMemberIds
	 *            可选-接受者的用户的ID
	 * @param friendGroupIds
	 *            可选-朋友分组
	 * @param phones
	 *            可选-接收者的手机号码 接受者如果需要输入名字可以用分隔符分开，例如：13810116246#MC#名字
	 * @param emails
	 *            可选-接收者的email
	 * @param messageContent
	 * @param isPlainText
	 * @param type
	 *            OBJ_TEXT/OBJ_PHOTO/OBJ_VOICE
	 * @param l
	 * @param uploadName
	 * @return
	 * @throws Exception
	 */
	public static MCResult sendMessage(final Context context,
			final String[] friendMemberIds, String[] friendGroupIds,
			String[] phones, String[] emails, String messageContent,
			String isPlainText, String type, String l, String uploadName,
			String uri, String path) throws Exception {
		String url = URLProperties.MESSAGE_JSON;
		String params = new SendMessageParams(context, friendMemberIds,
				friendGroupIds, phones, emails, messageContent, true,
				isPlainText, type, l, uploadName, uri, path).getParams();
		L.d(TAG, "sendMessage url=" + url + "?" + params);
		String str = StreamUtil.readData(new HttpRequestServers().postRequest(
				url, params));
		L.d(TAG, "sendMessage str=" + str);
		new Thread() {
			public void run() {
				FriendListService.getService(context).saveContactTime(
						friendMemberIds);
			};
		}.start();
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 获取招呼列表
	 */
	public static MCResult greetInfoList(final Context context,
			String startRecord, String maxResults) throws Exception {
		String url = URLProperties.MESSAGE_JSON;
		String params = new GreetInfoListParams(context, startRecord,
				maxResults).getParams();
		L.i(TAG, "greetInfoList url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "greetInfoList", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				MessageGreetBean.class);
		mcResult.setResult(objectList);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& objectList != null
				&& objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_GREET, trends);
		}
		return mcResult;
	}

	/**
	 * 打招呼
	 * 
	 * @param context
	 * @param memberId
	 *            打招呼对象社员ID
	 * @param noticeGreetId
	 *            回复招呼：招呼表主键 去个人地盘打招呼：0
	 * @param greetConfigId
	 *            打招呼配置表主键
	 * @return
	 * @throws Exception
	 */
	public static MCResult greetMamber(final Context context,
			final String memberId, String noticeGreetId, String greetConfigId)
			throws Exception {
		String url = URLProperties.MESSAGE_JSON;
		String params = new GreetMemberParams(context, memberId, noticeGreetId,
				greetConfigId).getParams();
		L.i(TAG, "greetMamber url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "greetMamber", str);
		new Thread() {
			public void run() {
				FriendListService.getService(context).saveContactTime(
						new String[] { memberId });
			};
		}.start();
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 获取新通知
	 * 
	 * @param context
	 * @param timeStamp
	 * @return
	 * @throws Exception
	 */
	public static MCResult getNewNotice(Context context, String timeStamp)
			throws Exception {
		String uriAPI = URLProperties.SPIDER_JSON;
		String paramStr = new GetNoticeParams(context, timeStamp).getParams();
		L.i(TAG, "getNewNotice url=" + uriAPI + "?" + paramStr);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(uriAPI + "?" + paramStr));
		L.getLongLog(TAG, "getNewNotice", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取通知列表
	 */
	public static MCResult noticeList(final Context context,
			String startRecord, String maxResults) throws Exception {
		String url = URLProperties.NOTICE_JSON;
		String params = new NoticeListParams(context, "1", "false",
				startRecord, maxResults).getParams();
		L.i(TAG, "noticeList url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "noticeList", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		MapMessageNoticeBean mapBean = (MapMessageNoticeBean) JsonParseTool
				.dealComplexResult(trends, MapMessageNoticeBean.class);
		L.i(TAG, "noticeList mapBean.TOTAL_NUM=" + mapBean.getTOTAL_NUM());
		mcResult.setResult(mapBean);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& mapBean != null
				&& mapBean.getTOTAL_NUM() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_NOTICE, trends);
		}
		return mcResult;
	}

	/**
	 * 获取通知列表
	 */
	public static MCResult newNotices(final Context context,
			String startRecord, String maxResults, String time)
			throws Exception {
		String url = URLProperties.NOTICE_JSON;
		String params = new NewNoticeParams(context, "1", "false", startRecord,
				maxResults, "all", time).getParams();
		L.i(TAG, "noticeList url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "noticeList", str);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		MapMessageNoticeBean mapBean = (MapMessageNoticeBean) JsonParseTool
				.dealComplexResult(trends, MapMessageNoticeBean.class);
		L.i(TAG, "noticeList mapBean.TOTAL_NUM=" + mapBean.getTOTAL_NUM());
		mcResult.setResult(mapBean);

		return mcResult;
	}

	/**
	 * 回应加入圈子申请、回应管理员申请
	 * 
	 * @param context
	 * @param method
	 *            加入圈子：replyJoinApply、管理员申请：replyManagerApply
	 * @param groupId
	 * @param memberId
	 * @param replyType
	 *            回应类型：1同意 2忽略
	 * @return
	 * @throws Exception
	 */
	public static MCResult replyGroupApply(Context context, String method,
			String groupId, String memberId, int replyType) throws Exception {
		String url = URLProperties.NOTICE_JSON;
		String params = new ReplyGroupApplyParams(context, method, groupId,
				memberId, replyType).getParams();
		L.i(TAG, "replyJoinApply url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "replyJoinApply", str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * replyCollaborate回应圈子合作申请 replySubGroupApply回应下级圈子申请 replyCombine回应合并申请
	 * applyToBeSubGroup申请成为下级圈子
	 * 
	 * @param context
	 * @param method
	 * @param srcGroupId
	 * @param destGroupId
	 * @param isAgree
	 * @return
	 * @throws Exception
	 */
	public static MCResult replyGroupOper(Context context, String method,
			String srcGroupId, String destGroupId, boolean isAgree)
			throws Exception {
		String url = URLProperties.NOTICE_JSON;
		String params = new ReplyGroupOperParams(context, method, srcGroupId,
				destGroupId, isAgree).getParams();
		L.i(TAG, "replyGroupOper url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "replyGroupOper", str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 删除私信
	 * 
	 * @param context
	 * @param messageIds
	 * @return
	 * @throws Exception
	 */
	public static MCResult deleteMessages(Context context, String[] messageIds)
			throws Exception {
		String url = URLProperties.MESSAGE_JSON;
		String params = new DeleteMessagesParams(context, messageIds)
				.getParams();
		L.i(TAG, "deleteMessages url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteMessages", str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 删除通知
	 * 
	 * @param context
	 * @param noticeId
	 * @return
	 * @throws Exception
	 */
	public static MCResult deleteNotices(Context context, String noticeId)
			throws Exception {
		String url = URLProperties.NOTICE_JSON;
		String params = new DeleteNoticesParams(context, noticeId).getParams();
		L.i(TAG, "deleteNotices url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteNotices", str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 删除招呼
	 * 
	 * @param context
	 * @param noticeId
	 * @return
	 * @throws Exception
	 */
	public static MCResult deleteGreets(Context context, String noticeGreetId)
			throws Exception {
		String url = URLProperties.MESSAGE_JSON;
		String params = new DeleteGreetParams(context, noticeGreetId)
				.getParams();
		L.i(TAG, "deleteGreets url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteGreets", str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 删除与某人之间秘信
	 * 
	 * @param context
	 * @param noticeId
	 * @return
	 * @throws Exception
	 */
	public static MCResult deleteMessageContacters(Context context,
			String[] contactLeaguerIds) throws Exception {
		String url = URLProperties.MESSAGE_JSON;
		String params = new DeleteMessageContactersParams(context,
				contactLeaguerIds).getParams();
		L.i(TAG, "deleteMessageContacters url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteMessageContacters", str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 留言
	 */
	public static MCResult leaveGuestBookWord(final Context context,
			final String memberId, String content) throws Exception {
		String url = URLProperties.MEMBER_JSON;
		String params = new LeaveGuestBookWordParams(context, memberId, content)
				.getParams();
		L.i(TAG, "leaveGuestBookWord url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "leaveGuestBookWord", str);
		new Thread() {
			public void run() {
				FriendListService.getService(context).saveContactTime(
						new String[] { memberId });
			};
		}.start();
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 备份联系人
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static Object[] backupContacts(Context context, Handler handler)
			throws Exception {
		BackupPhoneBookParams backupPhoneBookParams = new BackupPhoneBookParams(
				context, handler);
		String params = backupPhoneBookParams.getParams();
		L.getLongLog(TAG, "backupContacts", params);
		String url = URLProperties.SPIDER_JSON;

		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.d(TAG, "backupContacts result=" + result);
		Object[] objects = new Object[2];
		objects[0] = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		objects[1] = backupPhoneBookParams.getBackupCount();
		return objects;
	}

	/**
	 * 获得备份时间列表
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static Object[] getBackupTimes(Context context, boolean noPaging,
			int startNum) throws Exception {
		Object[] objects = new Object[2];
		String url = URLProperties.SPIDER_JSON;
		String maxNum = "";
		if (noPaging) {
			maxNum = "5";
		} else {
			maxNum = "1";
		}
		String params = new GetRestorePointsParams(context, "" + startNum,
				maxNum, "" + false).getParams();
		// noPaging 是否不分页 true：不分页 false：分页
		L.i(TAG, "getBackupTimes : url = " + url + "?" + params);

		String resultStr = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getBackupTimes", resultStr);

		JSONObject jsonObject = new JSONObject(resultStr);
		JSONArray jsonArray = jsonObject.getJSONArray("result");
		ArrayList<BackupContactsInfo> contactInfos = null;
		if (jsonArray != null) {
			int size = jsonArray.length();
			// if (size > 0) {
			contactInfos = new ArrayList<BackupContactsInfo>();
			// }
			for (int i = 0; i < size; i++) {
				try {
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					JSONObject jsonObject3 = new JSONObject(
							jsonObject2.getString("backupTime"));
					BackupContactsInfo contactInfo = new BackupContactsInfo(
							jsonObject2.getString("deviceName"),
							jsonObject3.getLong("time"),
							jsonObject2.getInt("contactNum"),
							jsonObject2.getString("backupId"));
					contactInfo.setDeviceImei(jsonObject2
							.getString("deviceImei"));
					contactInfos.add(contactInfo);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		objects[0] = jsonObject.getInt("resultCode");
		objects[1] = contactInfos;
		return objects;
	}

	/**
	 * 删除某个时间点备份的联系人
	 * 
	 * @throws Exception
	 */
	public static MCResult deleteBackupContacts(Context context, String pointId)
			throws Exception {
		String url = URLProperties.SPIDER_JSON;
		String params = new GetContactsByPointIdParams(context,
				"deleteContactsByPointId", pointId).getParams();
		L.i(TAG, "deleteBackupContacts : url = " + url + "?" + params);

		String resultStr = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "deleteBackupContacts", resultStr);
		return (MCResult) JsonParseTool.dealSingleResult(resultStr,
				MCResult.class);
	}

	/**
	 * 获得某个时间点备份的联系人
	 * 
	 * @throws Exception
	 */
	public static Object[] getBackupContacts(Context context, String pointId)
			throws Exception {
		Object[] objects = new Object[2];
		String url = URLProperties.SPIDER_JSON;
		String params = new GetContactsByPointIdParams(context,
				"getcontactsbypointid", pointId).getParams();
		L.i(TAG, "getBackupContacts : url = " + url + "?" + params);

		String resultStr = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getBackupContacts", resultStr);
		JSONObject jsonObject = new JSONObject(resultStr);
		JSONArray jsonArray = jsonObject.getJSONArray("result");
		ArrayList<ContactEntity> contactEntities = null;
		if (jsonArray != null) {
			int size = jsonArray.length();
			if (size > 0) {
				contactEntities = new ArrayList<ContactEntity>();
				for (int i = 0; i < size; i++) {
					try {
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						ContactEntity entity = new ContactEntity(
								jsonObject2.getString("contactName"));
						entity.buildNumbers(jsonObject2
								.getString("contactPhone"));
						// entity.buildEmails(jsonObject2.getString("contactMail"));
						// entity.buildInfo(jsonObject2.getString("contactOther"));
						contactEntities.add(entity);
						entity = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		objects[0] = jsonObject.getInt("resultCode");
		objects[1] = contactEntities;
		return objects;
	}

	/**
	 * 上传通信录、圈人(朋友圈、交流圈)
	 * 
	 * @param context
	 * @param phones
	 * @param fIds
	 *            多个朋友圈时用英文逗号分隔 如：15831567497,18246579785 没有时传null或""
	 * @param groupId
	 *            交流圈ID 没有时传0
	 * @return 0：系统异常 1：可以发送邀请短信 2：手机号码错误：为空，或者不符合正则 3：visitId不存在 4：手机号已绑定
	 *         5:friendGroupIdS不存在 6:groupId不合法 7:visitId没有邀请权限 8:传递的性别参数不合法
	 * @throws Exception
	 */
	public static MCResult uploadPhoneBook(Context context, String[] phones,
			String[] fIds, String groupId) throws Exception {

		String url = null;
		if (phones == null) {
			url = URLProperties.SPIDER_JSON;
		} else {
			url = URLProperties.FRIEND_JSON;
		}

		UploadAddressBookParams bookParams = new UploadAddressBookParams(
				context, phones, fIds, groupId);
		String params = bookParams.getParams();
		L.getLongLog(TAG, "uploadPhoneBook", params);

		if (phones == null && bookParams.getUploadCount() == 0) {
			return null;
		}

		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.d(TAG, "uploadPhoneBook result=" + result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * email邀请朋友
	 * 
	 * @param context
	 * @param emails
	 * @return
	 * @throws Exception
	 */
	public static MCResult emailInviteFriend(Context context, String emails)
			throws Exception {
		String url = URLProperties.FRIEND_JSON;
		String params = new EmailInviteFriendParams(context, emails)
				.getParams();
		L.i(TAG, "emailInviteFriend " + params);

		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.d(TAG, "emailInviteFriend result=" + result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获得通信录
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static ArrayList<ContactInfo> getaddressBook(Context context)
			throws Exception {
		ArrayList<ContactInfo> contactInfos = null;
		String url = URLProperties.SPIDER_JSON;
		String params = new GetFriendListParams(context,
				"getPhonebookContacters", "0", "10", "true").getParams();
		// noPaging 是否不分页 true：不分页 false：分页
		L.i(TAG, "getaddressBook : url = " + url + "?" + params);

		String resultStr = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "getaddressBook", resultStr);

		JSONObject jsonObject = new JSONObject(resultStr);
		String resultStaus = jsonObject.getString("resultStaus");
		L.d(TAG, "getaddressBook : resultStaus = " + resultStaus);

		JSONArray jsonArray = jsonObject.getJSONArray("result");
		if (jsonArray == null)
			return contactInfos;
		contactInfos = new ArrayList<ContactInfo>();
		int size = jsonArray.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
				ContactInfo contactInfo = new ContactInfo(
						jsonObject2.getString("contactName"),
						jsonObject2.getString("contactMemberPhone"));
				// 联系人的朋友状态：1、是朋友 2、申请加为朋友;非朋友 3
				contactInfo.setRegisterStatus(jsonObject2
						.getString("contactMemberRegisterStatus"));
				contactInfo.setFriendStatus(jsonObject2
						.getString("contactMemberFriendStatus"));
				contactInfo.setContactMemberId(jsonObject2
						.getInt("contactMemberId"));
				contactInfo.setMemberHead(jsonObject2
						.getString("contactMemberHeadUrl")
						+ jsonObject2.getString("contactMemberHeadPath"));
				contactInfos.add(contactInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ContactsBookService bookService = new ContactsBookService(context);
		bookService.delete();
		bookService.save(contactInfos);
		return contactInfos;
	}

	/**
	 * 上传——头像、海报、云文件
	 * 
	 * @param context
	 * @param file
	 * @param uploadMethodEnum
	 * @param groupId
	 */
	public static void uploadFile(Context context, File file,
			UploadMethodEnum uploadMethodEnum, String groupId) {
		String uploadUrl = null;
		String tip = null;
		switch (uploadMethodEnum) {
		case UPLOADHEAD:// 个人头像
			tip = "个人头像";
			uploadUrl = URLProperties.MEMBER_JSON
			// .replace("https", "http")
					+ "?"
					+ new UploadFileParams(context, "uploadHead",
							file.getName(), null).getParams();
			break;
		case UPLOADPOSTER:// 圈子海报
			tip = "圈子海报";
			uploadUrl = URLProperties.GROUP_JSON
			// .replace("https", "http")
					+ "?"
					+ new UploadFileParams(context, "uploadPoster",
							file.getName(), groupId).getParams();
			break;
		case UPLOADFILE:// 云文件
			tip = "个人文件";
			uploadUrl = URLProperties.FILE_JSON
			// .replace("https", "http")
					+ "?"
					+ new UploadFileParams(context, "fileUpload",
							file.getName(), null).getParams();
			break;
		case GROUPCHATPHOTO:
			tip = "圈聊照片";
			uploadUrl = URLProperties.GROUP_CHAT_JSON
					// .replace("https", "http")
					+ "?"
					+ new UploadFileParams(context, "uploadGroupChatImage",
							null, null).getParams();
			break;
		default:
			break;
		}
		new UploadFileThread(context, file, uploadUrl, tip, groupId).start();
	}

	/**
	 * 获取个人、圈子文件下载地址
	 * 
	 * @param context
	 * @param groupId
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public static MCResult fileDownloadPath(Context context, String groupId,
			String fileId) throws Exception {
		L.d(TAG, "fileDownloadPath fileId=" + fileId + ",groupId=" + groupId);

		String uriAPI = null;
		if (groupId != null) {
			uriAPI = URLProperties.GROUPFILE_JSON;
		} else {
			uriAPI = URLProperties.FILE_JSON;
		}

		String url = uriAPI
				+ "?"
				+ new GetFileDownloadPathParams(context, groupId, fileId)
						.getParams();
		L.i(TAG, "fileDownloadPath url=" + url);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url));
		L.i(TAG, "fileDownloadPath str:" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 获取圈博、邮件中文件下载地址
	 * 
	 * @param context
	 * @param groupId
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupTopicFileDownloadPath(Context context,
			String fileUrl, String filePath, String fileName) throws Exception {
		L.d(TAG, "groupTopicFileDownloadPath fileUrl=" + fileUrl + ",filePath="
				+ filePath + ",fileName=" + fileName);
		String uriAPI = URLProperties.GROUP_TOPIC_JSON;

		String url = uriAPI
				+ "?"
				+ new GetGroupTopicFileDownloadPathParams(context, fileUrl,
						filePath, fileName).getParams();
		L.i(TAG, "groupTopicFileDownloadPath url:" + url);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url));
		L.i(TAG, "groupTopicFileDownloadPath str:" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 获取成员皮肤
	 * 
	 * @param context
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public static MCResult memberSkin(Context context, String memberId)
			throws Exception {
		String uriAPI = URLProperties.MEMBER_JSON;

		String url = uriAPI
				+ "?"
				+ new BackgroundSkinParams(context, memberId, "memberSkin")
						.getParams();
		L.i(TAG, "memberSkin url:" + url);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url));
		L.i(TAG, "memberSkin str:" + str);
		// {"result":"http://img.yuuquu.com/mc/default/group/iphone/group_back_ground_img_14.jpg",
		// "resultStaus":true,"resultCode":1,"resultMessage":"","version":"v1.0"}
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 获取圈子皮肤
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupSkin(Context context, String groupId)
			throws Exception {
		String uriAPI = URLProperties.GROUP_JSON;

		String url = uriAPI
				+ "?"
				+ new BackgroundSkinParams(context, groupId, "groupSkin")
						.getParams();
		L.i(TAG, "groupSkin url:" + url);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url));
		L.i(TAG, "groupSkin str:" + str);
		// {"result":"http://img.yuuquu.com/mc/default/member/iphone/member_back_ground_img_17.jpg",
		// "resultStaus":true,"resultCode":1,"resultMessage":"","version":"v1.0"}
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 获取第三方应用列表
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static MCResult thirdInfoList(Context context) throws Exception {
		String uriAPI = URLProperties.SPIDER_JSON;

		String url = uriAPI + "?"
				+ new ThirdInfoListParams(context, "thirdList").getParams();
		L.i(TAG, "thirdInfoList url:" + url);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url));
		L.i(TAG, "thirdInfoList str:" + str);

		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		MapThirdInfoListBean mapBean = (MapThirdInfoListBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapThirdInfoListBean.class);
		L.i(TAG, "thirdInfoList mapBean.NUM=" + mapBean.getNUM());
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 一键分享到第三方
	 * 
	 * @param context
	 * @param content
	 * @param objectPic
	 * @param thirdIds
	 * @param temp
	 * @param groupId
	 * @param objectId
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareThirdNew(Context context, String content,
			String objectPic, String[] thirdIds, String temp, String groupId,
			String objectId) throws Exception {
		String uriAPI = URLProperties.SPIDER_JSON;

		String url = uriAPI
				+ "?"
				+ new ShareThirdNewParams(context, content, objectPic,
						thirdIds, temp, groupId, objectId).getParams();
		L.i(TAG, "shareThirdNew url=" + url);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url));
		L.i(TAG, "shareThirdNew str=" + str);

		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		String re = mcResult.getResult().toString();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		if (re != null) {
			JSONArray array = new JSONArray(re);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				map.put(object.getString("TYPE"), object.getInt("FLAG"));
			}
		}
		mcResult.setResult(map);
		return mcResult;
	}

	/**
	 * 第三方分享统计
	 * 
	 * @param context
	 * @param objectType
	 *            1:圈博 2：圈子照片 3：社员心情
	 * @param objectId
	 * @param thirdType
	 *            1:新浪 2：腾讯 3：人人 4：开心 5：搜狐 6：网易 7：豆瓣 8：微信
	 * @return
	 * @throws Exception
	 */
	public static MCResult shareToThirdRecord(Context context,
			String objectType, String objectId, String thirdType)
			throws Exception {
		String uriAPI = URLProperties.MEMBER_JSON;

		String url = uriAPI
				+ "?"
				+ new ShareToThirdRecordParams(context, objectType, objectId,
						thirdType).getParams();
		L.i(TAG, "shareToThirdRecord url=" + url);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url));
		L.i(TAG, "shareToThirdRecord str=" + str);

		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 检测新版本
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getNewVersion(Context context)
			throws Exception {
		Map<String, String> result = null;
		String url = URLProperties.SPIDER_JSON;
		String params = new GetNewVersionParams(context).getParams();
		L.i(TAG, "getNewVersion url=" + url + ",params=" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "getNewVersion str=" + str);
		if (str != null && !"".equals(str)) {
			result = new HashMap<String, String>();
			JSONObject jsonObject = new JSONObject(str);
			if (jsonObject.getInt("resultCode") != 1) {
				result.put("isUpdate", "false");
				return result;
			}

			// 获取服务器最新版本（当当前版本小于服务器版本时提示升级）
			String versionName = jsonObject.getJSONObject("result").getString(
					"versionName");
			String currentVersion = new SoftPhoneInfo(context).getVersionName();
			L.i(TAG, "getNewVersion versionName:" + versionName);
			L.i(TAG, "getNewVersion currentVersion:" + currentVersion);
			// 匹配升级版本号
			boolean isUpdate = VersionUtil
					.isUpdate(currentVersion, versionName);
			L.i(TAG, "getNewVersion isUpdate:" + isUpdate);
			if (!isUpdate) {
				result.put("isUpdate", "false");
				return result;
			}

			String versionURL = jsonObject.getJSONObject("result").getString(
					"versionUrl");
			String versionPath = jsonObject.getJSONObject("result").getString(
					"versionPath");
			String versionDesc = jsonObject.getJSONObject("result").getString(
					"versionDesc");

			result.put("versionName", versionName);
			result.put("fileUrl", versionURL + versionPath);
			result.put("isUpdate", "true");
			result.put("versionDesc", versionDesc);
			L.i(TAG, "getNewVersion fileUrl=" + versionURL + versionPath);
			L.i(TAG, "getNewVersion versionDesc=" + versionDesc);
		}
		return result;
	}

	/**
	 * 修改密码
	 * 
	 * @param context
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	public static MCResult resetPassword(Context context, String oldPassword,
			String newPassword) throws Exception {
		String url = URLProperties.MEMBER_JSON;
		String params = new UpdatePasswordParams(context, oldPassword,
				newPassword).getParams();
		L.i(TAG, "resetPassword url=" + url + "?" + params);
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.i(TAG, "resetPassword str=" + str);
		return (MCResult) JsonParseTool.dealSingleResult(str, MCResult.class);
	}

	/**
	 * 根据邮箱找回密码
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static MCResult retrievePasswordEmail(Context context, String email)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.MEMBER_JSON;
		String params = new RetrievePasswordEmailParams(context, email)
				.getParams();
		L.i(TAG, "retrievePasswordEmail url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "retrievePasswordEmail", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 根据注册手机号随即产生验证码（忘记密码修改密码时用）
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static MCResult createVerifyCodeByPhone(Context context, String phone)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.MEMBER_JSON;
		String params = new CreateVerifyCodeByPhoneParams(context, phone)
				.getParams();
		L.i(TAG, "createVerifyCodeByPhone url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "createVerifyCodeByPhone", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 手机号登录(不需要输入密码)
	 * 
	 * @param context
	 * @param phone
	 * @param verifyCode
	 * @return
	 * @throws Exception
	 */
	public static MCResult loginJustByPhone(Context context, String phone,
			String verifyCode) throws Exception {
		// 拼接请求参数
		String url = URLProperties.MEMBER_JSON;
		String params = new LoginJustByPhoneParams(context, phone, verifyCode)
				.getParams();
		L.i(TAG, "loginJustByPhone url=" + url + ",params=" + params);
		// 获取数据
		String str = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "loginJustByPhone", str);

		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(str,
				MCResult.class);
		if (mcResult.getResultCode() == 1) {
			LoginJustByPhoneBean bean = (LoginJustByPhoneBean) JsonParseTool
					.dealSingleResult(mcResult.getResult().toString(),
							LoginJustByPhoneBean.class);
			mcResult.setResult(bean);
		}

		return mcResult;
	}

	/**
	 * 忘记重置密码
	 * 
	 * @param context
	 * @param phone
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	public static MCResult forgetResetPassword(Context context, String phone,
			String newPassword) throws Exception {
		// 拼接请求参数
		String url = URLProperties.MEMBER_JSON;
		String params = new ResetPasswordParams(context, phone, newPassword)
				.getParams();
		L.i(TAG, "forgetResetPassword url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "forgetResetPassword", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取朋友所在朋友圈
	 * 
	 * @param context
	 * @param phones
	 * @return
	 * @throws Exception
	 */
	public static MCResult getMyFriendGroupList(Context context, int memberId)
			throws Exception {
		// 拼接请求参数
		MCResult mcResult = null;
		String url = URLProperties.FRIEND_JSON;
		String params = new GetMyFriendGroupListParams(context, memberId)
				.getParams();
		L.i(TAG, "getMyFriendGroupList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "getMyFriendGroupList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(mcResult
				.getResult().toString(), FriendGroupBean.class);
		mcResult.setResult(objectList);
		return mcResult;
	}

}

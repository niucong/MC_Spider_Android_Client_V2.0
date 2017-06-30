package com.datacomo.mc.spider.android.net;

import android.content.Context;
import android.os.AsyncTask;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.db.MailInfoService;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MailBean;
import com.datacomo.mc.spider.android.net.been.map.MapMailContactBean;
import com.datacomo.mc.spider.android.net.been.map.MapMailsByLeaguer;
import com.datacomo.mc.spider.android.params.mail.ContactLeaguersParams;
import com.datacomo.mc.spider.android.params.mail.MailDeleteToRecycleParams;
import com.datacomo.mc.spider.android.params.mail.MailInfoParams;
import com.datacomo.mc.spider.android.params.mail.MailsByLeaguerParams;
import com.datacomo.mc.spider.android.params.mail.SearchMyContactLeaguersAndFriendsParams;
import com.datacomo.mc.spider.android.params.mail.SearchMyContactLeaguersParams;
import com.datacomo.mc.spider.android.params.mail.SendMailParams;
import com.datacomo.mc.spider.android.params.mail.TransferAttachmentParams;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.StreamUtil;

public class APIMailRequestServers {
	private static final String TAG = "APIRequestServers";

	/**
	 * 获取我的联系人
	 * 
	 * @param context
	 * @param startRecord
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static MCResult contactLeaguers(final Context context,
			String startRecord, String pageSize) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.MAIL_JSON;
		String params = new ContactLeaguersParams(context, startRecord,
				pageSize).getParams();
		L.i(TAG, "contactLeaguers url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "contactLeaguers", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		// 解析mailBean
		MapMailContactBean mapBean = (MapMailContactBean) JsonParseTool
				.dealComplexResult(trends, MapMailContactBean.class);
		mcResult.setResult(mapBean);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& mapBean != null
				&& mapBean.getTOTALNUM() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_MAILWITH, trends);
		}

		return mcResult;
	}

	/**
	 * 搜索我的邮件联系人
	 * 
	 * @param context
	 * @param content
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchMyContactLeaguers(Context context,
			String content, String startRecord, String maxResult,
			String noPaging) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.MAIL_JSON;
		String params = new SearchMyContactLeaguersParams(context, content,
				startRecord, maxResult, noPaging).getParams();

		L.i(TAG, "searchMyContactLeaguers url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "searchMyContactLeaguers", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapMailContactBean mapBean = (MapMailContactBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapMailContactBean.class);
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 搜索我的邮件联系人及好友
	 * 
	 * @param context
	 * @param content
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchMyContactLeaguersAndFriends(Context context,
			String content, String startRecord, String maxResult,
			String noPaging, @SuppressWarnings("rawtypes") AsyncTask a)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.MAIL_JSON;
		String params = new SearchMyContactLeaguersAndFriendsParams(context,
				content, startRecord, maxResult, noPaging).getParams();

		L.i(TAG, "searchMyContactLeaguersAndFriends url=" + url + ",params="
				+ params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");
		L.getLongLog(TAG, "searchMyContactLeaguersAndFriends", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		if (a.isCancelled())
			throw new InterruptedException("中断操作 ");
		MapMailContactBean mapBean = (MapMailContactBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapMailContactBean.class);
		mcResult.setResult(mapBean);
		return mcResult;
	}

	/**
	 * 获取我与某个联系人之间的邮件联系记录
	 * 
	 * @param context
	 * @param leaguerId
	 * @param isUpdateRead
	 * @param isRead
	 * @param startRecord
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static MCResult mailsByLeaguer(Context context, String leaguerId,
			String isUpdateRead, String isRead, String startRecord,
			String pageSize) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.MAIL_JSON;
		String params = new MailsByLeaguerParams(context, leaguerId,
				isUpdateRead, isRead, startRecord, pageSize).getParams();
		L.i(TAG, "mailsByLeaguer url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "mailsByLeaguer", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		String list = mcResult.getResult().toString();
		// 解析mailBean
		MapMailsByLeaguer mapBean = (MapMailsByLeaguer) JsonParseTool
				.dealComplexResult(list, MapMailsByLeaguer.class);
		L.i(TAG, "mailsByLeaguer TOTALNUM=" + mapBean.getTOTALNUM());
		mcResult.setResult(mapBean);

		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& mapBean != null
				&& mapBean.getTOTALNUM() > 0) {
			App.app.share.saveStringMessage("mail_list", leaguerId, list);
		}

		return mcResult;
	}

	/**
	 * 发送邮件
	 * 
	 * @param context
	 * @param receiveMailArr
	 *            收件人邮箱列表（已绑定的）：手机号码+@yuuquu.com
	 * @param receiveNotRegistMailArr
	 *            接收者邮箱列表(未绑定的)
	 * @param carbonCopyMailArr
	 *            抄送人邮箱列表（已绑定的）
	 * @param carbonCopyNotRegistMailArr
	 *            抄送人邮箱列表(未绑定的)
	 * @param blindCarbonCopyMailArr
	 *            密送人邮箱列表（已绑定的）
	 * @param blindCarbonCopyNotRegistMailArr
	 *            密送人邮箱列表(未绑定的)
	 * @param mailTitle
	 * @param mailContent
	 * @param attachmentSize
	 * @param attachmentStrs
	 *            附件 name#MC_API#path#MC_API#size
	 * @param sendType
	 *            发送类型：1，发送；2，回复；3，转发
	 * @param sourceMailId
	 *            源邮件编号（回复或转发时传源邮件的编号，否则传0）
	 * @return
	 * @throws Exception
	 */
	public static MCResult sendMail(final Context context,
			final String[] receiveMailArr, String[] receiveNotRegistMailArr,
			final String[] carbonCopyMailArr,
			String[] carbonCopyNotRegistMailArr,
			final String[] blindCarbonCopyMailArr,
			String[] blindCarbonCopyNotRegistMailArr, String mailTitle,
			String mailContent, String attachmentSize, String[] attachmentStrs,
			String sendType, String sourceMailId, String[] fileIds)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.MAIL_JSON;
		String params = new SendMailParams(context, receiveMailArr,
				receiveNotRegistMailArr, carbonCopyMailArr,
				carbonCopyNotRegistMailArr, blindCarbonCopyMailArr,
				blindCarbonCopyNotRegistMailArr, mailTitle, mailContent,
				attachmentSize, attachmentStrs, sendType, sourceMailId, fileIds)
				.getParams();
		L.i(TAG, "sendMail url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.postRequest(url, params));
		L.getLongLog(TAG, "sendMail", result);
		new Thread() {
			public void run() {
				int i = 0;
				if (receiveMailArr != null)
					i = receiveMailArr.length;
				int j = 0;
				if (carbonCopyMailArr != null)
					j = carbonCopyMailArr.length;
				int k = 0;
				if (blindCarbonCopyMailArr != null)
					k = blindCarbonCopyMailArr.length;
				String[] memberIds = new String[i + j + k];
				if (i > 0) {
					for (int l = 0; l < i; l++) {
						memberIds[l] = receiveMailArr[l];
					}
				}
				if (j > 0) {
					for (int l = 0; l < j; l++) {
						memberIds[i + l] = carbonCopyMailArr[l];
					}
				}
				if (k > 0) {
					for (int l = 0; l < k; l++) {
						memberIds[i + j + l] = blindCarbonCopyMailArr[l];
					}
				}
				FriendListService.getService(context)
						.saveContactTime(memberIds);
			};
		}.start();
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取邮件详情
	 * 
	 * @param context
	 * @param mailId
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public static MCResult mailInfo(Context context, String mailId,
			String recordId) throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.MAIL_JSON;
		String params = new MailInfoParams(context, mailId, recordId)
				.getParams();
		L.i(TAG, "mailInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "mailInfo", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		String content = mcResult.getResult().toString();
		// 解析mailBean
		MailBean mailBean = (MailBean) JsonParseTool.dealComplexResult(content,
				MailBean.class);
		mcResult.setResult(mailBean);
		MailInfoService.getService(context).save(Integer.valueOf(mailId),
				Integer.valueOf(recordId), content);
		return mcResult;
	}

	/**
	 * 删除某封邮件到垃圾箱
	 * 
	 * @param context
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public static MCResult mailDeleteToRecycle(Context context,
			String[] recordIds) throws Exception {
		// 拼接请求参数
		String url = URLProperties.MAIL_JSON;
		String params = new MailDeleteToRecycleParams(context, recordIds)
				.getParams();
		L.i(TAG, "mailDeleteToRecycle url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "mailDeleteToRecycle", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 转存附件到个人云文件
	 * 
	 * @param context
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public static MCResult transferAttachment(Context context, String filePath,
			String fileName, String fileSize) throws Exception {
		// 拼接请求参数
		String url = URLProperties.MAIL_JSON;
		String params = new TransferAttachmentParams(context, filePath,
				fileName, fileSize).getParams();
		L.i(TAG, "transferAttachment url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "transferAttachment", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

}

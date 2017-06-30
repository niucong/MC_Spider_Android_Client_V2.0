package com.datacomo.mc.spider.android.params.mail;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;
import com.datacomo.mc.spider.android.url.L;

/**
 * 发送邮件
 * 
 * @author datacomo-160
 * 
 */
public class SendMailParams extends BasicParams {
	private static final String TAG = "SendMailParams";

	/**
	 * 发送邮件参数设置
	 * 
	 * @param context
	 * @param receiveMailArr
	 *            收件人邮箱列表（已绑定的）
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
	 */
	public SendMailParams(Context context, String[] receiveMailArr,
			String[] receiveNotRegistMailArr, String[] carbonCopyMailArr,
			String[] carbonCopyNotRegistMailArr,
			String[] blindCarbonCopyMailArr,
			String[] blindCarbonCopyNotRegistMailArr, String mailTitle,
			String mailContent, String attachmentSize, String[] attachmentStrs,
			String sendType, String sourceMailId, String[] fileIds) {
		super(context);
		setVariable(receiveMailArr, receiveNotRegistMailArr, carbonCopyMailArr,
				carbonCopyNotRegistMailArr, blindCarbonCopyMailArr,
				blindCarbonCopyNotRegistMailArr, mailTitle, mailContent,
				attachmentSize, attachmentStrs, sendType, sourceMailId, fileIds);
	}

	/**
	 * 设置参数
	 **/
	private void setVariable(String[] receiveMailArr,
			String[] receiveNotRegistMailArr, String[] carbonCopyMailArr,
			String[] carbonCopyNotRegistMailArr,
			String[] blindCarbonCopyMailArr,
			String[] blindCarbonCopyNotRegistMailArr, String mailTitle,
			String mailContent, String attachmentSize, String[] attachmentStrs,
			String sendType, String sourceMailId, String[] fileIds) {
		mHashMap = new HashMap<String, String[]>();
		if (receiveMailArr != null && receiveMailArr.length > 0) {
			mHashMap.put("receiveMailArrIds", receiveMailArr);
			paramsMap.put("receiveMailArrIds", "");
		}

		if (receiveNotRegistMailArr != null
				&& receiveNotRegistMailArr.length > 0) {
			mHashMap.put("receiveNotRegistMailArr", receiveNotRegistMailArr);
			paramsMap.put("receiveNotRegistMailArr", "");
		}

		if (carbonCopyMailArr != null && carbonCopyMailArr.length > 0) {
			mHashMap.put("carbonCopyMailArrIds", carbonCopyMailArr);
			paramsMap.put("carbonCopyMailArrIds", "");
		}

		if (carbonCopyNotRegistMailArr != null
				&& carbonCopyNotRegistMailArr.length > 0) {
			mHashMap.put("carbonCopyNotRegistMailArr",
					carbonCopyNotRegistMailArr);
			paramsMap.put("carbonCopyNotRegistMailArr", "");
		}

		if (blindCarbonCopyMailArr != null && blindCarbonCopyMailArr.length > 0) {
			mHashMap.put("blindCarbonCopyMailArrIds", blindCarbonCopyMailArr);
			paramsMap.put("blindCarbonCopyMailArrIds", "");
		}

		if (blindCarbonCopyNotRegistMailArr != null
				&& blindCarbonCopyNotRegistMailArr.length > 0) {
			mHashMap.put("blindCarbonCopyNotRegistMailArr",
					blindCarbonCopyNotRegistMailArr);
			paramsMap.put("blindCarbonCopyNotRegistMailArr", "");
		}

		paramsMap.put("mailTitle", mailTitle);
		paramsMap.put("mailContent", mailContent);
		paramsMap.put("mailhtmlcontent", mailContent);

		L.getLongLog(TAG, "setVariable", mailContent);

		paramsMap.put("attachmentSize", attachmentSize);
		if (attachmentStrs != null && attachmentStrs.length > 0) {
			mHashMap.put("attachmentStrs", attachmentStrs);
			paramsMap.put("attachmentStrs", "");
		}

		paramsMap.put("sendType", sendType);
		paramsMap.put("sourceMailId", sourceMailId);

		if (fileIds != null && fileIds.length > 0) {
			mHashMap.put("fileIds", fileIds);
			paramsMap.put("fileIds", "");
		} else {
			// paramsMap.put("fileIds", "0");
		}

		paramsMap.put("method", "sendMailOfNew");
		super.setMailVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}

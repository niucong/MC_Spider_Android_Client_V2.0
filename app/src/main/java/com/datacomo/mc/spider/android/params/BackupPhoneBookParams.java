package com.datacomo.mc.spider.android.params;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.datacomo.mc.spider.android.bean.ContactEntity;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.ContactsUtil;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;

/**
 * 备份通讯录
 * 
 * @author datacomo-160
 * 
 */

public class BackupPhoneBookParams extends BasicParams {
	private static final String TAG = "BackupPhoneBookParams";
	private Context context;
	private SoftPhoneInfo phoneInfo;
	private int backupCount;

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param phones
	 * @param deviceName
	 * @param imei
	 * @throws JSONException
	 */
	public BackupPhoneBookParams(Context context, Handler handler)
			throws JSONException {
		super(context);
		this.context = context;
		phoneInfo = new SoftPhoneInfo(context);
		setVariables(handler);
	}

	/**
	 * 设置参数
	 * 
	 * @param session_key
	 * @throws JSONException
	 */
	private void setVariables(Handler handler) throws JSONException {
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("phones", getAddressbook(handler));

		paramsMap.put("method", "backupphonebook");
		paramsMap.put("deviceName", phoneInfo.getPhoneModel());
		paramsMap.put("imei", phoneInfo.getPhoneMark());
		paramsMap.put("phones", "");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

	/**
	 * 备份数量
	 * 
	 * @return
	 */
	public int getBackupCount() {
		return backupCount;
	}

	private String[] getAddressbook(Handler handler) throws JSONException {
		ArrayList<ContactEntity> phoneList = ContactsUtil.getPhonebook(context,
				handler);
		backupCount = phoneList.size();
		// if (handler != null) {
		// Message msg = new Message();
		// msg.what = 1;
		// handler.sendMessage(msg);
		// }
		L.d(TAG, "getPhonebook backupCount=" + backupCount);
		String[] phonebooks = new String[backupCount];
		int k = 0;
		for (int i = 0; i < backupCount; i++) {
			ContactEntity entity = phoneList.get(i);
			String name = entity.getName();
			// 电话信息
			StringBuffer phoneBuffer = new StringBuffer(entity.formPhoneJson()
					.toString());
			// StringBuffer emailBuffer = new
			// StringBuffer(entity.formEmailJson()
			// .toString());
			// StringBuffer otherBuffer = new
			// StringBuffer(entity.formOtherJson()
			// .toString());

			StringBuffer contactEntity = new StringBuffer(phoneBuffer
					+ ConstantUtil.PHONE_SEPARATOR + name
			// + ConstantUtil.PHONE_SEPARATOR + emailBuffer
			// + ConstantUtil.PHONE_SEPARATOR + otherBuffer
			);
			L.d(TAG, "getPhonebook contactEntity=" + contactEntity);
			phonebooks[k] = new String(contactEntity);
			k++;
		}
		return phonebooks;
	}

}

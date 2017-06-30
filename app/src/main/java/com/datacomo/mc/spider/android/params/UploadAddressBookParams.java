package com.datacomo.mc.spider.android.params;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.bean.CPhone;
import com.datacomo.mc.spider.android.bean.ContactEntity;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.ContactsUtil;

/**
 * 上传客户端通讯录信息
 * 
 * @author datacomo-160
 * 
 */

public class UploadAddressBookParams extends BasicParams {
	private static final String TAG = "UploadAddressBookParams";
	private Context context;

	private int uploadCount = 0;

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param phones
	 * @param groupIds
	 * @param groupId
	 */
	public UploadAddressBookParams(Context context, String[] phones,
			String[] groupIds, String groupId) {
		super(context);
		this.context = context;
		setVariables(phones, groupIds, groupId);
	}

	/**
	 * 设置参数
	 * 
	 * @param session_key
	 */
	private void setVariables(String[] phones, String[] groupIds, String groupId) {
		mHashMap = new HashMap<String, String[]>();
		if (phones == null) {
			mHashMap.put("phones", getPhonebook());
			paramsMap.put("method", "uploadPhoneBook");
			paramsMap.put("phones", "");
		} else {
			uploadCount = phones.length;
			// mHashMap.put("phones", phones);
			// paramsMap.put("phones", "");
			if (groupIds != null) {
				// mHashMap.put("groupIds", groupIds);
				// paramsMap.put("groupIds", "");
				mHashMap.put("fIds", groupIds);
				paramsMap.put("fIds", "");
			}
			if (groupId != null)
				paramsMap.put("groupId", groupId);
			// paramsMap.put("method", "addPhoneAsFriend");
			mHashMap.put("namePhone", phones);
			paramsMap.put("namePhone", "");
			paramsMap.put("method", "invitePhoneAsFriend");
		}
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

	/**
	 * 上传数量
	 * 
	 * @return
	 */
	public int getUploadCount() {
		return uploadCount;
	}

	/**
	 * 获取通讯录数组
	 * 
	 * @return
	 */
	private String[] getPhonebook() {
		ArrayList<ContactEntity> phoneList = ContactsUtil.getPhonebook(context,
				null);
		int size = phoneList.size();
		uploadCount = size;
		L.d(TAG, "getPhonebook size=" + size);
		String[] phonebooks = new String[ContactsUtil
				.getAllPhonesCount(phoneList)];
		int j = 0;
		for (int i = 0; i < size; i++) {
			ContactEntity entity = phoneList.get(i);
			String name = entity.getName();
			String defEmail = entity.getDefEmail();
			ArrayList<CPhone> phones = entity.getNumbers();
			for (CPhone phone : phones) {
				StringBuffer contactEntity = new StringBuffer(phone.getPhone()
						+ ConstantUtil.PHONE_SEPARATOR + name);
				if (null != defEmail && !"".equals(defEmail)) {
					contactEntity.append(ConstantUtil.PHONE_SEPARATOR
							+ defEmail);
				}
				phonebooks[j] = new String(contactEntity);
				j++;
			}
		}
		return phonebooks;
	}
}

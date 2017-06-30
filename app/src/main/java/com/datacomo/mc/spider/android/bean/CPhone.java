package com.datacomo.mc.spider.android.bean;

import org.json.JSONException;
import org.json.JSONObject;

import android.provider.ContactsContract.CommonDataKinds.Phone;

public class CPhone implements JsonString {

	String phone;
	Integer type = Phone.TYPE_WORK;
	String label = "CUSTOM";

	public CPhone(String phone, Integer type) {
		this.phone = phone;
		if (null != type) {
			this.type = type;
		}
	}

	public String getPhone() {
		if (phone != null)
			phone = phone.replace(" ", "");
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		if (type == null) {
			return;
		}
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toJsonString() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("phone", phone);
		jo.put("type", type);
		jo.put("label", label);
		return jo.toString();
	}

}

package com.datacomo.mc.spider.android.bean;

import org.json.JSONException;
import org.json.JSONObject;

import android.provider.ContactsContract.CommonDataKinds.Email;

public class CEmail implements JsonString{
	String email;
	Integer type = Email.TYPE_WORK;
	String label = "CUSTOM";
	
	public CEmail(String email, Integer type) {
		this.email = email;
		if(null != type){
			this.type = type;
		}
	}
	
	public String getEmail() {
		return email;
	}
	public Integer getType() {
		return type;
		
	}
	public void setType(Integer type) {
		if(null == type){
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
		jo.put("email", email);
		jo.put("type", type);
		jo.put("label", label);
		return jo.toString();
	}
	
}

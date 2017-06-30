package com.datacomo.mc.spider.android.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.datacomo.mc.spider.android.url.L;

public class ContactEntity implements JsonString {
	private static final String TAG = "ContactEntity";

	private String contactId;
	private String name;
	private ArrayList<CPhone> numbers;
	private ArrayList<CEmail> emails;
	private ArrayList<CAddress> addrs;

	private String note = "";
	private String orgName = "";
	private String orgTitle = "";

	private int starred;

	public ContactEntity(String name) {
		this.name = name;
	}

	public ContactEntity(String name, String number) {
		this.name = name;
		CPhone phone = new CPhone(number, null);
		numbers = new ArrayList<CPhone>();
		numbers.add(phone);
	}

	public ArrayList<CAddress> getAddrs() {
		if (addrs == null) {
			addrs = new ArrayList<CAddress>();
		}
		return addrs;
	}

	public void setAddrs(ArrayList<CAddress> addrs) {
		this.addrs = addrs;
	}

	public String getName() {
		return name;
	}

	public ArrayList<CPhone> getNumbers() {
		if (numbers == null) {
			numbers = new ArrayList<CPhone>();
		}
		return numbers;
	}

	public void setNumbers(ArrayList<CPhone> numbers) {
		this.numbers = numbers;
	}

	public ArrayList<CEmail> getEmails() {
		if (emails == null) {
			emails = new ArrayList<CEmail>();
		}
		return emails;
	}

	public void setEmails(ArrayList<CEmail> emails) {
		this.emails = emails;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgTitle() {
		return orgTitle;
	}

	public void setOrgTitle(String orgTitle) {
		this.orgTitle = orgTitle;
	}

	public int getStarred() {
		return starred;
	}

	public void setStarred(int starred) {
		this.starred = starred;
	}

	public String getDefEmail() {
		if (null != emails && emails.size() > 0) {
			return emails.get(0).getEmail();
		}
		return null;
	}

	@Override
	public String toString() {
		L.i(TAG, "ContactEntity=====>>>book: name=" + this.getName());
		L.i(TAG, " id=" + this.contactId);
		L.i(TAG, "----> phones:");
		for (int i = 0; i < numbers.size(); i++) {
			L.i(TAG, " phone" + i + ":  " + numbers.get(i).getPhone());
			L.i(TAG, " phoneType" + i + ":  " + numbers.get(i).getType());
			L.i(TAG, " phoneLable" + i + ":  " + numbers.get(i).getLabel());
		}

		L.i(TAG, "----> emails:");
		for (int i = 0; i < emails.size(); i++) {
			L.i(TAG, " email" + i + ":  " + emails.get(i).getEmail());
			L.i(TAG, " emailType" + i + ":  " + emails.get(i).getType());
			L.i(TAG, " emailLable" + i + ":  " + emails.get(i).getLabel());
		}

		L.i(TAG, "----> addr:");
		for (int i = 0; i < addrs.size(); i++) {
			L.i(TAG, " addrType" + i + ":  " + emails.get(i).getType());
			L.i(TAG, " addr" + i + ":  " + addrs.get(i).toString());
		}

		L.i(TAG, " note=" + this.note);
		L.i(TAG, " orgTitle=" + this.orgTitle);
		L.i(TAG, " orgName=" + this.orgName);
		return super.toString();
	}

	@Override
	public String toJsonString() throws JSONException {
		JSONObject jo = formOtherJson();
		jo.put("name", name);
		JSONArray phonesArray = new JSONArray();
		for (int i = 0; i < addrs.size(); i++) {
			phonesArray.put(i, new JSONObject(addrs.get(i).toJsonString()));
		}
		jo.put("addrs", phonesArray);

		JSONArray emailsArray = new JSONArray();
		for (int i = 0; i < addrs.size(); i++) {
			emailsArray.put(i, new JSONObject(addrs.get(i).toJsonString()));
		}

		jo.put("addrs", emailsArray);

		return jo.toString();
	}

	public JSONObject formOtherJson() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("orgTitle", orgTitle);
		jo.put("orgName", orgName);
		jo.put("starred", starred);
		jo.put("contactId", contactId);
		jo.put("note", note);

		JSONArray array = new JSONArray();
		for (int i = 0; i < getAddrs().size(); i++) {
			array.put(i, new JSONObject(addrs.get(i).toJsonString()));
		}
		jo.put("addrs", array);
		return jo;
	}

	public JSONObject formPhoneJson() throws JSONException {
		JSONObject jo = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < getNumbers().size(); i++) {
			array.put(i, new JSONObject(numbers.get(i).toJsonString()));
		}
		jo.put("phones", array);
		return jo;
	}

	public JSONObject formEmailJson() throws JSONException {
		JSONObject jo = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < getEmails().size(); i++) {
			array.put(i, new JSONObject(emails.get(i).toJsonString()));
		}
		jo.put("emails", array);
		return jo;
	}

	public void buildNumbers(String phone) throws JSONException {
		L.i(TAG, "buildNumbers phone=" + phone);
		JSONObject jo = new JSONObject(phone);
		JSONArray jPhones = jo.optJSONArray("phones");
		ArrayList<CPhone> cPhones = new ArrayList<CPhone>();
		for (int i = 0; i < jPhones.length(); i++) {
			JSONObject jPhone = jPhones.getJSONObject(i);
			CPhone cPhone = new CPhone(jPhone.optString("phone"),
					jPhone.optInt("type"));
			cPhone.setLabel(jPhone.optString("label"));
			cPhones.add(cPhone);
		}
		setNumbers(cPhones);
	}

	public void buildEmails(String email) throws JSONException {
		L.i(TAG, "buildNumbers email=" + email);
		JSONObject jo = new JSONObject(email);
		JSONArray jEmails = jo.optJSONArray("emails");
		ArrayList<CEmail> cEmails = new ArrayList<CEmail>();
		for (int i = 0; i < jEmails.length(); i++) {
			JSONObject jEmail = jEmails.getJSONObject(i);
			CEmail cEmail = new CEmail(jEmail.optString("email"),
					jEmail.optInt("type"));
			cEmail.setLabel(jEmail.optString("label"));
			cEmails.add(cEmail);
		}
		setEmails(cEmails);
	}

	public void buildInfo(String other) throws JSONException {
		L.i(TAG, "buildNumbers other=" + other);
		JSONObject jo = new JSONObject(other);
		JSONArray addrs = jo.optJSONArray("addrs");
		ArrayList<CAddress> cAddrs = new ArrayList<CAddress>();
		for (int i = 0; i < addrs.length(); i++) {
			JSONObject addr = addrs.getJSONObject(i);
			cAddrs.add(new CAddress(addr.optString("poBox"), addr
					.optString("street"), addr.optString("city"), addr
					.optString("state"), addr.optString("postalCode"), addr
					.optString("country"), addr.optInt("type")));
		}
		setAddrs(cAddrs);
		setContactId(jo.optString("contactId"));
		setStarred(jo.optInt("starred"));
		setOrgName(jo.optString("orgName"));
		setOrgTitle(jo.optString("orgTitle"));
		setNote(jo.optString("note"));
	}
}

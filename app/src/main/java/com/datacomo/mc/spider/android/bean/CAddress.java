package com.datacomo.mc.spider.android.bean;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

public class CAddress implements JsonString{
	private String poBox;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private int type = StructuredPostal.TYPE_HOME;
	private String label = "CUSTOM";
	private String asString = "";

	public int getType() {
		return type;
	}
    
	public void setType(int type) {
		this.type = type;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public String getPoBox() {
		return poBox;
	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getJsonString() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("poBox", poBox);
		map.put("street", street);
		map.put("city", city);
		map.put("state", state);
		map.put("postalCode", postalCode);
		map.put("country", country);
		map.put("type", type + "");
		map.put("label", label);
		
		StringBuffer json = new StringBuffer("{");
		for (String key : map.keySet()) {
			json.append("\"" + key + "\":" + map.get(key) + ",");
		}
		String str = json.toString();
		return str.substring(0, str.length() - 1) + "}";
	}
	

	public String toString() {
		if (this.asString.length() > 0) {
			return (this.asString);
		} else {
			StringBuffer addr = new StringBuffer("");
			if (this.getPoBox() != null) {
				addr.append(this.getPoBox() + " ");
			}
			if (this.getStreet() != null) {
				addr.append(this.getStreet() + " ");
			}
			if (this.getCity() != null) {
				addr.append(this.getCity() + ", ");
			}
			if (this.getState() != null) {
				addr.append(this.getState() + " ");
			}
			if (this.getPostalCode() != null) {
				addr.append(this.getPostalCode() + " ");
			}
			if (this.getCountry() != null) {
				addr.append(this.getCountry());
			}
			return (new String(addr));
		}
	}

	public CAddress(String asString, int type) {
		this.asString = asString;
		this.type = type;
	}

	public CAddress(String poBox, String street, String city, String state,
			String postal, String country, int type) {
		this.setPoBox(poBox);
		this.setStreet(street);
		this.setCity(city);
		this.setState(state);
		this.setPostalCode(postal);
		this.setCountry(country);
		this.setType(type);
	}

	@Override
	public String toJsonString() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("poBox", poBox);
		jo.put("street", street);
		jo.put("state", state);
		jo.put("city", city);
		jo.put("postalCode", postalCode);
		jo.put("label", label);
		jo.put("country", country);
		jo.put("type", type);
		return jo.toString();
	}
}

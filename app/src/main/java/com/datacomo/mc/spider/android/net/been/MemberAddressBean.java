package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberAddressBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 社员标识号
	 */
	private int memberId;

	/**
	 * 全地址唯一标识号
	 */
	private int addressId;

	/**
	 * 全地址全名
	 */
	private String addressName;

	/**
	 * 居住地国家编号
	 */
	private int nationalId;

	/**
	 * 居住地国家名字
	 */
	private String nationalName;

	/**
	 * 居住地省份编号
	 */
	private int provinceId;

	/**
	 * 居住地省份名字
	 */
	private String provinceName;

	/**
	 * 居住城市编号
	 */
	private int cityId;

	/**
	 * 居住地城市名字
	 */
	private String cityName;

	/**
	 * 居住地分区编号
	 */
	private int districtId;

	/**
	 * 居住地分区编号
	 */
	private String districtName;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAddressName() {
		if (addressName == null || addressName.equals("")) {
			if (provinceName != null) {
				addressName = provinceName;
			}
			if (cityName != null && !cityName.equals(provinceName)) {
				addressName += cityName;
			}
			if (districtName != null) {
				addressName += districtName;
			}
		}
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public int getNationalId() {
		return nationalId;
	}

	public void setNationalId(int nationalId) {
		this.nationalId = nationalId;
	}

	public String getNationalName() {
		return nationalName;
	}

	public void setNationalName(String nationalName) {
		this.nationalName = nationalName;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

}

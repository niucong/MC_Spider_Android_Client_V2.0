package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberCompanyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 公司表主键
	 */
	private int companyId;
	/**
	 * 社员编号
	 */
	private int memberId;
	/**
	 * 公司系统配置表主键
	 */
	private int basisCompanyId;
	/**
	 * 公司名称
	 */
	private String companyName;
	/**
	 * 职务
	 */
	private String companyJob;
	/**
	 * 职务编号
	 */
	private int basisJobId;
	/**
	 * 公司地址
	 */
	private String companyAddress;
	/**
	 * 描述信息
	 */
	private String companyDescription;
	/**
	 * 在职状态： 1 是 2否
	 */
	private int companyStatus;
	/**
	 * 入职时间
	 */
	private String jobStartTime;
	// private Object jobStartTime;
	/**
	 * 离职时间
	 */
	private String jobEndTime;
	// private Object jobEndTime;
	/**
	 * 公司图片前缀
	 */
	private String companyImageUrl;
	/**
	 * 公司图片路径
	 */
	private String companyImagePath;
	/**
	 * 创建时间
	 */
	private String createTime;

	// private Object createTime;

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getBasisCompanyId() {
		return basisCompanyId;
	}

	public void setBasisCompanyId(int basisCompanyId) {
		this.basisCompanyId = basisCompanyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyJob() {
		return companyJob;
	}

	public void setCompanyJob(String companyJob) {
		this.companyJob = companyJob;
	}

	public int getBasisJobId() {
		return basisJobId;
	}

	public void setBasisJobId(int basisJobId) {
		this.basisJobId = basisJobId;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyDescription() {
		return companyDescription;
	}

	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}

	public int getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(int companyStatus) {
		this.companyStatus = companyStatus;
	}

	public String getJobStartTime() {
		return jobStartTime;
	}

	public void setJobStartTime(String jobStartTime) {
		this.jobStartTime = jobStartTime;
	}

	public String getJobEndTime() {
		return jobEndTime;
	}

	public void setJobEndTime(String jobEndTime) {
		this.jobEndTime = jobEndTime;
	}

	public String getCompanyImageUrl() {
		return companyImageUrl;
	}

	public void setCompanyImageUrl(String companyImageUrl) {
		this.companyImageUrl = companyImageUrl;
	}

	public String getCompanyImagePath() {
		return companyImagePath;
	}

	public void setCompanyImagePath(String companyImagePath) {
		this.companyImagePath = companyImagePath;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

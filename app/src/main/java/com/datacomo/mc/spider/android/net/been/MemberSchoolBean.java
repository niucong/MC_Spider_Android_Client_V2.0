package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberSchoolBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 社员标识号
	 */
	private int memberId;

	/**
	 * 社员学校标识号
	 */
	private int schoolId;

	/**
	 * 学校所对应的系统基础数据中的学校标识号
	 */
	private int sysSchoolId;

	/**
	 * 学校名
	 */
	private String schoolName;

	/**
	 * 学校类型 1：幼儿园; 2：小学; 3：初中; 4：高中; 5：大学
	 */
	private int schoolType;

	/**
	 * 学校专业
	 */
	private String schoolProfessional;

	/**
	 * 学校描述
	 */
	private String schoolDescription;

	/**
	 * 入学时间
	 */
	private String joinTime;

	/**
	 * 毕业时间
	 */
	private String graduateTime;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 学校图片路径前缀
	 */
	private String schoolImageUrl;

	/**
	 * 学校图片路径前缀
	 */
	private String schoolImagePath;

	/**
	 * 社员学历 1：小学; 2：初中; 3：高中; 4：高职; 5：高专; 6：专科; 7：本科; 8：硕士; 9：博士
	 */
	private int memberEduction;

	/**
	 * 现在是否在这个学校上学 1：是; 2：否
	 */
	private int isInSchool;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	public int getSysSchoolId() {
		return sysSchoolId;
	}

	public void setSysSchoolId(int sysSchoolId) {
		this.sysSchoolId = sysSchoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(int schoolType) {
		this.schoolType = schoolType;
	}

	public String getSchoolProfessional() {
		return schoolProfessional;
	}

	public void setSchoolProfessional(String schoolProfessional) {
		this.schoolProfessional = schoolProfessional;
	}

	public String getSchoolDescription() {
		return schoolDescription;
	}

	public void setSchoolDescription(String schoolDescription) {
		this.schoolDescription = schoolDescription;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public String getGraduateTime() {
		return graduateTime;
	}

	public void setGraduateTime(String graduateTime) {
		this.graduateTime = graduateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSchoolImageUrl() {
		return schoolImageUrl;
	}

	public void setSchoolImageUrl(String schoolImageUrl) {
		this.schoolImageUrl = schoolImageUrl;
	}

	public String getSchoolImagePath() {
		return schoolImagePath;
	}

	public void setSchoolImagePath(String schoolImagePath) {
		this.schoolImagePath = schoolImagePath;
	}

	public int getMemberEduction() {
		return memberEduction;
	}

	public void setMemberEduction(int memberEduction) {
		this.memberEduction = memberEduction;
	}

	public int getIsInSchool() {
		return isInSchool;
	}

	public void setIsInSchool(int isInSchool) {
		this.isInSchool = isInSchool;
	}

}

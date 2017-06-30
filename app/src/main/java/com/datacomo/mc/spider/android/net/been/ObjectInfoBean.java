package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class ObjectInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3402010150463630116L;

	/**
	 * 对象Id
	 */
	private int objectId;
	/**
	 * 圈子Id
	 */
	private int groupId;

	/**
	 * 对象类型
	 */
	private String objSourceType;
	/**
	 * 对象名字
	 */
	private String objectName;

	/**
	 * 对象图片路径前缀
	 */

	private String objectUrl;

	/**
	 * 对象图片路径
	 */
	private String objectPath;

	/**
	 * 对象大小
	 */
	private int objectSize;

	/**
	 * 对象描述信息
	 */
	private String objectDescription;

	/**
	 * 资源详情
	 */
	private ResourceBean resourceBean;

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getObjSourceType() {
		return objSourceType;
	}

	public void setObjSourceType(String objSourceType) {
		this.objSourceType = objSourceType;
	}

	public String getObjectName() {
		if (objectName == null) {
			objectName = "";
		}
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectUrl() {
		return objectUrl;
	}

	public void setObjectUrl(String objectUrl) {
		this.objectUrl = objectUrl;
	}

	public String getObjectPath() {
		return objectPath;
	}

	public void setObjectPath(String objectPath) {
		this.objectPath = objectPath;
	}

	public int getObjectSize() {
		return objectSize;
	}

	public void setObjectSize(int objectSize) {
		this.objectSize = objectSize;
	}

	public String getObjectDescription() {
		if (objectDescription == null) {
			objectDescription = "";
		}
		return objectDescription;
	}

	public void setObjectDescription(String objectDescription) {
		this.objectDescription = objectDescription;
	}

	public String getFullImgPath() {
		return objectUrl + objectPath;
	}

	public ResourceBean getResourceBean() {
		return resourceBean;
	}

	public void setResourceBean(ResourceBean resourceBean) {
		this.resourceBean = resourceBean;
	}

}

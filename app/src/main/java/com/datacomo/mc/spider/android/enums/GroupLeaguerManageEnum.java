package com.datacomo.mc.spider.android.enums;

/**
 * 圈子成员管理
 */
public enum GroupLeaguerManageEnum {

	/**
	 * 移除社员 0:异常 1：成功2：操作者不是圈主或者管理员 3:被移除的社员是管理员，或者是圈主，不可移除 4:社员不在圈子内
	 */
	REMOVEFROMGROUP,
	/**
	 * 任命圈子管理员 0:异常 1：成功 2：操作者不是圈主，没有处理的权限 3：成员已经是有管理圈子权限 4： 任命的成员没有当管理员资格
	 */
	APPOINTGROUPMANAGER,
	/**
	 * 撤销管理员 0:异常 1：成功 2：操作者不是圈主，没有处理的权限 3：成员不在圈子内 4：成员不是管理员
	 */
	REVOKEMANAGER;
}

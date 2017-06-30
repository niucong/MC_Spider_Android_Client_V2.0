/**
 * 
 */
package com.datacomo.mc.spider.android.bean;

/**
 * 通知动作类型
 * 
 * @author zhujigao
 * @date 2011-8-5 下午01:55:25
 * @version v1.0.0
 */
public class NoticeActionType {

	//1 - 申请加为 	
	//例如申请加入圈子，
	//申请朋友，
	//申请成员，申请管理员，
	//申请合作圈子，申请合并圈子，申请下级圈子		对应URL：friend-request.do
	public static final String APPLY_ADD = "APPLY_ADD";
	
	//2 - 推荐加为
	//推荐朋友
	//邀请加入圈子								对应URL：friend-request.do
	public static final String RECOMMEND_ADD= "RECOMMEND_ADD";
	
	//3 - 反馈加为或加入  
	//朋友申请
	//圈子申请
	//管理员申请
	//圈子合作申请
	//圈子上下级申请
	//圈子合并申请								对应URL：notices.do
	public static final String FEEDBACK_ADD = "FEEDBACK_ADD";
	
	//4 - 反馈推荐
	//回复加圈子
	//回复推荐朋友								对应URL：notices.do
	public static final String FEEDBACK_RECOMMEND = "FEEDBACK_RECOMMEND";
	
	//5 - 告知加为
	//任命管理员									对应URL：notices.do
	//留言										对应URL：home.do?memberId=?
	public static final String NOTIFY_ADD = "NOTIFY_ADD"; 
	
	//6 - 告知解除
	//退出圈子									对应URL：notices.do
	//解除上下级
	//解除合作圈子
	public static final String NOTIFY_RELEASE = "NOTIFY_RELEASE"; 
	
	//7 - 告知被解除
	//推出圈子		·							对应URL：notices.do
	public static final String NOTIFY_BE_RELEASED = "NOTIFY_BE_RELEASED"; 
	
	//8 - 分享类类
	//分享圈子话题
	//分享圈子文件
	//分享圈子照片
	//分享个人文件								对应URL：notices.do
	public static final String SHARE_RESOURCE = "SHARE_RESOURCE"; 
	
	//9 -告知推荐成功
	//推荐朋友成功
	//邀请加入圈子成功							对应URL：notices.do
	public static final String  NOTIFY_RECOMMEND = "NOTIFY_RECOMMEND";
	
	// 10 - 新邮件
	public static final String  NOTIFY_SEND_MAIL = "NOTIFY_SEND_MAIL";
}

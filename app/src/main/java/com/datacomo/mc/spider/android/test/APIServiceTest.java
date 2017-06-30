package com.datacomo.mc.spider.android.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.os.AsyncTask;
import android.os.Environment;
import android.test.AndroidTestCase;

import com.datacomo.mc.spider.android.bean.ContactEntity;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.enums.FileListTypeEnum;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.APIMailRequestServers;
import com.datacomo.mc.spider.android.net.APINoteRequestServers;
import com.datacomo.mc.spider.android.net.APIOpenPageRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.FileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.FriendGroupBean;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MailBean;
import com.datacomo.mc.spider.android.net.been.MailContactBean;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.net.been.MemberOrGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.MembercacheMInfo;
import com.datacomo.mc.spider.android.net.been.MessageBean;
import com.datacomo.mc.spider.android.net.been.MessageContacterBean;
import com.datacomo.mc.spider.android.net.been.MessageGreetBean;
import com.datacomo.mc.spider.android.net.been.MessageNoticeBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.QuuboInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.ResourceCommentBean;
import com.datacomo.mc.spider.android.net.been.ResourceGreatBean;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.ResourceVisitBean;
import com.datacomo.mc.spider.android.net.been.ResultAll;
import com.datacomo.mc.spider.android.net.been.ResultMessageBean;
import com.datacomo.mc.spider.android.net.been.groupchat.ChatLeaguerUnreadNumberBean;
import com.datacomo.mc.spider.android.net.been.groupchat.GroupChatMessageBean;
import com.datacomo.mc.spider.android.net.been.groupchat.MapGroupChatMessage;
import com.datacomo.mc.spider.android.net.been.map.MapFileInfoBean;
import com.datacomo.mc.spider.android.net.been.map.MapFileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapMailContactBean;
import com.datacomo.mc.spider.android.net.been.map.MapMessageNoticeBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceCommentBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceGreatBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceVisitBean;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.ContactsUtil;
import com.datacomo.mc.spider.android.view.MailFileView;

public class APIServiceTest extends AndroidTestCase {
	private static final String TAG = "APIServiceTest";

	public void ContactsUtil() {
		ArrayList<ContactEntity> infos = new ArrayList<ContactEntity>();
		for (int i = 500; i < 1000; i++) {
			ContactEntity ce = new ContactEntity("a阿安" + i, "18712340" + i);
			infos.add(ce);
		}
		ContactsUtil.cogradientContact(getContext(), infos, null);
	}

	// 登录
	public void loginTest() throws Exception {
		APINoteRequestServers.noteInfo(getContext(), "21862");

		// MonitorLog.saveLog("order.json?getWorkOrderNums&0f8e3761e4761033",
		// "Exception");

		// MCResult response = APIRequestServers.uploadPhoneBook(App.app, null,
		// null, null);

		// MCResult mcResult = APIRequestServers.ValidatePhoneForInviteRegister(
		// mContext, "15101024105");
		// List<InviteRegisterBean> inviteRegisterBeans =
		// (List<InviteRegisterBean>) mcResult
		// .getResult();
		// InviteRegisterBean inviteRegisterBean = inviteRegisterBeans.get(0);
		// MCResult mcResult = APIRequestServers.resourceVisitList(getContext(),
		// "53", "40561", "OBJ_GROUP_QUUBO", "false", "0", "10");
		// L.d(TAG, "commentRecords ResultCode=" + mcResult.getResultCode());
		// MapResourceVisitBean mapBean = (MapResourceVisitBean) mcResult
		// .getResult();
		// L.v(TAG, "commentRecords mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		// L.v(TAG,
		// "commentRecords mapBean.TOTALMEMBERNUM="
		// + mapBean.getTOTALMEMBERNUM());
		// List<ResourceVisitBean> LIST = mapBean.getLIST();
		// if (LIST != null) {
		// for (ResourceVisitBean resourceVisitBean : LIST) {
		// L.i(TAG, "commentRecords resourceVisitBean.MemberName="
		// + resourceVisitBean.getMemberName());
		// }
		// }
		// MCResult mcResult = APIRequestServers.praiseRecords(getContext(),
		// "4205", "13912", "OBJ_GROUP_FILE", "false", "0", "10");
		// L.d(TAG, "praiseRecords ResultCode=" + mcResult.getResultCode());
		// MapResourceGreatBean mapBean = (MapResourceGreatBean) mcResult
		// .getResult();
		// L.v(TAG, "praiseRecords mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		// List<ResourceGreatBean> PRAISELIST = mapBean.getPRAISELIST();
		// if (PRAISELIST != null) {
		// for (ResourceGreatBean resourceGreatBean : PRAISELIST) {
		// L.i(TAG, "praiseRecords resourceGreatBean.MemberName="
		// + resourceGreatBean.getMemberName());
		// }
		// }
		// MapResourceGreatBean rgBean = (MapResourceGreatBean)
		// APIThemeRequestServers
		// .themeFocusOrBrowseOrShareList(getContext(), "101", "2", "0",
		// "5").getResult();
		// L.d(TAG, " " + rgBean.getCOUNT());
		// int j = 0;
		// for (ResourceGreatBean bean : rgBean.getLIST()) {
		// L.i(TAG,
		// ++j
		// + " "
		// + bean.getMemberName()
		// + " "
		// + DateTimeUtil.aTimeFormat(DateTimeUtil
		// .getLongTime(bean.getLastVisitTime())));
		// }

		// GroupInfoBean bean = (GroupInfoBean)
		// APIRequestServers.detailGroupInfo(
		// getContext(), "36").getResult();
		// L.i(TAG,
		// bean.getGROUP_BG() + " " + bean.getRESULT() + " "
		// + bean.getOPEN_PAGE_ID());

		// APIThemeRequestServers.attentionOrCancelTheme(getContext(), "101",
		// "2");
		// APIThemeRequestServers.themesList(getContext(), "4205", "0", "20");
		// MapResourceBean mapResourceBean = (MapResourceBean)
		// APIThemeRequestServers
		// .quuboListForGroupByTheme(getContext(), "4205", "101", "0",
		// "20").getResult();
		// L.d(TAG,
		// " " + mapResourceBean.getTOTALNUM() + " "
		// + mapResourceBean.getRELATION());
		// int i = 0;
		// for (ResourceBean bean : mapResourceBean.getLIST()) {
		// L.i(TAG, ++i + " " + bean.getObjectId());
		// }

		// GroupThemeBean bean = (GroupThemeBean)
		// APIThemeRequestServers.themeInfo(getContext(), "1118").getResult();
		// L.d(TAG, "" + bean.getThemeContent());

		// APIRequestServers.myGroupChatList(getContext(), "0", "20");
		// APIRequestServers.searchGroupChat(getContext(), "测试", "0", "20",
		// "true");

		// APIRequestServers.newNum(getContext(), NewNumTypeEnum.NEWALLNUM);

		// ArrayList<GroupEntity> LIST =
		// GroupListService.getService(getContext())
		// .queryGroupListsByContactTime();
		// for (GroupEntity groupEntity : LIST) {
		// L.d(TAG, "" + groupEntity.getName());
		// }

		MCResult mcResult = APIRequestServers.login(getContext(),
				"13439627592", "aaaaaa");
		L.d(TAG, "loginTest ResultCode=" + mcResult.getResultCode());
		L.d(TAG, "loginTest Result=" + mcResult.getResult());

		// APIRequestServers.groupLeaguerManage(getContext(),
		// GroupLeaguerManageEnum.REMOVEFROMGROUP, "456", "123");
		// APIRequestServers.removeFriendFromGroup(getContext(), "1121336",
		// null);
		// APIRequestServers.singleInfo(getContext());

		// APIRequestServers.uploadFile(getContext(), new File(
		// "/mnt/sdcard/tencent/weibo/save/20130203074427.jpg"),
		// UploadMethodEnum.GROUPCHATPHOTO, null);
		// APIRequestServers.singleInfo(getContext());

		// APIRequestServers.searchInviteMemberListForAndroid(getContext(),
		// "7160", "卢", "0", "20", false);
	}

	// 个人信息
	public void getMemberBasicInfoTest() throws Exception {
		MCResult mcResult = APIRequestServers.getMemberBasicInfo(getContext(),
				"0");
		L.d(TAG,
				"getMemberBasicInfoTest ResultCode=" + mcResult.getResultCode());
		MemberBean memberBean = (MemberBean) mcResult.getResult();
		L.i(TAG,
				"getMemberBasicInfoTest memberBean.MemberId="
						+ memberBean.getMemberId());
		MemberBasicBean memberBasicBean = (MemberBasicBean) memberBean
				.getBasicInfo();
		L.i(TAG,
				"getMemberBasicInfoTest memberBean.memberBasicBean.MemberName="
						+ memberBasicBean.getMemberName());
		MemberHeadBean memberHeadBean = memberBasicBean.getHeadImage();
		L.i(TAG,
				"getMemberBasicInfoTest memberBean.memberBasicBean.memberHeadBean.HeadUrl="
						+ memberHeadBean.getHeadUrl());
		String viewBirthdaySetting = memberBasicBean.getViewBirthdaySetting();
		L.i(TAG,
				"getMemberBasicInfoTest memberBean.memberBasicBean.viewBirthdaySetting="
						+ viewBirthdaySetting);
	}

	// 动态墙
	public void getAllGroupTrendList() throws Exception {
		// 交流圈动态墙
		MCResult mcResult = APIRequestServers.getAllGroupTrendList(
				getContext(), "0", "10", true,
				new AsyncTask<String, Integer, String>() {

					@Override
					protected String doInBackground(String... params) {
						return null;
					}
				});
		// 朋友圈动态墙
		// MCResult mcResult = APIRequestServers.myFriendsTrends(getContext(),
		// "0", "10", true);
		// L.d(TAG, "getAllGroupTrendList ResultCode=" +
		// mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		// ArrayList<Object> objectList = APIRequestServers
		// .getLocTrends(getContext());
		if (objectList != null) {
			for (Object object : objectList) {
				ResourceTrendBean resourceTrendBean = (ResourceTrendBean) object;
				L.i(TAG, "getAllGroupTrendList resourceTrendBean.TrendId="
						+ resourceTrendBean.getTrendId());
				MemberOrGroupInfoBean sendMemberInfo = resourceTrendBean
						.getSendMemberInfo();
				L.i(TAG,
						"getAllGroupTrendList resourceTrendBean.sendMemberInfo.Name="
								+ sendMemberInfo.getName());

				ResourceBean resourceBean = resourceTrendBean.getResourceBean();
				List<ObjectInfoBean> objectInfo = resourceBean.getObjectInfo();
				if (objectInfo != null) {
					for (ObjectInfoBean objectInfoBean : objectInfo) {
						L.i(TAG,
								"getAllGroupTrendList resourceTrendBean.resourceBean.objectInfoBean.ObjSourceType="
										+ objectInfoBean.getObjSourceType());
					}
				} else {
					L.d(TAG, "getAllGroupTrendList objectInfo=null");
				}
			}
		}
	}

	// 圈博详情
	public void quuboInfo() throws Exception {
		// groupId：ResourceTrendBean.resourceBean.objOwnerMemberInfo.id
		// quuboId：ResourceTrendBean.resourceBean.objectId
		MCResult mcResult = APIRequestServers.quuboInfo(getContext(), "53",
				"41594");
		L.d(TAG, "quuboInfo ResultCode=" + mcResult.getResultCode());
		QuuboInfoBean quuboInfoBean = (QuuboInfoBean) mcResult.getResult();
		L.i(TAG,
				"quuboInfo quuboInfoBean.QuuboId=" + quuboInfoBean.getQuuboId());
		MemberOrGroupInfoBean groupInfoBean = quuboInfoBean.getGroupInfoBean();
		L.i(TAG,
				"quuboInfo quuboInfoBean.groupInfoBean.Name="
						+ groupInfoBean.getName());
		List<ObjectInfoBean> objectInfoBeans = quuboInfoBean
				.getObjectInfoBeans();
		if (objectInfoBeans != null) {
			for (ObjectInfoBean objectInfoBean : objectInfoBeans) {
				L.d(TAG,
						"quuboInfo quuboInfo quuboInfoBean.objectInfoBeans.ObjSourceType="
								+ objectInfoBean.getObjSourceType());
			}
		}
	}

	// 圈子详情
	public void groupInfo() throws Exception {
		// MCResult mcResult = APIRequestServers.groupInfo(getContext(), "53");
		// L.d(TAG, "quuboInfo ResultCode=" + mcResult.getResultCode());
		// GroupBean groupBean = (GroupBean) mcResult.getResult();
		// L.i(TAG, "quuboInfo groupBean.GroupId=" + groupBean.getGroupId());
		// L.i(TAG, "quuboInfo groupBean.GroupName=" +
		// groupBean.getGroupName());
		APIRequestServers.exitGroup(getContext(), "7533");
	}

	// 赞成员列表
	public void praiseRecords() throws Exception {
		// method=praiseRecords&groupId=4205&quuboId=13912&objectType=OBJ_GROUP_FILE&noPaging=false&startRecord=0&maxResults=10
		// MCResult mcResult = APIRequestServers.praiseRecords(getContext(),
		// "4205", "13912", "OBJ_GROUP_FILE", "false", "0", "10");
		MCResult mcResult = APIRequestServers.praiseRecords(getContext(),
				"4205", "13912", "OBJ_GROUP_FILE", "false", "0", "10");
		L.d(TAG, "praiseRecords ResultCode=" + mcResult.getResultCode());
		MapResourceGreatBean mapBean = (MapResourceGreatBean) mcResult
				.getResult();
		L.v(TAG, "praiseRecords mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		List<ResourceGreatBean> PRAISELIST = mapBean.getPRAISELIST();
		if (PRAISELIST != null) {
			for (ResourceGreatBean resourceGreatBean : PRAISELIST) {
				L.i(TAG, "praiseRecords resourceGreatBean.MemberName="
						+ resourceGreatBean.getMemberName());
			}
		}
	}

	// 访问成员列表
	public void resourceVisitBean() throws Exception {
		// method=praiseRecords&groupId=53&quuboId=40561&objectType=OBJ_GROUP_QUUBO&noPaging=false&startRecord=0&maxResults=10
		// MCResult mcResult = APIRequestServers.praiseRecords(getContext(),
		// "4205", "13912", "OBJ_GROUP_FILE", "false", "0", "10");
		MCResult mcResult = APIRequestServers.resourceVisitList(getContext(),
				"53", "40561", "OBJ_GROUP_QUUBO", "false", "0", "10");
		L.d(TAG, "commentRecords ResultCode=" + mcResult.getResultCode());
		MapResourceVisitBean mapBean = (MapResourceVisitBean) mcResult
				.getResult();
		L.v(TAG, "commentRecords mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		L.v(TAG,
				"commentRecords mapBean.TOTALMEMBERNUM="
						+ mapBean.getTOTALMEMBERNUM());
		List<ResourceVisitBean> LIST = mapBean.getLIST();
		if (LIST != null) {
			for (ResourceVisitBean resourceVisitBean : LIST) {
				L.i(TAG, "commentRecords resourceVisitBean.MemberName="
						+ resourceVisitBean.getMemberName());
			}
		}
	}

	// 评论成员列表
	public void commentRecords() throws Exception {
		// method=praiseRecords&groupId=53&quuboId=40561&objectType=OBJ_GROUP_QUUBO&noPaging=false&startRecord=0&maxResults=10
		// MCResult mcResult = APIRequestServers.praiseRecords(getContext(),
		// "4205", "13912", "OBJ_GROUP_FILE", "false", "0", "10");
		MCResult mcResult = APIRequestServers.commentRecords(getContext(),
				"53", "40561", "OBJ_GROUP_QUUBO", "false", "0", "10");
		L.d(TAG, "commentRecords ResultCode=" + mcResult.getResultCode());
		MapResourceCommentBean mapBean = (MapResourceCommentBean) mcResult
				.getResult();
		L.v(TAG, "commentRecords mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		List<ResourceCommentBean> LIST = mapBean.getLIST();
		if (LIST != null) {
			for (ResourceCommentBean resourceCommentBean : LIST) {
				L.i(TAG, "commentRecords resourceCommentBean.MemberName="
						+ resourceCommentBean.getMemberName());
			}
		}
	}

	// 赞
	public void praiseResource() throws Exception {
		MCResult mcResult = APIRequestServers.praiseResource(getContext(),
				"4185", "41748", "OBJ_GROUP_QUUBO");
		L.d(TAG, "praiseResource ResultCode=" + mcResult.getResultCode());
		L.d(TAG, "praiseResource Result=" + mcResult.getResult());
	}

	// 评论
	public void commentResource() throws Exception {
		MCResult mcResult = APIRequestServers.commentResource(getContext(),
				"7174", "42807", "OBJ_GROUP_QUUBO", "猪猪",
				new String[] { "2053527" });
		L.d(TAG, "commentResource ResultCode=" + mcResult.getResultCode());
		L.d(TAG, "commentResource Result=" + mcResult.getResult());
	}

	// 最近访问的人列表
	public void visitFriendListForIPhone() throws Exception {
		MCResult mcResult = APIRequestServers.visitFriendListForIPhone(
				getContext(), "0", "0", "10");
		L.d(TAG,
				"visitFriendListForIPhone ResultCode="
						+ mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			for (Object object : objectList) {
				FriendBean friendBean = (FriendBean) object;
				L.i(TAG, "visitFriendListForIPhone friendBean.FriendName="
						+ friendBean.getMemberName());
			}
		}
	}

	// 获得成员照片列表
	public void getMemberPhotosLists() throws Exception {
		// MCResult mcResult = APIRequestServers.getMemberPhotosLists(
		// getContext(), "", "0", "10", false);
		MCResult mcResult = APIRequestServers.collectionResourceList(
				getContext(), ResourceTypeEnum.OBJ_GROUP_QUUBO, "0", "10",
				false);

		L.d(TAG, "getMemberPhotosLists ResultCode=" + mcResult.getResultCode());
		MapResourceBean mapBean = (MapResourceBean) mcResult.getResult();
		L.v(TAG,
				"getMemberPhotosLists mapBean.TOTALNUM="
						+ mapBean.getTOTALNUM());
		List<ResourceBean> LIST = mapBean.getLIST();
		if (LIST != null) {
			int i = 0;
			for (ResourceBean resourceBean : LIST) {
				L.d(TAG, "getMemberPhotosLists i=" + ++i);
				List<ObjectInfoBean> objectInfo = resourceBean.getObjectInfo();
				if (objectInfo != null) {
					for (ObjectInfoBean objectInfoBean : objectInfo) {
						L.i(TAG,
								"getMemberPhotosLists resourceTrendBean.resourceBean.objectInfoBean.ObjSourceType="
										+ objectInfoBean.getObjSourceType());
						if ("OBJ_GROUP_PHOTO".equals(objectInfoBean
								.getObjSourceType())) {
							L.v(TAG,
									"getMemberPhotosLists resourceTrendBean.resourceBean.objectInfoBean.ObjectName="
											+ objectInfoBean.getObjectUrl()
											+ objectInfoBean.getObjectPath());
						}
					}
				}
			}
		}
	}

	// 获得圈子照片列表
	public void getGroupPhotosLists() throws Exception {
		MCResult mcResult = APIRequestServers.groupResourceLists(getContext(),
				"7174", ResourceTypeEnum.OBJ_GROUP_PHOTO, "0", "10", false);
		L.d(TAG, "getGroupPhotosLists ResultCode=" + mcResult.getResultCode());
		MapResourceBean mapBean = (MapResourceBean) mcResult.getResult();
		L.v(TAG,
				"getGroupPhotosLists mapBean.TOTALNUM=" + mapBean.getTOTALNUM());
		List<ResourceBean> LIST = mapBean.getLIST();
		if (LIST != null) {
			int i = 0;
			for (ResourceBean resourceBean : LIST) {
				L.d(TAG, "getGroupPhotosLists i=" + ++i);
				List<ObjectInfoBean> objectInfo = resourceBean.getObjectInfo();
				if (objectInfo != null) {
					for (ObjectInfoBean objectInfoBean : objectInfo) {
						L.i(TAG,
								"getGroupPhotosLists resourceTrendBean.resourceBean.objectInfoBean.ObjSourceType="
										+ objectInfoBean.getObjSourceType());
						if ("OBJ_GROUP_PHOTO".equals(objectInfoBean
								.getObjSourceType())) {
							L.v(TAG,
									"getGroupPhotosLists resourceTrendBean.resourceBean.objectInfoBean.ObjectName="
											+ objectInfoBean.getObjectUrl()
											+ objectInfoBean.getObjectPath());
						}
					}
				}
			}
		}
	}

	// 我的动态列表
	public void myMemberTrends() throws Exception {
		// MCResult mcResult = APIRequestServers.myMemberTrends(getContext(),
		// "0",
		// "10", true);
		MCResult mcResult = APIOpenPageRequestServers
				.myAttentionOpenPageResourceTrends(getContext(), "0", "10",
						true);
		L.d(TAG, "myMemberTrends ResultCode=" + mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			int i = 0;
			for (Object object : objectList) {
				ResourceTrendBean resourceTrendBean = (ResourceTrendBean) object;
				L.i(TAG, "myMemberTrends resourceTrendBean.TrendId="
						+ resourceTrendBean.getTrendId());
				ResourceBean resourceBean = resourceTrendBean.getResourceBean();
				L.d(TAG, "myMemberTrends i=" + ++i);
				List<ObjectInfoBean> objectInfo = resourceBean.getObjectInfo();
				if (objectInfo != null) {
					for (ObjectInfoBean objectInfoBean : objectInfo) {
						L.i(TAG,
								"myMemberTrends resourceTrendBean.resourceBean.objectInfoBean.ObjSourceType="
										+ objectInfoBean.getObjSourceType());

					}
				}
			}
		}
	}

	// 圈子列表
	public void groupLists() throws Exception {
		// MCResult mcResult = APIRequestServers.myMemberTrends(getContext(),
		// "0",
		// "10", true);
		MCResult mcResult = APIOpenPageRequestServers.openPageList(
				getContext(), "1", "0", "20", false);
		L.d(TAG, "myMemberTrends ResultCode=" + mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			int i = 0;
			for (Object object : objectList) {
				i++;
				GroupBean groupBean = (GroupBean) object;
				L.i(TAG, i + " myMemberTrends resourceTrendBean.TrendId="
						+ groupBean.getGroupName());
			}
		}
	}

	// 某个圈子动态列表
	public void singleGroupResourceTrends() throws Exception {
		MCResult mcResult = APIRequestServers.singleGroupResourceTrends(
				getContext(), "7270", "0", "10", true);
		L.d(TAG,
				"singleGroupResourceTrends ResultCode="
						+ mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			for (Object object : objectList) {
				ResourceTrendBean resourceTrendBean = (ResourceTrendBean) object;
				L.i(TAG, "singleGroupResourceTrends resourceTrendBean.TrendId="
						+ resourceTrendBean.getTrendId());
				MemberOrGroupInfoBean sendMemberInfo = resourceTrendBean
						.getSendMemberInfo();
				L.i(TAG,
						"getAllGroupTrendList resourceTrendBean.sendMemberInfo.Name="
								+ sendMemberInfo.getName());
			}
		}
	}

	// 交流圈成员列表
	public void groupLeaguerList() throws Exception {
		MCResult mcResult = APIRequestServers.groupLeaguerList(getContext(),
				"7270", "0", "10", false);
		L.d(TAG, "groupLeaguerList ResultCode=" + mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			for (Object object : objectList) {
				GroupLeaguerBean groupLeaguerBean = (GroupLeaguerBean) object;
				L.i(TAG, "groupLeaguerList groupLeaguerBean.MemberName="
						+ groupLeaguerBean.getMemberName());
				L.i(TAG, "groupLeaguerList groupLeaguerBean.FriendStatus="
						+ groupLeaguerBean.getFriendStatus());
			}
		}
	}

	// 交流圈列表
	// public void commGroupList() throws Exception {
	// MCResult mcResult = APIRequestServers.commGroupList(getContext(), "0",
	// "10", true);
	// L.d(TAG, "commGroupList ResultCode=" + mcResult.getResultCode());
	// @SuppressWarnings("unchecked")
	// ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
	// if (objectList != null) {
	// L.d(TAG, "commGroupList size=" + objectList.size());
	// for (Object object : objectList) {
	// GroupBasicBean groupBasicBean = (GroupBasicBean) object;
	// L.i(TAG, "commGroupList groupBasicBean.GroupName="
	// + groupBasicBean.getGroupName());
	// }
	// }
	// }

	// 朋友圈列表
	public void friendGroupList() throws Exception {
		MCResult mcResult = APIRequestServers.friendGroupList(getContext());
		L.d(TAG, "friendGroupList ResultCode=" + mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			L.d(TAG, "friendGroupList size=" + objectList.size());
			for (Object object : objectList) {
				FriendGroupBean friendGroupBean = (FriendGroupBean) object;
				L.i(TAG, "friendGroupList friendGroupBean.GroupName="
						+ friendGroupBean.getGroupName());
			}
		}
	}

	/**
	 * 加到朋友圈
	 * 
	 * @throws Exception
	 */
	public void addFriendToGroup() throws Exception {
		// APIRequestServers.addFriendToGroup(getContext(), "1564497",
		// new String[] { "23246160" });
		APIRequestServers.createGroup(getContext(), "牛s啊", "", "", "1");
	}

	// 朋友个数、被圈个数、打招呼
	public void friendNum() throws Exception {
		MCResult mcResult = APIRequestServers.friendNum(getContext(), "0");
		// MCResult mcResult = APIRequestServers.fansNum(getContext(), "0");
		// MCResult mcResult = APIRequestServers.greetMamber(getContext(),
		// "1121336", "5");
		// MCResult mcResult =
		// APIRequestServers.leaveGuestBookWord(getContext(),
		// "1121336", "哈哈，终于搞定了");
		Integer num = (Integer) mcResult.getResult();
		int a = num;
		L.d(TAG, "friendNum ResultCode=" + mcResult.getResultCode());
		L.d(TAG, "friendNum FriendNum=" + a);
	}

	// 朋友列表、被圈列表
	public void friendList() throws Exception {
		// MCResult mcResult = APIRequestServers.friendList(getContext(), "0",
		// "0", "370");
		MCResult mcResult = APIRequestServers.fansList(getContext(), "1121336",
				"0", "311");
		L.d(TAG, "friendList ResultCode=" + mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			int i = 0;
			for (Object object : objectList) {
				i++;
				FriendBean friendBean = (FriendBean) object;
				L.i(TAG,
						i + "=friendList friendGroupBean.MemberName="
								+ friendBean.getMemberName() + "："
								+ friendBean.getFriendName() + "："
								+ friendBean.getFriendNamePY());
			}
		}
	}

	// 获取我的短信联系人列表
	public void myMessageList() throws Exception {
		MCResult mcResult = APIRequestServers.myMessageList(getContext(), "0",
				"20");
		L.d(TAG, "myMessageList ResultCode=" + mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			int i = 0;
			for (Object object : objectList) {
				i++;
				MessageContacterBean messageContacterBean = (MessageContacterBean) object;
				L.i(TAG, i + "=myMessageList messageContacterBean="
						+ messageContacterBean.getContacterName() + "："
						+ messageContacterBean.getContacterLeaguerId() + "："
						+ messageContacterBean.getLastMessageContent());
			}
		}
	}

	// 获取我与某人的短信列表
	public void contactMemberMessages() throws Exception {
		MCResult mcResult = APIRequestServers.contactMemberMessages(
				getContext(), "", "0", "20", false, null);
		L.d(TAG, "contactMemberMessages ResultCode=" + mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			int i = 0;
			for (Object object : objectList) {
				i++;
				MessageBean messageBean = (MessageBean) object;
				L.i(TAG,
						i + "=contactMemberMessages messageBean="
								+ messageBean.getSenderName() + "："
								+ messageBean.getMessageContent());
			}
		}
	}

	// 获取招呼列表
	public void greetInfoList() throws Exception {
		MCResult mcResult = APIRequestServers.greetInfoList(getContext(), "0",
				"20");
		L.d(TAG, "myMessageList ResultCode=" + mcResult.getResultCode());
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			int i = 0;
			for (Object object : objectList) {
				i++;
				MessageGreetBean messageGreetBean = (MessageGreetBean) object;
				L.i(TAG, i + "=myMessageList messageContacterBean="
						+ messageGreetBean.getSendMemberName() + "："
						+ messageGreetBean.getGreetValue());
			}
		}
	}

	// 获取通知列表
	public void noticeList() throws Exception {
		MCResult mcResult = APIRequestServers.noticeList(getContext(), "0",
				"20");
		L.d(TAG, "noticeList ResultCode=" + mcResult.getResultCode());

		MapMessageNoticeBean mapBean = (MapMessageNoticeBean) mcResult
				.getResult();
		L.v(TAG,
				"getMemberPhotosLists mapBean.TOTALNUM="
						+ mapBean.getTOTAL_NUM());
		List<MessageNoticeBean> LIST = mapBean.getLIST();

		if (LIST != null) {
			int i = 0;
			for (MessageNoticeBean messageNoticeBean : LIST) {
				i++;
				L.i(TAG, i + "=noticeList messageNoticeBean="
						+ messageNoticeBean.getSendMemberName() + "："
						+ messageNoticeBean.getObjectName() + "："
						+ messageNoticeBean.getNoticeContent());
			}
		}
	}

	// 搜索
	public void searchTest() throws Exception {
		MCResult mcResult = APIRequestServers.searchResource(getContext(),
				"影视", null, 1, 10, "sa", "0", "");
		L.d(TAG, "searchTest ResultCode=" + mcResult.getResultCode());

		ResultAll resultAll = (ResultAll) mcResult.getResult();
		L.d(TAG, "searchTest resultAll.size=" + resultAll.getCountNum());

		List<ResultMessageBean> rmList = resultAll.getRmList();
		if (rmList != null) {
			for (ResultMessageBean resultMessageBean : rmList) {
				L.i(TAG, "searchTest resultMessageBean.title="
						+ resultMessageBean.getTitle());

				MembercacheMInfo info = resultMessageBean.getMMInfo();
				if (info != null) {
					L.i(TAG, "searchTest sendName=" + info.getMemberName());
				} else {
					L.i(TAG, "searchTest MembercacheMInfo==null");
				}
			}
		}
	}

	// 上传
	public void uploadFile() {
		String path = ConstantUtil.DOWNLOAD_PATH + "201212131106551490.jpg";
		APIRequestServers.uploadFile(getContext(), new File(path),
				UploadMethodEnum.UPLOADHEAD, null);
	}

	/**
	 * 下载
	 * 
	 * @throws Exception
	 */
	public void downLoadFile() throws Exception {
		// groupId=7160&fileId=14590
		MCResult mcResult = APIRequestServers.fileDownloadPath(getContext(),
				"7160", "14590");
		// MCResult mcResult = APIRequestServers.groupTopicFileDownloadPath(
		// getContext(), "", "", "");
		if (mcResult != null && mcResult.getResultCode() == 1) {
			String fileUrl = mcResult.getResult().toString();
			L.d(TAG, "downLoadFile fileUrl=" + fileUrl);
			new DownLoadFileThread(getContext(), fileUrl, 0, "").start();
		}
	}

	/**
	 * 获得通信录
	 * 
	 * @throws Exception
	 */
	public void getaddressBookTest() throws Exception {
		APIRequestServers.getaddressBook(getContext());
	}

	public void mailInfo() throws Exception {
		MCResult mc = APIMailRequestServers.mailInfo(getContext(), "9929",
				"13746");
		L.d(TAG, "test mc=" + mc.getResultCode());
		MailBean mailBean = (MailBean) mc.getResult();
		L.i(TAG, "" + mailBean.getSendMemberMail());
		L.i(TAG, "" + mailBean.getMailSubject());
	}

	public void shareThirdNew() throws Exception {
		MCResult mc = APIMailRequestServers.contactLeaguers(getContext(), "0",
				"20");
		MapMailContactBean mapBean = (MapMailContactBean) mc.getResult();
		L.d(TAG, "shareThirdNew TOTALNUM=" + mapBean.getTOTALNUM());
		List<MailContactBean> list = mapBean.getCONTACTLIST();
		for (MailContactBean mailContactBean : list) {
			L.i(TAG,
					"shareThirdNew SendMemberName="
							+ mailContactBean.getRelationMemberName());
		}
	}

	public void recommendGroupList() throws Exception {
		Object[] objects = APIRequestServers.recommendGroupList(getContext());
		L.d(TAG, "recommendGroupList objects=" + objects[0]);
		@SuppressWarnings("unchecked")
		ArrayList<GroupEntity> list = (ArrayList<GroupEntity>) objects[1];
		L.d(TAG, "recommendGroupList size=" + list.size());
		for (GroupEntity groupEntity : list) {
			L.i(TAG, "name=" + groupEntity.getName());
		}
	}

	public void diaryList() throws Exception {
		// // MCResult mCResult = APINoteRequestServers.diaryLists(getContext(),
		// // "1",
		// // "false", "0", "20");
		// MCResult mCResult = APINoteRequestServers.searchDiary(getContext(),
		// "优优工作圈", "false", "0", "20");
		// L.d(TAG, "ResultCode=" + mCResult.getResultCode());
		// @SuppressWarnings("unchecked")
		// ArrayList<DiaryInfoBean> objectList = (ArrayList<DiaryInfoBean>)
		// mCResult
		// .getResult();
		// for (DiaryInfoBean bean : objectList) {
		// L.i(TAG, "bean.getTitle=" + bean.getTitle());
		// }

		MCResult mCResult = APINoteRequestServers.cloudNoteList(getContext(),
				"4", "false", "0", "0", "20");
		com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean map = (com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean) mCResult
				.getResult();
		L.d(TAG, "diaryList num=" + map.getALLNOTENUM());
		for (com.datacomo.mc.spider.android.net.been.note.NoteInfoBean bean : map
				.getNOTE_LIST()) {
			L.i(TAG, "diaryList NoteTitle=" + bean.getNoteTitle());
		}
	}

	public void diaryOper() throws Exception {
		// createDiary、editDiary、deleteDiary
		MCResult mCResult = APINoteRequestServers.shareDiary(getContext(),
				"8419", new String[] { "1121336", "1229816" });
		L.d(TAG, "ResultCode=" + mCResult.getResultCode());
	}

	// 获取朋友有更新的列表
	public void friendUpdateList() throws Exception {
		UpdateFriendListThread.updateFriendList(mContext, null);
		// MCResult mCResult = APIRequestServers.friendUpdateList(getContext(),
		// "0", "0", "0", "10", "true");
		// L.d(TAG, "friendUpdateList ResultCode=" + mCResult.getResultCode());
		// MapFriendSimpleBean simpleBean = (MapFriendSimpleBean) mCResult
		// .getResult();
		// L.i(TAG,
		// "friendUpdateList START_SEARCH_TIME="
		// + simpleBean.getSTART_SEARCH_TIME());
		// L.i(TAG,
		// "friendUpdateList LAST_UPDATE_TIME="
		// + simpleBean.getLAST_UPDATE_TIME());
		// L.i(TAG,
		// "friendUpdateList LAST_DELETE_TIME="
		// + simpleBean.getLAST_DELETE_TIME());
		// List<FriendSimpleBean> ulist = simpleBean.getFRIEND_UPDATE_LIST();
		// if (ulist != null) {
		// for (FriendSimpleBean friendSimpleBean : ulist) {
		// L.d(TAG,
		// "friendUpdateList MemberName="
		// + friendSimpleBean.getMemberName());
		// L.i(TAG,
		// "friendUpdateList FriendGroupId="
		// + friendSimpleBean.getFriendGroupId()
		// .substring(
		// 1,
		// friendSimpleBean
		// .getFriendGroupId()
		// .length() - 1));
		// }
		// }
		// List<FriendSimpleBean> dlist = simpleBean.getFRIEND_DELETE_LIST();
		// if (dlist != null) {
		// for (FriendSimpleBean friendSimpleBean : dlist) {
		//
		// }
		// }
	}

	public void uploadMailFile() {
		// /mnt/sdcard/tencent/weibo/save/20120202103657.jpg
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + "tencent/weibo/save/20120202103657.jpg");
		new MailFileView(getContext(), false, file);
	}

	public void queryDB() throws Exception {
		ArrayList<GroupEntity> list = GroupListService.getService(getContext())
				.searchGroupLists("android");
		L.i(TAG, "queryDB...");
		if (list != null) {
			int i = 0;
			for (GroupEntity groupEntity : list) {
				L.d(TAG,
						++i + ":" + groupEntity.getName() + ":"
								+ groupEntity.getGroupNamePy());
			}
		}

		// ArrayList<String> ids = GroupListService.getService(getContext())
		// .queryGroupIds();
		// if (ids != null) {
		// for (String id : ids) {
		// L.i(TAG, "queryDB id=" + id);
		// }
		// }
		// String groupID = "";
		// if (ids != null) {
		// for (int i = 0; i < ids.size(); i++) {
		// if (i == ids.size() - 1) {
		// groupID += ids.get(i);
		// } else {
		// groupID += ids.get(i) + ",";
		// }
		// }
		// }
		// L.i(TAG, "queryDB groupID=" + groupID);

		// int[] ids = new FriendToGroupService(getContext())
		// .queryFriendIds(7439915);
		// if (ids != null) {
		// for (int i = 0; i < ids.length; i++) {
		// L.i(TAG, "queryDB " + i + " id=" + ids[i]);
		// }
		//
		// List<FriendSimpleBean> beans = FriendListService.getService(
		// getContext()).queryFriendListsByIds(ids);
		// if (beans != null) {
		// for (int i = 0; i < beans.size(); i++) {
		// L.i(TAG, "queryDB " + i + " name="
		// + beans.get(i).getMemberName());
		// }
		// }
		// }

	}

	// 获取云文件列表：全部，分享的，自己跌
	public void getFileList() throws Exception {
		MCResult mcResult = APIFileRequestServers.getFileList(getContext(),
				FileListTypeEnum.ALL_FILE, 0, 5, false);
		if (null != mcResult) {
			int resultCode = mcResult.getResultCode();
			if (resultCode == 1) {
				MapFileInfoBean map_bean = (MapFileInfoBean) mcResult
						.getResult();
				int num = map_bean.getTOTAL_NUM();
				L.d(TAG, "getFileList num:" + num);
				List<FileInfoBean> bean = map_bean.getFILE_LIST();
				if (null != bean) {
					for (FileInfoBean fileInfoBean : bean) {
						L.d(TAG,
								"getFileList FileSize:"
										+ fileInfoBean.getFileSize());
						String tag = "";
						String tags[] = fileInfoBean.getFileTags();
						if (tags != null) {
							int j = tags.length;
							for (int i = 0; i < j; i++) {
								if (i == (j - 1)) {
									tag += tags[i];
								} else {
									tag += tags[i] + ",";
								}
							}
							L.i(TAG, "getFileList tag=" + tag);
						}
					}
				}
			}
		}
	}

	// 编辑云文件
	public void editFile() throws Exception {
		// MCResult mcResult = APIFileRequestServers.editFile(getContext(),
		// 232973, "班大师", "班大师截图");
		// if (null != mcResult) {
		// int resultCode = mcResult.getResultCode();
		// if (resultCode == 1) {
		// int result = (Integer) mcResult.getResult();
		// L.d(TAG, "result:" + result);
		// }
		// }

		MCResult mcResult = APIFileRequestServers.fileShareMembers(
				getContext(), "237617", 0, 10, false);

		MapFileShareLeaguerBean mapBean = (MapFileShareLeaguerBean) mcResult
				.getResult();
		for (FileShareLeaguerBean bean : mapBean.getSHARE_LEAGUERS()) {
			L.d(TAG, bean.getRelationMemberName());
		}
	}

	// 删除云文件
	public void deleteFile() throws Exception {
		MCResult mcResult = APIFileRequestServers.deleteFile(getContext(),
				227668);
		if (null != mcResult) {
			int resultCode = mcResult.getResultCode();
			if (resultCode == 1) {
				int result = (Integer) mcResult.getResult();
				L.d(TAG, "result:" + result);
			}
		}
	}

	// 获取云文件路径
	public void getFilePath() throws Exception {
		MCResult mcResult = APIFileRequestServers.getFilePath(getContext(),
				232973);
		if (null != mcResult) {
			int resultCode = mcResult.getResultCode();
			if (resultCode == 1) {
				String path = (String) mcResult.getResult();
				L.d(TAG, "path:" + path);
			}
		}
	}

	// 分享文件
	public void shareFile() throws Exception {
		MCResult mcResult = APIFileRequestServers.shareFile(getContext(),
				225970, new String[] { "752001" }, "测试");
		if (null != mcResult) {
			int resultCode = mcResult.getResultCode();
			if (resultCode == 1) {
				int result = (Integer) mcResult.getResult();
				L.d(TAG, "result:" + result);
			}
		}
	}

	// 分享文件到圈子
	public void shareFileToGroup() throws Exception {
		MCResult mcResult = APIFileRequestServers.shareFileToGroup(
				getContext(), 225970, new String[] { "7541" }, "ceshi");
		if (null != mcResult) {
			int resultCode = mcResult.getResultCode();
			if (resultCode == 1) {
				int result = (Integer) mcResult.getResult();
				L.d(TAG, "result:" + result);
			}
		}
	}

	// //获取联系人对应的分组ID -1为未分组的
	// public void getPhoneGroup() {
	// String text1= "15210691922";
	// String text2= "13466784236";
	// String text3= "65272735";
	// String group=ContactsUtil.getGroupIdByPhone(getContext(),text2);
	// L.d(TAG, "group:"+group);
	// }
	//
	// //获取分组信息
	// public void getGroup() {
	// List<ContactGroupEntity>
	// entities_ContactGroup=ContactsUtil.getGroups(getContext());
	// L.d(TAG, "group.size"+entities_ContactGroup.size() );
	// }
	//
	//
	// //获取分组信息
	// public void addGroupIdInContact() {
	// String text1= "15210691922";
	// String text2= "13466784236";
	// String groupId="1";
	// int result=ContactsUtil.addGroupIdInContact(getContext(), text1,
	// groupId);
	// L.d(TAG, "result"+result);
	// }
	//
	// //获取分组信息
	// public void addGroup() {
	// List<ContactGroupEntity> entities=new ArrayList<ContactGroupEntity>();
	// ContactGroupEntity entity=new ContactGroupEntity("22", "tt");
	// entity.setmAccount_Name("local-contacts");
	// entity.setmAccount_Type("com.local.contacts");
	// entity.setDelete("0");
	// entities.add(entity);
	// ContactsUtil.addGroups(getContext(), entities);
	// }

	public void createFriendGroup() throws Exception {
		// MCResult mcResult = APIRequestServers.createFriendGroup(getContext(),
		// "ceshi", new String[] { "" });
		// if (mcResult.getResultCode() == 1) {
		// JSONObject jsonObject = (JSONObject) mcResult.getResult();
		// L.d(TAG, "result: " + jsonObject.toString());
		//
		// }

		HashMap<String, String> map = ContactsUtil
				.getRawIdVersion(getContext());
		L.i(TAG, "size=" + map.size());
		int i = 0;
		for (Iterator<?> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			L.d(TAG, "_id " + key + " VERSION " + map.get(key));
			i = Math.max(i, Integer.valueOf(key));
		}
		L.i(TAG, "i=" + i);
	}

	// 加入圈聊 ok
	// public void enterGroupChat() {
	// String groupId = "8897";
	// MCResult mcResult = null;
	// try {
	// mcResult = APIGroupChatRequestServers.enterGroupChat(getContext(),
	// groupId);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// if (mcResult.getResultCode() == 1) {
	// MapEnterGroupChat map = (MapEnterGroupChat) mcResult.getResult();
	// if ("1".equals(map.getRESULT())) {
	// L.d(TAG, "enterGroupChat ok");
	// }
	// }
	// }

	// 退出圈聊 ok
	public void editGroupChat() {
		int groupId = 8897;
		MCResult mcResult = null;
		try {
			mcResult = APIGroupChatRequestServers.exitGroupChat(getContext(),
					groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mcResult.getResultCode() == 1) {
			if ((Integer) mcResult.getResult() == 1) {
				L.d(TAG, "editGroupChat ok");
			}
		}
	}

	// 获取私信联系人列表 ok
	public void getContactMembersForGroupChat() {

		MCResult mcResult = null;
		try {
			mcResult = APIGroupChatRequestServers
					.getContactMembersForGroupChat(getContext(), "0", "30");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mcResult.getResultCode() == 1) {
			List<Object> beans = (List<Object>) mcResult.getResult();
			if (null != beans && beans.size() > 0) {
				L.d(TAG,
						"getContactMembersForGroupChat ok+bean.size"
								+ beans.size());
			}
		}
	}

	// 发送信息 ok
	public void sendGroupMessage() {
		int chatId = 2;
		int groupId = 8897;
		String content = "{objectType:'OBJ_PHOTO',objectName:'processing_72',objectUrl:'http://img.yuuquu.com/',objectPath:'group_photo_image/2013/02/03/201302030942449462_800_0.jpg',objectSize:'objectSize',objectFormatName:'jpg',objectFormatUrl:'objectFormatUrl',objectFormatPath:'objectFormatPath'}";
		MCResult mcResult = null;
		try {
			mcResult = APIGroupChatRequestServers.sendGroupMessage(
					getContext(), new String[] { groupId + "" }, true, content,
					null, null, null, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mcResult.getResultCode() == 1) {
			if ((Integer) mcResult.getResult() == 1) {
				L.d(TAG, "sendGroupMessage ok");
			}
		}
	}

	// 获取群聊内容 ok
	public void getGroupChatMessage() {
		MCResult mcResult = null;
		int chatId = 2;
		try {
			mcResult = APIGroupChatRequestServers.getGroupChatMessage(
					getContext(), chatId, 0, 1, 20, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mcResult.getResultCode() == 1) {
			MapGroupChatMessage chatmemberList = (MapGroupChatMessage) mcResult
					.getResult();
			List<GroupChatMessageBean> beans = chatmemberList.getLIST();
			if (null != beans && beans.size() > 0) {
				L.d(TAG, "getGroupChatMessage ok+bean.size" + beans.size());
			}
		}
	}

	// 获取圈聊成员列表 ok
	public void getGroupChatmemberList() {
		MCResult mcResult = null;
		int chatId = 2;
		try {
			mcResult = APIGroupChatRequestServers.getGroupChatMemberList(
					getContext(), chatId, 0, 20, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mcResult.getResultCode() == 1) {
			// MapMemberSimpleBean chatmemberList = (MapMemberSimpleBean)
			// mcResult
			// .getResult();
			// List<MemberSimpleBean> beans =
			// chatmemberList.getCHATMEMBERLIST();
			// if (null != beans && beans.size() > 0) {
			// L.d(TAG, "getGroupChatmemberList ok+bean.size" + beans.size());
			// }
		}
	}

	// 邀请圈子成员加入圈聊 ok
	public void inviteLeaguerEnterChat() {
		int chatId = 2;
		int groupId = 8897;
		String[] memberId = new String[] { "6748190" };
		MCResult mcResult = null;
		try {
			mcResult = APIGroupChatRequestServers.inviteLeaguerEnterChat(
					getContext(), memberId, chatId, groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mcResult.getResultCode() == 1) {
			if ((Integer) mcResult.getResult() == 1) {
				L.d(TAG, "inviteLeaguerEnterChat ok");
			}
		}
	}

	public void getInvitableGroupChatMemberList() {
		MCResult mcResult = null;
		int chatId = 2;
		int groupId = 8897;
		try {
			mcResult = APIGroupChatRequestServers
					.getInvitableGroupChatMemberList(getContext(), chatId,
							groupId, 0, 20, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mcResult.getResultCode() == 1) {
			// MapMemberSimpleBean chatmemberList = (MapMemberSimpleBean)
			// mcResult
			// .getResult();
			// List<MemberSimpleBean> beans =
			// chatmemberList.getCHATMEMBERLIST();
			// if (null != beans && beans.size() > 0) {
			// L.d(TAG, "getGroupChatmemberList ok+bean.size" + beans.size());
			// }
		}
	}

	// 获取群聊成员未读数量 OK
	public void getChatLeaguerUnreadNumber() {
		String groupId = "8840";
		MCResult mcResult = null;
		try {
			mcResult = APIGroupChatRequestServers.getChatLeaguerUnreadNumber(
					getContext(), groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mcResult.getResultCode() == 1) {
			ChatLeaguerUnreadNumberBean bean = (ChatLeaguerUnreadNumberBean) mcResult
					.getResult();
			if (bean.getRESULT() == 1) {
				L.d(TAG, "getChatLeaguerUnreadNumber : ok" + " UNREAD_NUM ："
						+ bean.getUNREAD_NUM());
			}
		}
	}

	// 获取朋友所在朋友圈 OK
	public void getMyFriendGroupList() {
		MCResult mcResult = null;
		int memberId = 3628171;
		try {
			mcResult = APIRequestServers.getMyFriendGroupList(getContext(),
					memberId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mcResult.getResultCode() == 1) {
		}
	}

	public void getQuuChat() {
		try {
			MCResult result = APIGroupChatRequestServers.getGroupChatMessage(
					getContext(), 8897, 0, 2, 20, true);
			MapGroupChatMessage map = (MapGroupChatMessage) result.getResult();
			ArrayList<GroupChatMessageBean> requestInfo = (ArrayList<GroupChatMessageBean>) map
					.getLIST();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

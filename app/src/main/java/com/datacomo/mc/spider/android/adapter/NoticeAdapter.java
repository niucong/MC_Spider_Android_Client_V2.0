package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.ChooseGroupsDialogActivity;
import com.datacomo.mc.spider.android.CircleBlogDetailsActivity;
import com.datacomo.mc.spider.android.FileDetailActivity;
import com.datacomo.mc.spider.android.GroupFriendActivity;
import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.MailDetailsIndexActivity;
import com.datacomo.mc.spider.android.MailListActivity;
import com.datacomo.mc.spider.android.NoteDetailsActivity;
import com.datacomo.mc.spider.android.QChatActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.ReceiveRecommendActivity;
import com.datacomo.mc.spider.android.TopicActivity;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.db.QuuBoInfoService;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.interfaces.OnRecallListener;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.APIThemeRequestServers;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.GroupThemeBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MessageNoticeBean;
import com.datacomo.mc.spider.android.net.been.QuuboInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FaceUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.util.TypeUtil;

public class NoticeAdapter extends BaseAdapter {
	private static final String TAG = "NoticeAdapter";

	private static Context mContext;
	private ArrayList<MessageNoticeBean> noticeInfo;
	// private ListView mListView;
	private OnRecallListener recallListener;

	private LoadInfoTask loadInfoTask;
	private ReplyApplyTask replyApplyTask;
	private ReplyGroupApplyTask replyGroupApplyTask;
	private RemoveFriendFromGroupTask removeFriendFromGroupTask;
	private DeleteNoticesStateTask deleteNoticesStateTask;
	private ResourceInfoTask resourceInfoTask;
	private FileInfoTask fileInfoTask;

	private final int LOADINFO = 0;
	private final int REPLYAPPLY = 1;
	private final int UPDATENOTICESTATE = 2;
	private final int RESOURCEINFO = 3;
	private final int REPLYGROUPAPPLY = 4;
	private final int FILEINFO = 5;
	private final int REMOVEFRIENDFROMGROUPTASK = 6;

	private ViewHolder holder = null;

	private LayoutInflater inflater;

	public boolean refersh = false;

	private boolean isLocal;

	private SpinnerProgressDialog spdDialog;

	public NoticeAdapter(Context context, ArrayList<MessageNoticeBean> notices,
			ListView listview, OnRecallListener listener) {
		mContext = context;
		noticeInfo = notices;
		// mListView = listview;
		recallListener = listener;
		spdDialog = new SpinnerProgressDialog(context);

		inflater = LayoutInflater.from(mContext);
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	@Override
	public int getCount() {
		return noticeInfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		return noticeInfo.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_notice, null);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.b1 = (TextView) convertView.findViewById(R.id.agree);
			holder.b2 = (TextView) convertView.findViewById(R.id.ignore);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.details = (ImageView) convertView.findViewById(R.id.details);
			holder.textlayout = (LinearLayout) convertView
					.findViewById(R.id.text_linear);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.b1.setVisibility(View.GONE);
		holder.b2.setVisibility(View.GONE);
		final MessageNoticeBean bean = noticeInfo.get(position);
		String date = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(bean
				.getNoticeTime()));
		holder.time.setText(date);

		holder.num.setVisibility((bean.isRead() || isLocal) ? View.GONE
				: View.VISIBLE);
		holder.num.setTag(bean);

		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.findViewWithTag(bean).setVisibility(View.GONE);
				if (!bean.isRead()) {
					bean.setIsRead(true);
					noticeInfo.remove(position);
					noticeInfo.add(position, bean);
				}
				String objectType = bean.getObjectType();
				String actionType = bean.getActionType();
				if ("SHARE_RESOURCE".equals(actionType)// 分享资源
						|| "COMMENT_RESOURCE".equals(actionType)// 评论资源
						|| "REPLY_COMMENTRESOURCE".equals(actionType)// 回复某人
						|| "NOTIFY_ADD".equals(actionType)//
						|| "SHARE_FILE".equals(actionType)//
						|| "AT_MEMBERS_COMMENT".equals(actionType)// 评论资源时@某人
						|| "AT_MEMBERS_CREATE".equals(actionType)// 创建资源时@某人
						|| "NOTIFY_OBJ_ATTENTION_CREATE".equals(actionType)// 关注专题有新圈博
						|| "NOTIFY_OBJ_ATTENTION_COMMENT".equals(actionType)) {// 关注专题中圈博有新评论
					String objectId = bean.getObjectId() + "";
					if ("OBJ_GROUP_QUUBO".equals(objectType)
							|| "OBJ_GROUP_TOPIC".equals(objectType)
							|| "OBJ_OPEN_PAGE_QUUBO".equals(objectType)
							|| "OBJ_OPEN_PAGE_TOPIC".equals(objectType)) {
						QuuboInfoBean qib = null;
						try {
							qib = QuuBoInfoService.getService(mContext)
									.queryQuubo(bean.getSendObjectId(),
											Integer.valueOf(objectId));
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (qib != null) {
							Bundle b = new Bundle();
							b.putSerializable("info", qib);
							b.putString("type_From",
									TypeUtil.type_ResultMessage);
							b.putString("type_Object", objectType);
							LogicUtil.enter(mContext,
									CircleBlogDetailsActivity.class, b, false);
						} else {
							stopTask(LOADINFO);
							spdDialog.showProgressDialog("正在加载中...");
							loadInfoTask = new LoadInfoTask(
									bean.getSendObjectId() + "", objectId,
									objectType);
							loadInfoTask.execute();
						}
					} else if ("OBJ_GROUP_FILE".equals(objectType)
							|| "OBJ_GROUP_PHOTO".equals(objectType)
							|| "OBJ_OPEN_PAGE_PHOTO".equals(objectType)
							|| "OBJ_OPEN_PAGE_FILE".equals(objectType)
							|| "OBJ_MEMBER_RES_LEAVEMESSAGE".equals(objectType)
							|| "OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(objectType)
							|| "OBJ_MEMBER_RES_MOOD".equals(objectType)) {
						// TODO
						stopTask(RESOURCEINFO);
						spdDialog.showProgressDialog("正在加载中...");
						resourceInfoTask = new ResourceInfoTask(
								bean.getSendObjectId() + "", objectId,
								objectType);
						resourceInfoTask.execute();
					} else if ("OBJ_MEMBER_DIARY".equals(objectType)) {
						Bundle b = new Bundle();
						b.putString("diaryId", objectId);
						b.putString("diaryTitle", bean.getObjectName());
						b.putString("shareMemberName", bean.getSendMemberName());
						b.putString("shareMemberId", bean.getSendMemberId()
								+ "");
						b.putString("updateTime", bean.getNoticeTime());
						LogicUtil.enter(mContext, NoteDetailsActivity.class, b,
								false);
					} else if ("OBJ_MEMBER_FILE".equals(objectType)) {
						stopTask(FILEINFO);
						spdDialog.showProgressDialog("正在加载中...");
						fileInfoTask = new FileInfoTask(objectId);
						fileInfoTask.execute();

						// if (FileUtil.checkInstantApp(mContext,
						// "com.datacomo.mc.cloudfile")) {
						// // 组件名称，第一个参数是包名，也是主配置文件Manifest里设置好的包名,第二个是类名，要带上包名
						// ComponentName com = new ComponentName(
						// "com.datacomo.mc.cloudfile",
						// "com.datacomo.mc.cloudfile.LoadingActivity");
						// Intent oIntent = new Intent();
						// oIntent.setComponent(com);
						// oIntent.putExtra("ReFresh", true);
						// mContext.startActivity(oIntent);
						// } else {
						// new DownLoadFileThread(
						// mContext,
						// "http://img.yuuquu.com/m6/default/android/cloud_file.apk",
						// 0, "云文件.apk").start();
						// }
					} else if ("NOTIFY_ADD".equals(actionType)
							&& ("OBJ_GROUP_LEAGUER".equals(objectType)
									|| "OBJ_GROUP_MANAGER".equals(objectType)
									|| "OBJ_GROUP_OWNER".equals(objectType) || "OBJ_GROUP_GOLD"
										.equals(objectType))) {
						enterGroup(bean);
					} else if ("OBJ_GROUP_THEME".equals(objectType)) {
						stopTask(RESOURCEINFO);
						spdDialog.showProgressDialog("正在加载中...");
						resourceInfoTask = new ResourceInfoTask("", objectId,
								"Theme");
						resourceInfoTask.execute();
					}
				} else if ("NOTIFY_SEND_MAIL".equals(actionType)) {
					if ("OBJ_MEMBER_MAIL".equals(objectType)) {
						String noticeContent = bean.getNoticeContent();
						if (noticeContent != null
								&& noticeContent.contains(",")) {
							String[] datas = noticeContent.split(",");
							if (datas != null && datas.length > 4) {
								Bundle b = new Bundle();
								b.putString("mailId", "" + bean.getObjectId());
								b.putString("recordId", "" + datas[3]);
								b.putString("friendId", bean.getSendMemberId()
										+ "");
								b.putString("friendName",
										bean.getSendMemberName());
								LogicUtil.enter(mContext,
										MailDetailsIndexActivity.class, b,
										false);
								return;
							}
						}
						LogicUtil.enter(mContext, MailListActivity.class, null,
								false);
					}
				} else if ("NOTIFY_RELEASE".equals(actionType)
						&& ("OBJ_GROUP_LEAGUER".equals(objectType) || "OBJ_GROUP_MANAGER"
								.equals(objectType))) {
					enterGroup(bean);
				} else if (("APPLY_ADD".equals(actionType) || "FEEDBACK_ADD"
						.equals(actionType))
						&& ("OBJ_GROUP_LEAGUER".equals(objectType) || "OBJ_GROUP_MANAGER"
								.equals(objectType))) {
					enterGroup(bean);
				} else if ("FEEDBACK_AUTHENTICATION".equals(actionType)) {
					enterGroup(bean);
				} else if ("FEEDBACK_ADD".equals(actionType)
						&& ("OBJ_GROUP_LEAGUER".equals(objectType) || "OBJ_GROUP_MANAGER"
								.equals(objectType))) {
					enterGroup(bean);
				} else if (("FEEDBACK_RECOMMEND".equals(actionType)
						|| "NOTIFY_BE_RELEASED".equals(actionType) || "NOTIFY_RECOMMEND"
							.equals(actionType))
						&& ("OBJ_GROUP_LEAGUER".equals(objectType) || "OBJ_GROUPCHAT_LEAGUER"
								.equals(objectType))) {
					enterGroup(bean);
				}
			}
		};
		// holder.text.setOnClickListener(listener);
		holder.text.setText(Html.fromHtml(
				FaceUtil.faceToHtml(setNoticeView(position, bean)),
				imageGetter, null));

		String head = ThumbnailImageUrl.getThumbnailHeadUrl(
				bean.getSendMemberHeadUrl() + bean.getSendMemberHeadPath(),
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		String sendName = bean.getSendMemberName();
		final String actionType = bean.getActionType();
		final String objectType = bean.getObjectType();
		if (("NOTICE_ACTION_TYPE_INVITE_SUCESS_BY_FRIENDGROUP"
				.equals(actionType) && "OBJ_FRIEND".equals(objectType))
				|| ("NOTICE_ACTION_TYPE_ACCEPTINVITE_DOWNLOADCLIENT_LOGIN"
						.equals(actionType) && "OBJ_GOLD".equals(objectType))) {
			head = ThumbnailImageUrl
					.getThumbnailHeadUrl(
							bean.getObjectUrl() + bean.getObjectPath(),
							HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
			sendName = bean.getObjectName();
		}
		holder.name.setText(sendName);

		String path = bean.getSendMemberHeadPath();
		if (path != null && "/m6/default/email/email.jpg".equals(path)) {
			holder.head.setImageResource(R.drawable.icon_mail);
		} else {
			holder.head.setTag(position + head);
			MyFinalBitmap.setHeader(mContext, holder.head, head);
		}

		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int id = bean.getSendMemberId();
				String name = bean.getSendMemberName();
				if (("NOTICE_ACTION_TYPE_INVITE_SUCESS_BY_FRIENDGROUP"
						.equals(actionType) && "OBJ_FRIEND".equals(objectType))
						|| ("NOTICE_ACTION_TYPE_ACCEPTINVITE_DOWNLOADCLIENT_LOGIN"
								.equals(actionType) && "OBJ_GOLD"
								.equals(objectType))) {
					id = bean.getObjectId();
					name = bean.getObjectName();
				}
				if (id != 0) {
					Bundle b = new Bundle();
					b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					b.putString("id", id + "");
					b.putString("name", name);
					LogicUtil.enter(mContext, HomePgActivity.class, b, false);
				}
			}
		});

		convertView.setOnClickListener(listener);

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				new AlertDialog.Builder(mContext)
						.setTitle("删除提示")
						.setMessage("是否删除该条通知？")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										deleteNotices(bean);
									}
								}).setNegativeButton("否", null).show();
				return false;
			}
		});

		return convertView;
	}

	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String id) {
			int ids = 0;
			try {
				ids = Integer.valueOf(id);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			Drawable drawable = mContext.getResources().getDrawable(ids);
			int width = drawable.getIntrinsicWidth();
			int height = drawable.getIntrinsicHeight();
			drawable.setBounds(0, 0, width / 2, height / 2);
			return drawable;
		}
	};

	/**
	 * 进入圈子
	 * 
	 * @param bean
	 */
	private void enterGroup(MessageNoticeBean bean) {
		Bundle b = new Bundle();
		b.putString("Id", bean.getSendObjectId() + "");
		LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
	}

	/**
	 * 显示处理所有通知
	 * 
	 * @param bean
	 * @return
	 */
	private String setNoticeView(int position, final MessageNoticeBean bean) {
		String actionType = bean.getActionType();
		String objectType = bean.getObjectType();
		String s1 = "";
		String s2 = "";
		String s3 = "";

		String objectName = bean.getObjectName();
		if (objectName == null) {
			objectName = "";
		} else {
			// CharUtil.filterText(objectName); //过滤标签 会很卡，暂时注掉
		}
		String group = bean.getSendObjectName();
		if (group == null)
			group = "";
		String content = bean.getNoticeContent();
		if (content == null)
			content = "";

		if ("APPLY_ADD".equals(actionType)) {
			// 申请类
			if ("OBJ_GROUP_LEAGUER".equals(objectType)) {
				// 申请加入
				s1 = "申请加入	";
				s2 = " 圈子";
				objectName = "";
				replyAddGroup(bean);
			} else if ("OBJ_GROUP_MANAGER".equals(objectType)) {
				// 申请成为管理员
				s1 = "申请成为	";
				s2 = "	管理员";
				objectName = "";
				replyGroupManager(bean);
			} else if ("OBJ_GROUP_SUB_GROUP".equals(objectType)) {
				// 申请成为下级圈子
				s1 = "申请 ";
				s2 = " 成为 ";
				objectName += " 的下级圈子";
				replySubOrSupGroup(bean, true);
			} else if ("OBJ_GROUP_COLLABORATE_GROUP".equals(objectType)) {
				// 申请圈子合作
				s1 = "申请 ";
				s2 = " 和 ";
				objectName += " 合作";
				replyCollaborateGroup(bean);
			} else if ("OBJ_GROUP_COMBINE_GROUP".equals(objectType)) {
				// 申请圈子合并
				s1 = "申请 ";
				s2 = " 和 ";
				objectName += " 合并";
				replyCombineGroup(bean);
			} else if ("OBJ_GROUP_SUP_GROUP".equals(objectType)) {
				// XX申请Group1成为Group2的上级圈子
				s1 = "申请 ";
				s2 = " 成为 ";
				objectName += " 的上级圈子";
				replySubOrSupGroup(bean, false);
			}
		} else if ("RECOMMEND_ADD".equals(actionType)) {
			// 推荐类
			if ("OBJ_FRIEND".equals(objectType)
					|| "OBJ_GROUP_LEAGUER".equals(objectType)) {
				// 推荐成为朋友
				s1 = "推荐  " + objectName + " 成为您的朋友";
				if (content != null && !"".equals(content)) {
					s1 += "\n附言：" + bean.getNoticeContent();
				}
				group = "";
				objectName = "";
				addFriend(bean);
			} else if ("OBJ_FRIEND_GROUP".equals(objectType)) {
				// TODO 推荐分组（人数、详情）
				s1 = "与您分享Ta的朋友圈：" + group;
				if (content != null && !"".equals(content)) {
					s1 += "，附言：" + bean.getNoticeContent();
				}
				group = "";
				objectName = "";
				replyRecommendFriendGroup(bean);
				holder.text.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putInt("GroupId", bean.getSendObjectId());
						bundle.putString("GroupName", bean.getSendObjectName());
						bundle.putInt("RecommendId", bean.getObjectId());
						LogicUtil.enter(mContext,
								ReceiveRecommendActivity.class, bundle, false);
					}
				});
			}
		} else if ("SHARE_FILE".equals(actionType)) {
			// 分享类
			if ("OBJ_GROUP_QUUBO".equals(objectType)) {
				// 分享圈子内的文件
				s1 = "给您分享了 ";
				s2 = " 中的文件【";
				s3 = "】";
				objectName = bean.getRemarkContent();
			}
		} else if ("SHARE_RESOURCE".equals(actionType)) {
			// 分享类
			if ("OBJ_GROUP_FILE".equals(objectType)) {
				// 分享圈子内的文件
				s1 = "给您分享了 ";
				s2 = " 中的文件【";
				s3 = "】";
			} else if ("OBJ_GROUP_PHOTO".equals(objectType)) {
				// 分享圈子内的照片
				s1 = "给您分享了 ";
				s2 = " 中的照片【";
				s3 = "】";
			} else if ("OBJ_GROUP_QUUBO".equals(objectType)
					|| "OBJ_GROUP_TOPIC".equals(objectType)) {
				// 分享圈子内的圈博
				s1 = "给您分享了 ";
				s2 = " 中的圈博【";
				s3 = "】";
			} else if ("OBJ_OPEN_PAGE_FILE".equals(objectType)) {
				// 分享开放主页内的文件
				s1 = "给您分享了 ";
				s2 = " 中的文件【";
				s3 = "】";
				L.d(TAG, "setNoticeView objectName=" + objectName + ",content="
						+ content);
				if ("".equals(objectName)) {
					try {
						content = content.substring(1, content.length() - 1);
						objectName = new JSONObject(content)
								.getString("objectName");
						L.i(TAG, "setNoticeView objectName=" + objectName
								+ ",content=" + content);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else if ("OBJ_OPEN_PAGE_PHOTO".equals(objectType)) {
				// 分享开放主页内的照片
				s1 = "给您分享了 ";
				s2 = " 中的照片【";
				s3 = "】";
				L.d(TAG, "setNoticeView objectName=" + objectName + ",content="
						+ content);
				if ("".equals(objectName)) {
					try {
						content = content.substring(1, content.length() - 1);
						objectName = new JSONObject(content)
								.getString("objectName");
						L.i(TAG, "setNoticeView objectName=" + objectName
								+ ",content=" + content);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else if ("OBJ_OPEN_PAGE_QUUBO".equals(objectType)) {
				// 分享开放主页内的圈博
				s1 = "给您分享了 ";
				s2 = " 中的圈博【";
				s3 = "】";
				L.d(TAG, "setNoticeView objectName=" + objectName + ",content="
						+ content);
				if ("".equals(objectName)) {
					try {
						content = content.substring(1, content.length() - 1);
						objectName = new JSONObject(content)
								.getString("objectName");
						L.i(TAG, "setNoticeView objectName=" + objectName
								+ ",content=" + content);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else if ("OBJ_MEMBER_FILE".equals(objectType)) {
				// 分享个人文件
				s1 = "给您分享了";
				s2 = "TA的文件【";
				s3 = "】";
			} else if ("OBJ_MEMBER_DIARY".equals(objectType)) {
				// 分享笔记
				s1 = "给您分享了";
				s2 = "TA的笔记【";
				s3 = "】";
			} else if ("OBJ_GROUP_THEME".equals(objectType)) {
				// 分享专题
				s1 = "给您分享了 ";
				s2 = " 中的专题【";
				s3 = "】";
			}
		} else if ("FEEDBACK_AUTHENTICATION".equals(actionType)) {
			// 回复类
			if ("OBJ_GROUP".equals(objectType)) {
				// 回复圈子认证结果通知
				if ("0".equals(content)) {
					s1 = "您好，很遗憾，您的 ";
					s2 = " 圈子没有通过优优工作圈认证。";
					objectName = "";
				} else {
					s1 = "您好，您的 ";
					s2 = " 圈子已经通过了优优工作圈认证";
					objectName = "";
				}
			}
		} else if ("FEEDBACK_ADD".equals(actionType)) {
			// 回复添加类
			if ("OBJ_FRIEND".equals(objectType)) {
				// 回复加朋友申请
				s1 = "同意您的朋友请求";
				objectName = "";
			} else if ("OBJ_GROUP_LEAGUER".equals(objectType)) {
				// 回复加圈子申请
				s1 = "同意您加入 ";
				s2 = " 圈子";
				objectName = "";
			} else if ("OBJ_GROUP_MANAGER".equals(objectType)) {
				// 回复管理员申请
				s1 = "同意您成为 ";
				s2 = " 圈子的管理员";
				objectName = "";
			} else if ("OBJ_GROUP_COLLABORATE_GROUP".equals(objectType)) {
				// 回复圈子合作申请
				s1 = "同意 ";
				s2 = " 圈子与 ";
				s3 = " 圈子合作";
			} else if ("OBJ_GROUP_SUB_GROUP".equals(objectType)) {
				// 回复圈子上下级申请
				s1 = "同意 ";
				s2 = " 圈子成为 ";
				s3 = " 圈子的下级";
			} else if ("OBJ_GROUP_COMBINE_GROUP".equals(objectType)) {
				// 回复圈子合并申请
				s1 = "同意 ";
				s2 = " 圈子与 ";
				s2 = " 圈子合并";
			}
		} else if ("NOTIFY_ADD".equals(actionType)) {
			// 回复类-任命
			if ("OBJ_GROUP_MANAGER".equals(objectType)) {
				// 任命管理员
				s1 = "任命了您成为 ";
				s2 = " 圈子的管理员";
				objectName = "";
			} else if ("OBJ_GROUP_LEAGUER".equals(objectType)) {
				// 被导入人收到的通知
				s1 = "把您加入 ";
				s2 = " 圈子";
				objectName = "";
			} else if ("OBJ_FRIEND".equals(objectType)) {
				// 告知朋友成功
				s1 = "推荐 ";
				s2 = " 与您成为朋友";
				objectName = "";
			} else if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(objectType)) {
				// 留言
				s1 = "给您留言";
				s2 = "：";
			} else if ("OBJ_GROUP_OWNER".equals(objectType)) {
				// 圈子转让
				s1 = "将 ";
				s2 = " 圈子转让给您，您已是圈主";
				objectName = "";
			} else if ("OBJ_GOLD".equals(objectType)) {
				// 充值圈币
				s1 = content;
				objectName = "";
			} else if ("OBJ_MEMBER_TASK".equals(objectType)) {
				// 完成任务奖励魔币
				String[] task = content.split(",");
				s1 = "提醒您：您通过 " + taskName(task[0]) + " 获得 " + task[1]
						+ " 圈币，可发送 " + task[1] + " 条信息，想赚取更多圈币，赶快邀请朋友注册吧！";
			} else if ("OBJ_GROUP_GOLD".equals(objectType)) {
				// 圈子充值圈币
				String gold = bean.getNoticeContent();
				s1 = gold.substring(0, gold.indexOf("{")) + " " + objectName
						+ " " + gold.substring(gold.lastIndexOf("}") + 1);
				s2 = "";
				objectName = "";
			}
		} else if ("FEEDBACK_RECOMMEND".equals(actionType)) {
			// 回复类-加入圈子
			if ("OBJ_GROUP_LEAGUER".equals(objectType)) {
				// 回复加圈子邀请
				s1 = "接受您的邀请，加入 ";
				s2 = " 圈子";
				objectName = "";
			} else if ("OBJ_FRIEND".equals(objectType)) {
				s1 = "接受您的推荐与";
				s2 = " 成为朋友";
				objectName = "";
			}
		} else if ("NOTIFY_RELEASE".equals(actionType)) {
			// 撤销类-回复
			if ("OBJ_GROUP_MANAGER".equals(objectType)) {
				// 撤销管理员
				s1 = "撤销您 ";
				s2 = " 圈子的管理员";
				objectName = "";
			} else if ("OBJ_GROUP_LEAGUER".equals(objectType)) {
				// 退出圈子
				s1 = "退出 ";
				s2 = " 圈子";
				objectName = "";
			} else if ("OBJ_GROUP_SUB_GROUP".equals(objectType)) {
				// 撤销上下级
				s1 = "解除 ";
				s2 = " 和 ";
				s3 = " 的上下级关系";
			} else if ("OBJ_GROUP_COLLABORATE_GROUP".equals(objectType)) {
				// 撤销合作关系
				s1 = "解除 ";
				s2 = " 和 ";
				s3 = " 的合作关系";
			} else if ("OBJ_MEMBER_SCHEDULE".equals(objectType)) {
				// 回复日程提醒删除
				s1 = "取消了为您设置的日程提醒：";
				objectName = "";
			}
		} else if ("NOTIFY_BE_RELEASED".equals(actionType)) {
			// 踢出圈子
			if ("OBJ_GROUP_LEAGUER".equals(objectType)) {
				// 踢出圈子
				s1 = "把您请出  ";
				s2 = " 圈子";
				objectName = "";
			} else if ("OBJ_GROUPCHAT_LEAGUER".equals(objectType)) {
				s1 = "把您请出 ";
				s2 = " 圈聊";
				objectName = "";
			}
		} else if ("NOTIFY_RECOMMEND".equals(actionType)) {
			// 告知推荐成功
			if ("OBJ_FRIEND".equals(objectType)) {
				// 推荐朋友成功
				s1 = "接受您的推荐与 ";
				s2 = " 成为朋友";
				objectName = "";
			} else if ("OBJ_GROUP_LEAGUER".equals(objectType)) {
				// 推荐朋友加入圈子成功
				s1 = "接受您的邀请，加入圈子 ";
				objectName = "";
			}
		} else if ("FEEDBACK_IMPORT".equals(actionType)) {
			// 反馈导入
			if ("OBJ_GROUP_LEAGUER".equals(objectType)) {
				// 反馈圈子导入成员通知
				s1 = content.substring(0, content.indexOf("{"));
				s2 = content.substring(content.indexOf("}") + 1);
				objectName = "";
			}
		} else if ("NOTIFY_ATTENTION_UNILATERAL".equals(actionType)) {
			// 告知类
			if ("OBJ_FRIEND".equals(objectType)) {
				// 告知关注(对方并未加操作者为朋友)
				s1 = "在Ta的朋友圈里添加了您，把Ta添加到您的朋友圈里吧";
				objectName = "";
				// 选择朋友圈 忽略
				addFriend(bean);
			}
		} else if ("NOTIFY_ATTENTION_MUTUAL".equals(actionType)) {
			// 告知类
			if ("OBJ_FRIEND".equals(objectType)) {
				// 告知关注(对方已加操作者为朋友)
				s1 = "在TA的朋友圈里添加了您";
				s2 = "";
				objectName = "";

				reGreet(position, bean.getSendMemberId() + "",
						bean.getSendMemberName(), null, bean);
			}
		} else if ("COMMENT_RESOURCE".equals(actionType)) {
			// 评论类
			s1 = "评论了 ";
			if ("OBJ_GROUP_FILE".equals(objectType)) {
				// 评论圈子文件
				s2 = " 中的文件【";
				s3 = "】\n" + content;
			} else if ("OBJ_GROUP_PHOTO".equals(objectType)) {
				// 圈子照片
				s2 = " 中的照片【";
				s3 = "】\n" + content;
			} else if ("OBJ_GROUP_QUUBO".equals(objectType)
					|| "OBJ_GROUP_TOPIC".equals(objectType)) {
				// 圈博
				s2 = " 中的圈博【";
				s3 = "】\n" + content;
			} else if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(objectType)) {
				// 评论留言
				s2 = " 的留言【";
				s3 = "】\n" + content;
			} else if ("OBJ_MEMBER_INDIRECT_LEAVEMESSAGE".equals(objectType)) {
				// A评论了B给您的留言
				s2 = " 给您的留言【";
				s3 = "】\n" + content;
			} else if ("OBJ_MEMBER_RES_MOOD".equals(objectType)) {
				// 心情
				s2 = " 的心情【";
				s3 = "】\n" + content;
			}
		} else if ("REPLY_COMMENTRESOURCE".equals(actionType)) {
			// 回复资源
			s1 = "在 ";
			s2 = " 中提到您： ";
			s3 = "\n " + content;
			if ("OBJ_GROUP_FILE".equals(objectType)) {
				// 圈子文件
				objectName = "";
			} else if ("OBJ_GROUP_PHOTO".equals(objectType)) {
				// 圈子照片
				objectName = "";
			} else if ("OBJ_GROUP_QUUBO".equals(objectType)
					|| "OBJ_GROUP_TOPIC".equals(objectType)) {
				// 圈博
				objectName = "";
			} else if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(objectType)) {
				// 评论留言
				s1 = "在 ";
				s2 = " 的留言中提到您";
			} else if ("OBJ_MEMBER_RES_MOOD".equals(objectType)) {
				// 心情
				s1 = "在 ";
				s2 = " 的心情中提到您";
			} else if ("OBJ_OPEN_PAGE_FILE".equals(objectType)) {
				// 回复开放主页文件
				objectName = "";
			} else if ("OBJ_OPEN_PAGE_PHOTO".equals(objectType)) {
				// 回复开放主页照片
				objectName = "";
			} else if ("OBJ_OPEN_PAGE_QUUBO".equals(objectType)) {
				// 回复开放主页圈博
				objectName = "";
			}
		} else if ("NOTIFY_SEND_MAIL".equals(actionType)) {
			// 邮件通知
			if ("OBJ_MEMBER_MAIL".equals(objectType)) {
				// 新邮件通知
				s1 = "给您发来邮件";
				s2 = "";
				objectName = bean.getRemarkContent();
				if (objectName != null && !"".equals(objectName)) {
					objectName = "【" + objectName + "】";
				} else {
					objectName = "";
				}
			}
		} else if ("IDENTIFY_MAIL".equals(actionType)) {
			// 邮件指认
			if ("OBJ_MEMBER_IDENTIFY_MAIL".equals(objectType)) {
				// 指认通知
				s1 = "将邮箱：" + content + "指认给您，请登录该邮箱激活";
				objectName = "";
			}
		} else if ("NOTIFY_MAIL_IDENTIFIED".equals(actionType)) {
			// 指认成功
			if ("OBJ_MEMBER_NOTIFY_MAIL_IDENTIFIED".equals(objectType)) {
				// 指认成功通知
				// s1 = "邮箱：" + content + "已成功激活";
				// Xxx确认XXXXX@126.com.cn为TA的邮箱，尽情享受互动社区邮箱为您和朋友带来的便利吧。
				s1 = "确认" + content + "为TA的邮箱，尽情享受互动社区邮箱为您和TA带来的便利吧。";
				objectName = "";
			}
		} else if ("NOTIFY_OBJ_ATTENTION_COMMENT".equals(actionType)) {
			// 关注专题中圈博有新评论
			if ("OBJ_GROUP_QUUBO".equals(objectType)) {
				s1 = "";
				s2 = " 中和专题 " + bean.getRemarkContent() + " 相关的圈博【";
				s3 = "】有新评论";
			}
		} else if ("NOTIFY_OBJ_ATTENTION_CREATE".equals(actionType)) {
			// 关注专题有新圈博
			if ("OBJ_GROUP_QUUBO".equals(objectType)) {
				s1 = "在 ";
				s2 = " 中发布关于专题 " + bean.getRemarkContent() + " 的圈博【";
				s3 = "】";
			}
		} else if ("AT_MEMBERS_COMMENT".equals(actionType)) {
			// 在评论资源时@某人的
			if ("OBJ_GROUP_QUUBO".equals(objectType)) {
				// 圈子圈博
				s1 = "在 ";
				s2 = " 的圈博【";
				// s3 = "】中@您：" + content;
				s3 = "】中" + content;
			} else if ("OBJ_GROUP_FILE".equals(objectType)) {
				// 圈子文件
				s1 = "在 ";
				s2 = " 的文件【";
				// s3 = "】中@您：" + content;
				s3 = "】中" + content;
			} else if ("OBJ_GROUP_PHOTO".equals(objectType)) {
				// 圈子照片
				s1 = "在 ";
				s2 = " 的照片【";
				// s3 = "】中@您：" + content;
				s3 = "】中" + content;
			}
		} else if ("AT_MEMBERS_CREATE".equals(actionType)) {
			// 创建圈博时@某人
			if ("OBJ_GROUP_QUUBO".equals(objectType)) {
				// 圈子圈博
				s1 = "在 ";
				s2 = " 的圈博【";
				s3 = "】中@您";
			}
		} else if ("NOTICE_ACTION_TYPE_INVITE_SUCESS_BY_LETTER"
				.equals(actionType)) {
			if ("OBJ_FRIEND".equals(objectType)) {
				// objectName = "";
				s3 = " 已接受您的邀请加入了优优工作圈，系统奖励您50圈币，赶快把TA添加到您的朋友圈吧！";
				addFriend(bean);
			}
		} else if ("NOTICE_ACTION_TYPE_INVITE_SUCESS_BY_GROUP"
				.equals(actionType)) {
			if ("OBJ_FRIEND".equals(objectType)) {
				s1 = " 已接受您的邀请加入了优优工作圈，加入了圈子 ";
				s2 = " ，系统奖励您50圈币，顺便把TA添加到您的朋友圈吧！";
				objectName = "";
				addFriend(bean);
			}
		} else if ("NOTICE_ACTION_TYPE_INVITE_SUCESS_BY_FRIENDGROUP"
				.equals(actionType)) {
			if ("OBJ_FRIEND".equals(objectType)) {
				// objectName = "";
				s3 = " 已接受您的邀请加入了优优工作圈，系统奖励您50圈币！";
				// addFriend(bean);
				reGreet(position, bean.getObjectId() + "", objectName, null,
						bean);
			}
		} else if ("NOTICE_ACTION_TYPE_ACCEPTINVITE_DOWNLOADCLIENT_LOGIN"
				.equals(actionType)) {
			if ("OBJ_GOLD".equals(objectType)) {
				group = "";
				// s2 = "您邀请的 ";
				// s3 = " 安装了优优工作圈客户端，系统再奖励您50圈币（圈币可用来在线发短信）。";
				objectName = "";
				s3 = "已接受您的邀请并安装了优优工作圈客户端，系统再奖励您50圈币（圈币可用来在线发短信）。";
				reGreet(position, bean.getObjectId() + "", objectName, null,
						bean);
			}
		}
		return s1 + group + s2 + objectName + s3;
	}

	// private void setOwn(final MessageNoticeBean bean, ImageView iv,
	// TextView tv, int position) {
	// tv.setText(bean.getObjectName());
	//
	// String head = ThumbnailImageUrl.getThumbnailHeadUrl(bean.getObjectUrl()
	// + bean.getObjectPath(), HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
	//
	// iv.setTag(position + head);
	// MyFinalBitmap.setHeader(mContext, iv, head);
	//
	// iv.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// int id = bean.getObjectId();
	// if (id != 0) {
	// Bundle b = new Bundle();
	// b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
	// b.putString("id", id + "");
	// b.putString("name", bean.getObjectName());
	// LogicUtil.enter(mContext, HomePgActivity.class, b, false);
	// }
	// }
	// });
	// }

	// String objectType = bean.getObjectType();
	// String actionType = bean.getActionType();
	// if ("NOTICE_ACTION_TYPE_INVITE_SUCESS_BY_LETTER".equals(actionType)
	// && "OBJ_FRIEND".equals(objectType)) {
	//
	// } else if ("NOTICE_ACTION_TYPE_INVITE_SUCESS_BY_GROUP"
	// .equals(actionType) && "OBJ_FRIEND".equals(objectType)) {
	//
	// } else if ("NOTICE_ACTION_TYPE_INVITE_SUCESS_BY_FRIENDGROUP"
	// .equals(actionType) && "OBJ_FRIEND".equals(objectType)) {
	//
	// } else if ("NOTICE_ACTION_TYPE_ACCEPTINVITE_DOWNLOADCLIENT_LOGIN"
	// .equals(actionType) && "OBJ_GOLD".equals(objectType)) {
	//
	// }

	/**
	 * 获取新手任务名称
	 * 
	 * @param type
	 * @return
	 */
	private String taskName(String type) {
		if ("MODIFY_MEMBER_HEAD".equals(type)) {
			return "编辑头像";
		} else if ("MODIFY_MEMBER_NAME".equals(type)) {
			return "编辑名字";
		} else if ("MODIFY_MEMBER_BIRTHDAY".equals(type)) {
			return "编辑生日";
		} else if ("MODIFY_MEMBER_HOBBY".equals(type)) {
			return "编辑兴趣爱好";
		} else if ("MODIFY_MEMBER_COMPANY".equals(type)) {
			return "编辑工作单位";
		} else if ("MODIFY_MEMBER_SCHOOL".equals(type)) {
			return "编辑教育背景";
		} else if ("MODIFY_MEMBER_MAIL".equals(type)) {
			return "编辑联系方式";
		} else if ("MODIFY_MEMBER_COMPANY".equals(type)) {
			return "编辑个人简介";
		} else if ("PUBLISH_MEMBER_MOOD".equals(type)) {
			return "发表心情";
		} else if ("PUBLISH_MEMBER_POSTER".equals(type)) {
			return "发表轻博客";
		} else if ("MEMBER_CREATE_GROUP".equals(type)) {
			return "创建交流圈";
		}
		return type;
	}

	/**
	 * 打招呼、发私信
	 * 
	 * @param memberId
	 * @param memberName
	 * @param noticeGreetId
	 */
	private void reGreet(final int position, final String memberId,
			final String memberName, final String noticeGreetId,
			final MessageNoticeBean bean) {
		holder.b1.setVisibility(View.VISIBLE);
		holder.b1.setText("打招呼");
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				recallListener.onRecall(position, memberId, memberName,
						noticeGreetId);
			}
		});

		holder.b2.setVisibility(View.VISIBLE);
		holder.b2.setText("发私信");
		holder.b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle secret = new Bundle();
				secret.putString("memberId", memberId);
				secret.putString("name", memberName);
				secret.putString("head",
						bean.getObjectUrl() + bean.getObjectPath());
				LogicUtil.enter(mContext, QChatActivity.class, secret, false);

				deleteNotices(bean);
			}
		});
	}

	/**
	 * 回复推荐朋友圈
	 * 
	 * @param bean
	 */
	private void replyRecommendFriendGroup(final MessageNoticeBean bean) {
		holder.b1.setVisibility(View.VISIBLE);
		holder.b1.setText("接	受");
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REMOVEFRIENDFROMGROUPTASK);
				removeFriendFromGroupTask = new RemoveFriendFromGroupTask(bean
						.getSendObjectName(), bean);
				removeFriendFromGroupTask.execute();
			}
		});
		holder.b2.setVisibility(View.VISIBLE);
		holder.b2.setText("忽	略");
		holder.b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteNotices(bean);
			}
		});
	}

	/**
	 * 回复圈子上下级
	 * 
	 * @param bean
	 * @param isSub
	 *            true:申请 成为 的下级圈子, false:申请 成为 的上级圈子
	 */
	private void replySubOrSupGroup(final MessageNoticeBean bean,
			final boolean isSub) {
		holder.b1.setVisibility(View.VISIBLE);
		holder.b1.setText("同	意");
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REPLYGROUPAPPLY);
				if (isSub) {
					replyGroupApplyTask = new ReplyGroupApplyTask(
							"replySubGroupApply", true, bean);
				} else {
					replyGroupApplyTask = new ReplyGroupApplyTask(
							"replySupGroupApply", true, bean);
				}
				replyGroupApplyTask.execute();
			}
		});

		holder.b2.setVisibility(View.VISIBLE);
		holder.b2.setText("忽	略");
		holder.b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteNotices(bean);
			}
		});
	}

	/**
	 * 回复圈子合作
	 * 
	 * @param bean
	 */
	private void replyCollaborateGroup(final MessageNoticeBean bean) {
		holder.b1.setVisibility(View.VISIBLE);
		holder.b1.setText("同	意");
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REPLYGROUPAPPLY);
				replyGroupApplyTask = new ReplyGroupApplyTask(
						"replyCollaborate", true, bean);
				replyGroupApplyTask.execute();
			}
		});

		holder.b2.setVisibility(View.VISIBLE);
		holder.b2.setText("忽	略");
		holder.b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REPLYGROUPAPPLY);
				replyGroupApplyTask = new ReplyGroupApplyTask(
						"replyCollaborate", false, bean);
				replyGroupApplyTask.execute();
			}
		});
	}

	/**
	 * 回复申请圈子合并
	 * 
	 * @param bean
	 */
	private void replyCombineGroup(final MessageNoticeBean bean) {
		holder.b1.setVisibility(View.VISIBLE);
		holder.b1.setText("同	意");
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REPLYGROUPAPPLY);
				replyGroupApplyTask = new ReplyGroupApplyTask("replyCombine",
						true, bean);
				replyGroupApplyTask.execute();
			}
		});

		holder.b2.setVisibility(View.VISIBLE);
		holder.b2.setText("忽	略");
		holder.b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REPLYGROUPAPPLY);
				replyGroupApplyTask = new ReplyGroupApplyTask("replyCombine",
						false, bean);
				replyGroupApplyTask.execute();
			}
		});
	}

	/**
	 * 回复申请圈子管理员
	 * 
	 * @param bean
	 */
	private void replyGroupManager(final MessageNoticeBean bean) {
		holder.b1.setVisibility(View.VISIBLE);
		holder.b1.setText("同	意");
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REPLYAPPLY);
				replyApplyTask = new ReplyApplyTask("replyManagerApply", 1,
						bean);
				replyApplyTask.execute();
			}
		});

		holder.b2.setVisibility(View.VISIBLE);
		holder.b2.setText("忽	略");
		holder.b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REPLYAPPLY);
				replyApplyTask = new ReplyApplyTask("replyManagerApply", 2,
						bean);
				replyApplyTask.execute();
			}
		});
	}

	/**
	 * 回复加入圈子
	 * 
	 * @param bean
	 */
	private void replyAddGroup(final MessageNoticeBean bean) {
		holder.b1.setVisibility(View.VISIBLE);
		holder.b1.setText("同	意");
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REPLYAPPLY);
				replyApplyTask = new ReplyApplyTask("replyJoinApply", 1, bean);
				replyApplyTask.execute();
			}
		});

		holder.b2.setVisibility(View.VISIBLE);
		holder.b2.setText("忽	略");
		holder.b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTask(REPLYAPPLY);
				replyApplyTask = new ReplyApplyTask("replyJoinApply", 2, bean);
				replyApplyTask.execute();
			}
		});
	}

	/**
	 * 添加朋友圈、忽略
	 * 
	 * @param bean
	 */
	private void addFriend(final MessageNoticeBean bean1) {
		holder.b1.setVisibility(View.VISIBLE);
		holder.b1.setText("加入朋友圈");
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChooseGroupBean bean = new ChooseGroupBean();
				String actionType = bean1.getActionType();
				String objectType = bean1.getObjectType();
				if ("RECOMMEND_ADD".equals(actionType)
						|| "NOTICE_ACTION_TYPE_INVITE_SUCESS_BY_LETTER"
								.equals(actionType)) {
					// 告知类
					if ("OBJ_FRIEND".equals(objectType)
							|| "OBJ_GROUP_LEAGUER".equals(objectType)) {
						bean.setName(new String[] { bean1.getObjectName() },
								null);
						bean.setMemberId(bean1.getObjectId());
					}
				} else {
					bean.setName(new String[] { bean1.getSendMemberName() },
							null);
					bean.setMemberId(bean1.getSendMemberId());
				}
				// bean.setName(new String[] { bean1.getObjectName() }, null);
				bean.setTitle("加入朋友圈");
				bean.setChosenGroupMap(new HashMap<String, Object>());
				ChooseGroupsDialogActivity.mCurrent = bean1;
				Bundle bundle = new Bundle();
				bundle.putSerializable(BundleKey.CHOOSEGROUPBEAN, bean);
				ChooseGroupsDialogActivity.setIsNeedRefresh(true);
				LogicUtil.enter(mContext, ChooseGroupsDialogActivity.class,
						bundle, ChooseGroupsDialogActivity.CHOOSEGROUPOFFRIEND);
			}
		});

		holder.b2.setVisibility(View.VISIBLE);
		holder.b2.setText("忽	略");
		holder.b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteNotices(bean1);
			}
		});
	}

	public void remove(MessageNoticeBean bean) {
		stopTask(UPDATENOTICESTATE);
		deleteNoticesStateTask = new DeleteNoticesStateTask(bean, false);
		deleteNoticesStateTask.execute();
	}

	// private void loadImg(Drawable imageDrawable, String imageUrl) {
	// ImageView imgView = (ImageView) mListView.findViewWithTag(imageUrl);
	// if (null != imgView) {
	// if (null != imageDrawable) {
	// imgView.setImageDrawable(imageDrawable);
	// }
	// }
	// }

	static class ViewHolder {
		ImageView img, head, details;
		TextView time, text, name, b1, b2, num;
		LinearLayout textlayout;
	}

	private void stopTask(int i) {
		switch (i) {
		case LOADINFO:
			if (null != loadInfoTask
					&& loadInfoTask.getStatus() == AsyncTask.Status.RUNNING) {
				loadInfoTask.cancel(true);
			}
			break;
		case REPLYAPPLY:
			if (null != replyApplyTask
					&& replyApplyTask.getStatus() == AsyncTask.Status.RUNNING) {
				replyApplyTask.cancel(true);
			}
			break;
		case UPDATENOTICESTATE:
			if (null != deleteNoticesStateTask
					&& deleteNoticesStateTask.getStatus() == AsyncTask.Status.RUNNING) {
				deleteNoticesStateTask.cancel(true);
			}
			break;
		case REPLYGROUPAPPLY:
			if (null != replyGroupApplyTask
					&& replyGroupApplyTask.getStatus() == AsyncTask.Status.RUNNING) {
				replyGroupApplyTask.cancel(true);
			}
			break;
		case REMOVEFRIENDFROMGROUPTASK:
			if (null != removeFriendFromGroupTask
					&& removeFriendFromGroupTask.getStatus() == AsyncTask.Status.RUNNING) {
				removeFriendFromGroupTask.cancel(true);
			}
			break;
		case RESOURCEINFO:
			if (null != resourceInfoTask
					&& resourceInfoTask.getStatus() == AsyncTask.Status.RUNNING) {
				resourceInfoTask.cancel(true);
			}
			break;
		case FILEINFO:
			if (null != fileInfoTask
					&& fileInfoTask.getStatus() == AsyncTask.Status.RUNNING) {
				fileInfoTask.cancel(true);
			}
			break;
		default:
			break;
		}

	}

	public void deleteNotices(MessageNoticeBean bean) {
		stopTask(UPDATENOTICESTATE);
		deleteNoticesStateTask = new DeleteNoticesStateTask(bean, true);
		deleteNoticesStateTask.execute();
	}

	class DeleteNoticesStateTask extends AsyncTask<String, Integer, MCResult> {

		private MessageNoticeBean noticeBean;
		private boolean showTip = true;

		public DeleteNoticesStateTask(MessageNoticeBean noticeBean,
				boolean showTip) {
			this.noticeBean = noticeBean;
			this.showTip = showTip;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.deleteNotices(App.app,
						noticeBean.getNoticeId() + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			updateList(mcResult, noticeBean, showTip);
		}
	}

	class ReplyGroupApplyTask extends AsyncTask<String, Integer, MCResult> {

		private String method;
		private boolean isAgree;
		private MessageNoticeBean noticeBean;

		public ReplyGroupApplyTask(String method, boolean isAgree,
				MessageNoticeBean noticeBean) {
			this.method = method;
			this.isAgree = isAgree;
			this.noticeBean = noticeBean;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.replyGroupOper(App.app, method,
						noticeBean.getSendObjectId() + "",
						noticeBean.getObjectId() + "", isAgree);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			updateList(mcResult, noticeBean, false);
		}
	}

	/**
	 * 
	 * @param mcResult
	 * @param noticeBean
	 */
	private void updateList(MCResult mcResult, MessageNoticeBean noticeBean,
			boolean showTip) {
		if (null == mcResult) {
			if (showTip)
				showTip(T.ErrStr);
		} else {
			if (1 != mcResult.getResultCode()) {
				if (showTip)
					showTip(T.ErrStr);
			} else {
				noticeInfo.remove(noticeBean);
				notifyDataSetChanged();
				// if (showTip)
				// showTip("已删除！");
				refersh = true;
			}
		}
	}

	class RemoveFriendFromGroupTask extends
			AsyncTask<String, Integer, MCResult> {

		private String groupName;
		private MessageNoticeBean noticeBean;

		public RemoveFriendFromGroupTask(String groupName,
				MessageNoticeBean noticeBean) {
			this.groupName = groupName;
			this.noticeBean = noticeBean;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.acceptRecommended(mContext,
						groupName, noticeBean.getSendObjectId() + "",
						noticeBean.getObjectId() + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else {
					String mc = mcResult.getResult().toString();
					if ("true".equals(mc)) {
						// TODO 新API发布后去掉
						final EditText editText = new EditText(mContext);
						editText.setMaxLines(50);
						editText.setBackgroundResource(R.drawable.edit_bg);
						if (groupName != null && !groupName.equals("")) {
							editText.setText(groupName);
							editText.setSelection(groupName.length());
						}
						new AlertDialog.Builder(mContext)
								.setTitle("此朋友圈名字不合法或者与现有的朋友圈重名，换一个吧！")
								.setView(editText)
								.setNegativeButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												BaseData.hideKeyViewBoard(
														(Activity) mContext,
														editText);
												stopTask(REMOVEFRIENDFROMGROUPTASK);
												removeFriendFromGroupTask = new RemoveFriendFromGroupTask(
														editText.getEditableText()
																.toString(),
														noticeBean);
												removeFriendFromGroupTask
														.execute();
											}
										})
								.setPositiveButton("取消",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												BaseData.hideKeyViewBoard(
														(Activity) mContext,
														editText);
											}
										}).show();
						return;
					}
					try {
						final JSONObject json = new JSONObject(mc);
						int STATUS = json.getInt("STATUS");
						if (STATUS == 1) {
							deleteNotices(noticeBean);
							new AlertDialog.Builder(mContext)
									.setTitle("优优工作圈")
									.setMessage(
											"已接受 "
													+ noticeBean
															.getSendMemberName()
													+ " 分享的朋友圈！现在就可以去您的朋友圈查看了！")
									.setPositiveButton("以后再说", null)
									.setNegativeButton(
											"去看看",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													try {
														Bundle bundle = new Bundle();
														bundle.putString(
																"id_Group",
																json.getInt("GROUPID")
																		+ "");
														// TODO 新API发布后放开
														// bundle.putInt(
														// "num_Total",
														// json.getInt("RECOMMEND_NUM"));
														bundle.putString(
																"name_Group",
																json.getString("GROUPNAME"));
														LogicUtil
																.enter(mContext,
																		GroupFriendActivity.class,
																		bundle,
																		false);
													} catch (JSONException e) {
														e.printStackTrace();
													}
												}
											}).show();
							noticeInfo.remove(noticeBean);
							notifyDataSetChanged();
							refersh = true;
						} else if (STATUS == 2 || STATUS == 4) {
							final EditText editText = new EditText(mContext);
							editText.setMaxLines(50);
							if (groupName != null && !groupName.equals("")) {
								editText.setText(groupName);
								editText.setSelection(groupName.length());
							}
							new AlertDialog.Builder(mContext)
									.setTitle("此朋友圈名字不合法或者与现有的朋友圈重名，换一个吧！")
									.setView(editText)
									.setNegativeButton(
											"确定",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													BaseData.hideKeyViewBoard(
															(Activity) mContext,
															editText);
													stopTask(REMOVEFRIENDFROMGROUPTASK);
													removeFriendFromGroupTask = new RemoveFriendFromGroupTask(
															editText.getEditableText()
																	.toString(),
															noticeBean);
													removeFriendFromGroupTask
															.execute();
												}
											})
									.setPositiveButton(
											"取消",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													BaseData.hideKeyViewBoard(
															(Activity) mContext,
															editText);
												}
											}).show();
						} else if (STATUS == 5) {
							showTip("创建好友分组失败，好友数量已达上限");
						} else {
							showTip(T.ErrStr);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	class ReplyApplyTask extends AsyncTask<String, Integer, MCResult> {

		private String method;
		private int replyType;
		private MessageNoticeBean noticeBean;

		public ReplyApplyTask(String method, int replyType,
				MessageNoticeBean noticeBean) {
			this.method = method;
			this.replyType = replyType;
			this.noticeBean = noticeBean;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.replyGroupApply(App.app, method,
						noticeBean.getSendObjectId() + "",
						noticeBean.getSendMemberId() + "", replyType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			updateList(mcResult, noticeBean, false);
		}
	}

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private String groupId, objectId, objectType;

		public LoadInfoTask(String groupId, String objectId, String objectType) {
			this.groupId = groupId;
			this.objectId = objectId;
			this.objectType = objectType;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers
						.quuboInfo(App.app, groupId, objectId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else {
					QuuboInfoBean quuboInfoBean = (QuuboInfoBean) mcResult
							.getResult();
					if (quuboInfoBean.getQuuboId() == 0) {
						showTip("该资源已被删除。");
					} else {
						Bundle b = new Bundle();
						b.putSerializable("info", quuboInfoBean);
						b.putString("type_From", TypeUtil.type_ResultMessage);
						b.putString("type_Object", objectType);
						LogicUtil.enter(mContext,
								CircleBlogDetailsActivity.class, b, false);
					}
				}
			}
		}
	}

	class ResourceInfoTask extends AsyncTask<String, Integer, MCResult> {
		private String groupId, objectId, objectType;

		public ResourceInfoTask(String groupId, String objectId,
				String objectType) {
			this.groupId = groupId;
			this.objectId = objectId;
			this.objectType = objectType;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				if ("Theme".equals(objectType)) {
					result = APIThemeRequestServers.themeInfo(mContext,
							objectId);
				} else {
					result = APIRequestServers.resourceInfo(App.app, groupId,
							objectId, objectType);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else {
					if ("Theme".equals(objectType)) {
						GroupThemeBean groupThemeBean = (GroupThemeBean) mcResult
								.getResult();
						if (groupThemeBean.getThemeId() == 0) {
							showTip("该专题已删除。");
						} else {
							Bundle b = new Bundle();
							b.putSerializable("data", groupThemeBean);
							LogicUtil.enter(mContext, TopicActivity.class, b,
									false);
						}
					} else {
						ResourceBean resourceBean = (ResourceBean) mcResult
								.getResult();
						if (resourceBean.getObjectId() == 0) {
							showTip("该资源已删除。");
						} else {
							Bundle b = new Bundle();
							b.putSerializable("info", resourceBean);
							b.putString("type_From", "resource");
							LogicUtil.enter(mContext,
									CircleBlogDetailsActivity.class, b, false);
						}
					}
				}
			}
		}
	}

	class FileInfoTask extends AsyncTask<String, Integer, MCResult> {
		private String fileId;

		public FileInfoTask(String fileId) {
			this.fileId = fileId;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIFileRequestServers.fileInfo(App.app,
						Integer.valueOf(fileId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else {
					FileInfoBean fileInfoBean = (FileInfoBean) mcResult
							.getResult();
					if (fileInfoBean != null) {
						Bundle b = new Bundle();
						b.putSerializable("fileInfoBean", fileInfoBean);
						LogicUtil.enter(mContext, FileDetailActivity.class, b,
								false);
					} else {
						showTip("此文件已被删除");
					}
				}
			}
		}
	}

	private void showTip(String text) {
		T.show(App.app, text);
	}

}

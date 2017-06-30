package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.CommentSendService;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.fragmt.GroupOrMemberSpan;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendSimpleBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceCommentBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FaceUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

@SuppressWarnings("deprecation")
public class CommentAdapter extends BaseAdapter {
	private static final String TAG = "CommentAdapter";
	private Context context;
	private List<ResourceCommentBean> beans_ResourceComment;
	private ViewHolder holder;
	private Handler handler;
	private int id_Default_Member;
	private int[] mHandlerIds;

	private DeleteCommentStateTask deleteCommentStateTask;
	private String objectId, objectType;

	private String groupId;
	private boolean hasDelete = false;

	public void setHasDelete(boolean hasDelete) {
		this.hasDelete = hasDelete;
	}

	private Handler infoHandler;

	public void setInfoHandler(Handler infoHandler) {
		this.infoHandler = infoHandler;
	}

	public CommentAdapter(List<ResourceCommentBean> beans_ResourceComment,
			Context context, Handler handler, int id_Default_Member,
			int[] handlerIds, String groupId, String objectId, String objectType) {
		// if (beans_ResourceComment != null) {
		this.beans_ResourceComment = beans_ResourceComment;
		// } else {
		// this.beans_ResourceComment = new ArrayList<ResourceCommentBean>();
		// }
		this.context = context;
		this.handler = handler;
		this.id_Default_Member = id_Default_Member;
		this.groupId = groupId;
		this.objectId = objectId;
		this.objectType = objectType;
		mHandlerIds = handlerIds;
	}

	@Override
	public int getCount() {
		return beans_ResourceComment.size();
	}

	@Override
	public Object getItem(int arg0) {
		return beans_ResourceComment.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		if (null == arg1 || arg1 instanceof TextView) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.circleblogdetailsitemform, null);
			holder.head_img = (ImageView) arg1.findViewById(R.id.head_img);
			holder.name = (TextView) arg1
					.findViewById(R.id.circleblogdetailsitemform_name);
			holder.comment = (TextView) arg1
					.findViewById(R.id.circleblogdetailsitemform_info);
			holder.timeandfrom = (TextView) arg1
					.findViewById(R.id.circleblogdetailsitemform_timeandfrom);
			holder.reply = (ImageView) arg1
					.findViewById(R.id.circleblogdetailsitemform_reply);
			holder.delete = (TextView) arg1
					.findViewById(R.id.circleblogdetailsitemform_delete);

			holder.bar = (ProgressBar) arg1
					.findViewById(R.id.circleblogdetailsitemform_loading);
			holder.resend = (ImageView) arg1
					.findViewById(R.id.circleblogdetailsitemform_resend);
			arg1.setTag(holder);
			arg1.setFocusable(true);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		final ResourceCommentBean bean_ResourceComment = (ResourceCommentBean) beans_ResourceComment
				.get(arg0);
		String name = bean_ResourceComment.getMemberName();
		if (id_Default_Member == bean_ResourceComment.getMemberId()) {
			name = "我";
		}
		holder.name.setText(name);

		String content = bean_ResourceComment.getCommentContent();
		// content = FaceUtil.faceToHtml(content);
		if (content == null)
			content = "";
		content = content.replace("&yen;", "¥");
		SpannableStringBuilder ssb = new SpannableStringBuilder(content);
		// ssb.append(Html.fromHtml(content, imageGetter, null));
		for (int i = 0; i < FaceUtil.FACE_TEXTS.length; i++) {
			String face = FaceUtil.FACE_TEXTS[i];
			if (content.contains(face)) {
				for (int j = 0; j <= content.length() - face.length(); j++) {
					if (content.substring(j, j + face.length()).equals(face)) {
						Drawable drawable = context.getResources().getDrawable(
								FaceUtil.FACE_RES_IDS[i]);
						int width = drawable.getIntrinsicWidth();
						int height = drawable.getIntrinsicHeight();
						drawable.setBounds(0, 0, width / 2, height / 2);
						ImageSpan span = new ImageSpan(drawable,
								ImageSpan.ALIGN_BASELINE);
						ssb.setSpan(span, j, j + face.length(),
								Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}

		List<FriendSimpleBean> atMemberInfos = bean_ResourceComment
				.getAtMemberInfos();
		if (atMemberInfos != null && atMemberInfos.size() > 0) {
			for (FriendSimpleBean friendSimpleBean : atMemberInfos) {
				String aStr = "@" + friendSimpleBean.getMemberName() + "：";
				L.i(TAG, "aStr=" + aStr);
				for (int i = 0; i <= content.length() - aStr.length(); i++) {
					if (content.substring(i, i + aStr.length()).equals(aStr)) {
						String fName = "@" + friendSimpleBean.getFriendName()
								+ "：";
						ssb.replace(i, i + aStr.length(), fName);
						ssb.setSpan(new GroupOrMemberSpan(context, "MemberId#"
								+ friendSimpleBean.getMemberId()), i,
								i + fName.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		holder.comment.setText(ssb);
		holder.comment.setMovementMethod(LinkMovementMethod.getInstance());

		String time = DateTimeUtil.aTimeFormat(bean_ResourceComment
				.getCommentTime());
		long time_Comment = bean_ResourceComment.getSendStatus();
		if (time_Comment == -1) {
			time = "发送中";
		} else if (time_Comment == -2) {
			time = "发送失败";
		}

		holder.timeandfrom.setText(time + "  "
				+ SendWay.resoureSendWay(bean_ResourceComment.getCommentWay()));
		String head_path = bean_ResourceComment.getFullHeadPath();
		head_path = ThumbnailImageUrl.getThumbnailHeadUrl(head_path,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		// 对头像进行异步加载。
		holder.head_img.setTag(bean_ResourceComment.getMemberId());
		final String memberName = bean_ResourceComment.getMemberName();
		MyFinalBitmap.setHeader(context, holder.head_img, head_path);

		holder.head_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String temp = v.getTag().toString();
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", temp);
				b.putString("name", memberName);
				LogicUtil.enter(context, HomePgActivity.class, b, false);
			}
		});

		// L.d(TAG, "getView hasDelete=" + hasDelete);
		if (id_Default_Member == bean_ResourceComment.getMemberId()
				|| hasDelete) {
			if (time_Comment == -1) {
				holder.bar.setVisibility(View.VISIBLE);
				holder.resend.setVisibility(View.GONE);
				holder.delete.setVisibility(View.GONE);
			} else if (time_Comment == -2) {
				holder.bar.setVisibility(View.GONE);
				holder.resend.setVisibility(View.VISIBLE);
				holder.delete.setVisibility(View.GONE);
				holder.resend.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(context).setItems(
								new String[] { "重新发送", "删除", "取消" },
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											beans_ResourceComment.remove(arg0);
											bean_ResourceComment
													.setSendStatus(-1);
											beans_ResourceComment.add(arg0,
													bean_ResourceComment);
											notifyDataSetChanged();
											new Thread() {
												@Override
												public void run() {
													try {
														MCResult mcResult = null;
														String[] rIds = bean_ResourceComment
																.getReceiveIds();
														if (null != rIds
																&& rIds.length > 0) {
															mcResult = APIRequestServers
																	.commentResource(
																			App.app,
																			groupId,
																			objectId,
																			objectType,
																			bean_ResourceComment
																					.getCommentContent(),
																			rIds);
														} else {
															mcResult = APIRequestServers
																	.commentResource(
																			App.app,
																			groupId,
																			objectId,
																			objectType,
																			bean_ResourceComment
																					.getCommentContent(),
																			null);
														}
														int Code_result = mcResult
																.getResultCode();
														if (Code_result == 1) {
															if (infoHandler != null) {
																Message msg = new Message();
																msg.obj = new String[] {
																		bean_ResourceComment
																				.getCommentContent(),
																		bean_ResourceComment
																				.getFullHeadPath() };
																infoHandler
																		.sendMessage(msg);
															} else {
																L.i(TAG,
																		"infoHandler = null");
															}
															CommentSendService
																	.getService(
																			context)
																	.deleteComment(
																			bean_ResourceComment
																					.getCommentTime());

															beans_ResourceComment
																	.remove(arg0);
															try {
																JSONObject jsonObject = new JSONObject(
																		mcResult.getResult()
																				.toString());
																int commentId = jsonObject
																		.getInt("COMMENT_ID");
																bean_ResourceComment
																		.setCommentId(commentId);
																bean_ResourceComment
																		.setCommentTime(System
																				.currentTimeMillis());
															} catch (Exception e) {
																e.printStackTrace();
															}
															bean_ResourceComment
																	.setSendStatus(0);
														} else {
															beans_ResourceComment
																	.remove(arg0);
															bean_ResourceComment
																	.setSendStatus(-2);
														}
													} catch (Exception e) {
														e.printStackTrace();
														beans_ResourceComment
																.remove(arg0);
														bean_ResourceComment
																.setSendStatus(-2);
													}
													beans_ResourceComment
															.add(arg0,
																	bean_ResourceComment);
													notifyDataSetChanged();
												}
											}.run();
										} else if (which == 1) {
											beans_ResourceComment.remove(arg0);
											notifyDataSetChanged();
											CommentSendService.getService(
													context).deleteComment(
													bean_ResourceComment
															.getCommentTime());
											L.d(TAG,
													"CommentTime="
															+ bean_ResourceComment
																	.getCommentTime());
										}
										dialog.dismiss();
									}
								}).show();
					}
				});
			} else {
				holder.bar.setVisibility(View.GONE);
				holder.resend.setVisibility(View.GONE);
				holder.delete.setVisibility(View.VISIBLE);
			}
			holder.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(context)
							.setTitle("删除提示")
							.setMessage("是否删除该条评论？")
							.setPositiveButton("是",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											stopTask();
											deleteCommentStateTask = new DeleteCommentStateTask(
													bean_ResourceComment);
											deleteCommentStateTask.execute();
										}
									}).setNegativeButton("否", null).show();
				}
			});
		} else {
			holder.bar.setVisibility(View.GONE);
			holder.resend.setVisibility(View.GONE);
			holder.delete.setVisibility(View.GONE);
		}
		if (id_Default_Member != bean_ResourceComment.getMemberId()) {
			holder.reply.setVisibility(View.VISIBLE);
			final String[] temp = new String[] {
					bean_ResourceComment.getMemberName(),
					String.valueOf(bean_ResourceComment.getMemberId()),
					String.valueOf(arg0) };
			holder.head_img.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					Message msg = Message.obtain();
					msg.what = mHandlerIds[0];
					msg.obj = temp;
					msg.arg1 = arg0;
					msg.arg2 = ((View) ((View) v.getParent()).getParent())
							.getBottom();
					handler.sendMessage(msg);
					return true;
				}
			});
			holder.reply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Message msg = Message.obtain();
					msg.what = mHandlerIds[0];
					msg.obj = temp;
					msg.arg1 = arg0;
					msg.arg2 = ((View) ((View) v.getParent()).getParent())
							.getBottom();
					handler.sendMessage(msg);
				}
			});
		} else {
			holder.reply.setVisibility(View.GONE);
		}
		holder.comment.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				ClipboardManager clipboardManager = (ClipboardManager) context
						.getSystemService(Context.CLIPBOARD_SERVICE);
				clipboardManager.setText(bean_ResourceComment
						.getCommentContent());
				T.show(context, "已复制到剪切板");
				return true;
			}
		});
		return arg1;
	}

	class ViewHolder {
		ImageView head_img, reply;
		TextView name, comment, timeandfrom, delete;

		ImageView resend;
		ProgressBar bar;
	}

	// /**
	// *
	// * @param temp_Beans
	// * @param location
	// * the index at which to insert when location is zero
	// * @param isRefresh
	// */
	// public void add(List<ResourceCommentBean> temp_Beans, int location,
	// boolean isRefresh) {
	// try {
	// if (isRefresh)
	// beans_ResourceComment.clear();
	//
	// if (location == 0)
	// beans_ResourceComment.addAll(location, temp_Beans);
	// else
	// beans_ResourceComment.addAll(temp_Beans);
	// notifyDataSetChanged();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// /**
	// * 局部刷新
	// *
	// * @param rcb
	// * @param location
	// */
	// public void refresh(ResourceCommentBean rcb, int location) {
	// if (location == -1) {
	// beans_ResourceComment.add(0, rcb);
	// } else {
	// beans_ResourceComment.remove(location);
	// beans_ResourceComment.add(location, rcb);
	// }
	// // notifyDataSetChanged();
	// }

	private void stopTask() {
		if (null != deleteCommentStateTask
				&& deleteCommentStateTask.getStatus() == AsyncTask.Status.RUNNING) {
			deleteCommentStateTask.cancel(true);
		}
	}

	class DeleteCommentStateTask extends AsyncTask<String, Integer, MCResult> {

		private ResourceCommentBean bean;

		public DeleteCommentStateTask(ResourceCommentBean bean) {
			this.bean = bean;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers
						.deleteCommentResource(context, groupId, objectId,
								bean.getCommentId() + "", objectType);
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
					try {
						int i = (Integer) mcResult.getResult();
						if (i == 1) {
							// showTip("已删除！");
							beans_ResourceComment.remove(bean);
							notifyDataSetChanged();
							handler.sendEmptyMessage(mHandlerIds[1]);
						} else {
							showTip(T.ErrStr);
						}
					} catch (Exception e) {
						showTip(T.ErrStr);
						e.printStackTrace();
					}

				}
			}
		}
	}

	private void showTip(String text) {
		T.show(context, text);
	}
}

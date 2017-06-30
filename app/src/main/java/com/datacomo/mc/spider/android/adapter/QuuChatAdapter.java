package com.datacomo.mc.spider.android.adapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.ChatGroupChooseActivity;
import com.datacomo.mc.spider.android.FriendsChooserActivity;
import com.datacomo.mc.spider.android.GroupsChooserActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.PhotoGalleryActivity;
import com.datacomo.mc.spider.android.QuuChatActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.MyQuuMessageBean;
import com.datacomo.mc.spider.android.bean.MyQuuMessageBean.LoadQuuMessageInfo;
import com.datacomo.mc.spider.android.db.ChatGroupMessageBeanService;
import com.datacomo.mc.spider.android.db.QChatSendService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.manager.ChatOperateAlertManager;
import com.datacomo.mc.spider.android.manager.MediaManager;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.groupchat.GroupChatMessageBean;
import com.datacomo.mc.spider.android.net.been.groupchat.ObjectInfoBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.TaskUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.util.CustomPlayer.OnCustomCompletionListener;
import com.datacomo.mc.spider.android.view.ChatLinearLayout;
import com.datacomo.mc.spider.android.view.MixedTextView;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.wxapi.WXEntryActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

@SuppressWarnings("deprecation")
public class QuuChatAdapter extends BaseAdapter {
	private final String TAG = "QuuChatAdapter";

	private Context c;
	private ArrayList<MyQuuMessageBean> chatInfos;
	private Resources res;
	private RefreshListView listView;
	private HashMap<String, String> map_Id;
	private UserBusinessDatabase business;
	private String session_key, headUrlPath;
	private String mBerId, groupId;
	private final int mMaxWH;
	private final int mMinWH;
	private final int mMaxScale = 5;
	private final int mMaxZoomScale = 3;
	private int maxLong = 60;
	private int contentWidth;
	private int screenWidth;
	private ImageView curV;

	private DownLoadTask downLoadTask;

	public QuuChatAdapter(Context context, ArrayList<MyQuuMessageBean> infos,
			ListView lv, String id, String groupId) {
		c = context;
		chatInfos = infos;
		res = c.getResources();
		listView = (RefreshListView) lv;
		map_Id = new HashMap<String, String>();
		mBerId = id;
		this.groupId = groupId;
		business = new UserBusinessDatabase(App.app);
		session_key = App.app.share.getSessionKey();
		try {
			headUrlPath = business.getHeadUrlPath(session_key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
				TaskUtil.IMGDEFAULTLOADSTATEIMG[0]);
		L.d(TAG, "width:" + bm.getWidth());
		mMaxWH = bm.getWidth();
		mMinWH = mMaxWH / 6;
		screenWidth = BaseData.getScreenWidth();
		contentWidth = screenWidth / 2 - 30;
		MediaManager.initPlayer();
	}

	@Override
	public int getCount() {
		return chatInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return chatInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ChatViewHolder holder = null;
		if (null == convertView) {
			holder = new ChatViewHolder();
			convertView = View.inflate(c, R.layout.item_quu_chat, null);
			holder.left = convertView.findViewById(R.id.left);
			holder.right = convertView.findViewById(R.id.right);
			holder.leftHead = (ImageView) holder.left
					.findViewById(R.id.head_img);
			holder.rightHead = (ImageView) holder.right
					.findViewById(R.id.head_img);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.notice = (TextView) convertView.findViewById(R.id.notice);
			holder.ll = (ChatLinearLayout) convertView.findViewById(R.id.ll);
			holder.out = (LinearLayout) convertView.findViewById(R.id.ll_out);
			holder.linear = (LinearLayout) convertView
					.findViewById(R.id.linear);
			holder.signLeft = (ImageView) convertView
					.findViewById(R.id.sign_left);
			holder.textLeft = (TextView) convertView
					.findViewById(R.id.video_text_left);
			holder.textRight = (TextView) convertView
					.findViewById(R.id.video_text_right);
			holder.bar = (ProgressBar) convertView.findViewById(R.id.loading);
			convertView.setTag(holder);
		} else {
			holder = (ChatViewHolder) convertView.getTag();
		}
		final MyQuuMessageBean myBean = chatInfos.get(position);

		try {
			if (c instanceof QuuChatActivity)
				((QuuChatActivity) c).refreshNewNum(myBean.getMsgBean()
						.getMessageId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		holder.signLeft.setVisibility(View.GONE);
		holder.textLeft.setVisibility(View.GONE);
		holder.textRight.setVisibility(View.GONE);
		holder.name.setVisibility(View.GONE);
		switch (myBean.getDataType()) {
		case -1:
			holder.linear.setVisibility(View.GONE);
			holder.date.setVisibility(View.GONE);
			holder.notice.setVisibility(View.GONE);
			break;
		case 1: // -1-无数据 1-消息 2-邀请成员加入，3-主动加入，4-退出，5-踢人
			holder.linear.setVisibility(View.VISIBLE);
			holder.notice.setVisibility(View.GONE);
			holder.bar.setVisibility(View.GONE);
			final GroupChatMessageBean chatBean = myBean.getMsgBean();
			final boolean isSending = myBean.isSendState();
			final boolean isSuccess = myBean.isSuccess();
			String createTime = chatBean.getCreateTime();
			String date = "";
			if (null == createTime) {
				date = DateTimeUtil.aTimeFormat(System.currentTimeMillis());
				chatBean.setCreateTime(createTime);
			} else {
				date = DateTimeUtil.aTimeFormat(DateTimeUtil
						.getLongTime(chatBean.getCreateTime()));
			}

			boolean showTime = true;
			if (position > 0) {
				try {
					String lastTime = chatInfos.get(position - 1).getMsgBean()
							.getCreateTime();
					if (lastTime != null) {
						String lastDate = DateTimeUtil.aTimeFormat(DateTimeUtil
								.getLongTime(lastTime));
						if (lastDate != null && lastDate.equals(date)) {
							showTime = false;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (showTime) {
				holder.date.setVisibility(View.VISIBLE);
				holder.date.setText(date);
			} else {
				holder.date.setVisibility(View.GONE);
			}

			ArrayList<ObjectInfoBean> data = (ArrayList<ObjectInfoBean>) chatBean
					.getMessageList();
			if (null == data || data.size() <= 0) {
				break;
			}

			String head_img = ThumbnailImageUrl.getThumbnailHeadUrl(
					chatBean.getSendMemberUrl() + chatBean.getSendMemberPath(),
					HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
			final String name = myBean.getMsgBean().getSendMemberName();
			OnClickListener ownerListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					String id = map_Id.get(v.getTag().toString());
					Bundle b = new Bundle();
					b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					b.putString("id", id);
					b.putString("name", name);
					LogicUtil.enter(c, HomePgActivity.class, b, false);
				}
			};

			OnLongClickListener longClickListener = new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					String eText = QuuChatActivity.instance.edit.getText()
							.toString();
					String str = "@" + name + "：";
					if (!eText.contains(str)) {
						QuuChatActivity.instance.edit.getText().append(str);
						QuuChatActivity.instance.edit
								.setSelection((eText + str).length());
						QuuChatActivity.instance.onInputMenuClick();
					} else {
						QuuChatActivity.instance.edit.setSelection(eText
								.indexOf(str) + str.length());
						QuuChatActivity.instance.onInputMenuClick();
					}
					return true;
				}
			};

			boolean right = false;
			try {
				right = mBerId.trim().equals(chatBean.getSendMemberId() + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!right) {
				holder.out.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				holder.ll.setBackgroundDrawable(res
						.getDrawable(R.drawable.bg_dialog_left));
				holder.right.setVisibility(View.INVISIBLE);
				holder.left.setVisibility(View.VISIBLE);
				holder.leftHead.setTag(position + head_img);
				map_Id.put(position + head_img,
						String.valueOf(chatBean.getSendMemberId()));
				MyFinalBitmap.setHeader(c, holder.leftHead, head_img);

				holder.leftHead.setOnClickListener(ownerListener);
				holder.leftHead.setOnLongClickListener(longClickListener);
				holder.name.setVisibility(View.VISIBLE);
				holder.name.setText(chatBean.getSendMemberName());
			} else {
				holder.out.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				holder.ll.setBackgroundDrawable(res
						.getDrawable(R.drawable.bg_dialog_right));
				holder.right.setVisibility(View.VISIBLE);
				holder.left.setVisibility(View.GONE);
				if (head_img == null || "".equals(head_img)) {
					head_img = ThumbnailImageUrl.getThumbnailHeadUrl(
							headUrlPath, HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
				}
				holder.rightHead.setTag(position + head_img);
				map_Id.put(position + head_img,
						String.valueOf(chatBean.getSendMemberId()));
				MyFinalBitmap.setHeader(c, holder.rightHead, head_img);

				holder.rightHead.setOnClickListener(ownerListener);
				holder.rightHead.setOnLongClickListener(longClickListener);
				holder.name.setVisibility(View.GONE);
			}

			if (isSending) {
				holder.bar.setVisibility(View.VISIBLE);
				holder.signLeft.setVisibility(View.GONE);
			} else {
				if (!isSuccess) {
					holder.signLeft.setVisibility(View.VISIBLE);
				} else {
					if (right) {
						holder.bar.setVisibility(View.INVISIBLE);
					} else {
						holder.bar.setVisibility(View.GONE);
					}
				}
			}

			for (final ObjectInfoBean bean : data) {
				// 对象类型 文本 OBJ_TEXT, 文件 OBJ_FILE, 照片 OBJ_PHOTO, 语音 OBJ_VOICE;
				holder.ll.removeAllViews();
				String oType = bean.getObjectType();

				final LoadQuuMessageInfo infoSet = new LoadQuuMessageInfo() {

					@Override
					public void setMessage(MyQuuMessageBean bean) {

						if (bean.isSuccess()) {
							chatInfos.remove(position);
							chatInfos.add(position, bean);
						}
						notifyDataSetChanged();
						listView.setSelection(chatInfos.size());
					}

				};
				holder.signLeft.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						reSendDialog(myBean, infoSet);
					}
				});

				if ("OBJ_TEXT".equals(oType)) {
					final String content = bean.getMessageContent();
					MixedTextView mtv = (MixedTextView) View.inflate(c,
							R.layout.chat_txt, null);
					mtv.setTag(myBean);
					mtv.setFaceText(StringUtil.trimInnerSpaceStr(content));
					holder.ll.addView(mtv);
					if (!isSending) {
						holder.ll
								.setOnLongClickListener(new OnLongClickListener() {

									@Override
									public boolean onLongClick(View v) {
										if (isSuccess) {
											showDialog(myBean, content, 0,
													null, null);
										} else {
											showFailLongDialog(myBean, infoSet);
										}
										return false;
									}
								});
					}
				} else if ("OBJ_VOICE".equals(oType)) {
					final String fileUrl = bean.getObjectUrl()
							+ bean.getObjectPath();
					float vLong = bean.getObjectLength();
					LinearLayout voice = (LinearLayout) View.inflate(c,
							R.layout.chat_voice, null);

					final ImageView voiceView = (ImageView) voice
							.findViewById(R.id.chat_voice_img);

					voiceView.setScaleType(ScaleType.CENTER_INSIDE);
					int resDrawale = 0;
					int resDefDrawale = 0;
					if (right) {
						voice.findViewById(R.id.chat_voice_left).setPadding(
								(int) (contentWidth * (vLong / maxLong)), 0, 0,
								0);
						((TextView) voice.findViewById(R.id.chat_voice_right))
								.setText((int) vLong + "''");
						resDrawale = R.drawable.audio_play_left;
						resDefDrawale = R.drawable.wave_left_3;
					} else {
						voice.findViewById(R.id.chat_voice_right).setPadding(0,
								0, (int) (contentWidth * (vLong / maxLong)), 0);
						((TextView) voice.findViewById(R.id.chat_voice_left))
								.setText((int) vLong + "''");
						resDrawale = R.drawable.audio_play_right;
						resDefDrawale = R.drawable.wave_right_3;
					}
					voiceView.setImageResource(resDefDrawale);
					voiceView.setTag(new int[] { resDrawale, resDefDrawale });
					holder.ll.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (fileUrl.startsWith(ConstantUtil.SDCARD_PATH)) {
								playVoice(voiceView, fileUrl);
							} else {
								String fileAbsolutePath = ConstantUtil.VOICE_PATH;
								String filePath = fileAbsolutePath
										+ fileUrl.substring(fileUrl
												.lastIndexOf("/") + 1);
								File vFile = new File(filePath);
								if (vFile.exists() && vFile.length() > 0) {
									playVoice(voiceView, filePath);
								} else {
									stopTask();
									downLoadTask = new DownLoadTask(voiceView,
											fileUrl);
									downLoadTask.execute();
								}
							}
						}
					});
					if (!isSending) {
						holder.ll
								.setOnLongClickListener(new OnLongClickListener() {

									@Override
									public boolean onLongClick(View v) {
										if (isSuccess) {
											showDialog(myBean, "分享语音", 2, null,
													fileUrl);
										} else {
											showFailLongDialog(myBean, infoSet);
										}
										return false;
									}
								});
					}
					holder.ll.addView(voice, new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
				} else if ("OBJ_PHOTO".equals(oType)) {
					final String path = bean.getObjectPath();
					final String url = bean.getObjectUrl() + path;
					final ImageView img = new ImageView(c);
					img.setDrawingCacheEnabled(true);
					img.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					img.setScaleType(ScaleType.CENTER_INSIDE);
					img.setAdjustViewBounds(true);
					if (!isSuccess) {
						MyFinalBitmap.setLoaclImage(img, path,
								new SimpleImageLoadingListener() {
									@Override
									public void onLoadingStarted(
											String imageUri, View view) {
									}

									@Override
									public void onLoadingFailed(
											String imageUri, View view,
											FailReason failReason) {
									}

									@Override
									public void onLoadingComplete(
											String imageUri, View view,
											Bitmap loadedImage) {
										// 图片原始宽高
										int iWidth = loadedImage.getWidth();
										int iHeight = loadedImage.getHeight();
										L.d(TAG, "mMaxWH:" + mMaxWH);
										L.d(TAG, "iWidth:" + iWidth
												+ " iHeight:" + iHeight);
										if (iHeight > iWidth) {
											double hwScale = (double) iHeight
													/ (double) iWidth; // 图片高宽缩放比
											// 组件宽
											int vWidth = (int) (mMaxWH / hwScale);
											L.d(TAG, "hwScale:" + hwScale
													+ " vWidth:" + vWidth);
											double mwScale = (double) mMaxWH
													/ (double) iHeight; // 原图与最大图片缩放比
											L.d(TAG, "mwScale:" + mwScale);
											if (hwScale > mMaxScale) {
												if (iWidth > mMaxWH)
													view.setLayoutParams(new LayoutParams(
															mMaxWH, mMaxWH));
												else if (iWidth < mMinWH)
													view.setLayoutParams(new LayoutParams(
															mMinWH, mMaxWH));
												else
													view.setLayoutParams(new LayoutParams(
															iWidth, mMaxWH));
												((ImageView) view)
														.setScaleType(ScaleType.CENTER_CROP);
											} else if (mwScale > mMaxZoomScale) {
												int vHeight = mMaxZoomScale
														* iHeight;
												vWidth = (int) (vHeight / hwScale);
												L.d(TAG, "vWidth:" + vWidth
														+ " vHeight:" + vHeight);
												((ImageView) view)
														.setScaleType(ScaleType.FIT_CENTER);
												view.setLayoutParams(new LayoutParams(
														vWidth, vHeight));
											} else {
												view.setLayoutParams(new LayoutParams(
														vWidth, mMaxWH));
											}
										} else if (iWidth > iHeight) {
											double whScale = (double) iWidth
													/ (double) iHeight;// 图片宽高缩放比
											// 组件高
											int vHeight = (int) (mMaxWH / whScale);
											L.d(TAG, "whScale:" + whScale
													+ " vHeight:" + vHeight);
											double mwScale = (double) mMaxWH
													/ (double) iWidth; // 原图与最大图片缩放比
											if (whScale > mMaxScale) {
												if (iHeight > mMaxWH)
													view.setLayoutParams(new LayoutParams(
															mMaxWH, mMaxWH));
												else if (iHeight < mMinWH)
													view.setLayoutParams(new LayoutParams(
															mMinWH, mMaxWH));
												else
													view.setLayoutParams(new LayoutParams(
															mMaxWH, iHeight));
												((ImageView) view)
														.setScaleType(ScaleType.CENTER_CROP);
											} else if (mwScale > mMaxZoomScale) {
												int vWidth = mMaxZoomScale
														* iWidth;
												vHeight = (int) (vWidth / whScale);
												L.d(TAG, "vWidth:" + vWidth
														+ " vHeight:" + vHeight);
												((ImageView) view)
														.setScaleType(ScaleType.FIT_CENTER);
												view.setLayoutParams(new LayoutParams(
														vWidth, vHeight));
											} else {
												view.setLayoutParams(new LayoutParams(
														mMaxWH, vHeight));
											}
										} else {
											double whScale = (double) iWidth
													/ (double) iHeight;
											// 组件高
											int vHeight = (int) (mMaxWH / whScale);
											L.d(TAG, "whScale:" + whScale
													+ " vHeight:" + vHeight);
											double mwScale = (double) mMaxWH
													/ (double) iWidth;
											L.d(TAG, "mwScale:" + mwScale);
											if (mwScale > mMaxZoomScale) {
												L.d(TAG, "vWidth:"
														+ mMaxZoomScale
														* iWidth + " vHeight:"
														+ mMaxZoomScale
														* iHeight);
												((ImageView) view)
														.setScaleType(ScaleType.FIT_CENTER);
												view.setLayoutParams(new LayoutParams(
														mMaxZoomScale * iWidth,
														mMaxZoomScale * iHeight));
											} else {
												view.setLayoutParams(new LayoutParams(
														mMaxWH, mMaxWH));
											}
										}

									}
								});
					} else {
						String img_url = ThumbnailImageUrl
								.getThumbnailImageUrl(url,
										ImageSizeEnum.THREE_HUNDRED);
						img.setTag("img:" + position + img_url);
						MyFinalBitmap.setImage(null, img, img_url, 0,
								new SimpleImageLoadingListener() {
									@Override
									public void onLoadingStarted(
											String imageUri, View view) {
										L.d(TAG,
												"loading width:"
														+ view.getWidth());
									}

									@Override
									public void onLoadingFailed(
											String imageUri, View view,
											FailReason failReason) {
									}

									@Override
									public void onLoadingComplete(
											String imageUri, View view,
											Bitmap loadedImage) {
										// 图片原始宽高
										int iWidth = loadedImage.getWidth();
										int iHeight = loadedImage.getHeight();
										L.d(TAG, "mMaxWH:" + mMaxWH);
										L.d(TAG, "iWidth:" + iWidth
												+ " iHeight:" + iHeight);
										if (iHeight > iWidth) {
											double hwScale = (double) iHeight
													/ (double) iWidth; // 图片高宽缩放比
											// 组件宽
											int vWidth = (int) (mMaxWH / hwScale);
											L.d(TAG, "hwScale:" + hwScale
													+ " vWidth:" + vWidth);
											double mwScale = (double) mMaxWH
													/ (double) iHeight; // 原图与最大图片缩放比
											L.d(TAG, "mwScale:" + mwScale);
											if (hwScale > mMaxScale) {
												if (iWidth > mMaxWH)
													view.setLayoutParams(new LayoutParams(
															mMaxWH, mMaxWH));
												else if (iWidth < mMinWH)
													view.setLayoutParams(new LayoutParams(
															mMinWH, mMaxWH));
												else
													view.setLayoutParams(new LayoutParams(
															iWidth, mMaxWH));
												((ImageView) view)
														.setScaleType(ScaleType.CENTER_CROP);
											} else if (mwScale > mMaxZoomScale) {
												int vHeight = mMaxZoomScale
														* iHeight;
												vWidth = (int) (vHeight / hwScale);
												L.d(TAG, "vWidth:" + vWidth
														+ " vHeight:" + vHeight);
												((ImageView) view)
														.setScaleType(ScaleType.FIT_CENTER);
												view.setLayoutParams(new LayoutParams(
														vWidth, vHeight));
											} else {
												view.setLayoutParams(new LayoutParams(
														vWidth, mMaxWH));
											}
										} else if (iWidth > iHeight) {
											double whScale = (double) iWidth
													/ (double) iHeight;// 图片宽高缩放比
											// 组件高
											int vHeight = (int) (mMaxWH / whScale);
											L.d(TAG, "whScale:" + whScale
													+ " vHeight:" + vHeight);
											double mwScale = (double) mMaxWH
													/ (double) iWidth; // 原图与最大图片缩放比
											if (whScale > mMaxScale) {
												if (iHeight > mMaxWH)
													view.setLayoutParams(new LayoutParams(
															mMaxWH, mMaxWH));
												else if (iHeight < mMinWH)
													view.setLayoutParams(new LayoutParams(
															mMinWH, mMaxWH));
												else
													view.setLayoutParams(new LayoutParams(
															mMaxWH, iHeight));
												((ImageView) view)
														.setScaleType(ScaleType.CENTER_CROP);
											} else if (mwScale > mMaxZoomScale) {
												int vWidth = mMaxZoomScale
														* iWidth;
												vHeight = (int) (vWidth / whScale);
												L.d(TAG, "vWidth:" + vWidth
														+ " vHeight:" + vHeight);
												((ImageView) view)
														.setScaleType(ScaleType.FIT_CENTER);
												view.setLayoutParams(new LayoutParams(
														vWidth, vHeight));
											} else {
												view.setLayoutParams(new LayoutParams(
														mMaxWH, vHeight));
											}
										} else {
											double whScale = (double) iWidth
													/ (double) iHeight;
											// 组件高
											int vHeight = (int) (mMaxWH / whScale);
											L.d(TAG, "whScale:" + whScale
													+ " vHeight:" + vHeight);
											double mwScale = (double) mMaxWH
													/ (double) iWidth;
											L.d(TAG, "mwScale:" + mwScale);
											if (mwScale > mMaxZoomScale) {
												L.d(TAG, "vWidth:"
														+ mMaxZoomScale
														* iWidth + " vHeight:"
														+ mMaxZoomScale
														* iHeight);
												((ImageView) view)
														.setScaleType(ScaleType.FIT_CENTER);
												view.setLayoutParams(new LayoutParams(
														mMaxZoomScale * iWidth,
														mMaxZoomScale * iHeight));
											} else {
												view.setLayoutParams(new LayoutParams(
														mMaxWH, mMaxWH));
											}
										}
									}
								});

						if (url != null && !url.equals("")) {
							holder.ll.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									showPhoto(url, "type_only");
								}
							});
						}
					}
					if (!isSending) {
						holder.ll
								.setOnLongClickListener(new OnLongClickListener() {

									@Override
									public boolean onLongClick(View v) {
										if (isSuccess) {
											showDialog(myBean, "分享照片", 1, img,
													url);
										} else {
											showFailLongDialog(myBean, infoSet);
										}
										return false;
									}
								});
					}
					holder.ll.addView(img);
				} else if ("OBJ_FILE".equals(oType)) {
				}

				if (myBean.isSendable()) {
					myBean.setSendable(false);
					myBean.sendMsg(infoSet);
				}

			}
			return convertView;
		case 2:
		case 3:
		case 4:
		case 5:
			holder.linear.setVisibility(View.GONE);
			holder.date.setVisibility(View.GONE);
			holder.notice.setVisibility(View.VISIBLE);
			GroupChatMessageBean noticeBean = myBean.getMsgBean();
			if (null != noticeBean) {
				List<ObjectInfoBean> notices = noticeBean.getMessageList();
				if (null != notices && notices.size() > 0) {
					holder.notice.setText(noticeBean.getShowText());
					return convertView;
				}
			}
			break;
		default:
			break;
		}
		return convertView;
	}

	/**
	 * 长按处理发送失败消息
	 */
	private void showFailLongDialog(final MyQuuMessageBean myBean,
			final LoadQuuMessageInfo infoSet) {
		Builder builder = new Builder(c).setTitle("提示")
				.setItems(new String[] { "重新发送", "删除" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									myBean.setSendState(true);
									QuuChatAdapter.this.notifyDataSetChanged();
									myBean.sendMsg(infoSet);
								} else {
									chatInfos.remove(myBean);
									notifyDataSetChanged();
									QChatSendService.getService(c).deleteChat(
											myBean.getTime());
								}
							}
						});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	private void showPhoto(String url, String type) {
		Bundle bundle = new Bundle();
		int index = 1;
		bundle.putInt("index", (index - 1));
		bundle.putString("type", type);
		bundle.putString("url", url);
		LogicUtil.enter(c, PhotoGalleryActivity.class, bundle, false);
	}

	class ChatViewHolder {
		ChatLinearLayout ll;
		LinearLayout linear, out;
		TextView date, notice, textLeft, textRight, name;
		View left, right;
		ImageView leftHead, rightHead, signLeft;
		ProgressBar bar;
	}

	public boolean checkSendState() {
		for (int i = 0; i < chatInfos.size(); i++) {
			if (chatInfos.get(i).isSendState()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 处理消息
	 * 
	 * @param myBean
	 * @param msgType
	 *            0：文本 1、照片 2、音频 3、视频
	 */
	private void showDialog(final MyQuuMessageBean myBean, final String text,
			final int msgType, final ImageView shareImg,
			final String shareImageUrl) {
		new ChatOperateAlertManager(c).showAlertDialog(msgType,
				new ChatOperateAlertManager.OnItemClickListener() {

					@Override
					public void onItemClick(DialogInterface dialog, int which) {
						onAlertItemClick(which, myBean, msgType, text,
								shareImg, shareImageUrl, dialog);
					}

				});
	}

	public void onAlertItemClick(int which, MyQuuMessageBean myBean,
			int msgType, String text, ImageView shareImg, String shareImageUrl,
			DialogInterface dialog) {
		GroupChatMessageBean bean = myBean.getMsgBean();
		switch (which) {
		case 0:// 复制
			ClipboardManager clipboardManager = (ClipboardManager) c
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboardManager.setText(text);
			T.show(c, "已复制到剪切板");
			L.d(TAG, "text" + text);
			break;
		case 1:// 转发到朋友
			L.d(TAG, "showDialog msgType=" + msgType);
			if (msgType == 0) {
				String createTime = bean.getCreateTime();
				String date = "";
				if (null == createTime) {
					date = DateTimeUtil.cTimeFormat(System.currentTimeMillis());
				} else {
					date = DateTimeUtil.cTimeFormat(DateTimeUtil
							.getLongTime(bean.getCreateTime()));
				}
				String name = "";
				if (GetDbInfoUtil.getMemberId(c) == bean.getSendMemberId()) {
					name = "我";
				} else {
					name = bean.getSendMemberName();
				}
				String info = "【转】" + name + "：" + text + "【" + date + "】";
				Bundle b = new Bundle();
				b.putInt("type", 4);
				b.putString("sendInfo", info);
				LogicUtil.enter(c, FriendsChooserActivity.class, b,
						FriendsChooserActivity.RESCODE);
			} else {
				ObjectInfoBean mrInfo = bean.getMessageList().get(0);
				Bundle b = new Bundle();
				b.putInt("type", 4);
				b.putString("objectType", mrInfo.getObjectType());
				b.putString("uri", mrInfo.getObjectUrl());
				b.putString("path", mrInfo.getObjectPath());
				b.putLong("l", mrInfo.getObjectLength());
				b.putInt(BundleKey.TYPE_SENDINFO, msgType);
				LogicUtil.enter(c, FriendsChooserActivity.class, b,
						FriendsChooserActivity.RESCODE);
			}
			break;
		case 2:// 转发到圈聊
			L.d(TAG, "showDialog msgType=" + msgType);
			Bundle bundle = new Bundle();
			if (msgType == 0) {
				String createTime = bean.getCreateTime();
				String date = "";
				if (null == createTime) {
					date = DateTimeUtil.cTimeFormat(System.currentTimeMillis());
				} else {
					date = DateTimeUtil.cTimeFormat(DateTimeUtil
							.getLongTime(bean.getCreateTime()));
				}
				String name = "";
				if (GetDbInfoUtil.getMemberId(c) == bean.getSendMemberId()) {
					name = "我";
				} else {
					name = bean.getSendMemberName();
				}
				String info = "【转】" + name + "：" + text + "【" + date + "】";
				bundle.putString(BundleKey.SENDINFO, info);
			} else {
				ObjectInfoBean mrInfo = bean.getMessageList().get(0);
				bundle.putString(BundleKey.TYPE_OBJECT, mrInfo.getObjectType());
				bundle.putString(BundleKey.URL, mrInfo.getObjectUrl());
				bundle.putString(BundleKey.PATH, mrInfo.getObjectPath());
				bundle.putLong(BundleKey.LENGTH, mrInfo.getObjectLength());
				bundle.putInt(BundleKey.TYPE_SENDINFO, msgType);
			}
			bundle.putSerializable(BundleKey.TYPE_REQUEST, Type.CHATGROUP);
			LogicUtil.enter(c, ChatGroupChooseActivity.class, bundle,
					ChatGroupChooseActivity.CHOOSECHATGROUP);
			break;
		case 3:// 转发到微信
			IWXAPI api = WXAPIFactory
					.createWXAPI(c, ConstantUtil.APP_ID, false);
			api.registerApp(ConstantUtil.APP_ID);
			if (api.isWXAppInstalled()) {
				Intent wIntent = new Intent(c, WXEntryActivity.class);
				wIntent.putExtra("shareTopic", text);
				if (msgType == 1 || msgType == 2) {
					wIntent.putExtra("shareImageUrl", shareImageUrl);
					wIntent.putExtra("type", msgType);
					if (msgType == 1)
						wIntent.putExtra(
								"data",
								ImageDealUtil.getBitmapBytes(
										shareImg.getDrawingCache(), false));
				}
				c.startActivity(wIntent);
			} else {
				T.show(c, "您还未安装微信客户端！");
			}
			break;
		case 4:// 删除
			showdeleteDialog(myBean);
			break;
		// case 5:// 取消
		// dialog.dismiss();
		// break;
		case 6:// 转发到交流圈
			QuuChatActivity.bean = bean;
			Bundle b = new Bundle();
			b.putString("btnString", "分享");
			LogicUtil.enter(c, GroupsChooserActivity.class, b,
					GroupsChooserActivity.RESCODE);
		default:
			break;
		}
	}

	private void showdeleteDialog(final MyQuuMessageBean myBean) {
		Builder builder = new Builder(c);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (myBean.isSuccess()) {
					new DeleteMsgTask(myBean).execute();
				} else {
					chatInfos.remove(myBean);
					notifyDataSetChanged();
					QChatSendService.getService(c).deleteChat(myBean.getTime());
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

	class DeleteMsgTask extends AsyncTask<String, Integer, MCResult> {
		private MyQuuMessageBean myBean;

		public DeleteMsgTask(MyQuuMessageBean myBean) {
			this.myBean = myBean;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIGroupChatRequestServers
						.deleteGroupChatMessageByMember(c, groupId, myBean
								.getMsgBean().getMessageId() + "");
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null != mcResult) {
				if (mcResult.getResultCode() == 1) {
					ChatGroupMessageBeanService.getService(c).deleteChat(
							myBean.getMsgBean().getMessageId(), groupId);
					T.show(c, "已删除！");
					L.d(TAG, "DeleteMsgTask chatInfos=" + chatInfos.size());
					chatInfos.remove(myBean);
					notifyDataSetChanged();
					L.d(TAG, "DeleteMsgTask chatInfos=" + chatInfos.size());
				} else {
					T.show(c, T.ErrStr);
				}
			} else {
				T.show(c, T.ErrStr);
			}
		}
	}

	private void playVoice(ImageView v, String fileUrl) {
		L.i(TAG, "playVoice fileUrl=" + fileUrl);
		AnimationDrawable ad = null;
		if (((ImageView) v).getDrawable() instanceof AnimationDrawable) {
			ad = (AnimationDrawable) ((ImageView) v).getDrawable();
		}

		if (ad == null || !ad.isRunning()) {
			if (null != curV && v != curV) {
				resetWaveView();
			}
			MediaManager
					.setOnCustomCompletionListener(new OnCustomCompletionListener() {

						@Override
						public void onCompletion() {
							if (null != curV) {
								resetWaveView();
							}
						}
					});
			MediaManager.playAudio(fileUrl, c);
			v.setImageResource(((int[]) v.getTag())[0]);
			if (ad == null)
				ad = (AnimationDrawable) ((ImageView) v).getDrawable();
			ad.start();
			curV = (ImageView) v;
		} else {
			MediaManager.stopAudio();
			resetWaveView();
		}
	}

	public void resetWaveView() {
		if (null != curV) {
			curV.setImageDrawable(null);
			curV.setImageResource(((int[]) curV.getTag())[1]);
		}
	}

	private void stopTask() {
		if (null != downLoadTask
				&& downLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
			downLoadTask.cancel(true);
		}
	}

	class DownLoadTask extends AsyncTask<String, Integer, String> {

		private ImageView v;
		private String fileUrl;

		public DownLoadTask(ImageView v, String fileUrl) {
			this.v = v;
			this.fileUrl = fileUrl;
		}

		@Override
		protected String doInBackground(String... params) {
			String path = null;
			try {
				path = downloadVoice(fileUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return path;
		}

		@Override
		protected void onPostExecute(String path) {
			super.onPostExecute(path);
			if (null != path) {
				playVoice(v, path);
			} else {
				T.show(c, T.ErrStr);
			}
		}
	}

	@SuppressWarnings("resource")
	private String downloadVoice(String fileUrl) throws Exception {
		InputStream fisFileInputStream = null;
		HttpURLConnection hrc = null;
		BufferedInputStream is = null;
		FileOutputStream fos = null;
		File proFile = null;
		URL url = new URL(fileUrl);
		hrc = HttpRequestServers.getHttpURLConnection(url);

		hrc.setRequestProperty("User-Agent", "NetBear");
		hrc.setRequestProperty("Content-type",
				"application/x-java-serialized-object");
		hrc.setRequestProperty("connection", "Keep-Alive");
		hrc.setConnectTimeout(20 * 1000);
		hrc.setReadTimeout(30 * 1000);
		hrc.connect();

		fisFileInputStream = hrc.getInputStream();

		is = new BufferedInputStream(fisFileInputStream);

		File myFile = null;

		String fileAbsolutePath = ConstantUtil.VOICE_PATH;
		FileUtil.createFile(fileAbsolutePath);
		String downName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		proFile = new File(fileAbsolutePath + downName + ".sp");
		myFile = new File(fileAbsolutePath + downName);

		fos = new FileOutputStream(proFile);

		long fileLength = hrc.getContentLength();
		if (fileLength == -1) {
			fileLength = fisFileInputStream.available();
		}
		L.d(TAG, "downloadVoice fileLength=" + fileLength);
		int progress = 0;
		int percent = 0;
		int length = 0;
		byte buf[] = new byte[1024];
		while ((length = is.read(buf)) != -1) {
			percent += length;
			int i = (int) (percent * 100 / fileLength);
			if (i > progress) {
				progress = i;
				L.i(TAG, "downloadVoice progress=" + progress);
			}
			fos.write(buf, 0, length);
		}
		fos.flush();

		if (proFile != null && proFile.exists())
			proFile.renameTo(myFile);

		return fileAbsolutePath + downName;
	}

	private void reSendDialog(final MyQuuMessageBean myBean,
			final LoadQuuMessageInfo infoSet) {
		new Builder(c).setTitle("提示").setMessage("确认重发该信息？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						myBean.setSendState(true);
						QuuChatAdapter.this.notifyDataSetChanged();
						myBean.sendMsg(infoSet);
					}
				}).setNegativeButton("取消", null).show();
	}
}

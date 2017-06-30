package com.datacomo.mc.spider.android.adapter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CircleBlogDetailsActivity;
import com.datacomo.mc.spider.android.CreateGroupTopicActivity;
import com.datacomo.mc.spider.android.InfoWallActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.TopicActivity;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.CommentSendBean;
import com.datacomo.mc.spider.android.db.CommentSendService;
import com.datacomo.mc.spider.android.db.QuuBoInfoService;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.APIThemeRequestServers;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupThemeBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.QuuboInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.params.UploadFileParams;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.APIMethodName;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.HandlerUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.StreamUtil;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImgUtil;
import com.datacomo.mc.spider.android.util.TypeUtil;
import com.datacomo.mc.spider.android.view.DialogView;
import com.datacomo.mc.spider.android.view.DottedLine;
import com.datacomo.mc.spider.android.view.MixedTextView;

public class CommentFailAdapter extends BaseAdapter {
	private final String TAG = "CommentFailAdapter";

	private Context context;
	private ArrayList<CommentSendBean> csbs;
	private ViewHolder holder;

	private static ArrayList<Long> times = new ArrayList<Long>();

	private int imgWidth, imgHeight;

	public CommentFailAdapter(Context context, ArrayList<CommentSendBean> csbs) {
		this.context = context;
		this.csbs = csbs;
		int width = BaseData.getScreenWidth();
		imgWidth = width / 4;
		imgHeight = width / 5;
	}

	@Override
	public int getCount() {
		return csbs.size();
	}

	@Override
	public Object getItem(int position) {
		return csbs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.comment_fail_item, null);
			holder.content = (MixedTextView) convertView
					.findViewById(R.id.comment_content);
			holder.quubo = (MixedTextView) convertView
					.findViewById(R.id.comment_quubo);
			holder.more = (ImageView) convertView
					.findViewById(R.id.comment_more);

			holder.imgNum = (TextView) convertView.findViewById(R.id.imgnum);
			holder.img = (ImageView) convertView.findViewById(R.id.image);
			holder.images = (RelativeLayout) convertView
					.findViewById(R.id.images);
			holder.blank1 = convertView.findViewById(R.id.blank1);
			holder.blank2 = convertView.findViewById(R.id.blank2);

			holder.fileNum = (TextView) convertView.findViewById(R.id.filenum);
			holder.files = (LinearLayout) convertView.findViewById(R.id.files);
			((DottedLine) convertView.findViewById(R.id.dottedline))
					.setLineColor(context.getResources().getColor(
							R.color.gray_light));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.files.setVisibility(View.GONE);
		while (holder.files.getChildCount() > 1) { // 清理文件
			holder.files.removeViewAt(1);
		}

		final CommentSendBean csb = csbs.get(position);
		final String mType = csb.getmType();

		ArrayList<String> items = new ArrayList<String>();
		if ("OBJ_SEND_QUUBO".equals(mType)) {
			items.add("重新发送");
			items.add("编辑圈博");
			items.add("删除圈博");
			String gname = "";
			try {
				JSONArray array = new JSONArray(csb.getGidname());
				for (int i = 0; i < array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
					gname += json.getString("name") + "、";
				}
				gname = gname.substring(0, gname.length() - 1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			holder.content.setOriTxtss(csb.getName(), gname);
			String title = csb.getTitle();
			if (title != null && !title.equals("")) {
				title = "【" + title + "】";
			} else {
				title = csb.getContent();
				if (title == null || title.equals("")) {
					try {
						JSONArray array = new JSONArray(csb.getFilePaths());
						for (int i = 0; i < array.length(); i++) {
							JSONObject json = array.getJSONObject(i);
							if (json.getInt("type") == 0) {
								title = "【分享图片】";
								break;
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (title == null || title.equals(""))
						title = "【分享文件】";
				}
			}
			holder.quubo.setResTextsss(title);

			try {
				JSONArray array = new JSONArray(csb.getFilePaths());
				String imgUrl = null;
				int imgNum = 0;
				List<String> filePaths = new ArrayList<String>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
					int type = json.getInt("type");
					if (type == 0) {
						imgNum++;
						if (null == imgUrl) {
							imgUrl = json.getString("path");
							// imgUrl = imgUrl.substring(0,
							// imgUrl.lastIndexOf("_"));
						}
					} else if (type == 1) {
						String filePath = json.getString("path");
						filePaths.add(filePath.substring(filePath
								.lastIndexOf("/") + 1));
					} else if (type == 2) {
						JSONObject fj = new JSONObject(json.getString("path"));
						filePaths.add(fj.getString("name"));
					}
				}

				try {
					if (imgNum > 0) {
						L.i(TAG, "imgUrl=" + imgUrl);
						holder.images.setVisibility(View.VISIBLE);
						holder.imgNum.setText(imgNum + "张");
						LayoutParams lp = (LayoutParams) holder.img
								.getLayoutParams();
						lp.width = imgWidth;
						lp.height = imgHeight;
						holder.img.setLayoutParams(lp);
						MyFinalBitmap.setLocalAndDisPlayImage(null, holder.img,
								imgUrl);
						if (imgNum > 1) {
							showBlankImg(holder);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				int fileNum = filePaths.size();
				if (fileNum > 0) {
					holder.fileNum.setText(fileNum + "个文件");
					for (String file_name : filePaths) {
						LinearLayout file_layout = (LinearLayout) LayoutInflater
								.from(context).inflate(
										R.layout.layout_info_file, null);
						((TextView) file_layout.findViewById(R.id.file_name))
								.setText(file_name);
						((ImageView) file_layout.findViewById(R.id.file_icon))
								.setImageResource(FileUtil
										.getFileIcon(file_name));
						holder.files.addView(file_layout);
						holder.files.setVisibility(View.VISIBLE);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			items.add("重新发送");
			items.add("查看详情");
			items.add("删除评论");
			// items.add("取消");
			holder.content.setResTextsss(csb.getContent());
			if ("OBJ_MEMBER_RES_MOOD".equals(mType)) {
				holder.quubo.setResTextsss("评论 " + csb.getRname() + " 的心情",
						csb.getTitle());
			} else if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(mType)
					|| "OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(mType)) {
				holder.quubo.isLeave(true);
				holder.quubo.setOriTextsss("评论 " + csb.getRname(),
						csb.getGname() + " 的留言", csb.getTitle());
			} else {
				holder.quubo.setOriTextsss("评论 " + csb.getRname(),
						csb.getGname() + " 的圈博", csb.getTitle());
			}
		}

		final String[] itms = new String[items.size()];
		for (int i = 0; i < itms.length; i++) {
			itms[i] = items.get(i);
		}
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context).setItems(itms,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									if ("OBJ_SEND_QUUBO".equals(mType)) {
										try {
											sendGroupTopic(csb);
										} catch (JSONException e) {
											e.printStackTrace();
										}
									} else {
										if (times.contains(csb.getTime())) {
											showTip("正在评论中...");
										} else {
											times.add(csb.getTime());
											new LoadInfoTask(csb).execute();
										}
									}
								} else if (which == 1) {
									if ("OBJ_SEND_QUUBO".equals(mType)) {
										// 进入发圈博
										Bundle b = new Bundle();
										b.putInt("position", position);
										b.putSerializable("CommentSendBean",
												csb);
										LogicUtil.enter(context,
												CreateGroupTopicActivity.class,
												b, 0);

										CommentSendService.getService(context)
												.deleteComment(csb.getTime());
										csbs.remove(position);
										notifyDataSetChanged();
									} else {
										enterCommend(csb);
									}
								} else if (which == 2) {
									CommentSendService.getService(context)
											.deleteComment(csb.getTime());
									csbs.remove(position);
									notifyDataSetChanged();
								}
								dialog.dismiss();
							}
						}).show();
			}
		};
		holder.content.setOnClickListener(listener);
		holder.quubo.setOnClickListener(listener);
		holder.more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("OBJ_SEND_QUUBO".equals(mType)) {
					try {
						sendGroupTopic(csb);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					if (times.contains(csb.getTime())) {
						showTip("正在评论中...");
					} else {
						times.add(csb.getTime());
						new LoadInfoTask(csb).execute();
					}
				}
			}
		});
		return convertView;
	}

	private void enterCommend(CommentSendBean csb) {
		String mType = csb.getmType();
		if ("OBJ_MEMBER_RES_MOOD".equals(mType)
				|| "OBJ_MEMBER_RES_LEAVEMESSAGE".equals(mType)
				|| "OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(mType)) {
			if (null != resourceInfoTask
					&& resourceInfoTask.getStatus() == AsyncTask.Status.RUNNING) {
				resourceInfoTask.cancel(true);
			}
			spdDialog.showProgressDialog("正在加载中...");
			resourceInfoTask = new ResourceInfoTask(csb.getGid() + "",
					csb.getRid() + "", mType);
			resourceInfoTask.execute();
		} else {
			QuuboInfoBean qib = null;
			try {
				qib = QuuBoInfoService.getService(context).queryQuubo(
						csb.getGid(), csb.getRid());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (qib != null) {
					Bundle b = new Bundle();
					b.putSerializable("info", qib);
					b.putString("type_From", TypeUtil.type_ResultMessage);
					b.putString("type_Object", mType);
					LogicUtil.enter(context, CircleBlogDetailsActivity.class,
							b, false);
				} else {
					if (null != loadInfoTask
							&& loadInfoTask.getStatus() == AsyncTask.Status.RUNNING) {
						loadInfoTask.cancel(true);
					}
					spdDialog.showProgressDialog("正在加载中...");
					loadInfoTask = new LoadQuuBoInfoTask(csb.getGid() + "",
							csb.getRid() + "", mType);
					loadInfoTask.execute();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private ResourceInfoTask resourceInfoTask;

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
					result = APIThemeRequestServers
							.themeInfo(context, objectId);
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
							LogicUtil.enter(context, TopicActivity.class, b,
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
							LogicUtil.enter(context,
									CircleBlogDetailsActivity.class, b, false);
						}
					}
				}
			}
		}
	}

	private LoadQuuBoInfoTask loadInfoTask;

	class LoadQuuBoInfoTask extends AsyncTask<String, Integer, MCResult> {
		private String groupId, objectId, objectType;

		public LoadQuuBoInfoTask(String groupId, String objectId,
				String objectType) {
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
						LogicUtil.enter(context,
								CircleBlogDetailsActivity.class, b, false);
					}
				}
			}
		}
	}

	private void showBlankImg(ViewHolder holder) {
		holder.blank1.setVisibility(View.VISIBLE);
		holder.blank2.setVisibility(View.VISIBLE);

		LayoutParams lp1 = (LayoutParams) holder.blank1
				.getLayoutParams();
		lp1.width = imgWidth;
		lp1.height = imgHeight;
		holder.blank1.setLayoutParams(lp1);

		LayoutParams lp2 = (LayoutParams) holder.blank2
				.getLayoutParams();
		lp2.width = imgWidth;
		lp2.height = imgHeight;
		holder.blank2.setLayoutParams(lp2);
	}

	AlertDialog pDialog;
	DialogView dialogContent;
	boolean allowedPublish;

	/**
	 * 开启发圈博线程
	 * 
	 * @param csb
	 * @throws JSONException
	 */
	private void sendGroupTopic(final CommentSendBean csb) throws JSONException {
		if (times.contains(csb.getTime())) {
			T.show(context, "正在后台发送中...");
			return;
		} else {
			times.add(csb.getTime());
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		TextView dTitle = new TextView(context);
		dTitle.setGravity(Gravity.CENTER);
		dTitle.setPadding(0, 15, 0, 0);
		dTitle.setTextSize(22);
		dTitle.setText("发送中...");
		builder.setCustomTitle(dTitle);
		dialogContent = new DialogView(context);
		String allInfo = null;

		final ArrayList<String> fPaths = new ArrayList<String>();
		final ArrayList<String> pPaths = new ArrayList<String>();
		String filePaths = csb.getFilePaths();
		JSONArray array = new JSONArray(filePaths);
		final ArrayList<String> cFile = new ArrayList<String>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject json = array.getJSONObject(i);
			if (json.getInt("type") == 0) {
				pPaths.add(json.getString("path"));
			} else if (json.getInt("type") == 1) {
				fPaths.add(json.getString("path"));
			} else if (json.getInt("type") == 2) {
				JSONObject fj = new JSONObject(json.getString("path"));
				cFile.add(fj.getInt("id") + "");
			}
		}
		int numFile = fPaths.size() + cFile.size();
		int numPhoto = pPaths.size();

		if (numFile > 0 && numPhoto > 0) {
			allInfo = "共 " + numPhoto + " 张图片，" + numFile + " 个文件";
		} else if (numFile > 0) {
			allInfo = "共 " + numFile + " 个文件";
		} else if (numPhoto > 0) {
			allInfo = "共 " + numPhoto + " 张图片";
		} else {
			allInfo = null;
		}
		dialogContent.setAllFileInfo(allInfo);
		builder.setView(dialogContent);
		builder.setCancelable(false);
		builder.setNegativeButton("取消上传",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						pDialog.cancel();
						allowedPublish = false;
						times.remove(csb.getTime());
					}
				});
		pDialog = builder.show();
		new Thread() {
			@SuppressWarnings({ "unchecked" })
			public void run() {
				allowedPublish = true;
				try {
					String title = csb.getTitle();
					String content = StringUtil.trimInnerSpaceStr(csb
							.getContent());
					if (content == null || "".equals(content)) {
						content = title;
					}

					LinkedHashMap<String, String> fileTempsList = new LinkedHashMap<String, String>();
					LinkedHashMap<String, String> photoTempsList = new LinkedHashMap<String, String>();
					for (String path : pPaths) {
						if (!allowedPublish) {
							break;
						}
						if (null != path && !"".equals(path)
						// 发送失败后，如果重传 去除已上传成功的
								&& !photoTempsList.containsKey(path)) {
							String toast = null;
							if (null != pDialog && pDialog.isShowing()) {
								int fsize = fileTempsList.size() + cFile.size();
								int psize = photoTempsList.size();
								if (fsize == 0) {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片");
								} else {
									toast = StringUtil
											.merge("已上传 ", (1 + psize),
													" 张图片，", fsize, " 个文件");
								}

								HandlerUtil.sendMsgToHandler(sendHandler, 3,
										StringUtil.merge("正在上传第 ", (1 + psize),
												" 张图片..."), 0);
								HandlerUtil.sendMsgToHandler(sendHandler,
										HandlerUtil.getMessage(2, path, 0, -1));
							}
							startThread(path, true, toast, fileTempsList,
									photoTempsList, csb);
						}
					}

					for (String path : fPaths) {
						if (!allowedPublish) {
							break;
						}
						L.d(TAG, "imgPath" + path);
						if (null != path && !"".equals(path)
						// 发送失败后，如果重传 去除已上传成功的
								&& !fileTempsList.containsKey(path)) {
							String toast = null;
							if (null != pDialog && pDialog.isShowing()) {
								int fsize = fileTempsList.size() + cFile.size();
								int psize = photoTempsList.size();
								if (psize == 0) {
									toast = StringUtil.merge("已上传 ", fsize,
											" 个文件");
								} else {
									toast = StringUtil
											.merge("已上传 ", (1 + psize),
													" 张图片，", fsize, " 个文件");
								}
								HandlerUtil.sendMsgToHandler(sendHandler, 3,
										StringUtil.merge("正在上传第 ", (1 + fsize),
												" 个文件..."), 0);
								HandlerUtil.sendMsgToHandler(sendHandler,
										HandlerUtil.getMessage(2,
												new File(path), 0, 1));
							}
							startThread(path, false, toast, fileTempsList,
									photoTempsList, csb);
						}
					}

					if (!allowedPublish) {
						return;
					}
					HandlerUtil.sendMsgToHandler(sendHandler, 5, "正在发圈博...",
							View.GONE);

					int i = photoTempsList.size();
					L.d(TAG, "photoTempsList.size" + photoTempsList.size());
					String[] photoTemps = new String[i];
					for (Entry<String, String> entry : photoTempsList
							.entrySet()) {
						photoTemps[i - 1] = entry.getValue();
						i--;
					}
					i = fileTempsList.size();
					String[] fileTemps = new String[i];
					for (Entry<String, String> entry : fileTempsList.entrySet()) {
						fileTemps[i - 1] = entry.getValue();
						i--;
					}
					String[] fileId = null;
					if (cFile.size() > 0) {
						fileId = new String[cFile.size()];
						for (int j = 0; j < fileId.length; j++) {
							fileId[j] = cFile.get(j);
						}
					}

					@SuppressWarnings("rawtypes")
					LinkedHashMap atMap = null;
					try {
						String ats = csb.getAtidname();
						if (ats != null && !"".equals(ats)) {
							atMap = new LinkedHashMap<String, String>();
							ats = ats.substring(0, ats.length() - 1);
							L.d(TAG, "onCreate ats=" + ats);
							if (ats.contains("&")) {
								String[] as = ats.split("&");
								for (String a : as) {
									atMap.put(a.substring(0, a.indexOf("#")),
											a.substring(a.indexOf("#") + 1));
								}
							} else {
								atMap.put(ats.substring(0, ats.indexOf("#")),
										ats.substring(ats.indexOf("#") + 1));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					String tags = "";
					if (null != atMap && atMap.size() > 0) {
						for (Iterator<?> iterator = atMap.keySet().iterator(); iterator
								.hasNext();) {
							String id = (String) iterator.next();
							if (!content.contains((CharSequence) atMap.get(id))) {
								atMap.remove(id);
							}
						}

						int size = atMap.size();
						if (size > 0) {
							for (Iterator<?> iterator = atMap.keySet()
									.iterator(); iterator.hasNext();) {
								String id = (String) iterator.next();
								tags += id + ",";
							}
							tags = tags.substring(0, tags.length() - 1);
						}
					}

					if (content == null || content.equals("")) {
						if (photoTemps.length > 0) {
							content = "【分享照片】";
						} else if (fileTemps.length > 0
								|| (fileId != null && fileId.length > 0)) {
							content = "【分享文件】";
						}
					}

					content = content.replaceAll("\n", "<br />");

					String[] groupIds = null;
					try {
						JSONArray array = new JSONArray(csb.getGidname());
						L.d(TAG, "loadDraft ges=" + array.toString());
						groupIds = new String[array.length()];
						for (int j = 0; j < array.length(); j++) {
							JSONObject json = array.getJSONObject(j);
							groupIds[i] = json.getString("id") + "";
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					MCResult response = APIRequestServers.createGroupTopic(
							App.app, groupIds, fileTemps, photoTemps, fileId,
							title, content, tags, null, null);
					int resultCode = response.getResultCode();
					L.i(TAG, "sendGroupTopic resultCode=" + resultCode);
					if (!allowedPublish) {
						return;
					}
					if (resultCode == 1) {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = csb;
						sendHandler.sendMessage(msg);
					} else {
						sendHandler.sendEmptyMessage(0);
					}
					// }
				} catch (SocketTimeoutException e) {
					sendHandler.sendEmptyMessage(10);
					e.printStackTrace();
				} catch (Exception e) {
					sendHandler.sendEmptyMessage(0);
					e.printStackTrace();
				}
				times.remove(csb.getTime());
			}

		}.start();
	}

	private void startThread(String filepath, boolean isPhoto,
			String finishToast, LinkedHashMap<String, String> fileTempsList,
			LinkedHashMap<String, String> photoTempsList,
			final CommentSendBean nb) {
		String url = URLProperties.MEMBER_JSON;
		String method = "";
		// String name = null;
		File file = null;
		if (isPhoto) {
			file = new File(ThumbnailImgUtil.getData(filepath));
			L.d(TAG, "filepath:" + filepath);
			method = APIMethodName.UPLOAD_PHOTO;
			file = FileUtil.ChangeImage(file, false);
		} else {
			file = new File(filepath);
			method = APIMethodName.UPLOAD_FILE;
		}
		L.d(TAG, "filepath:" + file.getAbsolutePath());
		String params = new UploadFileParams(App.app, method, file.getName(),
				null).getParams();
		L.d(TAG, "startThread url=" + url + "?" + params);
		try {
			String result = httpUpload(url + "?" + params, file, isPhoto,
					finishToast);
			L.d(TAG, "startThread result=" + result);
			if (!allowedPublish) {
				return;
			}
			if (result != null) {
				final JSONObject object = new JSONObject(result);
				if (object.getInt("resultCode") == 1) {
					String temps = object.getString("result");
					if (isPhoto) {
						photoTempsList.put(filepath, temps);
					} else {
						fileTempsList.put(file.getPath(), temps);
					}
				} else {
					onUploadFailed(file, nb);
				}
			} else {
				onUploadFailed(file, nb);
			}
		} catch (Exception e) {
			e.printStackTrace();
			onUploadFailed(file, nb);
		}
	}

	private void onUploadFailed(File file, CommentSendBean nb) {
		allowedPublish = false;
		HandlerUtil.sendMsgToHandler(sendHandler, 9, file.getName()
				+ "上传失败，请重新上传！", 0);
		times.remove(nb.getTime());
	}

	/**
	 * HttpURLConnection POST上传文件
	 * 
	 * @param uploadUrl
	 * @param filename
	 * @throws Exception
	 */
	private String httpUpload(String uploadUrl, File file, boolean isPhoto,
			String finishToast) throws Exception {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		DataOutputStream dos = null;
		FileInputStream fis = null;
		String result = null;

		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = HttpRequestServers
					.getHttpURLConnection(url);

			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			String reqHeader = twoHyphens
					+ boundary
					+ end
					+ "Content-Disposition: form-data; name=\"upload\"; filename=\""
					+ file.getName() + "\"" + end
					+ "Content-Type: application/octet-stream" + end + end;
			String reqEnder = end + twoHyphens + boundary + twoHyphens + end;

			long totalLength = file.length();
			httpURLConnection.setFixedLengthStreamingMode(reqHeader.length()
					+ (int) (totalLength) + reqEnder.length());

			dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(reqHeader);
			L.d(TAG, "path=" + file.getAbsolutePath());
			fis = new FileInputStream(file);
			L.d(TAG, "httpUpload totalLength=" + totalLength);
			long uploadSize = 0;
			int progress = 0;
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				if (!allowedPublish) {
					return null;
				}
				dos.write(buffer, 0, count);
				uploadSize += count;
				progress = (int) (uploadSize * 100 / totalLength);
				if (progress > 0) {
					L.d(TAG, "httpUpload uploadSize=" + uploadSize
							+ ",progress=" + progress);
				}

				HandlerUtil.sendMsgToHandler(sendHandler, 2, null, progress);
				if (progress >= 100) {
					HandlerUtil.sendMsgToHandler(sendHandler,
							HandlerUtil.getMessage(4, finishToast, 0, 0));
				}
			}

			dos.writeBytes(reqEnder);
			dos.flush();

			L.d(TAG, "httpUpload dos.size=" + dos.size());
			result = StreamUtil.readData(httpURLConnection.getInputStream());
			L.d(TAG, "httpUpload result=" + result);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			if (dos != null)
				try {
					dos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	/**
	 * 加载完成提醒
	 * 
	 * @param msg
	 */
	private void updateUI(String msg) {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.cancel();
		}
		T.show(App.app, msg);
	}

	@SuppressLint("HandlerLeak")
	private Handler sendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI("发送失败");
				break;
			case 10:
				updateUI("发送失败");
				break;
			case 1:
				InfoWallActivity.isNeedRefresh = true;
				updateUI("已发布！");
				CommentSendBean csb = (CommentSendBean) msg.obj;
				CommentSendService.getService(context).deleteComment(
						csb.getTime());
				csbs.remove(csb);
				notifyDataSetChanged();
				break;
			case 2:
				if (null != dialogContent) {
					dialogContent.updatePBar(msg.arg1);
					if (null != msg.obj && 0 == msg.arg1) {
						if (1 == msg.arg2) { // 文件
							dialogContent.setFileIcon((File) msg.obj);
						} else if (-1 == msg.arg2) {
							dialogContent.setIcon((String) msg.obj);
						}
					}
				}
				break;

			case 3:
				if (null != dialogContent) {
					dialogContent.startNewFile((String) msg.obj);
				}
				break;

			case 4:
				if (null != dialogContent) {
					dialogContent.finishFile((String) msg.obj);
					dialogContent.setPublishProgress(msg.arg1);
				}
				break;
			case 5:
				if (null != dialogContent) {
					dialogContent.finishFile((String) msg.obj);
					dialogContent.setPublishProgress(msg.arg1);
					dialogContent.showDialogBar();
				}

				if (msg.arg1 == View.GONE) {
					Button b = pDialog.getButton(Dialog.BUTTON_NEGATIVE);
					b.setTag(new Object());
					b.setText("后台发送");
				}
				break;
			case 9:
				showTip((String) msg.obj);
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.cancel();
				}
				break;

			default:
				break;
			}
		}
	};

	private SpinnerProgressDialog spdDialog;

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private CommentSendBean csb;

		public LoadInfoTask(CommentSendBean csb) {
			this.csb = csb;
			spdDialog = new SpinnerProgressDialog(context);
			spdDialog.showProgressDialog("正在评论中...");
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				String ats = csb.getAt();
				String[] rIds = null;
				if (ats != null && ats.length() > 0) {
					rIds = ats.split("#");
				}
				result = APIRequestServers.commentResource(App.app,
						csb.getGid() + "", csb.getRid() + "", csb.getmType(),
						csb.getContent(), rIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
			times.remove(csb.getTime());
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult) {
				showTip("评论失败");
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip("评论失败");
				} else {
					CommentSendService.getService(context).deleteComment(
							csb.getTime());
					showTip("评论成功");
					csbs.remove(csb);
					notifyDataSetChanged();
				}
			}
		}
	}

	private void showTip(String text) {
		T.show(App.app, text);
	}

	class ViewHolder {
		MixedTextView content, quubo;
		ImageView more, img;
		TextView fileNum, imgNum;
		LinearLayout files;
		RelativeLayout images;
		View blank1, blank2;
	}

}

package com.datacomo.mc.spider.android.adapter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseActivity;
import com.datacomo.mc.spider.android.CloudNoteWithActivity;
import com.datacomo.mc.spider.android.FriendsChooserActivity;
import com.datacomo.mc.spider.android.GroupsChooserActivity;
import com.datacomo.mc.spider.android.MailCreateActivity;
import com.datacomo.mc.spider.android.NoteActivity;
import com.datacomo.mc.spider.android.NoteCreateActivity;
import com.datacomo.mc.spider.android.NoteDetailsActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.NoteDraftBean;
import com.datacomo.mc.spider.android.db.NoteCreateService;
import com.datacomo.mc.spider.android.net.APINoteRequestServers;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MailBean;
import com.datacomo.mc.spider.android.net.been.note.NoteBookBean;
import com.datacomo.mc.spider.android.net.been.note.NoteInfoBean;
import com.datacomo.mc.spider.android.params.UploadFileParams;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.HandlerUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.StreamUtil;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.util.ThumbnailImgUtil;
import com.datacomo.mc.spider.android.view.DialogView;

public class CloudNoteListAdapter extends BaseAdapter {
	private final String TAG = "CloudNoteListAdapter";
	private Context mContext;
	private ArrayList<NoteInfoBean> notes;

	private static ArrayList<Long> times = new ArrayList<Long>();

	private boolean isLocal;

	public CloudNoteListAdapter(Context c, ArrayList<NoteInfoBean> notes) {
		mContext = c;
		this.notes = notes;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	@Override
	public int getCount() {
		return notes.size();
	}

	@Override
	public Object getItem(int position) {
		return notes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_cloud_note, null);
			holder.num = (TextView) convertView
					.findViewById(R.id.item_note_num);
			holder.send = (ImageView) convertView
					.findViewById(R.id.item_note_send);
			holder.title = (TextView) convertView
					.findViewById(R.id.item_note_title);
			holder.content = (TextView) convertView
					.findViewById(R.id.item_note_content);
			holder.time = (TextView) convertView
					.findViewById(R.id.item_note_time);
			holder.name = (TextView) convertView
					.findViewById(R.id.item_note_name);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.item_note_iv);
			holder.link = (ImageView) convertView
					.findViewById(R.id.item_note_link);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final NoteInfoBean note = notes.get(position);

		holder.num.setVisibility(View.GONE);
		if (!isLocal && note.getIsRead() == 1) {
			holder.num.setVisibility(View.VISIBLE);
		}

		if (note.getNoteId() == 0) {
			holder.send.setVisibility(View.VISIBLE);
			holder.send.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						save(((NoteActivity) mContext).beans.get(position));
					} catch (Exception e) {
						v.setClickable(true);
					}
				}
			});
		} else {
			holder.send.setVisibility(View.GONE);
		}

		holder.title.setText(note.getNoteTitle());
		holder.content.setText(CharUtil.filterText(note.getNoteContent()
				.replace("\n", " ").replace("\t", " ").replace("<br/ >", " ")
				.replace("&nbsp;", " ")));
		holder.time.setText(DateTimeUtil.aTimeFormat(DateTimeUtil
				.getLongTime(note.getUpdateTime())));

		holder.name.setVisibility(View.INVISIBLE);
		String shareMemberName = note.getShareMemberName();
		if (shareMemberName != null && !"".equals(shareMemberName)) {
			holder.name.setText("Fr: " + shareMemberName);
			holder.name.setVisibility(View.VISIBLE);
		}

		if (note.getAdjunctNum() > 0) {
			holder.link.setVisibility(View.VISIBLE);
		} else {
			holder.link.setVisibility(View.GONE);
		}

		String url = note.getFirstPhotoPath();
		if (url != null && !"".equals(url)) {
			if (note.getNoteId() == 0) {
				MyFinalBitmap.setLocalAndDisPlayImage(null, holder.icon, url);
			} else {
				url = URLProperties.FILE_URL
						+ url.replace("_680_0", "_120_120");
				MyFinalBitmap.setImage(mContext, holder.icon, url);
			}
			holder.icon.setVisibility(View.VISIBLE);
		} else {
			holder.icon.setVisibility(View.GONE);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				L.d(TAG, "NoteId=" + note.getNoteId());
				if (note.getNoteId() == 0) {
					Bundle b = new Bundle();
					b.putInt("position", position);
					b.putInt("id", note.getNoteBookId());
					if (mContext instanceof NoteActivity) {
						b.putSerializable("NoteDraftBean",
								((NoteActivity) mContext).beans.get(position));
					} else if (mContext instanceof CloudNoteWithActivity) {
						b.putSerializable("NoteDraftBean",
								((CloudNoteWithActivity) mContext).beans
										.get(position));
					}
					LogicUtil.enter(mContext, NoteCreateActivity.class, b, 10);
				} else {
					Bundle b = new Bundle();
					b.putString("diaryId", note.getNoteId() + "");
					b.putString("diaryTitle", note.getNoteTitle());
					b.putString("shareMemberName", note.getShareMemberName());
					b.putString("shareMemberId", note.getShareMemberId() + "");
					b.putString("updateTime", note.getUpdateTime());
					LogicUtil.enter(mContext, NoteDetailsActivity.class, b, 10);
				}
			}
		});

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (note.getNoteId() != 0) {
					operNote(note, v, position);
				} else {
					new AlertDialog.Builder(mContext)
							.setTitle("提示")
							.setMessage("您确定要删除此笔记吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											NoteCreateService
													.getService(mContext)
													.delete(((NoteActivity) mContext).beans
															.get(position)
															.getTime(), 0);
											notes.remove(position);
											notifyDataSetChanged();
										}
									}).setNegativeButton("取消", null).show();
				}
				return false;
			}
		});

		return convertView;
	}

	class ViewHolder {
		ImageView icon, link, send;
		TextView num, title, content, time, name;
	}

	private void operNote(final NoteInfoBean bean, View v, final int position) {
		String[] data = { "分享", "移动", "星标", "删除" };
		final int isStarTarget = bean.getIsStarTarget();
		if (isStarTarget == 1) {
			data = new String[] { "分享", "移动", "取消星标", "删除" };
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setAdapter(new ArrayAdapter(mContext, R.layout.choice_item,
						data), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							shareNote(bean);
							break;
						case 1:
							getNoteBook(bean, position);
							break;
						case 2:
							if (isStarTarget == 1) {
								bean.setIsStarTarget(2);
							} else {
								bean.setIsStarTarget(1);
							}
							notes.remove(position);
							notes.add(position, bean);
							notifyDataSetChanged();
							new Thread() {
								public void run() {
									String isAddStarMark = "1";
									if (isStarTarget == 1)
										isAddStarMark = "2";
									try {
										APINoteRequestServers
												.addOrDeleteStarMark(mContext,
														bean.getNoteId() + "",
														isAddStarMark);
									} catch (Exception e) {
										e.printStackTrace();
									}
								};
							}.start();
							break;
						case 3:
							deleteNote(bean, position);
							break;
						}
					}
				});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	private int dex = 0;

	private void getNoteBook(NoteInfoBean bean, int position) {
		ArrayList<NoteBookBean> noteBooks = null;
		try {
			noteBooks = (ArrayList<NoteBookBean>) LocalDataService
					.getInstense()
					.getLocNoteBooks(mContext, LocalDataService.TXT_MYBOOK)
					.getNOTEBOOKLIST();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (noteBooks != null && noteBooks.size() > 0) {
			selectBook(noteBooks, bean, position);
		} else {
			new LoadInfoTask(bean, position).execute();
		}
	}

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {

		private NoteInfoBean bean;
		private int position;

		@SuppressWarnings("static-access")
		public LoadInfoTask(NoteInfoBean bean, int position) {
			this.bean = bean;
			this.position = position;
			((BaseActivity) mContext).spdDialog
					.showProgressDialog("正在获取笔记本列表...");
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APINoteRequestServers.cloudNoteBookList(mContext, "0",
						"10");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings("static-access")
		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			((BaseActivity) mContext).spdDialog.cancelProgressDialog(null);
			if (null == mcResult || 1 != mcResult.getResultCode()) {
				T.show(mContext, T.ErrStr);
			} else {
				ArrayList<NoteBookBean> noteBooks = null;
				try {
					noteBooks = (ArrayList<NoteBookBean>) LocalDataService
							.getInstense()
							.getLocNoteBooks(mContext,
									LocalDataService.TXT_MYBOOK)
							.getNOTEBOOKLIST();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (noteBooks != null && noteBooks.size() > 0) {
					selectBook(noteBooks, bean, position);
				} else {
					T.show(mContext, T.ErrStr);
				}
			}
		}

	}

	private void selectBook(ArrayList<NoteBookBean> noteBooks,
			final NoteInfoBean bean, final int position) {
		int size = noteBooks.size();
		final String[] books = new String[size];
		final String[] ids = new String[size];
		for (int i = 0; i < size; i++) {
			books[i] = noteBooks.get(i).getTitle();
			ids[i] = noteBooks.get(i).getNoteBookId() + "";
			if (books[i].equals(bean.getNoteBookName())) {
				dex = i;
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setTitle("移动笔记本")
				.setSingleChoiceItems(books, dex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dex = which;
							}
						})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new MoveBookTask(ids[dex], bean, position).execute();
					}
				}).setNegativeButton("取消", null);
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	private void deleteNote(final NoteInfoBean bean, final int position) {
		new AlertDialog.Builder(mContext).setTitle("提示")
				.setMessage("您确定要删除此笔记吗？删除后将无法恢复。")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new DeleteNoteTask(bean, position).execute();
					}
				}).setNegativeButton("取消", null).show();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shareNote(final NoteInfoBean bean) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setTitle("分享").setAdapter(
						new ArrayAdapter(mContext, R.layout.choice_item,
								new String[] { "分享到朋友", "分享到交流圈", "邮件分享" }),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:// 分享到朋友
									Intent intent = new Intent(mContext,
											FriendsChooserActivity.class);
									intent.putExtra("tpye", 3);
									intent.putExtra("id", bean.getNoteId());
									((Activity) mContext)
											.startActivityForResult(
													intent,
													FriendsChooserActivity.RESCODE);
									break;
								case 1:// 分享到交流圈
									Bundle bd = new Bundle();
									bd.putString("btnString", "分享");
									bd.putInt("id", bean.getNoteId());
									LogicUtil.enter(mContext,
											GroupsChooserActivity.class, bd,
											GroupsChooserActivity.RESCODE);
									break;
								case 2:
									MailBean mailBean = new MailBean();
									mailBean.setMailSubject(bean.getNoteTitle());
									mailBean.setMailContent(bean
											.getNoteContent());
									Intent im = new Intent(mContext,
											MailCreateActivity.class);
									im.putExtra("sendType", 5);
									im.putExtra("MailBean", mailBean);
									mContext.startActivity(im);
									break;
								}
							}
						});// .setNegativeButton("取消", null).show();
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	class MoveBookTask extends AsyncTask<String, Integer, MCResult> {
		private String notebookId;
		private NoteInfoBean bean;
		private int position;

		@SuppressWarnings("static-access")
		public MoveBookTask(String notebookId, NoteInfoBean bean, int position) {
			this.notebookId = notebookId;
			this.bean = bean;
			this.position = position;
			((BaseActivity) mContext).spdDialog.showProgressDialog("正在处理中...");
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APINoteRequestServers.moveNoteToOtherNoteBook(
						mContext, bean.getNoteId() + "", notebookId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings("static-access")
		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			((BaseActivity) mContext).spdDialog.cancelProgressDialog(null);
			if (null == mcResult) {
				T.show(mContext, T.ErrStr);
			} else {
				if (1 == mcResult.getResultCode()) {
					if ("1".equals(mcResult.getResult().toString())) {
						T.show(mContext, "移动成功");
						try {
							bean.setNotebookId(Integer.valueOf(notebookId));
							notes.remove(position);
							notes.add(position, bean);
							notifyDataSetChanged();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						T.show(mContext, "移动失败");
					}
				} else {
					T.show(mContext, "移动失败");
				}
			}
		}
	}

	class DeleteNoteTask extends AsyncTask<String, Integer, MCResult> {

		private NoteInfoBean bean;
		private int position;

		public DeleteNoteTask(NoteInfoBean bean, int position) {
			this.bean = bean;
			this.position = position;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APINoteRequestServers.deleteCloudNote(mContext,
						bean.getNoteId() + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null == mcResult) {
				T.show(mContext, T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					T.show(mContext, T.ErrStr);
				} else {
					T.show(mContext, "已删除！");
					notes.remove(position);
					notifyDataSetChanged();
				}
			}
		}
	}

	AlertDialog pDialog;
	DialogView dialogContent;
	boolean allowedPublish;

	private void save(final NoteDraftBean nb) throws JSONException {
		if (times.contains(nb.getTime())) {
			T.show(mContext, "正在后台发送中...");
			return;
		} else {
			L.d(TAG, "save add time=" + nb.getTime());
			times.add(nb.getTime());
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		TextView dTitle = new TextView(mContext);
		dTitle.setGravity(Gravity.CENTER);
		dTitle.setPadding(0, 15, 0, 0);
		dTitle.setTextSize(22);
		dTitle.setText("发送中...");
		builder.setCustomTitle(dTitle);
		dialogContent = new DialogView(mContext);
		String allInfo = null;

		final ArrayList<String> fPaths = new ArrayList<String>();
		final ArrayList<String> pPaths = new ArrayList<String>();
		String filePaths = nb.getFilePaths();
		JSONArray array = new JSONArray(filePaths);
		for (int i = 0; i < array.length(); i++) {
			JSONObject json = array.getJSONObject(i);
			if (json.getInt("type") == 1) {
				fPaths.add(json.getString("path"));
			} else {
				pPaths.add(json.getString("path"));
			}
		}
		int numFile = fPaths.size();
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
						times.remove(nb.getTime());
						L.d(TAG, "save remove time=" + nb.getTime());
					}
				});
		pDialog = builder.show();
		new Thread() {
			public void run() {
				allowedPublish = true;
				try {
					String nContentStr = nb.getContent();
					String nTitleStr = nb.getTitle();
					if (nContentStr == null || "".equals(nContentStr)) {
						nContentStr = nTitleStr;
					}
					if (nTitleStr == null || "".equals(nTitleStr)) {
						nTitleStr = nContentStr;
						while (nTitleStr.startsWith(" ")
								|| nTitleStr.startsWith("\n")) {
							nTitleStr = nTitleStr.substring(1,
									nTitleStr.length()).trim();
						}
						if (nTitleStr.contains("\n"))
							nTitleStr = nTitleStr.substring(0,
									nTitleStr.indexOf("\n"));
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
								int fsize = fileTempsList.size();
								int psize = photoTempsList.size();
								if (fsize == 0) {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片");
								} else {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片，", (fsize),
											" 个文件");
								}

								HandlerUtil.sendMsgToHandler(sendHandler, 3,
										StringUtil.merge("正在上传第 ", (1 + psize),
												" 张图片..."), 0);
								HandlerUtil.sendMsgToHandler(sendHandler,
										HandlerUtil.getMessage(2, path, 0, -1));
							}
							startThread(path, true, toast, fileTempsList,
									photoTempsList, nb);
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
								int fsize = fileTempsList.size();
								int psize = photoTempsList.size();
								if (psize == 0) {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 个文件");
								} else {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片，", (fsize),
											" 个文件");
								}
								HandlerUtil.sendMsgToHandler(sendHandler, 3,
										StringUtil.merge("正在上传第 ", (1 + fsize),
												" 个文件..."), 0);
								HandlerUtil.sendMsgToHandler(sendHandler,
										HandlerUtil.getMessage(2,
												new File(path), 0, 1));
							}
							startThread(path, false, toast, fileTempsList,
									photoTempsList, nb);
						}
					}
					if (!allowedPublish) {
						return;
					}
					HandlerUtil.sendMsgToHandler(sendHandler, 5, "正在发布笔记...",
							View.GONE);

					int i = photoTempsList.size();
					L.d(TAG, "photoTempsList.size" + photoTempsList.size());

					// String[] photoTemps = new String[i];
					String firstPhotoPath = null;
					for (Entry<String, String> entry : photoTempsList
							.entrySet()) {
						// photoTemps[i - 1] = entry.getValue();
						i--;
						try {
							JSONObject json = new JSONObject(entry.getValue());
							firstPhotoPath = json.getString("adjunctPath");
							nContentStr += String
									.format("<br /><img src=\"%s\" alt=\"\" /><br />",
											json.getString("adjunctUrl")
													+ ThumbnailImageUrl
															.getThumbnailNoteUrl(firstPhotoPath));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					i = fileTempsList.size();
					String[] fileTemps = new String[i];
					for (Entry<String, String> entry : fileTempsList.entrySet()) {
						fileTemps[i - 1] = entry.getValue();
						i--;
						try {
							JSONObject json = new JSONObject(entry.getValue());
							String formatUrl = json.getString("formatUrl");
							String adjunctPath = json.getString("adjunctPath");
							String formatPath = json.getString("formatPath");
							String adjunctSize = json.getString("adjunctSize");
							String adjunctName = json.getString("adjunctName");
							String adjunctUrl = json.getString("adjunctUrl");
							// formatUrl":"https://img.yuuquu.com","adjunctPath":"/note_file/2014/08/05/201408051541304820.jpg",
							// "formatPath":"/2014/08/05/201408051541313061.png","noteId":0,"adjuctLocal":"","formatName":"",
							// "adjunctSize":27480,"formatId":0,"adjunctName":"img-427b870c51526ee02f316168aa1d47c9.jpg",
							// "adjunctUrl":"https://img.yuuquu.com","urlAndPath":[],"uploadTime":null}
							nContentStr += "<br /><div class=\"mojiImgBks\"><a class=\"allShareYun\" href=\"javascript:;\" title=\"转存为云文件\" "
									+ "style=\"display:none;\"></a><a href=\"http://www.yuuquu.com/file/downloadDiaryAttachment.do?attachmentPath="
									+ adjunctPath
									+ "&attachmentName="
									+ adjunctName
									+ "\">"
									+ "<img class=\"note-accessory\" rel=\""
									+ adjunctName
									+ ","
									+ adjunctSize
									+ ","
									+ adjunctUrl
									+ ","
									+ adjunctPath
									+ ","
									+ adjunctName.substring(adjunctName
											.lastIndexOf(".") + 1)
									+ "\" style=\"padding-top:3px;border:1px solid #ccc;display:block;\" src=\""
									+ formatUrl
									+ formatPath
									+ "\" /></a></div><br />";
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					MCResult mc = null;
					try {
						nContentStr = nContentStr.replaceAll("\n", "<br />");
						int noteBookId = nb.getNoteBookId();
						mc = APINoteRequestServers.createCloudNote(mContext,
								(noteBookId != 0) + "", noteBookId + "",
								nTitleStr, nContentStr, fileTemps, "2", null,
								firstPhotoPath);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (mc != null) {
						int resultCode = mc.getResultCode();
						L.i(TAG, "sendGroupTopic resultCode=" + resultCode);
						if (!allowedPublish) {
							return;
						}
						if (resultCode == 1) {
							sendHandler.sendEmptyMessage(1);
							NoteCreateService.getService(mContext).delete(
									nb.getTime(), 0);
						} else {
							sendHandler.sendEmptyMessage(0);
						}
					} else {
						sendHandler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					sendHandler.sendEmptyMessage(0);
					e.printStackTrace();
				}

				L.d(TAG, "save remove time=" + nb.getTime());
				times.remove(nb.getTime());
			}

		}.start();
	}

	private void startThread(String filepath, boolean isPhoto,
			String finishToast, LinkedHashMap<String, String> fileTempsList,
			LinkedHashMap<String, String> photoTempsList, final NoteDraftBean nb) {
		String url = URLProperties.CLOUD_NOTE_JSON;
		String method = "";
		// String name = null;
		File file = null;
		if (isPhoto) {
			file = new File(ThumbnailImgUtil.getData(filepath));
			L.d(TAG, "filepath:" + filepath);
			method = "uploadAttachmentImge";
			file = FileUtil.ChangeImage(file, false);
		} else {
			file = new File(filepath);
			method = "uploadAttachmentFile";
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

	private void onUploadFailed(File file, NoteDraftBean nb) {
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
		T.show(mContext, msg);
	}

	@SuppressLint("HandlerLeak")
	private Handler sendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI(T.ErrStr);
				break;
			case 10:
				updateUI(T.TimeOutStr);
				break;
			case 1:
				updateUI("已发布！");
				((NoteActivity) mContext).loadInfo(0, true);
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
				T.show(mContext, (String) msg.obj);
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.cancel();
				}
				break;

			default:
				break;
			}
		}
	};

}

package com.datacomo.mc.spider.android;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.NoteInfoService;
import com.datacomo.mc.spider.android.net.APINoteRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MailBean;
import com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean;
import com.datacomo.mc.spider.android.net.been.note.NoteBookBean;
import com.datacomo.mc.spider.android.net.been.note.NoteInfoBean;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.umeng.analytics.MobclickAgent;

public class NoteDetailsActivity extends BasicActionBarActivity {
	private static String TAG = "NoteDetailsActivity";

	private TextView blog_detail_title, blog_detail_date, share_by, pro_tv;
	private WebView blog_detail_webview;
	private LinearLayout pro_layout;
	private ProgressBar pro_bar;
	private ImageView iv_edit, iv_share, iv_book, iv_star, iv_more;

	// private String updateTime;
	private String noteContent;

	private String noteTitle = "";
	private String noteId = "";
	// private DiaryInfoBean bean;
	private NoteInfoBean bean;
	private String[] shareFriendIds;
	private String[] shareGroupIds;

	private boolean flag_friendSharing;
	private boolean flag_groupSharing;
	// private String sendMemberName;
	// private String SendMemberId;
	private boolean refresh;

	/** 全局web样式 */
	public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} "
			+ "img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} "
			+ "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;}</style>";

	private RefreshListView rlv;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "17");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.cloudnotedetails);
		ab.setTitle("查看笔记");
		init();

		initCommentData(getIntent());
	}

	private void init() {
		rlv = (RefreshListView) findViewById(R.id.cloudnotedetails_list);
		rlv.setDivider(null);
		LinearLayout llayout_Header = (LinearLayout) ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.note_details, null);
		blog_detail_title = (TextView) llayout_Header
				.findViewById(R.id.blog_detail_title);
		blog_detail_date = (TextView) llayout_Header
				.findViewById(R.id.blog_detail_date);
		share_by = (TextView) llayout_Header.findViewById(R.id.share_by);

		blog_detail_webview = (WebView) llayout_Header
				.findViewById(R.id.blog_detail_webview);

		WebSettings webSettings = blog_detail_webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// webSettings.setSupportZoom(true);
		// webSettings.setBuiltInZoomControls(true);
		webSettings.setDefaultFontSize(15);
		// webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		pro_layout = (LinearLayout) llayout_Header.findViewById(R.id.note_pro);
		pro_tv = (TextView) llayout_Header.findViewById(R.id.text);
		pro_bar = (ProgressBar) llayout_Header.findViewById(R.id.progress);
		pro_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!pro_bar.isShown()) {
					pro_bar.setVisibility(View.VISIBLE);
					pro_tv.setText("正在加载中…");
					new DiaryInfoTask(noteId).execute();
				}
			}
		});

		rlv.addHeaderView(llayout_Header, null, false);
		rlv.setAdapter(null);

		iv_edit = (ImageView) findViewById(R.id.note_detail_edit);
		iv_share = (ImageView) findViewById(R.id.note_detail_share);
		iv_book = (ImageView) findViewById(R.id.note_detail_book);
		iv_star = (ImageView) findViewById(R.id.note_detail_star);
		iv_more = (ImageView) findViewById(R.id.note_detail_more);
		iv_edit.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		iv_book.setOnClickListener(this);
		iv_star.setOnClickListener(this);
		iv_more.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.note_detail_edit:
			Bundle b = new Bundle();
			b.putSerializable("diaryInfoBean", bean);
			LogicUtil.enter(NoteDetailsActivity.this, NoteCreateActivity.class,
					b, 10);
			break;
		case R.id.note_detail_share:
			shareNote();
			break;
		case R.id.note_detail_book:
			getNoteBook();
			break;
		case R.id.note_detail_star:
			final int isStarTarget = bean.getIsStarTarget();
			if (isStarTarget == 1) {
				bean.setIsStarTarget(2);
				iv_star.setImageResource(R.drawable.note_star_1);
			} else {
				bean.setIsStarTarget(1);
				iv_star.setImageResource(R.drawable.note_star_2);
			}
			new Thread() {
				public void run() {
					String isAddStarMark = "1";
					if (isStarTarget == 1)
						isAddStarMark = "2";
					try {
						APINoteRequestServers.addOrDeleteStarMark(
								NoteDetailsActivity.this, noteId + "",
								isAddStarMark);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
			break;
		case R.id.note_detail_more:
			moreNote();
			break;
		default:
			break;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void moreNote() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setAdapter(
				new ArrayAdapter(this, R.layout.choice_item, new String[] {
						"笔记详情", "删除" }), new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Bundle b = new Bundle();
							b.putSerializable("diaryInfoBean", bean);
							LogicUtil.enter(NoteDetailsActivity.this,
									NoteCloudDetailsActivity.class, b, false);
							break;
						case 1:
							deleteNote();
							break;
						}
					}
				});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);

		ad.show();
	}

	private int dex = 0;

	private void getNoteBook() {
		ArrayList<NoteBookBean> noteBooks = null;
		try {
			noteBooks = (ArrayList<NoteBookBean>) LocalDataService
					.getInstense()
					.getLocNoteBooks(this, LocalDataService.TXT_MYBOOK)
					.getNOTEBOOKLIST();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (noteBooks != null && noteBooks.size() > 0) {
			selectBook(noteBooks);
		} else {
			new LoadInfoTask().execute();
		}
	}

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {

		public LoadInfoTask() {
			spdDialog.showProgressDialog("正在获取笔记本列表...");
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APINoteRequestServers.cloudNoteBookList(
						NoteDetailsActivity.this, "0", "10");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult || 1 != mcResult.getResultCode()) {
				showTip(T.ErrStr);
			} else {
				ArrayList<NoteBookBean> noteBooks = null;
				try {
					noteBooks = (ArrayList<NoteBookBean>) LocalDataService
							.getInstense()
							.getLocNoteBooks(NoteDetailsActivity.this,
									LocalDataService.TXT_MYBOOK)
							.getNOTEBOOKLIST();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (noteBooks != null && noteBooks.size() > 0) {
					selectBook(noteBooks);
				} else {
					showTip(T.ErrStr);
				}
			}
		}

	}

	private void selectBook(ArrayList<NoteBookBean> noteBooks) {
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

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("移动笔记本")
				.setSingleChoiceItems(books, dex,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dex = which;
							}
						})
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new MoveBookTask(ids[dex]).execute();
					}
				}).setNegativeButton("取消", null);
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * 分享文件
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shareNote() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"分享").setAdapter(
				new ArrayAdapter(this, R.layout.choice_item, new String[] {
						"分享到朋友", "分享到交流圈", "邮件分享" }),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:// 分享到朋友
							Intent intent = new Intent(
									NoteDetailsActivity.this,
									FriendsChooserActivity.class);
							intent.putExtra("tpye", 3);
							startActivityForResult(intent,
									FriendsChooserActivity.RESCODE);
							break;
						case 1:// 分享到交流圈
							Bundle bd = new Bundle();
							bd.putString("btnString", "分享");
							LogicUtil.enter(NoteDetailsActivity.this,
									GroupsChooserActivity.class, bd,
									GroupsChooserActivity.RESCODE);
							break;
						case 2:
							MailBean mailBean = new MailBean();
							mailBean.setMailSubject(bean.getNoteTitle());
							mailBean.setMailContent(bean.getNoteContent());
							Intent im = new Intent(NoteDetailsActivity.this,
									MailCreateActivity.class);
							im.putExtra("sendType", 5);
							im.putExtra("MailBean", mailBean);
							startActivity(im);
							break;
						}
					}
				});// .setNegativeButton("取消", null).show();
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	private void initCommentData(Intent i) {
		Bundle b = i.getExtras();
		noteId = b.getString("diaryId");
		noteTitle = b.getString("diaryTitle");
		blog_detail_title.setText(noteTitle);

		refresh = b.getBoolean("refresh", false);

		String shareMemberName = b.getString("shareMemberName");
		if (shareMemberName != null) {
			share_by.setText(shareMemberName + " ");
			share_by.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putString("id", b.getString("shareMemberId"));
					LogicUtil.enter(NoteDetailsActivity.this,
							HomePgActivity.class, b, false);
				}
			});
		}

		try {
			bean = NoteInfoService.getService(NoteDetailsActivity.this)
					.queryMail(Integer.valueOf(noteId)).getNOTEINFOBEAN();
			if (bean != null)
				updateMsg(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		new DiaryInfoTask(noteId).execute();
	}

	private void updateMsg(boolean isLocal) {
		if (!isLocal)
			findViewById(R.id.note_detail_oper).setVisibility(View.VISIBLE);

		noteTitle = bean.getNoteTitle();
		L.d(TAG, "updateMsg noteTitle=" + noteTitle);
		if (noteTitle != null)
			noteTitle = noteTitle.replaceAll("\n", " ");
		blog_detail_title.setText(noteTitle);

		String ts = "创建";
		String share = bean.getShareMemberName();
		if (share != null && !"".equals(share)) {
			ts = "分享";
			share_by.setText(share + "  ");
			share_by.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putString("id", bean.getShareMemberId() + "");
					LogicUtil.enter(NoteDetailsActivity.this,
							HomePgActivity.class, b, false);
				}
			});
		}

		String ct = DateTimeUtil.cTimeFormat(DateTimeUtil.getLongTime(bean
				.getCreateTime()));
		String ut = DateTimeUtil.cTimeFormat(DateTimeUtil.getLongTime(bean
				.getUpdateTime()));
		ts += "于 " + ct;
		if (!ct.equals(ut))
			ts += "  更新于 " + ut;
		blog_detail_date.setText(ts);

		noteContent = bean.getNoteContent();

		noteContent = noteContent.replaceAll(
				"(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
		noteContent = noteContent.replaceAll(
				"(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
		if (!noteContent.trim().startsWith("<style>")) {
			String html = WEB_STYLE;
			noteContent = html + noteContent;
		}

		noteContent = "<head><style type=\"text/css\"><!--body{line-height:150%}--></style></head><body>"
				+ noteContent + "</body>";
		blog_detail_webview.loadDataWithBaseURL(null, noteContent
				+ "<br/ ><br/ ><br/ >", "text/html", "utf-8", null);
		blog_detail_webview.setWebViewClient(getWebViewClient());

		pro_layout.setVisibility(View.GONE);

		int isStarTarget = bean.getIsStarTarget();
		if (isStarTarget == 1) {
			iv_star.setImageResource(R.drawable.note_star_2);
		} else {
			iv_star.setImageResource(R.drawable.note_star_1);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initCommentData(intent);
	}

	/**
	 * 获取webviewClient对象
	 * 
	 * @return
	 */
	public WebViewClient getWebViewClient() {
		return new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				L.d(TAG, "getWebViewClient url=" + url);
				if (getNoteFile(url)) {
					return true;
				}
				showUrlRedirect(view.getContext(), url);
				return true;
			}
		};
	}

	/**
	 * 下载笔记附件
	 * 
	 * @param url
	 * @return
	 */
	private boolean getNoteFile(String url) {
		String adjunctPath = "";
		String name = "";
		if (url != null && url.contains("downloadDiaryAttachment.do")) {
			try {
				String str2 = url.substring(url.indexOf("?") + 1);
				String[] strs = str2.split("&");
				adjunctPath = strs[0].substring(strs[0].indexOf("=") + 1);
				name = strs[1].substring(strs[1].indexOf("=") + 1);
				name = URLDecoder.decode(name, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}

		if ("".equals(adjunctPath))
			return false;

		operAttachment(adjunctPath, name);
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void operAttachment(final String adjunctPath, final String name) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				name).setAdapter(
				new ArrayAdapter(this, R.layout.choice_item, new String[] {
						"打开", "转存到云文件" }),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							String downloadUrl = URLProperties.FILE_URL
									+ adjunctPath;
							final String tempName = downloadUrl
									.substring(downloadUrl.lastIndexOf("/") + 1);
							File myFile = new File(ConstantUtil.CLOUD_PATH
									+ tempName);
							if (myFile != null && myFile.exists()) {
								new FileUtil().openFile(
										NoteDetailsActivity.this, myFile);
								return;
							} else if (ConstantUtil.downloadingList
									.contains(tempName)) {
								showTip(NoteDetailsActivity.this.getResources()
										.getString(R.string.downloading));
								return;
							}

							ConstantUtil.downloadingList.add(tempName);
							new DownLoadFileThread(NoteDetailsActivity.this,
									downloadUrl, 0, name, true).start();
							break;
						case 1:
							new SaveFilTask(adjunctPath).execute();
							break;
						}
					}
				});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	class SaveFilTask extends AsyncTask<String, Integer, MCResult> {
		private String adjunctPath;

		public SaveFilTask(String adjunctPath) {
			this.adjunctPath = adjunctPath;
			spdDialog.showProgressDialog("正在处理中...");
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APINoteRequestServers.saveToAppFile(
						NoteDetailsActivity.this, adjunctPath);
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
				if (1 == mcResult.getResultCode()) {
					// {"STATUS":1,"FILEID":275294,"ADJUNCTID":752001}
					int STATUS = 0;
					try {
						JSONObject json = new JSONObject(mcResult.getResult()
								.toString());
						STATUS = json.getInt("STATUS");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (STATUS == 1) {
						showTip("转存成功");
					} else if (STATUS == 3) {
						showTip("附件不存在");
					} else {
						showTip("转存失败");
					}
				} else {
					showTip("转存失败");
				}
			}
		}
	}

	/**
	 * url跳转
	 * 
	 * @param context
	 * @param url
	 */
	public static void showUrlRedirect(Context context, String url) {
		openBrowser(context, url);
	}

	/**
	 * 打开浏览器
	 * 
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			// ToastMessage(context, "无法浏览此网页", 500);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		// menu.findItem(R.id.action_more).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (refresh)
				setResult(RESULT_OK, new Intent());
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (refresh)
				setResult(RESULT_OK, new Intent());
		}
		return super.onKeyDown(keyCode, event);
	}

	private void deleteNote() {
		new AlertDialog.Builder(NoteDetailsActivity.this).setTitle("提示")
				.setMessage("您确定要删除此笔记吗？删除后将无法恢复。")
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new DeleteNoteTask().execute();
					}
				}).setNegativeButton("取消", null).show();
	}

	class DiaryInfoTask extends AsyncTask<String, Integer, MCResult> {
		private String diaryId;

		public DiaryInfoTask(String diaryId) {
			this.diaryId = diaryId;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				// result = APINoteRequestServers.diaryInfo(
				// NoteDetailsActivity.this, diaryId);
				result = APINoteRequestServers.noteInfo(
						NoteDetailsActivity.this, diaryId);
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
				pro_bar.setVisibility(View.GONE);

				if (bean != null) {
				} else {
					pro_tv.setText("加载失败，点击重新加载…");
				}
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
					pro_bar.setVisibility(View.GONE);
					pro_tv.setText("加载失败，点击重新加载…");
				} else {
					// bean = (DiaryInfoBean) mcResult.getResult();
					bean = ((MapNoteInfoBean) mcResult.getResult())
							.getNOTEINFOBEAN();
					if (bean != null) {
						updateMsg(false);
					} else {
						showTip("此笔记已被删除");
						finish();
					}
				}
			}
		}
	}

	class MoveBookTask extends AsyncTask<String, Integer, MCResult> {
		private String notebookId;

		public MoveBookTask(String notebookId) {
			this.notebookId = notebookId;
			spdDialog.showProgressDialog("正在处理中...");
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APINoteRequestServers.moveNoteToOtherNoteBook(
						NoteDetailsActivity.this, bean.getNoteId() + "",
						notebookId);
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
				if (1 == mcResult.getResultCode()) {
					if ("1".equals(mcResult.getResult().toString())) {
						showTip("移动成功");
						try {
							bean.setNotebookId(Integer.valueOf(notebookId));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						showTip("移动失败");
					}
				} else {
					showTip("移动失败");
				}
			}
		}
	}

	class DeleteNoteTask extends AsyncTask<String, Integer, MCResult> {

		public DeleteNoteTask() {
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APINoteRequestServers.deleteCloudNote(
						NoteDetailsActivity.this, noteId);
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
					showTip("已删除！");
					NoteActivity.needRefresh = true;
					NoteDetailsActivity.this.finish();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case FriendsChooserActivity.RESCODE:
			shareFriendIds = data.getStringArrayExtra("ids");
			if (null == shareFriendIds || 0 == shareFriendIds.length) {
				showTip("您没有选择任何朋友！");
			} else if (flag_friendSharing) {
				showTip("正在分享！");
			} else {
				new ShareFriendTask().execute();
			}
			break;

		case GroupsChooserActivity.RESCODE:
			shareGroupIds = data.getStringArrayExtra("ids");
			if (null == shareGroupIds || 0 == shareGroupIds.length) {
				// showTip("您没有选择任何圈子！");
			} else if (flag_groupSharing) {
				showTip("正在分享！");
			} else {
				new ShareGroupTask().execute();
			}
			break;
		case 10:
			refresh = true;
			new DiaryInfoTask(noteId).execute();
			break;
		default:
			break;
		}
	}

	class ShareFriendTask extends AsyncTask<Void, Integer, MCResult> {
		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			flag_friendSharing = true;
			try {
				mc = APINoteRequestServers.shareDiary(NoteDetailsActivity.this,
						noteId, shareFriendIds);
			} catch (Exception e) {

				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || 1 != result.getResultCode()) {

				new AlertDialog.Builder(NoteDetailsActivity.this)
						.setTitle("分享失败!")
						.setPositiveButton("重新分享", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new ShareFriendTask().execute();
							}
						}).setNegativeButton("取消", null).show();
			} else {
				showTip("已分享！");
				shareFriendIds = null;
			}
			flag_friendSharing = false;
		}

	}

	class ShareGroupTask extends AsyncTask<Void, Integer, MCResult> {
		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			flag_groupSharing = true;
			try {
				mc = APINoteRequestServers.shareDiaryToGroup(
						NoteDetailsActivity.this, noteId, shareGroupIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || 1 != result.getResultCode()) {
				new AlertDialog.Builder(NoteDetailsActivity.this)
						.setTitle("分享失败!")
						.setPositiveButton("重新分享", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new ShareGroupTask().execute();
							}
						}).setNegativeButton("取消", null).show();
			} else {
				InfoWallActivity.isNeedRefresh = true;
				showTip("已分享！");
				shareGroupIds = null;
			}
			flag_groupSharing = false;
		}

	}

}

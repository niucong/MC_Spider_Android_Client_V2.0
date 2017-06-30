package com.datacomo.mc.spider.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.fragmt.GroupOrMemberSpan;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.FileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapFileShareLeaguerBean;
import com.datacomo.mc.spider.android.service.DownLoadCloudFileThread;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileDetailActivity extends BasicActionBarActivity {
	public static final String TAG = "FileDetailActivity";

	private FileInfoBean fileInfoBean;
	private LinearLayout linearLayout2, linearLayout3;
	private ImageView fileImg;
	private TextView fileName, fileSize, fileDesc, fileTag, fileFr, fileRemark,
			uploadTime, fileFrPeople;
	private ImageView new_pic_1, new_pic_2, new_pic_3, new_pic_4, new_pic_5;
	private ImageView newPic_image;
	private TextView tvfileOpen, tvfileFr, tvfileRename, tvfileDelete;

	private ArrayList<ImageView> new_pic = new ArrayList<ImageView>();
	private ArrayList<String> visitor_head = new ArrayList<String>();
	private ArrayList<String> visitor_memberId = new ArrayList<String>();

	private String[] shareFriendIds;
	private String[] shareGroupIds;
	private int shareNum;
	int fileId, position;

	private boolean flag_friendSharing;
	private boolean flag_groupSharing;
	private DeleteFileStateTask deletefileStateTask;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "16");
		L.d(TAG, "onStart");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.file_detail_activity);
		ab.setTitle("文件信息");
		init();
	}

	private void init() {
		getIntentMsg();
		linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
		linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
		fileImg = (ImageView) findViewById(R.id.fileImg);
		fileName = (TextView) findViewById(R.id.fileName);
		fileSize = (TextView) findViewById(R.id.fileSize);
		fileDesc = (TextView) findViewById(R.id.fileDesc);
		fileTag = (TextView) findViewById(R.id.fileTag);
		fileFr = (TextView) findViewById(R.id.fileFr);
		fileRemark = (TextView) findViewById(R.id.fileRemark);
		uploadTime = (TextView) findViewById(R.id.uploadTime);
		fileFrPeople = (TextView) findViewById(R.id.fileFrPeople);

		new_pic_1 = (ImageView) findViewById(R.id.new_pic_1);
		new_pic_2 = (ImageView) findViewById(R.id.new_pic_2);
		new_pic_3 = (ImageView) findViewById(R.id.new_pic_3);
		new_pic_4 = (ImageView) findViewById(R.id.new_pic_4);
		new_pic_5 = (ImageView) findViewById(R.id.new_pic_5);

		newPic_image = (ImageView) findViewById(R.id.newPic_image);

		fileName.setOnClickListener(this);

		tvfileOpen = (TextView) findViewById(R.id.bottomfileOpen);
		tvfileFr = (TextView) findViewById(R.id.bottomfileFr);
		tvfileRename = (TextView) findViewById(R.id.bottomfileRename);
		tvfileDelete = (TextView) findViewById(R.id.bottomfileDelete);

		tvfileOpen.setCompoundDrawables(null,
				getResDrawable(R.drawable.file_open), null, null);
		tvfileFr.setCompoundDrawables(null, getResDrawable(R.drawable.file_share),
				null, null);
		tvfileRename.setCompoundDrawables(null,
				getResDrawable(R.drawable.file_rename), null, null);
		tvfileDelete.setCompoundDrawables(null,
				getResDrawable(R.drawable.file_delete), null, null);

		tvfileOpen.setOnClickListener(this);
		tvfileFr.setOnClickListener(this);
		tvfileRename.setOnClickListener(this);
		tvfileDelete.setOnClickListener(this);

		new_pic.add(new_pic_1);
		new_pic.add(new_pic_2);
		new_pic.add(new_pic_3);
		new_pic.add(new_pic_4);
		new_pic.add(new_pic_5);

		new_pic_1.setOnClickListener(this);
		new_pic_2.setOnClickListener(this);
		new_pic_3.setOnClickListener(this);
		new_pic_4.setOnClickListener(this);
		new_pic_5.setOnClickListener(this);

		linearLayout3.setOnClickListener(this);
		newPic_image.setOnClickListener(this);

		setMessage();
	}

	@SuppressLint("Override")
	private Drawable getResDrawable(int id) {
		Drawable drawable = getResources().getDrawable(id);
		int dw = drawable.getIntrinsicWidth();
		int dh = drawable.getIntrinsicHeight();
		drawable.setBounds(0, 0, 1 * dw / 2, 1 * dh / 2);
		return drawable;
	}

	private void getIntentMsg() {
		Bundle b = getIntent().getExtras();
		fileInfoBean = (FileInfoBean) b.getSerializable("fileInfoBean");
		position = b.getInt("position", -1);
	}

	private void setMessage() {
		String filename = fileInfoBean.getFileName() + "."
				+ fileInfoBean.getFormatName();
		fileImg.setImageResource(FileUtil.getFileIcon(filename));
		fileName.setText(filename);
		// fileSize.setText(FileUtil.computeFileSize(fileInfoBean.getFileSize()));
		if (fileInfoBean.getFileDesc() != null) {
			fileDesc.setVisibility(View.VISIBLE);
			fileDesc.setText(fileInfoBean.getFileDesc());
		} else {
			fileDesc.setVisibility(View.GONE);
		}
		String tag = "";
		String tags[] = fileInfoBean.getFileTags();
		if (tags != null) {
			fileTag.setVisibility(View.GONE);
			int j = tags.length;
			for (int i = 0; i < j; i++) {
				if (i == (j - 1)) {
					tag += tags[i];
				} else {
					tag += tags[i] + ",";
				}
			}
			fileTag.setText("标签：  " + tag);
		} else {
			fileTag.setVisibility(View.VISIBLE);
		}

		int memberId = fileInfoBean.getMemberId();
		int shareMemberId = fileInfoBean.getShareMemberId();
		int fileOwnerId = fileInfoBean.getFileOwnerId();
		if (shareMemberId == 0) {
			fileSize.setText(FileUtil.computeFileSize(fileInfoBean
					.getFileSize())
					+ "   "
					+ DateTimeUtil.aTimeFormat(DateTimeUtil
							.getLongTime(fileInfoBean.getUploadTime())));
			linearLayout2.setVisibility(View.GONE);
		} else {
			fileSize.setText(FileUtil.computeFileSize(fileInfoBean
					.getFileSize()));
			linearLayout2.setVisibility(View.VISIBLE);
			String str1 = "Fr：";
			String str2 = fileInfoBean.getShareMemberName();
			int j1 = str1.length();
			int j2 = j1 + str2.length();
			SpannableStringBuilder ssb = new SpannableStringBuilder();
			ssb.append(str1 + str2);
			ssb.setSpan(
					new GroupOrMemberSpan(this, "MemberId#"
							+ fileInfoBean.getShareMemberId()), j1, j2,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			if (fileOwnerId == memberId) {
				// fileFr.setText("Fr：" + fileInfoBean.getShareMemberName());
			} else {
				// fileFr.setText("Fr：" + fileInfoBean.getShareMemberName()
				// + "（圈子：" + fileInfoBean.getFileOwnerName() + "）");
				String str3 = "（圈子：";
				String str4 = fileInfoBean.getFileOwnerName();
				String str5 = "）";
				int j3 = j2 + str3.length();
				int j4 = j3 + str4.length();
				ssb.append(str3 + str4 + str5);
				ssb.setSpan(new GroupOrMemberSpan(this, "GroupId#"
						+ fileInfoBean.getFileOwnerId()), j3, j4,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			fileFr.setText(ssb);
			fileFr.setMovementMethod(LinkMovementMethod.getInstance());
		}
		if (fileInfoBean.getFileRemark() == null) {
			fileRemark.setVisibility(View.GONE);
		} else {
			fileRemark.setVisibility(View.VISIBLE);
			fileRemark.setBackgroundResource(R.drawable.file_descrise);
			fileRemark.setText(fileInfoBean.getFileRemark());
			// fileRemark.getBackground().setAlpha(100);
		}

		uploadTime.setText("分享时间： "
				+ DateTimeUtil.aTimeFormat(DateTimeUtil
						.getLongTime(fileInfoBean.getUploadTime())));
		shareNum = fileInfoBean.getShareMemberNum();
		if (shareNum != 0) {
			linearLayout3.setVisibility(View.VISIBLE);
			fileFrPeople.setText("您向" + shareNum + "人分享过：");
			fileId = fileInfoBean.getFileId();
			new LoadInfoTask(FileDetailActivity.this, fileId + "").execute();
		} else {
			linearLayout3.setVisibility(View.GONE);
		}
	}

	/**
	 * 按成员id跳转界面
	 * 
	 * @param id_Member
	 */
	private void startActivityByMemberId(String id_Member) {
		Bundle b = new Bundle();
		b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
		b.putString("id", id_Member);
		b.putString("name", fileInfoBean.getFileOwnerName());
		LogicUtil.enter(this, HomePgActivity.class, b, false);
	}

	/**
	 * 对图片进行异步加载显示
	 */
	private void setImageInfo(ArrayList<ImageView> pic_Views,
			ArrayList<String> pic_Url) {
		int j = pic_Url.size() - 1;
		if (j >= 4) {
			j = 4;
		}
		for (int i = 0; i <= j; i++) {
			// Drawable vis_drawable = PublicLoadPicture.loadHead(
			// FileDetailActivity.this, pic_Url.get(i), pic_Views.get(i),
			// "filedetailactivity");
			// pic_Views.get(i).setImageDrawable(vis_drawable);
			// TODO
			MyFinalBitmap.setHeader(this, pic_Views.get(i), pic_Url.get(i));
		}
	}

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private Context context;
		private String Id;

		public LoadInfoTask(Context context, String Id) {
			this.context = context;
			this.Id = Id;
		}

		@Override
		protected MCResult doInBackground(String... arg0) {
			MCResult mcResult = null;
			try {
				mcResult = APIFileRequestServers.fileShareMembers(context, Id,
						0, 5, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (result == null) {

			} else {
				MapFileShareLeaguerBean mapBean = (MapFileShareLeaguerBean) result
						.getResult();
				if (mapBean == null)
					return;
				List<FileShareLeaguerBean> share_leaguers = mapBean
						.getSHARE_LEAGUERS();
				int size = mapBean.getSHARE_LEAGUERS_NUM();
				L.d(TAG, "LoadInfoTask size=" + size + ",shareNum=" + shareNum);
				if (size > 0 && size != shareNum) {
					shareNum = size;
					linearLayout3.setVisibility(View.VISIBLE);

					fileFrPeople.setText("您向" + size + "人分享过：");
					new LoadInfoTask(FileDetailActivity.this,
							fileInfoBean.getFileId() + "").execute();
				} else {
					if (share_leaguers != null) {
						visitor_memberId.clear();
						visitor_head.clear();
						for (int i = 0; i < share_leaguers.size(); i++) {
							FileShareLeaguerBean fileShareLeaguerBean = share_leaguers
									.get(i);
							String info = fileShareLeaguerBean
									.getRelationMemberHeadUrl()
									+ fileShareLeaguerBean
											.getRelationMemberHeadPath();
							info = ThumbnailImageUrl.getThumbnailHeadUrl(info,
									HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
							visitor_memberId.add(fileShareLeaguerBean
									.getRelationMemberId() + "");
							visitor_head.add(info);
						}
						setImageInfo(new_pic, visitor_head);
					}
				}
			}
		}

	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// LogicUtil.enter(this, InfoWallActivity.class, null, true);
	// }

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
				spdDialog.showProgressDialog("正在处理中...");
				new ShareFriendTask().execute();
			}
			break;

		case GroupsChooserActivity.RESCODE:
			shareGroupIds = data.getStringArrayExtra("ids");
			if (null == shareGroupIds || 0 == shareGroupIds.length) {
				showTip("您没有选择任何圈子！");
			} else if (flag_groupSharing) {
				showTip("正在分享！");
			} else {
				spdDialog.showProgressDialog("正在处理中...");
				new ShareGroupTask().execute();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bottomfileOpen:
		case R.id.fileName:
			openFile();
			break;
		case R.id.bottomfileFr:
			shareFile();
			break;
		case R.id.bottomfileRename:
			editFile();
			break;
		case R.id.bottomfileDelete:
			deleteFile();
			break;
		case R.id.linearLayout3:
		case R.id.newPic_image:
			if (shareNum > 0) {
				Bundle b = new Bundle();
				b.putSerializable(BundleKey.TYPE_REQUEST, Type.FILESHARE);
				b.putString(BundleKey.ID_FILEMEMBERS, String.valueOf(fileId));
				LogicUtil.enter(FileDetailActivity.this,
						MemberListActivity.class, b, false);
			}
			break;
		case R.id.new_pic_1:
			if (visitor_memberId != null && visitor_memberId.size() > 0) {
				startActivityByMemberId(visitor_memberId.get(0));
			}
			break;
		case R.id.new_pic_2:
			if (visitor_memberId != null && visitor_memberId.size() > 1) {
				startActivityByMemberId(visitor_memberId.get(1));
			}
			break;
		case R.id.new_pic_3:
			if (visitor_memberId != null && visitor_memberId.size() > 2) {
				startActivityByMemberId(visitor_memberId.get(2));
			}
			break;
		case R.id.new_pic_4:
			if (visitor_memberId != null && visitor_memberId.size() > 3) {
				startActivityByMemberId(visitor_memberId.get(3));
			}
			break;
		case R.id.new_pic_5:
			if (visitor_memberId != null && visitor_memberId.size() > 4) {
				startActivityByMemberId(visitor_memberId.get(4));
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			exit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 0：无变化，1：改名字，2：删除
	 */
	private int isChange = 0;

	private void exit() {
		if (isChange != 0) {
			Intent i = new Intent();
			i.putExtra("position", position);
			i.putExtra("isChange", isChange);
			i.putExtra("fileInfoBean", fileInfoBean);
			setResult(RESULT_OK, i);
		}
		finish();
	}

	/**
	 * 删除文件
	 */
	private void deleteFile() {
		new AlertDialog.Builder(this)
				.setTitle("删除文件")
				.setMessage(
						"确定要删除 " + fileInfoBean.getFileName() + "."
								+ fileInfoBean.getFormatName() + " 吗？")
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						stopTask();
						deletefileStateTask = new DeleteFileStateTask(
								fileInfoBean.getFileId());
						deletefileStateTask.execute();
					}
				}).setNegativeButton("取消", null).show();
	}

	private void stopTask() {
		if (null != deletefileStateTask
				&& deletefileStateTask.getStatus() == AsyncTask.Status.RUNNING) {
			deletefileStateTask.cancel(true);
		}
	}

	/**
	 * 编辑文件
	 */
	@SuppressLint("HandlerLeak")
	private void editFile() {
		View view = LayoutInflater.from(this).inflate(R.layout.edit_file, null);
		final EditText editText = (EditText) view
				.findViewById(R.id.editfile_name);
		TextView textView = (TextView) view.findViewById(R.id.edit_fromat);

		String mName = fileInfoBean.getFileName();
		int id = fileInfoBean.getFileId();

		editText.setText(mName);
		try {
			if (mName != null && !"".equals(mName))
				editText.setSelection(mName.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		editText.setHint("请输入文件名");
		textView.setVisibility(View.VISIBLE);
		textView.setText("." + fileInfoBean.getFormatName());

		final String name = mName;
		final int objectId = id;

		final Handler renameHandler = new Handler() {
			public void handleMessage(Message msg) {
				spdDialog.cancelProgressDialog(null);
				switch (msg.what) {
				case 0:
					showTip(T.ErrStr);
					break;
				case 1:
					// showTip("修改成功");
					String reName = (String) msg.obj;
					fileName.setText(reName + "."
							+ fileInfoBean.getFormatName());
					fileInfoBean.setFileName(reName);
					isChange = 1;
					break;
				default:
					break;
				}
			};
		};

		new AlertDialog.Builder(this).setTitle("重命名").setView(view)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BaseData.hideKeyViewBoard(FileDetailActivity.this,
								editText);

						final String reName = editText.getEditableText()
								.toString();
						if (reName != null && !reName.equals("")) {
							if (reName.equals(name)) {
								return;
							}
							spdDialog.showProgressDialog("正在处理中...");
							new Thread() {
								public void run() {
									Message msg = new Message();
									try {
										MCResult mcResult = APIFileRequestServers
												.editFile(
														FileDetailActivity.this,
														objectId, reName, null,
														null);
										if (mcResult != null
												&& mcResult.getResultCode() == 1) {
											msg.what = 1;
											msg.obj = reName;
										} else {
											msg.what = 0;
										}
									} catch (Exception e) {
										msg.what = 0;
										e.printStackTrace();
									}
									renameHandler.sendMessage(msg);
								};
							}.start();
						} else {
							showTip("名字不能为空");
						}
					}
				}).setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BaseData.hideKeyViewBoard(FileDetailActivity.this,
								editText);
					}
				}).show();
	}

	/**
	 * 分享文件
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shareFile() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"分享")
		// .setItems(new String[] { "分享到朋友", "分享到交流圈" },
		// new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(this, R.layout.choice_item,
								new String[] { "分享到朋友", "分享到交流圈" }),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:// 分享到朋友
									Intent intent = new Intent(
											FileDetailActivity.this,
											FriendsChooserActivity.class);
									intent.putExtra("type", 3);
									startActivityForResult(intent,
											FriendsChooserActivity.RESCODE);
									break;
								case 1:// 分享到交流圈
									Bundle b = new Bundle();
									b.putString("btnString", "分享");
									LogicUtil.enter(FileDetailActivity.this,
											GroupsChooserActivity.class, b,
											GroupsChooserActivity.RESCODE);
									break;
								}
							}
						});// .setNegativeButton("取消", null).show();
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * 下载
	 */
	@SuppressLint("HandlerLeak")
	private void openFile() {
		String filePath = fileInfoBean.getFilePath();
		final String tempName = filePath
				.substring(filePath.lastIndexOf("/") + 1);
		File myFile = new File(ConstantUtil.CLOUD_PATH + tempName);
		if (myFile != null && myFile.exists()) {
			new FileUtil().openFile(this, myFile);
			return;
		} else if (ConstantUtil.downloadingList.contains(tempName)) {
			showTip("该文件正在下载中…");
			return;
		} else {
			spdDialog.showProgressDialog("正在处理中...");
			final Handler openHandler = new Handler() {
				public void handleMessage(Message msg) {
					spdDialog.cancelProgressDialog(null);
					switch (msg.what) {
					case 0:
						showTip(T.ErrStr);
						break;
					case 1:
						break;
					default:
						break;
					}
				};
			};
			ConstantUtil.downloadingList.add(tempName);
			new Thread() {
				public void run() {
					try {
						MCResult mcResult = APIFileRequestServers.getFilePath(
								FileDetailActivity.this,
								fileInfoBean.getFileId());
						if (mcResult != null && mcResult.getResultCode() == 1) {
							String fileUrl = mcResult.getResult().toString();
							new DownLoadCloudFileThread(
									FileDetailActivity.this, fileUrl,
									fileInfoBean.getFileSize(),
									fileInfoBean.getFileName() + "."
											+ fileInfoBean.getFormatName(),
									true, 0).start();
							openHandler.sendEmptyMessage(1);
						} else {
							ConstantUtil.downloadingList.remove(tempName);
							openHandler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						ConstantUtil.downloadingList.remove(tempName);
						openHandler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	public class DeleteFileStateTask extends
			AsyncTask<String, Integer, MCResult> {

		private int fileId;

		public DeleteFileStateTask(int fileId) {
			this.fileId = fileId;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIFileRequestServers.deleteFile(
						FileDetailActivity.this, fileId);
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
					isChange = 2;
					exit();
				}
			}
		}
	}

	class ShareFriendTask extends AsyncTask<Void, Integer, MCResult> {
		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			flag_friendSharing = true;
			try {
				mc = APIFileRequestServers.shareFile(FileDetailActivity.this,
						fileInfoBean.getFileId(), shareFriendIds, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || 1 != result.getResultCode()) {
				new AlertDialog.Builder(FileDetailActivity.this)
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
				CloudFileActivity.isNeedRefresh = true;
				new LoadInfoTask(FileDetailActivity.this,
						fileInfoBean.getFileId() + "").execute();
			}
			flag_friendSharing = false;
			spdDialog.cancelProgressDialog(null);
		}

	}

	class ShareGroupTask extends AsyncTask<Void, Integer, MCResult> {
		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			flag_groupSharing = true;
			try {
				mc = APIFileRequestServers.shareFileToGroup(
						FileDetailActivity.this, fileInfoBean.getFileId(),
						shareGroupIds, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || 1 != result.getResultCode()) {
				new AlertDialog.Builder(FileDetailActivity.this)
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
			spdDialog.cancelProgressDialog(null);
		}

	}
}

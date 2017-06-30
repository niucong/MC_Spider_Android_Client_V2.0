package com.datacomo.mc.spider.android;

import java.io.File;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.service.UpdateGroupListThread;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.CheckNameUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.T;

public class CircleEditorActivity extends BasicActionBarActivity implements
		OnClickListener {
	private static final int FROM_GALLERY = 0;
	private static final int FROM_CAMERA = 1;
	private static final int FROM_CROP = 2;

	private String headimg_file = ConstantUtil.HEAD_PATH;
	private String headimg_name = "headimg.jpg";
	private Bitmap headimgbit;
	private File headPic;

	private EditText circle_name, circle_introduction, circle_label;
	private ImageView circle_posters;
	private String name, introduction, label, groupPostTh;
	private String groupId;
	private String imgPath;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.circle_editor);
		initView();
	}

	private void initView() {
		circle_name = (EditText) findViewById(R.id.circle_editor_name);
		circle_introduction = (EditText) findViewById(R.id.circle_editor_introduction);
		circle_label = (EditText) findViewById(R.id.circle_editor_label);
		circle_posters = (ImageView) findViewById(R.id.circle_posters);
		circle_posters.setOnClickListener(this);
		getIntentMsg();
		setImageInfo();
		circle_name.setText(name);
		if (name != null && !"".equals(name)) {
			circle_name.setSelection(name.length());
		}
		circle_introduction.setText(introduction);
		circle_label.setText(label);
		// setTitle(name, R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle(name);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理拍照返回数据
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			File saveFile = null;
			try {
				switch (requestCode) {
				case FROM_CAMERA:
					saveFile = new File(headimg_file);
					File picture = new File(saveFile, headimg_name);
					uri = Uri.fromFile(picture);
					startPhotoZoom(uri);
					break;
				case FROM_GALLERY:
					uri = data.getData();
					startPhotoZoom(uri);
					break;
				case FROM_CROP:
					new AlertDialog.Builder(CircleEditorActivity.this)
							.setTitle(
									getResources()
											.getString(
													R.string.circle_uploadimgdialog_title))
							.setMessage(
									getResources()
											.getString(
													R.string.circle_uploadimgdialog_confirmupload))
							.setPositiveButton(
									getResources().getString(
											R.string.alertdialog_confirm),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											File saveFile = new File(
													headimg_file);
											headPic = new File(saveFile,
													headimg_name);
											getHeadFilePath(Uri
													.fromFile(headPic));
											circle_posters
													.setImageBitmap(ImageDealUtil
															.toRoundCorner(
																	headimgbit,
																	DensityUtil
																			.dip2px(CircleEditorActivity.this,
																					circle_posters
																							.getHeight())));
											// circle_posters
											// .setImageBitmap(headimgbit);
										}
									})
							.setNegativeButton(
									getResources().getString(
											R.string.alertdialog_cancel), null)
							.show();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 显示修改头像对话框
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showUpHeadDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("修改海报");
		String[] dialogMsg = new String[] {
				getResources().getString(R.string.selectImgdialog_fromlocality),
				getResources().getString(R.string.selectImgdialog_fromcamera)
		// , getResources().getString(R.string.selectImgdialog_cancel)
		};
		builder
		// .setItems(dialogMsg, new DialogInterface.OnClickListener() {
		.setAdapter(new ArrayAdapter(this, R.layout.choice_item, dialogMsg),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(
									Intent.createChooser(intent, "选择图片"),
									FROM_GALLERY);
							break;
						case 1:
							Intent intent1 = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							File saveFile = new File(headimg_file);
							if (saveFile.exists()) {
								// Log.d(TAG,"目录已存在");
							} else {
								saveFile.mkdirs();
							}
							intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
									.fromFile(new File(saveFile, headimg_name)));
							startActivityForResult(intent1, FROM_CAMERA);
							break;
						case 2:
							dialog.dismiss();
							break;
						}
					}
				});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	private void editorCircleInfo() {
		name = circle_name.getText().toString();
		if (!CheckNameUtil.checkGroupName(name)) {
			showTip("请输入规范的名字！");
			return;
		}
		introduction = circle_introduction.getText().toString();
		label = circle_label.getText().toString();

		if (headPic != null) {
			if (headPic.exists()) {
				uploadHead(headPic);
			}
		}
		spdDialog.showProgressDialog("正在处理中...");
		new loadInfoTask().execute();
	}

	class loadInfoTask extends AsyncTask<String, Integer, MCResult> {

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				if (label == null || "".equals(label))
					label = " ";
				mcResult = APIRequestServers.editGroupBasicInfo(
						CircleEditorActivity.this, groupId, name, true,
						introduction, true, label, true, null, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (result == null || result.getResultCode() != 1) {
				showTip(T.ErrStr);
			} else {
				int EDIT_RESULT = 0;
				try {
					EDIT_RESULT = new JSONObject(result.getResult().toString())
							.getInt("EDIT_RESULT");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (EDIT_RESULT == 1) {
					// showTip("圈子信息编辑成功！");
					Bundle b = new Bundle();
					b.putString("name", name);
					b.putString("description", introduction);
					SimpleReceiver.sendBoardcast(CircleEditorActivity.this,
							SimpleReceiver.RECEIVER_DATA_CHANGED, b);
					finish();

					new Thread() {
						public void run() {
							try {
								UpdateGroupListThread.updateGroupList(
										CircleEditorActivity.this, null);
							} catch (Exception e) {
								e.printStackTrace();
							}
						};
					}.start();
				} else {
					showTip(T.ErrStr);
				}
			}
		}

	}

	/**
	 * 获取intent
	 * 
	 * @return String
	 **/
	private void getIntentMsg() {
		Bundle b = getIntent().getExtras();
		groupId = b.getString("id");
		name = b.getString("name");
		introduction = b.getString("introduction");
		label = b.getString("label");
		groupPostTh = b.getString("grouPost");
	}

	/**
	 * 对界面中的图片进行加载
	 */
	private void setImageInfo() {
		MyFinalBitmap.setPosterCorner(this, circle_posters, groupPostTh);
	}

	/**
	 * 获取返回图片的路径
	 * 
	 * @param uri
	 */
	private void getHeadFilePath(Uri uri) {
		String sUri = uri.toString();
		// 如果是从系统gallery取图片，返回一个contentprovider的uri
		if (sUri.startsWith("content://")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				imgPath = cursor.getString(1); // 图片文件路径
			} else {

			}
		} else if (sUri.startsWith("file://")
				&& (sUri.endsWith(".png") || sUri.endsWith(".jpg") || sUri
						.endsWith(".gif"))) {
			// 如果从某些第三方软件中选取文件，返回的是一个文件具体路径。
			imgPath = sUri.replace("file://", "");
		} else if (sUri.startsWith(ConstantUtil.SDCARD_PATH)) {
			// 直接获取临时图片地址
			imgPath = sUri;
		} else {
			// 返回的uri不合法或者文件不是图片。

		}
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
		headimgbit = bitmap;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 *            图片路径
	 **/
	public void startPhotoZoom(Uri uri) {
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType(uri, "image/*");// 设置要裁剪的图片
		cropIntent.putExtra("crop", "true");// crop=true 裁剪页面.
		// 设置其他信息：
		cropIntent.putExtra("aspectX", 1);// 设置裁剪框的比例.
		cropIntent.putExtra("aspectY", 1);// x:y=1:1
		// outputX outputY 是裁剪图片宽高
		// cropIntent.putExtra("outputX", 120);
		// cropIntent.putExtra("outputY", 120);
		cropIntent.putExtra("return-data", "false");
		File saveFile = new File(headimg_file);
		File picture = new File(saveFile, headimg_name);
		cropIntent.putExtra("output", Uri.fromFile(picture));// 保存输出文件
		cropIntent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(cropIntent, FROM_CROP);
	}

	/**
	 * 图片路径并上传
	 * 
	 * @param uriString
	 **/
	private void uploadHead(final File file) {
		if (file != null && file.exists())
			APIRequestServers.uploadFile(CircleEditorActivity.this, file,
					UploadMethodEnum.UPLOADPOSTER, groupId);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.circle_posters:
			showUpHeadDialog();
			break;
		case R.id.circle_editor_name:
			break;
		case R.id.circle_editor_introduction:
			break;
		case R.id.circle_editor_label:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_send).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_send:
			editorCircleInfo();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// editorCircleInfo();
	// }

}

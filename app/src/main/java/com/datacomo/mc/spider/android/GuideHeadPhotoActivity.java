package com.datacomo.mc.spider.android;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.SocketUploadFile;
import com.datacomo.mc.spider.android.params.UploadFileParams;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FormFile;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class GuideHeadPhotoActivity extends BasicActionBarActivity {
	private static final String TAG = "GuideHeadPhotoActivity";

	// private ImageView ;
	private TextView next_tv, currency_tv;
	private LinearLayout save_tv;
	private ImageView head_iv;

	private MyHanlder mHandler = new MyHanlder();

	private String pictureName = null;
	private static final int FROM_GALLERY = 0;
	private static final int FROM_CAMERA = 1;
	private static final int FROM_CROP = 2;

	private ProgressBar pBar;
	private String method = "uploadHead";
	private String temporaryPath = ConstantUtil.UPLOAD_HEAD_PATH; // 裁剪过后的临时文件
	public static final String DIALOG_HIDE = "com.datacomo.mc.spider.android.guideheadphotoactivity";

	private String imgPath = null;
	private String imgName = null;

	public static GuideHeadPhotoActivity guideHeadPhotoActivity;

	private int num = 16;
	private final String currency_str = "*完成头像设置，首次修改您还将获赠%d个圈币。";

	private boolean isRehead = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.guide_headphoto);
		guideHeadPhotoActivity = this;

		findView();
		setView();
	}

	private void findView() {
		currency_tv = (TextView) findViewById(R.id.guide_headphoto_currency);

		head_iv = (ImageView) findViewById(R.id.guide_headphoto_icon);
		save_tv = (LinearLayout) findViewById(R.id.guide_headphoto_setting);

		next_tv = (TextView) findViewById(R.id.guide_headphoto_next);
		pBar = (ProgressBar) findViewById(R.id.main_progressBar);
	}

	private void setView() {
		// setTitle("设置头像", R.drawable.title_fanhui, null);
		ab.setTitle("设置头像");

		int nums = App.app.share.getIntMessage("program", "MODIFY_MEMBER_HEAD", 0);
		if (nums != 0) {
			num = nums;
		}
		currency_tv.setText(String.format(currency_str, num));

		if (URLProperties.HEAD_PATH != null) {
			// new ImageTask(this, ThumbnailImageUrl.getThumbnailHeadUrl(
			// URLProperties.HEAD_PATH, HeadSizeEnum.EIGHTY),
			// R.drawable.defaultimg, true, 3).execute(head_iv);
			// Drawable drawable = new ImageDownLoadTask(this,
			// TaskUtil.HEADDEFAULTLOADSTATEIMG, ConstantUtil.HEAD_PATH)
			// .start(ThumbnailImageUrl.getThumbnailHeadUrl(
			// URLProperties.HEAD_PATH, HeadSizeEnum.ONE_HUNDRED_AND_TWENTY),
			// null, new ImageCallback() {
			//
			// @Override
			// public void load(Object object, Object[] tags) {
			// head_iv.setImageDrawable(ImageDealUtil
			// .getPosterCorner((Drawable) object,
			// 4));
			// }
			// });
			// head_iv.setImageDrawable(ImageDealUtil.getPosterCorner(drawable,
			// 4));
			// TODO
			MyFinalBitmap.setHeader(this, head_iv, ThumbnailImageUrl
					.getThumbnailHeadUrl(URLProperties.HEAD_PATH,
							HeadSizeEnum.ONE_HUNDRED_AND_TWENTY));
		}

		save_tv.setOnClickListener(this);
		next_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.guide_headphoto_setting:// 选择图片
			uploadPicture(null, null, "上传头像", method);
			break;
		case R.id.guide_headphoto_next:// 跳过
			enterNextActivity(GuideInviteActivity.class);
			break;
		default:
			break;
		}
	}

	/**
	 * 页面跳转
	 */
	private void enterNextActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		this.startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			switch (requestCode) {
			case FROM_CAMERA:
				File saveFile = new File(ConstantUtil.CAMERA_PATH);
				File picture = new File(saveFile, pictureName);
				uri = Uri.fromFile(picture);
				cropPicture(uri);
				break;
			case FROM_GALLERY:
				if (data != null) {
					uri = data.getData();
					cropPicture(uri);
				}
				break;
			case FROM_CROP:
				pBar.setVisibility(View.VISIBLE);
				upload(Uri.parse(temporaryPath));
				break;
			}

		}
	}

	/**
	 * 剪切头像
	 * 
	 * @param uri
	 */
	private void cropPicture(Uri uri) {
		File file = new File(ConstantUtil.UPLOAD_HEAD_PATH);
		if (file.exists()) {
			file.delete();
		}
		Uri fileUri = Uri.fromFile(file);
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType(uri, "image/*");
		cropIntent.putExtra("crop", "true");
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", 240);
		// intent.putExtra("outputY", 240);
		cropIntent.putExtra("return-data", "false");
		cropIntent.putExtra("output", fileUri);// 保存输出文件
		cropIntent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(cropIntent, FROM_CROP);
	}

	/**
	 * 上传图片
	 * 
	 * @param groupId
	 *            ：圈子-传对应Id、个人传null
	 * @param pageUrl
	 * @param prompts
	 *            ：上传头像、上传图片、上传海报
	 * @param method
	 *            ：uploadHead、uploadPhoto、uploadPoster
	 */
	private void uploadPicture(final String groupId, final String pageUrl,
			final String prompts, final String method) {
		L.d(TAG, "uploadPicture groupId=" + groupId + ",pageUrl=" + pageUrl
				+ ",prompts=" + prompts + ",method=" + method);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"上传图片")
		// .setItems(new String[] { "本地上传", "拍照上传"
		// // , "取消上传"
		// }, new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(this, R.layout.choice_item,
								new String[] { "本地上传", "拍照上传" }),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = null;
								switch (which) {
								case 0:
									intent = new Intent(
											Intent.ACTION_GET_CONTENT);
									intent.setType("image/*");
									startActivityForResult(Intent
											.createChooser(intent, "选择图片"),
											FROM_GALLERY);
									break;
								case 1:
									intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									pictureName = System.currentTimeMillis()
											+ ".jpg";
									File saveFile = new File(
											ConstantUtil.CAMERA_PATH);
									if (!saveFile.exists()) {
										saveFile.mkdirs();
									}
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(saveFile,
													pictureName)));
									startActivityForResult(intent, FROM_CAMERA);
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

	/**
	 * 获取返回图片的名字和路径
	 * 
	 * @param uri
	 */
	private void upload(Uri uri) {
		String sUri = uri.toString();
		L.i(TAG, "upload sUri=" + sUri);
		// 如果是从系统gallery取图片，返回一个contentprovider的uri
		if (sUri.startsWith("content://")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				imgPath = cursor.getString(1); // 图片文件路径
				imgName = cursor.getString(3); // 图片文件名
			} else {
				mHandler.sendEmptyMessage(1);
				return;
			}
		} else if (sUri.startsWith("file://")
				&& (sUri.endsWith(".png") || sUri.endsWith(".jpg") || sUri
						.endsWith(".gif"))) {
			// 如果从某些第三方软件中选取文件，返回的是一个文件具体路径。
			imgPath = sUri.replace("file:/", "");
			imgName = new File(uri.toString()).getName();
		} else if (sUri.startsWith(ConstantUtil.SDCARD_PATH)) {
			// 直接获取临时图片地址
			imgPath = sUri;
			imgName = "headimg.jpg";
		} else {
			// 返回的uri不合法或者文件不是图片。
			mHandler.sendEmptyMessage(1);
			return;
		}
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
		head_iv.setImageBitmap(bitmap);
		// TODO
		new UploadPhoto(imgName, imgPath).start();
	}

	/**
	 * 开启线程上传头像
	 * 
	 * @author datacomo-160
	 * 
	 */
	class UploadPhoto extends Thread {
		String imgName;
		String imgPath;

		UploadPhoto(String name, String path) {
			imgName = name;
			imgPath = path;
		}

		@Override
		public void run() {
			uploadPhoto(imgName, imgPath);
		}
	}

	/**
	 * 上传头像
	 * 
	 * @param imgName
	 * @param imgPath
	 */
	private void uploadPhoto(String imgName, String imgPath) {
		Map<String, String> paramsMap = new UploadFileParams(
				GuideHeadPhotoActivity.this, method, imgName, "0")
				.getParamsMap();
		if (paramsMap != null) {
			L.i(TAG, "paramsMap  = " + paramsMap.size());
		} else {
			L.i(TAG, "paramsMap  = null");
		}

		FormFile formFile;
		try {
			formFile = new FormFile(URLEncoder.encode(imgName, "UTF-8"),
					new File(imgPath), "upload", "application/octet-stream");

			if (SocketUploadFile.uploadFile(URLProperties.API_URL + "member",
					paramsMap, formFile, 0, mHandler)) {
				try {
					mHandler.sendEmptyMessage(0);
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				enterNextActivity(GuideInviteActivity.class);
			} else {
				mHandler.sendEmptyMessage(4);
				mHandler.sendEmptyMessage(2);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHanlder extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (pBar != null && pBar.isShown()) {
					pBar.setVisibility(View.GONE);
				}
				if (isRehead) {
					isRehead = false;
					updateUI("头像更新成功，您已获得" + num + "个圈币。");
				} else {
					updateUI("头像更新成功！");
				}
				break;
			case 1:
				updateUI("格式错误，请重新选择");
				break;
			case 2:
				updateUI(T.ErrStr);
				break;
			case 3:
				if (pBar != null && pBar.isShown()) {
					pBar.setProgress((Integer) msg.obj);
				}
				break;
			case 4:
				updateUI("");
				new AlertDialog.Builder(GuideHeadPhotoActivity.this)
						.setTitle("提示").setMessage("上传失败")
						.setPositiveButton("重新上传", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new UploadPhoto(imgName, imgPath).start();
							}
						}).setNegativeButton("取消上传", null).show();
				break;
			default:
				break;
			}

		}
	}

	private void updateUI(String msg) {
		if (pBar != null && pBar.isShown()) {
			pBar.setVisibility(View.GONE);
		}
		if (msg != null && !"".equals(msg)) {
			T.show(this, msg);
		}
	}

	/**
	 * 捕捉键盘事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ConfirmExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 退出确认 */
	public void ConfirmExit() {
		new AlertDialog.Builder(this).setTitle("退出").setMessage("是否退出软件?")
				.setPositiveButton("是", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						if (GuideNameActivity.guideNameActivity != null) {
							LogicUtil
									.finish(((Context) GuideNameActivity.guideNameActivity));
						}
						if (GuideInviteActivity.guideInviteActivity != null) {
							LogicUtil
									.finish(((Context) GuideInviteActivity.guideInviteActivity));
						}
						LogicUtil.finish(GuideHeadPhotoActivity.this);
					}
				}).setNegativeButton("否", null).show();// 显示对话框
	}

	@Override
	protected void onDestroy() {
		L.d(TAG, "onDestroy...");
		File file = new File(temporaryPath);
		if (file.exists()) {
			file.delete();
		}
		URLProperties.HEAD_PATH = null;
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(this, GuideNameActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// startActivity(new Intent(this, GuideNameActivity.class));
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// }
}

package com.datacomo.mc.spider.android;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.fragmt.HomeQboFragment;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberContactBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.CheckNameUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class PiEditorActivity extends BasicActionBarActivity implements
		OnClickListener, OnDateSetListener {
	private final static String TAG = "PiEditorActivity";

	private static final int FROM_GALLERY = 0;
	private static final int FROM_CAMERA = 1;
	private static final int FROM_CROP = 2;

	private String headimg_file = ConstantUtil.HEAD_PATH;
	private String headimg_name = "headimg.jpg";
	private Bitmap headimgbit;
	private File headPic;

	private EditText pi_name, pi_introduction;// , pi_interestSex;
	private TextView pi_phone, pi_birthday, pi_sex;
	private ImageView pi_picHead;

	private String lName, name, birthday, introduction;
	private int sex;
	private String sexInfo;

	private int mYear;
	private int mMonth;
	private int mDay;

	// private String memberBeanId;
	private MemberBean memberBean;
	private MemberBasicBean basicInfo;
	private MemberContactBean contactInfo;

	static final int DATE_DIALOG_ID = 1;

	private Intent intent;
	private Bundle bundle;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		LinearLayout pi_linearLayout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.pi_editor, null);
		layout.addView(pi_linearLayout);
		// setTitle("个人信息", R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle("个人信息");
		init();
		intent = getIntent();
		bundle = intent.getExtras();
		// memberBeanId = bundle.getString("id");
		memberBean = (MemberBean) bundle.getSerializable("memberBean");
		if (memberBean == null)
			finish();
		basicInfo = memberBean.getBasicInfo();
		contactInfo = memberBean.getContactInfo();
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			// getPerInformation(PiEditorActivity.this, memberBeanId);
			setView();
			setImageInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		pi_name = (EditText) findViewById(R.id.pi_name);
		pi_birthday = (TextView) findViewById(R.id.pi_birthday);
		pi_sex = (TextView) findViewById(R.id.pi_sex);
		pi_introduction = (EditText) findViewById(R.id.pi_introduction);

		pi_picHead = (ImageView) findViewById(R.id.pi_picHead);
		pi_phone = (TextView) findViewById(R.id.pi_phone);

		pi_picHead.setOnClickListener(this);
		pi_birthday.setOnClickListener(this);
		pi_sex.setOnClickListener(this);
	}

	private void setView() {
		lName = basicInfo.getMemberName();
		pi_name.setText(lName);
		pi_name.setSelection(lName.length());
		String date = basicInfo.getBirthday();
		String[] lastDate = ConstantUtil.YYYYMMDD.format(
				DateTimeUtil.getLongTime(date)).split("-");
		try {
			mYear = Integer.valueOf(lastDate[0]);
			mMonth = Integer.valueOf(lastDate[1]) - 1;
			mDay = Integer.valueOf(lastDate[2]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		pi_birthday.setText(ConstantUtil.YYYYMMDD.format(DateTimeUtil
				.getLongTime(date)));
		if (basicInfo.getMemberSex() == 1) {
			sexInfo = "男";
		} else {
			sexInfo = "女";
		}
		pi_sex.setText(sexInfo);
		String phone = contactInfo.getMemberPhone();
		if (CharUtil.isValidPhone(phone))
			pi_phone.setText(phone);
		pi_introduction.setText(basicInfo.getIntroduction());
	}

	/**
	 * 对界面中的图片进行加载
	 */
	private void setImageInfo() {
		String head_Url = basicInfo.getHeadImage().getHeadUrl()
				+ basicInfo.getHeadImage().getHeadPath();
		head_Url = ThumbnailImageUrl.getThumbnailHeadUrl(head_Url,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		MyFinalBitmap.setHeader(this, pi_picHead, head_Url);
	}

	// /**
	// * 获取带有成员信息的Bean
	// *
	// * @param context
	// * @param memberId
	// * @throws Exception
	// */
	// private void getPerInformation(Context context, String memberId)
	// throws Exception {
	// MCResult mcResult = APIRequestServers.getMemberBasicInfo(context,
	// memberId);
	// memberBean = (MemberBean) mcResult.getResult();
	// basicInfo = memberBean.getBasicInfo();
	// contactInfo = memberBean.getContactInfo();
	// }

	private void getEditText() {
		name = pi_name.getText().toString();
		birthday = pi_birthday.getText().toString();
		sexInfo = pi_sex.getText().toString();
		if (sexInfo.equals("男")) {
			sex = 1;
		} else {
			sex = 2;
		}
		introduction = pi_introduction.getText().toString();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理拍照返回数据
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			try {
				switch (requestCode) {
				case FROM_CAMERA:
					File tempFile = new File(headimg_file);
					File picture = new File(tempFile, headimg_name);
					uri = Uri.fromFile(picture);
					startPhotoZoom(uri);
					break;
				case FROM_GALLERY:
					uri = data.getData();
					startPhotoZoom(uri);
					break;
				case FROM_CROP:
					new Builder(PiEditorActivity.this)
							.setTitle("修改头像")
							.setMessage(
									getResourcesString(R.string.personinfo_uploadimgdialog_confirmupload))
							.setPositiveButton(
									getResources().getString(
											R.string.alertdialog_confirm),
									new OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											File saveFile = new File(
													headimg_file);
											headPic = new File(saveFile,
													headimg_name);
											getHeadFilePath(Uri
													.fromFile(headPic));
											pi_picHead
													.setImageBitmap(headimgbit);

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
	 * 获取返回图片的路径
	 * 
	 * @param uri
	 */
	private void getHeadFilePath(Uri uri) {
		String sUri = uri.toString();
		String imgPath = null;
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
	 * 或许图片路径并上传
	 * 
	 * @param uriString
	 **/
	private void uploadHead(final File file) {
		APIRequestServers.uploadFile(PiEditorActivity.this, file,
				UploadMethodEnum.UPLOADHEAD, null);
	}

	/**
	 * 显示修改头像对话框
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showUpHeadDialog() {
		Builder builder = new Builder(this);
		builder.setTitle("修改头像");
		String[] dialogMsg = new String[] {
				getResourcesString(R.string.selectImgdialog_fromlocality),
				getResourcesString(R.string.selectImgdialog_fromcamera)
		// , getResourcesString(R.string.selectImgdialog_cancel)
		};
		builder
		// .setItems(dialogMsg, new DialogInterface.OnClickListener() {
		.setAdapter(new ArrayAdapter(this, R.layout.choice_item, dialogMsg),
				new OnClickListener() {
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

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.pi_picHead:
			showUpHeadDialog();
			break;
		case R.id.pi_birthday:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.pi_sex:
			showAlertDialog();
			break;
		default:
			break;
		}
	}

	class loadInfoTask extends AsyncTask<String, Integer, MCResult> {

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.editeMyData(PiEditorActivity.this,
						name, sex, birthday, introduction);
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
				int i = (Integer) result.getResult();
				if (i == 1) {
					setResult(11, intent);

					if (!lName.equals(name)) {
						new UserBusinessDatabase(App.app).updateName(
								App.app.share.getSessionKey(), name);
						// if (InfoWallActivity.infoWallActivity != null
						// && InfoWallActivity.infoWallActivity.menus != null) {
						// InfoWallActivity.infoWallActivity.menus
						// .resetName(name);
						// }
						// MenuFragment.refreshName(name);
						BasicMenuActivity.refreshInfo();
					}
					HomeQboFragment.needRefresh = true;
					finish();
				} else if (i == 2) {
					showTip("请输入规范的名字！");
				} else {
					showTip(T.ErrStr);
				}
			}
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
			getEditText();
			if (!CheckNameUtil.checkMemberName(name)) {
				showTip("请输入规范的名字！");
				return true;
			}
			if (headPic != null) {
				if (headPic.exists()) {
					uploadHead(headPic);
				}
			}
			spdDialog.showProgressDialog("正在处理中...");
			new loadInfoTask().execute();
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
	// getEditText();
	// if (!CheckNameUtil.checkMemberName(name)) {
	// showTip("请输入规范的名字！");
	// return;
	// }
	// if (headPic != null) {
	// if (headPic.exists()) {
	// uploadHead(headPic);
	// }
	// }
	// spdDialog.showProgressDialog("正在处理中...");
	// new loadInfoTask().execute();
	// }

	/**
	 * 更新显示
	 */
	private void updateDisplay() {
		pi_birthday.setText(new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay));
	}

	/**
	 * 获取string文件中的文字 返回值：获取到得文字
	 * 
	 * @param id
	 *            int類型 string文件中的id
	 * @return String
	 **/
	private String getResourcesString(int id) {
		return getResources().getString(id);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case DATE_DIALOG_ID:
			L.i(TAG, "onCreateDialog mYear=" + mYear + ",mMonth=" + mMonth
					+ ",mDay=" + mDay);
			return new DatePickerDialog(this, this, mYear, mMonth, mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			L.i(TAG, "onPrepareDialog mYear=" + mYear + ",mMonth=" + mMonth
					+ ",mDay=" + mDay);
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mYear = year;
		mMonth = monthOfYear;
		mDay = dayOfMonth;
		updateDisplay();
	}

	/**
	 * 展示选择性别的dialog，里面包含单选列表
	 */
	private void showAlertDialog() {
		String[] info = { "男", "女" };
		Builder builder = new Builder(PiEditorActivity.this);
		builder.setTitle("设置性别");
		if (sexInfo != null && sexInfo.endsWith("男")) {
			builder.setSingleChoiceItems(info, 0, this);
		} else {
			builder.setSingleChoiceItems(info, 1, this);
		}
		builder.setPositiveButton("确认", this);
		builder.setNegativeButton("取消", this);
		builder.create().show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case 0:
			sexInfo = "男";
			break;
		case 1:
			sexInfo = "女";
			break;
		case -2:
			dialog.dismiss();
			break;
		case -1:
			pi_sex.setText(sexInfo);
			dialog.dismiss();

			break;
		default:
			break;
		}
	};

}

package com.datacomo.mc.spider.android.fragmt;

import java.io.File;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.EditActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.PhotoGalleryActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberContactBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class HomeInfoFragment extends BaseGroupFragment {
	private static final String TAG = "HomeInfoFragment";
	private static final int FROM_GALLERY = 0;
	private static final int FROM_CAMERA = 1;
	private static final int FROM_CROP = 2;
	public static final int FROM_MOOD = 3;
	public static final int FROM_EDIT = 4;

	private String headimg_file = ConstantUtil.HEAD_PATH;
	private String headimg_name = "headimg.jpg";
	private Bitmap headimgbit;

	private LinearLayout ll_fname;
	private TextView pi_name, pi_fname, pi_sex, pi_birthday, pi_interest,
			pi_phone, pi_email, pi_hometown, pi_home, pi_shool, pi_workunit,
			pi_introduction, pi_hobby;
	private TextView space_name, space_mood, space_money;
	private ImageView pi_head, poster;
	// public static boolean needRefresh;

	public MemberBean memberBean;
	private MemberBasicBean basicInfo;
	private MemberContactBean contactInfo;
	private LoadPersonInfo task;

	private HomePgActivity homePg;
	private ViewGroup view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homePg = (HomePgActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = (ViewGroup) inflater
				.inflate(R.layout.layout_personal_info, null);
		findViews();
		loadInfo();
		new SetMemberSkin().execute();
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = null;
			switch (requestCode) {
			case FROM_CAMERA:
				File tempFile = new File(headimg_file);
				File picture = new File(tempFile, headimg_name);
				uri = Uri.fromFile(picture);
				startPhotoZoom(uri, headimg_file, headimg_name);
				break;
			case FROM_GALLERY:
				uri = data.getData();
				startPhotoZoom(uri, headimg_file, headimg_name);
				break;
			case FROM_CROP:
				File saveFile = new File(headimg_file);
				File headPic = new File(saveFile, headimg_name);
				headimgbit = HomeQboFragment.getHeadFilePath(homePg,
						Uri.fromFile(headPic));
				pi_head.setImageBitmap(headimgbit);

				if (headPic.exists()) {
					uploadHead(headPic);
				}

				break;
			case FROM_MOOD:
				String newMood = data.getStringExtra("feelword");
				L.d(TAG, "onActivityResult mood=");
				if (newMood != null && !newMood.equals("")
						&& !newMood.equals(space_mood.getText().toString())) {
					setMood(0, newMood);
				}
				break;
			case FROM_EDIT:
				loadInfo();
			default:
				break;
			}
		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 *            图片路径
	 **/
	public void startPhotoZoom(Uri uri, String path, String name) {
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType(uri, "image/*");// 设置要裁剪的图片
		cropIntent.putExtra("crop", "true");// crop=true 裁剪页面.
		// 设置其他信息：
		cropIntent.putExtra("aspectX", 1);// 设置裁剪框的比例.
		cropIntent.putExtra("aspectY", 1);// x:y=1:1
		// outputX outputY 是裁剪图片宽高
		// cropIntent.putExtra("outputX", 1200);
		// cropIntent.putExtra("outputY", 1200);
		cropIntent.putExtra("return-data", "false");
		File saveFile = new File(path);
		File picture = new File(saveFile, name);
		cropIntent.putExtra("output", Uri.fromFile(picture));// 保存输出文件
		cropIntent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(cropIntent, HomePgActivity.FROM_CROP);
	}

	private void uploadHead(final File file) {
		APIRequestServers.uploadFile(homePg, file, UploadMethodEnum.UPLOADHEAD,
				null);
	}

	/**
	 * 初始化基本组件
	 */
	private void findViews() {
		if (homePg.menu != null) {
			if (homePg.menu.findItem(R.id.action_search) != null)
				homePg.menu.findItem(R.id.action_search).setVisible(false);
			if (homePg.menu.findItem(R.id.action_write_mood) != null)
				homePg.menu.findItem(R.id.action_write_mood).setVisible(false);
			if (homePg.menu.findItem(R.id.action_more) != null)
				homePg.menu.findItem(R.id.action_more).setVisible(false);
			if (homePg.menu.findItem(R.id.action_write) != null)
				if (homePg.userSelf) {
					homePg.menu.findItem(R.id.action_write).setVisible(true);
				} else {
					homePg.menu.findItem(R.id.action_write).setVisible(false);
				}
		}

		ll_fname = (LinearLayout) view
				.findViewById(R.id.linearLayout_friend_name);
		pi_fname = (TextView) view.findViewById(R.id.friend_name);

		pi_name = (TextView) view.findViewById(R.id.pi_name);
		pi_sex = (TextView) view.findViewById(R.id.pi_sex);
		pi_birthday = (TextView) view.findViewById(R.id.pi_birthday);
		pi_interest = (TextView) view.findViewById(R.id.pi_interest);
		pi_phone = (TextView) view.findViewById(R.id.pi_phone);
		pi_email = (TextView) view.findViewById(R.id.pi_email);
		pi_hometown = (TextView) view.findViewById(R.id.pi_hometown);
		pi_home = (TextView) view.findViewById(R.id.pi_home);
		pi_shool = (TextView) view.findViewById(R.id.pi_shool);
		pi_workunit = (TextView) view.findViewById(R.id.pi_workunit);
		pi_introduction = (TextView) view.findViewById(R.id.pi_introduction);
		pi_hobby = (TextView) view.findViewById(R.id.pi_hobby);

		ImageView img_camera = (ImageView) view.findViewById(R.id.img_camera);
		img_camera.setVisibility(View.VISIBLE);
		img_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setAlterHead();
			}
		});

		pi_head = (ImageView) view.findViewById(R.id.img_header);
		pi_head.setOnClickListener(this);
		pi_head.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				setAlterHead();
				return true;
			}
		});

		poster = (ImageView) view.findViewById(R.id.img_poster);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) poster
				.getLayoutParams();
		if (null == lp) {
			lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
		}
		lp.height = homePg.sW * 3 / 5;
		poster.setLayoutParams(lp);
		poster.requestLayout();
		space_name = (TextView) view.findViewById(R.id.name);
		space_money = (TextView) view.findViewById(R.id.money);
		space_mood = (TextView) view.findViewById(R.id.mood);
		// setMidChosen(3);
	}

	void loadInfo() {
		try {
			stopTask();
			task = new LoadPersonInfo();
			task.execute();
		} catch (RejectedExecutionException re) {
			re.printStackTrace();
		}
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	class SetMemberSkin extends AsyncTask<Void, Integer, MCResult> {

		protected void onPostExecute(MCResult result) {
			if (result == null || result.getResultCode() != 1) {
				homePg.showTip(T.ErrStr);
			} else {
				String memberSkin = (String) result.getResult();
				// Drawable vis_drawable = PublicLoadPicture.getDrawable(
				// PerInformationActivity.this, memberSkin, poster,
				// R.drawable.bg_translucent);
				// Drawable vis_drawable =
				// PublicLoadPicture.loadPoster(PerInformationActivity.this,
				// memberSkin, poster,"perinformationactivity_1");
				// poster.setImageDrawable(vis_drawable);
				// TODO
				MyFinalBitmap.setSkin(homePg, poster, memberSkin);
			}
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			try {
				return APIRequestServers.memberSkin(App.app, homePg.id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		};
	}

	class LoadPersonInfo extends AsyncTask<Void, Integer, MCResult> {

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.getMemberBasicInfo(App.app,
						homePg.id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			try {
				setTextInfo(mcResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 根据Bean获取见面的数据并显示
	 */
	private void setTextInfo(MCResult mcResult) {
		if (mcResult == null || mcResult.getResultCode() != 1) {
			// homePg.finish();
			homePg.showTip(T.ErrStr);
		} else {
			memberBean = (MemberBean) mcResult.getResult();
			basicInfo = memberBean.getBasicInfo();
			if (basicInfo != null) {
				setSpaceInfo(basicInfo);
				String name = basicInfo.getMemberName();
				String friendRemarkName = memberBean.getFriendRemarkName();
				pi_name.setText(name);
				if (friendRemarkName != null && !"".equals(friendRemarkName)
						&& !friendRemarkName.equals(name)) {
					pi_fname.setText(friendRemarkName);
					space_name.setText(friendRemarkName);
					ll_fname.setVisibility(View.VISIBLE);
				} else {
					ll_fname.setVisibility(View.GONE);
				}
			}
			contactInfo = memberBean.getContactInfo();
			// 异步加载头像
			setImageInfo();
			if (basicInfo.getMemberSex() == 1) {
				pi_sex.setText("男");
			} else {
				pi_sex.setText("女");
			}

			String birthday = basicInfo.getBirthday();
			if (birthday != null && !"".equals(birthday))
				pi_birthday.setText(ConstantUtil.YYYYMMDD.format(DateTimeUtil
						.getLongTime(birthday)));

			int memberSexual = basicInfo.getMemberSexual();
			if (memberSexual == 1) {
				pi_interest.setText("男性");
			} else if (memberSexual == 2) {
				pi_interest.setText("女性");
			} else {
				pi_interest.setText("男性和女性");
			}
			String phone = contactInfo.getMemberPhone();
			if (CharUtil.isValidPhone(phone))
				pi_phone.setText(phone);
			pi_email.setText(contactInfo.getMemberMail());
			pi_hometown.setText(memberBean.getHometownAddress()
					.getAddressName());
			pi_home.setText(memberBean.getResidenceAddress().getAddressName());
			pi_shool.setText(memberBean.getSchoolInfo().getSchoolName());
			pi_workunit.setText(memberBean.getCompanyInfo().getCompanyName());
			String info = basicInfo.getIntroduction();
			if (null != info) {
				info = info.replace("&nbsp;", "");
			}
			pi_introduction.setText(info);
			pi_hobby.setText(basicInfo.getMemberHobby());
		}

		String phone = contactInfo.getMemberPhone();
		if (CharUtil.isValidPhone(phone))
			pi_phone.setText(phone);
		pi_email.setText(contactInfo.getMemberMail());
		pi_hometown.setText(memberBean.getHometownAddress().getAddressName());
		pi_home.setText(memberBean.getResidenceAddress().getAddressName());
		pi_shool.setText(memberBean.getSchoolInfo().getSchoolName());
		pi_workunit.setText(memberBean.getCompanyInfo().getCompanyName());
		String info = basicInfo.getIntroduction();
		if (null != info) {
			pi_introduction.setText(info.replace("&nbsp;", ""));
		} else {
			pi_introduction.setText("");
		}
		pi_introduction.setText(info);
		pi_hobby.setText(basicInfo.getMemberHobby());
	}

	private void setSpaceInfo(MemberBasicBean mberBean) {
		String name = mberBean.getMemberName();
		space_name.setText(name);
		MemberHeadBean memberHeadBean = mberBean.getHeadImage();
		String head_Url = memberHeadBean.getHeadUrl()
				+ memberHeadBean.getHeadPath();
		head_Url = ThumbnailImageUrl.getThumbnailHeadUrl(head_Url,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);

		// MemberContactBean bean_MemberContact = memberBean.getContactInfo();
		// if (null != bean_MemberContact) {
		// String number = bean_MemberContact.getMemberPhone();
		// }
		int friendStatus = memberBean.getFriendStatus();
		if (friendStatus == 0) {
			// 获取自己的圈币数量
			int memberGold = memberBean.getAccountInfo().getMemberGold();
			space_money.setText("" + memberGold);
			space_money.setVisibility(View.VISIBLE);
			space_money.setClickable(false);
			view.findViewById(R.id.money_text).setVisibility(View.VISIBLE);
			view.findViewById(R.id.img_camera).setVisibility(View.VISIBLE);
			pi_head.setClickable(true);
		} else {
			view.findViewById(R.id.money_text).setVisibility(View.GONE);
			view.findViewById(R.id.img_camera).setVisibility(View.GONE);
			view.findViewById(R.id.money_text).setVisibility(View.GONE);
			space_money.setVisibility(View.GONE);
			// if (friendStatus != 1) {
			// space_invite.setVisibility(View.VISIBLE);
			// }
		}

		int sex = 0;
		try {
			sex = mberBean.getMemberSex();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String mMood = mberBean.getFeelingWord();
		if (mMood != null && !mMood.equals("")) {
			mMood = mMood.replace("&nbsp;", "");
			setMood(friendStatus, mMood);
		} else {
			if (0 == friendStatus) {
				setMood(friendStatus, "发表个心情吧~~~~");
			} else if (sex == 1) {
				setMood(friendStatus, "他还没有发表过心情哦~~~~");
			} else if (sex == 2) {
				setMood(friendStatus, "她还没有发表过心情哦~~~~");
			} else {
				setMood(friendStatus, "TA还没有发表过心情哦~~~~");
			}
		}
	}

	/**
	 * 对界面中的图片进行加载
	 */
	private void setImageInfo() {
		String head_Url = basicInfo.getHeadImage().getHeadUrl()
				+ basicInfo.getHeadImage().getHeadPath();
		pi_head.setTag(head_Url);
		head_Url = ThumbnailImageUrl.getThumbnailHeadUrl(head_Url,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		// Drawable drawable = PublicLoadPicture.loadHead(
		// PerInformationActivity.this, head_Url, pi_head,
		// "perinformationactivity_2");
		// pi_head.setImageDrawable(drawable);
		// TODO
		MyFinalBitmap.setHeader(homePg, pi_head, head_Url);
	}

	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			int id = 0;
			try {
				id = Integer.parseInt(source);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 根据id从资源文件中获取图片对象
			Drawable d = getResources().getDrawable(id);
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			return d;
		}
	};

	// @Override
	// public void onResume() {
	// super.onResume();
	// if (HomePgActivity.needRefresh) {
	// loadInfo();
	// }
	// }

	private void checkHead(String url) {
		if (null == url || "".equals(url)) {
			return;
		}
		Bundle bundle = new Bundle();
		int index = 1;
		bundle.putInt("index", (index - 1));
		bundle.putString("type", "type_none");
		bundle.putBoolean("isOriginal", true);
		bundle.putString("url", url);
		LogicUtil.enter(homePg, PhotoGalleryActivity.class, bundle, false);
	}

	private void setMood(int friendState, final String moodText) {
		space_mood.setText(Html.fromHtml(moodText + "<img src='"
				+ R.drawable.icon_edit + "'/>", imageGetter, null));
		if (0 == friendState) {
			space_mood.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if ((GetDbInfoUtil.getMemberId(homePg) + "")
							.equals(homePg.id)) {
						Intent mIntent = new Intent(homePg, EditActivity.class);
						Bundle b = new Bundle();
						b.putString("ori", moodText);
						b.putString("id", homePg.id);
						b.putInt("type", HomePgActivity.TYPE_MBER);
						b.putInt("typedata", EditActivity.TYPE_MOOD);
						mIntent.putExtras(b);
						startActivityForResult(mIntent, FROM_MOOD);
					}
				}
			});
		} else {
			space_mood.setText(moodText);
			space_mood.setOnClickListener(null);
		}
	}

	// @Override
	// protected void onRightClick(View v) {
	// if ((GetDbInfoUtil.getMemberId(this) + "").equals(id)) {
	// Intent intent = new Intent();
	// intent.setClass(this, PiEditorActivity.class);
	// Bundle b = new Bundle();
	// b.putString("id", id);
	// b.putSerializable("memberBean", memberBean);
	// intent.putExtras(b);
	// startActivityForResult(intent, 11);
	// // LogicUtil.enter(PerInformationActivity.this,
	// // PiEditorActivity.class,
	// // b, false);
	// } else {
	// LogicUtil.enter(this, InfoWallActivity.class, null, true);
	// }
	// }

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_header:
			checkHead((String) v.getTag());
		}
	}

	private void setAlterHead() {
		if (homePg.sType == HomePgActivity.TYPE_MBER) {
			if ((GetDbInfoUtil.getMemberId(homePg) + "").equals(homePg.id)) {
				showUpHeadDialog(HomePgActivity.TYPE_MBER, headimg_file,
						headimg_name);
			}
		} else {
		}
	}

	/**
	 * 显示修改头像对话框
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void showUpHeadDialog(int type, final String head_file,
			final String head_name) {
		AlertDialog.Builder builder = new AlertDialog.Builder(homePg);
		if (type == HomePgActivity.TYPE_MBER) {
			builder.setTitle("修改头像");
		} else {
			builder.setTitle("修改海报");
		}

		String[] dialogMsg = new String[] {
				getResourcesString(homePg,
						R.string.selectImgdialog_fromlocality),
				getResourcesString(homePg, R.string.selectImgdialog_fromcamera)
		// , getResourcesString(c, R.string.selectImgdialog_cancel)
		};
		builder
		// .setItems(dialogMsg, new DialogInterface.OnClickListener() {
		.setAdapter(new ArrayAdapter(homePg, R.layout.choice_item, dialogMsg),
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
									HomePgActivity.FROM_GALLERY);
							break;
						case 1:
							Intent intent1 = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							File saveFile = new File(head_file);
							if (saveFile.exists()) {
								// Log.d(TAG,"目录已存在");
							} else {
								saveFile.mkdirs();
							}
							intent1.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(saveFile, head_name)));
							startActivityForResult(intent1,
									HomePgActivity.FROM_CAMERA);
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

	public static String getResourcesString(Context c, int id) {
		return c.getResources().getString(id);
	}

	@Override
	public void search(String s) {
		// TODO
	}

}

package com.datacomo.mc.spider.android;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.ClientWelcomePicEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class SpiderTouchApp extends BaseUMengActivity {
	private String TAG = "SpiderTouchApp";

	RelativeLayout back;
	ImageView sign;// logo,

	@Override
	protected void onPause() {
		super.onPause();
		//
		BaseData.setStateBarHeight(this);
		// 计算header高度
		if (-1 == App.app.share.getIntMessage("program", "header_height", -1)) {
			try {
				View header = findViewById(R.id.header);
				int headerHeight = header.getHeight();
				App.app.share.saveIntMessage("program", "header_height",
						headerHeight);
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		ImageDealUtil.releaseImageBackground(back, false);
		ImageDealUtil.releaseImageDrawable(sign, false);
		// ImageDealUtil.releaseImageDrawable(logo, false);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		// VMRuntime.getRuntime().setTargetHeapUtilization(
		// BaseActivity.TARGET_HEAP_UTILIZATION);
		setContentView(R.layout.welcome);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

		if (-1 == App.app.share.getIntMessage("program", "screen_width", -1)) {
			App.app.share.saveIntMessage("program", "screen_width",
					outMetrics.widthPixels);
			App.app.share.saveIntMessage("program", "screen_height",
					outMetrics.heightPixels);
		}
		findView();
		// back.setBackgroundResource(R.drawable.wel_bg);
		String url = App.app.share.getStringMessage("program", "welurl", "");
		if (!"".equals(url)) {
			setGroupSkin(url);
		}

		String currentVersion = new SoftPhoneInfo(App.app).getVersionName();
		if (currentVersion != null && !"".equals(currentVersion)) {
			TextView version = (TextView) findViewById(R.id.welcome_version);
			version.setText("V" + currentVersion);
		}

		String[] folder = { ConstantUtil.HEAD_PATH, ConstantUtil.IMAGE_PATH,
				// ConstantUtil.POSTER_PATH,
				ConstantUtil.VOICE_PATH, ConstantUtil.CAMERA_PATH,
				// ConstantUtil.DOWNLOAD_PATH,
				ConstantUtil.TEMP_PATH, ConstantUtil.CLOUD_PATH
		// , ConstantUtil.CACHE_PATH
		};
		for (String path : folder) {
			FileUtil.createFile(path);
		}
		// findViewById(R.id.progressBar).getBackground().setAlpha(192);

		new Thread() {
			@Override
			public void run() {
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LogicUtil.enter(SpiderTouchApp.this, LoginActivity.class, null,
						true);

				try {
					String welurl = "";
					String welname = "";
					// String str = StreamUtil
					// .readData(new HttpRequestServers()
					// .getRequest("http://192.168.1.173:8080/YuuQuu/update.xml"));
					// L.i(TAG, "Thread str=" + str);
					// int start0 = str.indexOf("<welurl>") +
					// "<welurl>".length();
					// int end0 = str.lastIndexOf("</welurl>");
					// welurl = str.substring(start0, end0);

					// TODO
					// MCResult mc = APIRequestServers.bootScreenImage(App.app);
					String session_key = App.app.share.getSessionKey();
					if (session_key == null || "".equals(session_key)) {
						return;
					}
					MCResult mc = APIRequestServers.clientWelcomePic(App.app,
							ClientWelcomePicEnum.CLIENTWELCOMEPIC, 0);
					if (mc != null && mc.getResultCode() == 1) {
						// welurl = mc.getResult().toString();
						try {
							JSONObject object = new JSONObject(mc.getResult()
									.toString());
							int STATUS = object.getInt("STATUS");
							if (STATUS == 1) {
								String url = object.getString("PHOTO_URL");
								String path = object.getString("PHOTO_PATH");
								if (url != null && !"".equals(url))
									welurl += url;
								if (path != null && !"".equals(path))
									welurl += path;

								welname = object.getString("GROUP_NAME");
							}
						} catch (Exception e) {
							JSONObject object = new JSONObject(mc.getResult()
									.toString());
							welurl = object.getString("PHOTO_URL");
						}
					}
					L.d(TAG, "welurl=" + welurl);
					if (welurl != null && welurl.startsWith("http")) {
						App.app.share.saveStringMessage("program", "welurl",
								welurl);
						if (welname == null || "".equals(welname))
							welname = "优优工作圈";
						App.app.share.saveStringMessage("program", "welname",
								welname);
					} else {
						// preferences.saveStringMessage("program", "welurl",
						// "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void findView() {
		back = (RelativeLayout) findViewById(R.id.welcome_layout);
		sign = (ImageView) findViewById(R.id.welcome_back);
		// logo = (ImageView) findViewById(R.id.welcome_logo);
	}

	private void setGroupSkin(String groupSkin) {
		L.i(TAG, "setGroupSkin groupSkin=" + groupSkin);
		MyFinalBitmap.setGroupSkin(this, sign, groupSkin,
				new ImageLoadingListener() {
					@Override
					public void setProgressMax(int arg0) {
					}

					@Override
					public void onStartProgress() {
					}

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
					}

					@Override
					public void onLoadingProgress(int arg0, int load, int len) {
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						L.i(TAG, "onLoadingComplete arg0=" + arg0);
						// back.setBackgroundDrawable(ImageDealUtil
						// .bitmapToDrawable(arg2));
						sign.setImageBitmap(arg2);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
					}

					@Override
					public int getProgressMax() {
						return 0;
					}
				});
	}

}

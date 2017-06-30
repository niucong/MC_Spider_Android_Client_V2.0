package com.datacomo.mc.spider.android.application;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.AppSharedPreferences;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class App extends Application {
	private static final String TAG = "App";

	public static App app;

	// public LocationClient mLocationClient = null;
	// public MyLocationListenner myListener;
	public NotifyLister mNotifyer = null;
	public Vibrator mVibrator;

	public String loc;
	public double mlat, mLon;
	
	public AppSharedPreferences share;
	
	@Override
	public void onCreate() {
		// mLocationClient = new LocationClient(this);
		// myListener = new MyLocationListenner();
		// mLocationClient.registerLocationListener(myListener);

		super.onCreate();
		app = this;
		initImageLoader(getApplicationContext());
		
		share = new AppSharedPreferences(this);
	}

	private void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3).denyCacheImageMultipleSizesInMemory()
				// .memoryCacheSize(6 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() // Not
				// necessary
				// in
				// common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			loc = location.getAddrStr();
			mlat = location.getLatitude();
			mLon = location.getLongitude();
			L.i(TAG, "onReceiveLocation loc=" + loc);
			L.i(TAG, "onReceiveLocation mlat=" + mlat);
			L.i(TAG, "onReceiveLocation mLon=" + mLon);
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null)
				return;
			loc = poiLocation.getAddrStr();
			mlat = poiLocation.getLatitude();
			mLon = poiLocation.getLongitude();
			L.d(TAG, "onReceivePoi loc=" + loc);
			L.d(TAG, "onReceivePoi mlat=" + mlat);
			L.d(TAG, "onReceivePoi mLon=" + mLon);
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
			mVibrator.vibrate(1000);
		}
	}
}

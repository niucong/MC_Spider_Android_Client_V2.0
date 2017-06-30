package com.datacomo.mc.spider.android.view;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.RejectedExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.datacomo.mc.spider.android.util.TaskUtil;

/**
 * ImageView extended class allowing easy downloading of remote images
 */
public class RemoteImageView extends ImageView {

	public HashMap<String, SoftReference<Drawable>> imageCache = TaskUtil.drawableCache;

	// private static final int MAX_FAIL_TIME = 5;
	private static final int MAX_FAIL_TIME = 1; // 涓嶅仛閲嶆柊璇诲彇澶勭悊
	private int mFails = 0;
	String key;
	private String mUrl;

	public RemoteImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RemoteImageView(Context context) {
		super(context);
	}

	public void setDefaultImage(int resId) {
		this.setImageResource(resId);
		this.setBackgroundResource(resId);
	}

	public void setImageUrl(String url) {
		if (mUrl != null && mUrl.equals(url)) {
			mFails++;
		} else {
			mFails = 0;
			mUrl = url;
		}

		if (mFails >= MAX_FAIL_TIME)
			return;

		mUrl = url;
		key = TaskUtil.getKey(mUrl);
		if (isCached(key))
			return;

		startDownload(url);
	}

	public String getImageUrl() {
		return mUrl;
	}

	public boolean isCached(String key) {
		if (imageCache.containsKey(key)) {
			SoftReference<Drawable> reference = imageCache.get(key);
			Drawable drawable = reference.get();
			this.setImageDrawable(drawable);
			return true;
		}

		return false;
	}

	private void startDownload(String url) {
		try {
			new DownloadTask().execute(url);
		} catch (RejectedExecutionException e) {
		}
	}

	private void reDownload(String url) {
		setImageUrl(url);
	}

	class DownloadTask extends AsyncTask<String, Void, String> {

		private String imageUrl;

		@Override
		protected String doInBackground(String... params) {
			imageUrl = params[0];
			InputStream is = null;
			Bitmap bmp = null;
			try {
				HttpGet httpRequest = new HttpGet(imageUrl);
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient
						.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
						entity);
				is = bufferedHttpEntity.getContent();
				bmp = BitmapFactory.decodeStream(is);
				if (bmp != null) {
					Drawable drawable = new BitmapDrawable(bmp);
					SoftReference<Drawable> reference = new SoftReference<Drawable>(
							drawable);
					imageCache.put(key, reference);
				} else {
					reDownload(imageUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return imageUrl;
		}

		@Override
		protected void onPostExecute(String result) {
			Bitmap bmp = null;
			if (imageCache.containsKey(key)) {
				SoftReference<Drawable> reference = imageCache.get(key);
				Drawable drawable = reference.get();
				// RemoteImageView.this.setImageBitmap(bmp);
				RemoteImageView.this.setBackgroundDrawable(drawable);
			} else {
				reDownload(imageUrl);
			}
			super.onPostExecute(result);
		}

	}

}

package com.datacomo.mc.spider.android.task;
//package com.datacomo.mc.spider.android.task;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.widget.ImageView;
//
//import com.datacomo.mc.spider.android.application.App;
//
//@SuppressWarnings("rawtypes")
//public class InflateTask extends AsyncTask {
//	private ImageView gView;
//	String iconurl;
//
//	public InflateTask(String url) {
//
//		this.iconurl = url;
//	}
//
//	@Override
//	protected Object doInBackground(Object... views) {
//		Bitmap bitmap = null;
//		try {
//			URL url = new URL(iconurl);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.connect();
//			App.saveTextData(iconurl.length());
//			App.saveFileData(conn.getContentLength());
//			InputStream is = conn.getInputStream();
//			bitmap = BitmapFactory.decodeStream(is);
//			is.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		this.gView = (ImageView) views[0];
//		return bitmap;
//	}
//
//	@Override
//	protected void onCancelled() {
//		super.onCancelled();
//	}
//
//	@Override
//	protected void onPostExecute(Object result) {
//		if (result != null) {
//			this.gView.setImageBitmap((Bitmap) result);
//			this.gView = null;
//		}
//	}
//
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void onProgressUpdate(Object... values) {
//		super.onProgressUpdate(values);
//	}
//
//}

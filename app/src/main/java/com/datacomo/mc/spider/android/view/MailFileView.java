package com.datacomo.mc.spider.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.MailCreateActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.AdditiveFileBean;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.util.FileUtil;

@SuppressLint("HandlerLeak")
public class MailFileView {
	// private static final String TAG = "MailFileView";

	private View view;
	private ImageView icon_iv;// , del_iv, line_tv;
	private TextView name_tv, progress_tv;
	// private ProgressBar pb;

	private LayoutInflater inflater;

	// private boolean isDel = false;
	// private String attachmentStrs = null;
	// private String uploadTag = "";
	// private Context context;

	private boolean isPhoto;
	private Object bean;

	public MailFileView(Context context, boolean isPhoto, Object bean) {
		// this.context = context;
		this.isPhoto = isPhoto;
		this.bean = bean;
		inflater = LayoutInflater.from(context);
		findView();
		setView();
		// startThread();
	}

	// private void startThread() {
	// new Thread() {
	// public void run() {
	// String url = URLProperties.MEMBER_JSON;
	// // .replace("https", "http");
	// String method = APIMethodName.UPLOADEMAILFILE;
	// uploadTag = file.getPath();
	// L.d(TAG, "startThread 上传开始..." + uploadTag);
	// MailCreateActivity.mailCreate.isUploadOver
	// .put(uploadTag, false);
	//
	// String params = new UploadFileParams(context, method,
	// file.getName(), null).getParams();
	//
	// L.d(TAG, "startThread url=" + url + "?" + params);
	// try {
	// String result = httpUpload(url + "?" + params, file);
	// L.d(TAG, "startThread result=" + result);
	// if (isDel) {
	// return;
	// }
	// if (result != null) {
	// JSONObject object = new JSONObject(result);
	// if (object.getInt("resultCode") == 1) {
	// //
	// //
	// {"result":"//2012/10/22/201210220958584298.jpg","resultStaus":true,"resultCode":1,"resultMessage":"上传附件成功","version":"v1.0"}
	// //
	// //
	// yuuquu.apk#MC_API#http://image.mobi5.cn/m6/default/android/client-android.apk#MC_API#1234567
	// attachmentStrs = file.getName()
	// + ConstantUtil.PHONE_SEPARATOR
	// + object.getString("result")
	// + ConstantUtil.PHONE_SEPARATOR
	// + file.length();
	// L.i(TAG, "startThread attachmentStrs="
	// + attachmentStrs);
	// MailCreateActivity.mailCreate.attachmentsList
	// .add(attachmentStrs);
	// } else {
	// L.d(TAG, "startThread 上传失败...");
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// // 上传失败，点击重传
	// progressHandler.sendEmptyMessage(2);
	// }
	// L.i(TAG, "startThread 上传结束..." + uploadTag);
	// MailCreateActivity.mailCreate.isUploadOver.put(uploadTag, true);
	// };
	// }.start();
	// }

	private void findView() {
		view = inflater.inflate(R.layout.file_item, null);
		icon_iv = (ImageView) view.findViewById(R.id.file_item_icon);
		name_tv = (TextView) view.findViewById(R.id.file_item_name);
		// del_iv = (ImageView) view.findViewById(R.id.file_item_del);
		// pb = (ProgressBar)
		view.findViewById(R.id.file_item_progressBar).setVisibility(View.GONE);
		progress_tv = (TextView) view.findViewById(R.id.file_item_progressTv);
		// line_tv = (ImageView) view.findViewById(R.id.file_item_line);
	}

	private void setView() {
		String filePath = "";
		String fileName = "";
		String size = "";
		if (bean instanceof FileInfoBean) {
			FileInfoBean fiBean = (FileInfoBean) bean;
			fileName = fiBean.getFileName() + "." + fiBean.getFormatName();
			size = FileUtil.computeFileSize(fiBean.getFileSize());
		} else if (bean instanceof AdditiveFileBean) {
			AdditiveFileBean afBean = (AdditiveFileBean) bean;
			filePath = afBean.getUri();
			fileName = afBean.getName();
			size = afBean.getSize();
		}
		if (isPhoto) {
			Bitmap bitmap = ThumbnailUtils.extractThumbnail(
					BitmapFactory.decodeFile(filePath), 120, 120);
			icon_iv.setImageBitmap(bitmap);
		} else {
			icon_iv.setImageResource(FileUtil.getFileIcon(fileName));
		}
		name_tv.setText(fileName);
		progress_tv.setText(size);
	}

	public View getView() {
		return view;
	}

	public void delView() {
		// isDel = true;
		MailCreateActivity.mailCreate.attachmentsList.remove(bean);
	}

	/**
	 * 刷新上传进度
	 */
	// private Handler progressHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 0:
	// // pb.setProgress(msg.arg1);
	// // progress_tv.setText("已完成" + msg.arg1 + "%...");
	// progress_tv.setText("已完成（" + FileUtil.computeFileSize(msg.arg1)
	// + "/" + FileUtil.computeFileSize(msg.arg2) + "）...");
	// break;
	// case 1:
	// // name_tv.setTextColor(Color.GREEN);
	// pb.setVisibility(View.GONE);
	// progress_tv.setVisibility(View.GONE);
	// break;
	// case 2:
	// pb.setVisibility(View.GONE);
	// progress_tv.setVisibility(View.GONE);
	//
	// try {
	// Dialog dialog = new AlertDialog.Builder(context)
	// .setTitle("失败提示")
	// .setMessage("“" + file.getName() + "”上传失败！")
	// .setPositiveButton("重新上传",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(
	// DialogInterface dialog,
	// int which) {
	// startThread();
	// }
	// })
	// .setNegativeButton("取消上传",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(
	// DialogInterface dialog,
	// int which) {
	// delView();
	// MailCreateActivity.mailCreate
	// .deleteView(view);
	// }
	// }).create();
	// dialog.setCancelable(false);
	// dialog.show();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// break;
	// default:
	// break;
	// }
	// };
	// };

	/**
	 * HttpURLConnection POST上传文件
	 * 
	 * @param uploadUrl
	 * @param filename
	 * @throws Exception
	 */
	// @SuppressWarnings("resource")
	// private String httpUpload(String uploadUrl, File file) throws Exception {
	// String end = "\r\n";
	// String twoHyphens = "--";
	// String boundary = "******";
	// DataOutputStream dos = null;
	// FileInputStream fis = null;
	// String result = null;
	//
	// try {
	// URL url = new URL(uploadUrl);
	// HttpURLConnection httpURLConnection = HttpRequestServers
	// .getHttpURLConnection(url);
	//
	// httpURLConnection.setDoInput(true);
	// httpURLConnection.setDoOutput(true);
	// httpURLConnection.setUseCaches(true);
	// httpURLConnection.setRequestMethod("POST");
	// httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
	// httpURLConnection.setRequestProperty("Charset", "UTF-8");
	// httpURLConnection.setRequestProperty("Content-Type",
	// "multipart/form-data;boundary=" + boundary);
	//
	// String reqHeader = twoHyphens
	// + boundary
	// + end
	// + "Content-Disposition: form-data; name=\"upload\"; filename=\""
	// + file.getName() + "\"" + end
	// + "Content-Type: application/octet-stream" + end + end;
	// String reqEnder = end + twoHyphens + boundary + twoHyphens + end;
	//
	// long totalLength = file.length();
	// httpURLConnection.setFixedLengthStreamingMode(reqHeader.length()
	// + (int) (totalLength) + reqEnder.length());
	//
	// dos = new DataOutputStream(httpURLConnection.getOutputStream());
	// dos.writeBytes(reqHeader);
	// fis = new FileInputStream(file);
	// L.i(TAG, "httpUpload totalLength=" + totalLength);
	// long uploadSize = 0;
	// int progress = 0;
	// byte[] buffer = new byte[1024];
	// int count = 0;
	// while ((count = fis.read(buffer)) != -1) {
	// if (isDel) {
	// return null;
	// }
	// dos.write(buffer, 0, count);
	// uploadSize += count;
	// int i = (int) (uploadSize * 100 / totalLength);
	// if (i > progress) {
	// progress = i;
	// Message msg = new Message();
	// msg.what = 0;
	// // msg.arg1 = progress;
	// msg.arg1 = (int) uploadSize;
	// msg.arg2 = (int) totalLength;
	// progressHandler.sendMessage(msg);
	// }
	// }
	//
	// dos.writeBytes(reqEnder);
	// dos.flush();
	//
	// result = StreamUtil.readData(httpURLConnection.getInputStream());
	// L.d(TAG, "httpUpload result=" + result);
	//
	// Message msg = new Message();
	// msg.what = 1;
	// progressHandler.sendMessage(msg);
	// L.i(TAG, "httpUpload upload end...");
	// } finally {
	// if (fis != null)
	// try {
	// fis.close();
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	// if (dos != null)
	// try {
	// dos.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return result;
	// }
}

package com.datacomo.mc.spider.android;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.service.DownLoadCloudFileThread;
import com.datacomo.mc.spider.android.util.ConstantUtil;

public class ReLoadingActivity extends BaseActivity {

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(R.drawable.nothing);

		Intent intent = getIntent();
		boolean isDownLoad = intent.getBooleanExtra("IsDownLoad", true);

		if (isDownLoad) {
			final String fileUrl = intent.getStringExtra("FileUrl");
			final long fileLength = intent.getLongExtra("FileLength", 0);
			final String fileName = intent.getStringExtra("FileName");
			final long currentSize = intent.getLongExtra("currentSize", 0);
			if (fileUrl != null && !"".equals(fileUrl)) {
				AlertDialog dialog = new AlertDialog.Builder(
						new ContextThemeWrapper(this, R.style.AppBaseTheme))
						// Builder(this)
						.setTitle("失败提示")
						.setMessage("“" + fileName + "”下载失败！")
						.setPositiveButton("重新下载",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										new DownLoadCloudFileThread(
												ReLoadingActivity.this,
												fileUrl, fileLength, fileName,
												true, currentSize).start();
										finish();
									}
								})
						.setNegativeButton("取消下载",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								}).create();
				dialog.setCancelable(false);
				dialog.show();
			} else {
				finish();
			}
		} else {
			final String filePath = intent.getStringExtra("FilePath");
			if (filePath != null && !"".equals(filePath)) {
				final File file = new File(filePath);
				AlertDialog uDialog = new AlertDialog.Builder(
						new ContextThemeWrapper(this, R.style.AppBaseTheme))
						// Builder(this)
						.setTitle("失败提示")
						.setMessage("“" + file.getName() + "”上传失败！")
						.setPositiveButton("重新上传",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										ConstantUtil.uploadingList
												.add(filePath);
										APIRequestServers.uploadFile(
												ReLoadingActivity.this, file,
												UploadMethodEnum.UPLOADFILE,
												null);
										finish();
									}
								})
						.setNegativeButton("取消上传",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								}).create();
				uDialog.setCancelable(false);
				uDialog.show();
			}
		}
	}

}
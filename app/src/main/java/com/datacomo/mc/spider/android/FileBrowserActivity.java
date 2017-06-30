package com.datacomo.mc.spider.android;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FileBrowser;
import com.datacomo.mc.spider.android.view.OnFileBrowserListener;

/**
 * 文件浏览器
 */
public class FileBrowserActivity extends BaseActivity implements
		OnFileBrowserListener {
	// private final String TAG = "FileBrowserActivity";

	private boolean isCreateTopic = false;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filebrowser);

		FileBrowser fileBrowser = (FileBrowser) findViewById(R.id.filebrowser);
		fileBrowser.setOnFileBrowserListener(this);

		setTitle("当前目录：" + fileBrowser.getCurrentPath());

		Intent message = getIntent();
		// method = message.getStringExtra("method");
		// groupId = message.getStringExtra("groupId");
		// prompts = message.getStringExtra("prompts");
		isCreateTopic = message.getBooleanExtra("isCreateTopic", false);
	}

	public void onDirItemClick(String path) {
		setTitle("当前目录：" + path);
	}

	public void onFileItemClick(final String filename) {
		final File ifile = new File(filename);
		// 判断图片大小
		if (ifile.length() > 1024 * 1024 * 700) {
			T.show(FileBrowserActivity.this, "文件超过了700M,请重新选择");
			return;
		}
		if (isCreateTopic) {
			Intent intent = new Intent();
			intent.putExtra("filePath", ifile.getPath());
			setResult(RESULT_OK, intent);
			LogicUtil.finish(FileBrowserActivity.this);
			return;
		}
		/*
		 * else { new AlertDialog.Builder(this) .setTitle("提示")
		 * .setMessage("确定上传 " + ifile.getName() + " 文件吗？")
		 * .setPositiveButton("确认", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int which) {
		 * uploadFile(ifile); finish(); } }).setNegativeButton("取消",
		 * null).show(); }
		 */
	}

	/**
	 * 上传文件
	 * 
	 * @param intent
	 * @param groupId
	 * @param pageUrl
	 * @param prompts
	 * @param method
	 */
	/*
	 * private void startUploadFile(String groupId, String prompts, String
	 * method, String filepath) { L.d(TAG, "startUploadFile groupId=" + groupId
	 * + ",prompts=" + prompts + ",method=" + method + ",filepath=" + filepath);
	 * Intent intent = new Intent(this, UploadFileService.class); if (groupId !=
	 * null) { new UploadFileThread(this, new File(filepath), method, groupId)
	 * .start(); } else { intent.putExtra("notification_id", (int)
	 * System.currentTimeMillis()); intent.putExtra("method", method);
	 * intent.putExtra("path", filepath); intent.putExtra("prompts", prompts);
	 * startService(intent); } }
	 */
}

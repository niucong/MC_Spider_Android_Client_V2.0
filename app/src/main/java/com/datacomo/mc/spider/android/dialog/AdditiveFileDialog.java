package com.datacomo.mc.spider.android.dialog;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;

import com.datacomo.mc.spider.android.CloudFileChooseActivity;
import com.datacomo.mc.spider.android.FileBrowserActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;

/**
 * 创建新文件类
 */
public class AdditiveFileDialog {
	private static final String TAG = "CreateFile";
	private Context context;
	private final String[] ITEMS = { "视频", "音频", "云文件", "其它" };// , "文本文件"
	public static final int VIDEO = 1, AUDIO = 2, CLOUDFILE = 3,
			OTHAR = 4;// TEXT
	// =
	// 3,
	private List<Object> mFiles;

	// public List<Object> getFiles() {
	// return mFiles;
	// }

	public void setFiles(List<Object> files) {
		mFiles = files;
	}

	public AdditiveFileDialog(Context context) {
		this.context = context;
	}

	/**
	 * 创建对话框
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle("选择上传文件类型")
				// .setItems(ITEMS, new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(context, R.layout.choice_item, ITEMS),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								L.d(TAG, "which:" + which + 1);
								createFileType(which + 1);
							}
						});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * 创建类型
	 * 
	 * @param which
	 */
	private void createFileType(int which) {
		switch (which) {
		case VIDEO:// 视频
			createVideo();
			break;
		case AUDIO:// 音频
			createAudio();
			break;
		// case TEXT:// 文本文件
		// createText();
		// break;
		case CLOUDFILE:// 云文件
			createCloudFile(mFiles);
			break;
		case OTHAR:// 其它类型
			Intent intent = new Intent(context, FileBrowserActivity.class);
			intent.putExtra("isCreateTopic", true);
			((Activity) context).startActivityForResult(intent, OTHAR);
			break;
		default:
			break;
		}
	}

	/**
	 * 创建视频
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createVideo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle("上传视频")
				// .setItems(new String[] { "本地视频", "新视频", "取消" },
				// new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(context, R.layout.choice_item,
								new String[] { "本地视频", "新视频" }),// "取消"
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Intent intent = new Intent(
											Intent.ACTION_GET_CONTENT);
									intent.setType("video/*");
									((Activity) context)
											.startActivityForResult(Intent
													.createChooser(intent,
															"选择视频"), VIDEO);
									break;
								case 1:
									Intent vIntent = new Intent(
											MediaStore.ACTION_VIDEO_CAPTURE);
									((Activity) context)
											.startActivityForResult(vIntent,
													VIDEO);
									break;
								// case 2:
								// dialog.dismiss();
								// break;
								}
							}
						});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * 创建音频
	 */
	private void createAudio() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/*");
		((Activity) context).startActivityForResult(
				Intent.createChooser(intent, "选择音频"), AUDIO);
	}

	// /**
	// * 创建文本文件
	// */
	// private void createText() {
	// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	// intent.setType("text/*");
	// ((Activity) context).startActivityForResult(
	// Intent.createChooser(intent, "选择文本文件"), TEXT);
	// }

	/**
	 * 创建文本文件
	 */
	private void createCloudFile(List<Object> chooseds) {
		Intent intent = new Intent(context, CloudFileChooseActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleKey.CHOOSEDS, (Serializable) chooseds);
		intent.putExtras(bundle);
		((Activity) context).startActivityForResult(intent, CLOUDFILE);
	}
}

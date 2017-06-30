package com.datacomo.mc.spider.android.dialog;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;

import com.datacomo.mc.spider.android.FileBrowserActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.ConstantUtil;

/**
 * 创建新文件类
 */
public class CreateFile {
	// private static final String TAG = "CreateFile";
	private Context context;
	private final String[] ITEMS = { "图片", "视频", "音频", "其它" };// , "文本文件"
	public static final int IMAGE = 0, VIDEO = 1, AUDIO = 2, OTHAR = 3;// TEXT =
																		// 3,

	public String pictureName;

	private boolean onlyPhoto;

	public CreateFile(Context context) {
		this.context = context;
	}

	public CreateFile(Context context, boolean onlyPhoto) {
		this.context = context;
		this.onlyPhoto = onlyPhoto;
	}

	/**
	 * 创建对话框
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createDialog() {
		if (onlyPhoto) {
			createImage();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(context)
					.setTitle("选择上传文件类型")
					// .setItems(ITEMS, new DialogInterface.OnClickListener() {
					.setAdapter(
							new ArrayAdapter(context, R.layout.choice_item,
									ITEMS),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									createFileType(which);
								}
							});
			AlertDialog ad = builder.create();
			ad.setCanceledOnTouchOutside(true);
			ad.show();
		}
	}

	/**
	 * 创建类型
	 * 
	 * @param which
	 */
	private void createFileType(int which) {
		switch (which) {
		case IMAGE:// 图片
			createImage();
			break;
		case VIDEO:// 视频
			createVideo();
			break;
		case AUDIO:// 音频
			createAudio();
			break;
		// case TEXT:// 文本文件
		// createText();
		// break;
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
	 * 创建图片
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createImage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle("上传图片")
				// .setItems(new String[] { "本地上传", "拍照上传", "取消上传" },
				// new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(context, R.layout.choice_item,
								new String[] { "本地上传", "拍照上传" }),// , "取消上传"
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:// 本地上传
									getLocalImg();
									break;
								case 1:// 拍照上传
										// Intent mIntent = new Intent(
										// MediaStore.ACTION_IMAGE_CAPTURE);
										// ((Activity) context)
										// .startActivityForResult(mIntent,
										// IMAGE);
									takePhoto();
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

	public void getLocalImg() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		((Activity) context).startActivityForResult(
				Intent.createChooser(intent, "选择图片"), IMAGE);
	}

	public void takePhoto() {
		Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		pictureName = System.currentTimeMillis() + ".jpg";
		File saveFile = new File(ConstantUtil.CAMERA_PATH);
		if (!saveFile.exists()) {
			saveFile.mkdirs();
		}
		intent1.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(saveFile, pictureName)));
		((Activity) context).startActivityForResult(intent1, IMAGE);
	}

	/**
	 * 创建视频
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createVideo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle("上传视频")
				// .setItems(new String[] { "本地视频", "新视频", "取消" },
				// new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(context, R.layout.choice_item,
								new String[] { "本地视频", "新视频", }),// "取消"
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
		// Intent intent = new
		// Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		// startActivityForResult(intent, 3);

		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// intent.setType("audio/amr");
		// intent.setClassName("com.android.soundrecorder",
		// "com.android.soundrecorder.SoundRecorder");
		// startActivityForResult(intent, 3);

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
}

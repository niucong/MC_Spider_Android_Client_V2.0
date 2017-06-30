package com.datacomo.mc.spider.android.view;

import java.io.File;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.FileUtil;

public class DialogView extends FrameLayout {
	private TextView filesInfo, fileNow, fileWhich, proPercent;
	private ImageView fileIcon;
	private ProgressBar pBar, dialogBar;
	private View progress;

	public DialogView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context c) {
		View v = LayoutInflater.from(c).inflate(R.layout.dialog_publish, null);
		addView(v);
		filesInfo = (TextView) v.findViewById(R.id.file_info);
		fileNow = (TextView) v.findViewById(R.id.file_now);
		fileWhich = (TextView) v.findViewById(R.id.file_which);
		proPercent = (TextView) v.findViewById(R.id.progress_percent);
		fileIcon = (ImageView) v.findViewById(R.id.file_icon);
		dialogBar = (ProgressBar) v.findViewById(R.id.dialog_progress);
		pBar = (ProgressBar) v.findViewById(R.id.progress_bar);
		progress = v.findViewById(R.id.progress);
		fileNow.setText("上传中...");
	}

	public void updatePBar(int progress) {
		pBar.setProgress(progress);
		proPercent.setText(progress + "%");
	}

	public void setAllFileInfo(String info) {
		if (null == info) {
			filesInfo.setVisibility(View.GONE);
		} else {
			filesInfo.setVisibility(View.VISIBLE);
			filesInfo.setText(info);
		}
	}

	public void finishFile(String infoNow) {
		fileNow.setText(infoNow);
	}

	public void startNewFile(String infoWhich) {
		fileWhich.setText(infoWhich);
		updatePBar(0);
	}

	public void setPublishProgress(int showState) {
		progress.setVisibility(showState);
	}

	public void showDialogBar() {
		dialogBar.setVisibility(View.VISIBLE);
	}

	public void setIcon(String url) {
		MyFinalBitmap.setLocalAndDisPlayImage(getContext(), fileIcon, url);
	}

	public void setFileIcon(File f) {
		fileIcon.setImageResource(FileUtil.getFileIcon(f.getName()));
	}
}

package com.datacomo.mc.spider.android.view;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CreateGroupTopicActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.FileUtil;

public class FileView {
	private static final String TAG = "FileView";

	private View view;
	private ImageView icon_iv, del_iv, line_tv;
	private TextView name_tv, progress_tv;
	private ProgressBar pb;

	private LayoutInflater inflater;

	public String temps = null;

	public boolean isPhoto;
	private File file;

	public FileView(Context context, boolean isPhoto, File file) {
		this.isPhoto = isPhoto;
		this.file = file;
		inflater = LayoutInflater.from(context);
		findView();
		setView();
	}

	private void findView() {
		view = inflater.inflate(R.layout.file_item, null);
		icon_iv = (ImageView) view.findViewById(R.id.file_item_icon);
		name_tv = (TextView) view.findViewById(R.id.file_item_name);
		del_iv = (ImageView) view.findViewById(R.id.file_item_del);
		pb = (ProgressBar) view.findViewById(R.id.file_item_progressBar);
		pb.getBackground().setAlpha(128);
		progress_tv = (TextView) view.findViewById(R.id.file_item_progressTv);
		line_tv = (ImageView) view.findViewById(R.id.file_item_line);
	}

	private void setView() {
		String fileName = file.getName();
		if (isPhoto) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			options.inJustDecodeBounds = false;
			L.i(TAG, "setView options.outHeight=" + options.outHeight
					+ ",options.outWidth=" + options.outWidth
					+ ",options.inSampleSize=" + options.inSampleSize);
			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
			icon_iv.setImageBitmap(bitmap);

			name_tv.setVisibility(View.GONE);
			progress_tv.setVisibility(View.GONE);
			line_tv.setVisibility(View.GONE);
			del_iv.setVisibility(View.GONE);
		} else {
			icon_iv.setImageResource(FileUtil.getFileIcon(fileName));
			progress_tv.setVisibility(View.GONE);
		}
		name_tv.setText(fileName);
	}

	public View getView() {
		return view;
	}

}

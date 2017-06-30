package com.datacomo.mc.spider.android.view;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.net.APIMailRequestServers;
import com.datacomo.mc.spider.android.net.been.AttachMent;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.T;

public class MailFile extends LinearLayout {
	private final String TAG = "MailFile";

	private Context c;
	private TextView name, size;// , open, share;
	private ImageView icon, arraw;
	private AttachMent f;
	private LinearLayout operate;
	private boolean isOpen;

	private SpinnerProgressDialog spdDialog;

	public MailFile(Context context, AttachMent file) {
		super(context);
		c = context;
		spdDialog = new SpinnerProgressDialog(c);
		f = file;
		init();
	}

	private void init() {
		LinearLayout ll = (LinearLayout) LayoutInflater.from(c).inflate(
				R.layout.item_mail_file, null);
		name = (TextView) ll.findViewById(R.id.name);
		size = (TextView) ll.findViewById(R.id.size);
		icon = (ImageView) ll.findViewById(R.id.icon);
		arraw = (ImageView) ll.findViewById(R.id.arraw);
		// open = (TextView) ll.findViewById(R.id.open);
		// share = (TextView) ll.findViewById(R.id.share);
		operate = (LinearLayout) ll.findViewById(R.id.operate);
		final String nameStr = f.getName();
		L.d(TAG, "init nameStr=" + nameStr);
		name.setText(nameStr);
		size.setText(FileUtil.computeFileSize(f.getSize()));
		icon.setImageResource(FileUtil.getFileIcon(nameStr));
		setTag(f);

		name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openFile();
			}
		});

		arraw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showOperDialog(nameStr);
			}
		});
		addView(ll);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showOperDialog(String nameStr) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c).setTitle(nameStr)
		// .setItems(new String[] { "打开", "转存到云文件", "取消" },
		// new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(c, R.layout.choice_item, new String[] {
								"打开", "转存到云文件", }),// "取消"
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									openFile();
									break;
								case 1:
									shareFile();
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
	 * 打开
	 */
	private void openFile() {
		String fileUrl = f.getAttachmentUrl() + f.getPath();
		final String tempName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		File myFile = new File(ConstantUtil.CLOUD_PATH + tempName);
		if (myFile != null && myFile.exists()) {
			new FileUtil().openFile(c, myFile);
			return;
		} else if (ConstantUtil.downloadingList.contains(tempName)) {
			T.show(c, R.string.downloading);
			return;
		}

		ConstantUtil.downloadingList.add(tempName);
		new DownLoadFileThread(c, fileUrl, f.getSize(), f.getName(), true)
				.start();
	}

	/**
	 * 转存到云文件
	 */
	private void shareFile() {
		spdDialog.showProgressDialog("正在处理中...");
		new SaveAttachMentTask(c, f.getPath(), f.getName(), f.getSize() + "")
				.execute();
	}

	public void openOrClose() {
		if (isOpen) {
			isOpen = false;
			arraw.setImageResource(R.drawable.arrow_rights);
			operate.setVisibility(View.GONE);
		} else {
			isOpen = true;
			arraw.setImageResource(R.drawable.arrow_down);
			operate.setVisibility(View.VISIBLE);
		}
	}

	public void open() {
		if (!isOpen) {
			isOpen = true;
			operate.setVisibility(View.VISIBLE);
		}
	}

	public void close() {
		if (isOpen) {
			isOpen = false;
			operate.setVisibility(View.GONE);
		}
	}

	public boolean isOpen() {
		return isOpen;
	}

	class SaveAttachMentTask extends AsyncTask<String, Integer, MCResult> {
		private Context mContext;
		private String filePath, fileName, fileSize;

		public SaveAttachMentTask(Context context, String filePath,
				String fileName, String fileSize) {
			mContext = context;
			this.filePath = filePath;
			this.fileName = fileName;
			this.fileSize = fileSize;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIMailRequestServers.transferAttachment(mContext,
						filePath, fileName, fileSize);
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null != mcResult) {
				if (mcResult.getResultCode() == 1) {
					T.dialog(c, "已转存！");
				} else {
					T.show(c, T.ErrStr);
				}
			} else {
				T.show(c, T.ErrStr);
			}
		}
	}

}
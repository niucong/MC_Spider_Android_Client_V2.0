package com.datacomo.mc.spider.android.dialog;

import android.app.ProgressDialog;
import android.content.Context;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.T;

public class SpinnerProgressDialog {

	private Context context;
	private ProgressDialog pd;

	public SpinnerProgressDialog(Context context) {
		this.context = context;
	}

	/**
	 * 加载中对话框
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public void showProgressDialog(String msg) {
		try {
			if (pd == null) {
				pd = new ProgressDialog(context);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// pd.setIndeterminateDrawable(context.getResources().getDrawable(
				// R.drawable.drawable_progress));indeterminateProgressStyle
			}
			pd.setMessage(msg);
			if (!pd.isShowing()) {
				pd.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProgressDialog getPd() {
		return pd;
	}

	/**
	 * 提示
	 * 
	 * @param msg
	 */
	public void cancelProgressDialog(String msg) {
		try {
			if (pd != null && pd.isShowing()) {
				pd.cancel();
			}
			if (msg != null && !msg.equals("")) {
				T.show(context, msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

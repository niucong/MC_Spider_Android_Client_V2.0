package com.datacomo.mc.spider.android.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.datacomo.mc.spider.android.R;

public class ShowToast extends Toast {

	private static ShowToast instance = null;
	private View layout;
	private TextView text;

	private ShowToast(Context context) {
		super(context);
		init(context);
	}

	public static ShowToast getToast(Context context) {
		if (instance == null) {
			instance = new ShowToast(context);
		}
		return instance;
	}

	private void init(Context context) {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflate.inflate(R.layout.transient_notification, null);
		text = (TextView) layout.findViewById(R.id.message);
		setGravity(Gravity.CENTER, 0, 0);
		this.setView(layout);
	}

	public void show(String msg) {
		text.setText(msg);
		this.setDuration(Toast.LENGTH_LONG);
		this.show();
	}
}

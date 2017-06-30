package com.datacomo.mc.spider.android.fragmt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseGroupFragment extends Fragment implements
		OnClickListener {

	public BaseGroupFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public void onFragmentRefresh(FragmentActivity fContext) {
	}

	@Override
	public void onClick(View v) {

	};

	public abstract void search(String s);
}

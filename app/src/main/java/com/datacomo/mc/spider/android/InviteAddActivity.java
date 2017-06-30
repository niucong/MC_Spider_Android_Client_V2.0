package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.ContactsListAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ContactInfo;
import com.datacomo.mc.spider.android.db.ContactsBookService;
import com.datacomo.mc.spider.android.url.L;

public abstract class InviteAddActivity extends BasicActionBarActivity implements
		OnClickListener, OnCheckedChangeListener, OnItemClickListener {
	private static final String TAG = "InviteContactActivity";

	protected Button invite_btn, cancel_btn;
	private ListView listView;

	private TextView head_num, num_tv;
	private CheckBox head_cb;

	protected ContactsListAdapter adapter;
	private ArrayList<ContactInfo> contactInfos;

	protected ContactsBookService bookService;

	private final String source = "<span>" + "您的手机联系人在&nbsp;"
			+ "<b>优优工作圈</b>&nbsp;中：<br />已有&nbsp;"
			+ "<font color=#d50101>%d</font>&nbsp;位加入&nbsp;"
			+ "<font color=#d50101>%d</font>&nbsp;位未加入</span>";

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.guide_invite);

		findView();
		setView();

		bookService = new ContactsBookService(this);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void findView() {
		num_tv = (TextView) findViewById(R.id.guide_invite_num);
		listView = (ListView) findViewById(R.id.guide_invite_listView);

		invite_btn = (Button) findViewById(R.id.guide_invite_confirm);
		cancel_btn = (Button) findViewById(R.id.guide_invite_cancel);

		head_num = (TextView) findViewById(R.id.contacts_item_header_num);
		head_cb = (CheckBox) findViewById(R.id.contacts_item_header_checkBox);
	}

	private void setView() {
		num_tv.setText(Html.fromHtml(String.format(source, 0, 0)));

		invite_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);

		head_cb.setOnCheckedChangeListener(this);
		listView.setOnItemClickListener(this);
	}

	protected void init() {
		contactInfos = bookService.getContacts("2");
		int addNum = bookService.getCount("1");
		int allNum = bookService.getCount("2");
		L.i(TAG, "init addNum=" + addNum + ",allNum=" + allNum);
		num_tv.setText(Html.fromHtml(String.format(source, addNum, allNum)));
		setCheckedNum(0);

		if (contactInfos == null) {
			contactInfos = new ArrayList<ContactInfo>();
		}
		adapter = new ContactsListAdapter(this, contactInfos);
		listView.setAdapter(adapter);
	}

	private void setCheckedNum(int num) {
		head_num.setText("（" + num + "）");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		((CompoundButton) view.findViewById(R.id.contacts_item_checkBox))
				.toggle();
		if (!adapter.map.get(position)) {
			adapter.map.put(position, true);
		} else {
			adapter.map.put(position, false);
		}
		setCheckedNum(adapter.getCheckedSize());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		L.d(TAG, "onCheckedChanged isChecked=" + isChecked);
		head_cb.setChecked(isChecked);
		if (adapter != null) {
			setSelectAll(isChecked);
		}
	}

	/**
	 * 全选和全不选
	 * 
	 * @param flag
	 */
	private void setSelectAll(boolean flag) {
		// 让adapter里面的map全部为true;
		for (int i = 0; i < adapter.getCount(); i++) {
			adapter.map.put(i, flag);
		}
		listView.setAdapter(adapter);
		setCheckedNum(adapter.getCheckedSize());
	}
}

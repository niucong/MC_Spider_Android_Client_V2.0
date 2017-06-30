package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.ChooseGroupAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.bean.ContactInfo;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendGroupBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CheckNameUtil;
import com.datacomo.mc.spider.android.util.T;

public class ChooseGroupsDialogActivity extends BaseActivity implements
		OnClickListener {
	// static
	private static final String TAG_LOG = "ChooseGroupsDialog";
	private static final int ERRCODE = -2;
	public static final int CHOOSEGROUPOFFRIEND = 21;
	public static Object mCurrent;

	// variable
	private static boolean mIsNeedRefresh = false;
	private Context mContext;
	private static ContactInfo mContactInfo;
	private String[] mInitChosenIds;
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer, Integer> choose = new HashMap<Integer, Integer>();
	private static List<Object> mInitGroups = new ArrayList<Object>();
	private List<Object> mGroups = new ArrayList<Object>();
	// import class
	private ChooseGroupAdapter adapter;

	// view
	private ImageView img_CreateGroup;
	private TextView txt_Prompt;
	private TextView txt_Title;
	private ListView lv_Groups;
	private Button btn_Sure;
	private Button btn_Cancel;
	private LinearLayout llayout_DialogBox;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		setContentView(R.layout.dialog_choosegroups);
		initView();
		initData();
		bindListener();
	}

	private void initView() {
		llayout_DialogBox = (LinearLayout) findViewById(R.id.dialog_choosegroups_llayout_dialogbox);
		txt_Prompt = (TextView) findViewById(R.id.dialog_choosegroups_txt_prompt);
		lv_Groups = (ListView) findViewById(R.id.dialog_choosegroups_lv_groups);
		img_CreateGroup = (ImageView) findViewById(R.id.dialog_choosegroups_img_creategroup);
		txt_Title = (TextView) findViewById(R.id.dialog_choosegroups_txt_title);
		btn_Sure = (Button) findViewById(R.id.dialog_choosegroups_btn_sure);
		btn_Cancel = (Button) findViewById(R.id.dialog_choosegroups_btn_cancel);
	}

	private void initData() {
		Bundle bundle = getIntentMsg();
		ChooseGroupBean bean = null;
		if (null != bundle) {
			bean = (ChooseGroupBean) bundle
					.getSerializable(BundleKey.CHOOSEGROUPBEAN);
		}
		String name = "";
		String title = "";
		int lMemberId = 0;
		HashMap<String, Object> map_ChosenGroup = null;
		if (null != bean) {
			name = bean.getName();
			title = bean.getTitle();
			map_ChosenGroup = bean.getChosenGroupMap();
			lMemberId = bean.getMemberId();
		}
		if (isNeedRefresh()) {
			refresh(lMemberId);
		} else {
			mGroups.addAll(mInitGroups);
			llayout_DialogBox.setVisibility(View.VISIBLE);
		}
		txt_Title.setText(title);
		if ("调整朋友圈".equals(title))
			txt_Prompt.setText("调整 " + name + " 所属朋友圈");
		else
			txt_Prompt.setText("将 " + name + " 加到朋友圈");
		adapter = new ChooseGroupAdapter(mContext, mGroups);
		if (null == map_ChosenGroup)
			map_ChosenGroup = new HashMap<String, Object>();
		adapter.setChosen(map_ChosenGroup);
		mInitChosenIds = map_ChosenGroup.keySet().toArray(new String[0]);
		if (mInitChosenIds.length > 0)
			btn_Sure.setEnabled(true);
		lv_Groups.setAdapter(adapter);
	}

	private void bindListener() {
		lv_Groups.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FriendGroupBean bean = (FriendGroupBean) parent
						.getItemAtPosition(position);
				adapter.chooseChage(bean, view, position);
				if (adapter.getChosenIds().length > 0)
					btn_Sure.setEnabled(true);
				else
					btn_Sure.setEnabled(false);
			}
		});

		img_CreateGroup.setOnClickListener(this);
		btn_Cancel.setOnClickListener(this);
		btn_Sure.setOnClickListener(this);
	}

	private Bundle getIntentMsg() {
		Intent intent = getIntent();
		Bundle bundle = null;
		if (null != intent) {
			bundle = intent.getExtras();
		}
		return bundle;
	}

	public static ContactInfo getContactInfo() {
		return mContactInfo;
	}

	public static void setContactInfo(ContactInfo contactInfo) {
		mContactInfo = contactInfo;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_choosegroups_img_creategroup:
			showGreateGroupDialog();
			break;
		case R.id.dialog_choosegroups_btn_sure:
			choose.clear();
			HashMap<String, Object> chosenGroups = adapter.getChosen();
			String[] chosenIds = adapter.getChosenIds();
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			ChooseGroupBean bean = (ChooseGroupBean) bundle
					.getSerializable(BundleKey.CHOOSEGROUPBEAN);
			bean.setChosenGroupMap(chosenGroups);
			bundle.putStringArray(BundleKey.CHOOSEDS, chosenIds);
			bundle.putSerializable(BundleKey.CHOOSEGROUPBEAN, bean);
			intent.putExtras(bundle);
			if (isNoChange(chosenIds, chosenGroups)) {
				setResult(RESULT_CANCELED, intent);
			} else {
				setResult(RESULT_OK, intent);
			}
			finish();
			break;
		case R.id.dialog_choosegroups_btn_cancel:
			cancelChoose();
			Bundle cBundle = new Bundle();
			ChooseGroupBean cBean = new ChooseGroupBean();
			cBean.setChosenGroupMap(adapter.getChosen());
			cBundle.putStringArray(BundleKey.CHOOSEDS, adapter.getChosenIds());
			cBundle.putSerializable(BundleKey.CHOOSEGROUPBEAN, cBean);
			Intent cIntent = getIntent();
			cIntent.putExtras(cBundle);
			setResult(RESULT_CANCELED, cIntent);
			finish();
			break;
		}
	}

	private void cancelChoose() {
		if (!choose.isEmpty()) {
			for (Iterator<?> iterator = choose.keySet().iterator(); iterator
					.hasNext();) {
				int k = (Integer) iterator.next();
				int i = choose.get(k);
				if (i != 0) {
					FriendGroupBean fgb = (FriendGroupBean) mInitGroups.get(k);
					fgb.setFriendNum(fgb.getFriendNum() + i);
					mInitGroups.remove(k);
					mInitGroups.add(k, fgb);
				}
			}
			choose.clear();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			cancelChoose();
		}
		return super.onKeyDown(keyCode, event);
	}

	public static List<Object> getGroups() {
		return mInitGroups;
	}

	public static void setGroups(List<Object> groups) {
		mInitGroups = groups;
	}

	public static boolean isNeedRefresh() {
		if (mInitGroups.size() == 0) {
			mIsNeedRefresh = true;
		}
		return mIsNeedRefresh;
	}

	public static void setIsNeedRefresh(boolean isNeedRefresh) {
		mIsNeedRefresh = isNeedRefresh;
	}

	public void refresh(int amemberId) {
		spdDialog.showProgressDialog("正在加载中...");
		new RequestTask(mContext).execute(1, amemberId);
	}

	/**
	 * show this dialog to create group
	 */
	private void showGreateGroupDialog() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final EditText txt_Name = (EditText) inflater.inflate(
				R.layout.form_creategroup, null);
		Builder mBuilder = new Builder(new ContextThemeWrapper(
				this, R.style.AppBaseTheme));
		mBuilder.setTitle("创建朋友圈");
		mBuilder.setView(txt_Name);
		mBuilder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// hideKey();
				String name = txt_Name.getText().toString();
				if (CheckNameUtil.checkFriendName(name)) {
					createGroup(name);
				} else {
					showTip("请输入规范的名字！");
				}
			}
		});
		mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// hideKey();
			}
		});
		mBuilder.create();
		mBuilder.show();
	}

	/**
	 * create group
	 */
	private void createGroup(String name) {
		spdDialog.showProgressDialog("正在加载中...");
		new RequestTask(mContext).execute(0, name);
	}

	/**
	 * request
	 * 
	 * @author no 287
	 * 
	 */
	class RequestTask extends AsyncTask<Object, Integer, MCResult[]> {
		private Context mContext;
		private Object[] mParams;

		public RequestTask(Context context) {
			mContext = context;
		}

		@Override
		protected MCResult[] doInBackground(Object... params) {
			MCResult mcResult[] = new MCResult[2];
			mParams = params;
			try {
				switch ((Integer) mParams[0]) {
				case 0: // 创建圈子
					mcResult[0] = APIRequestServers.createFriendGroup(mContext,
							(String) mParams[1], new String[] { "" });
					break;
				case 1:// 朋友圈列表
					mcResult[0] = APIRequestServers.friendGroupList(mContext);
					if ((Integer) mParams[1] != 0) {
						mcResult[1] = APIRequestServers.getMyFriendGroupList(
								mContext, (Integer) mParams[1]);
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult mcResult[]) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			switch ((Integer) mParams[0]) {
			case 0:
				if (null != mcResult[0]) {
					if (1 == mcResult[0].getResultCode()) {
						refresh(mcResult[0], (String) mParams[1]);
					} else
						showTip(T.ErrStr);
				} else {
					showTip(T.ErrStr);
				}
				break;
			case 1:
				if ((Integer) mParams[1] != 0) {
					if (null != mcResult[0] && null != mcResult[1]) {
						if (1 == mcResult[0].getResultCode()
								&& 1 == mcResult[1].getResultCode()) {
							List<Object> groups = (List<Object>) mcResult[1]
									.getResult();
							if (null != groups && groups.size() > 0) {
								HashMap<String, Object> lMap_ChosenGroup = null;
								for (Object object : groups) {
									if (null == lMap_ChosenGroup) {
										lMap_ChosenGroup = new HashMap<String, Object>();
									}
									lMap_ChosenGroup.put(String
											.valueOf(((FriendGroupBean) object)
													.getGroupId()), object);
								}
								if (null != lMap_ChosenGroup) {
									mInitChosenIds = lMap_ChosenGroup.keySet()
											.toArray(new String[0]);
									btn_Sure.setEnabled(true);
									adapter.setChosen(lMap_ChosenGroup);
								}
							}
							groups = (List<Object>) mcResult[0].getResult();
							if (null != groups && groups.size() > 0) {
								mInitGroups.removeAll(mInitGroups);
								mInitGroups.addAll(groups);
								adapter.refreshAll(groups);
								llayout_DialogBox.setVisibility(View.VISIBLE);
								// rLayout_Bg.setBackgroundColor(R.color.transparent);
								mIsNeedRefresh = false;
							}
						} else {
							showTip(T.ErrStr);
							setResult(ERRCODE);
							finish();
						}
					} else {
						showTip(T.ErrStr);
						setResult(ERRCODE);
						finish();
					}
				} else {
					if (null != mcResult[0]) {
						if (1 == mcResult[0].getResultCode()) {
							List<Object> groups = (List<Object>) mcResult[0]
									.getResult();
							if (null != groups && groups.size() > 0) {
								mInitGroups.removeAll(mInitGroups);
								mInitGroups.addAll(groups);
								adapter.refreshAll(groups);
								llayout_DialogBox.setVisibility(View.VISIBLE);
								// rLayout_Bg.setBackgroundColor(R.color.transparent);
								mIsNeedRefresh = false;
							}
						} else {
							showTip(T.ErrStr);
							setResult(ERRCODE);
							finish();
						}
					} else {
						showTip(T.ErrStr);
						setResult(ERRCODE);
						finish();
					}
				}
				break;
			}
		}

	}

	private void refresh(MCResult mcResult, String name) {
		JSONObject result = (JSONObject) mcResult.getResult();
		L.d(TAG_LOG, "result: " + result.toString());
		try {
			int createResult = result.getInt("STATUS");
			if (createResult == 1) {
				mIsNeedRefresh = false;
				// showTip("创建成功");
				int id = result.getInt("GROUPID");
				FriendGroupBean bean = new FriendGroupBean();
				bean.setGroupId(id);
				bean.setGroupName(name);
				adapter.refresh(bean);
				mInitGroups.add(0, bean);
			} else if (createResult == 4) {
				showTip("该朋友圈名称已经存在");
			} else {
				showTip(T.ErrStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showTip(T.ErrStr);
		}
	}

	protected void showTip(String text) {
		T.show(mContext, text);
	}

	private Boolean isNoChange(String[] chosenIds,
			HashMap<String, Object> map_ChosenGroup) {
		boolean isNoChange = true;
		if (chosenIds.length != mInitChosenIds.length)
			return false;
		for (String id : mInitChosenIds) {
			if (!map_ChosenGroup.containsKey(id)) {
				L.d(TAG_LOG, "changed");
				isNoChange = false;
				break;
			}
		}
		return isNoChange;
	}

}

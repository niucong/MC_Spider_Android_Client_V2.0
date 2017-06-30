package com.datacomo.mc.spider.android;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.bean.ContactInfo;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendGroupBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CheckNameUtil;
import com.datacomo.mc.spider.android.util.ContactsUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;

public class InviteFriendActivity extends BasicActionBarActivity {
	private static final String TAG_LOG = "InviteFriendActivity";

	// variable
	// private String keyWord;
	private static ContactInfo mContactInfo;
	private Context mContext;
	private Object[] mMemberInfo;
	private HashMap<String, Object> mMap_ChosenGroup;
	private boolean AddGroup = false;
	private boolean mIsInit = true;
	private String groupId;
	private Type mType;

	// import class

	// view
	private EditText edit_Name;
	private RadioGroup rbtnGroup_sexBox;
	private TextView txt_Groups;
	private TextView txt_Phone;
	private LinearLayout lLayout_ChooseGroup;
	private LinearLayout lLayout_GroupBox;
	private LinearLayout llayout_SexBox;
	private LinearLayout llayout_Box;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_invitefriend);
		// setTitle("添加朋友", R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle("添加朋友");
		mContext = this;
		initView();
		initData();
		bindListener();
	}

	private void initView() {
		edit_Name = (EditText) findViewById(R.id.layout_invitefriend_edit_name);
		rbtnGroup_sexBox = (RadioGroup) findViewById(R.id.layout_invitefriend_rbtngroup_sexbox);
		txt_Groups = (TextView) findViewById(R.id.layout_invitefriend_txt_groups);
		txt_Phone = (TextView) findViewById(R.id.layout_invitefriend_txt_phone);
		lLayout_ChooseGroup = (LinearLayout) findViewById(R.id.layout_invitefriend_llayout_choosegroup);
		lLayout_GroupBox = (LinearLayout) findViewById(R.id.layout_invitefriend_llayout_groupbox);
		llayout_SexBox = (LinearLayout) findViewById(R.id.layout_invitefriend_llayout_sexbox);
		llayout_Box = (LinearLayout) findViewById(R.id.layout_invitefriend_llayout_box);
	}

	private void initData() {
		Bundle bundle = getIntentMag();
		mMemberInfo = new String[3];
		mMemberInfo[2] = "1";
		if (null != bundle) {
			mMemberInfo[1] = bundle.getString(BundleKey.PHONE);
			mMemberInfo[0] = bundle.getString(BundleKey.NAME);
			AddGroup = bundle.getBoolean(BundleKey.ADDGROUP, false);
			if (AddGroup) {
				groupId = bundle.getString(BundleKey.ID_GROUP);
			}
			mType = (Type) bundle.getSerializable(BundleKey.TYPE_REQUEST);
			if (mType == Type.ADDFRIENDTOGROUP) {
				String friendGroupId = bundle.getString(BundleKey.ID_GROUP);
				L.d(TAG_LOG, "groupId:" + friendGroupId);
				mMap_ChosenGroup = new HashMap<String, Object>();
				mMap_ChosenGroup.put(friendGroupId, null);
				lLayout_GroupBox.setVisibility(View.GONE);
				llayout_SexBox
						.setBackgroundResource(R.drawable.bg_threeline_03);
				llayout_Box.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, 100));
			}
			txt_Phone.setText((String) mMemberInfo[1]);
			if (null != mMemberInfo[0]) {
				String str = (String) mMemberInfo[0];
				edit_Name.setText(str);
				edit_Name.setSelection(str.length());
			} else {
				new RequestTask(mContext).execute(0, mMemberInfo[1]);
			}
			txt_Groups.setText("关注对象");
		}
	}

	private void bindListener() {
		lLayout_ChooseGroup.setOnClickListener(this);
		rbtnGroup_sexBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.layout_invitefriend_sexbox_rbtn_man:
							mMemberInfo[2] = "1";
							break;
						case R.id.layout_invitefriend_sexbox_rbtn_woman:
							mMemberInfo[2] = "2";
							break;
						}
					}
				});
		edit_Name.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					BaseData.hideKeyBoard(InviteFriendActivity.this);
				}
			}
		});
	}

	private Bundle getIntentMag() {
		Intent intent = getIntent();
		Bundle bundle = null;
		if (null != intent)
			bundle = intent.getExtras();
		return bundle;

	}

	public static ContactInfo getContactInfo() {
		return mContactInfo;
	}

	public static void setContactInfo(ContactInfo contactInfo) {
		mContactInfo = contactInfo;
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_send).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			finish();
			return true;
		case R.id.action_send:
			String name = edit_Name.getText().toString();
			if (!CheckNameUtil.checkMemberName(name)) {
				showTip("请输入规范的名字！");
			} else {
				mMemberInfo[0] = name;
				String[] names = null;
				if (null != mMap_ChosenGroup)
					names = mMap_ChosenGroup.keySet().toArray(new String[0]);
				if (null != names && names.length == 0)
					names = null;
				spdDialog.showProgressDialog("正在添加中...");
				new RequestTask(mContext).execute(2, mMemberInfo, names);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// setResult(RESULT_CANCELED);
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// String name = edit_Name.getText().toString();
	// if (!CheckNameUtil.checkMemberName(name)) {
	// showTip("请输入规范的名字！");
	// } else {
	// mMemberInfo[0] = name;
	// String[] names = null;
	// if (null != mMap_ChosenGroup)
	// names = mMap_ChosenGroup.keySet().toArray(new String[0]);
	// if (null != names && names.length == 0)
	// names = null;
	// spdDialog.showProgressDialog("正在添加中...");
	// new RequestTask(mContext).execute(2, mMemberInfo, names);
	// }
	// }

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.layout_invitefriend_llayout_choosegroup:
			if (ChooseGroupsDialogActivity.isNeedRefresh()) {
				spdDialog.showProgressDialog("正在加载中...");
				new RequestTask(mContext).execute(1);
			} else {
				initGroup(ChooseGroupsDialogActivity.getGroups());
				String name = edit_Name.getText().toString();
				if ("".equals(name))
					name = (String) mMemberInfo[1];
				ChooseGroupBean bean = new ChooseGroupBean();
				bean.setName(new String[] { name }, (String) mMemberInfo[1]);
				bean.setTitle("加入朋友圈");
				bean.setChosenGroupMap(mMap_ChosenGroup);
				Bundle bundle = new Bundle();
				bundle.putSerializable("ChooseGroupBean", bean);
				LogicUtil.enter(mContext, ChooseGroupsDialogActivity.class,
						bundle, 1);
			}
			break;
		}
	}

	/**
	 * request
	 * 
	 * @author no 287
	 * 
	 */
	class RequestTask extends AsyncTask<Object, Integer, Object> {
		private Object[] mParams;
		private Context mContext;

		public RequestTask(Context context) {
			mContext = context;
		}

		@Override
		protected Object doInBackground(Object... params) {
			mParams = params;
			Object result = null;
			MCResult mcResult = null;
			switch ((Integer) mParams[0]) {
			case 0:
				String name = ContactsUtil.getName(mContext,
						(String) mParams[1]);
				result = name;
				break;
			case 1:
				try {
					mcResult = APIRequestServers.friendGroupList(mContext);
				} catch (Exception e) {
					e.printStackTrace();
				}
				result = mcResult;
				break;
			case 2:
				try {
					String[] memberInfo = (String[]) mParams[1];
					String data = memberInfo[0] + "," + memberInfo[1] + ","
							+ (String) memberInfo[2];
					mcResult = APIRequestServers.uploadPhoneBook(mContext,
							new String[] { data }, (String[]) mParams[2],
							groupId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				result = mcResult;
				break;
			}
			return result;
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			MCResult mcResult = null;
			switch ((Integer) mParams[0]) {
			case 0:
				mMemberInfo[0] = (String) result;
				if (!"".equals(result)) {
					// edit_Name.setHint((String) result);
					String str = (String) result;
					edit_Name.setText(str);
					edit_Name.setSelection(str.length());
				} else {
					edit_Name.setHint("朋友真实姓名");
				}
				break;
			case 1:
				spdDialog.cancelProgressDialog(null);
				mcResult = (MCResult) result;
				if (null != mcResult) {
					if (1 == mcResult.getResultCode()) {
						List<Object> groups = (List<Object>) mcResult
								.getResult();
						if (groups != null && groups.size() > 0) {
							ChooseGroupsDialogActivity.setIsNeedRefresh(false);
							ChooseGroupsDialogActivity.setGroups(groups);
							initGroup(groups);
							String name = edit_Name.getText().toString();
							if ("".equals(name))
								name = (String) mMemberInfo[1];
							ChooseGroupBean bean = new ChooseGroupBean();
							bean.setName(new String[] { name },
									(String) mMemberInfo[1]);
							bean.setTitle("加入朋友圈");
							bean.setChosenGroupMap(mMap_ChosenGroup);
							Bundle bundle = new Bundle();
							bundle.putSerializable("ChooseGroupBean", bean);
							LogicUtil
									.enter(mContext,
											ChooseGroupsDialogActivity.class,
											bundle, 1);
						}
					} else {
						showTip(T.ErrStr);
					}
				} else {
					showTip(T.ErrStr);
				}
				break;
			case 2:
				spdDialog.cancelProgressDialog(null);
				mcResult = (MCResult) result;
				if (null == mcResult || mcResult.getResultCode() != 1) {
					showTip(T.ErrStr);
					return;
				}
				String result1 = mcResult.getResult().toString();
				L.d(TAG_LOG, result1);
				// TODO手机号验证问题
				// 0：系统异常 1：可以发送邀请短信 2：手机号码错误：为空，或者不符合正则 3：visitId不存在
				// 4：手机号已绑定 5:friendGroupIdS不存在 6:groupId不合法
				// 7:visitId没有邀请权限 8:传递的性别参数不合法
				if ("1".equals(result1)) {
					ChooseGroupsDialogActivity.setIsNeedRefresh(true);
					new AlertDialog.Builder(mContext)
							.setTitle("已发出邀请短信！")
							.setMessage("邀请成功后，您将获得50圈币。TA若安装优优工作圈客户端，您将再获50圈币。")
							.setPositiveButton("知道了",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											onSuccess();
											setResult(RESULT_OK);
											finish();
										}
									}).setCancelable(false).show();
				} else if ("4".equals(result1)) {
					showTip("该号码已被添加过！");
					setResult(RESULT_OK);
					finish();
				} else {
					showTip(T.ErrStr);
				}
			default:
				break;
			}
		}
	}

	private void onSuccess() {
		if (AddGroup) {
			Bundle b = new Bundle();
			b.putInt(BundleKey.SIZE, 1);
			try {
				b.putInt(BundleKey.ID_GROUP, Integer.valueOf(groupId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			SimpleReceiver.sendBoardcast(this,
					SimpleReceiver.RECEIVER_ADD_MEMBER, b);
		}
	}

	private void initGroup(List<Object> groups) {
		if (!mIsInit)
			return;
		if (mIsInit)
			mIsInit = false;
		FriendGroupBean bean = (FriendGroupBean) groups.get(0);
		for (Object object : groups) {
			String temp = ((FriendGroupBean) object).getGroupName();
			if ("关注对象".equals(temp)) {
				bean = (FriendGroupBean) object;
				L.d(TAG_LOG, "guanzhuID" + bean.getGroupId());
				break;
			}
		}
		// String name = bean.getGroupName();
		int id = bean.getGroupId();
		// txt_Groups.setText(name);
		mMap_ChosenGroup = new HashMap<String, Object>();
		mMap_ChosenGroup.put(String.valueOf(id), bean);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			ChooseGroupBean bean = (ChooseGroupBean) bundle
					.getSerializable(BundleKey.CHOOSEGROUPBEAN);
			mMap_ChosenGroup = bean.getChosenGroupMap();
			String[] chosenIds = bundle.getStringArray(BundleKey.CHOOSEDS);
			String groups = "";
			String group = "";
			for (int i = 0; i < chosenIds.length; i++) {
				group = ((FriendGroupBean) mMap_ChosenGroup.get(chosenIds[i]))
						.getGroupName();
				if (i > 0)
					groups = groups.concat("，" + group);
				else
					groups = group;

			}
			if (!"".equals(groups))
				txt_Groups.setText(groups);
		}
	}

}

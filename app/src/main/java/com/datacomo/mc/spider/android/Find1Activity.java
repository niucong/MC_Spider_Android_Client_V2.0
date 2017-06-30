package com.datacomo.mc.spider.android;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.InviteRegisterBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.nostra13.universalimageloader.utils.L;

public class Find1Activity extends BasicActionBarActivity {
	private static final String TAG = "Find1Activity";

	// 变量
	private Type mType;
	private Context mContext;
	private String name;
	private String memberId;
	private String toastTitle = "";
	private String mId_Group;
	private HashMap<String, Object> mMap_ChosenGroup;

	// 组件
	private EditText edit_Key;
	private Button btn_Add;
	private TextView txt_Title;
	private TextView txt_Phone;
	private TextView txt_Name;
	private TextView txt_JoinTime;
	private Button btn_Group;
	private ImageView img_Head;
	private LinearLayout lLayout_ManageBox;
	private RelativeLayout rLayout_HeadBox;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_find1);
		// setTitle("添加朋友", R.drawable.title_fanhui, R.drawable.title_home);
		ab.setTitle("添加朋友");
		mContext = this;
		initView();
		initData();
	}

	private void initView() {
		edit_Key = (EditText) findViewById(R.id.edit);
		btn_Add = (Button) findViewById(R.id.layout_find1_btn_add);
		txt_Title = (TextView) findViewById(R.id.layout_find1_managebox_txt_title);
		txt_Phone = (TextView) findViewById(R.id.layout_find1_managebox_txt_phone);
		txt_Name = (TextView) findViewById(R.id.layout_find1_managebox_txt_name);
		btn_Group = (Button) findViewById(R.id.layout_find1_managebox_btn_group);
		txt_JoinTime = (TextView) findViewById(R.id.layout_find1_txt_jointime);
		img_Head = (ImageView) findViewById(R.id.head_img);
		rLayout_HeadBox = (RelativeLayout) findViewById(R.id.layout_find1_managebox_rlayout_headbox);
		lLayout_ManageBox = (LinearLayout) findViewById(R.id.layout_find1_llayout_managebox);
	}

	private void initData() {
		Bundle bundle = getBundle();
		if (null != bundle) {
			mType = (Type) bundle.getSerializable(BundleKey.TYPE_REQUEST);
		} else {
			return;
		}
		mType = (Type) bundle.getSerializable(BundleKey.TYPE_REQUEST);
		if (mType == Type.ADDFRIENDTOGROUP)
			mId_Group = bundle.getString(BundleKey.ID_GROUP);
		bindListener();
	}

	private Bundle getBundle() {
		Intent intent = getIntent();
		if (null != intent)
			return intent.getExtras();
		return null;

	}

	private void bindListener() {
		// btn_Add.setEnabled(false);
		btn_Add.setOnClickListener(this);
		btn_Group.setOnClickListener(this);
		rLayout_HeadBox.setOnClickListener(this);
		edit_Key.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (View.VISIBLE == lLayout_ManageBox.getVisibility()) {
					lLayout_ManageBox.setVisibility(View.GONE);
				}
				// btn_Add.setEnabled(s.length() == 11);
			}
		});
	}

	// @Override
	// protected void onLeftClick(View v) {
	// setResult(RESULT_CANCELED);
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// LogicUtil.enter(this, InfoWallActivity.class, null, true);
	// }

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * check get edit text
	 * 
	 * @param keyWord
	 * @return a boolean if null or "" return true else return false
	 */
	private boolean isEidtNull(String keyWord) {
		if (null == keyWord || "".equals(keyWord)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * get edit text
	 * 
	 * @return a string that key word
	 */
	private String getKeyWords() {
		return edit_Key.getText().toString();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.layout_find1_btn_add:
			String keyWord = getKeyWords();
			if (isEidtNull(keyWord)) {
				showTip("查询条件不能为空！");
			} else {
				if (isPhoneNumber(keyWord)) {
					BaseData.hideKeyBoard(this);
					findMember(keyWord);
				} else {
					showTip("查询条件为非法手机号");
				}
			}
			break;
		case R.id.layout_find1_managebox_rlayout_headbox:
			Bundle b = new Bundle();
			b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
			b.putString("id", v.getTag().toString());
			b.putString("name", name);
			LogicUtil.enter(this, HomePgActivity.class, b, false);
			break;
		case R.id.layout_find1_managebox_btn_group:
			showChooseGroupDialog(((TextView) v).getText().toString(), name);
			break;
		default:
			break;
		}
	}

	/**
	 * check key is phoneNumber
	 * 
	 * @param key
	 *            a string that ckecked
	 * @return a boolean if key is phoneNumber return true else return false
	 */
	private boolean isPhoneNumber(String key) {
		boolean isPhoneNumber = false;
		isPhoneNumber = CharUtil.isValidPhone(key);
		return isPhoneNumber;
	}

	/**
	 * find member by key
	 * 
	 * @param key
	 *            a string that ckecked
	 * @return if has return true else false
	 */
	private void findMember(String key) {
		spdDialog.showProgressDialog("正在添加中...");
		key = CharUtil.cleanIP(key);
		new RequestTask(mType, 0).execute(key);
	}

	public void showChooseGroupDialog(final String title, final String name) {
		ChooseGroupBean bean = new ChooseGroupBean();
		bean.setName(new String[] { name }, null);
		bean.setTitle(title);
		if ("调整朋友圈".equals(title)) {
			try {
				bean.setMemberId(Integer.parseInt(memberId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		bean.setChosenGroupMap(mMap_ChosenGroup);
		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleKey.CHOOSEGROUPBEAN, bean);
		LogicUtil.enter(mContext, ChooseGroupsDialogActivity.class, bundle, 1);
	}

	/**
	 * request
	 * 
	 * @author no 287
	 * 
	 */
	class RequestTask extends AsyncTask<Object, Integer, MCResult> {
		private Object[] mParams;
		private Type mType;
		private int miType;

		/**
		 * @param type
		 * @param itype
		 *            0 search Members ,1 add members to specified group
		 */
		public RequestTask(Type type, int itype) {
			mType = type;
			miType = itype;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			mParams = params;
			MCResult mcResult = null;
			switch (miType) {
			case 0:
				try {
					mcResult = APIRequestServers
							.ValidatePhoneForInviteRegister(App.app,
									(String) mParams[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					mcResult = APIRequestServers.addFriendsToGroup(App.app,
							(String[]) mParams[0], (String[]) mParams[1],
							"false");
					if (null != mcResult && mcResult.getResultCode() == 1) {
						mcResult = APIRequestServers
								.ValidatePhoneForInviteRegister(App.app,
										(String) mParams[2]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			return mcResult;
		}

		@SuppressWarnings({})
		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (mType == Type.ADDFRIEND)
				spdDialog.cancelProgressDialog(null);
			if (null == result || result.getResultCode() != 1) {
				spdDialog.cancelProgressDialog(null);
				T.show(App.app, T.ErrStr);
				return;
			}
			switch (miType) {
			case 0:
				ChooseGroupsDialogActivity.setIsNeedRefresh(true);
				@SuppressWarnings("unchecked")
				List<InviteRegisterBean> inviteRegisterBeans = (List<InviteRegisterBean>) result
						.getResult();
				try {
					InviteRegisterBean inviteRegisterBean = inviteRegisterBeans
							.get(0);
					String status = inviteRegisterBean.getSTATUS();
					int statu = 0;
					try {
						statu = Integer.parseInt(status);
					} catch (Exception e) {
						e.printStackTrace();
					}
					switch (statu) {
					case 1:
						if (mType == Type.ADDFRIENDTOGROUP)
							spdDialog.cancelProgressDialog(null);
						Bundle bundle = getBundle();
						bundle.putString(BundleKey.PHONE, (String) mParams[0]);
						LogicUtil.enter(mContext, InviteFriendActivity.class,
								bundle, 0);
						break;
					case 2:
					case 3:
						edit_Key.setText("");
						edit_Key.requestFocus();
						switch (mType) {
						case ADDFRIEND:
							if (statu == 2) {
								txt_Title.setText("TA已加入优优工作圈");
								btn_Group.setText("加到朋友圈");
								toastTitle = "加到朋友圈";
							} else {
								txt_Title.setText("TA已在您的朋友圈中");
								btn_Group.setText("调整朋友圈");
								toastTitle = "调整朋友圈";

							}
							String path_Head = inviteRegisterBean.getHEADURL()
									+ inviteRegisterBean.getHEADPATH();
							path_Head = ThumbnailImageUrl.getThumbnailHeadUrl(
									path_Head,
									HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
							MyFinalBitmap.setHeader(mContext, img_Head,
									path_Head);

							memberId = inviteRegisterBean.getMEMBERID();
							img_Head.setTag(memberId);
							rLayout_HeadBox.setTag(memberId);
							name = inviteRegisterBean.getMEMBERNAME();
							txt_Name.setText(name);
							txt_Phone.setText(inviteRegisterBean.getPHONE());
							lLayout_ManageBox.setVisibility(View.VISIBLE);
							mMap_ChosenGroup = new HashMap<String, Object>();
							break;
						case ADDFRIENDTOGROUP:
							new RequestTask(mType, 1).execute(
									new String[] { inviteRegisterBean
											.getMEMBERID() },
									new String[] { mId_Group },
									(String) mParams[0]);
							break;
						default:
							break;
						}
						break;
					case 4:
						if (mType == Type.ADDFRIENDTOGROUP)
							spdDialog.cancelProgressDialog(null);
						showTip("不能填写自己的手机号！");
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				T.show(App.app, "已添加！");
				@SuppressWarnings("unchecked")
				List<InviteRegisterBean> inviteRegisterBeans1 = (List<InviteRegisterBean>) result
						.getResult();
				InviteRegisterBean inviteRegisterBean1 = inviteRegisterBeans1
						.get(0);
				spdDialog.cancelProgressDialog(null);
				edit_Key.setText("");
				edit_Key.requestFocus();
				String path_Head = inviteRegisterBean1.getHEADURL()
						+ inviteRegisterBean1.getHEADPATH();
				path_Head = ThumbnailImageUrl.getThumbnailHeadUrl(path_Head,
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
				MyFinalBitmap.setHeader(mContext, img_Head, path_Head);
				memberId = String.valueOf(inviteRegisterBean1.getMEMBERID());
				img_Head.setTag(memberId);
				rLayout_HeadBox.setTag(memberId);
				name = inviteRegisterBean1.getMEMBERNAME();
				txt_Name.setText(name);
				txt_Phone.setText(inviteRegisterBean1.getPHONE());
				txt_JoinTime.setText(DateTimeUtil.aTimeFormat(DateTimeUtil
						.getLongTime(inviteRegisterBean1.getREGISTER_TIME()))
						+ "加入优优工作圈");
				txt_JoinTime.setVisibility(View.VISIBLE);
				txt_Title.setVisibility(View.GONE);
				btn_Group.setVisibility(View.GONE);
				lLayout_ManageBox.setVisibility(View.VISIBLE);
				mMap_ChosenGroup = new HashMap<String, Object>();
				break;
			}
		}
	}

	class AddFriendTask extends AsyncTask<String, Integer, MCResult> {
		private String[] groupIds;

		public AddFriendTask(String[] groupIds) {
			this.groupIds = groupIds;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.addFriendToGroup(mContext,
						params[0], groupIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult || mcResult.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
			}

			if ("调整朋友圈".equals(toastTitle)) {
				T.show(App.app, "已调整！");
			} else if ("加到朋友圈".equals(toastTitle)) {
				T.show(App.app, " 已添加！");
			}
			ChooseGroupsDialogActivity.setIsNeedRefresh(true);
			txt_Title.setText("TA已在您的朋友圈中");
			btn_Group.setText("调整朋友圈");
			toastTitle = "调整朋友圈";
			new Thread(new Runnable() {
				@Override
				public void run() {
					UpdateFriendListThread.updateFriendList(mContext, null);
				}
			}).start();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;
		if (requestCode == 1 && resultCode == RESULT_OK) {
			L.d(TAG, "progress");
			spdDialog.showProgressDialog("正在加载中...");
			Bundle bundle = data.getExtras();
			String[] chosenIds = bundle.getStringArray(BundleKey.CHOOSEDS);
			ChooseGroupBean bean = (ChooseGroupBean) bundle
					.getSerializable(BundleKey.CHOOSEGROUPBEAN);
			mMap_ChosenGroup = bean.getChosenGroupMap();
			new AddFriendTask(chosenIds).execute(memberId);
		}
		if (requestCode == 1 && resultCode == RESULT_CANCELED) {
			Bundle bundle = data.getExtras();
			ChooseGroupBean bean = (ChooseGroupBean) bundle
					.getSerializable("ChooseGroupBean");
			mMap_ChosenGroup = bean.getChosenGroupMap();
		}
	}

}

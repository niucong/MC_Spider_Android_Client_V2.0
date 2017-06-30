package com.datacomo.mc.spider.android;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.T;

public class PhoneActivity extends BasicActionBarActivity {
	// private final String TAG = "PhoneActivity";
	private EditText edit;
	private String[] phoneNumber;
	private String groupId;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_find_phone);
		// setTitle("查找朋友", R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle("查找朋友");
		phoneNumber = new String[1];
		Bundle bundle = getIntent().getExtras();

		groupId = bundle.getString("groupId");
		edit = (EditText) findViewById(R.id.edit);
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
		case R.id.action_send:
			spdDialog.showProgressDialog("正在加载中...");
			if (isEidtNull()) {
				showTip("手机号码不合法！");
			} else {
				phoneNumber[0] = getKeyWords();
				LoadData ld = new LoadData(phoneNumber);
				ld.execute();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// spdDialog.showProgressDialog("正在加载中...");
	// if (isEidtNull()) {
	// showTip("手机号码不合法！");
	// } else {
	// phoneNumber[0] = getKeyWords();
	// LoadData ld = new LoadData(phoneNumber);
	// ld.execute();
	// }
	// }

	class LoadData extends AsyncTask<Void, Void, MCResult> {
		private String[] phoneNumber;

		public LoadData(String[] phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		@Override
		protected MCResult doInBackground(Void... arg0) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.addLeaguer(PhoneActivity.this,
						groupId, null, phoneNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (result != null && result.getResultCode() == 1) {
				try {
					// {"UNREGIST_PHONE":[],"SUCCEED_PHONE":[1231729],"REGISTER_PHONE_LIST":[]}
					JSONObject person = new JSONObject(result.getResult()
							.toString());
					JSONArray sArray = null;
					try {
						sArray = person.getJSONArray("SUCCEED_PHONE");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (sArray != null && sArray.length() > 0) {
						onSuccess();
						showTip("邀请成功！");
						finish();
					} else {
						JSONArray rAarray = null;
						try {
							rAarray = person
									.getJSONArray("REGISTER_PHONE_LIST");
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (rAarray != null && rAarray.length() > 0) {
							showTip("该用户已是此圈子成员！");
							finish();
						} else {
							JSONArray uAarray = person
									.getJSONArray("UNREGIST_PHONE");
							if (uAarray != null && uAarray.length() > 0) {
								showTip("该用户还未注册！");
								Intent intent = new Intent(PhoneActivity.this,
										InviteFriendActivity.class);
								intent.putExtra(BundleKey.PHONE, phoneNumber[0]);
								intent.putExtra(BundleKey.ID_GROUP, groupId);
								intent.putExtra(BundleKey.ADDGROUP, true);
								startActivity(intent);
								finish();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					showTip(T.ErrStr);
				}
			} else {
				showTip(T.ErrStr);
			}
		}

	}

	private void onSuccess() {
		Bundle b = new Bundle();
		b.putInt(BundleKey.SIZE, 1);
		b.putInt(BundleKey.ID_GROUP, Integer.valueOf(groupId));
		SimpleReceiver.sendBoardcast(this, SimpleReceiver.RECEIVER_ADD_MEMBER,
				b);
	}

	private boolean isEidtNull() {
		String text = getKeyWords();
		if (null == text && "".equals(text)) {
			return true;
		} else {
			if (CharUtil.isValidPhone(text)) {
				return false;
			} else {
				return true;
			}
		}
	}

	private String getKeyWords() {
		return edit.getText().toString().trim();
	}
}

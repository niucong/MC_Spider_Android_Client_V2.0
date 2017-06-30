package com.datacomo.mc.spider.android;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.CheckNameUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;

public class GroupCreateActivity extends BasicActionBarActivity {
	private static final String TAG = "GroupCreateActivity";

	private EditText et_title, et_intro, et_tag;
	// private TextView intro;
	// private RadioGroup radios;
	private CreateGroupTask task;
	private boolean isCreating;
	// private String type = "1";

	private String title;

	// private Vibrator mVibrator = null;
	// private LocationClient mLocClient;

	/**
	 * 设置相关参数
	 */
	// private void setLocationOption() {
	// LocationClientOption option = new LocationClientOption();
	// option.setOpenGps(true); // 打开gps
	// option.setPoiExtraInfo(true);
	// option.setAddrType("all");
	// option.setPriority(LocationClientOption.NetWorkFirst);
	// option.setPoiNumber(10);
	// option.disableCache(true);
	// mLocClient.setLocOption(option);
	// }

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
		// mLocClient.stop();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_group_create);
		// setTitle("创建圈子", R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle("创建圈子");
		findViews();

		// mLocClient = App.app.mLocationClient;
		// mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		// App.app.mVibrator = mVibrator;
		// setLocationOption();
		// mLocClient.start();
	}

	private void findViews() {
		et_title = (EditText) findViewById(R.id.t_title);
		et_intro = (EditText) findViewById(R.id.t_content);
		et_tag = (EditText) findViewById(R.id.t_tag);
		// radios = (RadioGroup) findViewById(R.id.radioGroup);
		// radios.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// switch (checkedId) {
		// case R.id.radio0:
		// type = "1";
		// intro.setText("任何人可申请加入，任何人可浏览圈子各种信息，只有成员可参与圈子里的各种互动。");
		// break;
		// case R.id.radio1:
		// type = "2";
		// intro.setText("只有成员才能查看圈子里的各种信息。");
		// break;
		// }
		// }
		// });
		// intro = (TextView) findViewById(R.id.intro);
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
			if (isCreating) {
				return true;
			}
			title = et_title.getText().toString();
			if (!CheckNameUtil.checkGroupName(title)) {
				showTip("请输入规范的名字！");
				return true;
			}
			spdDialog.showProgressDialog("正在创建圈子，请稍后...");
			create();
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
	// if (isCreating) {
	// return;
	// }
	// title = et_title.getText().toString();
	// if (!CheckNameUtil.checkGroupName(title)) {
	// showTip("请输入规范的名字！");
	// return;
	// }
	// spdDialog.showProgressDialog("正在创建圈子，请稍后...");
	// create();
	// }

	private void create() {
		task = new CreateGroupTask();
		task.execute();
	}

	class CreateGroupTask extends AsyncTask<Void, Integer, MCResult> {

		@Override
		protected MCResult doInBackground(Void... params) {
			isCreating = true;
			MCResult mc = null;
			try {
				String intro = et_intro.getText().toString();
				String tag = et_tag.getText().toString();
				mc = APIRequestServers.createGroup(GroupCreateActivity.this,
						title, intro, tag, "2");// TODO type
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			// {"result":{"GROUP_HEAD_URL":"https://img.yuuquu.com","GROUP_ID":9322,"GROUP_HEAD_PATH":"/m6/default/group/group8.jpg","CREATE_RESULT":1},"resultStaus":true,"resultCode":1,"resultMessage":"","version":"v1.0"}
			if (null != result && result.getResultCode() == 1) {
				int i = 0, id = 0;
				JSONObject object = null;
				try {
					object = new JSONObject(result.getResult().toString());
					i = object.getInt("CREATE_RESULT");
					id = object.getInt("GROUP_ID");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (i == 1) {
					Bundle b = new Bundle();
					b.putString("Id", id + "");
					LogicUtil.enter(GroupCreateActivity.this,
							HomeGpActivity.class, b, true);

					L.i(TAG, "addLocation loc=" + App.app.loc + App.app.mlat
							+ App.app.mLon);
					if ((App.app.loc != null && !App.app.loc.equals(""))
							|| (App.app.mlat != 0.0 && App.app.mLon != 0.0)) {
						try {
							GroupListService.getService(
									GroupCreateActivity.this).saveLocation(
									object.getString("GROUP_ID"), App.app.loc,
									App.app.mlat, App.app.mLon);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (i == 4) {
					showTip("您不能创建两个重名的圈子！");
				}
			} else {
				showTip(T.ErrStr);
			}
			isCreating = false;
		}

	}

}

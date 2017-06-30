package com.datacomo.mc.spider.android;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.GroupAtlasEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupBasicBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapGroupBasicBean;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.CircleFamily;

public class CircleFamilyActivity extends BasicActionBarActivity {
	int superNum, JuniorNum, cpNum;
	String id, name, poster;
	CircleFamily family;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		family = new CircleFamily(this, BaseData.getScreenWidth());
//		setTitle("圈子图谱", R.drawable.title_fanhui, R.drawable.title_home);
		ab.setTitle("圈子图谱");
		setContent(family.getView());
		init();
	}

	private void init() {
		Bundle b = getIntent().getExtras();
		id = b.getString("groupId");
		name = b.getString("groupName");
		poster = b.getString("groupPost");
		superNum = b.getInt("upGroupNum");
		JuniorNum = b.getInt("downGroupNum");
		cpNum = b.getInt("cooGroupNum");

		family.addCircle(id, name, poster);
		if (superNum != 0) {
			new loadInfoTask(GroupAtlasEnum.FATHERGROUPS).execute();
		}
		if (JuniorNum != 0) {
			new loadInfoTask(GroupAtlasEnum.SUBGROUPS).execute();
		}
		if (cpNum != 0) {
			new loadInfoTask(GroupAtlasEnum.COLLABORATEGROUPS).execute();
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// LogicUtil.enter(this, InfoWallActivity.class, null, true);
	// }

	class loadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private final GroupAtlasEnum cur;

		public loadInfoTask(GroupAtlasEnum type) {
			this.cur = type;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.groupAtlas(
						CircleFamilyActivity.this, cur, id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				switch (cur) {
				case FATHERGROUPS:// 上级
					GroupBasicBean groupBean = (GroupBasicBean) mcResult
							.getResult();
					family.addSuperCircle(groupBean);
					break;
				case SUBGROUPS:// 下级

					List<GroupBasicBean> jnGroups = (List<GroupBasicBean>) mcResult
							.getResult();
					family.addJuniorCircles(jnGroups);
					break;
				case COLLABORATEGROUPS:// 合作
					// 解析GroupBean
					MapGroupBasicBean mapGroupBean = (MapGroupBasicBean) mcResult
							.getResult();
					List<GroupBasicBean> cpGroups = mapGroupBean
							.getCOLLABORATEGROUPLIST();
					family.addCpCircles(cpGroups);
					break;
				default:
					break;
				}
			}
		}
	}
}

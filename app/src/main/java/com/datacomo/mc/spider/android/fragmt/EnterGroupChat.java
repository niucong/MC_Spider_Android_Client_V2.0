package com.datacomo.mc.spider.android.fragmt;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.datacomo.mc.spider.android.QuuChatActivity;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapGroupChatCurrentStatus;
import com.datacomo.mc.spider.android.util.LogicUtil;

public class EnterGroupChat {

	private Context context;
	private String groupId, groupName, url;

	private GroupChatStatusTask statusTask;
	private SpinnerProgressDialog spdDialog;

	private int position = -1;

	public EnterGroupChat(Context context, String groupId, String groupName,
			String groupUrl, int position) {
		this.context = context;
		this.groupId = groupId;
		this.groupName = groupName;
		this.url = groupUrl;
		this.position = position;

		spdDialog = new SpinnerProgressDialog(context);
		stopTask();

		spdDialog.showProgressDialog("正在处理中...");
		statusTask = new GroupChatStatusTask();
		statusTask.execute();
	}

	public EnterGroupChat(Context context, String groupId, String groupName,
			String groupUrl) {
		this.context = context;
		this.groupId = groupId;
		this.groupName = groupName;
		this.url = groupUrl;

		spdDialog = new SpinnerProgressDialog(context);
		stopTask();

		spdDialog.showProgressDialog("正在处理中...");
		statusTask = new GroupChatStatusTask();
		statusTask.execute();
	}

	private void stopTask() {
		if (null != statusTask
				&& statusTask.getStatus() == AsyncTask.Status.RUNNING) {
			statusTask.cancel(true);
		}
		spdDialog.cancelProgressDialog(null);
	}

	class GroupChatStatusTask extends AsyncTask<String, Integer, MCResult> {

		public GroupChatStatusTask() {
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIGroupChatRequestServers.getGroupChatCurrentStatus(
						App.app, groupId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (mcResult != null && mcResult.getResultCode() == 1) {
				MapGroupChatCurrentStatus bean = (MapGroupChatCurrentStatus) mcResult
						.getResult();
				int VISITROLE = bean.getVISITROLE();
				boolean isManager = VISITROLE == 1 || VISITROLE == 2;
				int num = bean.getMEMBERNUM();
				int VISITSTATUS = bean.getVISITSTATUS();
				if (VISITSTATUS == 2)
					num++;
				// TODO
				// if (VISITSTATUS != 1)
				// new Thread() {
				// public void run() {
				// XMPPAPI.getXmppapi().enterGroupChat(
				// GetDbInfoUtil.getMemberId(context) + "",
				// new String[] { groupId + "" }, context);
				// };
				// }.start();
				if (VISITSTATUS == 1 || num <= 200) {
					Bundle b = new Bundle();
					b.putString("name", groupName);
					b.putString("memberId", groupId);
					b.putString("url", url);
					b.putBoolean("isManager", isManager);
					b.putInt("num", num);
					b.putInt("position", position);
					LogicUtil.enter(context, QuuChatActivity.class, b, 22);
				} else {
					enter();
				}
			} else {
				// T.show(App.app, T.ErrStr);
				enter();
			}
		}
	}

	private void enter() {
		Bundle b = new Bundle();
		b.putString("name", groupName);
		b.putString("memberId", groupId);
		b.putString("url", url);
		b.putBoolean("isManager", false);
		b.putInt("num", 0);
		LogicUtil.enter(context, QuuChatActivity.class, b, 22);
	}

}

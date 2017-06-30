package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.ChooseGroupsDialogActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.InviteFriendActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.bean.ContactInfo;
import com.datacomo.mc.spider.android.db.ContactsBookService;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.PinYin4JCn;
import com.datacomo.mc.spider.android.util.T;

public class FindLocalAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<ContactInfo> infos;
	public final int TAG = -1;
	private Type mType;
	private String mGroupId;
	// private final int INVITE = 0;
	// private InviteTask inviteTask;
	private SpinnerProgressDialog spdDialog;

	public FindLocalAdapter(Context c, ArrayList<ContactInfo> data, Type type) {
		mContext = c;
		infos = format(data);
		mType = type;
		spdDialog = new SpinnerProgressDialog(c);
	}

	public FindLocalAdapter(Context c, ArrayList<ContactInfo> data, Type type,
			String groupId) {
		mContext = c;
		infos = format(data);
		mType = type;
		mGroupId = groupId;
		spdDialog = new SpinnerProgressDialog(c);
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ContactInfo info = infos.get(position);
		if (TAG == info.getContactMemberId()) {
			TextView t = new TextView(mContext);
			t.setBackgroundResource(R.drawable.item_title);
			t.setPadding(10, 0, 0, 0);
			t.setTextColor(Color.WHITE);
			t.setText(info.getName());
			t.setEnabled(false);
			return t;
		}
		ViewHolder holder = null;
		if (null == convertView || null == convertView.getTag()) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_find_result, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.findViewById(R.id.sex).setVisibility(View.GONE);
			holder.btn = (Button) convertView.findViewById(R.id.operate);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String name = info.getName();
		boolean status = info.hasRegiste();
		String headUrl = info.getMemberHead();
		holder.name.setText(name);
		if (status) {
			if (mType == Type.ADDFRIEND)
				holder.btn.setText("加入朋友圈");
			else if (mType == Type.ADDFRIENDTOGROUP)
				holder.btn.setText("添加");
			holder.btn.setTag(1);
		} else {
			holder.btn.setText("添加");
			holder.btn.setTag(2);
		}
		holder.btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = null;
				switch ((Integer) (v.getTag())) {
				case 1:// 添加
					switch (mType) {
					case ADDFRIEND:
						ChooseGroupBean bean = new ChooseGroupBean();
						bean.setName(new String[] { info.getName() }, null);
						bean.setTitle("加入朋友圈");
						bundle = new Bundle();
						bundle.putSerializable(BundleKey.CHOOSEGROUPBEAN, bean);
						ChooseGroupsDialogActivity.setContactInfo(info);
						LogicUtil.enter(mContext,
								ChooseGroupsDialogActivity.class, bundle, 1);
						break;
					case ADDFRIENDTOGROUP:
						spdDialog.showProgressDialog("正在添加中...");
						new AddFriendTask(mType).execute(info,
								new String[] { String.valueOf(info
										.getContactMemberId()) },
								new String[] { mGroupId });
						break;
					default:
						break;
					}
					break;
				case 2:// 邀请
					bundle = new Bundle();
					bundle.putString(BundleKey.PHONE, info.getNumber());
					bundle.putString(BundleKey.NAME, info.getName());
					if (mType == Type.ADDFRIENDTOGROUP)
						bundle.putString(BundleKey.ID_GROUP, mGroupId);
					bundle.putSerializable(BundleKey.TYPE_REQUEST, mType);
					LogicUtil.enter(mContext, InviteFriendActivity.class,
							bundle, 0);
					break;
				default:
					break;
				}

			}
		});

		holder.head.setTag(position + headUrl);
		try {
			MyFinalBitmap.setHeader(mContext, holder.head, headUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", String.valueOf(info.getContactMemberId()));
				b.putString("name", info.getName());
				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView head;
		TextView name;
		Button btn;
	}

	public void notify(boolean isShowTag) {
		if (isShowTag) {
			infos = format(infos);
		}
		super.notifyDataSetChanged();
	}

	private ArrayList<ContactInfo> format(ArrayList<ContactInfo> members) {
		ArrayList<Integer> index = new ArrayList<Integer>();
		HashMap<Integer, ContactInfo> tags = new HashMap<Integer, ContactInfo>();
		for (int i = 0; i < members.size(); i++) {
			ContactInfo g = checkTag(members, i);
			if (null != g) {
				index.add(i);
				tags.put(i, g);
			}
		}
		for (int i = index.size() - 1; i >= 0; i--) {
			int k = index.get(i);
			members.add(k, tags.get(k));
		}
		return members;
	}

	private ContactInfo checkTag(ArrayList<ContactInfo> groups, int i) {
		if (null == groups || i < 0 || i >= groups.size()) {
			return null;
		} else if (i == 0) {
			ContactInfo tagBean = new ContactInfo(getFirstName(groups.get(i)),
					"");
			tagBean.setContactMemberId(TAG);
			return tagBean;
		} else {
			String preName = getFirstName(groups.get(i - 1));
			String curName = getFirstName(groups.get(i));
			if (null != preName && null != curName && !preName.equals(curName)) {
				ContactInfo tagBean = new ContactInfo(curName, "");
				tagBean.setContactMemberId(TAG);
				return tagBean;
			}
		}
		return null;
	}

	private String getFirstName(ContactInfo group) {
		if (null == group) {
			return "#";
		}
		String f = PinYin4JCn.convertPy("leaguer", group.getName())
				.toLowerCase();
		if (null == f || f.length() <= 1) {
			return "#";
		}
		char c = f.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("[a-zA-Z]{1}+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	public void refresh(ContactInfo info) {
		infos.remove(info);
		notifyDataSetChanged();
		new ContactsBookService(mContext)
				.updateRegisterStatus(new String[] { info.getNumber() });
	}

	public void changeGroups(String[] chosenIds, ContactInfo info) {
		spdDialog.showProgressDialog("正在添加中...");
		new AddFriendTask(mType).execute(info,
				String.valueOf(info.getContactMemberId()), chosenIds);
	}

	class AddFriendTask extends AsyncTask<Object, Integer, MCResult> {
		private Object[] mParams;
		private Type mType;

		public AddFriendTask(Type type) {
			mType = type;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			MCResult result = null;
			mParams = params;
			try {
				switch (mType) {
				case ADDFRIEND:
					result = APIRequestServers.addFriendToGroup(mContext,
							(String) mParams[1], (String[]) mParams[2]);
					break;
				case ADDFRIENDTOGROUP:
					result = APIRequestServers.addFriendsToGroup(App.app,
							(String[]) mParams[1], (String[]) mParams[2],
							"false");
					break;
				default:
					break;
				}
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
				return;
			}
			T.show(App.app, "已添加！");
			ChooseGroupsDialogActivity.setIsNeedRefresh(true);
			infos.remove((ContactInfo) mParams[0]);
			notifyDataSetChanged();
			new ContactsBookService(mContext)
					.updateRegisterStatus(new String[] { ((ContactInfo) mParams[0])
							.getNumber() });
			switch (mType) {
			case ADDFRIEND:
				new Thread(new Runnable() {
					@Override
					public void run() {
						UpdateFriendListThread.updateFriendList(
								mContext, null);
					}
				}).start();
				break;

			default:
				break;
			}
		}
	}

}

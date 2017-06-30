package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.InviteLeaguerEnterGroupChatActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.groupchat.MemberSimpleBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class GroupChatAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private static final String TAG = "GroupChatAdapter";

	private ViewHolder mHolder;
	private Context context;
	private ArrayList<MemberSimpleBean> chatList;
	private int itemWidth;
	private int mGroupId;
	private boolean inDelete;
	private AlertDialog dialog;
	private boolean mIsManager;

	public GroupChatAdapter(Context context,
			ArrayList<MemberSimpleBean> chatList, int screenWidth, int groupId) {
		this.context = context;
		this.chatList = chatList;
		itemWidth = screenWidth / 4 / 8 * 7;
		mGroupId = groupId;
	}

	public GroupChatAdapter(Context context,
			ArrayList<MemberSimpleBean> chatList, int screenWidth, int groupId,
			boolean isManager) {
		this.context = context;
		this.chatList = chatList;
		itemWidth = screenWidth / 4 / 8 * 7;
		mGroupId = groupId;
		inDelete = false;
		mIsManager = isManager;
	}

	@Override
	public int getCount() {
		return chatList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.group_gv_item, null);
			mHolder = new ViewHolder();
			mHolder.iv = (ImageView) convertView.findViewById(R.id.gv_head);
			mHolder.iv_delete = (ImageView) convertView
					.findViewById(R.id.gv_delete);
			mHolder.tv = (TextView) convertView.findViewById(R.id.gv_name);
			// convertView.setLayoutParams(new GridView.LayoutParams(itemWidth,
			// itemWidth+17));
			RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
					itemWidth, itemWidth);
			lParams.setMargins(5, 8, 5, 0);
			mHolder.iv.setLayoutParams(lParams);
			mHolder.tv.setWidth(itemWidth);

			// mHolder.tv.setLayoutParams(new RelativeLayout.LayoutParams(
			// itemWidth, 30));
			mHolder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog = createRemoveDialog((Object[]) v.getTag());
					dialog.show();
				}
			});
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		final MemberSimpleBean memberSimpleBean = chatList.get(position);
		final String name = memberSimpleBean.getMemberName();
		mHolder.tv.setText(name);
		if ((chatList.size() - 2) == position) {
			mHolder.iv.setImageResource(R.drawable.create_new);
			mHolder.iv_delete.setVisibility(View.GONE);
			if (inDelete)
				convertView.setVisibility(View.GONE);
			else
				convertView.setVisibility(View.VISIBLE);
		} else if ((chatList.size() - 1) == position) {
			mHolder.iv.setImageResource(R.drawable.delete_mmeber);
			mHolder.iv_delete.setVisibility(View.GONE);
			if (inDelete) {
				convertView.setVisibility(View.GONE);
			} else {
				if (mIsManager) {
					convertView.setVisibility(View.VISIBLE);
				} else {
					convertView.setVisibility(View.GONE);
				}
			}
		} else {
			convertView.setVisibility(View.VISIBLE);
			String headpath = memberSimpleBean.getMemberHeadUrl()
					+ memberSimpleBean.getMemberHeadPath();
			headpath = ThumbnailImageUrl.getThumbnailHeadUrl(headpath,
					HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
			// Drawable vis_drawable = PublicLoadPicture.loadHead(context,
			// headpath, mHolder.iv,"groupchatadapter");
			// mHolder.iv.setImageDrawable(vis_drawable);
			// TODO
			MyFinalBitmap.setHeader(context, mHolder.iv, headpath);

			mHolder.iv_delete.setTag(new Object[] { mGroupId,
					memberSimpleBean.getMemberId(), position });
			if (inDelete)
				mHolder.iv_delete.setVisibility(View.VISIBLE);
			else
				mHolder.iv_delete.setVisibility(View.GONE);
		}

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if ((chatList.size() - 1) != position
						&& (chatList.size() - 2) != position && !inDelete) {
					Intent i = new Intent();
					i.putExtra("id", memberSimpleBean.getMemberId() + "");
					i.putExtra("name", name);
					((Activity) context).setResult(Activity.RESULT_OK, i);
					((Activity) context).finish();
					return true;
				}
				return false;
			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((chatList.size() - 1) == position) {
					if (mIsManager) {
						inDelete = true;
						notifyDataSetChanged();
					}
				} else if ((chatList.size() - 2) == position) {
					Bundle b = new Bundle();
					b.putInt("groupId", mGroupId);
					LogicUtil.enter(context,
							InviteLeaguerEnterGroupChatActivity.class, b, true);
				} else {
					if (!inDelete) {
						String id = memberSimpleBean.getMemberId() + "";
						String name = memberSimpleBean.getMemberName();
						Bundle b = new Bundle();
						b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
						b.putString("id", id);
						b.putString("name", name);
						LogicUtil
								.enter(context, HomePgActivity.class, b, false);
					}
				}
			}
		});

		return convertView;
	}

	public void cancelDelete() {
		inDelete = false;
		notifyDataSetChanged();
	}

	public boolean inDelete() {
		return inDelete;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv;
		ImageView iv_delete;
	}

	private AlertDialog createRemoveDialog(final Object[] params) {
		Builder builder = new Builder(context);
		builder.setMessage("确认踢出当前成员吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new RemoveMember(context).execute(params[0], params[1],
						params[2]);
			}
		});
		builder.setNegativeButton("取消", null);
		return builder.create();

	}

	/**
	 * 踢出圈聊
	 * 
	 * @author Administrator
	 * 
	 */
	class RemoveMember extends AsyncTask<Object, Integer, MCResult> {

		private Context mContext;
		private Object[] mParams;

		public RemoveMember(Context context) {
			mContext = context;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			mParams = params;
			MCResult result = null;
			try {
				result = APIGroupChatRequestServers.removeMemberFromGroupChat(
						mContext, (Integer) mParams[0], (Integer) mParams[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (result != null && result.getResultCode() == 1) {
				int i = (Integer) result.getResult();
				if (i == 1) {
					int position = (Integer) mParams[2];
					chatList.remove(position);
					inDelete = false;
					notifyDataSetChanged();
				} else if (i == 3) {
					T.show(context, "该成员已不在圈聊中");
				} else if (i == 5) {
					T.show(context, "不准许移除自己");
				} else {
					T.show(context, T.ErrStr);
				}
			} else {
				T.show(context, T.ErrStr);
			}
		}

	}

}

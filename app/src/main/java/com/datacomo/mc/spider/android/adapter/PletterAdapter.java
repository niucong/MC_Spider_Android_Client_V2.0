package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.MsgActivity;
import com.datacomo.mc.spider.android.QChatActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.fragmt.EnterGroupChat;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.net.been.MessageContacterBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FaceUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class PletterAdapter extends BaseAdapter {
	private static final String TAG = "PletterAdapter";

	private static Context mContext;
	private ArrayList<MessageContacterBean> pletterInfo;
	// private ListView mListView;

	private DeleteTask deleteTask;

	public boolean isPletter;

	private boolean isLocal;

	public PletterAdapter(Context context,
			ArrayList<MessageContacterBean> calls, ListView listview,
			boolean isPletter) {
		mContext = context;
		pletterInfo = calls;
		this.isPletter = isPletter;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	@Override
	public int getCount() {
		return pletterInfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		return pletterInfo.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_pletter, null);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.ll_content = (LinearLayout) convertView
					.findViewById(R.id.ll_content);
			holder.ll_details = (LinearLayout) convertView
					.findViewById(R.id.ll_details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MessageContacterBean bean = pletterInfo.get(position);
		String date = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(bean
				.getLastMessageTime()));
		String content = bean.getLastMessageContent();
		final int contacterId = bean.getContacterId();

		final String contacterName = bean.getContacterName();
		String newPletter = "";
		final int num = bean.getNewMessageNum();
		int pnum = num;
		// TODO
		// if (!isPletter) {
		// pnum = App.app.share.getIntMessage("group_chat_unread", contacterId
		// + "", 0);
		// }
		if (pnum > 0 && !isLocal) {
			if (pnum > 99) {
				newPletter = "n";
			} else {
				newPletter = String.valueOf(pnum);
			}
			holder.num.setVisibility(View.VISIBLE);
		} else {
			holder.num.setVisibility(View.GONE);
		}
		holder.num.setTag(bean);
		MemberHeadBean head = bean.getContacterHead();
		final String headUrl = head.getHeadUrl() + head.getHeadPath();
		// boolean isPoster = false;
		String mHeadUrl = ThumbnailImageUrl.getThumbnailHeadUrl(headUrl,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.name.setText(contacterName);

		Drawable drawable;
		if (bean.getContacterLeaguerId() == 0) {
			drawable = mContext.getResources().getDrawable(
					R.drawable.icon_warning);
			drawable.setBounds(0, 0, drawable.getMinimumWidth() / 3,
					drawable.getMinimumHeight() / 3);
		} else {
			drawable = null;
		}
		holder.text.setCompoundDrawables(drawable, null, null, null);

		String eStr = "";
		if (!isPletter) {
			eStr = App.app.share.getStringMessage("QuuChat", contacterId + "",
					"");
		} else {
			eStr = App.app.share
					.getStringMessage("QChat", contacterId + "", "");
		}
		if (eStr.contains("#") && eStr.length() > eStr.indexOf("#") + 1) {
			date = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(eStr
					.substring(0, eStr.indexOf("#"))));
			content = "<font color=#d50101>[草稿]</font> "
					+ eStr.substring(eStr.indexOf("#") + 1);
		}

		holder.text.setText(Html.fromHtml(
				FaceUtil.faceToHtml(dealSharePhotoVoice(content)), imageGetter,
				null));
		holder.time.setText(date);
		holder.num.setText(newPletter);
		holder.head.setTag(position + mHeadUrl);
		L.d("pletter", "mHeadUrl" + mHeadUrl);
		MyFinalBitmap.setHeader(mContext, holder.head, mHeadUrl);

		OnClickListener ownerListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				if (!isPletter) {
					b.putString("Id", String.valueOf(contacterId));
					LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
				} else {
					b.putString("id", String.valueOf(contacterId));
					b.putString("name", contacterName);
					b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					LogicUtil.enter(mContext, HomePgActivity.class, b, false);
				}
			}
		};
		holder.head.setOnClickListener(ownerListener);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.findViewWithTag(bean).setVisibility(View.GONE);
				if (num > 0) {
					bean.setUnRead(false);
					bean.setNewMessageNum(0);
					pletterInfo.remove(position);
					pletterInfo.add(position, bean);
				}
				int whichCut = MsgActivity.MSG_PLETTER;
				if (!isPletter) {
					new EnterGroupChat(mContext, contacterId + "",
							contacterName, headUrl, position);
				} else {
					whichCut = MsgActivity.MSG_GROUPCHAT;
					Bundle b = new Bundle();
					b.putString("name", contacterName);
					b.putString("memberId", String.valueOf(contacterId));
					b.putString("head", bean.getContacterHead()
							.getFullHeadPath());
					LogicUtil.enter(mContext, QChatActivity.class, b, 22);
				}
				if (mContext instanceof MsgActivity) {
					((MsgActivity) mContext).changeCallNum(whichCut,
							bean.getNewMessageNum());
				}
			}
		};
		convertView.setOnClickListener(listener);
		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (bean.getContacterLeaguerId() == 0) {
					return true;
				} else {
					String msg = "";
					if (bean.getContacterType() == 0) {
						msg = "您确定要退出 " + contacterName + " 的圈聊吗?";
					} else {
						msg = "您确定要删除和" + contacterName + "的所有短信来往吗? 删除后将无法恢复。";
					}
					new AlertDialog.Builder(mContext)
							.setTitle("提示")
							.setMessage(msg)
							.setPositiveButton("是",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											stopTask();
											deleteTask = new DeleteTask(bean);
											deleteTask.execute();
										}
									}).setNegativeButton("否", null).show();
				}
				return false;
			}
		});
		return convertView;
	}

	private String dealSharePhotoVoice(String content) {
		L.d(TAG, "dealSharePhotoVoice content=" + content);
		if (content != null && !"".equals(content)) {
			if ("分享[<:OBJ_PHOTO>]".equals(content)) {
				content = "[分享照片]";
			} else if ("发了一段语音[<:OBJ_VOICE>]".equals(content)) {
				content = "发了一段语音";
			}
		}
		return content;
	}

	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String id) {
			int ids = 0;
			try {
				ids = Integer.valueOf(id);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			Drawable drawable = mContext.getResources().getDrawable(ids);
			int width = drawable.getIntrinsicWidth();
			int height = drawable.getIntrinsicHeight();
			drawable.setBounds(0, 0, width / 2, height / 2);
			return drawable;
		}
	};

	private void stopTask() {
		if (null != deleteTask
				&& deleteTask.getStatus() == AsyncTask.Status.RUNNING) {
			deleteTask.cancel(true);
		}
	}

	class DeleteTask extends AsyncTask<String, Integer, MCResult> {

		private MessageContacterBean contacterBean;

		public DeleteTask(MessageContacterBean contacterBean) {
			this.contacterBean = contacterBean;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				if (contacterBean.getContacterType() == 0) {
					result = APIGroupChatRequestServers.exitGroupChat(App.app,
							contacterBean.getContacterId());
				} else {
					result = APIRequestServers.deleteMessageContacters(
							App.app,
							new String[] { contacterBean
									.getContacterLeaguerId() + "" });
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else {
					if (contacterBean.getContacterType() == 0) {
						showTip("已退出！");
					} else {
						showTip("已删除！");
					}
					pletterInfo.remove(contacterBean);
					notifyDataSetChanged();
					if (mContext instanceof MsgActivity) {
						int whichCut = MsgActivity.MSG_PLETTER;
						if (!isPletter) {
							whichCut = MsgActivity.MSG_GROUPCHAT;
						}
						((MsgActivity) mContext).changeCallNum(whichCut,
								contacterBean.getNewMessageNum());
					}
				}
			}
		}
	}

	private void showTip(String str) {
		T.show(App.app, str);
	}

	//
	// private void loadImg(Drawable imageDrawable, String imageUrl) {
	// ImageView imgView = (ImageView) mListView.findViewWithTag(imageUrl);
	// if (null != imgView) {
	// if (null != imageDrawable) {
	// imgView.setImageDrawable(imageDrawable);
	// }
	// }
	// }

	static class ViewHolder {
		ImageView head;
		TextView name, time, text, num;
		LinearLayout ll_details, ll_content;
	}

}

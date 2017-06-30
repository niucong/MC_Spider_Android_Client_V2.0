package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.MsgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.interfaces.OnRecallListener;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MessageGreetBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.GreetUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class CallAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<MessageGreetBean> callInfo;
	private OnRecallListener recallListener;
	private Handler handler;

	private DeleteTask deleteTask;

	public boolean refersh = false;

	public CallAdapter(Context context, ArrayList<MessageGreetBean> calls,
			ListView listview, OnRecallListener listener, Handler handler) {
		this.mContext = context;
		this.callInfo = calls;
		this.recallListener = listener;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		return callInfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		return callInfo.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int index = position;
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_call, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.btn = (TextView) convertView.findViewById(R.id.call_back);
			holder.greet = (WebView) convertView.findViewById(R.id.greet);

			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.textlayout = (LinearLayout) convertView
					.findViewById(R.id.text_linear);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MessageGreetBean bean = callInfo.get(position);
		final String memberId = String.valueOf(bean.getSendMemberId());
		final String sendName = bean.getSendMemberName();
		String date = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(bean
				.getCreateTime()));
		String content = bean.getGreetValue().replace("{member_name}", "");
		String head = bean.getHeadInfo().getHeadUrl()
				+ bean.getHeadInfo().getHeadPath();
		head = ThumbnailImageUrl.getThumbnailHeadUrl(head,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.name.setText(sendName + " " + content);
		holder.text.setVisibility(View.GONE);
		holder.time.setText(date);
		holder.head.setTag(position + head);
		MyFinalBitmap.setHeader(mContext, holder.head, head);

		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", memberId);
				b.putString("name", sendName);
				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
			}
		});

		holder.greet.loadDataWithBaseURL(null,
				GreetUtil.motionToAssetsHtml(bean.getGreetId() - 1),
				"text/html", "utf-8", null);

		holder.btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != recallListener) {
					recallListener.onRecall(index, bean.getSendMemberId() + "",
							bean.getSendMemberName() + "",
							bean.getNoticeGreetId() + "");

					Message msg = Message.obtain();
					msg.what = 0;
					msg.arg1 = index;
					msg.arg2 = ((View) ((View) ((View) v.getParent())
							.getParent()).getParent()).getBottom();
					handler.sendMessage(msg);
				}
			}
		});

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				new AlertDialog.Builder(mContext)
						.setTitle("删除提示")
						.setMessage("是否删除该条招呼？")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										stopTask();
										deleteTask = new DeleteTask(bean);
										deleteTask.execute();
									}
								}).setNegativeButton("否", null).show();
				return false;
			}
		});
		return convertView;
	}

	private void stopTask() {
		if (null != deleteTask
				&& deleteTask.getStatus() == AsyncTask.Status.RUNNING) {
			deleteTask.cancel(true);
		}
	}

	class DeleteTask extends AsyncTask<String, Integer, MCResult> {

		private MessageGreetBean greetBean;

		public DeleteTask(MessageGreetBean greetBean) {
			this.greetBean = greetBean;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.deleteGreets(App.app,
						greetBean.getNoticeGreetId() + "");
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
					refersh = true;
					showTip("已删除！");
					callInfo.remove(greetBean);
					notifyDataSetChanged();
					if (mContext instanceof MsgActivity) {
						((MsgActivity) mContext).changeCallNum(
								MsgActivity.MSG_GREET, 1);
					}
				}
			}
		}
	}

	private void showTip(String str) {
		T.show(App.app, str);
	}

	// private void loadImg(Drawable imageDrawable, String imageUrl) {
	// ImageView imgView = (ImageView) mListView.findViewWithTag(imageUrl);
	// if (null != imgView) {
	// if (null != imageDrawable) {
	// imgView.setImageDrawable(imageDrawable);
	// }
	// }
	// }

	static class ViewHolder {
		ImageView head;// img,
		WebView greet;
		TextView name, from, time, btn, text;
		LinearLayout textlayout;
	}
}

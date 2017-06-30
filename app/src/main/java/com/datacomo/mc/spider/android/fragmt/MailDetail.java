package com.datacomo.mc.spider.android.fragmt;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseFragAct;
import com.datacomo.mc.spider.android.MailCreateActivity;
import com.datacomo.mc.spider.android.MailDetailsIndexActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.MailInfoService;
import com.datacomo.mc.spider.android.dialog.ShowToast;
import com.datacomo.mc.spider.android.net.APIMailRequestServers;
import com.datacomo.mc.spider.android.net.been.AttachMent;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MailBean;
import com.datacomo.mc.spider.android.net.been.ReceiveBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.view.MailFile;
import com.datacomo.mc.spider.android.view.MailFileLayout;

@SuppressLint("ValidFragment")
public class MailDetail extends Fragment implements OnClickListener {
	protected static final String TAG = "MailDetail";
	private boolean isDetailsOpen;
	private TextView mTitle, mMailFrom, mMailTo, mMailCopyTo, mFileNum, mDate;
	private ImageView showAll;
	private WebView mWeb;
	private LinearLayout moreInfo, mailto, mailcopyto;
	private MailFileLayout files;
	private MailBean mailBean;
	private MailTask task;

	private String sendName, mailFrom;
	private int sendId;

	public MailDetail() {
	}

	public MailDetail newInstance(int position, String mailId, String recordId,
			MailBean mBean) {
		MailDetail md = new MailDetail();
		Bundle b = new Bundle();
		b.putString("mId", mailId);
		b.putString("rId", recordId);
		// b.putSerializable("mBean", mailBean);
		md.setArguments(b);
		mailBean = mBean;
		return md;
	}

	// public MailDetail(int position, String mailId, String recordId,
	// MailBean mBean) {
	// Bundle b = new Bundle();
	// b.putString("mId", mailId);
	// b.putString("rId", recordId);
	// setArguments(b);
	// mailBean = mBean;
	// }

	private String[] getIds() {
		String[] ids = new String[2];
		Bundle b = getArguments();
		if (null != b) {
			ids[0] = b.getString("mId");
			ids[1] = b.getString("rId");
			return ids;
		}
		return null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (null != mailBean)
			outState.putSerializable("mBean", mailBean);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ScrollView root = (ScrollView) inflater.inflate(
				R.layout.frag_mail_details, container, false);
		findView(root);
		setViews(savedInstanceState);
		return root;
	}

	private void findView(View root) {
		mTitle = (TextView) root.findViewById(R.id.mail_title);
		mMailFrom = (TextView) root.findViewById(R.id.mail_from);

		mDate = (TextView) root.findViewById(R.id.date);
		mWeb = (WebView) root.findViewById(R.id.content);
		mWeb.setBackgroundColor(Color.WHITE);
		WebSettings webSettings = mWeb.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// webSettings.setSupportZoom(true);
		// webSettings.setBuiltInZoomControls(true);
		webSettings.setDefaultFontSize(15);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		mWeb.setWebViewClient(new WebViewClient() {
			@SuppressWarnings("deprecation")
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				L.i(TAG, "findView shouldOverrideUrlLoading url=" + url);
				if (url != null) {
					if (url.startsWith("mailto:")) {
						Intent intent = new Intent(
								(MailDetailsIndexActivity) getActivity(),
								MailCreateActivity.class);
						intent.putExtra("friendName",
								URLDecoder.decode(url.replace("mailto:", "")));
						startActivity(intent);
						return true;
					}
				}
				Uri uri = Uri.parse(url);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		showAll = (ImageView) root.findViewById(R.id.show_details);
		showAll.setOnClickListener(this);
		mFileNum = (TextView) root.findViewById(R.id.files_text);
		files = (MailFileLayout) root.findViewById(R.id.files);
		moreInfo = (LinearLayout) root.findViewById(R.id.other_info);

		mailto = (LinearLayout) root.findViewById(R.id.ll_mailto);
		mailcopyto = (LinearLayout) root.findViewById(R.id.ll_copyto);
		mMailTo = (TextView) root.findViewById(R.id.mail_to);
		mMailCopyTo = (TextView) root.findViewById(R.id.mail_copyto);
	}

	private void setViews(Bundle saved) {
		if (null != mailBean) {
			setViewWithMailBean(mailBean);
		} else if (null != saved && saved.size() > 0) {
			mailBean = (MailBean) saved.getSerializable("mBean");
			setViewWithMailBean(mailBean);
		} else {
			try {
				String[] ids = getIds();
				mailBean = MailInfoService.getService(getActivity()).queryMail(
						Integer.valueOf(ids[0]), Integer.valueOf(ids[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (mailBean == null) {
				startTask();
			} else {
				((MailDetailsIndexActivity) getActivity()).putMap(getIds()[1],
						mailBean);
				setViewWithMailBean(mailBean);
			}
		}
	}

	private void setViewWithMailBean(MailBean mailBean) {
		if (2 == mailBean.getIsDeleted()) {
			ShowToast.getToast(App.app).show("此邮件已被删除");
			((MailDetailsIndexActivity) getActivity()).finish();
			return;
		}

		sendName = mailBean.getSendMemberName();
		mailFrom = mailBean.getSendMemberMail();
		sendId = mailBean.getSendMemberId();
		String mailTitle = mailBean.getMailSubject();
		String mailContent = mailBean.getMailHtmlContent();
		String mailContent2 = mailBean.getMailContent();
		L.i(TAG, "setViewWithMailBean mailContent=" + mailContent
				+ ",mailContent2=" + mailContent2);
		if (mailContent == null || "".equals(mailContent)) {
			if (mailContent2 != null) {
				mailContent = mailContent2;
			} else {
				mailContent = "";
			}
		}
		mailContent = mailContent.replaceAll("<a href='javascript:;' rel='",
				"<a href='mailto:");
		L.d(TAG, "setViewWithMailBean mailContent=" + mailContent
				+ ",mailTitle=" + mailTitle);
		String sendTime = mailBean.getCreateTime();
		if (mailTitle != null)
			mTitle.setText(Html.fromHtml(mailTitle));
		mWeb.setVisibility(View.VISIBLE);// .replaceAll(" ", "&nbsp;")
		mWeb.loadDataWithBaseURL(null, mailContent + "<br/ ><br/ >",
				"text/html", "utf-8", null);
		mDate.setText(DateTimeUtil.bTimeFormat(DateTimeUtil
				.getLongTime(sendTime)));
		if (0 == sendId) {
			mMailFrom.setText(mailFrom);
		} else {
			mMailFrom.setText(sendName);
		}

		files.clearFiles();
		int num = mailBean.getAttachmentNum();
		if (0 == num) {
			mFileNum.setVisibility(View.GONE);
		} else {
			mFileNum.setText(Html.fromHtml("共有  <font color='#669900'>" + num
					+ "</font> 个附件"));
		}
		ArrayList<AttachMent> aFiles = (ArrayList<AttachMent>) mailBean
				.getAttachMent();
		if (null != aFiles && aFiles.size() > 0) {
			for (int i = 0; i < aFiles.size(); i++) {
				AttachMent f = aFiles.get(i);
				MailFile file = new MailFile(getActivity(), f);
				files.addFile(file);
			}
		}

		List<ReceiveBean> receivers = mailBean.getReceivers();
		List<ReceiveBean> carbons = mailBean.getCarbonCopy();
		if (null != receivers && receivers.size() > 0) {
			mailto.setVisibility(View.VISIBLE);
			mMailTo.setText(setReceiver(receivers));
		}
		if (null != carbons && carbons.size() > 0) {
			mailcopyto.setVisibility(View.VISIBLE);
			mMailCopyTo.setText(setReceiver(carbons));
		}
	}

	private void startTask() {
		stopTask();
		task = new MailTask();
		task.execute();
	}

	private void stopTask() {
		if (task != null && task.getStatus() == Status.RUNNING) {
			task.cancel(true);
		}
	}

	class MailTask extends AsyncTask<Void, Integer, MCResult> {

		String[] ids;

		public MailTask() {
			((BaseFragAct) getActivity()).setLoadingState(true);
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			ids = getIds();
			try {
				mc = APIMailRequestServers.mailInfo(getActivity(), ids[0],
						ids[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);

			// if (result == null || result.getResultCode() != 1) {
			// return;
			// }
			try {
				mailBean = (MailBean) result.getResult();
				((MailDetailsIndexActivity) getActivity()).putMap(getIds()[1],
						mailBean);
				setViewWithMailBean(mailBean);
				((BaseFragAct) getActivity()).setLoadingState(false);
			} catch (Exception e) {
				e.printStackTrace();

				try {
					mailBean = MailInfoService.getService(getActivity())
							.queryMail(Integer.valueOf(ids[0]),
									Integer.valueOf(ids[1]));
					if (mailBean != null) {
						((MailDetailsIndexActivity) getActivity()).putMap(
								getIds()[1], mailBean);
						setViewWithMailBean(mailBean);
						((BaseFragAct) getActivity()).setLoadingState(false);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private String setReceiver(List<ReceiveBean> receivers) {
		String mailto = "";
		int size = receivers.size();
		for (int i = 0; i < size; i++) {
			ReceiveBean receiver = receivers.get(i);
			mailto += getReceiver(receiver);
			if (size > 1 && i != size - 1) {
				mailto += ";";
			}
		}
		return mailto;
	}

	private String getReceiver(ReceiveBean receiver) {
		String rName = receiver.getName();
		String rMail = receiver.getMail();
		if (null != rName || null != rMail) {
			if (0 != receiver.getId()) {
				return rName + "<" + rMail + ">";
			} else {
				return rMail;
			}
		} else {
			return "";
		}
	}

	private void showDetails() {
		moreInfo.setVisibility(View.VISIBLE);
		if (0 != sendId) {
			mMailFrom.setText(sendName + "<" + mailFrom + ">");
		}
		showAll.setImageResource(R.drawable.icon_up);
		isDetailsOpen = true;
	}

	private void hideDetails() {
		moreInfo.setVisibility(View.GONE);
		if (0 == sendId) {
			mMailFrom.setText(mailFrom);
		} else {
			mMailFrom.setText(sendName);
		}
		showAll.setImageResource(R.drawable.icon_down);
		isDetailsOpen = false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_details:
			if (isDetailsOpen) {
				hideDetails();
			} else {
				showDetails();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stopTask();
	}

	public void remailAll() {

	}
}

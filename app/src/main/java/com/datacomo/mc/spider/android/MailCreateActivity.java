package com.datacomo.mc.spider.android;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.MailTo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.AdditiveFileListAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.AdditiveFileBean;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.dialog.AdditiveFileDialog;
import com.datacomo.mc.spider.android.dialog.ShowToast;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.APIMailRequestServers;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.net.been.AttachMent;
import com.datacomo.mc.spider.android.net.been.FriendSimpleBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MailBean;
import com.datacomo.mc.spider.android.net.been.MailContactBean;
import com.datacomo.mc.spider.android.net.been.ReceiveBean;
import com.datacomo.mc.spider.android.net.been.map.MapMailContactBean;
import com.datacomo.mc.spider.android.params.UploadFileParams;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.url.APIMethodName;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.HandlerUtil;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;
import com.datacomo.mc.spider.android.util.StreamUtil;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.DialogView;
import com.datacomo.mc.spider.android.view.FileListView;
import com.datacomo.mc.spider.android.view.GroupAutoCompleteTextView;
import com.datacomo.mc.spider.android.view.GroupNameView;
import com.datacomo.mc.spider.android.view.GroupView;
import com.datacomo.mc.spider.android.view.MailFile;
import com.datacomo.mc.spider.android.view.MailFileLayout;
import com.umeng.analytics.MobclickAgent;

public class MailCreateActivity extends BasicActionBarActivity implements
		OnTouchListener, OnKeyListener, OnItemClickListener {
	private static final String TAG = "MailCreateActivity";

	private EditText title_et, content_et;
	private GroupView receiver_gv, shareTo_gv, sendTo_gv;
	private ImageView choose1, choose2, choose3;
	private ImageView pic, file;
	private LinearLayout fileLayout, group, ll_name, ll_share, ll_send;
	private TextView keyText;
	private GroupAutoCompleteTextView receiver_et, shareTo_et, sendTo_et;

	public static LinkedHashMap<String, FriendSimpleBean> receivers, shareTos,
			sendTos;

	private String mailTitle, mailContent;
	private int sendType, sourceMailId;
	private String pictureName = null;
	private final int INSERT_PHOTO = 11;
	// private final int INSERT_FILE = 2;
	public final static int ADD_RECEIVER = 13;
	public final static int ADD_SHARETO = 14;
	public final static int ADD_SENDTO = 5;
	private int requestTag = ADD_RECEIVER;

	public static MailCreateActivity mailCreate;
	// public LinkedHashMap<String, Boolean> isUploadOver;

	/**
	 * 已上传成功的path与attachmentStrs
	 */
	public LinkedHashMap<String, String> attachmentsMap;
	public ArrayList<String> attachmentsList;
	private AdditiveFileDialog mCreateFile;
	private AdditiveFileListAdapter mAdditiveFileLvAdapter_Choosed;
	private FileListView mAdditiveFileLv_ChoosedImg;
	private ViewStub mVs_FileLv;

	private MailBean mailBean;
	boolean isShowSourceMail = true;

	private String sourceMailContent = "";

	private String sign = "<br /><br /><br /><p style=\"color:#999999;border-top:1px dotted #999999;padding-top:3px;\">来自我的优优工作圈Android客户端%s</p>";

	private List<FriendSimpleBean> allMbers;// FriendSimpleBean
	// private List<FriendSimpleBean> matchMbers;// MailContactBean
	private String mail;

	// private FriendNamesAdapter<FriendSimpleBean> adapter;

	@Override
	protected void onDestroy() {
		sendHandler.removeCallbacksAndMessages(null);
		hideFile.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "23");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_mail_create);
		ab.setTitle("新邮件");

		mailCreate = this;
		attachmentsList = new ArrayList<String>();
		attachmentsMap = new LinkedHashMap<String, String>();

		// TODO allMbers =
		// FriendListService.getService(this).queryFriendListsByPy(0);
		allMbers = new ArrayList<FriendSimpleBean>();
		// matchMbers = new ArrayList<FriendSimpleBean>();

		findViews();

		Intent intent = getIntent();
		String friendName = intent.getStringExtra("friendName");
		int friendId = intent.getIntExtra("friendId", 0);
		L.i(TAG, "onCreate friendName=" + friendName + ",friendId=" + friendId);
		if (friendName != null && !"".equals(friendName)) {
			FriendSimpleBean friendBean = formatFriendSimpleBean(friendId,
					friendName);
			if (friendId == 0 && CharUtil.isValidEmail(friendName)) {
				L.d(TAG, "onCreate friendName=" + friendName);
				receivers.put(friendName, friendBean);
			} else {
				receivers.put(friendId + "", friendBean);
			}
			showReceivers(requestTag);
			title_et.requestFocus();
		} else {
			receiver_et.requestFocus();
			// setMailClient(intent);
		}

		sendType = intent.getIntExtra("sendType", 1);
		mailBean = (MailBean) intent.getSerializableExtra("MailBean");
		if (mailBean != null) {
			sourceMailId = mailBean.getMailId();
			mailTitle = mailBean.getMailSubject();
		}

		if (mailTitle != null && !"".equals(mailTitle)) {
			if (sendType == 2) {
				title_et.setText("回复：" + mailTitle);
				if (mailBean != null) {
					addSourceMail();
				}
				content_et.requestFocus();
			} else if (sendType == 3) {
				title_et.setText("转发：" + mailTitle);
				if (mailBean != null) {
					addSourceMail();
					setAttachMents();
				}
				receiver_et.requestFocus();
			} else if (sendType == 4) {
				title_et.setText("回复：" + mailTitle);
				if (mailBean != null) {
					addSourceMail();
					addAllReceivers(mailBean);
				}
				content_et.requestFocus();
			} else if (sendType == 5) {
				title_et.setText(mailTitle);
				if (mailBean != null) {
					addSourceMail();
				}
				content_et.requestFocus();
			} else {
				receiver_et.requestFocus();
			}
		}

		mAdditiveFileLvAdapter_Choosed = new AdditiveFileListAdapter(
				MailCreateActivity.this, new ArrayList<Object>());
		mAdditiveFileLvAdapter_Choosed.setHideFile(hideFile);
	}

	/**
	 * 分享到优优工作圈——邮件管家
	 * 
	 * @param intent
	 */
	@SuppressWarnings("unused")
	private void setMailClient(Intent intent) {
		String str3 = intent.getDataString();
		MailTo mt = MailTo.parse(str3);
		String to = mt.getTo();// 接收
		if (to != null && !"".equals(to)) {
			if (to.contains(";")) {
				String[] tos = to.split(";");
				for (String str : tos) {
					if (CharUtil.isValidEmail(str)) {
						FriendSimpleBean friendBean = formatFriendSimpleBean(0,
								str);
						receivers.put(str, friendBean);
					}
				}
			} else {
				if (CharUtil.isValidEmail(to)) {
					FriendSimpleBean friendBean = formatFriendSimpleBean(0, to);
					receivers.put(to, friendBean);
				}
			}
			showReceivers(requestTag);
		}
		String cc = mt.getCc();// 抄送
		if (cc != null && !"".equals(cc)) {
			if (cc.contains(";")) {
				String[] ccs = cc.split(";");
				for (String str : ccs) {
					if (CharUtil.isValidEmail(str)) {
						FriendSimpleBean friendBean = formatFriendSimpleBean(0,
								str);
						shareTos.put(str, friendBean);
					}
				}
			} else {
				if (CharUtil.isValidEmail(cc)) {
					FriendSimpleBean friendBean = formatFriendSimpleBean(0, cc);
					shareTos.put(cc, friendBean);
				}
			}
			showReceivers(ADD_SHARETO);
		}
		title_et.setText(mt.getSubject());// 主题
		content_et.setText(mt.getBody());// 内容
	}

	private FriendSimpleBean formatFriendSimpleBean(int id, String name) {
		FriendSimpleBean friendBean = new FriendSimpleBean();
		friendBean.setMemberName(name);
		friendBean.setMemberId(id);
		return friendBean;
	}

	private void addSourceMail() {
		LinearLayout sourceMailLayout = (LinearLayout) findViewById(R.id.sourceMail_layout);
		sourceMailLayout.setVisibility(View.VISIBLE);
		sourceMailLayout.setOnTouchListener(this);
		final LinearLayout sourceMail = (LinearLayout) findViewById(R.id.sourceMail);
		CheckBox box = (CheckBox) findViewById(R.id.show_sourceMail_checkBox);
		box.setChecked(true);
		sourceMail.setVisibility(View.VISIBLE);
		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (!isChecked) {
					sourceMail.setVisibility(View.GONE);
				} else {
					sourceMail.setVisibility(View.VISIBLE);
				}
				isShowSourceMail = isChecked;
			}
		});

		final TextView showSource = (TextView) findViewById(R.id.show_sourceMail);
		showSource.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowSourceMail) {
					showSource.setText("显示");
					sourceMail.setVisibility(View.GONE);
				} else {
					showSource.setText("隐藏");
					sourceMail.setVisibility(View.VISIBLE);
				}
				isShowSourceMail = !isShowSourceMail;
				closeGroup();
			}
		});

		WebView content = (WebView) findViewById(R.id.source_content);
		content.setBackgroundColor(0);
		sourceMailContent = mailBean.getMailHtmlContent();
		String sourceMailContent2 = mailBean.getMailContent();
		if (sourceMailContent == null || "".equals(sourceMailContent)) {
			sourceMailContent = sourceMailContent2;
		}
		if (sourceMailContent == null)
			sourceMailContent = "";
		String sendName = mailBean.getSendMemberName();
		String mailFrom = mailBean.getSendMemberMail();
		String mailStr = "";
		String mStr = "<span class=\"data-popage-sendEmail\"><a href=\"javascript:;\" rel=\"%s\">%s</a></span>";
		if (mailFrom != null && !mailFrom.equals("") && sendName != null
				&& !sendName.equals("") && !mailFrom.equals(sendName)) {
			mailStr += "<br />发件人：" + sendName + "("
					+ String.format(mStr, mailFrom, mailFrom) + ")";
		} else if (sendName != null && !sendName.equals("")) {
			mailStr += "<br />发件人：" + sendName;
		} else if (mailFrom != null && !mailFrom.equals("")) {
			mailStr += "<br />发件人：" + String.format(mStr, mailFrom, mailFrom);
		}

		List<ReceiveBean> receivers = mailBean.getReceivers();
		if (receivers != null && receivers.size() > 0) {
			mailStr += "<br />收件人：";
			for (ReceiveBean receiveBean : receivers) {
				String rn = receiveBean.getName();
				String rm = receiveBean.getMail();
				if (rn != null && !rn.equals("") && rm != null
						&& !rm.equals("") && !rn.equals(rm)) {
					mailStr += rn + "(" + String.format(mStr, rm, rm) + "); ";
				} else if (rn != null && !rn.equals("")) {
					mailStr += rn + "; ";
				} else if (rm != null && !rm.equals("")) {
					mailStr += String.format(mStr, rm, rm) + "; ";
				}
			}
		}
		List<ReceiveBean> carbons = mailBean.getCarbonCopy();
		if (carbons != null && carbons.size() > 0) {
			mailStr += "<br />抄送：";
			for (ReceiveBean receiveBean : carbons) {
				String rn = receiveBean.getName();
				String rm = receiveBean.getMail();
				if (rn != null && !rn.equals("") && rm != null
						&& !rm.equals("") && !rn.equals(rm)) {
					mailStr += rn + "(" + String.format(mStr, rm, rm) + "); ";
				} else if (rn != null && !rn.equals("")) {
					mailStr += rn + "; ";
				} else if (rm != null && !rm.equals("")) {
					mailStr += String.format(mStr, rm, rm) + "; ";
				}
			}
		}

		if (sendType == 5) {
			sendType = 1;
		} else {
			sourceMailContent = "<div><br />——原始邮件——"
					+ mailStr
					+ "<br />主题："
					+ mailTitle
					+ "<br />日期："
					+ ConstantUtil.YYYYMMDDHHMM.format(DateTimeUtil
							.getLongTime(mailBean.getCreateTime()))
					+ "<br /></div>" + sourceMailContent;
		}
		content.loadDataWithBaseURL(null, sourceMailContent, "text/html",
				"utf-8", null);
	}

	private void addAllReceivers(MailBean mailBean) {
		openGroup();
		List<ReceiveBean> temCarbons = mailBean.getCarbonCopy();
		List<ReceiveBean> temReceivers = mailBean.getReceivers();
		if (null != temCarbons) {
			for (int i = 0; i < temCarbons.size(); i++) {
				ReceiveBean bean = temCarbons.get(i);
				int _id = bean.getId();
				String _name = bean.getName();

				if (_id != GetDbInfoUtil.getMemberId(this)) {
					FriendSimpleBean fBean = formatFriendSimpleBean(_id, _name);
					if (_id == 0) {
						_name = bean.getMail();
						fBean.setMemberName(_name);
						shareTos.put(_name, fBean);
					} else {
						shareTos.put(_id + "", fBean);
					}
				}
			}
			showReceivers(ADD_SHARETO);
		}

		if (null != temReceivers) {
			for (int i = 0; i < temReceivers.size(); i++) {
				ReceiveBean bean = temReceivers.get(i);
				int _id = bean.getId();
				String _name = bean.getName();
				if (_id != GetDbInfoUtil.getMemberId(this)) {
					FriendSimpleBean fBean = formatFriendSimpleBean(_id, _name);
					if (_id == 0) {
						_name = bean.getMail();
						fBean.setMemberName(_name);
						receivers.put(_name, fBean);
					} else {
						receivers.put(_id + "", fBean);
					}
				}
			}
			showReceivers(ADD_RECEIVER);
		}
	}

	private void setAttachMents() {
		List<AttachMent> attachMents = mailBean.getAttachMent();
		if (attachMents != null) {
			int size = attachMents.size();
			L.d(TAG, "setAttachMents size=" + size);
			if (size > 0) {
				TextView fileNum = (TextView) findViewById(R.id.source_files_text);
				fileNum.setVisibility(View.VISIBLE);
				fileNum.setText(Html.fromHtml("共有  <font color='#669900'>"
						+ size + "</font> 个附件"));

				MailFileLayout sourceFiles = (MailFileLayout) findViewById(R.id.source_files);
				sourceFiles.setVisibility(View.VISIBLE);
				sourceFiles.clearFiles();
				for (AttachMent attachMent : attachMents) {
					attachmentsList.add(attachMent.getName()
							+ ConstantUtil.PHONE_SEPARATOR
							+ attachMent.getPath()
							+ ConstantUtil.PHONE_SEPARATOR
							+ attachMent.getSize());

					MailFile file = new MailFile(this, attachMent);
					sourceFiles.addFile(file);
				}
			}
		}
	}

	private void findViews() {
		title_et = (EditText) findViewById(R.id.subject);
		content_et = (EditText) findViewById(R.id.content);

		receiver_gv = (GroupView) findViewById(R.id.name);
		shareTo_gv = (GroupView) findViewById(R.id.shareTo);
		sendTo_gv = (GroupView) findViewById(R.id.sendTo);

		receiver_et = (GroupAutoCompleteTextView) findViewById(R.id.ll_name_et);
		shareTo_et = (GroupAutoCompleteTextView) findViewById(R.id.ll_shareTo_et);
		sendTo_et = (GroupAutoCompleteTextView) findViewById(R.id.ll_sendTo_et);

		keyText = (TextView) findViewById(R.id.key);
		group = (LinearLayout) findViewById(R.id.group);
		ll_name = (LinearLayout) findViewById(R.id.ll_name);
		ll_share = (LinearLayout) findViewById(R.id.ll_shareTo);
		ll_send = (LinearLayout) findViewById(R.id.ll_sendTo);

		choose1 = (ImageView) findViewById(R.id.choose1);
		choose2 = (ImageView) findViewById(R.id.choose2);
		choose3 = (ImageView) findViewById(R.id.choose3);

		choose1.setOnClickListener(this);
		choose2.setOnClickListener(this);
		choose3.setOnClickListener(this);

		ll_name.setOnClickListener(this);
		ll_share.setOnClickListener(this);
		ll_send.setOnClickListener(this);

		file = (ImageView) findViewById(R.id.file);
		pic = (ImageView) findViewById(R.id.pic);
		pic.setOnClickListener(this);
		file.setOnClickListener(this);
		keyText.setOnClickListener(this);

		title_et.setOnTouchListener(this);
		content_et.setOnTouchListener(this);

		String currentVersion = new SoftPhoneInfo(this).getVersionName();
		if (currentVersion != null && !"".equals(currentVersion)) {
			sign = String.format(sign, "V" + currentVersion);
		} else {
			sign = String.format(sign, "");
		}
		((TextView) findViewById(R.id.sign)).setText(Html.fromHtml(sign, null,
				null));

		fileLayout = (LinearLayout) findViewById(R.id.addFile);

		receivers = new LinkedHashMap<String, FriendSimpleBean>();
		shareTos = new LinkedHashMap<String, FriendSimpleBean>();
		sendTos = new LinkedHashMap<String, FriendSimpleBean>();

		receiver_et.addTextChangedListener(watcher);
		shareTo_et.addTextChangedListener(watcher);
		sendTo_et.addTextChangedListener(watcher);

		receiver_et.setOnFocusChangeListener(focusChangeListener);
		shareTo_et.setOnFocusChangeListener(focusChangeListener);
		sendTo_et.setOnFocusChangeListener(focusChangeListener);

		receiver_et.setOnItemClickListener(this);
		shareTo_et.setOnItemClickListener(this);
		sendTo_et.setOnItemClickListener(this);

		receiver_et.setOnKeyListener(this);
		shareTo_et.setOnKeyListener(this);
		sendTo_et.setOnKeyListener(this);

		int screenWidth = BaseData.getScreenWidth();
		L.i(TAG, "findViews screenWidth=" + screenWidth);
		receiver_et.setDropDownWidth(screenWidth);
		shareTo_et.setDropDownWidth(screenWidth);
		sendTo_et.setDropDownWidth(screenWidth);

		mVs_FileLv = (ViewStub) findViewById(R.id.create_mail_filelist);
		mVs_FileLv.setOnInflateListener(new OnInflateListener() {

			@Override
			public void onInflate(ViewStub stub, View inflated) {
				mAdditiveFileLv_ChoosedImg = (FileListView) inflated
						.findViewById(R.id.layout_createtopic_filelist);
				mAdditiveFileLv_ChoosedImg
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								TransitionBean bean = null;
								try {
									bean = new TransitionBean(parent
											.getItemAtPosition(position));
								} catch (Exception e) {
									e.printStackTrace();
								}
								switch (bean.getType()) {
								case CLOUDFILE:
									String name = StringUtil.subString(
											bean.getPath(), "/", null, 1, 0);
									L.d(TAG, "name:" + name);
									File myFile = new File(
											ConstantUtil.CLOUD_PATH + name);
									if (myFile != null && myFile.exists()) {
										new FileUtil()
												.openFile(App.app, myFile);
									} else if (ConstantUtil.downloadingList
											.contains(name)) {
										T.show(App.app, R.string.downloading);
									} else {
										openFile(name, bean.getId(),
												bean.getFileOriginalSize());
									}
									break;
								case ADDITIVEFILE:
									new FileUtil().openFile(
											MailCreateActivity.this, new File(
													bean.getPath()), bean
													.getMimeType());
									break;
								default:
									break;
								}
							}
						});
				mAdditiveFileLv_ChoosedImg
						.setAdapter(mAdditiveFileLvAdapter_Choosed);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private void openFile(final String filePath, final int fileId,
			final long fileLength) {
		final String tempName = filePath
				.substring(filePath.lastIndexOf("/") + 1);
		File myFile = new File(ConstantUtil.CLOUD_PATH + tempName);
		if (myFile != null && myFile.exists()) {
			new FileUtil().openFile(this, myFile);
			return;
		} else if (ConstantUtil.downloadingList.contains(tempName)) {
			T.show(App.app, R.string.downloading);
			return;
		} else {
			spdDialog.showProgressDialog("正在处理中...");
			final Handler openHandler = new Handler() {
				public void handleMessage(Message msg) {
					spdDialog.cancelProgressDialog(null);
					switch (msg.what) {
					case 0:
						T.show(App.app, T.ErrStr);
						break;
					case 1:
						break;
					default:
						break;
					}
				};
			};
			ConstantUtil.downloadingList.add(tempName);
			new Thread() {
				public void run() {
					try {
						MCResult mcResult = APIFileRequestServers.getFilePath(
								App.app, fileId);
						if (mcResult != null && mcResult.getResultCode() == 1) {
							String fileUrl = mcResult.getResult().toString();
							new DownLoadFileThread(MailCreateActivity.this,
									fileUrl, fileLength, tempName, true)
									.start();
							openHandler.sendEmptyMessage(1);
						} else {
							ConstantUtil.downloadingList.remove(tempName);
							openHandler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						ConstantUtil.downloadingList.remove(tempName);
						openHandler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (allMbers == null || allMbers.size() < position)
			return;
		FriendSimpleBean friendBean = allMbers.get(position);
		String name = friendBean.getMemberId() + "";
		if (friendBean.getMemberId() == 0)
			name = friendBean.getMemberName();
		switch (requestTag) {
		case ADD_RECEIVER:
			receivers.put(name, friendBean);
			break;
		case ADD_SHARETO:
			shareTos.put(name, friendBean);
			break;
		case ADD_SENDTO:
			sendTos.put(name, friendBean);
			break;
		}
		showReceivers();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
			switch (v.getId()) {
			case R.id.ll_name_et:
				autoAddReceiver();
				break;
			case R.id.ll_shareTo_et:
				autoAddShare();
				break;
			case R.id.ll_sendTo_et:
				autoAddSend();
				break;
			default:
				break;
			}
			return true;
		}
		return false;
	}

	/**
	 * 按回车、失去焦点时自动添加收件人
	 */
	private boolean autoAddReceiver() {
		requestTag = ADD_RECEIVER;
		mail = receiver_et.getText().toString();
		if (mail != null && !"".equals(mail))
			if (CharUtil.isValidEmail(mail)) {
				FriendSimpleBean friendBean = new FriendSimpleBean();
				friendBean.setMemberName(mail);
				receivers.put(mail, friendBean);
				showReceivers();
			} else {
				ShowToast.getToast(App.app).show("邮件地址格式不正确");
				return false;
			}
		return true;
	}

	/**
	 * 按回车、失去焦点时自动添加抄送人
	 */
	private boolean autoAddShare() {
		requestTag = ADD_SHARETO;
		mail = shareTo_et.getText().toString();
		if (mail != null && !"".equals(mail))
			if (CharUtil.isValidEmail(mail)) {
				FriendSimpleBean friendBean = new FriendSimpleBean();
				friendBean.setMemberName(mail);
				shareTos.put(mail, friendBean);
				showReceivers();
			} else {
				ShowToast.getToast(App.app).show("邮件地址格式不正确");
				return false;
			}
		return true;
	}

	/**
	 * 按回车、失去焦点时自动添加密送人
	 */
	private boolean autoAddSend() {
		requestTag = ADD_SENDTO;
		mail = sendTo_et.getText().toString();
		if (mail != null && !"".equals(mail))
			if (CharUtil.isValidEmail(mail)) {
				FriendSimpleBean friendBean = new FriendSimpleBean();
				friendBean.setMemberName(mail);
				sendTos.put(mail, friendBean);
				showReceivers();
			} else {
				ShowToast.getToast(App.app).show("邮件地址格式不正确");
				return false;
			}
		return true;
	}

	private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				// v.setVisibility(View.VISIBLE);
			} else {
				// v.setVisibility(View.GONE);
				switch (v.getId()) {
				case R.id.ll_name_et:
					autoAddReceiver();
					break;
				case R.id.ll_shareTo_et:
					autoAddShare();
					break;
				case R.id.ll_sendTo_et:
					autoAddSend();
					break;
				}
			}
		}
	};

	class SearchMailsTask extends AsyncTask<String, Integer, MCResult> {

		private String key;

		public SearchMailsTask(String key) {
			this.key = key;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIMailRequestServers
						.searchMyContactLeaguersAndFriends(
								MailCreateActivity.this, key, "0", "30",
								"true", searchTask);
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			L.d(TAG, "SearchMailsTask onPostExecute..." + key);
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					// showTip(T.ErrStr);
				} else {
					MapMailContactBean map = (MapMailContactBean) mcResult
							.getResult();
					List<MailContactBean> requestInfos = map.getCONTACTLIST();
					if (requestInfos != null && requestInfos.size() > 0) {
						// List<FriendSimpleBean> fsBeans = new
						// ArrayList<FriendSimpleBean>();
						allMbers.clear();
						ArrayList<String> arrayList = new ArrayList<String>();
						for (MailContactBean bean : requestInfos) {
							int id = bean.getRelationMemberId();
							// if (id == 0) {
							FriendSimpleBean fBean = new FriendSimpleBean();
							String memberName = bean.getRelationMemberName();
							fBean.setMemberName(memberName);
							fBean.setMemberId(id);
							fBean.setMemberHeadPath(bean
									.getRelationMemberPath());
							fBean.setMemberHeadUrl(bean.getRelationMemberUrl());
							// boolean isContains = false;
							// for (FriendSimpleBean sfBean : allMbers) {
							// if (memberName.contains(sfBean
							// .getMemberName())) {
							// isContains = false;
							// break;
							// }
							// }
							// if (!isContains) {
							arrayList.add(memberName);
							allMbers.add(fBean);
							// matchMbers.add(fBean);
							// }
							L.d(TAG, "SearchMailsTask memberName=" + memberName);
							// }
						}
						// TODO allMbers.addAll(fsBeans);
						// adapter.notifyDataSetChanged();

						L.d(TAG, "SearchMailsTask size=" + arrayList.size());
						// FriendNamesAdapter adapter = new
						// FriendNamesAdapter<FriendSimpleBean>(
						// MailCreateActivity.this,
						// android.R.layout.simple_dropdown_item_1line,
						// allMbers);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(
								MailCreateActivity.this,
								android.R.layout.simple_dropdown_item_1line,
								arrayList);
						if (receiver_et.hasFocus()) {
							receiver_et.setAdapter(adapter);
						} else if (shareTo_et.hasFocus()) {
							shareTo_et.setAdapter(adapter);
						} else if (sendTo_et.hasFocus()) {
							sendTo_et.setAdapter(adapter);
						}
						adapter.notifyDataSetChanged();
					} else {
					}
				}
			}
		}
	}

	SearchMailsTask searchTask;

	void loadInfo(String key) {
		if (null != searchTask
				&& searchTask.getStatus() == AsyncTask.Status.RUNNING) {
			searchTask.cancel(true);
		}
		searchTask = new SearchMailsTask(key);
		searchTask.execute();
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String account_str = s.toString();
			mail = "";
			L.d(TAG, "watcher onTextChanged account_str=" + account_str
					+ ",start=" + start + ",before=" + before + ",count="
					+ count + ",s=" + s.toString());
			// allMbers.clear();
			if (account_str != null && !account_str.equals("")) {
				// matchMbers.clear();
				loadInfo(account_str);
				// if (allMbers != null) {
				// L.i(TAG, "watcher size=" + allMbers.size());
				// for (FriendSimpleBean bean : allMbers) {
				// String memberName = bean.getMemberName();
				// String friendName = bean.getFriendName();
				// if (CharUtil.isValidPhone(account_str)) {
				// String phone = bean.getMemberPhone();
				// L.i(TAG, "account_str=" + account_str + ",phone="
				// + phone);
				// if (account_str.equals(phone)
				// || phone.equals(memberName)) {
				// matchMbers.add(bean);
				// }
				// } else {
				// if ((memberName != null && memberName
				// .startsWith(account_str))
				// || (friendName != null && friendName
				// .startsWith(account_str))) {
				// matchMbers.add(bean);
				// } else {
				// String py = bean.getMemberNamePY();
				// String fpy = bean.getFriendNamePY();
				// if ((py != null && py.startsWith(account_str))
				// || (fpy != null && fpy
				// .startsWith(account_str))) {
				// matchMbers.add(bean);
				// } else {
				// String jp = bean.getMemberNameJP();
				// String fjp = bean.getFriendNameJP();
				// if ((jp != null && jp
				// .startsWith(account_str))
				// || (fjp != null && fjp
				// .startsWith(account_str))) {
				// matchMbers.add(bean);
				// }
				// }
				// }
				// }
				// }
				// }
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			L.d(TAG, "watcher beforeTextChanged start=" + start + ",count="
					+ count + ",s=" + s.toString());
		}

		@Override
		public void afterTextChanged(Editable s) {
			L.d(TAG, "watcher afterTextChanged s=" + s.toString());
		}
	};

	private void showReceivers() {
		showReceivers(requestTag);
	}

	private void showReceivers(int request) {
		switch (request) {
		case ADD_RECEIVER:
			receiver_et.setText("");
			receiver_gv.removeAllViews();
			for (FriendSimpleBean s : receivers.values()) {
				receiver_gv.addView(getGroupNameView(s), requestTag);
			}
			break;
		case ADD_SHARETO:
			shareTo_et.setText("");
			shareTo_gv.removeAllViews();
			for (FriendSimpleBean s : shareTos.values()) {
				shareTo_gv.addView(getGroupNameView(s), requestTag);
			}
			break;
		case ADD_SENDTO:
			sendTo_et.setText("");
			sendTo_gv.removeAllViews();
			for (FriendSimpleBean s : sendTos.values()) {
				sendTo_gv.addView(getGroupNameView(s), requestTag);
			}
			break;
		}
	}

	private GroupNameView getGroupNameView(FriendSimpleBean s) {
		String name = s.getMemberName();
		int id = s.getMemberId();
		GroupNameView v = new GroupNameView(this, name);
		if (id == 0) {
			v.getView().setTag("" + name);
		} else {
			v.getView().setTag("" + id);
		}
		return v;
	}

	private DialogView dialogContent = null;
	private AlertDialog pDialog = null;
	private boolean allowedPublish = true;
	private int num = 0;

	private void sendMail(final int cur) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		TextView dTitle = new TextView(this);
		dTitle.setGravity(Gravity.CENTER);
		dTitle.setPadding(0, 15, 0, 0);
		dTitle.setTextSize(22);
		dTitle.setText("发布中...");
		builder.setCustomTitle(dTitle);
		dialogContent = new DialogView(this);
		String allInfo = null;
		int numFile = mAdditiveFileLvAdapter_Choosed.getCount();
		if (numFile > 0) {
			allInfo = "共 " + numFile + " 个文件";
		} else {
			allInfo = null;
		}
		dialogContent.setAllFileInfo(allInfo);
		builder.setView(dialogContent);
		builder.setCancelable(false);
		builder.setNegativeButton("取消上传",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						pDialog.cancel();
						allowedPublish = false;
					}
				});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (pDialog.getButton(Dialog.BUTTON_NEGATIVE).getTag() != null) {
					finish();
				}
			}
		});
		pDialog = builder.show();
		new Thread() {
			public void run() {
				allowedPublish = true;
				try {
					// 发送失败后，如果重选 去除已上传成功的
					if (!attachmentsMap.isEmpty()) {
						for (Entry<String, String> entry : attachmentsMap
								.entrySet()) {
							if (mAdditiveFileLvAdapter_Choosed.contains(entry
									.getKey()) == -1) {
								attachmentsMap.remove(entry.getKey());
							}
						}
					}

					mAdditiveFileLvAdapter_Choosed.classify();
					List<String> id_CloudFile = new ArrayList<String>();
					for (Object file : mAdditiveFileLvAdapter_Choosed
							.getCloudFile(false)) {
						int id = new TransitionBean(file).getId();
						id_CloudFile.add(String.valueOf(id));
						L.d(TAG, "cloud file:" + id);
					}

					int ffsize = attachmentsMap.size() + id_CloudFile.size();
					String oriToast = null;
					if (ffsize > 0) {
						oriToast = "已上传 " + ffsize + " 个文件";
					}
					if (null != oriToast) {
						HandlerUtil.sendMsgToHandler(sendHandler, 4, oriToast,
								0);
					}

					for (Object file : mAdditiveFileLvAdapter_Choosed
							.getAdditiveFile(false)) {
						String path = new TransitionBean(file).getPath();
						if (!allowedPublish || num != cur) {
							break;
						}
						L.d(TAG, "imgPath" + path);
						if (null != path && !"".equals(path)
						// 发送失败后，如果重传 去除已上传成功的
								&& !attachmentsMap.containsKey(path)) {
							String toast = null;
							if (null != pDialog && pDialog.isShowing()) {
								int fsize = attachmentsMap.size()
										+ id_CloudFile.size();
								toast = StringUtil.merge("已上传 ", fsize, " 个文件");
								HandlerUtil.sendMsgToHandler(sendHandler, 3,
										StringUtil.merge("正在上传第 ", (1 + fsize),
												" 个文件..."), 0);
								HandlerUtil.sendMsgToHandler(sendHandler,
										HandlerUtil.getMessage(2,
												new File(path), 0, 1));
							}
							startThread(path, toast, cur);
						}
					}
					if (!allowedPublish || num != cur) {
						return;
					}
					HandlerUtil.sendMsgToHandler(sendHandler, 5, "正在发邮件...",
							View.GONE);

					String[] fileId = id_CloudFile.toArray(new String[0]);
					id_CloudFile.clear();
					id_CloudFile = null;

					int attachmentSize = attachmentsMap.size()
							+ attachmentsList.size();
					String[] attachmentStrs = new String[attachmentSize];
					int i = 0;
					for (Iterator<?> iterator = attachmentsMap.keySet()
							.iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						attachmentStrs[i] = attachmentsMap.get(key);
						i++;
					}

					for (int j = 0; j < attachmentsList.size(); j++) {
						attachmentStrs[i + j] = attachmentsList.get(j);
					}

					MCResult response = APIMailRequestServers.sendMail(
							MailCreateActivity.this, transStr(receivers, true),
							transStr(receivers, false),
							transStr(shareTos, true),
							transStr(shareTos, false), transStr(sendTos, true),
							transStr(sendTos, false), mailTitle, mailContent,
							attachmentSize + "", attachmentStrs, sendType + "",
							sourceMailId + "", fileId);

					int resultCode = response.getResultCode();
					L.i(TAG, "sendGroupTopic resultCode=" + resultCode);
					if (!allowedPublish || num != cur) {
						return;
					}
					if (resultCode == 1) {
						sendHandler.sendEmptyMessage(1);
					} else {
						sendHandler.sendEmptyMessage(0);
					}
				} catch (SocketTimeoutException e) {
					sendHandler.sendEmptyMessage(10);
					e.printStackTrace();
				} catch (Exception e) {
					sendHandler.sendEmptyMessage(0);
					e.printStackTrace();
				}
			}

		}.start();
	}

	private void startThread(String filepath, String finishToast, int cur) {
		String url = URLProperties.MEMBER_JSON;
		String method = "";
		File file = new File(filepath);
		method = APIMethodName.UPLOADEMAILFILE;
		String params = new UploadFileParams(this, method, file.getName(), null)
				.getParams();
		L.d(TAG, "startThread url=" + url + "?" + params);
		try {
			String result = httpUpload(url + "?" + params, file, finishToast,
					cur);
			L.d(TAG, "startThread result=" + result);
			if (!allowedPublish || cur != num) {
				return;
			}
			if (result != null) {
				JSONObject object = new JSONObject(result);
				if (object.getInt("resultCode") == 1) {
					String attachmentStrs = file.getName()
							+ ConstantUtil.PHONE_SEPARATOR
							+ object.getString("result")
							+ ConstantUtil.PHONE_SEPARATOR + file.length();
					L.i(TAG, "startThread attachmentStrs=" + attachmentStrs);
					attachmentsMap.put(filepath, attachmentStrs);
				} else {
					onUploadFailed(file);
				}
			} else {
				onUploadFailed(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			onUploadFailed(file);
		}
	}

	private void onUploadFailed(File file) {
		allowedPublish = false;
		HandlerUtil.sendMsgToHandler(sendHandler, 9, file.getName()
				+ "上传失败，请重新上传！", 0);
	}

	/**
	 * HttpURLConnection POST上传文件
	 * 
	 * @param uploadUrl
	 * @param filename
	 * @throws Exception
	 */
	private String httpUpload(String uploadUrl, File file, String finishToast,
			int cur) throws Exception {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		DataOutputStream dos = null;
		FileInputStream fis = null;
		String result = null;

		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = HttpRequestServers
					.getHttpURLConnection(url);

			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			String reqHeader = twoHyphens
					+ boundary
					+ end
					+ "Content-Disposition: form-data; name=\"upload\"; filename=\""
					+ file.getName() + "\"" + end
					+ "Content-Type: application/octet-stream" + end + end;
			String reqEnder = end + twoHyphens + boundary + twoHyphens + end;

			long totalLength = file.length();
			httpURLConnection.setFixedLengthStreamingMode(reqHeader.length()
					+ (int) (totalLength) + reqEnder.length());

			dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(reqHeader);
			fis = new FileInputStream(file);
			L.i(TAG, "httpUpload totalLength=" + totalLength);
			long uploadSize = 0;
			int progress = 0;
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				if (!allowedPublish || num != cur) {
					return null;
				}
				dos.write(buffer, 0, count);
				uploadSize += count;
				progress = (int) (uploadSize * 100 / totalLength);
				if (progress > 0) {
					L.d(TAG, "httpUpload uploadSize=" + uploadSize
							+ ",progress=" + progress);
				}

				HandlerUtil.sendMsgToHandler(sendHandler, 2, null, progress);
				if (progress >= 100) {
					HandlerUtil.sendMsgToHandler(sendHandler,
							HandlerUtil.getMessage(4, finishToast, 0, 0));
				}
			}

			dos.writeBytes(reqEnder);
			dos.flush();

			L.d(TAG, "httpUpload dos.size=" + dos.size());
			result = StreamUtil.readData(httpURLConnection.getInputStream());
			L.d(TAG, "httpUpload result=" + result);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			if (dos != null)
				try {
					dos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	@SuppressLint("HandlerLeak")
	private Handler sendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI(T.ErrStr);
				break;
			case 10:
				updateUI(T.TimeOutStr);
				break;
			case 1:
				updateUI("已发送！");
				break;
			case 2:
				if (null != dialogContent) {
					dialogContent.updatePBar(msg.arg1);
					if (null != msg.obj && 0 == msg.arg1) {
						if (1 == msg.arg2) { // 文件
							dialogContent.setFileIcon((File) msg.obj);
						} else if (-1 == msg.arg2) {
							dialogContent.setIcon((String) msg.obj);
						}
					}
				}
				break;
			case 3:
				if (null != dialogContent) {
					dialogContent.startNewFile((String) msg.obj);
				}
				break;

			case 4:
				if (null != dialogContent) {
					dialogContent.finishFile((String) msg.obj);
					dialogContent.setPublishProgress(msg.arg1);
				}
				break;
			case 5:
				if (null != dialogContent) {
					dialogContent.finishFile((String) msg.obj);
					dialogContent.setPublishProgress(msg.arg1);
					dialogContent.showDialogBar();
				}

				if (msg.arg1 == View.GONE) {
					Button b = pDialog.getButton(Dialog.BUTTON_NEGATIVE);
					b.setTag(new Object());
					b.setText("后台发送");
				}
				break;
			case 9:
				showTip((String) msg.obj);
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.cancel();
				}
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 加载完成提醒
	 * 
	 * @param msg
	 */
	private void updateUI(String msg) {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.cancel();
		}
		T.show(this, msg);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_send).setVisible(true);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			BaseData.hideKeyBoard(this);
			backPrompt();
			return true;
		case R.id.action_send:
			sendM();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void sendM() {
		BaseData.hideKeyBoard(this);
		mailTitle = title_et.getText().toString();
		mailContent = content_et.getText().toString();
		mailContent = StringUtil.trimInnerSpaceStr(mailContent).replaceAll(
				"\n", "<br />");
		mailContent += sign;
		if (isShowSourceMail) {
			mailContent += sourceMailContent;
		}

		if (!autoAddReceiver())
			return;
		if (!autoAddShare())
			return;
		if (!autoAddSend())
			return;

		if (receivers.size() == 0 && shareTos.size() == 0
				&& sendTos.size() == 0) {
			showTip("收件人不能为空！");
			return;
		} else if ("".equals(mailTitle)) {
			mailTitle = "无主题";
		}

		sendMail(++num);
	}

	/**
	 * 捕捉键盘事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		spdDialog.cancelProgressDialog(null);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backPrompt();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 返回提示
	 */
	private void backPrompt() {
		mailTitle = title_et.getText().toString();
		mailContent = content_et.getText().toString();
		if ((mailTitle != null && !mailTitle.trim().equals(""))
				|| (mailContent != null && !mailContent.trim().equals("") && !mailContent
						.trim().equals(sign)) || attachmentsList.size() > 0
				|| !mAdditiveFileLvAdapter_Choosed.getBeans().isEmpty()) {
			// 已输入过内容
			ConfirmExit();
			return;
		}
		if (receivers.size() != 0 || shareTos.size() != 0
				|| sendTos.size() != 0) {
			// 已选过收件人
			ConfirmExit();
			return;
		}
		finish();
	}

	/** 退出确认 */
	private void ConfirmExit() {
		new AlertDialog.Builder(this).setTitle("提示").setMessage("是否放弃发送？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {// 退出按钮
							@Override
							public void onClick(DialogInterface dialog, int i) {
								MailCreateActivity.this.finish();
							}
						}).setNegativeButton("否", null).show();// 显示对话框
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.pic:
			choosePicture();
			closeGroup();
			break;
		case R.id.file:
			if (mCreateFile == null) {
				mCreateFile = new AdditiveFileDialog(this);
			}
			mCreateFile.setFiles(mAdditiveFileLvAdapter_Choosed.getBeans());
			mCreateFile.createDialog();
			break;
		case R.id.choose1:
			startChoose(ADD_RECEIVER);
			break;
		case R.id.choose2:
			startChoose(ADD_SHARETO);
			break;
		case R.id.choose3:
			startChoose(ADD_SENDTO);
			break;
		case R.id.key:
			openGroup();
			break;
		case R.id.ll_name:
			requestTag = ADD_RECEIVER;
			showAddImg();
			closeGroup();
			break;
		case R.id.ll_shareTo:
			requestTag = ADD_SHARETO;
			showAddImg();
			break;
		case R.id.ll_sendTo:
			requestTag = ADD_SENDTO;
			showAddImg();
			break;
		}
	}

	private void showAddImg() {
		switch (requestTag) {
		case ADD_RECEIVER:
			choose1.setVisibility(View.VISIBLE);
			choose2.setVisibility(View.VISIBLE);
			choose3.setVisibility(View.VISIBLE);
			receiver_et.setVisibility(View.VISIBLE);
			receiver_et.requestFocus();
			shareTo_et.setVisibility(View.VISIBLE);
			sendTo_et.setVisibility(View.VISIBLE);
			break;
		case ADD_SHARETO:
			choose1.setVisibility(View.VISIBLE);
			choose2.setVisibility(View.VISIBLE);
			choose3.setVisibility(View.VISIBLE);
			receiver_et.setVisibility(View.VISIBLE);
			shareTo_et.setVisibility(View.VISIBLE);
			shareTo_et.requestFocus();
			sendTo_et.setVisibility(View.VISIBLE);
			break;
		case ADD_SENDTO:
			choose1.setVisibility(View.VISIBLE);
			choose2.setVisibility(View.VISIBLE);
			choose3.setVisibility(View.VISIBLE);
			receiver_et.setVisibility(View.VISIBLE);
			shareTo_et.setVisibility(View.VISIBLE);
			sendTo_et.setVisibility(View.VISIBLE);
			sendTo_et.requestFocus();
			break;
		default:
			break;
		}
	}

	private void closeGroup() {
		if (shareTos.isEmpty() && sendTos.isEmpty()
				&& "".equals(shareTo_et.getText().toString())
				&& "".equals(sendTo_et.getText().toString())) {
			keyText.setVisibility(View.VISIBLE);
			group.setVisibility(View.GONE);
		}
	}

	private void openGroup() {
		keyText.setVisibility(View.GONE);
		group.setVisibility(View.VISIBLE);
		requestTag = ADD_SHARETO;
		showAddImg();
	}

	private void startChoose(int which) {
		requestTag = which;
		Intent intent = new Intent(MailCreateActivity.this,
				FriendsChooserActivity.class);
		intent.putExtra("datas", (Serializable) getOriArray(requestTag));
		intent.putExtra("type", 1);
		startActivityForResult(intent, which);
	}

	private List<FriendSimpleBean> getOriArray(int which) {
		switch (which) {
		case ADD_RECEIVER:
			return mapToArray(receivers);
		case ADD_SENDTO:
			return mapToArray(sendTos);
		case ADD_SHARETO:
			return mapToArray(shareTos);
		default:
			return null;
		}
	}

	private List<FriendSimpleBean> mapToArray(
			LinkedHashMap<String, FriendSimpleBean> datas) {
		List<FriendSimpleBean> list = new ArrayList<FriendSimpleBean>();
		for (FriendSimpleBean bean : datas.values()) {
			list.add(bean);
		}
		return list;
	}

	/**
	 * 获得发送地址-memberId或者Email
	 * 
	 * @param infos
	 * @param isMember
	 * @return
	 */
	private String[] transStr(HashMap<String, FriendSimpleBean> infos,
			boolean isMember) {
		if (null == infos || infos.size() == 0) {
			return null;
		}
		ArrayList<String> intList = new ArrayList<String>();
		ArrayList<String> strList = new ArrayList<String>();

		for (FriendSimpleBean s : infos.values()) {
			int id = s.getMemberId();
			if (id == 0) {
				strList.add(s.getMemberName());
			} else {
				intList.add(id + "");
			}
		}
		if (isMember) {
			int intSize = intList.size();
			String[] ints = new String[intSize];
			for (int i = 0; i < intSize; i++) {
				ints[i] = intList.get(i);
			}
			return ints;
		} else {
			int strSize = strList.size();
			String[] strs = new String[strSize];
			for (int i = 0; i < strSize; i++) {
				strs[i] = strList.get(i);
			}
			return strs;
		}
	}

	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		if (resultCode == RESULT_OK) {
			String filePath = null;
			Uri uri = null;
			switch (requestCode) {
			case INSERT_PHOTO://
				try {
					if (data != null) {
						uri = data.getData();
						L.i(TAG, "onActivityResult uri=" + uri);
						if (uri != null) {
							filePath = queryPhoto(uri);
						} else {
							File saveFile = new File(ConstantUtil.CAMERA_PATH);
							File picture = new File(saveFile, pictureName);
							uri = Uri.fromFile(picture);
							filePath = uri.getPath();
							pictureName = null;
						}
					} else {
						File saveFile = new File(ConstantUtil.CAMERA_PATH);
						File picture = new File(saveFile, pictureName);
						uri = Uri.fromFile(picture);
						filePath = uri.getPath();
						pictureName = null;
					}
					AdditiveFileBean file = new AdditiveFileBean();
					file.setUri(filePath);
					int length = filePath.lastIndexOf("/");
					file.setName(filePath.substring(length + 1));
					File temp = new File(filePath);
					file.setSize(temp.length());
					file.setMime_type(FileUtil.getMIMEType(temp));
					filePath = null;
					temp = null;
					showFileBox();
					if (mAdditiveFileLvAdapter_Choosed.contains(file.getUri()) != -1) {
						T.show(App.app, StringUtil.merge(file.getName(), "已存在"));
						file = null;
					} else {
						mAdditiveFileLvAdapter_Choosed.addAtFirst(file);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case AdditiveFileDialog.VIDEO:
				if (null != data) {
					uri = data.getData();
					L.i(TAG, "onActivityResult uri=" + uri);
					if (null != uri) {
						Cursor cursor = null;
						try {
							cursor = getContentResolver().query(
									uri,
									new String[] { Media.DATA,
											Media.DISPLAY_NAME, Media.SIZE,
											Media.MIME_TYPE,
											Video.VideoColumns.DURATION },
									null, null, null);
							if (null == cursor || cursor.getCount() < 0) {
								filePath = uri.getPath();
								String whereClause = Media.DATA + " = '"
										+ filePath + "'";
								cursor = getContentResolver().query(
										Media.EXTERNAL_CONTENT_URI,
										new String[] { Media.DATA,
												Media.DISPLAY_NAME, Media.SIZE,
												Media.MIME_TYPE,
												Video.VideoColumns.DURATION },
										whereClause, null, null);
							}
							AdditiveFileBean file = new AdditiveFileBean();
							if (null != cursor && cursor.getCount() > 0) {
								cursor.moveToFirst();
								file.setName(cursor.getString(cursor
										.getColumnIndex(Media.DISPLAY_NAME)));
								file.setUri(cursor.getString(cursor
										.getColumnIndex(Media.DATA)));
								file.setTime(cursor.getLong(cursor
										.getColumnIndex(Video.VideoColumns.DURATION)));
								file.setSize(cursor.getLong(cursor
										.getColumnIndex(Media.SIZE)));
								file.setMime_type(cursor.getString(cursor
										.getColumnIndex(Media.MIME_TYPE)));
							} else {
								filePath = uri.getPath();
								file.setUri(filePath);
								int length = filePath.lastIndexOf("/");
								file.setName(filePath.substring(length + 1));
								File temp = new File(filePath);
								file.setSize(temp.length());
								file.setMime_type(FileUtil.getMIMEType(temp));
								filePath = null;
								temp = null;
							}
							showFileBox();
							if (mAdditiveFileLvAdapter_Choosed.contains(file
									.getUri()) != -1) {
								T.show(App.app,
										StringUtil.merge(file.getName(), "已存在"));
								file = null;
							} else {
								mAdditiveFileLvAdapter_Choosed.addAtFirst(file);
							}

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (null != cursor) {
								cursor.close();
								cursor = null;
							}
						}
					}
				}
				break;
			case AdditiveFileDialog.AUDIO:
				if (null != data) {
					uri = data.getData();
					L.i(TAG, "onActivityResult uri=" + uri);
					if (null != uri) {
						Cursor cursor = null;
						try {
							cursor = getContentResolver().query(
									uri,
									new String[] { Media.DATA,
											Media.DISPLAY_NAME, Media.SIZE,
											Media.MIME_TYPE,
											Audio.AudioColumns.DURATION },
									null, null, null);
							if (null == cursor || cursor.getCount() < 0) {
								filePath = uri.getPath();
								String whereClause = Media.DATA + " = '"
										+ filePath + "'";
								try {
									cursor = getContentResolver()
											.query(Media.EXTERNAL_CONTENT_URI,
													new String[] {
															Media.DATA,
															Media.DISPLAY_NAME,
															Media.SIZE,
															Media.MIME_TYPE,
															Audio.AudioColumns.DURATION },
													whereClause, null, null);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							AdditiveFileBean file = new AdditiveFileBean();
							if (null != cursor && cursor.getCount() > 0) {
								cursor.moveToFirst();
								file.setName(cursor.getString(cursor
										.getColumnIndex(Media.DISPLAY_NAME)));
								file.setUri(cursor.getString(cursor
										.getColumnIndex(Media.DATA)));
								file.setTime(cursor.getLong(cursor
										.getColumnIndex(Video.VideoColumns.DURATION)));
								file.setSize(cursor.getLong(cursor
										.getColumnIndex(Media.SIZE)));
								file.setMime_type(cursor.getString(cursor
										.getColumnIndex(Media.MIME_TYPE)));
							} else {
								filePath = uri.getPath();
								file.setUri(filePath);
								int length = filePath.lastIndexOf("/");
								file.setName(filePath.substring(length + 1));
								File temp = new File(filePath);
								file.setSize(temp.length());
								file.setMime_type(FileUtil.getMIMEType(temp));
								MediaPlayer mp = new MediaPlayer();
								mp.reset();
								mp.setDataSource(filePath);
								mp.prepare();
								file.setTime(mp.getDuration());
								filePath = null;
								temp = null;
							}
							showFileBox();
							if (mAdditiveFileLvAdapter_Choosed.contains(file
									.getUri()) != -1) {
								T.show(App.app,
										StringUtil.merge(file.getName(), "已存在"));
								file = null;
							} else {
								mAdditiveFileLvAdapter_Choosed.addAtFirst(file);
							}

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (null != cursor) {
								cursor.close();
								cursor = null;
							}
						}
					}
				}
				break;
			case AdditiveFileDialog.CLOUDFILE:
				showFileBox();
				List<Object> beans = (List<Object>) data
						.getSerializableExtra(BundleKey.CHOOSEDS);
				mAdditiveFileLvAdapter_Choosed.flush(beans);
				break;
			case AdditiveFileDialog.OTHAR:
				AdditiveFileBean file = new AdditiveFileBean();
				filePath = data.getStringExtra("filePath");
				file.setUri(filePath);
				int length = filePath.lastIndexOf("/");
				file.setName(filePath.substring(length + 1));
				File temp = new File(filePath);
				file.setSize(temp.length());
				file.setMime_type(FileUtil.getMIMEType(temp));
				filePath = null;
				temp = null;
				showFileBox();
				if (mAdditiveFileLvAdapter_Choosed.contains(file.getUri()) != -1) {
					T.show(App.app, StringUtil.merge(file.getName(), "已存在"));
					file = null;
				} else {
					mAdditiveFileLvAdapter_Choosed.addAtFirst(file);
				}
				break;
			case ADD_RECEIVER:
				List<FriendSimpleBean> map0 = (List<FriendSimpleBean>) data
						.getSerializableExtra("datas");
				if (null != map0) {
					receivers = listToMap(map0);
				}
				showReceivers(requestCode);
				break;
			case ADD_SENDTO:
				List<FriendSimpleBean> map1 = (List<FriendSimpleBean>) data
						.getSerializableExtra("datas");
				if (null != map1) {
					sendTos = listToMap(map1);
				}
				showReceivers(requestCode);
				break;
			case ADD_SHARETO:
				List<FriendSimpleBean> map3 = (List<FriendSimpleBean>) data
						.getSerializableExtra("datas");
				if (null != map3) {
					shareTos = listToMap(map3);
				}
				showReceivers(requestCode);
				break;

			}
		}
	}

	public void showFileBox() {
		fileLayout.setVisibility(View.VISIBLE);
		if (mAdditiveFileLvAdapter_Choosed.isInit())
			mVs_FileLv.inflate();
	}

	@SuppressLint("HandlerLeak")
	private Handler hideFile = new Handler() {
		public void handleMessage(Message msg) {
			fileLayout.setVisibility(View.GONE);
		};
	};

	private LinkedHashMap<String, FriendSimpleBean> listToMap(
			List<FriendSimpleBean> list) {
		LinkedHashMap<String, FriendSimpleBean> map = new LinkedHashMap<String, FriendSimpleBean>();
		for (FriendSimpleBean bean : list) {
			map.put("" + bean.getMemberId(), bean);
		}
		return map;
	}

	/**
	 * 查找本地图片
	 * 
	 * @param uri
	 * @return
	 */
	private String queryPhoto(Uri uri) {
		String filepath = "";
		Cursor cursor = getContentResolver()
				.query(uri,
						new String[] { "_data", "_display_name", "_size",
								"mime_type" }, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			filepath = cursor.getString(0); // 图片文件路径
			cursor.close();
		} else {
			filepath = uri.getPath();
		}
		return filepath;
	}

	/**
	 * 删除图片、文件
	 * 
	 * @param view
	 */
	public void deleteView(View view) {
		fileLayout.removeView(view);
		if (fileLayout.getChildCount() == 1) {
			fileLayout.setVisibility(View.GONE);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void choosePicture() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"上传图片")
		// .setItems(new String[] { "本地上传", "拍照上传", "取消上传" },
		// new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(this, R.layout.choice_item,
								new String[] { "本地上传", "拍照上传" }),// , "取消上传"
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Intent intent = new Intent(
											Intent.ACTION_GET_CONTENT);
									intent.setType("image/*");
									startActivityForResult(Intent
											.createChooser(intent, "选择图片"),
											INSERT_PHOTO);
									break;
								case 1:
									Intent intent1 = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									pictureName = System.currentTimeMillis()
											+ ".jpg";
									File saveFile = new File(
											ConstantUtil.CAMERA_PATH);
									if (!saveFile.exists()) {
										saveFile.mkdirs();
									}
									intent1.putExtra(MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(saveFile,
													pictureName)));
									startActivityForResult(intent1,
											INSERT_PHOTO);
									break;
								// case 2:
								// dialog.dismiss();
								// break;
								}
							}
						});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		closeGroup();
		return false;
	}

	public static LinkedHashMap<String, FriendSimpleBean> getOri(int which) {
		if (MailCreateActivity.ADD_SENDTO == which) {
			return sendTos;
		} else if (MailCreateActivity.ADD_SHARETO == which) {
			return shareTos;
		} else {
			return receivers;
		}
	}

}

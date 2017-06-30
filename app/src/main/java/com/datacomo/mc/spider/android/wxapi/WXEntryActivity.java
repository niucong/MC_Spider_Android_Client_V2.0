package com.datacomo.mc.spider.android.wxapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.datacomo.mc.spider.android.BaseUMengActivity;
import com.datacomo.mc.spider.android.LoginActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WXEntryActivity extends BaseUMengActivity implements // OnClickListener,
		IWXAPIEventHandler {
	private static final String TAG = "WXEntryActivity";

	/** true：分享到朋友圈， false：分享给微信朋友 */
	private boolean flag = false;
	private boolean flagCancel = false;

	private IWXAPI api;
	private String shareTopic = "";
	private String id_Quubo = "";
	private String shareImageUrl = "";
	/**
	 * 0：文本 1、照片 2、音频 3、视频
	 */
	private int type = 0;

	private byte[] data = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wx_radio);
		api = WXAPIFactory.createWXAPI(this, ConstantUtil.APP_ID);

		Intent intent = getIntent();
		shareTopic = intent.getStringExtra("shareTopic");
		id_Quubo = intent.getStringExtra("objectId");
		data = intent.getByteArrayExtra("data");
		shareImageUrl = intent.getStringExtra("shareImageUrl");
		type = intent.getIntExtra("type", 0);

		L.i(TAG, "onCreate id_Quubo=" + id_Quubo + ",shareTopic=" + shareTopic
				+ ",shareImageUrl=" + shareImageUrl + ",type=" + type);

		if (Build.VERSION.SDK_INT < 14)
			findViewById(R.id.wx_radio_title).setVisibility(View.GONE);

		// RadioGroup group = new RadioGroup(this);
		// group.setPadding(25, 0, 0, 25);
		//
		// RadioButton mButton = new RadioButton(this);
		// mButton.setId(1);
		// mButton.setText("微信好友");
		// // if (Build.VERSION.SDK_INT > 14) {
		// // mButton.setTextColor(R.color.black);
		// // } else {
		// mButton.setTextColor(R.color.color_radiobutton);
		// // }
		// mButton.setChecked(true);
		// group.addView(mButton);
		//
		// RadioButton gButton = new RadioButton(this);
		// gButton.setId(2);
		// gButton.setText("微信朋友圈");
		// // if (Build.VERSION.SDK_INT > 14) {
		// // gButton.setTextColor(R.color.black);
		// // } else {
		// gButton.setTextColor(R.color.color_radiobutton);
		// // }
		// group.addView(gButton);

		RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
		// (RadioGroup) LayoutInflater.from(this).inflate(R.layout.wx_radio,
		// null);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio0:// 1:
					flag = false;
					break;
				case R.id.radio1:// 2:
					flag = true;
					break;
				}
			}
		});

		// new AlertDialog.Builder(this).setTitle("分享到微信").setView(group)
		// .setPositiveButton("分享", new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // if (data == null) {
		// shareToWeiXinReq();
		// // } else {
		// // sendReq(shareTopic, data);
		// // }
		// flagCancel = true;
		// }
		// })
		// .setNegativeButton("取消", new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// WXEntryActivity.this.finish();
		// }
		// }).setCancelable(false).show();

		findViewById(R.id.button_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						WXEntryActivity.this.finish();
					}
				});
		findViewById(R.id.button_share).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						shareToWeiXinReq();
						flagCancel = true;
					}
				});

		api.handleIntent(intent, this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		L.d(TAG, "onNewIntent...");

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		L.d(TAG, "onRestart..." + flagCancel);
		if (flagCancel) {
			finish();
			T.show(WXEntryActivity.this, "发送取消");
		}
	}

	@Override
	public void onReq(BaseReq req) {
		flagCancel = false;
		L.i(TAG, "onReq req.getType=" + req.getType());
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			LogicUtil.enter(this, LoginActivity.class, null, true);
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:

			break;
		default:
			break;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		flagCancel = false;
		L.d(TAG, "onResp resp.errCode=" + resp.errCode);
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "已分享！";
			if (id_Quubo != null && !"".equals(id_Quubo)) {
				new Thread() {
					public void run() {
						try {
							L.i(TAG, "onResp id_Quubo=" + id_Quubo);
							APIRequestServers.shareToThirdRecord(
									WXEntryActivity.this, "1", id_Quubo, "8");
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "发送取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送被拒绝";
			break;
		default:
			result = "发送返回";
			break;
		}
		T.show(WXEntryActivity.this, result);
		WXEntryActivity.this.finish();
	}

	private void shareToWeiXinReq() {
		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		if (type == 101 || type == 1) {//
			WXWebpageObject obj = new WXWebpageObject();
			obj.webpageUrl = shareImageUrl;
			msg.mediaObject = obj;
			msg.description = shareTopic;
			msg.title = "优优工作圈";
			msg.thumbData = data;
			req.transaction = buildTransaction(null);
			// } else if (type == 1) {
			// WXImageObject imgObj = new WXImageObject(data);
			// imgObj.imageUrl = shareImageUrl;
			// msg.mediaObject = imgObj;
			// // msg.description = shareTopic;
			// // msg.title = "优优工作圈";
			// // msg.thumbData = data;
			// req.transaction = buildTransaction("img");
		} else if (type == 2) {
			WXMusicObject musicObject = new WXMusicObject();
			musicObject.musicUrl = shareImageUrl;
			msg.mediaObject = musicObject;
			msg.description = shareTopic;
			// 发送文本类型的消息时，title字段不起作用
			msg.title = "优优工作圈";

			Bitmap thumb = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon);
			msg.thumbData = ImageDealUtil.bmpToByteArray(thumb, true);

			req.transaction = buildTransaction("music");
		} else {
			L.d(TAG, "shareToWeiXinReq data=null,shareTopic=" + shareTopic);
			// 初始化一个WXTextObject对象
			WXTextObject textObj = new WXTextObject();
			textObj.text = shareTopic;
			msg.mediaObject = textObj;
			msg.description = shareTopic;
			req.transaction = buildTransaction("text");
		}
		req.message = msg;
		req.scene = flag ? SendMessageToWX.Req.WXSceneTimeline
				: SendMessageToWX.Req.WXSceneSession;

		// 调用api接口发送数据到微信
		api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		L.d(TAG, "buildTransaction type=" + type);
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
}

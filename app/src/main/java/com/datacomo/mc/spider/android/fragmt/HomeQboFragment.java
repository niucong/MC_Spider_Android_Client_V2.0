package com.datacomo.mc.spider.android.fragmt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseActivity;
import com.datacomo.mc.spider.android.ChooseGroupsDialogActivity;
import com.datacomo.mc.spider.android.EditActivity;
import com.datacomo.mc.spider.android.FriendsChooserActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.PhotoGalleryActivity;
import com.datacomo.mc.spider.android.QChatActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.adapter.FaceTableAdapter;
import com.datacomo.mc.spider.android.adapter.InfoAdapter;
import com.datacomo.mc.spider.android.adapter.InfoSearchedAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberContactBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.ResultAll;
import com.datacomo.mc.spider.android.net.been.ResultMessageBean;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.GreetUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.MemberContactUtil;
import com.datacomo.mc.spider.android.util.PhotoUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;

@SuppressLint("ValidFragment")
public class HomeQboFragment extends BaseGroupFragment implements
		OnLoadMoreListener, OnClickListener {
	private final static String TAG = "HomeQboFragment";

	public LinearLayout rightll;
	private RefreshListView listView;
	private ImageView poster, iv_head;
	private TextView userName, invite, money;
	private TextView mood;
	private RelativeLayout headerView;

	public static boolean needRefresh;
	private String headimg_file = ConstantUtil.HEAD_PATH;
	private String headimg_name = "headimg.jpg";
	private File headPic;
	private Bitmap headimgbit;
	private String createTime = "0";
	private String trendId = "";

	private int searchStart = 1;
	private ArrayList<ResourceTrendBean> infos;
	private InfoAdapter infoAdapter;

	private ArrayList<ResultMessageBean> searchInfos;
	private InfoSearchedAdapter infoSearchAdapter;

	private LoadInfoTask task;
	private boolean searchState, isLoading;
	private GridView faces;
	private PopupWindow facePPW;
	private boolean isIntroduceToOther;
	private int mFriendState;
	private String mHeadUrl;

	private HomePgActivity homePg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homePg = (HomePgActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		listView = new RefreshListView(homePg);
		listView.setDivider(homePg.getResources().getDrawable(
				R.drawable.list_divider));
		listView.setHeaderDividersEnabled(true);
		findViews();
		setViews();
		return listView;
	}

	@Override
	public void onPause() {
		super.onPause();
		// int lastPositon = listView.getFirstVisiblePosition();
		// getArguments().putInt("lastPositon", lastPositon);
	}

	private void findViews() {
		headerView = (RelativeLayout) LayoutInflater.from(homePg).inflate(
				R.layout.space_info, null);
		poster = (ImageView) headerView.findViewById(R.id.img_poster);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) poster
				.getLayoutParams();
		if (null == lp) {
			lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
		}
		lp.height = homePg.sW * 3 / 5;
		poster.setLayoutParams(lp);
		poster.requestLayout();
		iv_head = (ImageView) headerView.findViewById(R.id.img_header);
		userName = (TextView) headerView.findViewById(R.id.name);
		invite = (TextView) headerView.findViewById(R.id.invite);
		invite.setOnClickListener(this);
		money = (TextView) headerView.findViewById(R.id.money);
		mood = (TextView) headerView.findViewById(R.id.mood);
		addList();

		faces = new GridView(homePg);
		faces.setNumColumns(5);
		faces.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		faces.setSelector(R.drawable.nothing);
		faces.setAdapter(new FaceTableAdapter(homePg));
		faces.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BaseActivity.spdDialog.showProgressDialog("正在处理中...");
				new GreetTask(arg2).execute();
			}
		});
		facePPW = new PopupWindow(faces, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		facePPW.setBackgroundDrawable(homePg.getResources().getDrawable(
				R.drawable.nothing));
		facePPW.setAnimationStyle(R.style.midmenu_ani_bottom);
		facePPW.setFocusable(true);
		facePPW.setOutsideTouchable(true);

		// TDOD setMidChosen(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_header:
			checkHead((String) v.getTag());
			break;
		case R.id.invite:
			ChooseGroupBean bean = new ChooseGroupBean();
			bean.setName(new String[] { homePg.name }, homePg.number);
			bean.setTitle("加入朋友圈");
			bean.setChosenGroupMap(null);
			Bundle bundle = new Bundle();
			bundle.putSerializable(BundleKey.CHOOSEGROUPBEAN, bean);
			// LogicUtil.enter(homePg, ChooseGroupsDialogActivity.class, bundle,
			// HomePgActivity.ADD_GROUP);
			Intent i = new Intent(homePg, ChooseGroupsDialogActivity.class);
			i.putExtras(bundle);
			startActivityForResult(i, HomePgActivity.ADD_GROUP);
			break;
		}
	}

	private void checkHead(String url) {
		if (null == url || "".equals(url)) {
			return;
		}
		Bundle bundle = new Bundle();
		int index = 1;
		bundle.putInt("index", (index - 1));
		bundle.putString("type", "type_none");
		bundle.putBoolean("isOriginal", true);
		bundle.putString("url", url);
		LogicUtil.enter(homePg, PhotoGalleryActivity.class, bundle, false);
	}

	private void setViews() {
		infos.clear();
		infoAdapter.notifyDataSetChanged();
		new SetMemberSkin().execute();
		new SetMemberTask().execute();
		loadInfo(true);
	}

	private void addList() {
		listView.setBackgroundResource(R.drawable.bg_main_white);
		listView.setFadingEdgeLength(0);
		listView.setFastScrollEnabled(true);
		listView.setCacheColorHint(Color.TRANSPARENT);
		headerView.setBackgroundColor(homePg.getResources().getColor(
				R.color.bg_header));
		listView.setPadding(0, -homePg.sW / 100, 0, 0); // 有道白线
		listView.addHeaderView(headerView);
		infos = new ArrayList<ResourceTrendBean>();
		infoAdapter = new InfoAdapter(homePg, infos, listView);
		infoAdapter.setFragment(this);
		listView.setAdapter(infoAdapter);
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (totalItemCount > 10
						&& firstVisibleItem + visibleItemCount == totalItemCount) {
					view.setSelection(view.getLastVisiblePosition() + 1);
					if (!isLoading) {
						loadInfo(false);
					}
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (needRefresh) {
			loadInfo(true);
			new SetMemberTask().execute();
			needRefresh = false;
		}
	}

	private String getMood() {
		return mood.getText().toString();
	}

	private void onRefersh(Intent data) {
		int position = data.getIntExtra("position", -1);
		if (position != -1) {
			ResourceTrendBean tBean = (ResourceTrendBean) data
					.getSerializableExtra("Trend");
			if (tBean != null) {
				infos.remove(position);
				infos.add(position, tBean);
				infoAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		// 处理拍照返回数据
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 10) {
				onRefersh(data);
			} else {
				Uri uri = null;
				try {
					switch (requestCode) {
					case HomePgActivity.FROM_CAMERA:
						File tempFile = new File(headimg_file);
						File picture = new File(tempFile, headimg_name);
						uri = Uri.fromFile(picture);
						startPhotoZoom(uri, headimg_file, headimg_name);
						break;
					case HomePgActivity.FROM_GALLERY:
						uri = data.getData();
						startPhotoZoom(uri, headimg_file, headimg_name);
						break;
					case HomePgActivity.FROM_CROP:
						File saveFile = new File(headimg_file);
						headPic = new File(saveFile, headimg_name);
						headimgbit = getHeadFilePath(homePg,
								Uri.fromFile(headPic));
						iv_head.setImageBitmap(headimgbit);

						if (headPic.exists()) {
							L.i(TAG, "onActivityResult headPic.length="
									+ headPic.length());
							uploadHead(headPic);
						}
						break;
					case HomePgActivity.SEND_MOOD:
						String newMood = data.getStringExtra("feelword");
						L.d(TAG, "onActivityResult mood=" + mood);
						if (newMood != null && !newMood.equals("")
								&& !newMood.equals(getMood())) {
							setMood(0, newMood);
							loadInfo(true);
						}
						break;
					case HomePgActivity.ADD_GROUP:
						Bundle bundle = data.getExtras();
						String[] chosenIds = bundle
								.getStringArray(BundleKey.CHOOSEDS);
						BaseActivity.spdDialog.showProgressDialog("正在处理中...");
						new AddFriendTask(chosenIds).execute(homePg.id);
						break;
					case FriendsChooserActivity.RESCODE:
						String[] shareFriendIds = data
								.getStringArrayExtra("ids");
						if (null != shareFriendIds && shareFriendIds.length > 0) {
							if (isIntroduceToOther) {
								BaseActivity.spdDialog
										.showProgressDialog("推荐中...");
								new RequestTask(1).execute(
										new String[] { homePg.id },
										shareFriendIds);
							} else {
								BaseActivity.spdDialog
										.showProgressDialog("引荐中...");
								new RequestTask(1).execute(shareFriendIds,
										new String[] { homePg.id });
							}
						}
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 朋友
	 */
	class RequestTask extends AsyncTask<Object, Integer, MCResult> {
		private int mType;
		private Object[] mParams;

		public RequestTask(int type) {
			mType = type;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			mParams = params;
			MCResult mcResult = null;
			switch (mType) {
			case 1:
				try {
					mcResult = APIRequestServers.recommendFriend(App.app,
							(String[]) mParams[0], null, null, null,
							(String[]) mParams[1], null, null, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}

			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			BaseActivity.spdDialog.cancelProgressDialog(null);
			if (null == result || result.getResultCode() != 1) {
				homePg.showTip(T.ErrStr);
				return;
			}
			if (isIntroduceToOther) {
				homePg.showTip("已推荐！");
			} else {
				homePg.showTip("已引荐！");
			}
		}
	}

	class AddFriendTask extends AsyncTask<String, Integer, MCResult> {
		private String[] groupIds;

		public AddFriendTask(String[] groupIds) {
			this.groupIds = groupIds;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.addFriendToGroup(homePg, params[0],
						groupIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			BaseActivity.spdDialog.cancelProgressDialog(null);
			if (null == mcResult || 1 != mcResult.getResultCode()) {
				homePg.showTip(T.ErrStr);
			} else {
				invite.setVisibility(View.GONE);
				onFriendStateChanged(1);
				new Thread(new Runnable() {
					@Override
					public void run() {
						UpdateFriendListThread.updateFriendList(homePg, null);
					}
				});
			}
		}

	}

	/**
	 * 0-自己 1-朋友 2-朋友的朋友 3-申请状态 4-陌生人
	 * 
	 * @param state
	 */
	private void onFriendStateChanged(int state) {
		mFriendState = 1;
		refreshRightMenu(homePg);
	}

	private void deleteFriend() {
		new AlertDialog.Builder(homePg).setTitle("提示")
				.setMessage("是否与“" + homePg.name + "”解除朋友关系？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new RemoveFriendTask(homePg, Integer.valueOf(homePg.id))
								.execute();
					}
				}).setNegativeButton("否", null).show();
	}

	class RemoveFriendTask extends AsyncTask<Void, Integer, MCResult> {
		int friendId;
		Context c;

		public RemoveFriendTask(Context context, int fId) {
			c = context;
			friendId = fId;
			BaseActivity.spdDialog.showProgressDialog("正在处理中...");
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			try {
				mc = APIRequestServers.removeFriendFromGroup(c, friendId + "",
						null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			BaseActivity.spdDialog.cancelProgressDialog(null);
			if (null != result && 1 == result.getResultCode()) {
				homePg.showTip("您已和 " + homePg.name + " 解除朋友关系！");
				ArrayList<Integer> ids = new ArrayList<Integer>();
				ids.add(friendId);
				FriendListService.getService(c).deleteList(ids);
				onFriendStateChanged(4);
				homePg.finish();
			} else {
				homePg.showTip(T.ErrStr);
			}
		}

	}

	/**
	 * 获取图片路径并上传
	 * 
	 * @param uriString
	 **/
	private void uploadHead(final File file) {
		APIRequestServers.uploadFile(homePg, file, UploadMethodEnum.UPLOADHEAD,
				null);
	}

	/**
	 * 获取返回图片的路径
	 * 
	 * @param uri
	 */
	public static Bitmap getHeadFilePath(Context c, Uri uri) {
		String sUri = uri.toString();
		String imgPath = null;
		// 如果是从系统gallery取图片，返回一个contentprovider的uri
		if (sUri.startsWith("content://")) {
			Cursor cursor = c.getContentResolver().query(uri, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				imgPath = cursor.getString(1); // 图片文件路径
			} else {

			}
		} else if (sUri.startsWith("file://")
				&& (sUri.endsWith(".png") || sUri.endsWith(".jpg") || sUri
						.endsWith(".gif"))) {
			// 如果从某些第三方软件中选取文件，返回的是一个文件具体路径。
			imgPath = sUri.replace("file://", "");
		} else if (sUri.startsWith(ConstantUtil.SDCARD_PATH)) {
			// 直接获取临时图片地址
			imgPath = sUri;
		} else {
			// 返回的uri不合法或者文件不是图片。
		}
		L.d(TAG, "getHeadFilePath imgPath=" + imgPath);
		return PhotoUtil.getBitmapFromFile(new File(imgPath),
				DensityUtil.dip2px(c, 180), DensityUtil.dip2px(c, 180));
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 *            图片路径
	 **/
	public void startPhotoZoom(Uri uri, String path, String name) {
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType(uri, "image/*");// 设置要裁剪的图片
		cropIntent.putExtra("crop", "true");// crop=true 裁剪页面.
		// 设置其他信息：
		cropIntent.putExtra("aspectX", 1);// 设置裁剪框的比例.
		cropIntent.putExtra("aspectY", 1);// x:y=1:1
		// outputX outputY 是裁剪图片宽高
		// cropIntent.putExtra("outputX", 1200);
		// cropIntent.putExtra("outputY", 1200);
		cropIntent.putExtra("return-data", "false");
		File saveFile = new File(path);
		File picture = new File(saveFile, name);
		cropIntent.putExtra("output", Uri.fromFile(picture));// 保存输出文件
		cropIntent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(cropIntent, HomePgActivity.FROM_CROP);
	}

	class SetMemberTask extends AsyncTask<Void, Integer, MCResult> {

		protected void onPostExecute(MCResult result) {
			if (result == null || result.getResultCode() != 1) {
				homePg.finish();
				homePg.showTip(T.ErrStr);
			} else {
				userName.setText(homePg.name);
				try {
					MemberBean memberBean = (MemberBean) result.getResult();
					MemberBasicBean mberBean = (MemberBasicBean) memberBean
							.getBasicInfo();
					homePg.name = mberBean.getMemberName();
					String friendRemarkName = memberBean.getFriendRemarkName();
					if (friendRemarkName != null
							&& !"".equals(friendRemarkName)
							&& !friendRemarkName.equals(homePg.name)) {
						userName.setText(friendRemarkName + "（" + homePg.name
								+ "）");
					} else {
						userName.setText(homePg.name);
					}

					MemberHeadBean memberHeadBean = mberBean.getHeadImage();
					String head_Url = memberHeadBean.getHeadUrl()
							+ memberHeadBean.getHeadPath();
					iv_head.setTag(head_Url);
					iv_head.setOnClickListener(homePg);
					if ((GetDbInfoUtil.getMemberId(homePg) + "")
							.equals(homePg.id)) {
						new UserBusinessDatabase(homePg).updateHeadUrlPath(
								App.app.share.getSessionKey(), head_Url);
					}
					head_Url = ThumbnailImageUrl.getThumbnailHeadUrl(head_Url,
							HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
					MyFinalBitmap.setHeader(homePg, iv_head, head_Url);

					MemberContactBean bean_MemberContact = memberBean
							.getContactInfo();
					if (null != bean_MemberContact) {
						homePg.number = bean_MemberContact.getMemberPhone();
					}
					mFriendState = memberBean.getFriendStatus();
					rightll = new LinearLayout(homePg);
					rightll.setOrientation(LinearLayout.VERTICAL);
					rightll.setBackgroundResource(R.drawable.bg_rightmenu);
					refreshRightMenu(homePg);

					if (homePg.menu != null) {
						if (homePg.menu.findItem(R.id.action_write_mood) != null)
							homePg.menu.findItem(R.id.action_write_mood)
									.setVisible(false);
						if (homePg.userSelf) {
							homePg.menu.findItem(R.id.action_search)
									.setVisible(true);
							homePg.menu.findItem(R.id.action_write).setVisible(
									true);
							homePg.menu.findItem(R.id.action_more).setVisible(
									false);

							searchInfos = new ArrayList<ResultMessageBean>();
							infoSearchAdapter = new InfoSearchedAdapter(
									getActivity(), searchInfos, listView);
						} else {
							homePg.menu.findItem(R.id.action_search)
									.setVisible(false);
							homePg.menu.findItem(R.id.action_write).setVisible(
									false);
							homePg.menu.findItem(R.id.action_more).setVisible(
									true);
						}
					}

					if (mFriendState == 0) {
						// 获取自己的圈币数量
						int memberGold = memberBean.getAccountInfo()
								.getMemberGold();
						money.setText("" + memberGold);
						money.setClickable(false);
						money.setVisibility(View.VISIBLE);
						homePg.findViewById(R.id.money_text).setVisibility(
								View.VISIBLE);
						ImageView img_camera = (ImageView) homePg
								.findViewById(R.id.img_camera);
						img_camera.setVisibility(View.VISIBLE);
						img_camera.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlterHead();
							}
						});
						iv_head.setOnLongClickListener(new OnLongClickListener() {

							@Override
							public boolean onLongClick(View arg0) {
								setAlterHead();
								return true;
							}
						});
						invite.setVisibility(View.GONE);
					} else {
						homePg.findViewById(R.id.img_camera).setVisibility(
								View.GONE);
						homePg.findViewById(R.id.money_text).setVisibility(
								View.GONE);
						money.setVisibility(View.GONE);
						if (mFriendState != 1) {
							invite.setVisibility(View.VISIBLE);
						}
					}

					int sex = 0;
					try {
						sex = mberBean.getMemberSex();
					} catch (Exception e) {
						e.printStackTrace();
					}
					String mMood = mberBean.getFeelingWord();
					if (mMood != null && !mMood.equals("")) {
						mMood = mMood.replace("&nbsp;", "");
						setMood(mFriendState, mMood);
					} else {
						if (0 == mFriendState) {
							setMood(mFriendState, "发表个心情吧~~~~");
						} else if (sex == 1) {
							setMood(mFriendState, "他还没有发表过心情哦~~~~");
						} else if (sex == 2) {
							setMood(mFriendState, "她还没有发表过心情哦~~~~");
						} else {
							setMood(mFriendState, "TA还没有发表过心情哦~~~~");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			try {
				return APIRequestServers.getMemberBasicInfo(homePg, homePg.id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		};

	}

	class SetMemberSkin extends AsyncTask<Void, Integer, MCResult> {

		protected void onPostExecute(MCResult result) {
			if (result == null || result.getResultCode() != 1) {
				// showTip(T.ErrStr);
			} else {
				String memberSkin = (String) result.getResult();
				MyFinalBitmap.setSkin(homePg, poster, memberSkin);
			}
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			try {
				return APIRequestServers.memberSkin(homePg, homePg.id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		};
	}

	public void loadInfo(boolean isRefresh) {
		try {
			stopTask();
			task = new LoadInfoTask(isRefresh);
			task.execute();
		} catch (RejectedExecutionException re) {
			re.printStackTrace();
		}
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	class LoadInfoTask extends AsyncTask<Void, Integer, MCResult> {
		private boolean isRefresh;

		public LoadInfoTask(boolean refresh) {
			isRefresh = refresh;
			isLoading = true;
			if (isRefresh) {
				trendId = "";
				createTime = "0";
			}
			if (isRefresh) {
				// showHeaderProgress();
				homePg.setLoadingState(true);
			} else {
				listView.showLoadFooter();
			}
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mcResult = null;
			try {
				if ((GetDbInfoUtil.getMemberId(homePg) + "").equals(homePg.id)) {
					if (searchState) {
						if (isRefresh) {
							searchStart = 1;
						} else {
							searchStart = searchInfos.size() / 20 + 1;
						}
						mcResult = APIRequestServers.searchResource(homePg,
								keyword, "", searchStart, 20, "sg", "4",
								homePg.id);
					} else {
						mcResult = APIRequestServers.myMemberTrends(homePg,
								trendId, ResourceTypeEnum.OBJ_GROUP_ALL, "10",
								true);
					}
				} else {
					mcResult = APIRequestServers.otherMemberTrends(homePg,
							homePg.id, ResourceTypeEnum.OBJ_GROUP_ALL, trendId,
							createTime, "10", true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (isRefresh) {
				// hintHeaderProgress();
				homePg.setLoadingState(false);
			} else {
				listView.showFinishLoadFooter();
			}
			if (null == mcResult) {
				onLoadInfoErr(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					onLoadInfoErr(T.ErrStr);
				} else {
					if (!searchState) {
						showResult(isRefresh, mcResult);
					} else {
						showSearchResult(isRefresh, mcResult);
					}
				}
			}
		}
	}

	private void showSearchResult(boolean isRefresh, MCResult mcResult) {
		ResultAll resultInfos0 = (ResultAll) mcResult.getResult();
		if (null == resultInfos0) {
			// homePg.showTip("没有更多数据！");
		} else {
			List<ResultMessageBean> requestInfos0 = resultInfos0.getRmList();
			if (requestInfos0 == null || 0 == requestInfos0.size()) {
				// homePg.showTip("没有更多数据！");
			} else {
				if (isRefresh || searchStart == 1)
					searchInfos.clear();
				searchInfos.addAll(requestInfos0);
				infoSearchAdapter.notifyDataSetChanged();
				if (isRefresh || searchStart == 1)
					listView.setSelectionAfterHeaderView();
			}
		}

	}

	private void showResult(boolean isRefresh, MCResult mcResult) {
		@SuppressWarnings("unchecked")
		ArrayList<ResourceTrendBean> requestInfos = (ArrayList<ResourceTrendBean>) mcResult
				.getResult();
		if (null == requestInfos || requestInfos.size() == 0) {
			if (infos.size() == 0) {
				onLoadInfoErr(null);
			} else {
				homePg.showTip("最后一页");
			}
		} else {
			if (isRefresh || null == trendId || "".equals(trendId)) {
				infos.clear();
				infos.addAll(requestInfos);
				ResourceTrendBean rBean = infos.get(infos.size() - 1);
				trendId = rBean.getTrendId() + "";
				createTime = DateTimeUtil.getLongTime(rBean.getCreateTime())
						+ "";
				listView.setAdapter(infoAdapter);
			} else {
				infos.addAll(requestInfos);
				ResourceTrendBean rBean = infos.get(infos.size() - 1);
				trendId = rBean.getTrendId() + "";
				createTime = DateTimeUtil.getLongTime(rBean.getCreateTime())
						+ "";
				infoAdapter.notifyDataSetChanged();
			}
			isLoading = false;
		}

	}

	class GreetTask extends AsyncTask<Void, Integer, MCResult> {
		private int greetId;

		public GreetTask(int greetId) {
			homePg.setLoadingState(true);
			// showHeaderProgress();
			this.greetId = greetId;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers
						.greetMamber(homePg, homePg.id, "0", String
								.valueOf(GreetUtil.GREET_CONFIG_ID[greetId]));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			homePg.setLoadingState(false);
			// hintHeaderProgress();
			BaseActivity.spdDialog.cancelProgressDialog(null);
			if (null != mcResult && 1 == mcResult.getResultCode()) {
				homePg.showTip("打招呼成功！");
			} else {
				homePg.showTip(T.ErrStr);
			}

			facePPW.dismiss();
			// faces.setVisibility(View.GONE);
			// faces.startAnimation(AnimationUtils.loadAnimation(
			// homePg, R.anim.push_left_out));
		};
	}

	private void onLoadInfoErr(String text) {
		if (null == text) {
			text = "暂无动态！";
		}
		// list.setVisibility(View.GONE);
		homePg.showTip(text);
	}

	@Override
	public void onLoadMore() {
		if (!isLoading) {
			loadInfo(false);
		}
	}

	@Override
	public void onFragmentRefresh(FragmentActivity fContext) {
		super.onFragmentRefresh(fContext);
		loadInfo(true);
	}

	OnClickListener rightMenu = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (null == homePg.name) {
				return;
			}
			homePg.ppw.dismiss();
			switch ((Integer) (v.getTag())) {
			case 0: // 发私信
				Bundle secret = new Bundle();
				secret.putString("memberId", homePg.id);
				secret.putString("name", homePg.name);
				secret.putString("head", mHeadUrl);
				LogicUtil.enter(homePg, QChatActivity.class, secret, false);
				break;
			case 1: // 拨打
				if (1 == mFriendState) { // 我的朋友可以拨打，陌生人不行
					if (CharUtil.isValidPhone(homePg.number)) {
						new MemberContactUtil(homePg).callPhone(homePg.number);

						new Thread() {
							public void run() {
								FriendListService.getService(homePg)
										.saveContactTime(
												new String[] { homePg.id });
							};
						}.start();
					} else {
						homePg.showTip("该用户未绑定手机号！");
					}
				} else {
					homePg.showTip("该用户还不是您的好友！");
				}
				break;
			case 2: // 留言
				Bundle message = new Bundle();
				message.putString("id", homePg.id);
				message.putInt("typedata", EditActivity.TYPE_MSG);
				message.putInt("type", HomePgActivity.TYPE_MBER);
				LogicUtil.enter(homePg, EditActivity.class, message, false);
				break;
			case 3: // 打招呼
				facePPW.showAtLocation(homePg.layout, Gravity.BOTTOM, 0, 0);
				break;
			case 4:// 向TA引荐朋友
				Bundle bundle = new Bundle();
				bundle.putInt("type", 5);
				bundle.putString("sendInfo", "");
				try {
					bundle.putInt("id", Integer.valueOf(homePg.id));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// LogicUtil.enter(homePg, FriendsChooserActivity.class, bundle,
				// FriendsChooserActivity.RESCODE);
				Intent i1 = new Intent(homePg, FriendsChooserActivity.class);
				i1.putExtras(bundle);
				startActivityForResult(i1, FriendsChooserActivity.RESCODE);
				isIntroduceToOther = false;
				break;
			case 5:// 把TA推荐给朋友
				Bundle bundle2 = new Bundle();
				bundle2.putInt("type", 5);
				bundle2.putString("sendInfo", "");
				try {
					bundle2.putInt("id", Integer.valueOf(homePg.id));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// LogicUtil.enter(homePg, FriendsChooserActivity.class,
				// bundle2,
				// FriendsChooserActivity.RESCODE);
				Intent i2 = new Intent(homePg, FriendsChooserActivity.class);
				i2.putExtras(bundle2);
				startActivityForResult(i2, FriendsChooserActivity.RESCODE);
				isIntroduceToOther = true;
				break;
			case 6:// 解除朋友关系
				deleteFriend();
				break;
			default:
				break;
			}
		}
	};

	void refreshRightMenu(Context c) {
		rightll.removeAllViews();
		homePg.addChild(rightll, getRightMenuItem(c, "拨打", 1, rightMenu));
		homePg.addChild(rightll, getRightMenuItem(c, "发私信", 0, rightMenu));
		homePg.addChild(rightll, getRightMenuItem(c, "打招呼", 3, rightMenu));
		homePg.addChild(rightll, getRightMenuItem(c, "留言    ", 2, rightMenu));
		if (1 == mFriendState) {
			homePg.addChild(rightll,
					getRightMenuItem(c, "向TA引荐朋友", 4, rightMenu));
			homePg.addChild(rightll,
					getRightMenuItem(c, "把TA推荐给朋友", 5, rightMenu));
			homePg.addChild(rightll,
					getRightMenuItem(c, "解除朋友关系", 6, rightMenu));
		}
		rightll.removeViewAt(rightll.getChildCount() - 1);
		rightll.getChildAt(rightll.getChildCount() - 1).setBackgroundResource(
				R.drawable.btn_menu_white_bottom_round);
	}

	public static View getRightMenuItem(Context c, String text, int id,
			OnClickListener listener) {
		TextView t = (TextView) LayoutInflater.from(c).inflate(
				R.layout.item_right, null);
		t.setText(text);
		t.setTag(id);
		t.setOnClickListener(listener);
		return t;
	}

	private void setAlterHead() {
		if (homePg.sType == HomePgActivity.TYPE_MBER) {
			if ((GetDbInfoUtil.getMemberId(homePg) + "").equals(homePg.id)) {
				showUpHeadDialog(HomePgActivity.TYPE_MBER, headimg_file,
						headimg_name);
			}
		} else {
		}

	}

	/**
	 * 显示修改头像对话框
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void showUpHeadDialog(int type, final String head_file,
			final String head_name) {
		AlertDialog.Builder builder = new AlertDialog.Builder(homePg);
		if (type == HomePgActivity.TYPE_MBER) {
			builder.setTitle("修改头像");
		} else {
			builder.setTitle("修改海报");
		}

		String[] dialogMsg = new String[] {
				getResourcesString(homePg,
						R.string.selectImgdialog_fromlocality),
				getResourcesString(homePg, R.string.selectImgdialog_fromcamera)
		// , getResourcesString(c, R.string.selectImgdialog_cancel)
		};
		builder
		// .setItems(dialogMsg, new DialogInterface.OnClickListener() {
		.setAdapter(new ArrayAdapter(homePg, R.layout.choice_item, dialogMsg),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(
									Intent.createChooser(intent, "选择图片"),
									HomePgActivity.FROM_GALLERY);
							break;
						case 1:
							Intent intent1 = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							File saveFile = new File(head_file);
							if (saveFile.exists()) {
								// Log.d(TAG,"目录已存在");
							} else {
								saveFile.mkdirs();
							}
							intent1.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(saveFile, head_name)));
							startActivityForResult(intent1,
									HomePgActivity.FROM_CAMERA);
							break;
						case 2:
							dialog.dismiss();
							break;
						}
					}
				});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	public static String getResourcesString(Context c, int id) {
		return c.getResources().getString(id);
	}

	private void setMood(int friendState, final String moodText) {
		if (0 == friendState) {
			mood.setText(Html.fromHtml(moodText + "<img src='"
					+ R.drawable.icon_edit + "'/>", imageGetter, null));
			mood.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if ((GetDbInfoUtil.getMemberId(homePg) + "")
							.equals(homePg.id)) {
						Intent mIntent = new Intent(homePg, EditActivity.class);
						Bundle b = new Bundle();
						b.putString("ori", moodText);
						b.putInt("typedata", EditActivity.TYPE_MOOD);
						b.putInt("type", HomePgActivity.TYPE_MBER);
						mIntent.putExtras(b);
						startActivityForResult(mIntent,
								HomePgActivity.SEND_MOOD);
					}
				}
			});
		} else {
			mood.setText(moodText);
			mood.setOnClickListener(null);
		}
	}

	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			int id = 0;
			try {
				id = Integer.parseInt(source);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 根据id从资源文件中获取图片对象
			Drawable d = getResources().getDrawable(id);
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			return d;
		}
	};

	String keyword;

	@Override
	public void search(String s) {
		L.d(TAG, "search..." + s);
		keyword = s;
		if (s != null && !"".equals(s)) {
			search();
		} else {
			clear();
		}
	}

	private void search() {
		listView.setAdapter(infoSearchAdapter);
		searchState = true;
		loadInfo(true);
	}

	private void clear() {
		listView.setAdapter(infoAdapter);
		searchState = false;
	}

}